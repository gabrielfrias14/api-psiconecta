package psiconecta.mapper;

import org.springframework.stereotype.Component;

import psiconecta.dto.paciente.PacienteResponseDTO;
import psiconecta.model.Paciente;

@Component
public class PacienteMapper {

    private final UsuarioMapper usuarioMapper;

    public PacienteMapper(
            UsuarioMapper usuarioMapper) {

        this.usuarioMapper = usuarioMapper;
    }

    public PacienteResponseDTO toResponse(
            Paciente paciente) {

        if (paciente == null) {
            return null;
        }

        return new PacienteResponseDTO(
                paciente.getId(),
                usuarioMapper.toResponse(
                        paciente.getUsuario()
                ),
                paciente.getCpf(),
                paciente.getDataNascimento(),
                paciente.getTelefone()
        );
    }
}