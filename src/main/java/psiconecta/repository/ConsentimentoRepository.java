package psiconecta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import psiconecta.model.Consentimento;
import psiconecta.model.enums.TipoConsentimento;

public interface ConsentimentoRepository
        extends JpaRepository<Consentimento, Long> {

    List<Consentimento> findByUsuario_Id(Long usuarioId);

    List<Consentimento> findByUsuario_IdAndTipo(
        Long usuarioId,
        TipoConsentimento tipo
    );

    boolean existsByUsuario_IdAndTipoAndVersaoAndAceitoTrue(
        Long usuarioId,
        TipoConsentimento tipo,
        String versao
    );
}