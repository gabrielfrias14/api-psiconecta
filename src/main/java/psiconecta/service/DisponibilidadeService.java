package psiconecta.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import psiconecta.dto.disponibilidade.CadastroDisponibilidadeDTO;
import psiconecta.dto.disponibilidade.DisponibilidadeResponseDTO;
import psiconecta.mapper.DisponibilidadeMapper;
import psiconecta.model.Disponibilidade;
import psiconecta.model.Estudante;
import psiconecta.model.enums.ModalidadeAtendimento;
import psiconecta.model.enums.StatusAprovacaoEstudante;
import psiconecta.repository.DisponibilidadeRepository;
import psiconecta.repository.EstudanteRepository;

@Service
public class DisponibilidadeService {

        private final DisponibilidadeRepository disponibilidadeRepository;
        private final EstudanteRepository estudanteRepository;
        private final DisponibilidadeMapper disponibilidadeMapper;

        public DisponibilidadeService(
                        DisponibilidadeRepository disponibilidadeRepository,
                        EstudanteRepository estudanteRepository,
                        DisponibilidadeMapper disponibilidadeMapper) {

                this.disponibilidadeRepository = disponibilidadeRepository;
                this.estudanteRepository = estudanteRepository;
                this.disponibilidadeMapper = disponibilidadeMapper;
        }

        public Optional<Disponibilidade> buscarPorId(Long id) {
                return disponibilidadeRepository.findById(id);
        }

        public List<Disponibilidade> listarPorEstudante(
                        Long estudanteId) {

                return disponibilidadeRepository
                                .findByEstudante_Id(estudanteId);
        }

        public List<Disponibilidade> listarAtivasPorEstudante(
                        Long estudanteId) {

                return disponibilidadeRepository
                                .findByEstudante_IdAndAtivoTrue(estudanteId);
        }

        public List<Disponibilidade> listarAtivasPorEstudanteEDia(
                        Long estudanteId,
                        Short diaSemana) {

                return disponibilidadeRepository
                                .findByEstudante_IdAndDiaSemanaAndAtivoTrue(
                                                estudanteId,
                                                diaSemana);
        }

        @Transactional
        public DisponibilidadeResponseDTO cadastrar(
                        Long usuarioId,
                        CadastroDisponibilidadeDTO dto) {

                Estudante estudante = estudanteRepository
                                .findByUsuario_Id(usuarioId)
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Estudante não encontrado."));

                if (estudante.getStatusAprovacao() != StatusAprovacaoEstudante.APROVADO) {

                        throw new IllegalArgumentException(
                                        "Somente estudantes aprovados podem cadastrar disponibilidades.");
                }

                validarModalidade(
                                estudante,
                                dto.modalidade());

                Disponibilidade disponibilidade = new Disponibilidade();

                disponibilidade.setEstudante(estudante);
                disponibilidade.setDiaSemana(dto.diaSemana());
                disponibilidade.setHoraInicio(dto.horaInicio());
                disponibilidade.setHoraFim(dto.horaFim());
                disponibilidade.setModalidade(dto.modalidade());
                disponibilidade.setAtivo(true);

                Disponibilidade salva = disponibilidadeRepository.save(
                                disponibilidade);

                return disponibilidadeMapper
                                .toResponse(salva);
        }

        private void validarModalidade(
                        Estudante estudante,
                        ModalidadeAtendimento modalidade) {

                ModalidadeAtendimento modalidadeEstudante = estudante.getModalidade();

                if (modalidade == ModalidadeAtendimento.AMBOS) {

                        throw new IllegalArgumentException(
                                        "A disponibilidade deve ser ONLINE ou PRESENCIAL.");
                }

                if (modalidadeEstudante == ModalidadeAtendimento.AMBOS) {

                        return;
                }

                if (modalidadeEstudante != modalidade) {

                        throw new IllegalArgumentException(
                                        "A modalidade informada não está disponível para este estudante.");
                }
        }

        @Transactional(readOnly = true)
        public List<DisponibilidadeResponseDTO> listarMinhasDisponibilidades(
                        Long usuarioId) {

                Estudante estudante = estudanteRepository
                                .findByUsuario_Id(usuarioId)
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Estudante não encontrado."));

                return disponibilidadeRepository
                                .findByEstudante_Id(estudante.getId())
                                .stream()
                                .map(disponibilidadeMapper::toResponse)
                                .toList();
        }

        @Transactional
        public DisponibilidadeResponseDTO desativar(
                        Long usuarioId,
                        Long disponibilidadeId) {

                Disponibilidade disponibilidade = disponibilidadeRepository
                                .findById(disponibilidadeId)
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Disponibilidade não encontrada."));

                Long usuarioDonoId = disponibilidade
                                .getEstudante()
                                .getUsuario()
                                .getId();

                if (!usuarioDonoId.equals(usuarioId)) {

                        throw new IllegalArgumentException(
                                        "Esta disponibilidade não pertence ao estudante autenticado.");
                }

                disponibilidade.setAtivo(false);

                Disponibilidade salva = disponibilidadeRepository.save(disponibilidade);

                return disponibilidadeMapper.toResponse(salva);
        }

        @Transactional(readOnly = true)
        public List<DisponibilidadeResponseDTO> listarDisponibilidadesPublicas(Long estudanteId) {

                Estudante estudante = estudanteRepository
                                .findById(estudanteId)
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Estudante não encontrado."));

                if (estudante.getStatusAprovacao() != StatusAprovacaoEstudante.APROVADO) {

                        throw new IllegalArgumentException(
                                        "Este estudante não está disponível para atendimentos.");
                }

                return disponibilidadeRepository
                                .findByEstudante_IdAndAtivoTrue(
                                                estudante.getId())
                                .stream()
                                .map(disponibilidadeMapper::toResponse)
                                .toList();
        }
}