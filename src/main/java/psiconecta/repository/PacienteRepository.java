package psiconecta.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import psiconecta.model.Paciente;

public interface PacienteRepository
        extends JpaRepository<Paciente, Long> {

    Optional<Paciente> findByUsuario_Id(Long usuarioId);

    Optional<Paciente> findByCpf(String cpf);

    boolean existsByCpf(String cpf);
}