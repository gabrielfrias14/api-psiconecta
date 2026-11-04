package psiconecta.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import psiconecta.dto.disponibilidade.CadastroDisponibilidadeDTO;
import psiconecta.dto.disponibilidade.DisponibilidadeResponseDTO;
import psiconecta.model.Usuario;
import psiconecta.service.DisponibilidadeService;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/estudante/disponibilidades")
public class EstudanteDisponibilidadeController {

        private final DisponibilidadeService disponibilidadeService;

        public EstudanteDisponibilidadeController(
                        DisponibilidadeService disponibilidadeService) {

                this.disponibilidadeService = disponibilidadeService;
        }

        @PostMapping
        public ResponseEntity<DisponibilidadeResponseDTO> cadastrar(
                        @Valid @RequestBody CadastroDisponibilidadeDTO dto,
                        Authentication authentication) {

                Usuario usuario = (Usuario) authentication.getPrincipal();

                DisponibilidadeResponseDTO resposta = disponibilidadeService.cadastrar(
                                usuario.getId(),
                                dto);

                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(resposta);
        }

        @GetMapping
        public ResponseEntity<List<DisponibilidadeResponseDTO>> listarMinhasDisponibilidades(
                        Authentication authentication) {

                Usuario usuario = (Usuario) authentication.getPrincipal();

                return ResponseEntity.ok(
                                disponibilidadeService
                                                .listarMinhasDisponibilidades(
                                                                usuario.getId()));
        }

        @PutMapping("/{id}/desativar")
        public ResponseEntity<DisponibilidadeResponseDTO> desativar(
                        @PathVariable Long id,
                        Authentication authentication) {

                Usuario usuario = (Usuario) authentication.getPrincipal();

                DisponibilidadeResponseDTO resposta = disponibilidadeService.desativar(
                                usuario.getId(),
                                id);

                return ResponseEntity.ok(resposta);
        }
}