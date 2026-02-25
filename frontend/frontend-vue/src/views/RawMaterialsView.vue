<script setup>
import { onMounted, ref } from "vue";
import { api } from "../services/api";
import RawMaterialForm from "../components/RawMaterialForm.vue";

const loading = ref(false);
const error = ref("");

const raws = ref([]);
const page = ref(0);
const size = ref(10);
const totalPages = ref(0);
const totalElements = ref(0);

const searchCode = ref("");

const modalOpen = ref(false);
const modalMode = ref("create");
const editing = ref(null);

function parseBackendError(e) {
  const data = e?.response?.data;
  if (!data) return "Erro de rede ou servidor indisponível.";
  return data.error ?? "Erro inesperado.";
}

async function load() {
  loading.value = true;
  error.value = "";

  try {
    const params = { page: page.value, size: size.value };
    if (searchCode.value?.trim()) params.code = searchCode.value.trim();

    const res = await api.get("/raw-materials", { params });

    raws.value = res.data?.content ?? [];
    totalPages.value = res.data?.totalPages ?? 1;
    totalElements.value = res.data?.totalElements ?? raws.value.length;
  } catch (e) {
    error.value = parseBackendError(e);
    raws.value = [];
    totalPages.value = 0;
    totalElements.value = 0;
  } finally {
    loading.value = false;
  }
}

function openCreate() {
  modalMode.value = "create";
  editing.value = null;
  modalOpen.value = true;
}

function openEdit(rm) {
  modalMode.value = "edit";
  editing.value = rm;
  modalOpen.value = true;
}

async function removeRaw(rm) {
  const ok = confirm(`Deletar a matéria-prima "${rm.code} - ${rm.name}"?`);
  if (!ok) return;

  loading.value = true;
  error.value = "";

  try {
    await api.delete(`/raw-materials/${rm.id}`);
    if (raws.value.length === 1 && page.value > 0) page.value -= 1;
    await load();
  } catch (e) {
    // aqui pega 409 "in use" também
    error.value = parseBackendError(e);
  } finally {
    loading.value = false;
  }
}

function applySearch() {
  page.value = 0;
  load();
}

function nextPage() {
  if (page.value + 1 >= totalPages.value) return;
  page.value += 1;
  load();
}
function prevPage() {
  if (page.value <= 0) return;
  page.value -= 1;
  load();
}

onMounted(load);
</script>

<template>
  <div class="wrap">
    <div class="top">
      <h1>Matérias-primas</h1>

      <div class="actions">
        <input
          v-model="searchCode"
          placeholder="Buscar por código (ex: RM-001)"
          @keyup.enter="applySearch"
        />
        <button @click="applySearch" :disabled="loading">Buscar</button>
        <button @click="load" :disabled="loading">Recarregar</button>
        <button class="primary" @click="openCreate" :disabled="loading">+ Nova</button>
      </div>
    </div>

    <p v-if="error" class="error">{{ error }}</p>
    <p v-if="loading">Carregando...</p>

    <table v-else class="tbl">
      <thead>
        <tr>
          <th>ID</th>
          <th>Código</th>
          <th>Nome</th>
          <th>Estoque</th>
          <th>Unidade</th>
          <th style="width: 160px;">Ações</th>
        </tr>
      </thead>

      <tbody>
        <tr v-for="r in raws" :key="r.id">
          <td>{{ r.id }}</td>
          <td>{{ r.code }}</td>
          <td>{{ r.name }}</td>
          <td>{{ r.stockQuantity }}</td>
          <td>{{ r.unit }}</td>
          <td class="btns">
            <button @click="openEdit(r)" :disabled="loading">Editar</button>
            <button class="danger" @click="removeRaw(r)" :disabled="loading">Excluir</button>
          </td>
        </tr>

        <tr v-if="raws.length === 0">
          <td colspan="6" class="muted">Nenhuma matéria-prima encontrada.</td>
        </tr>
      </tbody>
    </table>

    <div class="pager" v-if="totalPages > 0">
      <button @click="prevPage" :disabled="loading || page === 0">◀</button>
      <span>Página {{ page + 1 }} de {{ totalPages }} ({{ totalElements }} itens)</span>
      <button @click="nextPage" :disabled="loading || page + 1 >= totalPages">▶</button>

      <select v-model.number="size" @change="applySearch" :disabled="loading">
        <option :value="5">5</option>
        <option :value="10">10</option>
        <option :value="20">20</option>
      </select>
    </div>

    <RawMaterialForm
      :open="modalOpen"
      :mode="modalMode"
      :initial="editing"
      @close="modalOpen = false"
      @saved="load"
    />
  </div>
</template>

<style scoped>
.wrap { max-width: 980px; }
.top { display:flex; align-items:flex-end; justify-content:space-between; gap: 12px; flex-wrap: wrap; }
.actions { display:flex; gap: 8px; align-items:center; flex-wrap: wrap; }
.actions input{
  padding: 8px; border: 1px solid #ddd; border-radius: 8px; min-width: 220px;
}
button{
  padding: 8px 10px; border: 1px solid #ddd; border-radius: 8px; cursor:pointer;
  background:#f7f7f7;
}
button.primary{ background:#111; color:#fff; border-color:#111; }
button.danger{ background:#fff0f0; border-color:#ffd0d0; }
.tbl { width: 100%; border-collapse: collapse; margin-top: 12px; }
th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
.btns { display:flex; gap: 6px; }
.pager{
  display:flex; align-items:center; gap: 10px; margin-top: 12px;
}
.pager select{ padding: 7px; border: 1px solid #ddd; border-radius: 8px; }
.error{ color:#c00; margin: 8px 0; }
.muted{ color:#666; text-align:center; padding: 16px; }
</style>