# 🏭 Industrial Production Manager (Industrial PM)

Aplicação Full Stack para gerenciamento de produção industrial, permitindo o controle de matérias-primas, cadastro de produtos e otimização automática do plano de produção com base no estoque disponível.

O sistema calcula automaticamente qual a melhor combinação de produtos a serem fabricados para maximizar o valor total de venda, respeitando as restrições de estoque.

---

# 🚀 Tecnologias Utilizadas

## 🔹 Back-end
- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Validation
- H2 Database
- Gradle
- Swagger / OpenAPI

## 🔹 Front-end
- Vue 3
- Vite
- Axios
- Vue Router

---

# 🧠 Conceito do Sistema

O sistema resolve o seguinte problema:

> Dado um conjunto de matérias-primas disponíveis em estoque e produtos que utilizam essas matérias-primas em quantidades específicas, qual é o melhor plano de produção que gera o maior valor total possível?

O algoritmo:

1. Ordena os produtos por maior preço.
2. Calcula quantas unidades podem ser produzidas com o estoque atual.
3. Consome virtualmente o estoque disponível.
4. Gera:
   - Itens produzidos
   - Consumo por matéria-prima
   - Snapshot de estoque (before/after)
   - Valor total máximo possível

---

# 📦 Funcionalidades

## 📊 Dashboard
- Total de produtos cadastrados
- Total de matérias-primas
- Soma total de estoque
- Soma total do preço dos produtos
- Valor máximo de produção sugerido

---

## 🧱 Matérias-Primas
- Criar
- Editar
- Listar com paginação
- Buscar por código
- Excluir
- 🔒 Bloqueio de exclusão se estiver em uso (HTTP 409)

---

## 🛠️ Produtos
- Criar produto com composição de matérias-primas
- Editar produto
- Listar com paginação
- Buscar por código
- Excluir

---

## 🏭 Produção

### 🔍 Suggest
Calcula o plano ideal de produção com base no estoque atual.

### ✅ Apply
Aplica o plano ao estoque, retornando:
- Produtos produzidos
- Consumo por matéria-prima
- Snapshot do estoque
- Valor total gerado

---

# 🗂 Estrutura do Projeto

Industrial-Production-Manager/
│
├── backend/ # Spring Boot API
│
└── frontend/
└── frontend-vue/ # Vue 3 + Vite

---

# ⚙️ Como Rodar Localmente

## 🔹 Pré-requisitos
- Java 21
- Node.js (LTS recomendado)
- NPM

---

# ▶️ Back-end

### 1️⃣ Acesse a pasta
```bash
cd backend


---

# ⚙️ Como Rodar Localmente

## 🔹 Pré-requisitos
- Java 21
- Node.js (LTS recomendado)
- NPM

---

# ▶️ Back-end

### 1️⃣ Acesse a pasta
```bash
cd backend

### 2️⃣ Execute os testes
./gradlew clean test

### 3️⃣ Inicie o servidor
./gradlew bootRun

# Servidor rodando em:

http://localhost:8080

# 📘 Swagger

http://localhost:8080/swagger-ui/index.html

# ▶️ Front-end

### 1️⃣ Acesse a pasta
cd frontend/frontend-vue

### 2️⃣ Instale dependências
npm install

### 3️⃣ Rode o projeto
npm run dev

# Frontend disponível em:

http://localhost:5173

# 🔗 Comunicação Frontend ↔ Backend

# O Vite está configurado para usar proxy:

/api → http://localhost:8080

# Exemplo:

/api/products

# 🧪 Endpoints Principais

### Matérias-primas

GET /raw-materials

POST /raw-materials

PUT /raw-materials/{id}

DELETE /raw-materials/{id}

### Produtos

GET /products

POST /products

PUT /products/{id}

DELETE /products/{id}

### Produção

POST /production/suggest

POST /production/apply

### Dashboard

GET /dashboard/summary

# 🧮 Lógica do Algoritmo de Produção

Ordena produtos por maior valor.

Calcula produção máxima possível para cada produto.

Consome estoque virtualmente.

Gera plano final.

No Apply, persiste o novo estoque.

# Utiliza BigDecimal para evitar erros de precisão.

# 🛡️ Regras de Negócio Importantes

Não permite excluir matéria-prima que esteja vinculada a produto.

Não permite código duplicado.

Não permite valores negativos.

Produção nunca gera estoque negativo.

# 🧰 Possíveis Melhorias Futuras

Autenticação com JWT

Persistência em banco externo (PostgreSQL)

Deploy em cloud (Render / Railway)

Dashboard com gráficos

Histórico de produções aplicadas

Testes automatizados adicionais

Dockerização

# 📌 Status do Projeto

✔ CRUD completo
✔ Algoritmo de produção funcional
✔ Integração front-end/back-end
✔ Tratamento de erros
✔ Paginação
✔ Bloqueio 409
✔ Swagger
✔ Estrutura organizada

# 👨‍💻 Autor

Desenvolvido como projeto Full Stack para demonstração de arquitetura, regras de negócio e integração entre camadas.

# 📄 Licença

Projeto para fins educacionais e demonstração técnica.