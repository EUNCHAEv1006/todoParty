package com.thesun4sky.todoparty.todo;

import com.thesun4sky.todoparty.user.User;
import com.thesun4sky.todoparty.user.UserDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoService todoService;

    @Test
    @DisplayName("Create Todo")
    void testCreateTodo() {
        // Given
        TodoRequestDTO todoRequestDTO = new TodoRequestDTO();
        User user = new User();
        Todo savedTodo = new Todo();
        when(todoRepository.save(any(Todo.class))).thenReturn(savedTodo);

        // When
        TodoResponseDTO result = todoService.createTodo(todoRequestDTO, user);

        // Then
        assertEquals(savedTodo.getId(), result.getId());
    }

    @Test
    @DisplayName("Get Todo DTO")
    void testGetTodoDto() {
        // Given
        when(todoRepository.findById(anyLong())).thenReturn(java.util.Optional.of(new Todo()));

        // When
        TodoResponseDTO result = todoService.getTodoDto(1L);

        // Then
        assertEquals(TodoResponseDTO.class, result.getClass());
    }

    @Test
    @DisplayName("Get User Todo Map")
    void testGetUserTodoMap() {
        // Given
        List<Todo> todoList = Collections.singletonList(new Todo());
        when(todoRepository.findAll(any(Sort.class))).thenReturn(todoList);

        // When
        Map<UserDTO, List<TodoResponseDTO>> result = todoService.getUserTodoMap();

        // Then
        // 결과 검증
        // 필요한 추가적인 검증
    }

    // updateTodo, completeTodo, getTodo, getUserTodo 메서드에 대한 테스트 코드도 유사하게 작성할 수 있습니다.
}
