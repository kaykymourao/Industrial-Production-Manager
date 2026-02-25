<script setup>
import { computed, onMounted, ref, watch } from "vue";
import { api } from "../services/api";

const props = defineProps({
  open: { type: Boolean, default: false },
  mode: { type: String, default: "create" }, // "create" | "edit"
  initial: { type: Object, default: null }, // RawMaterialResponse
});

const emit = defineEmits(["close", "saved"]);

const saving = ref(false);
const error = ref("");
const fieldErrors = ref({});

const form = ref({
  code: "",
  name: "",
  stockQuantity: "",
  unit: "UNIT",
});

const isEdit = computed(() => props.mode === "edit");

function resetForm() {
  form.value = {
    code: props.initial?.code ?? "",
    name: props.initial?.name ?? "",
    stockQuantity:
      props.initial?.stockQuantity?.toString?.() ?? (props.initial?.stockQuantity ?? ""),
    unit: props.initial?.unit ?? "UNIT",
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

function parseBackendError(e) {
  const data = e?.response?.data;
  if (!data) return { msg: "Erro de rede ou servidor indisponível.", fields: {} };

  if (data.fieldErrors) {
    return { msg: data.error ?? "Validação falhou.", fields: data.fieldErrors };
  }
  return { msg: data.error ?? "Erro ao salvar.", fields: {} };
}

async function submit() {
  saving.value = true;
  error.value = "";
  fieldErrors.value = {};

  try {
    if (!form.value.code?.trim()) throw { response: { data: { error: "Código é obrigatório." } } };
    if (!form.value.name?.trim()) throw { response: { data: { error: "Nome é obrigatório." } } };

    const stockNorm = normalizeNumber(form.value.stockQuantity);
    if (stockNorm === "" || Number(stockNorm) < 0) {
      throw { response: { data: { error: "Estoque deve ser 0 ou maior." } } };
    }

    const payload = {
      code: form.value.code.trim(),
      name: form.value.name.trim(),
      stockQuantity: stockNorm, // BigDecimal-friendly
      unit: form.value.unit.trim(),
    };

    if (isEdit.value) {
      await api.put(`/raw-materials/${props.initial.id}`, payload);
    } else {
      await api.post("/raw-materials", payload);
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

onMounted(() => {});
</script>

<template>
  <div v-if="open" class="overlay" @click.self="close">
    <div class="modal">
      <div class="header">
        <h2>{{ isEdit ? "Editar Matéria-prima" : "Nova Matéria-prima" }}</h2>
        <button class="icon" @click="close">✕</button>
      </div>

      <p v-if="error" class="error">{{ error }}</p>

      <div class="grid">
        <div class="field">
          <label>Código</label>
          <input v-model="form.code" placeholder="Ex: RM-001" />
          <small v-if="fieldErrors.code" class="error">{{ fieldErrors.code }}</small>
        </div>

        <div class="field">
          <label>Nome</label>
          <input v-model="form.name" placeholder="Ex: Steel" />
          <small v-if="fieldErrors.name" class="error">{{ fieldErrors.name }}</small>
        </div>

        <div class="field">
          <label>Estoque</label>
          <input v-model="form.stockQuantity" placeholder="Ex: 100" />
          <small v-if="fieldErrors.stockQuantity" class="error">{{ fieldErrors.stockQuantity }}</small>
        </div>

        <div class="field">
          <label>Unidade</label>
          <select v-model="form.unit">
            <option value="UNIT">UNIT</option>
            <option value="KG">KG</option>
            <option value="G">G</option>
            <option value="L">L</option>
            <option value="ML">ML</option>
          </select>
          <small v-if="fieldErrors.unit" class="error">{{ fieldErrors.unit }}</small>
        </div>
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
  background:#fff; width: min(780px, 100%); border-radius: 10px;
  padding: 14px; box-shadow: 0 8px 24px rgba(0,0,0,.18);
}
.header{ display:flex; align-items:center; justify-content:space-between; gap: 10px; }
.icon{ border:0; background:transparent; font-size:18px; cursor:pointer; }
.grid{
  display:grid; grid-template-columns: 1fr 2fr 1fr 1fr; gap: 10px; margin-top: 10px;
}
.field label{ display:block; font-size: 12px; margin-bottom: 4px; color:#444; }
.field input, select{
  width:100%; padding: 8px; border:1px solid #ddd; border-radius: 8px;
}
.actions{
  display:flex; justify-content:flex-end; gap: 8px; margin-top: 14px;
}
button{
  padding: 8px 10px; border: 1px solid #ddd; border-radius: 8px; cursor:pointer;
  background:#f7f7f7;
}
button.primary{ background:#111; color:#fff; border-color:#111; }
.error{ color:#c00; margin: 8px 0; }
@media (max-width: 760px){
  .grid{ grid-template-columns: 1fr; }
}
</style>