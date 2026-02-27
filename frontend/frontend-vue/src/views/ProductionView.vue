<script setup>
import { computed, ref } from "vue";
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

function parseBackendError(e) {
  const data = e?.response?.data;
  if (!data) return "Erro de rede ou servidor indisponível.";
  return data.error ?? "Erro inesperado.";
}

const money = new Intl.NumberFormat("pt-BR", { style: "currency", currency: "BRL" });
const number = new Intl.NumberFormat("pt-BR", { maximumFractionDigits: 4 });

const hasPlan = computed(() => (result.value.items?.length ?? 0) > 0);
const canApply = computed(() => hasPlan.value && !loading.value);

async function suggest() {
  loading.value = true;
  error.value = "";
  try {
    const { data } = await api.post("/production/suggest");
    setResult(data);
  } catch (e) {
    error.value = parseBackendError(e);
  } finally {
    loading.value = false;
  }
}

async function apply() {
  loading.value = true;
  error.value = "";
  try {
    const { data } = await api.post("/production/apply");
    setResult(data);
  } catch (e) {
    error.value = parseBackendError(e);
  } finally {
    loading.value = false;
  }
}
</script>

<template>
  <div class="container">
    <div class="header">
      <div>
        <h1>Produção</h1>
        <p class="muted">
          Gere uma sugestão com base no estoque atual e aplique para baixar o estoque.
        </p>
      </div>

      <div class="headerActions">
        <button class="btn" @click="suggest" :disabled="loading">
          {{ loading ? "Gerando..." : "Suggest" }}
        </button>
        <button class="btn primary" @click="apply" :disabled="!canApply">
          Apply
        </button>
      </div>
    </div>

    <p v-if="error" class="error">{{ error }}</p>

    <div class="grid">
      <div class="card stat">
        <div class="statLabel">Status</div>
        <div class="statValue">
          <span class="badge" :class="result.applied ? 'ok' : 'no'">
            {{ result.applied ? "Aplicado" : "Não aplicado" }}
          </span>
        </div>
      </div>

      <div class="card stat">
        <div class="statLabel">Total de unidades</div>
        <div class="statValue">{{ number.format(result.totalUnits || 0) }}</div>
      </div>

      <div class="card stat">
        <div class="statLabel">Valor total</div>
        <div class="statValue">{{ money.format(Number(result.totalValue || 0)) }}</div>
      </div>
    </div>

    <div class="card section">
      <div class="sectionTitle">
        <h2>Itens sugeridos</h2>
        <span class="muted">{{ result.items.length }} item(ns)</span>
      </div>

      <div v-if="loading" class="muted" style="padding:12px;">Carregando...</div>

      <div v-else-if="result.items.length === 0" class="empty">
        <div class="emptyTitle">Sem itens para sugerir</div>
        <div class="muted">
          Isso geralmente acontece quando o estoque está <b>0</b> ou não é suficiente para produzir.
          Reabasteça matérias-primas e clique em <b>Suggest</b>.
        </div>
      </div>

      <table v-else class="table">
        <thead>
          <tr>
            <th>Produto</th>
            <th>Qtd</th>
            <th>Unit</th>
            <th>Total</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="i in result.items" :key="i.productId">
            <td>
              <div class="strong">{{ i.productCode }}</div>
              <div class="muted">{{ i.productName }}</div>
            </td>
            <td class="strong">{{ number.format(i.quantityToProduce) }}</td>
            <td>{{ money.format(Number(i.unitPrice || 0)) }}</td>
            <td class="strong">{{ money.format(Number(i.totalValue || 0)) }}</td>
          </tr>
        </tbody>
      </table>
    </div>

    <div class="split">
      <div class="card section">
        <div class="sectionTitle">
          <h2>Consumo</h2>
          <span class="muted">{{ result.consumptions.length }} item(ns)</span>
        </div>

        <div v-if="result.consumptions.length === 0" class="emptySmall muted">
          Sem consumo (só aparece quando você aplica).
        </div>

        <table v-else class="table">
          <thead>
            <tr>
              <th>Matéria-prima</th>
              <th>Consumido</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="c in result.consumptions" :key="c.rawMaterialId">
              <td>
                <div class="strong">{{ c.rawMaterialCode }}</div>
                <div class="muted">{{ c.rawMaterialName }}</div>
              </td>
              <td class="strong">
                {{ number.format(Number(c.consumedQuantity || 0)) }} {{ c.unit }}
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <div class="card section">
        <div class="sectionTitle">
          <h2>Snapshot do estoque</h2>
          <span class="muted">{{ result.stockSnapshot.length }} item(ns)</span>
        </div>

        <div v-if="result.stockSnapshot.length === 0" class="emptySmall muted">
          Sem snapshot (só aparece quando você aplica).
        </div>

        <table v-else class="table">
          <thead>
            <tr>
              <th>Matéria-prima</th>
              <th>Antes</th>
              <th>Depois</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="s in result.stockSnapshot" :key="s.rawMaterialId">
              <td>
                <div class="strong">{{ s.rawMaterialCode }}</div>
                <div class="muted">{{ s.rawMaterialName }}</div>
              </td>
              <td>{{ number.format(Number(s.before || 0)) }} {{ s.unit }}</td>
              <td class="strong">{{ number.format(Number(s.after || 0)) }} {{ s.unit }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <details class="card section" style="margin-top:16px;">
      <summary class="summary">Ver JSON completo</summary>
      <pre class="json">{{ result }}</pre>
    </details>
  </div>
</template>

<style scoped>
.header{
  display:flex;
  align-items:flex-start;
  justify-content:space-between;
  gap: 16px;
  margin-bottom: 14px;
}
.headerActions{ display:flex; gap: 10px; }

.muted{ color: var(--muted); }
.error{ color:#b91c1c; font-weight: 700; margin: 8px 0 12px; }

.grid{
  display:grid;
  grid-template-columns: repeat(3, minmax(220px, 1fr));
  gap: 12px;
  margin: 14px 0;
}
.stat{ padding: 14px; }
.statLabel{ color: var(--muted); font-weight: 700; font-size: 13px; text-transform: uppercase; letter-spacing: .04em; }
.statValue{ margin-top: 8px; font-size: 20px; font-weight: 800; }

.section{ padding: 14px; margin-top: 12px; }
.sectionTitle{
  display:flex;
  align-items: baseline;
  justify-content: space-between;
  gap: 12px;
  margin-bottom: 10px;
}
.strong{ font-weight: 800; }

.empty{
  padding: 16px;
  border: 1px dashed var(--border);
  border-radius: 14px;
  background: #fff;
}
.emptyTitle{ font-weight: 900; margin-bottom: 6px; }

.emptySmall{ padding: 10px 0; }

.split{
  display:grid;
  grid-template-columns: 1fr 1fr;
  gap: 12px;
  margin-top: 12px;
}

.summary{
  cursor: pointer;
  font-weight: 800;
  padding: 14px;
}
.json{
  padding: 14px;
  margin: 0;
  overflow: auto;
  border-top: 1px solid var(--border);
}

@media (max-width: 980px){
  .grid{ grid-template-columns: 1fr; }
  .split{ grid-template-columns: 1fr; }
  .header{ flex-direction: column; }
  .headerActions{ width: 100%; }
}
</style>