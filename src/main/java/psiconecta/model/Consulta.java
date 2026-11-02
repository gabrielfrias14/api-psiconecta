package psiconecta.model;

import java.time.OffsetDateTime;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

import psiconecta.model.enums.ModalidadeAtendimento;
import psiconecta.model.enums.StatusConsulta;

@Entity
@Table(name = "consultas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(
        fetch = FetchType.LAZY,
        optional = false
    )
    @JoinColumn(
        name = "paciente_id",
        nullable = false
    )
    private Paciente paciente;

    @ManyToOne(
        fetch = FetchType.LAZY,
        optional = false
    )
    @JoinColumn(
        name = "estudante_id",
        nullable = false
    )
    private Estudante estudante;

    @Column(
        name = "data_hora_inicio",
        nullable = false
    )
    private OffsetDateTime dataHoraInicio;

    @Column(
        name = "data_hora_fim",
        nullable = false
    )
    private OffsetDateTime dataHoraFim;

    @Column(
        name = "duracao_minutos",
        nullable = false
    )
    private Integer duracaoMinutos;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(
        name = "modalidade",
        nullable = false,
        columnDefinition = "modalidade_atendimento"
    )
    private ModalidadeAtendimento modalidade;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(
        name = "status",
        nullable = false,
        columnDefinition = "status_consulta"
    )
    private StatusConsulta status;

    @Column(name = "observacao")
    private String observacao;

    @Column(
        name = "link_reuniao",
        length = 500
    )
    private String linkReuniao;

    @Column(
        name = "created_at",
        insertable = false,
        updatable = false
    )
    private OffsetDateTime createdAt;

    @Column(
        name = "updated_at",
        insertable = false,
        updatable = false
    )
    private OffsetDateTime updatedAt;
}