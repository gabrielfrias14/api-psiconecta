package psiconecta.dto.consulta;

import java.time.OffsetDateTime;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import psiconecta.model.enums.ModalidadeAtendimento;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AgendamentoConsultaDTO {

    @NotNull(message = "O estudante é obrigatório.")
    @Positive(message = "O ID do estudante deve ser válido.")
    private Long estudanteId;

    @NotNull(message = "A data e o horário são obrigatórios.")
    @Future(message = "A consulta deve ser agendada para uma data futura.")
    private OffsetDateTime dataHoraInicio;

    @NotNull(message = "A modalidade é obrigatória.")
    private ModalidadeAtendimento modalidade;

    @Size(
        max = 1000,
        message = "A observação deve possuir no máximo 1000 caracteres."
    )
    private String observacao;

    @AssertTrue(
        message = "A modalidade da consulta deve ser ONLINE ou PRESENCIAL."
    )
    public boolean isModalidadeValida() {

        return modalidade == null
                || modalidade != ModalidadeAtendimento.AMBOS;
    }
}