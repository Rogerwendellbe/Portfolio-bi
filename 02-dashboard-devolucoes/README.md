# 02 — Dashboard de Devoluções

Painel analítico para monitorar devoluções de vendas, identificar padrões e quantificar impacto financeiro.

## 🎯 Problema

Devoluções eram tratadas de forma reativa, sem visão consolidada de volume, motivo ou concentração por cliente/produto. Sem isso, não dava pra agir na causa raiz.

## 💡 Solução

Dashboard alimentado por consultas Oracle SQL sobre as notas de devolução (operações específicas em `TGFCAB`), cruzando volume, valor e período, com visualização em Chart.js.

## 🔑 Decisões técnicas

- Filtro de tipo de operação de devolução direto via `CODTIPOPER` (sem JOIN em `TGFTOP`).
- `STATUSNOTA = 'L'` para considerar apenas devoluções efetivadas.
- Indicadores: valor total devolvido, % sobre faturamento, top produtos e top parceiros.

## 📂 Arquivos

- [`sql/devolucoes.sql`](./sql/devolucoes.sql)
