# Industrial Production Manager

Aplicação **Full Stack** para gerenciamento de **matérias-primas**, **produtos** (com suas composições/insumos) e **planejamento de produção**.  
O backend expõe uma API REST para CRUD e para gerar/aplicar uma sugestão de produção com base no estoque disponível.

---

## ✨ Funcionalidades (Backend)

### Matérias-primas (Raw Materials)
- Criar, listar, detalhar, atualizar e remover matérias-primas
- Campos: `code`, `name`, `stockQuantity`, `unit`

### Produtos (Products)
- Criar, listar, detalhar, atualizar e remover produtos
- Cada produto possui **lista de materiais** (BOM):
  - `rawMaterialId`
  - `quantityPerUnit` (quanto da matéria-prima é consumido por 1 unidade do produto)

### Planejamento de produção (Production Planning)
- `POST /production/suggest`: retorna uma sugestão de produção baseada no estoque atual
- `POST /production/apply`: aplica o plano, **consumindo o estoque** das matérias-primas conforme a produção sugerida

> A sugestão prioriza produtos de **maior preço** e calcula o máximo produzível considerando o gargalo de estoque por matéria-prima.

---

## 🧱 Tecnologias
### Backend
- Java + Spring Boot
- Gradle
- JPA/Hibernate
- MySQL

### Frontend
- (em desenvolvimento)

---

## ✅ Pré-requisitos
- **Java 21** (Eclipse Adoptium / Temurin recomendado)
- **Gradle Wrapper** (já incluso no projeto)
- **MySQL** (rodando localmente)
- (Opcional) VS Code / IntelliJ

---

## 🚀 Como rodar localmente (Backend)

### 1) Clonar o repositório
```bash
git clone <URL_DO_SEU_REPO>
cd industrial-production-manager/backend

2) Criar o banco no MySQL

Crie um banco (exemplo):

CREATE DATABASE industrial_production_manager;

3) Configurar o application.properties (ou application.yml)

No backend, configure as credenciais do MySQL.
Exemplo (ajuste usuário/senha/nome do banco):

spring.datasource.url=jdbc:mysql://localhost:3306/industrial_production_manager
spring.datasource.username=root
spring.datasource.password=SUA_SENHA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

4) Rodar a aplicação

No diretório backend:

Windows (PowerShell / CMD):

.\gradlew.bat bootRun

Linux/Mac:

./gradlew bootRun

A API deve subir em:

http://localhost:8080

🧪 Build e testes

Observação: caso você esteja no meio do desenvolvimento e queira apenas buildar:

./gradlew clean build

Para rodar testes:

./gradlew test
🔌 Endpoints principais
Raw Materials

GET /raw-materials — listar

GET /raw-materials/{id} — detalhar

POST /raw-materials — criar

PUT /raw-materials/{id} — atualizar

DELETE /raw-materials/{id} — remover

Products

GET /products — listar

GET /products/{id} — detalhar

POST /products — criar

PUT /products/{id} — atualizar

DELETE /products/{id} — remover

Production

POST /production/suggest — sugerir plano de produção

POST /production/apply — aplicar plano (consome estoque)

📌 Exemplos rápidos (PowerShell)
Listar produtos
Invoke-RestMethod -Uri "http://localhost:8080/products"

Listar matérias-primas
Invoke-RestMethod -Uri "http://localhost:8080/raw-materials"
Sugerir produção
Invoke-RestMethod -Method Post -Uri "http://localhost:8080/production/suggest"
Aplicar produção (consumir estoque)
Invoke-RestMethod -Method Post -Uri "http://localhost:8080/production/apply"
🗂️ Estrutura (Backend)

src/main/java/com/kayky/backend

domain — entidades JPA

dto — DTOs de request/response

repository — repositórios JPA

service — regras de negócio

web — controllers e handler global

src/test/java/com/kayky/backend

testes unitários (quando aplicável)

📝 Observações

Valores numéricos como stockQuantity podem aparecer como 10,0000 por causa do scale = 4 no banco (BigDecimal).

O endpoint /production/apply só altera estoque se houver um plano com itens (estoque suficiente).

📄 Licença

Este projeto é para fins de estudo/desafio técnico.
