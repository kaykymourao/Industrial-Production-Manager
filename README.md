🏭 Industrial Production Manager
<p align="center">
⚙️ Sistema Full-Stack de Gestão Industrial

Aplicação web moderna para gerenciamento de Produtos, Matérias-Primas, Estoque e Simulação de Produção, com arquitetura limpa e regras de negócio estruturadas.

</p>
<p align="center">












</p>
🔵 Visão Geral

O Industrial Production Manager permite:

🔹 Gerenciar produtos

🔹 Controlar matérias-primas

🔹 Monitorar estoque em tempo real

🔹 Simular produção máxima possível

🔹 Aplicar produção com baixa automática

🔹 Visualizar métricas estratégicas

O sistema calcula automaticamente quantas unidades podem ser produzidas com base na disponibilidade de estoque e no consumo por unidade.

🔵 • Arquitetura do Projeto

Industrial-Production-Manager/

│
├── backend/               → Spring Boot (API REST)

│
└── frontend/

        └── frontend-vue/ → Vue 3 + Vite
    
📌 Estrutura baseada em separação de responsabilidades:

Backend responsável por regras de negócio

Frontend responsável por experiência e visual

Comunicação via REST API

🔵 Backend

🧩 Tecnologias

☕ Java
🌱 Spring Boot
🗃️ Spring Data JPA
🔄 Hibernate
🛠️ Gradle
🗄️ Banco Relacional

🎯 Responsabilidades

🔹 Cálculo de produção
🔹 Atualização de estoque
🔹 Controle de integridade
🔹 Validação de regras
🔹 API REST estruturada

🔵 Frontend

🧩 Tecnologias

🟢 Vue 3
⚡ Vite
🔗 Axios
🎨 CSS moderno e responsivo

🎯 Responsabilidades

🔹 Interface moderna
🔹 Paginação dinâmica
🔹 Filtros por código
🔹 Feedback visual
🔹 Formatação monetária pt-BR

🔵 Funcionalidades

📊 Dashboard

🔹 Total de produtos
🔹 Total de matérias-primas
🔹 Soma total de estoque
🔹 Soma de preços dos produtos
🔹 Valor máximo de produção possível

📦 Produtos

🔹 Criar
🔹 Editar
🔹 Excluir
🔹 Paginação
🔹 Busca por código
🔹 Associação de matérias-primas
🔹 Definição de consumo por unidade

🏗️ Matérias-Primas

🔹 Criar
🔹 Editar
🔹 Excluir (com proteção se estiver em uso)
🔹 Controle de estoque
🔹 Paginação
🔹 Busca por código

⚙️ Produção

🔍 Suggest

✔ Calcula produção máxima possível
✔ Calcula valor total estimado
✔ Lista consumo necessário
✔ Não altera o estoque

✅ Apply

✔ Aplica produção
✔ Atualiza estoque
✔ Gera snapshot antes/depois
✔ Registra consumo

🔵 Regra de Negócio

A produção é limitada pela matéria-prima mais restritiva.

produção máxima =
menor valor entre (estoque disponível ÷ consumo por unidade)
📌 Exemplo

Produto consome:

2.5 KG de aço por unidade

Estoque disponível:

100 KG

Resultado:

100 ÷ 2.5 = 40 unidades

🔵 Principais Endpoints

📦 Produtos

GET /products

POST /products

PUT /products/{id}

DELETE /products/{id}

🏗️ Matérias-Primas

GET /raw-materials

POST /raw-materials

PUT /raw-materials/{id}

DELETE /raw-materials/{id}

⚙️ Produção

POST /production/suggest

POST /production/apply

📊 Dashboard

GET /dashboard/summary

🔵 Como Executar

🖥️ Backend

cd backend

./gradlew bootRun

Servidor em: http://localhost:8080

🌐 Frontend

cd frontend/frontend-vue

npm install

npm run dev

Aplicação em: http://localhost:5173

🔵 Regras Importantes

🔹 Matéria-prima não pode ser excluída se estiver vinculada
🔹 Suggest não altera dados
🔹 Apply altera estoque
🔹 Produção não ocorre se estoque for insuficiente

🔵 Roadmap Futuro

🔐 Autenticação JWT
👥 Controle de usuários
📜 Histórico de produção
📊 Gráficos estratégicos
🧪 Testes automatizados
☁️ Deploy em nuvem

👨‍💻 Desenvolvido por
Kayky Mourão

Projeto criado com foco em:

🔹 Arquitetura limpa
🔹 Organização de código
🔹 Integração Full-Stack
🔹 Regras de negócio estruturadas
🔹 Visual profissional

📄 Licença

Projeto para fins educacionais e demonstrativos.
