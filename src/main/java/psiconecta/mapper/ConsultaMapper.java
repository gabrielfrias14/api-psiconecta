package psiconecta.mapper;

import org.springframework.stereotype.Component;

import psiconecta.dto.consulta.ConsultaResponseDTO;
import psiconecta.model.Consulta;

@Component
public class ConsultaMapper {

    public ConsultaResponseDTO toResponse(
            Consulta consulta) {

        if (consulta == null) {
            return null;
        }

        return new ConsultaResponseDTO(
                consulta.getId(),

                consulta.getPaciente().getId(),
                consulta.getPaciente()
                        .getUsuario()
                        .getNome(),

                consulta.getEstudante().getId(),
                consulta.getEstudante()
                        .getUsuario()
                        .getNome(),

                consulta.getDataHoraInicio(),
                consulta.getDataHoraFim(),
                consulta.getDuracaoMinutos(),
                consulta.getModalidade(),
                consulta.getStatus(),
                consulta.getObservacao(),
                consulta.getLinkReuniao()
        );
    }
}