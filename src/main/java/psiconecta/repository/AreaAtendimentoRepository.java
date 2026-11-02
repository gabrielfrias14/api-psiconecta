package psiconecta.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import psiconecta.model.AreaAtendimento;

public interface AreaAtendimentoRepository
        extends JpaRepository<AreaAtendimento, Long> {

    Optional<AreaAtendimento> findByNomeIgnoreCase(
            String nome
    );

    boolean existsByNomeIgnoreCase(
            String nome
    );

    List<AreaAtendimento> findByAtivoTrue();
}