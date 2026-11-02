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

import psiconecta.model.enums.TipoConsentimento;

@Entity
@Table(name = "consentimentos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Consentimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(
        fetch = FetchType.LAZY,
        optional = false
    )
    @JoinColumn(
        name = "usuario_id",
        nullable = false
    )
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(
        name = "tipo",
        nullable = false,
        columnDefinition = "tipo_consentimento"
    )
    private TipoConsentimento tipo;

    @Column(
        name = "versao",
        nullable = false,
        length = 20
    )
    private String versao;

    @Column(
        name = "aceito",
        nullable = false
    )
    private Boolean aceito;

    @Column(
        name = "registrado_em",
        insertable = false,
        updatable = false
    )
    private OffsetDateTime registradoEm;
}