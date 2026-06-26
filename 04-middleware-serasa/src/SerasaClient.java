package br.com.portfolio.creditmiddleware.service;

import br.com.portfolio.creditmiddleware.dto.CreditResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Map;

/**
 * Cliente da API Serasa com autenticação OAuth2 (client credentials).
 * Faz cache simples do token até a expiração para evitar re-autenticação a cada chamada.
 */
@Service
public class SerasaClient {

    @Value("${serasa.token-url}")    private String tokenUrl;
    @Value("${serasa.api-url}")      private String apiUrl;
    @Value("${serasa.client-id}")    private String clientId;
    @Value("${serasa.client-secret}") private String clientSecret;

    private final RestTemplate rest = new RestTemplate();
    private String cachedToken;
    private Instant tokenExpiry = Instant.MIN;

    /** Obtém (ou renova) o token OAuth2 via client_credentials. */
    private synchronized String obterToken() {
        if (cachedToken != null && Instant.now().isBefore(tokenExpiry)) {
            return cachedToken;
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(clientId, clientSecret);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> req = new HttpEntity<>("grant_type=client_credentials", headers);
        ResponseEntity<Map> resp = rest.postForEntity(tokenUrl, req, Map.class);

        Map body = resp.getBody();
        cachedToken = (String) body.get("access_token");
        int expiresIn = (Integer) body.getOrDefault("expires_in", 3600);
        tokenExpiry = Instant.now().plusSeconds(expiresIn - 60); // margem de segurança
        return cachedToken;
    }

    /** Consulta o score de crédito de um documento (CPF/CNPJ). */
    public CreditResponse consultarScore(String documento) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(obterToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> req =
                new HttpEntity<>(Map.of("documento", documento), headers);

        ResponseEntity<CreditResponse> resp =
                rest.exchange(apiUrl + "/score", HttpMethod.POST, req, CreditResponse.class);

        return resp.getBody();
    }
}
