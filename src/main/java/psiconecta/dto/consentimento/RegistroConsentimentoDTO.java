package psiconecta.dto.consentimento;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import psiconecta.model.enums.TipoConsentimento;

public record RegistroConsentimentoDTO(

        @NotNull(message = "O tipo de consentimento é obrigatório.")
        TipoConsentimento tipo,

        @NotBlank(message = "A versão é obrigatória.")
        String versao,

        @NotNull(message = "Informe se o consentimento foi aceito.")
        Boolean aceito

) {
}