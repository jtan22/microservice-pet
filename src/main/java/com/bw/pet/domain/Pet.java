package com.bw.pet.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "pets")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Pet name must not be blank")
    private String name;

    @Column(name = "birth_date")
    @NotNull(message = "Pet birth date must not be null")
    private LocalDate birthDate;

    @Column(name = "owner_id")
    @NotNull(message = "Pet ownerId must not be null")
    private Integer ownerId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pet_type_id")
    @NotNull(message = "Pet type must not be null")
    private PetType petType;

}
