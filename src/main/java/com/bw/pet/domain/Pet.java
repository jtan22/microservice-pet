package com.bw.pet.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "pets")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "owner_id")
    private Integer ownerId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pet_type_id")
    private PetType petType;

}
