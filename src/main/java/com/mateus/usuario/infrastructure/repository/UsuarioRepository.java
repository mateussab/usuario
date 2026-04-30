package com.mateus.usuario.infrastructure.repository;


import com.mateus.usuario.infrastructure.entity.Usuario;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario,Long>{

    boolean existsByEmail(String email);

    //Evita quebrar se caso procurar email e nao achar
    Optional<Usuario> findByEmail(String email);

    //ajuda a nao causar erro na hora de deletar
    @Transactional
    void deleteByEmail(String email);

}
