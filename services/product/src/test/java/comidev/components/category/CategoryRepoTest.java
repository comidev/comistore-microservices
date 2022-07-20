package comidev.components.category;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import comidev.config.RepoUnitTest;

@RepoUnitTest
public class CategoryRepoTest {
    @Autowired
    private CategoryRepo categoryRepo;

    @Test
    void IS_PRESENT_CuandoExisteLaCategoria_findByName() {
        String name = "Categoria";
        categoryRepo.save(new Category(name));

        Optional<Category> categoryOPT = categoryRepo.findByName(name);

        assertTrue(categoryOPT.isPresent());
    }

    @Test
    void IS_EMPTY_CuandoNoExisteLaCategoria_findByName() {
        String name = "Categoria";

        Optional<Category> categoryOPT = categoryRepo.findByName(name);

        assertTrue(categoryOPT.isEmpty());
    }
}
