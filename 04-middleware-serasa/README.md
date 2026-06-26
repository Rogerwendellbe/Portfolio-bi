# 04 — Middleware Spring Boot + API Serasa

Middleware de integração entre o ERP Sankhya e a API do Serasa para consulta automatizada de crédito, com autenticação OAuth2 e conformidade LGPD.

## 🎯 Problema

Consultas de crédito de clientes eram feitas manualmente, fora do ERP, sem rastro e sem padronização — risco operacional e de conformidade.

## 💡 Solução

Um middleware em **Java / Spring Boot** que:
1. Recebe a requisição do Sankhya (via BO Action).
2. Autentica na API Serasa via **OAuth2** (client credentials).
3. Consulta o score/situação do CPF/CNPJ.
4. Registra log de conformidade **LGPD** (quem consultou, quando, qual finalidade).
5. Devolve o resultado estruturado para o ERP.

## 🏗️ Arquitetura

```
Sankhya (BO Action)  →  Middleware Spring Boot  →  API Serasa (OAuth2)
                              │
                              └→ Log LGPD (auditoria)
```

## 🔒 Conformidade LGPD

Toda consulta gera registro de auditoria com finalidade declarada, base legal e identificação do solicitante — atendendo aos princípios de finalidade e responsabilização da LGPD.

## 📂 Arquivos

- [`src/CreditController.java`](./src/CreditController.java) — controller REST
- [`src/SerasaClient.java`](./src/SerasaClient.java) — cliente OAuth2 da API Serasa
- [`src/LgpdAuditService.java`](./src/LgpdAuditService.java) — logging de conformidade
- [`src/SankhyaBOAction.snippet.java`](./src/SankhyaBOAction.snippet.java) — trecho da BO Action no Sankhya
