package psiconecta.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import psiconecta.model.LogAuditoria;
import psiconecta.model.Usuario;
import psiconecta.repository.LogAuditoriaRepository;
import psiconecta.model.Usuario;

@Service
public class LogAuditoriaService {

    private final LogAuditoriaRepository logAuditoriaRepository;

    public LogAuditoriaService(
            LogAuditoriaRepository logAuditoriaRepository) {

        this.logAuditoriaRepository = logAuditoriaRepository;
    }

    public List<LogAuditoria> listarTodos() {
        return logAuditoriaRepository.findAll();
    }

    public Optional<LogAuditoria> buscarPorId(Long id) {
        return logAuditoriaRepository.findById(id);
    }

    public List<LogAuditoria> listarPorUsuario(
            Long usuarioId) {

        return logAuditoriaRepository
                .findByUsuario_Id(usuarioId);
    }

    public List<LogAuditoria> listarPorEntidade(
            String entidade,
            Long entidadeId) {

        return logAuditoriaRepository
                .findByEntidadeAndEntidadeId(
                        entidade,
                        entidadeId
                );
    }

    public LogAuditoria registrar(
        Usuario usuario,
        String acao,
        String entidade,
        Long entidadeId,
        String detalhes) {

    LogAuditoria log = new LogAuditoria();

    log.setUsuario(usuario);
    log.setAcao(acao);
    log.setEntidade(entidade);
    log.setEntidadeId(entidadeId);
    log.setDetalhes(detalhes);

    return logAuditoriaRepository.save(log);
}
}