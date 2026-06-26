-- ============================================================
-- CONTRATOS RECORRENTES
-- ERP Sankhya / Oracle SQL
-- ------------------------------------------------------------
-- Usa campo customizado AD_CATEGORIA (criado via parametrização)
-- Valida previsões de receita contra TCSPRE (parcelas)
-- ============================================================

-- KPIs principais: contratos ativos, valor recorrente/mês, vencido
SELECT
    COUNT(DISTINCT C.NUMCONTRATO)                         AS CONTRATOS_ATIVOS,
    SUM(C.VLRCONTRATO)                                    AS VALOR_RECORRENTE_MES,
    SUM(CASE WHEN P.DTVENC < SYSDATE AND P.DHBAIXA IS NULL
             THEN P.VLRDESDOB ELSE 0 END)                 AS FATURAMENTO_VENCIDO,
    MAX(C.VLRCONTRATO)                                    AS MAIOR_CONTRATO
FROM TCSCON C
LEFT JOIN TCSPRE P ON P.NUMCONTRATO = C.NUMCONTRATO
WHERE C.AD_CATEGORIA = :CATEGORIA_RECORRENTE   -- campo customizado
  AND C.STATUS = 'A';                          -- ativos

-- Receita por centro de resultado
SELECT
    CR.DESCRCENCUS                              AS CENTRO_RESULTADO,
    SUM(C.VLRCONTRATO)                          AS RECEITA_RECORRENTE
FROM TCSCON C
INNER JOIN TSICUS CR ON CR.CODCENCUS = C.CODCENCUS
WHERE C.AD_CATEGORIA = :CATEGORIA_RECORRENTE
  AND C.STATUS = 'A'
GROUP BY CR.DESCRCENCUS
ORDER BY RECEITA_RECORRENTE DESC;

-- Validação: previsão (TCSPRE) x valor de contrato
SELECT
    C.NUMCONTRATO,
    C.VLRCONTRATO                               AS VLR_CONTRATO,
    SUM(P.VLRDESDOB)                            AS VLR_PREVISTO_TCSPRE,
    (SUM(P.VLRDESDOB) - C.VLRCONTRATO)          AS DIVERGENCIA
FROM TCSCON C
INNER JOIN TCSPRE P ON P.NUMCONTRATO = C.NUMCONTRATO
WHERE C.AD_CATEGORIA = :CATEGORIA_RECORRENTE
GROUP BY C.NUMCONTRATO, C.VLRCONTRATO
HAVING ABS(SUM(P.VLRDESDOB) - C.VLRCONTRATO) > 0.01
ORDER BY ABS(SUM(P.VLRDESDOB) - C.VLRCONTRATO) DESC;
