<template>
  <div>
    <div class="header">
      <h1>Dashboard</h1>
      <button class="btn-primary" @click="load">Recarregar</button>
    </div>

    <div v-if="loading" class="card p">Carregando...</div>

    <div v-else class="grid">
      <div class="card p">
        <div class="label">Total Produtos</div>
        <div class="value">{{ data.totalProducts }}</div>
      </div>

      <div class="card p">
        <div class="label">Total Matérias-primas</div>
        <div class="value">{{ data.totalRawMaterials }}</div>
      </div>

      <div class="card p">
        <div class="label">Estoque Total</div>
        <div class="value">{{ fmtNumber(data.totalStockQuantitySum) }}</div>
      </div>

      <div class="card p">
        <div class="label">Soma Preços Produtos</div>
        <div class="value">{{ fmtMoney(data.totalProductsPriceSum) }}</div>
      </div>

      <div class="card p">
        <div class="label">Sugestão Máx.</div>
        <div class="value">{{ fmtMoney(data.maxProductionValueSuggestion) }}</div>
      </div>
    </div>
  </div>
</template>

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

async function load() {
  loading.value = true;
  const res = await api.get("/dashboard/summary");
  data.value = res.data;
  loading.value = false;
}
onMounted(load);

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
</script>

<style scoped>
.header{
  display:flex;
  align-items:center;
  justify-content: space-between;
  gap: 12px;
  margin-bottom: 12px;
}

.grid{
  display:grid;
  grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
  gap: 12px;
}

.p{ padding: 14px; }
.label{ color: var(--muted); font-size: 13px; text-transform: uppercase; letter-spacing: .06em; }
.value{ font-size: 26px; font-weight: 800; margin-top: 6px; }
</style>