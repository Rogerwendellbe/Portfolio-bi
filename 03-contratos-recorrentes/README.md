# 03 — Dashboard de Contratos Recorrentes

Painel de gestão de contratos recorrentes, com indicadores de receita previsível e inadimplência — incluindo **parametrização customizada no Sankhya**.

## 🎯 Problema

A empresa não tinha visibilidade sobre sua base de contratos recorrentes: quanto de receita era previsível por mês, qual o maior contrato, quanto estava vencido e como a receita se distribuía por centro de resultado.

## 💡 Solução

1. **Parametrização Sankhya:** criação de um campo customizado `Categoria` para classificar e isolar os contratos recorrentes no modelo de dados.
2. **Dashboard analítico** com KPIs de negócio e validação cruzada via dados de `TCSPRE` (parcelas/previsões).

## 📊 KPIs entregues

- Contratos ativos
- Valor recorrente / mês
- Faturamento vencido
- Maior contrato
- Receita por centro de resultado
- Validação contra dados de `TCSPRE`

## 🔑 Decisões técnicas

- Campo customizado `Categoria` criado via parametrização (não hardcoded em query).
- Cruzamento com `TCSPRE` para validar previsões de receita contra o realizado.
- Agregações respeitando as regras de modelagem Sankhya (sem multiplicação por JOIN indevido).

## 📂 Arquivos

- [`sql/contratos.sql`](./sql/contratos.sql)
- [`dashboard/contratos.html`](./dashboard/contratos.html)
