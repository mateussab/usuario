package com.mateus.usuario.infrastructure.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor //construtor com argumentos
@NoArgsConstructor // construtor sem argumentos
@Entity
@Table(name = "telefone")
@Builder
public class Telefone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero", length = 10)
    private String numero;

    @Column(name = "ddd", length = 3)
    private String ddd;

}