package psiconecta.dto.estudante;

import java.util.Set;

import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import psiconecta.dto.usuario.CadastroUsuarioDTO;
import psiconecta.model.enums.ModalidadeAtendimento;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CadastroEstudanteDTO {

    @Valid
    @NotNull(message = "Os dados do usuário são obrigatórios.")
    private CadastroUsuarioDTO usuario;

    @NotBlank(message = "A universidade é obrigatória.")
    @Size(
        max = 200,
        message = "A universidade deve possuir no máximo 200 caracteres."
    )
    private String universidade;

    @NotBlank(message = "A matrícula é obrigatória.")
    @Size(
        max = 50,
        message = "A matrícula deve possuir no máximo 50 caracteres."
    )
    private String matricula;

    @NotNull(message = "O período é obrigatório.")
    @Min(
        value = 1,
        message = "O período deve ser no mínimo 1."
    )
    @Max(
        value = 12,
        message = "O período deve ser no máximo 12."
    )
    private Short periodo;

    @NotNull(message = "Informe se o estágio é supervisionado.")
    private Boolean estagioSupervisionado;

    @Size(
        max = 2000,
        message = "A bio deve possuir no máximo 2000 caracteres."
    )
    private String bio;

    @NotNull(message = "A modalidade de atendimento é obrigatória.")
    private ModalidadeAtendimento modalidade;

    @NotEmpty(message = "Informe pelo menos uma área de atendimento.")
    private Set<Long> areasAtendimentoIds;

    @AssertTrue(
        message = "É necessário estar em estágio supervisionado."
    )
    public boolean isEstagioSupervisionadoValido() {

        return Boolean.TRUE.equals(estagioSupervisionado);
    }
}