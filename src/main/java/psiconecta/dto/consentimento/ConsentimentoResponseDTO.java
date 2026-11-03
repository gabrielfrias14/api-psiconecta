package psiconecta.dto.consentimento;

import java.time.OffsetDateTime;

import psiconecta.model.enums.TipoConsentimento;

public record ConsentimentoResponseDTO(
        Long id,
        TipoConsentimento tipo,
        String versao,
        Boolean aceito,
        OffsetDateTime registradoEm
) {
}