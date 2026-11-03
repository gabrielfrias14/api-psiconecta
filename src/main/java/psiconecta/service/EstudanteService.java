package psiconecta.service;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import psiconecta.dto.estudante.AtualizacaoEstudanteDTO;
import psiconecta.dto.estudante.CadastroEstudanteDTO;
import psiconecta.dto.estudante.EstudanteResponseDTO;
import psiconecta.mapper.EstudanteMapper;
import psiconecta.model.AreaAtendimento;
import psiconecta.model.Estudante;
import psiconecta.model.Usuario;
import psiconecta.model.enums.PerfilUsuario;
import psiconecta.model.enums.StatusAprovacaoEstudante;
import psiconecta.repository.AreaAtendimentoRepository;
import psiconecta.repository.EstudanteRepository;
import psiconecta.repository.UsuarioRepository;
import java.time.OffsetDateTime;

@Service
public class EstudanteService {

        private final EstudanteRepository estudanteRepository;
        private final UsuarioRepository usuarioRepository;
        private final AreaAtendimentoRepository areaAtendimentoRepository;
        private final PasswordEncoder passwordEncoder;
        private final EstudanteMapper estudanteMapper;
        private final LogAuditoriaService logAuditoriaService;

        public EstudanteService(
                        EstudanteRepository estudanteRepository,
                        UsuarioRepository usuarioRepository,
                        AreaAtendimentoRepository areaAtendimentoRepository,
                        PasswordEncoder passwordEncoder,
                        EstudanteMapper estudanteMapper,
                        LogAuditoriaService logAuditoriaService) {

                this.estudanteRepository = estudanteRepository;
                this.usuarioRepository = usuarioRepository;
                this.areaAtendimentoRepository = areaAtendimentoRepository;
                this.passwordEncoder = passwordEncoder;
                this.estudanteMapper = estudanteMapper;
                this.logAuditoriaService = logAuditoriaService;
        }

        public List<Estudante> listarTodos() {
                return estudanteRepository.findAll();
        }

        public Optional<Estudante> buscarPorId(Long id) {
                return estudanteRepository.findById(id);
        }

        public Optional<Estudante> buscarPorUsuarioId(
                        Long usuarioId) {

                return estudanteRepository
                                .findByUsuario_Id(usuarioId);
        }

        public Optional<Estudante> buscarPorUniversidadeEMatricula(
                        String universidade,
                        String matricula) {

                return estudanteRepository
                                .findByUniversidadeIgnoreCaseAndMatricula(
                                                universidade,
                                                matricula);
        }

        public boolean vinculoAcademicoJaCadastrado(
                        String universidade,
                        String matricula) {

                return estudanteRepository
                                .existsByUniversidadeIgnoreCaseAndMatricula(
                                                universidade,
                                                matricula);
        }

        public List<Estudante> listarAprovados() {

                return estudanteRepository.findByStatusAprovacao(
                                StatusAprovacaoEstudante.APROVADO);
        }

        public List<Estudante> listarPendentes() {

                return estudanteRepository.findByStatusAprovacao(
                                StatusAprovacaoEstudante.PENDENTE);
        }

        @Transactional
        public EstudanteResponseDTO cadastrar(
                        CadastroEstudanteDTO dto) {

                String email = dto.getUsuario()
                                .getEmail()
                                .trim()
                                .toLowerCase(Locale.ROOT);

                String universidade = dto.getUniversidade().trim();

                String matricula = dto.getMatricula().trim();

                if (usuarioRepository.existsByEmail(email)) {

                        throw new IllegalArgumentException(
                                        "E-mail já cadastrado.");
                }

                if (estudanteRepository
                                .existsByUniversidadeIgnoreCaseAndMatricula(
                                                universidade,
                                                matricula)) {

                        throw new IllegalArgumentException(
                                        "Esta matrícula já está cadastrada para essa universidade.");
                }

                if (!Boolean.TRUE.equals(
                                dto.getEstagioSupervisionado())) {

                        throw new IllegalArgumentException(
                                        "É necessário estar em estágio supervisionado.");
                }

                Set<Long> areasIds = dto.getAreasAtendimentoIds();

                List<AreaAtendimento> areasEncontradas = areaAtendimentoRepository
                                .findAllById(areasIds);

                if (areasEncontradas.size() != areasIds.size()) {

                        throw new IllegalArgumentException(
                                        "Uma ou mais áreas de atendimento informadas não existem.");
                }

                boolean existeAreaInativa = areasEncontradas
                                .stream()
                                .anyMatch(area -> !Boolean.TRUE.equals(
                                                area.getAtivo()));

                if (existeAreaInativa) {

                        throw new IllegalArgumentException(
                                        "Uma ou mais áreas de atendimento estão inativas.");
                }

                Usuario usuario = new Usuario();

                usuario.setNome(
                                dto.getUsuario()
                                                .getNome()
                                                .trim());

                usuario.setEmail(email);

                usuario.setSenha(
                                passwordEncoder.encode(
                                                dto.getUsuario().getSenha()));

                usuario.setPerfil(
                                PerfilUsuario.ESTUDANTE);

                usuario.setAtivo(true);

                Usuario usuarioSalvo = usuarioRepository.save(usuario);

                Estudante estudante = new Estudante();

                estudante.setUsuario(usuarioSalvo);
                estudante.setUniversidade(universidade);
                estudante.setMatricula(matricula);
                estudante.setPeriodo(dto.getPeriodo());

                estudante.setEstagioSupervisionado(true);

                estudante.setBio(dto.getBio());

                estudante.setModalidade(
                                dto.getModalidade());

                estudante.setStatusAprovacao(
                                StatusAprovacaoEstudante.PENDENTE);

                estudante.setAreasAtendimento(
                                new HashSet<>(areasEncontradas));

                Estudante estudanteSalvo = estudanteRepository.save(estudante);

                return estudanteMapper.toResponse(
                                estudanteSalvo);
        }

        @Transactional
        public EstudanteResponseDTO aprovar(
                        Long estudanteId,
                        Long administradorUsuarioId) {

                Estudante estudante = buscarEstudantePendente(estudanteId);

                Usuario administrador = buscarAdministrador(administradorUsuarioId);

                estudante.setStatusAprovacao(
                                StatusAprovacaoEstudante.APROVADO);

                estudante.setAvaliadoPorUsuario(administrador);
                estudante.setAvaliadoEm(OffsetDateTime.now());

                Estudante estudanteSalvo = estudanteRepository.save(estudante);

                logAuditoriaService.registrar(
                                administrador,
                                "APROVAR_ESTUDANTE",
                                "ESTUDANTE",
                                estudante.getId(),
                                "Estudante aprovado pelo administrador.");

                return estudanteMapper.toResponse(estudanteSalvo);
        }

        @Transactional
        public EstudanteResponseDTO rejeitar(
                        Long estudanteId,
                        Long administradorUsuarioId) {

                Estudante estudante = buscarEstudantePendente(estudanteId);

                Usuario administrador = buscarAdministrador(administradorUsuarioId);

                estudante.setStatusAprovacao(
                                StatusAprovacaoEstudante.REJEITADO);

                estudante.setAvaliadoPorUsuario(administrador);
                estudante.setAvaliadoEm(OffsetDateTime.now());

                Estudante estudanteSalvo = estudanteRepository.save(estudante);

                logAuditoriaService.registrar(
                                administrador,
                                "REJEITAR_ESTUDANTE",
                                "ESTUDANTE",
                                estudante.getId(),
                                "Estudante rejeitado pelo administrador.");

                return estudanteMapper.toResponse(estudanteSalvo);
        }

        private Estudante buscarEstudantePendente(
                        Long estudanteId) {

                Estudante estudante = estudanteRepository.findById(estudanteId)
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Estudante não encontrado."));

                if (estudante.getStatusAprovacao() != StatusAprovacaoEstudante.PENDENTE) {

                        throw new IllegalArgumentException(
                                        "O estudante já foi avaliado.");
                }

                return estudante;
        }

        private Usuario buscarAdministrador(
                        Long administradorUsuarioId) {

                Usuario administrador = usuarioRepository
                                .findById(administradorUsuarioId)
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Administrador não encontrado."));

                if (administrador.getPerfil() != PerfilUsuario.ADMIN) {

                        throw new IllegalArgumentException(
                                        "O usuário informado não é administrador.");
                }

                if (!Boolean.TRUE.equals(
                                administrador.getAtivo())) {

                        throw new IllegalArgumentException(
                                        "Administrador inativo.");
                }

                return administrador;
        }

        @Transactional(readOnly = true)
        public List<EstudanteResponseDTO> listarPendentesResponse() {

                return estudanteRepository
                                .findByStatusAprovacao(
                                                StatusAprovacaoEstudante.PENDENTE)
                                .stream()
                                .map(estudanteMapper::toResponse)
                                .toList();
        }

        @Transactional(readOnly = true)
        public List<EstudanteResponseDTO> listarAprovadosResponse() {

                return estudanteRepository
                                .findByStatusAprovacao(
                                                StatusAprovacaoEstudante.APROVADO)
                                .stream()
                                .map(estudanteMapper::toResponse)
                                .toList();
        }

        @Transactional(readOnly = true)
        public List<EstudanteResponseDTO> listarAprovadosPorArea(
                        Long areaId) {

                return estudanteRepository
                                .findDistinctByStatusAprovacaoAndAreasAtendimento_Id(
                                                StatusAprovacaoEstudante.APROVADO,
                                                areaId)
                                .stream()
                                .map(estudanteMapper::toResponse)
                                .toList();
        }

        @Transactional(readOnly = true)
        public EstudanteResponseDTO buscarMeuPerfil(
                        Long usuarioId) {

                Estudante estudante = estudanteRepository
                                .findByUsuario_Id(usuarioId)
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Estudante não encontrado."));

                return estudanteMapper.toResponse(estudante);
        }

        @Transactional
        public EstudanteResponseDTO atualizarMeuPerfil(
                        Long usuarioId,
                        AtualizacaoEstudanteDTO dto) {

                Estudante estudante = estudanteRepository
                                .findByUsuario_Id(usuarioId)
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Estudante não encontrado."));

                Usuario usuario = estudante.getUsuario();

                usuario.setNome(
                                dto.nome().trim());

                usuarioRepository.save(usuario);

                List<AreaAtendimento> areas = areaAtendimentoRepository
                                .findAllById(
                                                dto.areasAtendimentoIds());

                if (areas.size() != dto.areasAtendimentoIds().size()) {

                        throw new IllegalArgumentException(
                                        "Uma ou mais áreas de atendimento não foram encontradas.");
                }

                boolean possuiAreaInativa = areas.stream()
                                .anyMatch(area -> !Boolean.TRUE.equals(
                                                area.getAtivo()));

                if (possuiAreaInativa) {

                        throw new IllegalArgumentException(
                                        "Uma ou mais áreas de atendimento estão inativas.");
                }

                estudante.setPeriodo(
                                dto.periodo());

                estudante.setBio(
                                dto.bio());

                estudante.setModalidade(
                                dto.modalidade());

                estudante.setAreasAtendimento(
                                new HashSet<>(areas));

                Estudante salvo = estudanteRepository.save(estudante);

                return estudanteMapper.toResponse(salvo);
        }

}