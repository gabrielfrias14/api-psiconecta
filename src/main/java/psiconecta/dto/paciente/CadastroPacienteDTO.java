package psiconecta.dto.paciente;

import java.time.LocalDate;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import psiconecta.dto.usuario.CadastroUsuarioDTO;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CadastroPacienteDTO {

    @Valid
    @NotNull(message = "Os dados do usuário são obrigatórios.")
    private CadastroUsuarioDTO usuario;

    @Pattern(
        regexp = "^[0-9]{11}$",
        message = "O CPF deve possuir exatamente 11 números."
    )
    private String cpf;

    @Past(
        message = "A data de nascimento deve ser anterior à data atual."
    )
    private LocalDate dataNascimento;

    @Size(
        max = 20,
        message = "O telefone deve possuir no máximo 20 caracteres."
    )
    private String telefone;
}