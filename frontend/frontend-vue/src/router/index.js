import { createRouter, createWebHistory } from "vue-router";
import DashboardView from "../views/DashboardView.vue";
import ProductsView from "../views/ProductsView.vue";
import RawMaterialsView from "../views/RawMaterialsView.vue";
import ProductionView from "../views/ProductionView.vue";

export const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: "/", name: "dashboard", component: DashboardView },
    { path: "/products", name: "products", component: ProductsView },
    { path: "/raw-materials", name: "rawMaterials", component: RawMaterialsView },
    { path: "/production", name: "production", component: ProductionView },
  ],
});