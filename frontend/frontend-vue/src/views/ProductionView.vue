<script setup>
import { ref } from "vue";
import { api } from "../services/api";

const loading = ref(false);
const error = ref("");

const result = ref({
  applied: false,
  totalUnits: 0,
  totalValue: 0,
  items: [],
  consumptions: [],
  stockSnapshot: []
});

function setResult(data) {
  result.value = {
    applied: data?.applied ?? false,
    totalUnits: data?.totalUnits ?? 0,
    totalValue: data?.totalValue ?? 0,
    items: data?.items ?? [],
    consumptions: data?.consumptions ?? [],
    stockSnapshot: data?.stockSnapshot ?? []
  };
}

async function suggest() {
  console.log("CLICK suggest");
  loading.value = true;
  error.value = "";
  try {
    const { data } = await api.post("/production/suggest");
    console.log("SUGGEST OK:", data);
    setResult(data);
  } catch (e) {
    console.log("SUGGEST ERROR:", e);
    error.value =
      e?.response?.data?.error ||
      JSON.stringify(e?.response?.data) ||
      e?.message ||
      "Erro ao buscar sugestão.";
  } finally {
    loading.value = false;
  }
}

async function apply() {
  console.log("CLICK apply");
  loading.value = true;
  error.value = "";
  try {
    const { data } = await api.post("/production/apply");
    console.log("APPLY OK:", data);
    setResult(data);
  } catch (e) {
    console.log("APPLY ERROR:", e);
    error.value =
      e?.response?.data?.error ||
      JSON.stringify(e?.response?.data) ||
      e?.message ||
      "Erro ao aplicar plano.";
  } finally {
    loading.value = false;
  }
}
</script>

<template>
  <h1>Produção</h1>

  <div style="display:flex; gap: 8px; margin: 12px 0;">
    <button @click="suggest" :disabled="loading">Suggest</button>
    <button @click="apply" :disabled="loading">Apply</button>
  </div>

  <p v-if="loading">Carregando...</p>
  <p v-if="error" style="color:red">{{ error }}</p>

  <div v-if="!loading">
    <p><strong>Applied:</strong> {{ result.applied }}</p>
    <p><strong>Total Units:</strong> {{ result.totalUnits }}</p>
    <p><strong>Total Value:</strong> {{ result.totalValue }}</p>

    <h3>Itens</h3>
    <p v-if="result.items.length === 0">Sem itens (faça Suggest).</p>
    <ul v-else>
      <li v-for="i in result.items" :key="i.productId">
        <strong>{{ i.productCode }}</strong> — {{ i.quantityToProduce }} un —
        Unit: {{ i.unitPrice }} — Total: {{ i.totalValue }}
      </li>
    </ul>

    <h3>Consumo</h3>
    <p v-if="result.consumptions.length === 0">Sem consumo.</p>
    <ul v-else>
      <li v-for="c in result.consumptions" :key="c.rawMaterialId">
        <strong>{{ c.rawMaterialCode }}</strong> — consumiu {{ c.consumedQuantity }}
        {{ c.unit }}
      </li>
    </ul>

    <h3>Snapshot do Estoque</h3>
    <p v-if="result.stockSnapshot.length === 0">Sem snapshot.</p>
    <ul v-else>
      <li v-for="s in result.stockSnapshot" :key="s.rawMaterialId">
        <strong>{{ s.rawMaterialCode }}</strong> — {{ s.before }} → {{ s.after }}
        ({{ s.unit }})
      </li>
    </ul>

    <details style="margin-top: 12px;">
      <summary>Ver JSON completo</summary>
      <pre>{{ result }}</pre>
    </details>
  </div>
</template>