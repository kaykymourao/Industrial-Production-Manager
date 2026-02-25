<template>
  <div class="app">
    <aside class="sidebar">
      <h2>Industrial PM</h2>
      <p class="sub">Industrial Production Manager</p>
      <nav>
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
.app {
  display: grid;
  grid-template-columns: 240px 1fr;
  min-height: 100vh;
  font-family: Arial, sans-serif;
}

.sidebar {
  padding: 16px;
  border-right: 1px solid #e6e6e6;
  background: #fff;
  position: sticky;
  top: 0;
  height: 100vh;
}

.sidebar h2 {
  margin: 0 0 4px;
  font-size: 18px;
}

.sidebar .sub {
  margin: 0 0 14px;
  font-size: 12px;
  color: #666;
}

nav {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

a {
  text-decoration: none;
  padding: 10px 12px;
  border-radius: 10px;
  color: #111;
  border: 1px solid transparent;
}

a:hover {
  border-color: #e6e6e6;
  background: #fafafa;
}

a.router-link-active {
  background: #111;
  color: #fff;
  font-weight: 700;
}

.content {
  padding: 18px;
  background: #fafafa;
}

/* Mobile */
@media (max-width: 820px) {
  .app {
    grid-template-columns: 1fr;
    grid-template-rows: auto 1fr;
  }

  .sidebar {
    height: auto;
    position: static;
    border-right: none;
    border-bottom: 1px solid #e6e6e6;
  }

  nav {
    flex-direction: row;
    flex-wrap: wrap;
    gap: 8px;
  }
}
</style>