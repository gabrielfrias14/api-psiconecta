package psiconecta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import psiconecta.model.LogAuditoria;

public interface LogAuditoriaRepository
        extends JpaRepository<LogAuditoria, Long> {

    List<LogAuditoria> findByUsuario_Id(Long usuarioId);

    List<LogAuditoria> findByEntidadeAndEntidadeId(
        String entidade,
        Long entidadeId
    );
}