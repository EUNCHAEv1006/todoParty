package com.thesun4sky.todoparty.todo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thesun4sky.todoparty.user.User;
import com.thesun4sky.todoparty.user.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(TodoController.class)
@ComponentScan(basePackages = {"com.thesun4sky.todoparty.todo", "com.thesun4sky.todoparty.user"})
public class TodoControllerTest {

    @MockBean
    private TodoRepository todoRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Mock
    private TodoService todoService;

    @InjectMocks
    private TodoController todoController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(todoController).build();  // 변경된 부분: standaloneSetup 사용
        objectMapper = new ObjectMapper();
    }

    @Test
    void testPostTodo() throws Exception {
        TodoRequestDTO todoRequestDTO = new TodoRequestDTO();
        TodoResponseDTO responseDTO = new TodoResponseDTO(new Todo());

        when(todoService.createTodo(any(TodoRequestDTO.class), any(User.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(todoRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void testGetTodo() throws Exception {
        Long todoId = 1L;
        TodoResponseDTO responseDTO = new TodoResponseDTO(new Todo());

        when(todoService.getTodoDto(anyLong())).thenReturn(responseDTO);

        mockMvc.perform(get("/api/todos/{todoId}", todoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void testGetTodoList() throws Exception {
        TodoResponseDTO todoResponseDTO = new TodoResponseDTO(new Todo());
        UserDTO userDTO = new UserDTO();

        when(todoService.getUserTodoMap()).thenReturn(Map.of(userDTO, List.of(todoResponseDTO)));

        mockMvc.perform(get("/api/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].user.id").exists())
                .andExpect(jsonPath("$[0].todoList[0].id").exists());
    }

    @Test
    void testPutTodo() throws Exception {
        Long todoId = 1L;
        TodoRequestDTO todoRequestDTO = new TodoRequestDTO();
        TodoResponseDTO responseDTO = new TodoResponseDTO(new Todo());

        when(todoService.updateTodo(anyLong(), any(TodoRequestDTO.class), any(User.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/api/todos/{todoId}", todoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(todoRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void testPatchTodo() throws Exception {
        Long todoId = 1L;
        TodoResponseDTO responseDTO = new TodoResponseDTO(new Todo());

        when(todoService.completeTodo(anyLong(), any(User.class))).thenReturn(responseDTO);

        mockMvc.perform(patch("/api/todos/{todoId}/complete", todoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());
    }
}
