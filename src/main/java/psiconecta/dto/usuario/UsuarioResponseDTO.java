package psiconecta.dto.usuario;

import psiconecta.model.enums.PerfilUsuario;

public record UsuarioResponseDTO(
        Long id,
        String nome,
        String email,
        PerfilUsuario perfil,
        Boolean ativo
) {
}