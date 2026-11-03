package psiconecta.dto.paciente;

import java.time.LocalDate;

import psiconecta.dto.usuario.UsuarioResponseDTO;

public record PacienteResponseDTO(
        Long id,
        UsuarioResponseDTO usuario,
        String cpf,
        LocalDate dataNascimento,
        String telefone
) {
}