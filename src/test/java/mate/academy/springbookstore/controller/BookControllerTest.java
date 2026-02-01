package mate.academy.springbookstore.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import mate.academy.springbookstore.dto.book.CreateBookRequestDto;
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
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Create book - success (ADMIN)")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = "/database/add-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/database/remove-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createBook_ValidRequest_ReturnsBookDto() throws Exception {
        CreateBookRequestDto requestDto = TestUtil.createBookRequestDto();
        String json = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(requestDto.getTitle()))
                .andExpect(jsonPath("$.author").value(requestDto.getAuthor()));
    }

    @Test
    @DisplayName("Create book - forbidden (USER)")
    @WithMockUser(username = "user", roles = {"USER"})
    void createBook_UserRole_Forbidden() throws Exception {
        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                TestUtil.createBookRequestDto()
                        )))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Create book - validation error")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void createBook_InvalidRequest_ReturnsBadRequest() throws Exception {
        CreateBookRequestDto invalidDto = new CreateBookRequestDto(); // empty

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Get all books - success")
    @WithMockUser(username = "user", roles = {"USER"})
    @Sql(scripts = "/database/add-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/database/remove-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAllBooks_ReturnsPage() throws Exception {
        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1));
    }

    @Test
    @DisplayName("Get book by id - success")
    @WithMockUser(username = "user", roles = {"USER"})
    @Sql(scripts = "/database/add-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/database/remove-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getBookById_ValidId_ReturnsBook() throws Exception {
        mockMvc.perform(get("/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Process"));
    }

    @Test
    @DisplayName("Get book by id - not found")
    @WithMockUser(username = "user", roles = {"USER"})
    void getBookById_InvalidId_ReturnsNotFound() throws Exception {
        mockMvc.perform(get("/books/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Update book - success (ADMIN)")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = "/database/add-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/database/remove-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateBook_ValidId_ReturnsUpdatedBook() throws Exception {
        CreateBookRequestDto requestDto = TestUtil.createBookRequestDto();
        requestDto.setTitle("Updated Title");

        mockMvc.perform(put("/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"));
    }

    @Test
    @DisplayName("Update book - forbidden (USER)")
    @WithMockUser(username = "user", roles = {"USER"})
    void updateBook_UserRole_Forbidden() throws Exception {
        mockMvc.perform(put("/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                TestUtil.createBookRequestDto()
                        )))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Update book - not found")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void updateBook_InvalidId_ReturnsNotFound() throws Exception {
        mockMvc.perform(put("/books/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                TestUtil.createBookRequestDto()
                        )))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Delete book - success (ADMIN)")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = "/database/add-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/database/remove-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteBook_ValidId_NoContent() throws Exception {
        mockMvc.perform(delete("/books/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Delete book - forbidden (USER)")
    @WithMockUser(username = "user", roles = {"USER"})
    void deleteBook_UserRole_Forbidden() throws Exception {
        mockMvc.perform(delete("/books/1"))
                .andExpect(status().isForbidden());
    }
}
