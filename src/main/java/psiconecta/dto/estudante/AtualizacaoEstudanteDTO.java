package psiconecta.dto.estudante;

import java.util.Set;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import psiconecta.model.enums.ModalidadeAtendimento;

public record AtualizacaoEstudanteDTO(

        @NotBlank(message = "O nome é obrigatório.")
        @Size(
            min = 2,
            max = 150,
            message = "O nome deve possuir entre 2 e 150 caracteres."
        )
        String nome,

        @NotNull(message = "O período é obrigatório.")
        @Min(
            value = 1,
            message = "O período deve ser no mínimo 1."
        )
        @Max(
            value = 12,
            message = "O período deve ser no máximo 12."
        )
        Short periodo,

        @Size(
            max = 2000,
            message = "A bio deve possuir no máximo 2000 caracteres."
        )
        String bio,

        @NotNull(message = "A modalidade é obrigatória.")
        ModalidadeAtendimento modalidade,

        @NotEmpty(message = "Informe pelo menos uma área de atendimento.")
        Set<Long> areasAtendimentoIds

) {
}