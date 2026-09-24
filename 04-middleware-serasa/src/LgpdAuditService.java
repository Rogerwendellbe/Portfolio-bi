package br.com.portfolio.creditmiddleware.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Exemplo de logging de eventos de consulta de crédito.
 * Este log não substitui uma trilha de auditoria persistente nem comprova
 * conformidade com a LGPD.
 *
 * Em produção, persistir em tabela de auditoria imutável (append-only),
 * nunca apenas em log de texto.
 */
@Service
public class LgpdAuditService {

    private static final Logger log = LoggerFactory.getLogger(LgpdAuditService.class);

    public void registrarConsulta(String documento, String solicitante, String finalidade) {
        // Documento mascarado no log para minimização de dados
        log.info("[AUDITORIA_EXEMPLO][CONSULTA] doc={} solicitante={} finalidade='{}' em={}",
                mascarar(documento), solicitante, finalidade, LocalDateTime.now());
        // TODO antes de uso real: definir e implementar auditoria persistente e protegida.
    }

    public void registrarResultado(String documento, Integer score) {
        log.info("[AUDITORIA_EXEMPLO][RESULTADO] doc={} score={} em={}",
                mascarar(documento), score, LocalDateTime.now());
    }

    /** Mascara o documento mantendo apenas os 3 primeiros e 2 últimos dígitos. */
    private String mascarar(String doc) {
        if (doc == null || doc.length() < 6) return "***";
        return doc.substring(0, 3) + "****" + doc.substring(doc.length() - 2);
    }
}
