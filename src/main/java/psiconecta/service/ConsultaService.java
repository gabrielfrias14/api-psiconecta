package psiconecta.service;

import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import psiconecta.dto.consulta.AgendamentoConsultaDTO;
import psiconecta.dto.consulta.ConsultaResponseDTO;
import psiconecta.mapper.ConsultaMapper;
import psiconecta.model.Consulta;
import psiconecta.model.Disponibilidade;
import psiconecta.model.Estudante;
import psiconecta.model.Paciente;
import psiconecta.model.enums.ModalidadeAtendimento;
import psiconecta.model.enums.StatusAprovacaoEstudante;
import psiconecta.model.enums.StatusConsulta;
import psiconecta.repository.ConsultaRepository;
import psiconecta.repository.DisponibilidadeRepository;
import psiconecta.repository.EstudanteRepository;
import psiconecta.repository.PacienteRepository;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
public class ConsultaService {

        private final ConsultaRepository consultaRepository;
        private final PacienteRepository pacienteRepository;
        private final EstudanteRepository estudanteRepository;
        private final DisponibilidadeRepository disponibilidadeRepository;
        private final ConsultaMapper consultaMapper;

        public ConsultaService(
                        ConsultaRepository consultaRepository,
                        PacienteRepository pacienteRepository,
                        EstudanteRepository estudanteRepository,
                        DisponibilidadeRepository disponibilidadeRepository,
                        ConsultaMapper consultaMapper) {

                this.consultaRepository = consultaRepository;
                this.pacienteRepository = pacienteRepository;
                this.estudanteRepository = estudanteRepository;
                this.disponibilidadeRepository = disponibilidadeRepository;
                this.consultaMapper = consultaMapper;
        }

        public List<Consulta> listarTodas() {
                return consultaRepository.findAll();
        }

        public Optional<Consulta> buscarPorId(Long id) {
                return consultaRepository.findById(id);
        }

        public List<Consulta> listarPorPaciente(
                        Long pacienteId) {

                return consultaRepository
                                .findByPaciente_Id(pacienteId);
        }

        public List<Consulta> listarPorEstudante(
                        Long estudanteId) {

                return consultaRepository
                                .findByEstudante_Id(estudanteId);
        }

        public List<Consulta> listarPorPacienteEStatus(
                        Long pacienteId,
                        StatusConsulta status) {

                return consultaRepository
                                .findByPaciente_IdAndStatus(
                                                pacienteId,
                                                status);
        }

        public List<Consulta> listarPorEstudanteEStatus(
                        Long estudanteId,
                        StatusConsulta status) {

                return consultaRepository
                                .findByEstudante_IdAndStatus(
                                                estudanteId,
                                                status);
        }

        public List<Consulta> listarPorEstudanteEPeriodo(
                        Long estudanteId,
                        OffsetDateTime inicio,
                        OffsetDateTime fim) {

                return consultaRepository
                                .findByEstudante_IdAndDataHoraInicioBetween(
                                                estudanteId,
                                                inicio,
                                                fim);
        }

        public List<Consulta> listarPorPacienteEPeriodo(
                        Long pacienteId,
                        OffsetDateTime inicio,
                        OffsetDateTime fim) {

                return consultaRepository
                                .findByPaciente_IdAndDataHoraInicioBetween(
                                                pacienteId,
                                                inicio,
                                                fim);
        }

        @Transactional
        public ConsultaResponseDTO agendar(
                        Long usuarioId,
                        AgendamentoConsultaDTO dto) {

                Paciente paciente = pacienteRepository
                                .findByUsuario_Id(usuarioId)
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Paciente não encontrado."));

                Estudante estudante = estudanteRepository
                                .findById(dto.getEstudanteId())
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Estudante não encontrado."));

                if (estudante.getStatusAprovacao() != StatusAprovacaoEstudante.APROVADO) {

                        throw new IllegalArgumentException(
                                        "Este estudante ainda não está disponível para atendimentos.");
                }

                if (dto.getModalidade() == ModalidadeAtendimento.AMBOS) {

                        throw new IllegalArgumentException(
                                        "Escolha a modalidade ONLINE ou PRESENCIAL.");
                }

                OffsetDateTime inicio = dto.getDataHoraInicio();

                OffsetDateTime fim = inicio.plusMinutes(50);

                validarDisponibilidade(
                                estudante,
                                inicio,
                                fim,
                                dto.getModalidade());

                if (consultaRepository
                                .existeConflitoEstudante(
                                                estudante.getId(),
                                                inicio,
                                                fim,
                                                StatusConsulta.CANCELADA)) {

                        throw new IllegalArgumentException(
                                        "O estudante já possui uma consulta neste horário.");
                }

                if (consultaRepository.existeConflitoPaciente(
                                paciente.getId(),
                                inicio,
                                fim,
                                StatusConsulta.CANCELADA)) {

                        throw new IllegalArgumentException(
                                        "Você já possui uma consulta neste horário.");
                }

                Consulta consulta = new Consulta();

                consulta.setPaciente(paciente);
                consulta.setEstudante(estudante);
                consulta.setDataHoraInicio(inicio);
                consulta.setDataHoraFim(fim);
                consulta.setDuracaoMinutos(50);
                consulta.setModalidade(dto.getModalidade());
                consulta.setStatus(StatusConsulta.CONFIRMADA);
                consulta.setObservacao(dto.getObservacao());

                Consulta salva = consultaRepository.save(consulta);

                return consultaMapper.toResponse(salva);
        }

        private void validarDisponibilidade(
                        Estudante estudante,
                        OffsetDateTime inicio,
                        OffsetDateTime fim,
                        ModalidadeAtendimento modalidade) {

                ZoneId zonaLocal = ZoneId.of("America/Recife");

                ZonedDateTime inicioLocal = inicio.atZoneSameInstant(zonaLocal);

                ZonedDateTime fimLocal = fim.atZoneSameInstant(zonaLocal);

                short diaSemana = (short) inicioLocal
                                .getDayOfWeek()
                                .getValue();

                LocalTime horaInicio = inicioLocal.toLocalTime();

                LocalTime horaFim = fimLocal.toLocalTime();

                List<Disponibilidade> disponibilidades = disponibilidadeRepository
                                .findByEstudante_IdAndDiaSemanaAndAtivoTrue(
                                                estudante.getId(),
                                                diaSemana);

                boolean horarioDisponivel = disponibilidades
                                .stream()
                                .anyMatch(disponibilidade -> {

                                        boolean modalidadeCompativel = disponibilidade.getModalidade() == modalidade
                                                        || disponibilidade
                                                                        .getModalidade() == ModalidadeAtendimento.AMBOS;

                                        boolean inicioValido = !horaInicio.isBefore(
                                                        disponibilidade.getHoraInicio());

                                        boolean fimValido = !horaFim.isAfter(
                                                        disponibilidade.getHoraFim());

                                        return modalidadeCompativel
                                                        && inicioValido
                                                        && fimValido;
                                });

                if (!horarioDisponivel) {

                        throw new IllegalArgumentException(
                                        "O estudante não possui disponibilidade para o horário e modalidade escolhidos.");
                }
        }

        @Transactional(readOnly = true)
        public List<ConsultaResponseDTO> listarMinhasConsultas(
                        Long usuarioId) {

                Paciente paciente = pacienteRepository
                                .findByUsuario_Id(usuarioId)
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Paciente não encontrado."));

                return consultaRepository
                                .findByPaciente_Id(paciente.getId())
                                .stream()
                                .map(consultaMapper::toResponse)
                                .toList();
        }

        @Transactional(readOnly = true)
        public List<ConsultaResponseDTO> listarMinhasConsultasComoEstudante(
                        Long usuarioId) {

                Estudante estudante = estudanteRepository
                                .findByUsuario_Id(usuarioId)
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Estudante não encontrado."));

                return consultaRepository
                                .findByEstudante_Id(estudante.getId())
                                .stream()
                                .map(consultaMapper::toResponse)
                                .toList();
        }

        @Transactional
        public ConsultaResponseDTO cancelarComoPaciente(
                        Long usuarioId,
                        Long consultaId) {

                Paciente paciente = pacienteRepository
                                .findByUsuario_Id(usuarioId)
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Paciente não encontrado."));

                Consulta consulta = consultaRepository
                                .findById(consultaId)
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Consulta não encontrada."));

                if (!consulta.getPaciente()
                                .getId()
                                .equals(paciente.getId())) {

                        throw new IllegalArgumentException(
                                        "Esta consulta não pertence ao paciente autenticado.");
                }

                if (consulta.getStatus() != StatusConsulta.PENDENTE
                                && consulta.getStatus() != StatusConsulta.CONFIRMADA) {

                        throw new IllegalArgumentException(
                                        "Esta consulta não pode mais ser cancelada.");
                }

                if (!consulta.getDataHoraInicio()
                                .isAfter(OffsetDateTime.now())) {

                        throw new IllegalArgumentException(
                                        "Não é possível cancelar uma consulta que já iniciou ou ocorreu.");
                }

                consulta.setStatus(
                                StatusConsulta.CANCELADA);

                Consulta salva = consultaRepository.save(consulta);

                return consultaMapper.toResponse(salva);
        }

        @Transactional
        public ConsultaResponseDTO iniciarComoEstudante(
                        Long usuarioId,
                        Long consultaId) {

                Estudante estudante = estudanteRepository
                                .findByUsuario_Id(usuarioId)
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Estudante não encontrado."));

                Consulta consulta = consultaRepository
                                .findById(consultaId)
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Consulta não encontrada."));

                if (!consulta.getEstudante()
                                .getId()
                                .equals(estudante.getId())) {

                        throw new IllegalArgumentException(
                                        "Esta consulta não pertence ao estudante autenticado.");
                }

                if (consulta.getStatus() != StatusConsulta.CONFIRMADA) {

                        throw new IllegalArgumentException(
                                        "Somente consultas confirmadas podem ser iniciadas.");
                }

                if (consulta.getDataHoraInicio()
                                .isAfter(OffsetDateTime.now())) {

                        throw new IllegalArgumentException(
                                        "A consulta ainda não pode ser iniciada antes do horário agendado.");
                }

                consulta.setStatus(
                                StatusConsulta.EM_ANDAMENTO);

                Consulta salva = consultaRepository.save(consulta);

                return consultaMapper.toResponse(salva);
        }

        @Transactional
        public ConsultaResponseDTO concluirComoEstudante(
                        Long usuarioId,
                        Long consultaId) {

                Estudante estudante = estudanteRepository
                                .findByUsuario_Id(usuarioId)
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Estudante não encontrado."));

                Consulta consulta = consultaRepository
                                .findById(consultaId)
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Consulta não encontrada."));

                if (!consulta.getEstudante()
                                .getId()
                                .equals(estudante.getId())) {

                        throw new IllegalArgumentException(
                                        "Esta consulta não pertence ao estudante autenticado.");
                }

                if (consulta.getStatus() != StatusConsulta.EM_ANDAMENTO) {

                        throw new IllegalArgumentException(
                                        "Somente consultas em andamento podem ser concluídas.");
                }

                consulta.setStatus(
                                StatusConsulta.CONCLUIDA);

                Consulta salva = consultaRepository.save(consulta);

                return consultaMapper.toResponse(salva);
        }

        @Transactional
        public ConsultaResponseDTO marcarNaoCompareceu(
                        Long usuarioId,
                        Long consultaId) {

                Estudante estudante = estudanteRepository
                                .findByUsuario_Id(usuarioId)
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Estudante não encontrado."));

                Consulta consulta = consultaRepository
                                .findById(consultaId)
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Consulta não encontrada."));

                if (!consulta.getEstudante()
                                .getId()
                                .equals(estudante.getId())) {

                        throw new IllegalArgumentException(
                                        "Esta consulta não pertence ao estudante autenticado.");
                }

                if (consulta.getStatus() != StatusConsulta.CONFIRMADA) {

                        throw new IllegalArgumentException(
                                        "Somente consultas confirmadas podem ser marcadas como não compareceu.");
                }

                if (consulta.getDataHoraInicio()
                                .isAfter(OffsetDateTime.now())) {

                        throw new IllegalArgumentException(
                                        "Não é possível marcar ausência antes do horário da consulta.");
                }

                consulta.setStatus(
                                StatusConsulta.NAO_COMPARECEU);

                Consulta salva = consultaRepository.save(consulta);

                return consultaMapper.toResponse(salva);
        }
}