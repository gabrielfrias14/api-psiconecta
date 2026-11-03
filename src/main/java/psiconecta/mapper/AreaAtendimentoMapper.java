package psiconecta.mapper;

import org.springframework.stereotype.Component;

import psiconecta.dto.areaatendimento.AreaAtendimentoResponseDTO;
import psiconecta.model.AreaAtendimento;

@Component
public class AreaAtendimentoMapper {

    public AreaAtendimentoResponseDTO toResponse(
            AreaAtendimento areaAtendimento) {

        if (areaAtendimento == null) {
            return null;
        }

        return new AreaAtendimentoResponseDTO(
                areaAtendimento.getId(),
                areaAtendimento.getNome(),
                areaAtendimento.getDescricao(),
                areaAtendimento.getAtivo()
        );
    }
}