package psiconecta.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import psiconecta.dto.estudante.EstudanteResponseDTO;
import psiconecta.model.Usuario;
import psiconecta.service.EstudanteService;

@RestController
@RequestMapping("/api/admin/estudantes")
public class AdminEstudanteController {

    private final EstudanteService estudanteService;

    public AdminEstudanteController(
            EstudanteService estudanteService) {

        this.estudanteService = estudanteService;
    }

    @GetMapping("/pendentes")
    public ResponseEntity<List<EstudanteResponseDTO>>
            listarPendentes() {

        return ResponseEntity.ok(
                estudanteService.listarPendentesResponse()
        );
    }

    @PutMapping("/{id}/aprovar")
    public ResponseEntity<EstudanteResponseDTO> aprovar(
            @PathVariable Long id,
            Authentication authentication) {

        Usuario administrador =
                (Usuario) authentication.getPrincipal();

        EstudanteResponseDTO resposta =
                estudanteService.aprovar(
                        id,
                        administrador.getId()
                );

        return ResponseEntity.ok(resposta);
    }

    @PutMapping("/{id}/rejeitar")
    public ResponseEntity<EstudanteResponseDTO> rejeitar(
            @PathVariable Long id,
            Authentication authentication) {

        Usuario administrador =
                (Usuario) authentication.getPrincipal();

        EstudanteResponseDTO resposta =
                estudanteService.rejeitar(
                        id,
                        administrador.getId()
                );

        return ResponseEntity.ok(resposta);
    }
}