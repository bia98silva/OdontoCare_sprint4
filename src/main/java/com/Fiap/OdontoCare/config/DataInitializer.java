package com.Fiap.OdontoCare.config;

import com.Fiap.OdontoCare.entity.Perfil;
import com.Fiap.OdontoCare.entity.Usuario;
import com.Fiap.OdontoCare.repository.PerfilRepository;
import com.Fiap.OdontoCare.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UsuarioService usuarioService;
    private final PerfilRepository perfilRepository;

    public DataInitializer(UsuarioService usuarioService, PerfilRepository perfilRepository) {
        this.usuarioService = usuarioService;
        this.perfilRepository = perfilRepository;
    }
    @Override
    public void run(String... args) throws Exception {
        criarPerfilSeNaoExistir("ADMIN");
        criarPerfilSeNaoExistir("DENTISTA");
        criarPerfilSeNaoExistir("RECEPCIONISTA");

        if (usuarioService.buscarPorUsername("admin").isEmpty()) {
            Usuario admin = new Usuario();
            admin.setUsername("admin");
            admin.setPassword("admin123");
            admin.setNome("Administrador");
            admin.setEmail("admin@odonto.com");

            Set<Perfil> perfis = new HashSet<>();
            perfis.add(perfilRepository.findByNome("ADMIN").get());
            admin.setPerfis(perfis);

            usuarioService.salvar(admin);
        }

        if (usuarioService.buscarPorUsername("dentista").isEmpty()) {
            Usuario dentista = new Usuario();
            dentista.setUsername("dentista");
            dentista.setPassword("dentista123");
            dentista.setNome("Dr. José Silva");
            dentista.setEmail("jose@odonto.com");

            Set<Perfil> perfis = new HashSet<>();
            perfis.add(perfilRepository.findByNome("DENTISTA").get());
            dentista.setPerfis(perfis);

            usuarioService.salvar(dentista);
        }

        if (usuarioService.buscarPorUsername("recepcionista").isEmpty()) {
            Usuario recepcionista = new Usuario();
            recepcionista.setUsername("recepcionista");
            recepcionista.setPassword("recepcionista123");
            recepcionista.setNome("Maria Oliveira");
            recepcionista.setEmail("maria@odonto.com");

            Set<Perfil> perfis = new HashSet<>();
            perfis.add(perfilRepository.findByNome("RECEPCIONISTA").get());
            recepcionista.setPerfis(perfis);

            usuarioService.salvar(recepcionista);
        }
    }

    private void criarPerfilSeNaoExistir(String nomePerfil) {
        if (perfilRepository.findByNome(nomePerfil).isEmpty()) {
            Perfil perfil = new Perfil();
            perfil.setNome(nomePerfil);
            perfilRepository.save(perfil);
        }
    }
}