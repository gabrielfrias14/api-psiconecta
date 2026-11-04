package psiconecta.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import psiconecta.dto.paciente.CadastroPacienteDTO;
import psiconecta.dto.paciente.PacienteResponseDTO;
import psiconecta.service.PacienteService;

@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {

    private final PacienteService pacienteService;

    public PacienteController(
            PacienteService pacienteService) {

        this.pacienteService = pacienteService;
    }

    @PostMapping
    public ResponseEntity<PacienteResponseDTO> cadastrar(
            @Valid @RequestBody CadastroPacienteDTO dto) {

        PacienteResponseDTO paciente =
                pacienteService.cadastrar(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(paciente);
    }
}