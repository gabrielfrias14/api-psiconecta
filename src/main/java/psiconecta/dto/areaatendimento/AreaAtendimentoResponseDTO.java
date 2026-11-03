package psiconecta.dto.areaatendimento;

public record AreaAtendimentoResponseDTO(
        Long id,
        String nome,
        String descricao,
        Boolean ativo
) {
}