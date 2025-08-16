package com.ufs.ReservaAerea.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ufs.ReservaAerea.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByEmailAndSenha(String email, String senha);
}
