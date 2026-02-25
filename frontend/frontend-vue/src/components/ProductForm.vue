<script setup>
import { computed, onMounted, ref, watch } from "vue";
import { api } from "../services/api";

const props = defineProps({
  open: { type: Boolean, default: false },
  mode: { type: String, default: "create" }, // "create" | "edit"
  initial: { type: Object, default: null }, // ProductResponse
});

const emit = defineEmits(["close", "saved"]);

const saving = ref(false);
const error = ref("");
const fieldErrors = ref({});

const rawMaterials = ref([]);
const rmLoading = ref(false);

const form = ref({
  code: "",
  name: "",
  price: "",
  materials: [{ rawMaterialId: "", quantityPerUnit: "" }],
});

const isEdit = computed(() => props.mode === "edit");

function resetForm() {
  form.value = {
    code: props.initial?.code ?? "",
    name: props.initial?.name ?? "",
    price: props.initial?.price?.toString?.() ?? (props.initial?.price ?? ""),
    materials:
      props.initial?.materials?.length
        ? props.initial.materials.map((m) => ({
            rawMaterialId: m.rawMaterialId?.toString?.() ?? "",
            quantityPerUnit: m.quantityPerUnit?.toString?.() ?? "",
          }))
        : [{ rawMaterialId: "", quantityPerUnit: "" }],
  };
  error.value = "";
  fieldErrors.value = {};
}

watch(
  () => [props.open, props.initial, props.mode],
  () => {
    if (props.open) resetForm();
  },
  { deep: true }
);

function normalizeNumber(v) {
  if (v === null || v === undefined) return "";
  return String(v).trim().replace(",", ".");
}

function addMaterial() {
  form.value.materials.push({ rawMaterialId: "", quantityPerUnit: "" });
}

function removeMaterial(idx) {
  if (form.value.materials.length === 1) return;
  form.value.materials.splice(idx, 1);
}

function parseBackendError(e) {
  const data = e?.response?.data;
  if (!data) return { msg: "Erro de rede ou servidor indisponível.", fields: {} };

  // GlobalExceptionHandler pode retornar { error, status, timestamp } ou { fieldErrors: {...} }
  if (data.fieldErrors) {
    return { msg: data.error ?? "Validação falhou.", fields: data.fieldErrors };
  }
  return { msg: data.error ?? "Erro ao salvar.", fields: {} };
}

async function loadRawMaterials() {
  rmLoading.value = true;
  try {
    // tenta paginado (como você já tem no backend)
    const res = await api.get("/raw-materials", { params: { page: 0, size: 200 } });
    rawMaterials.value = res.data?.content ?? res.data ?? [];
  } catch {
    rawMaterials.value = [];
  } finally {
    rmLoading.value = false;
  }
}

onMounted(loadRawMaterials);

async function submit() {
  saving.value = true;
  error.value = "";
  fieldErrors.value = {};

  try {
    // validações básicas no front
    if (!form.value.code?.trim()) throw { response: { data: { error: "Código é obrigatório." } } };
    if (!form.value.name?.trim()) throw { response: { data: { error: "Nome é obrigatório." } } };

    const priceNorm = normalizeNumber(form.value.price);
    if (!priceNorm || Number(priceNorm) <= 0) {
      throw { response: { data: { error: "Preço deve ser maior que 0." } } };
    }

    const materialsClean = (form.value.materials ?? []).map((m) => ({
      rawMaterialId: Number(normalizeNumber(m.rawMaterialId)),
      quantityPerUnit: normalizeNumber(m.quantityPerUnit),
    }));

    if (!materialsClean.length) {
      throw { response: { data: { error: "Informe pelo menos 1 matéria-prima." } } };
    }

    // valida material
    for (const m of materialsClean) {
      if (!m.rawMaterialId || m.rawMaterialId <= 0) {
        throw { response: { data: { error: "Selecione a matéria-prima em todos os itens." } } };
      }
      if (!m.quantityPerUnit || Number(m.quantityPerUnit) <= 0) {
        throw { response: { data: { error: "Quantidade por unidade deve ser maior que 0." } } };
      }
    }

    const payload = {
      code: form.value.code.trim(),
      name: form.value.name.trim(),
      price: priceNorm, // manda string com ponto (BigDecimal friendly)
      materials: materialsClean.map((m) => ({
        rawMaterialId: m.rawMaterialId,
        quantityPerUnit: m.quantityPerUnit,
      })),
    };

    if (isEdit.value) {
      await api.put(`/products/${props.initial.id}`, payload);
    } else {
      await api.post("/products", payload);
    }

    emit("saved");
    emit("close");
  } catch (e) {
    const parsed = parseBackendError(e);
    error.value = parsed.msg;
    fieldErrors.value = parsed.fields;
  } finally {
    saving.value = false;
  }
}

function close() {
  emit("close");
}
</script>

<template>
  <div v-if="open" class="overlay" @click.self="close">
    <div class="modal">
      <div class="header">
        <h2>{{ isEdit ? "Editar Produto" : "Novo Produto" }}</h2>
        <button class="icon" @click="close">✕</button>
      </div>

      <p v-if="error" class="error">{{ error }}</p>

      <div class="grid">
        <div class="field">
          <label>Código</label>
          <input v-model="form.code" placeholder="Ex: P-001" />
          <small v-if="fieldErrors.code" class="error">{{ fieldErrors.code }}</small>
        </div>

        <div class="field">
          <label>Nome</label>
          <input v-model="form.name" placeholder="Ex: Steel Chair" />
          <small v-if="fieldErrors.name" class="error">{{ fieldErrors.name }}</small>
        </div>

        <div class="field">
          <label>Preço</label>
          <input v-model="form.price" placeholder="Ex: 199.90" />
          <small v-if="fieldErrors.price" class="error">{{ fieldErrors.price }}</small>
        </div>
      </div>

      <div class="section">
        <div class="sectionHeader">
          <h3>Materiais</h3>
          <button @click="addMaterial" :disabled="saving">+ Adicionar</button>
        </div>

        <div v-if="rmLoading" class="muted">Carregando matérias-primas...</div>

        <div v-for="(m, idx) in form.materials" :key="idx" class="row">
          <select v-model="m.rawMaterialId">
            <option value="">Selecione</option>
            <option v-for="rm in rawMaterials" :key="rm.id" :value="rm.id">
              {{ rm.code }} — {{ rm.name }}
            </option>
          </select>

          <input v-model="m.quantityPerUnit" placeholder="Qtd por unidade (ex: 2.5)" />

          <button class="danger" @click="removeMaterial(idx)" :disabled="saving">Remover</button>
        </div>

        <small v-if="fieldErrors.materials" class="error">{{ fieldErrors.materials }}</small>
      </div>

      <div class="actions">
        <button @click="close" :disabled="saving">Cancelar</button>
        <button class="primary" @click="submit" :disabled="saving">
          {{ saving ? "Salvando..." : (isEdit ? "Salvar" : "Criar") }}
        </button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.overlay{
  position: fixed; inset: 0; background: rgba(0,0,0,.35);
  display:flex; align-items:center; justify-content:center; padding: 16px;
}
.modal{
  background:#fff; width: min(820px, 100%); border-radius: 10px;
  padding: 14px; box-shadow: 0 8px 24px rgba(0,0,0,.18);
}
.header{ display:flex; align-items:center; justify-content:space-between; gap: 10px; }
.icon{ border:0; background:transparent; font-size:18px; cursor:pointer; }
.grid{
  display:grid; grid-template-columns: 1fr 2fr 1fr; gap: 10px; margin-top: 10px;
}
.field label{ display:block; font-size: 12px; margin-bottom: 4px; color:#444; }
.field input, select{
  width:100%; padding: 8px; border:1px solid #ddd; border-radius: 8px;
}
.section{ margin-top: 14px; }
.sectionHeader{ display:flex; align-items:center; justify-content:space-between; }
.row{
  display:grid; grid-template-columns: 2fr 1fr auto; gap: 8px;
  margin-top: 8px; align-items:center;
}
.actions{
  display:flex; justify-content:flex-end; gap: 8px; margin-top: 14px;
}
button{
  padding: 8px 10px; border: 1px solid #ddd; border-radius: 8px; cursor:pointer;
  background:#f7f7f7;
}
button.primary{ background:#111; color:#fff; border-color:#111; }
button.danger{ background:#fff0f0; border-color:#ffd0d0; }
.error{ color:#c00; margin: 8px 0; }
.muted{ color:#666; font-size: 13px; margin-top: 8px; }
@media (max-width: 720px){
  .grid{ grid-template-columns: 1fr; }
  .row{ grid-template-columns: 1fr; }
}
</style>