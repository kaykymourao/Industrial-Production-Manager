<template>
  <div class="layout">
    <aside class="sidebar card">
      <div class="brand">
        <div class="logo">🏭</div>
        <div>
          <div class="title">Industrial PM</div>
          <div class="subtitle">Production Manager</div>
        </div>
      </div>

      <nav class="menu">
        <RouterLink to="/">Dashboard</RouterLink>
        <RouterLink to="/products">Produtos</RouterLink>
        <RouterLink to="/raw-materials">Matérias-primas</RouterLink>
        <RouterLink to="/production">Produção</RouterLink>
      </nav>
    </aside>

    <main class="content">
      <RouterView />
    </main>
  </div>
</template>

<style scoped>
.layout{
  display: grid;
  grid-template-columns: 280px 1fr;
  gap: 16px;
  padding: 16px;
  min-height: 100vh;
}

.sidebar{
  padding: 16px;
  position: sticky;
  top: 16px;
  height: calc(100vh - 32px);
  display: flex;
  flex-direction: column;
  gap: 18px;
}

.brand{
  display:flex;
  gap: 12px;
  align-items:center;
  padding: 10px;
  border-radius: 12px;
  background: #f8fafc;
  border: 1px solid var(--border);
}

.logo{
  width: 44px;
  height: 44px;
  display:flex;
  align-items:center;
  justify-content:center;
  border-radius: 12px;
  background: #eef2ff;
  border: 1px solid #e0e7ff;
  font-size: 20px;
}

.title{ font-weight: 800; font-size: 16px; }
.subtitle{ font-size: 12px; color: var(--muted); margin-top: 2px; }

.menu{
  display:flex;
  flex-direction: column;
  gap: 8px;
}

a{
  text-decoration: none;
  padding: 12px 12px;
  border-radius: 12px;
  color: var(--text);
  border: 1px solid transparent;
  font-weight: 650;
}

a:hover{
  background: #f8fafc;
  border-color: var(--border);
}

a.router-link-active{
  background: #eef2ff;
  border-color: #e0e7ff;
  color: #1d4ed8;
}

.content{
  padding: 18px;
}

@media (max-width: 900px){
  .layout{ grid-template-columns: 1fr; }
  .sidebar{
    position: static;
    height: auto;
  }
}
</style>