package psiconecta.repository;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import psiconecta.model.Consulta;
import psiconecta.model.enums.StatusConsulta;

public interface ConsultaRepository
    extends JpaRepository<Consulta, Long> {

  List<Consulta> findByPaciente_Id(
      Long pacienteId);

  List<Consulta> findByEstudante_Id(
      Long estudanteId);

  List<Consulta> findByPaciente_IdAndStatus(
      Long pacienteId,
      StatusConsulta status);

  List<Consulta> findByEstudante_IdAndStatus(
      Long estudanteId,
      StatusConsulta status);

  List<Consulta> findByEstudante_IdAndDataHoraInicioBetween(
      Long estudanteId,
      OffsetDateTime inicio,
      OffsetDateTime fim);

  List<Consulta> findByPaciente_IdAndDataHoraInicioBetween(
      Long pacienteId,
      OffsetDateTime inicio,
      OffsetDateTime fim);

  @Query("""
          SELECT COUNT(c) > 0
          FROM Consulta c
          WHERE c.estudante.id = :estudanteId
            AND c.status <> :statusIgnorado
            AND c.dataHoraInicio < :fim
            AND c.dataHoraFim > :inicio
      """)
  boolean existeConflitoEstudante(
      @Param("estudanteId") Long estudanteId,
      @Param("inicio") OffsetDateTime inicio,
      @Param("fim") OffsetDateTime fim,
      @Param("statusIgnorado") StatusConsulta statusIgnorado);

  @Query("""
          SELECT COUNT(c) > 0
          FROM Consulta c
          WHERE c.paciente.id = :pacienteId
            AND c.status <> :statusIgnorado
            AND c.dataHoraInicio < :fim
            AND c.dataHoraFim > :inicio
      """)
  boolean existeConflitoPaciente(
      @Param("pacienteId") Long pacienteId,
      @Param("inicio") OffsetDateTime inicio,
      @Param("fim") OffsetDateTime fim,
      @Param("statusIgnorado") StatusConsulta statusIgnorado);
}