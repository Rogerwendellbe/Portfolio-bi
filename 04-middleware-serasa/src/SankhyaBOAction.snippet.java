// ============================================================
// SANKHYA — BO Action (snippet)
// Dispara a consulta de crédito chamando o middleware Spring Boot.
// Roda no contexto do Sankhya (JavaScript de BO Action / regra).
// ============================================================

function consultarCreditoMiddleware(codParc, documento, usuario) {
    var url = 'https://middleware.interno/api/v1/credito/consultar';

    var payload = JSON.stringify({
        documento:   documento,
        solicitante: usuario,
        finalidade:  'Analise de credito para liberacao de pedido'
    });

    var conn = new java.net.URL(url).openConnection();
    conn.setRequestMethod('POST');
    conn.setRequestProperty('Content-Type', 'application/json');
    conn.setDoOutput(true);

    var os = conn.getOutputStream();
    os.write(new java.lang.String(payload).getBytes('UTF-8'));
    os.flush();
    os.close();

    var status = conn.getResponseCode();
    if (status === 200) {
        var resposta = lerResposta(conn.getInputStream());
        // Grava o score retornado no parceiro (campo customizado)
        atualizarScoreParceiro(codParc, resposta.score);
        return resposta;
    } else {
        throw new Error('Falha na consulta de credito. HTTP ' + status);
    }
}
