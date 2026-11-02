package psiconecta.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import psiconecta.model.Estudante;
import psiconecta.model.enums.StatusAprovacaoEstudante;

public interface EstudanteRepository
        extends JpaRepository<Estudante, Long> {

    Optional<Estudante> findByUsuario_Id(Long usuarioId);

    Optional<Estudante> findByUniversidadeIgnoreCaseAndMatricula(
            String universidade,
            String matricula
    );

    boolean existsByUniversidadeIgnoreCaseAndMatricula(
            String universidade,
            String matricula
    );

    List<Estudante> findByStatusAprovacao(
            StatusAprovacaoEstudante statusAprovacao
    );

    List<Estudante> findDistinctByStatusAprovacaoAndAreasAtendimento_Id(
        StatusAprovacaoEstudante statusAprovacao,
        Long areaId
);
}