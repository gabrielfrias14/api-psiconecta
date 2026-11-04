package psiconecta.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import psiconecta.dto.estudante.EstudanteResponseDTO;
import psiconecta.service.EstudanteService;

import org.springframework.web.bind.annotation.PathVariable;

import psiconecta.dto.disponibilidade.DisponibilidadeResponseDTO;
import psiconecta.service.DisponibilidadeService;

import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/paciente/estudantes")
public class PacienteEstudanteController {

    private final EstudanteService estudanteService;
    private final DisponibilidadeService disponibilidadeService;

    public PacienteEstudanteController(
            EstudanteService estudanteService,
            DisponibilidadeService disponibilidadeService) {

        this.estudanteService = estudanteService;
        this.disponibilidadeService = disponibilidadeService;
    }

    @GetMapping
    public ResponseEntity<List<EstudanteResponseDTO>> listarAprovados(
            @RequestParam(required = false) Long areaId) {

        if (areaId != null) {

            return ResponseEntity.ok(
                    estudanteService.listarAprovadosPorArea(
                            areaId));
        }

        return ResponseEntity.ok(
                estudanteService.listarAprovadosResponse());
    }

    @GetMapping("/{id}/disponibilidades")
    public ResponseEntity<List<DisponibilidadeResponseDTO>> listarDisponibilidades(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                disponibilidadeService
                        .listarDisponibilidadesPublicas(id));
    }
}