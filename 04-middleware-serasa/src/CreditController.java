package br.com.portfolio.creditmiddleware.controller;

import br.com.portfolio.creditmiddleware.dto.CreditRequest;
import br.com.portfolio.creditmiddleware.dto.CreditResponse;
import br.com.portfolio.creditmiddleware.service.SerasaClient;
import br.com.portfolio.creditmiddleware.service.LgpdAuditService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller REST que expõe a consulta de crédito para o ERP Sankhya.
 * Recebe a requisição da BO Action, dispara a consulta Serasa
 * e chama o serviço de log demonstrativo. Não está pronto para produção.
 */
@RestController
@RequestMapping("/api/v1/credito")
public class CreditController {

    private final SerasaClient serasaClient;
    private final LgpdAuditService auditService;

    public CreditController(SerasaClient serasaClient, LgpdAuditService auditService) {
        this.serasaClient = serasaClient;
        this.auditService = auditService;
    }

    @PostMapping("/consultar")
    public ResponseEntity<CreditResponse> consultar(@RequestBody CreditRequest req) {
        // 1. Escreve um evento demonstrativo (não é auditoria persistente)
        auditService.registrarConsulta(req.getDocumento(), req.getSolicitante(), req.getFinalidade());

        // 2. Consulta a API Serasa (OAuth2 tratado internamente no client)
        CreditResponse resposta = serasaClient.consultarScore(req.getDocumento());

        // 3. Registra o resultado para auditoria
        auditService.registrarResultado(req.getDocumento(), resposta.getScore());

        return ResponseEntity.ok(resposta);
    }
}
