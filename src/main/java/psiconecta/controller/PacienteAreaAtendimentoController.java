package psiconecta.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import psiconecta.dto.areaatendimento.AreaAtendimentoResponseDTO;
import psiconecta.mapper.AreaAtendimentoMapper;
import psiconecta.service.AreaAtendimentoService;

@RestController
@RequestMapping("/api/paciente/areas-atendimento")
public class PacienteAreaAtendimentoController {

    private final AreaAtendimentoService areaAtendimentoService;
    private final AreaAtendimentoMapper areaAtendimentoMapper;

    public PacienteAreaAtendimentoController(
            AreaAtendimentoService areaAtendimentoService,
            AreaAtendimentoMapper areaAtendimentoMapper) {

        this.areaAtendimentoService = areaAtendimentoService;
        this.areaAtendimentoMapper = areaAtendimentoMapper;
    }

    @GetMapping
    public ResponseEntity<List<AreaAtendimentoResponseDTO>>
            listarAtivas() {

        List<AreaAtendimentoResponseDTO> resposta =
                areaAtendimentoService
                        .listarAtivas()
                        .stream()
                        .map(areaAtendimentoMapper::toResponse)
                        .toList();

        return ResponseEntity.ok(resposta);
    }
}