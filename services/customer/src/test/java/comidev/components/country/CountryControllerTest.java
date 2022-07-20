package comidev.components.country;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import comidev.config.ApiIntegrationTest;
import comidev.services.AppFabric;

@ApiIntegrationTest
public class CountryControllerTest {
    @Autowired
    private AppFabric fabric;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.fabric.getCountryRepo().deleteAll();
    }

    // * GET, /categories
    @Test
    void NO_CONTENT_CuandoEstaVacio_findAll() throws Exception {
        ResultActions res = mockMvc.perform(get("/countries"));

        res.andExpect(status().isNoContent());
    }

    @Test
    void OK_CuandoHayAlMenosHayUnaCategoria_findAll() throws Exception {
        fabric.createCountry(null);

        ResultActions res = mockMvc.perform(get("/countries"));

        res.andExpect(status().isOk());
    }
}
