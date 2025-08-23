package com.ufs.ReservaAerea.repository;

import com.ufs.ReservaAerea.model.TipoAviao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoAviaoRepository extends JpaRepository<TipoAviao, Long> {}