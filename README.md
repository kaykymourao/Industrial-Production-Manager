Industrial Production Manager

Sistema Full-Stack para gerenciamento industrial de produtos, matérias-primas, estoque e simulação/aplicação de produção, desenvolvido com Spring Boot (Java) no backend e Vue 3 + Vite no frontend.

📌 Sobre o Projeto

O Industrial Production Manager é uma aplicação web que permite:

Cadastro e gerenciamento de Produtos

Cadastro e controle de Matérias-primas

Controle de estoque

Geração de sugestão de produção baseada no estoque disponível

Aplicação da produção com baixa automática de estoque

Visualização de métricas no Dashboard

O sistema calcula automaticamente quantas unidades de cada produto podem ser produzidas com base na disponibilidade de matérias-primas e seus consumos por unidade.

🏗️ Arquitetura

Projeto dividido em duas camadas principais:

Industrial-Production-Manager/
│
├── backend/              # Spring Boot (Java)
│
└── frontend/
    └── frontend-vue/     # Vue 3 + Vite
Backend

Java

Spring Boot

Spring Data JPA

Hibernate

Banco de dados relacional

Gradle

Frontend

Vue 3

Vite

Axios

CSS moderno com layout responsivo

🚀 Funcionalidades
📊 Dashboard

Total de produtos cadastrados

Total de matérias-primas

Soma total de estoque

Soma total dos preços dos produtos

Valor máximo possível de produção com base no estoque

📦 Produtos

Criar produto

Editar produto

Excluir produto

Paginação

Busca por código

Associação de matérias-primas ao produto

Definição de consumo por unidade produzida

🏭 Matérias-primas

Criar matéria-prima

Editar matéria-prima

Excluir matéria-prima (bloqueado se estiver em uso)

Controle de estoque

Paginação

Busca por código

⚙️ Produção
Suggest

Calcula automaticamente:

Quantidade máxima possível de produção

Valor total estimado

Consumo necessário de cada matéria-prima

Apply

Aplica a produção

Atualiza estoque

Gera snapshot antes/depois do estoque

Registra consumo

🧮 Regra de Negócio da Produção

Para cada produto:

quantidade possível = menor valor entre:
  (estoque da matéria-prima / consumo por unidade)

Exemplo:

Se um produto consome:

2.5 KG de aço por unidade
E o estoque disponível é:

100 KG

Produção máxima:

100 / 2.5 = 40 unidades
⚙️ Como Executar o Projeto
🔹 1. Backend

Entre na pasta:

cd backend

Execute:

./gradlew bootRun

O backend rodará em:

http://localhost:8080
🔹 2. Frontend

Entre na pasta:

cd frontend/frontend-vue

Instale dependências:

npm install

Execute:

npm run dev

O frontend rodará em:

http://localhost:5173
🔗 Comunicação Frontend ↔ Backend

O frontend utiliza:

baseURL: "/api"

Com proxy configurado no Vite para redirecionar chamadas para:

http://localhost:8080
🧪 Principais Endpoints
Produtos

GET /products

POST /products

PUT /products/{id}

DELETE /products/{id}

Matérias-primas

GET /raw-materials

POST /raw-materials

PUT /raw-materials/{id}

DELETE /raw-materials/{id}

Produção

POST /production/suggest

POST /production/apply

Dashboard

GET /dashboard/summary

🎨 Interface

Layout moderno

Cards informativos

Tabelas responsivas

Paginação dinâmica

Feedback visual de erro

Badges de status

Formatação monetária em pt-BR

🛡️ Regras Importantes

Matéria-prima não pode ser excluída se estiver vinculada a produto.

Estoque é atualizado apenas no apply.

suggest não altera dados.

Sistema impede produção se estoque for insuficiente.

📈 Melhorias Futuras

Autenticação (JWT)

Controle de usuários

Histórico de produção

Gráficos no Dashboard

Logs de auditoria

Testes automatizados

Deploy em nuvem

🧑‍💻 Autor

Desenvolvido por Kayky Mourão

Projeto criado para prática Full-Stack com foco em:

Arquitetura organizada

Separação de responsabilidades

Lógica de negócio estruturada

Integração Frontend + Backend

📄 Licença

Este projeto é de uso educacional e demonstrativo.
