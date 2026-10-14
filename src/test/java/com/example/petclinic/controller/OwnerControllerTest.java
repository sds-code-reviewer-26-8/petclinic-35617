package com.example.petclinic.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.startsWithIgnoringCase;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OwnerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void 전체_동물주인_리스트_조회() throws Exception {
        mockMvc.perform(get("/api/v1/owners"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(10)));
    }

    @Test
    void lastname으로_시작하는_동물주인_리스트_조회() throws Exception {
        // 더미 데이터: Davis 2명 (Betty, Harold)
        mockMvc.perform(get("/api/v1/owners").param("lastname", "da"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].lastName", everyItem(startsWithIgnoringCase("da"))));
    }

    @Test
    void lastname이_일치하지_않으면_빈_리스트() throws Exception {
        mockMvc.perform(get("/api/v1/owners").param("lastname", "kim"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void lastname이_비어있으면_전체_리스트() throws Exception {
        mockMvc.perform(get("/api/v1/owners").param("lastname", ""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(10)));
    }
}
