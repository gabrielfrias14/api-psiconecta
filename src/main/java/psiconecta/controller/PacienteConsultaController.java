package psiconecta.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import psiconecta.dto.consulta.AgendamentoConsultaDTO;
import psiconecta.dto.consulta.ConsultaResponseDTO;
import psiconecta.model.Usuario;
import psiconecta.service.ConsultaService;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/paciente/consultas")
public class PacienteConsultaController {

        private final ConsultaService consultaService;

        public PacienteConsultaController(
                        ConsultaService consultaService) {

                this.consultaService = consultaService;
        }

        @PostMapping
        public ResponseEntity<ConsultaResponseDTO> agendar(
                        @Valid @RequestBody AgendamentoConsultaDTO dto,
                        Authentication authentication) {

                Usuario usuario = (Usuario) authentication.getPrincipal();

                ConsultaResponseDTO resposta = consultaService.agendar(
                                usuario.getId(),
                                dto);

                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(resposta);
        }

        @GetMapping
        public ResponseEntity<List<ConsultaResponseDTO>> listarMinhasConsultas(
                        Authentication authentication) {

                Usuario usuario = (Usuario) authentication.getPrincipal();

                return ResponseEntity.ok(
                                consultaService.listarMinhasConsultas(
                                                usuario.getId()));
        }

        @PutMapping("/{id}/cancelar")
        public ResponseEntity<ConsultaResponseDTO> cancelar(
                        @PathVariable Long id,
                        Authentication authentication) {

                Usuario usuario = (Usuario) authentication.getPrincipal();

                return ResponseEntity.ok(
                                consultaService.cancelarComoPaciente(
                                                usuario.getId(),
                                                id));
        }
}