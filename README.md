# 📚 Plataforma de Reutilização e Troca de Materiais Acadêmicos

> Sistema web desenvolvido para facilitar a doação, troca ou venda de materiais acadêmicos usados entre estudantes e professores. Este projeto integra a disciplina de **Engenharia de Software da UFAPE**, avaliado como parte da **Segunda Verificação de Aprendizagem (2ª VA)**.

---

## 👥 Equipe

* David Saymmon dos Santos Felipe
* Edielson Rodrigues de Souza Silva
* José Matheus Nogueira Luciano
* Luís Filipe de Barros Ferreira
* Stênio Medeiros Freitas
* Jeasiel Abner Silva Maciel

---

## 📃 Sobre o Projeto

A plataforma tem como objetivo **reduzir o descarte desnecessário de materiais acadêmicos** e fomentar a **economia circular** na comunidade universitária. Usuários podem anunciar ou buscar por:

* Livros didáticos usados
* Cadernos, apostilas e anotações
* Equipamentos de laboratório em bom estado
* Mobiliário acadêmico (mesas, cadeiras, suportes etc.)

Os itens podem ser **doados, trocados ou vendidos**, promovendo um consumo consciente entre estudantes e professores.

---

## 🧩 Funcionalidades Previstas

* Cadastro de usuários (estudantes e professores)
* Publicação de anúncios com fotos, descrições e categorias
* Filtros de busca por tipo de material, curso ou disciplina
* Chat interno para negociação direta entre usuários
* Sistema de reputação/avaliação entre usuários
* Interface simples e responsiva

---

## 🛠️ Tecnologias Utilizadas

| Camada   | Tecnologia                                                                                                       |
| -------- | ---------------------------------------------------------------------------------------------------------------- |
| Frontend | [Angular](https://angular.io/)                                                                                   |
| Backend  | [Spring Boot (Java)](https://spring.io/projects/spring-boot)                                                     |
| Gerência | [GitHub Projects (Scrum Board)](https://github.com/orgs/Projeto-E-S-2025-1/projects/7/views/1?custom_template=2) |

---

## 🌐 Deploy

* 👉 [Study Swap - Frontend](https://study-swap-frontend.onrender.com/)
* 👉 [Study Swap - Backend](https://study-swap-backend.onrender.com/)

---

## 📁 Estrutura do Repositório

```bash
/
├── frontend/   # Projeto Angular criado e configurado
└── backend/    # Projeto Spring Boot criado e configurado
```

---

## 🚧 Status do Projeto

🔄 Em Andamento — Primeira Iteração (Configuração de ambientes e organização do projeto)

---

## ⚙️ Execução Local

### 🔹 Backend (Spring Boot)

```bash
cd backend
./mvnw spring-boot:run
# ou, se estiver usando Gradle:
# ./gradlew bootRun
```

### 🔹 Frontend (Angular)

```bash
cd frontend
npm install
ng serve
```
