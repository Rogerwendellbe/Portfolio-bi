package br.com.portfolio.creditmiddleware.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Serviço de logging de conformidade LGPD.
 * Toda consulta de crédito é registrada com finalidade, solicitante e timestamp,
 * atendendo aos princípios de finalidade (art. 6, I) e responsabilização (art. 6, X).
 *
 * Em produção, persistir em tabela de auditoria imutável (append-only),
 * nunca apenas em log de texto.
 */
@Service
public class LgpdAuditService {

    private static final Logger log = LoggerFactory.getLogger(LgpdAuditService.class);

    public void registrarConsulta(String documento, String solicitante, String finalidade) {
        // Documento mascarado no log para minimização de dados
        log.info("[LGPD][CONSULTA] doc={} solicitante={} finalidade='{}' em={}",
                mascarar(documento), solicitante, finalidade, LocalDateTime.now());
        // TODO produção: gravar em TB_AUDITORIA_LGPD (append-only)
    }

    public void registrarResultado(String documento, Integer score) {
        log.info("[LGPD][RESULTADO] doc={} score={} em={}",
                mascarar(documento), score, LocalDateTime.now());
    }

    /** Mascara o documento mantendo apenas os 3 primeiros e 2 últimos dígitos. */
    private String mascarar(String doc) {
        if (doc == null || doc.length() < 6) return "***";
        return doc.substring(0, 3) + "****" + doc.substring(doc.length() - 2);
    }
}
