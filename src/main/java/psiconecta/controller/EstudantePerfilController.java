package psiconecta.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import psiconecta.dto.estudante.EstudanteResponseDTO;
import psiconecta.model.Usuario;
import psiconecta.service.EstudanteService;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;

import psiconecta.dto.estudante.AtualizacaoEstudanteDTO;

@RestController
@RequestMapping("/api/estudante/perfil")
public class EstudantePerfilController {

        private final EstudanteService estudanteService;

        public EstudantePerfilController(
                        EstudanteService estudanteService) {

                this.estudanteService = estudanteService;
        }

        @GetMapping
        public ResponseEntity<EstudanteResponseDTO> buscarPerfil(
                        Authentication authentication) {

                Usuario usuario = (Usuario) authentication.getPrincipal();

                return ResponseEntity.ok(
                                estudanteService.buscarMeuPerfil(
                                                usuario.getId()));
        }

        @PutMapping
        public ResponseEntity<EstudanteResponseDTO> atualizarPerfil(
                        @Valid @RequestBody AtualizacaoEstudanteDTO dto,
                        Authentication authentication) {

                Usuario usuario = (Usuario) authentication.getPrincipal();

                return ResponseEntity.ok(
                                estudanteService.atualizarMeuPerfil(
                                                usuario.getId(),
                                                dto));
        }
}