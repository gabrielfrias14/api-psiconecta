package psiconecta.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import psiconecta.dto.consulta.ConsultaResponseDTO;
import psiconecta.model.Usuario;
import psiconecta.service.ConsultaService;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/estudante/consultas")
public class EstudanteConsultaController {

        private final ConsultaService consultaService;

        public EstudanteConsultaController(
                        ConsultaService consultaService) {

                this.consultaService = consultaService;
        }

        @GetMapping
        public ResponseEntity<List<ConsultaResponseDTO>> listarMinhasConsultas(
                        Authentication authentication) {

                Usuario usuario = (Usuario) authentication.getPrincipal();

                return ResponseEntity.ok(
                                consultaService
                                                .listarMinhasConsultasComoEstudante(
                                                                usuario.getId()));
        }

        @PutMapping("/{id}/iniciar")
        public ResponseEntity<ConsultaResponseDTO> iniciar(
                        @PathVariable Long id,
                        Authentication authentication) {

                Usuario usuario = (Usuario) authentication.getPrincipal();

                return ResponseEntity.ok(
                                consultaService.iniciarComoEstudante(
                                                usuario.getId(),
                                                id));
        }

        @PutMapping("/{id}/concluir")
        public ResponseEntity<ConsultaResponseDTO> concluir(
                        @PathVariable Long id,
                        Authentication authentication) {

                Usuario usuario = (Usuario) authentication.getPrincipal();

                return ResponseEntity.ok(
                                consultaService.concluirComoEstudante(
                                                usuario.getId(),
                                                id));
        }

        @PutMapping("/{id}/nao-compareceu")
        public ResponseEntity<ConsultaResponseDTO> marcarNaoCompareceu(
                        @PathVariable Long id,
                        Authentication authentication) {

                Usuario usuario = (Usuario) authentication.getPrincipal();

                return ResponseEntity.ok(
                                consultaService.marcarNaoCompareceu(
                                                usuario.getId(),
                                                id));
        }
}