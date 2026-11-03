package psiconecta.dto.consulta;

import java.time.OffsetDateTime;

import psiconecta.model.enums.ModalidadeAtendimento;
import psiconecta.model.enums.StatusConsulta;

public record ConsultaResponseDTO(
        Long id,
        Long pacienteId,
        String pacienteNome,
        Long estudanteId,
        String estudanteNome,
        OffsetDateTime dataHoraInicio,
        OffsetDateTime dataHoraFim,
        Integer duracaoMinutos,
        ModalidadeAtendimento modalidade,
        StatusConsulta status,
        String observacao,
        String linkReuniao
) {
}