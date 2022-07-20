package comidev.components.category;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import comidev.config.ApiIntegrationTest;
import comidev.services.AppFabric;

@ApiIntegrationTest
public class CategoryControllerTest {
    @Autowired
    private AppFabric fabric;
    @Autowired
    private MockMvc mockMvc;

    // * GET, /categories
    @Test
    void OK_CuandoHayAlMenosHayUnaCategoria_findAll() throws Exception {
        fabric.createCategory(null);

        ResultActions res = mockMvc.perform(get("/categories"));

        res.andExpect(status().isOk());
    }
}
