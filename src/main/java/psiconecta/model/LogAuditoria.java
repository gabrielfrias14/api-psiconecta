package psiconecta.model;

import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "logs_auditoria")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LogAuditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(
        name = "acao",
        nullable = false,
        length = 100
    )
    private String acao;

    @Column(
        name = "entidade",
        length = 100
    )
    private String entidade;

    @Column(name = "entidade_id")
    private Long entidadeId;

    @Column(name = "detalhes")
    private String detalhes;

    @Column(
        name = "data_hora",
        insertable = false,
        updatable = false
    )
    private OffsetDateTime dataHora;
}