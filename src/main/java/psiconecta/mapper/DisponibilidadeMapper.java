package psiconecta.mapper;

import org.springframework.stereotype.Component;

import psiconecta.dto.disponibilidade.DisponibilidadeResponseDTO;
import psiconecta.model.Disponibilidade;

@Component
public class DisponibilidadeMapper {

    public DisponibilidadeResponseDTO toResponse(
            Disponibilidade disponibilidade) {

        if (disponibilidade == null) {
            return null;
        }

        return new DisponibilidadeResponseDTO(
                disponibilidade.getId(),
                disponibilidade.getDiaSemana(),
                disponibilidade.getHoraInicio(),
                disponibilidade.getHoraFim(),
                disponibilidade.getModalidade(),
                disponibilidade.getAtivo()
        );
    }
}