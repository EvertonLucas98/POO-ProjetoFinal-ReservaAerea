package com.ufs.ReservaAerea.repository;

import com.ufs.ReservaAerea.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    
    List<Reserva> findByClienteIdOrderByIdDesc(Long clienteId);
    List<Reserva> findByVooId(Long vooId);

}
