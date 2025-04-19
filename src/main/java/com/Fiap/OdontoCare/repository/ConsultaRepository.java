package com.Fiap.OdontoCare.repository;

import com.Fiap.OdontoCare.entity.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {


    List<Consulta> findByDataConsultaAfter(LocalDate now);
}
