package psiconecta.dto.auth;

import psiconecta.model.enums.PerfilUsuario;

public record LoginResponseDTO(
        String token,
        Long usuarioId,
        String nome,
        String email,
        PerfilUsuario perfil
) {
}