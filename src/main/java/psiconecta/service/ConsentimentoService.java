package psiconecta.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import psiconecta.dto.consentimento.ConsentimentoResponseDTO;
import psiconecta.dto.consentimento.RegistroConsentimentoDTO;
import psiconecta.model.Consentimento;
import psiconecta.model.Usuario;
import psiconecta.model.enums.TipoConsentimento;
import psiconecta.repository.ConsentimentoRepository;
import psiconecta.repository.UsuarioRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
public class ConsentimentoService {

        private final ConsentimentoRepository consentimentoRepository;
        private final UsuarioRepository usuarioRepository;

        public ConsentimentoService(
                        ConsentimentoRepository consentimentoRepository,
                        UsuarioRepository usuarioRepository) {

                this.consentimentoRepository = consentimentoRepository;
                this.usuarioRepository = usuarioRepository;
        }

        public List<Consentimento> listarTodos() {
                return consentimentoRepository.findAll();
        }

        public Optional<Consentimento> buscarPorId(Long id) {
                return consentimentoRepository.findById(id);
        }

        public List<Consentimento> listarPorUsuario(
                        Long usuarioId) {

                return consentimentoRepository
                                .findByUsuario_Id(usuarioId);
        }

        public List<Consentimento> listarPorUsuarioETipo(
                        Long usuarioId,
                        TipoConsentimento tipo) {

                return consentimentoRepository
                                .findByUsuario_IdAndTipo(
                                                usuarioId,
                                                tipo);
        }

        public boolean consentimentoJaAceito(
                        Long usuarioId,
                        TipoConsentimento tipo,
                        String versao) {

                return consentimentoRepository
                                .existsByUsuario_IdAndTipoAndVersaoAndAceitoTrue(
                                                usuarioId,
                                                tipo,
                                                versao);
        }

        @Transactional
        public ConsentimentoResponseDTO registrar(
                        Long usuarioId,
                        RegistroConsentimentoDTO dto) {

                Usuario usuario = usuarioRepository
                                .findById(usuarioId)
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Usuário não encontrado."));

                if (Boolean.TRUE.equals(dto.aceito())) {

                        boolean jaAceitou = consentimentoRepository
                                        .existsByUsuario_IdAndTipoAndVersaoAndAceitoTrue(
                                                        usuarioId,
                                                        dto.tipo(),
                                                        dto.versao().trim());

                        if (jaAceitou) {

                                throw new IllegalArgumentException(
                                                "Esta versão do consentimento já foi aceita pelo usuário.");
                        }
                }

                Consentimento consentimento = new Consentimento();

                consentimento.setUsuario(usuario);
                consentimento.setTipo(dto.tipo());
                consentimento.setVersao(
                                dto.versao().trim());
                consentimento.setAceito(
                                dto.aceito());

                Consentimento salvo = consentimentoRepository.save(
                                consentimento);

                return new ConsentimentoResponseDTO(
                                salvo.getId(),
                                salvo.getTipo(),
                                salvo.getVersao(),
                                salvo.getAceito(),
                                salvo.getRegistradoEm());
        }

        @Transactional(readOnly = true)
        public List<ConsentimentoResponseDTO> listarMeusConsentimentos(
                        Long usuarioId) {

                return consentimentoRepository
                                .findByUsuario_Id(usuarioId)
                                .stream()
                                .map(consentimento -> new ConsentimentoResponseDTO(
                                                consentimento.getId(),
                                                consentimento.getTipo(),
                                                consentimento.getVersao(),
                                                consentimento.getAceito(),
                                                consentimento.getRegistradoEm()))
                                .toList();
        }
}