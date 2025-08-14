package com.ufs.ReservaAerea.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ufs.ReservaAerea.model.Poltrona;
import java.util.List;

public interface PoltronaRepository extends JpaRepository<Poltrona, Long> {

    List<Poltrona> findByVoo_Id(Long vooId);
    
}
