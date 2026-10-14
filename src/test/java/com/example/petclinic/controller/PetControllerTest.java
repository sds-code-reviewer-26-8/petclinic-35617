package com.example.petclinic.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void 전체_동물_리스트_조회() throws Exception {
        mockMvc.perform(get("/api/v1/pets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(13)));
    }

    @Test
    void 생후_n년_미만_동물_리스트_조회() throws Exception {
        // 더미 데이터: cat 4마리 (Leo 2021-09-07, Samantha 2023-09-04, Max 2023-09-04, Sly 2023-06-08)
        // 현재 날짜 기준 4년 전 날짜 이후에 태어난 cat만 조회되어야 한다.
        // birthDate는 ISO-8601 문자열로 직렬화되므로 문자열 비교로 시각 비교가 가능하다.
        String cutoff = LocalDate.now().minusYears(4).atStartOfDay().toString();

        mockMvc.perform(get("/api/v1/pets").param("year", "4").param("type", "cat"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].type.name", everyItem(equalToIgnoringCase("cat"))))
                .andExpect(jsonPath("$[*].birthDate", everyItem(greaterThan(cutoff))));
    }

    @Test
    void 충분히_큰_year면_해당_type_전체_조회_type은_대소문자_무시() throws Exception {
        mockMvc.perform(get("/api/v1/pets").param("year", "100").param("type", "CAT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[*].type.name", everyItem(equalToIgnoringCase("cat"))));
    }

    @Test
    void 조건에_맞는_동물이_없으면_빈_리스트() throws Exception {
        // 더미 데이터의 가장 최근 생일은 2023년이므로 1년 미만은 없다.
        mockMvc.perform(get("/api/v1/pets").param("year", "1").param("type", "cat"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void year만_전달하면_IllegalArgumentException_400() throws Exception {
        mockMvc.perform(get("/api/v1/pets").param("year", "3"))
                .andExpect(status().isBadRequest())
                .andExpect(r -> assertThat(r.getResolvedException(), instanceOf(IllegalArgumentException.class)))
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void type만_전달하면_IllegalArgumentException_400() throws Exception {
        mockMvc.perform(get("/api/v1/pets").param("type", "cat"))
                .andExpect(status().isBadRequest())
                .andExpect(r -> assertThat(r.getResolvedException(), instanceOf(IllegalArgumentException.class)))
                .andExpect(jsonPath("$.status").value(400));
    }
}
