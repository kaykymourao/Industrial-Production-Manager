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

const nf = new Intl.NumberFormat("pt-BR", { maximumFractionDigits: 4 });

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
  <div class="container">
    <div class="header">
      <div>
        <h1>Matérias-primas</h1>
        <p class="muted">Controle o estoque e evite excluir itens que estão vinculados a produtos.</p>
      </div>

      <div class="actions">
        <input
          v-model="searchCode"
          placeholder="Buscar por código (ex: RM-001)"
          @keyup.enter="applySearch"
        />
        <button class="btn" @click="applySearch" :disabled="loading">Buscar</button>
        <button class="btn" @click="load" :disabled="loading">Recarregar</button>
        <button class="btn primary" @click="openCreate" :disabled="loading">+ Nova</button>
      </div>
    </div>

    <p v-if="error" class="error">{{ error }}</p>

    <div class="card section">
      <div class="sectionTitle">
        <h2>Lista</h2>
        <span class="muted">{{ totalElements }} item(ns)</span>
      </div>

      <div v-if="loading" class="muted" style="padding: 12px;">Carregando...</div>

      <div v-else-if="raws.length === 0" class="empty">
        <div class="emptyTitle">Nenhuma matéria-prima encontrada</div>
        <div class="muted">Clique em <b>+ Nova</b> para cadastrar uma matéria-prima.</div>
      </div>

      <table v-else class="table">
        <thead>
          <tr>
            <th style="width: 90px;">ID</th>
            <th style="width: 160px;">Código</th>
            <th>Nome</th>
            <th style="width: 180px;">Estoque</th>
            <th style="width: 140px;">Unidade</th>
            <th style="width: 190px;">Ações</th>
          </tr>
        </thead>

        <tbody>
          <tr v-for="r in raws" :key="r.id">
            <td class="strong">{{ r.id }}</td>
            <td>
              <span class="pill">{{ r.code }}</span>
            </td>
            <td>
              <div class="strong">{{ r.name }}</div>
            </td>
            <td class="strong">
              {{ nf.format(Number(r.stockQuantity || 0)) }}
            </td>
            <td>
              <span class="unit">{{ r.unit }}</span>
            </td>
            <td class="btns">
              <button class="btn" @click="openEdit(r)" :disabled="loading">Editar</button>
              <button class="btn danger" @click="removeRaw(r)" :disabled="loading">Excluir</button>
            </td>
          </tr>
        </tbody>
      </table>

      <div class="pager" v-if="totalPages > 0">
        <button class="btn" @click="prevPage" :disabled="loading || page === 0">◀</button>

        <span class="muted">
          Página <b>{{ page + 1 }}</b> de <b>{{ totalPages }}</b>
          <span v-if="totalElements"> — {{ totalElements }} itens</span>
        </span>

        <button class="btn" @click="nextPage" :disabled="loading || page + 1 >= totalPages">▶</button>

        <select class="select" v-model.number="size" @change="applySearch" :disabled="loading">
          <option :value="5">5</option>
          <option :value="10">10</option>
          <option :value="20">20</option>
        </select>
      </div>
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
.header{
  display:flex;
  align-items:flex-start;
  justify-content:space-between;
  gap: 16px;
  margin-bottom: 14px;
  flex-wrap: wrap;
}
.actions{
  display:flex;
  gap: 10px;
  align-items:center;
  flex-wrap: wrap;
}
.actions input{
  padding: 10px 12px;
  border: 1px solid var(--border);
  border-radius: 12px;
  min-width: 280px;
  background: #fff;
}

.section{ padding: 14px; }
.sectionTitle{
  display:flex;
  align-items: baseline;
  justify-content: space-between;
  gap: 12px;
  margin-bottom: 10px;
}

.btns{ display:flex; gap: 8px; }

.pager{
  display:flex;
  align-items:center;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 14px;
  flex-wrap: wrap;
}

.select{
  padding: 10px 12px;
  border: 1px solid var(--border);
  border-radius: 12px;
  background: #fff;
}

.pill{
  display:inline-flex;
  padding: 6px 10px;
  border-radius: 999px;
  border: 1px solid var(--border);
  background: #fff;
  font-weight: 800;
  font-size: 12px;
}

.unit{
  display:inline-flex;
  padding: 6px 10px;
  border-radius: 10px;
  border: 1px solid var(--border);
  background: #fff;
  font-weight: 800;
  font-size: 12px;
}

.strong{ font-weight: 800; }
.muted{ color: var(--muted); }
.error{ color:#b91c1c; font-weight: 800; margin: 8px 0 12px; }

.empty{
  padding: 16px;
  border: 1px dashed var(--border);
  border-radius: 14px;
  background: #fff;
}
.emptyTitle{ font-weight: 900; margin-bottom: 6px; }
</style>