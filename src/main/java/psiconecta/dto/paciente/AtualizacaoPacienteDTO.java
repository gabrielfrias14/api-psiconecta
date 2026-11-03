package psiconecta.dto.paciente;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

public record AtualizacaoPacienteDTO(

        @NotBlank(message = "O nome é obrigatório.")
        @Size(
            min = 2,
            max = 150,
            message = "O nome deve possuir entre 2 e 150 caracteres."
        )
        String nome,

        @Past(message = "A data de nascimento deve estar no passado.")
        LocalDate dataNascimento,

        @Size(
            max = 20,
            message = "O telefone deve possuir no máximo 20 caracteres."
        )
        String telefone

) {
}