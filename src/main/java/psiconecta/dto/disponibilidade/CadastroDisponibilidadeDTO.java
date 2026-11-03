package psiconecta.dto.disponibilidade;

import java.time.LocalTime;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import psiconecta.model.enums.ModalidadeAtendimento;

public record CadastroDisponibilidadeDTO(

        @NotNull(message = "O dia da semana é obrigatório.")
        @Min(value = 1, message = "O dia da semana deve estar entre 1 e 7.")
        @Max(value = 7, message = "O dia da semana deve estar entre 1 e 7.")
        Short diaSemana,

        @NotNull(message = "O horário inicial é obrigatório.")
        LocalTime horaInicio,

        @NotNull(message = "O horário final é obrigatório.")
        LocalTime horaFim,

        @NotNull(message = "A modalidade é obrigatória.")
        ModalidadeAtendimento modalidade

) {

    @AssertTrue(message = "O horário final deve ser posterior ao horário inicial.")
    public boolean isHorarioValido() {

        if (horaInicio == null || horaFim == null) {
            return true;
        }

        return horaFim.isAfter(horaInicio);
    }

    @AssertTrue(message = "A modalidade deve ser ONLINE ou PRESENCIAL.")
    public boolean isModalidadeValida() {

        if (modalidade == null) {
            return true;
        }

        return modalidade != ModalidadeAtendimento.AMBOS;
    }
}