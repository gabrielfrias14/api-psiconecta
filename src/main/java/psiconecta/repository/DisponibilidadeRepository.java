package psiconecta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import psiconecta.model.Disponibilidade;

public interface DisponibilidadeRepository
        extends JpaRepository<Disponibilidade, Long> {

    List<Disponibilidade> findByEstudante_Id(
            Long estudanteId
    );

    List<Disponibilidade> findByEstudante_IdAndAtivoTrue(
            Long estudanteId
    );

    List<Disponibilidade> findByEstudante_IdAndDiaSemanaAndAtivoTrue(
            Long estudanteId,
            Short diaSemana
    );
}