package com.Fiap.OdontoCare.service;

import com.Fiap.OdontoCare.entity.Dentista;
import com.Fiap.OdontoCare.repository.DentistaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DentistaService {

    @Autowired
    private DentistaRepository dentistaRepository;

    public long count() {
        return dentistaRepository.count();
    }

    public List<Dentista> findAll() {
        return dentistaRepository.findAll();
    }

    public Optional<Dentista> findById(Long id) {
        return dentistaRepository.findById(id);
    }


    public Dentista save(Dentista dentista) {
        return dentistaRepository.save(dentista);
    }


    public Dentista update(Dentista dentista) {
        return dentistaRepository.save(dentista);
    }

    public void deleteById(Long id) {
        dentistaRepository.deleteById(id);
    }
}
