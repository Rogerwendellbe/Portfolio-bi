-- ============================================================
-- DASHBOARD DE DEVOLUÇÕES
-- ERP Sankhya / Oracle SQL
-- ------------------------------------------------------------
-- Parâmetros:
--   :TOPS_DEVOL -> CODTIPOPER de devolução de venda
--   :DT_INI / :DT_FIM
-- ============================================================

-- Valor total devolvido por competência
SELECT
    TO_CHAR(CAB.DTNEG, 'YYYY-MM')        AS COMPETENCIA,
    COUNT(DISTINCT CAB.NUNOTA)           AS QTD_NOTAS,
    SUM(ITE.VLRTOT)                      AS VLR_DEVOLVIDO
FROM TGFCAB CAB
INNER JOIN TGFITE ITE ON ITE.NUNOTA = CAB.NUNOTA
WHERE CAB.STATUSNOTA = 'L'
  AND CAB.CODTIPOPER IN (:TOPS_DEVOL)
  AND CAB.DTNEG BETWEEN :DT_INI AND :DT_FIM
GROUP BY TO_CHAR(CAB.DTNEG, 'YYYY-MM')
ORDER BY COMPETENCIA;

-- Top produtos devolvidos
SELECT
    PRO.DESCRPROD                        AS PRODUTO,
    SUM(ITE.QTDNEG)                      AS QTD_DEVOLVIDA,
    SUM(ITE.VLRTOT)                      AS VLR_DEVOLVIDO
FROM TGFCAB CAB
INNER JOIN TGFITE ITE ON ITE.NUNOTA = CAB.NUNOTA
INNER JOIN TGFPRO PRO ON PRO.CODPROD = ITE.CODPROD
WHERE CAB.STATUSNOTA = 'L'
  AND CAB.CODTIPOPER IN (:TOPS_DEVOL)
  AND CAB.DTNEG BETWEEN :DT_INI AND :DT_FIM
GROUP BY PRO.DESCRPROD
ORDER BY VLR_DEVOLVIDO DESC
FETCH FIRST 10 ROWS ONLY;
