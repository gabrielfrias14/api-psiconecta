package psiconecta.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import psiconecta.model.AreaAtendimento;
import psiconecta.repository.AreaAtendimentoRepository;

@Service
public class AreaAtendimentoService {

    private final AreaAtendimentoRepository areaAtendimentoRepository;

    public AreaAtendimentoService(
            AreaAtendimentoRepository areaAtendimentoRepository) {

        this.areaAtendimentoRepository = areaAtendimentoRepository;
    }

    public List<AreaAtendimento> listarTodas() {
        return areaAtendimentoRepository.findAll();
    }

    public List<AreaAtendimento> listarAtivas() {
        return areaAtendimentoRepository.findByAtivoTrue();
    }

    public Optional<AreaAtendimento> buscarPorId(Long id) {
        return areaAtendimentoRepository.findById(id);
    }

    public Optional<AreaAtendimento> buscarPorNome(
            String nome) {

        return areaAtendimentoRepository
                .findByNomeIgnoreCase(nome);
    }

    public boolean nomeJaCadastrado(String nome) {

        return areaAtendimentoRepository
                .existsByNomeIgnoreCase(nome);
    }
}