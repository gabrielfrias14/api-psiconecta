package psiconecta.model;

import java.time.LocalTime;
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

@Entity
@Table(name = "disponibilidades")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Disponibilidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
        name = "dia_semana",
        nullable = false
    )
    private Short diaSemana;

    @Column(
        name = "hora_inicio",
        nullable = false
    )
    private LocalTime horaInicio;

    @Column(
        name = "hora_fim",
        nullable = false
    )
    private LocalTime horaFim;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(
        name = "modalidade",
        nullable = false,
        columnDefinition = "modalidade_atendimento"
    )
    private ModalidadeAtendimento modalidade;

    @Column(
        name = "ativo",
        nullable = false
    )
    private Boolean ativo;

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