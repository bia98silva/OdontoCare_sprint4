package com.Fiap.OdontoCare.repository;

import com.Fiap.OdontoCare.entity.Dentista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DentistaRepository extends JpaRepository<Dentista, Long> {
}
