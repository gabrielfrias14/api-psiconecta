package psiconecta.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "areas_atendimento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AreaAtendimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
        name = "nome",
        nullable = false,
        length = 150
    )
    private String nome;

    @Column(name = "descricao")
    private String descricao;

    @Column(
        name = "ativo",
        nullable = false
    )
    private Boolean ativo;
}