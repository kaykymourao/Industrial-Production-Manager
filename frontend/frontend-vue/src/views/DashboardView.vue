<script setup>
import { onMounted, ref } from "vue";
import { api } from "../services/api";

const data = ref({
  totalProducts: 0,
  totalRawMaterials: 0,
  totalStockQuantitySum: 0,
  totalProductsPriceSum: 0,
  maxProductionValueSuggestion: 0
});

const loading = ref(true);
const error = ref("");

const nf = new Intl.NumberFormat("pt-BR", { maximumFractionDigits: 2 });
const cf = new Intl.NumberFormat("pt-BR", { style: "currency", currency: "BRL" });

function fmtNumber(v) {
  if (v === null || v === undefined) return "-";
  return nf.format(Number(v));
}
function fmtMoney(v) {
  if (v === null || v === undefined) return "-";
  return cf.format(Number(v));
}

async function load() {
  loading.value = true;
  error.value = "";
  try {
    const res = await api.get("/dashboard/summary");
    data.value = res.data;
  } catch (e) {
    error.value =
      e?.response?.data?.error ||
      e?.message ||
      "Erro ao carregar o dashboard.";
  } finally {
    loading.value = false;
  }
}

onMounted(load);
</script>

<template>
  <div class="wrap">
    <div class="top">
      <h1>Dashboard</h1>
      <button @click="load" :disabled="loading">Recarregar</button>
    </div>

    <p v-if="loading">Carregando...</p>
    <p v-if="error" class="error">{{ error }}</p>

    <div v-if="!loading && !error" class="cards">
      <div class="card">
        <div class="label">Total Produtos</div>
        <div class="value">{{ data.totalProducts }}</div>
      </div>

      <div class="card">
        <div class="label">Total Matérias-primas</div>
        <div class="value">{{ data.totalRawMaterials }}</div>
      </div>

      <div class="card">
        <div class="label">Estoque Total</div>
        <div class="value">{{ fmtNumber(data.totalStockQuantitySum) }}</div>
      </div>

      <div class="card">
        <div class="label">Soma Preços Produtos</div>
        <div class="value">{{ fmtMoney(data.totalProductsPriceSum) }}</div>
      </div>

      <div class="card">
        <div class="label">Sugestão Máx.</div>
        <div class="value">{{ fmtMoney(data.maxProductionValueSuggestion) }}</div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.wrap { max-width: 980px; }
.top {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
}
button{
  padding: 8px 10px;
  border: 1px solid #ddd;
  border-radius: 10px;
  cursor: pointer;
  background: #f7f7f7;
}
.cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
  gap: 12px;
  margin-top: 12px;
}
.card {
  border: 1px solid #e6e6e6;
  background: #fff;
  padding: 14px;
  border-radius: 12px;
}
.label { color: #666; font-size: 12px; margin-bottom: 6px; }
.value { font-size: 22px; font-weight: 800; }
.error { color: #c00; margin-top: 10px; }
</style>