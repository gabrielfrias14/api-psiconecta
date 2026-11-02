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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import psiconecta.model.enums.ModalidadeAtendimento;
import psiconecta.model.enums.StatusAprovacaoEstudante;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

@Entity
@Table(name = "estudantes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Estudante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    @Column(name = "universidade", nullable = false, length = 200)
    private String universidade;

    @Column(name = "matricula", nullable = false, length = 50)
    private String matricula;

    @Column(name = "periodo", nullable = false)
    private Short periodo;

    @Column(name = "estagio_supervisionado", nullable = false)
    private Boolean estagioSupervisionado;

    @Column(name = "bio")
    private String bio;

    @Column(name = "foto_url", length = 500)
    private String fotoUrl;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "modalidade", nullable = false, columnDefinition = "modalidade_atendimento")
    private ModalidadeAtendimento modalidade;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "estudante_areas_atendimento", joinColumns = @JoinColumn(name = "estudante_id"), inverseJoinColumns = @JoinColumn(name = "area_atendimento_id"))
    private Set<AreaAtendimento> areasAtendimento = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "status_aprovacao", nullable = false, columnDefinition = "status_aprovacao_estudante")
    private StatusAprovacaoEstudante statusAprovacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "avaliado_por_usuario_id")
    private Usuario avaliadoPorUsuario;

    @Column(name = "avaliado_em")
    private OffsetDateTime avaliadoEm;

    @Column(name = "created_at", insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private OffsetDateTime updatedAt;
}