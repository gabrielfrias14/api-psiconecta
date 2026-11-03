package psiconecta.mapper;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import psiconecta.dto.areaatendimento.AreaAtendimentoResponseDTO;
import psiconecta.dto.estudante.EstudanteResponseDTO;
import psiconecta.model.Estudante;

@Component
public class EstudanteMapper {

    private final UsuarioMapper usuarioMapper;
    private final AreaAtendimentoMapper areaAtendimentoMapper;

    public EstudanteMapper(
            UsuarioMapper usuarioMapper,
            AreaAtendimentoMapper areaAtendimentoMapper) {

        this.usuarioMapper = usuarioMapper;
        this.areaAtendimentoMapper = areaAtendimentoMapper;
    }

    public EstudanteResponseDTO toResponse(
            Estudante estudante) {

        if (estudante == null) {
            return null;
        }

        Set<AreaAtendimentoResponseDTO> areasAtendimento =
                estudante.getAreasAtendimento()
                        .stream()
                        .map(areaAtendimentoMapper::toResponse)
                        .collect(Collectors.toSet());

        return new EstudanteResponseDTO(
                estudante.getId(),
                usuarioMapper.toResponse(
                        estudante.getUsuario()
                ),
                estudante.getUniversidade(),
                estudante.getMatricula(),
                estudante.getPeriodo(),
                estudante.getEstagioSupervisionado(),
                estudante.getBio(),
                estudante.getFotoUrl(),
                estudante.getModalidade(),
                estudante.getStatusAprovacao(),
                areasAtendimento
        );
    }
}