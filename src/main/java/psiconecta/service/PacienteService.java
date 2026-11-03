package psiconecta.service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import psiconecta.dto.paciente.AtualizacaoPacienteDTO;
import psiconecta.dto.paciente.CadastroPacienteDTO;
import psiconecta.dto.paciente.PacienteResponseDTO;
import psiconecta.mapper.PacienteMapper;
import psiconecta.model.Paciente;
import psiconecta.model.Usuario;
import psiconecta.model.enums.PerfilUsuario;
import psiconecta.repository.PacienteRepository;
import psiconecta.repository.UsuarioRepository;

@Service
public class PacienteService {

        private final PacienteRepository pacienteRepository;
        private final UsuarioRepository usuarioRepository;
        private final PasswordEncoder passwordEncoder;
        private final PacienteMapper pacienteMapper;

        public PacienteService(
                        PacienteRepository pacienteRepository,
                        UsuarioRepository usuarioRepository,
                        PasswordEncoder passwordEncoder,
                        PacienteMapper pacienteMapper) {

                this.pacienteRepository = pacienteRepository;
                this.usuarioRepository = usuarioRepository;
                this.passwordEncoder = passwordEncoder;
                this.pacienteMapper = pacienteMapper;
        }

        public List<Paciente> listarTodos() {
                return pacienteRepository.findAll();
        }

        public Optional<Paciente> buscarPorId(Long id) {
                return pacienteRepository.findById(id);
        }

        public Optional<Paciente> buscarPorUsuarioId(
                        Long usuarioId) {

                return pacienteRepository
                                .findByUsuario_Id(usuarioId);
        }

        public Optional<Paciente> buscarPorCpf(String cpf) {
                return pacienteRepository.findByCpf(cpf);
        }

        public boolean cpfJaCadastrado(String cpf) {
                return pacienteRepository.existsByCpf(cpf);
        }

        @Transactional
        public PacienteResponseDTO cadastrar(
                        CadastroPacienteDTO dto) {

                String email = dto.getUsuario()
                                .getEmail()
                                .trim()
                                .toLowerCase(Locale.ROOT);

                String cpf = dto.getCpf();

                if (usuarioRepository.existsByEmail(email)) {
                        throw new IllegalArgumentException(
                                        "E-mail já cadastrado.");
                }

                if (cpf != null
                                && !cpf.isBlank()
                                && pacienteRepository.existsByCpf(cpf)) {

                        throw new IllegalArgumentException(
                                        "CPF já cadastrado.");
                }

                Usuario usuario = new Usuario();

                usuario.setNome(
                                dto.getUsuario().getNome().trim());

                usuarioRepository.save(usuario);

                usuario.setEmail(email);

                usuario.setSenha(
                                passwordEncoder.encode(
                                                dto.getUsuario().getSenha()));

                usuario.setPerfil(
                                PerfilUsuario.PACIENTE);

                usuario.setAtivo(true);

                Usuario usuarioSalvo = usuarioRepository.save(usuario);

                Paciente paciente = new Paciente();

                paciente.setUsuario(usuarioSalvo);
                paciente.setCpf(cpf);
                paciente.setDataNascimento(
                                dto.getDataNascimento());
                paciente.setTelefone(
                                dto.getTelefone());

                Paciente pacienteSalvo = pacienteRepository.save(paciente);

                return pacienteMapper.toResponse(
                                pacienteSalvo);
        }

        @Transactional(readOnly = true)
        public PacienteResponseDTO buscarMeuPerfil(
                        Long usuarioId) {

                Paciente paciente = pacienteRepository
                                .findByUsuario_Id(usuarioId)
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Paciente não encontrado."));

                return pacienteMapper.toResponse(paciente);
        }

        @Transactional
        public PacienteResponseDTO atualizarMeuPerfil(
                        Long usuarioId,
                        AtualizacaoPacienteDTO dto) {

                Paciente paciente = pacienteRepository
                                .findByUsuario_Id(usuarioId)
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Paciente não encontrado."));

                Usuario usuario = paciente.getUsuario();

                usuario.setNome(
                                dto.nome().trim());

                paciente.setDataNascimento(
                                dto.dataNascimento());

                paciente.setTelefone(
                                dto.telefone());

                Paciente salvo = pacienteRepository.save(paciente);

                return pacienteMapper.toResponse(salvo);
        }
}