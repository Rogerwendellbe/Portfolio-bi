-- ============================================================
-- PAINEL COMERCIAL — Faturamento consolidado
-- ERP Sankhya / Oracle SQL
-- ------------------------------------------------------------
-- Regras críticas aplicadas:
--   * NUNCA fazer JOIN com TGFTOP em queries de soma (multiplica linhas)
--   * Filtro de operação direto via CODTIPOPER IN (...) no TGFCAB
--   * STATUSNOTA = 'L' (notas liberadas / vendas efetivas)
--   * Filtro de produto via EXISTS (não infla agregação)
-- ------------------------------------------------------------
-- Parâmetros (substituir conforme ambiente):
--   :TOPS_VENDA  -> lista de CODTIPOPER de venda
--   :PARC_EXCL   -> parceiros a excluir (intercompany / ajustes)
--   :GRUPO_PROD  -> grupo de produto a filtrar (opcional)
--   :DT_INI / :DT_FIM -> período de análise
-- ============================================================

SELECT
    TO_CHAR(CAB.DTNEG, 'YYYY-MM')                  AS COMPETENCIA,
    SUM(ITE.VLRTOT)                                AS FATURAMENTO
FROM TGFCAB CAB
INNER JOIN TGFITE ITE
        ON ITE.NUNOTA = CAB.NUNOTA
WHERE CAB.STATUSNOTA = 'L'
  AND CAB.CODTIPOPER IN (:TOPS_VENDA)            -- ex.: 1100, 1117
  AND CAB.DTNEG BETWEEN :DT_INI AND :DT_FIM
  AND CAB.CODPARC NOT IN (:PARC_EXCL)            -- ex.: 307, 320, 1148
  AND EXISTS (
        SELECT 1
        FROM TGFPRO PRO
        WHERE PRO.CODPROD = ITE.CODPROD
          AND PRO.CODGRUPOPROD = :GRUPO_PROD     -- opcional
      )
GROUP BY TO_CHAR(CAB.DTNEG, 'YYYY-MM')
ORDER BY COMPETENCIA;

-- ------------------------------------------------------------
-- Variação: faturamento por Centro de Resultado
-- ------------------------------------------------------------
SELECT
    CR.DESCRCENCUS                                AS CENTRO_RESULTADO,
    SUM(ITE.VLRTOT)                               AS FATURAMENTO
FROM TGFCAB CAB
INNER JOIN TGFITE ITE  ON ITE.NUNOTA = CAB.NUNOTA
INNER JOIN TSICUS CR   ON CR.CODCENCUS = CAB.CODCENCUS
WHERE CAB.STATUSNOTA = 'L'
  AND CAB.CODTIPOPER IN (:TOPS_VENDA)
  AND CAB.DTNEG BETWEEN :DT_INI AND :DT_FIM
GROUP BY CR.DESCRCENCUS
ORDER BY FATURAMENTO DESC;
