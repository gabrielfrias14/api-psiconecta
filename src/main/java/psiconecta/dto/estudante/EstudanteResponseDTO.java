package psiconecta.dto.estudante;

import java.util.Set;

import psiconecta.dto.areaatendimento.AreaAtendimentoResponseDTO;
import psiconecta.dto.usuario.UsuarioResponseDTO;
import psiconecta.model.enums.ModalidadeAtendimento;
import psiconecta.model.enums.StatusAprovacaoEstudante;

public record EstudanteResponseDTO(
        Long id,
        UsuarioResponseDTO usuario,
        String universidade,
        String matricula,
        Short periodo,
        Boolean estagioSupervisionado,
        String bio,
        String fotoUrl,
        ModalidadeAtendimento modalidade,
        StatusAprovacaoEstudante statusAprovacao,
        Set<AreaAtendimentoResponseDTO> areasAtendimento
) {
}