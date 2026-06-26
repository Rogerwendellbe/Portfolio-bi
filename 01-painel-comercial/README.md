# 01 — Painel Comercial

Dashboard analítico de vendas construído sobre o ERP Sankhya, validado contra o Portal de Vendas oficial.

## 🎯 Problema

A área comercial não tinha uma visão consolidada e confiável de faturamento que pudesse ser cruzada rapidamente por período, tipo de operação e grupo de produto. Relatórios manuais divergiam dos números oficiais.

## 💡 Solução

Dashboard HTML5 embarcado no Sankhya via **BI Component Builder**, alimentado por consultas Oracle SQL diretas sobre `TGFCAB`/`TGFITE`, com renderização dinâmica via Chart.js.

## 🔑 Decisões técnicas

- **Nunca fazer JOIN com `TGFTOP`** em queries de soma de valores — causa multiplicação massiva de linhas. Filtro de tipo de operação feito direto via `CODTIPOPER IN (...)` no `TGFCAB`.
- Filtro `STATUSNOTA = 'L'` (notas liberadas) para refletir apenas vendas efetivas.
- Subqueries com `EXISTS` para filtragem por produto/grupo sem inflar o resultado.
- Fetch com `credentials:'include'` para autenticação de sessão Sankhya.
- CSS escopado com prefixo `pc-` para evitar conflito com o estilo do ERP.

## ✅ Validação

Resultado bateu **exatamente** com o Portal de Vendas oficial:
**R$ 21.040.569,67** (referência maio/2026).

## 📂 Arquivos

- [`sql/painel_comercial.sql`](./sql/painel_comercial.sql) — query principal de faturamento
- [`dashboard/painel.html`](./dashboard/painel.html) — dashboard completo (HTML + Chart.js)
