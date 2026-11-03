package psiconecta.dto.disponibilidade;

import java.time.LocalTime;

import psiconecta.model.enums.ModalidadeAtendimento;

public record DisponibilidadeResponseDTO(
        Long id,
        Short diaSemana,
        LocalTime horaInicio,
        LocalTime horaFim,
        ModalidadeAtendimento modalidade,
        Boolean ativo
) {
}