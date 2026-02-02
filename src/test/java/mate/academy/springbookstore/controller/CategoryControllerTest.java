package mate.academy.springbookstore.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.fasterxml.jackson.databind.ObjectMapper;
import mate.academy.springbookstore.dto.category.CreateCategoryRequestDto;
import mate.academy.springbookstore.util.TestUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Create category - success (ADMIN)")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = "/database/add-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/database/remove-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createCategory_ValidRequest_ReturnsCategoryDto() throws Exception {
        CreateCategoryRequestDto requestDto = TestUtil.createCategoryRequestDto();

        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(requestDto.name()));
    }

    @Test
    @DisplayName("Create category - forbidden (USER)")
    @WithMockUser(username = "user", roles = {"USER"})
    void createCategory_UserRole_Forbidden() throws Exception {
        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                TestUtil.createCategoryRequestDto()
                        )))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Create category - validation error")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void createCategory_InvalidRequest_ReturnsBadRequest() throws Exception {
        CreateCategoryRequestDto invalidDto =
                new CreateCategoryRequestDto("", "");

        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Get all categories - success")
    @WithMockUser(username = "user", roles = {"USER"})
    @Sql(scripts = "/database/add-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/database/remove-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAllCategories_ReturnsPage() throws Exception {
        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1));
    }

    @Test
    @DisplayName("Get category by id - success")
    @WithMockUser(username = "user", roles = {"USER"})
    @Sql(scripts = "/database/add-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/database/remove-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getCategoryById_ValidId_ReturnsCategory() throws Exception {
        mockMvc.perform(get("/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").exists());
    }

    @Test
    @DisplayName("Get category by id - not found")
    @WithMockUser(username = "user", roles = {"USER"})
    void getCategoryById_InvalidId_ReturnsNotFound() throws Exception {
        mockMvc.perform(get("/categories/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Update category - success (ADMIN)")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = "/database/add-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/database/remove-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateCategory_ValidId_ReturnsUpdatedCategory() throws Exception {
        CreateCategoryRequestDto requestDto =
                new CreateCategoryRequestDto("Updated", "Updated description");

        mockMvc.perform(put("/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated"));
    }

    @Test
    @DisplayName("Update category - forbidden (USER)")
    @WithMockUser(username = "user", roles = {"USER"})
    void updateCategory_UserRole_Forbidden() throws Exception {
        mockMvc.perform(put("/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                TestUtil.createCategoryRequestDto()
                        )))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Update category - not found")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void updateCategory_InvalidId_ReturnsNotFound() throws Exception {
        mockMvc.perform(put("/categories/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                TestUtil.createCategoryRequestDto()
                        )))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Delete category - success (ADMIN)")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = "/database/add-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/database/remove-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteCategory_ValidId_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/categories/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Delete category - forbidden (USER)")
    @WithMockUser(username = "user", roles = {"USER"})
    void deleteCategory_UserRole_Forbidden() throws Exception {
        mockMvc.perform(delete("/categories/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Get books by category - success")
    @WithMockUser(username = "user", roles = {"USER"})
    @Sql(scripts = "/database/add-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/database/remove-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getBooksByCategoryId_ValidId_ReturnsBooks() throws Exception {
        mockMvc.perform(get("/categories/1/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1));
    }

    @Test
    @DisplayName("Get books by category - category not found")
    @WithMockUser(username = "user", roles = {"USER"})
    void getBooksByCategoryId_InvalidId_ReturnsNotFound() throws Exception {
        mockMvc.perform(get("/categories/999/books"))
                .andExpect(status().isNotFound());
    }
}
