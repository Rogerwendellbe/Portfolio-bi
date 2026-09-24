# 04 — Middleware Spring Boot + API Serasa

Exemplo didático de integração entre ERP Sankhya e serviço externo de consulta de crédito, com fluxo OAuth2 e pontos de auditoria. **Não é uma aplicação executável nem uma integração homologada para produção.**

Os arquivos em `src/` são trechos ilustrativos. Faltam classes DTO, configuração de dependências e aplicação, testes, validação de entrada, autenticação/autorização do endpoint e persistência de auditoria. Endereços, payloads e regras da API externa devem ser confirmados com a documentação e o contrato do serviço antes de implementar uma integração real.

## 🎯 Problema

Consultas de crédito de clientes eram feitas manualmente, fora do ERP, sem rastro e sem padronização — risco operacional e de conformidade.

## 💡 Solução

O exemplo em **Java / Spring Boot** ilustra como um middleware poderia:
1. Receber a requisição do Sankhya (via BO Action).
2. Autenticar na API Serasa via **OAuth2** (client credentials).
3. Consultar o score/situação do CPF/CNPJ.
4. Registrar informações de auditoria (quem consultou, quando e qual finalidade) em um sistema apropriado.
5. Devolver o resultado estruturado para o ERP.

## 🏗️ Arquitetura

```
Sankhya (BO Action)  →  Middleware Spring Boot  →  API Serasa (OAuth2)
                              │
                              └→ Serviço de auditoria (persistência pendente)
```

## 🔒 Dados e auditoria

O trecho atual escreve eventos em log e mascara parte do documento. **Ele não implementa uma trilha de auditoria persistente, não registra a base legal e não demonstra conformidade com a LGPD.** O `TODO` em `LgpdAuditService.java` marca a persistência ainda pendente. Antes de um uso real, é necessário definir base legal, controle de acesso, retenção, proteção dos logs, auditoria persistente e revisão jurídica/técnica apropriada.

## 📂 Arquivos

- [`src/CreditController.java`](./src/CreditController.java) — controller REST
- [`src/SerasaClient.java`](./src/SerasaClient.java) — cliente OAuth2 da API Serasa
- [`src/LgpdAuditService.java`](./src/LgpdAuditService.java) — logging de conformidade
- [`src/SankhyaBOAction.snippet.java`](./src/SankhyaBOAction.snippet.java) — trecho da BO Action no Sankhya
