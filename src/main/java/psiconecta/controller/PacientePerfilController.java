package psiconecta.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import psiconecta.dto.paciente.PacienteResponseDTO;
import psiconecta.model.Usuario;
import psiconecta.service.PacienteService;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;

import psiconecta.dto.paciente.AtualizacaoPacienteDTO;

@RestController
@RequestMapping("/api/paciente/perfil")
public class PacientePerfilController {

        private final PacienteService pacienteService;

        public PacientePerfilController(
                        PacienteService pacienteService) {

                this.pacienteService = pacienteService;
        }

        @GetMapping
        public ResponseEntity<PacienteResponseDTO> buscarPerfil(
                        Authentication authentication) {

                Usuario usuario = (Usuario) authentication.getPrincipal();

                return ResponseEntity.ok(
                                pacienteService.buscarMeuPerfil(
                                                usuario.getId()));
        }

        @PutMapping
        public ResponseEntity<PacienteResponseDTO> atualizarPerfil(
                        @Valid @RequestBody AtualizacaoPacienteDTO dto,
                        Authentication authentication) {

                Usuario usuario = (Usuario) authentication.getPrincipal();

                return ResponseEntity.ok(
                                pacienteService.atualizarMeuPerfil(
                                                usuario.getId(),
                                                dto));
        }
}