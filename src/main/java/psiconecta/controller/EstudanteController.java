package psiconecta.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import psiconecta.dto.estudante.CadastroEstudanteDTO;
import psiconecta.dto.estudante.EstudanteResponseDTO;
import psiconecta.service.EstudanteService;

@RestController
@RequestMapping("/api/estudantes")
public class EstudanteController {

    private final EstudanteService estudanteService;

    public EstudanteController(
            EstudanteService estudanteService) {

        this.estudanteService = estudanteService;
    }

    @PostMapping
    public ResponseEntity<EstudanteResponseDTO> cadastrar(
            @Valid @RequestBody CadastroEstudanteDTO dto) {

        EstudanteResponseDTO estudante =
                estudanteService.cadastrar(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(estudante);
    }
}