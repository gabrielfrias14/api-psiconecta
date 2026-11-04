package psiconecta.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import psiconecta.dto.consentimento.ConsentimentoResponseDTO;
import psiconecta.dto.consentimento.RegistroConsentimentoDTO;
import psiconecta.model.Usuario;
import psiconecta.service.ConsentimentoService;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/consentimentos")
public class ConsentimentoController {

        private final ConsentimentoService consentimentoService;

        public ConsentimentoController(
                        ConsentimentoService consentimentoService) {

                this.consentimentoService = consentimentoService;
        }

        @PostMapping
        public ResponseEntity<ConsentimentoResponseDTO> registrar(
                        @Valid @RequestBody RegistroConsentimentoDTO dto,
                        Authentication authentication) {

                Usuario usuario = (Usuario) authentication.getPrincipal();

                ConsentimentoResponseDTO resposta = consentimentoService.registrar(
                                usuario.getId(),
                                dto);

                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(resposta);
        }

        @GetMapping
        public ResponseEntity<List<ConsentimentoResponseDTO>> listarMeusConsentimentos(
                        Authentication authentication) {

                Usuario usuario = (Usuario) authentication.getPrincipal();

                return ResponseEntity.ok(
                                consentimentoService
                                                .listarMeusConsentimentos(
                                                                usuario.getId()));
        }
}