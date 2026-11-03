package psiconecta.mapper;

import org.springframework.stereotype.Component;

import psiconecta.dto.usuario.UsuarioResponseDTO;
import psiconecta.model.Usuario;

@Component
public class UsuarioMapper {

    public UsuarioResponseDTO toResponse(Usuario usuario) {

        if (usuario == null) {
            return null;
        }

        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getPerfil(),
                usuario.getAtivo()
        );
    }
}