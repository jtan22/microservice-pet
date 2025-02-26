package com.bw.pet.controller;

import com.bw.pet.domain.Pet;
import com.bw.pet.domain.PetType;
import com.bw.pet.repository.PetRepository;
import com.bw.pet.repository.PetTypeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(PetController.class)
public class PetControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PetRepository petRepository;

    @MockBean
    private PetTypeRepository petTypeRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testFindAllDefault() throws Exception {
        when(petRepository.findAll()).thenReturn(List.of(new Pet(), new Pet(), new Pet()));
        mockMvc
                .perform(get("/pets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    public void testFindByOwnerId() throws Exception {
        when(petRepository.findByOwnerId(1)).thenReturn(List.of(new Pet(), new Pet()));
        mockMvc
                .perform(get("/pets?ownerId=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void testFindAllPetTypes() throws Exception {
        when(petTypeRepository.findAll()).thenReturn(List.of(new PetType(), new PetType(), new PetType()));
        mockMvc
                .perform(get("/pet-types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    public void testAdd() throws Exception {
        objectMapper.findAndRegisterModules();
        Pet pet = createPet();
        when(petRepository.save(pet)).thenReturn(pet);
        mockMvc
                .perform(post("/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(pet)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Test")))
                .andExpect(jsonPath("$.ownerId", is(1)));
    }

    @Test
    public void testFindById() throws Exception {
        Pet pet = createPet();
        pet.setId(1);
        when(petRepository.findById(1)).thenReturn(Optional.of(pet));
        mockMvc
                .perform(get("/pets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    public void testFindByIdNotFound() throws Exception {
        mockMvc
                .perform(get("/pets/10000"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Not Found [Pet [10000] not found]"));
    }

    @Test
    public void testUpdate() throws Exception {
        objectMapper.findAndRegisterModules();
        Pet pet = createPet();
        pet.setId(1);
        when(petRepository.findById(1)).thenReturn(Optional.of(pet));
        when(petRepository.save(pet)).thenReturn(pet);
        mockMvc
                .perform(put("/pets/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(pet)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Test")));
    }

    @Test
    public void testUpdateNotFound() throws Exception {
        objectMapper.findAndRegisterModules();
        Pet pet = createPet();
        pet.setId(10000);
        mockMvc
                .perform(put("/pets/10000")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(pet)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Not Found [Pet [10000] not found]"));
    }

    private Pet createPet() {
        PetType petType = new PetType();
        petType.setId(1);
        petType.setName("Cat");
        Pet pet = new Pet();
        pet.setName("Test");
        pet.setBirthDate(LocalDate.now());
        pet.setOwnerId(1);
        pet.setPetType(petType);
        return pet;
    }

}
