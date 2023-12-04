package com.thesun4sky.todoparty.todo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class TodoRepositoryTest {

    @Autowired
    private TodoRepository todoRepository;

    @Test
    void testTodoRepository() {
        // 테스트 데이터 생성
        Todo todo = new Todo();
        todo.setTitle("테스트 Todo");
        todoRepository.save(todo);

        // Todo ID로 Todo를 찾아오는 메서드 테스트
        Optional<Todo> foundTodoOptional = todoRepository.findById(todo.getId());
        assertTrue(foundTodoOptional.isPresent());
        Todo foundTodo = foundTodoOptional.get();
        assertEquals("테스트 Todo", foundTodo.getTitle());

        // Todo 삭제 메서드 테스트
        todoRepository.delete(foundTodo);
        Optional<Todo> deletedTodoOptional = todoRepository.findById(todo.getId());
        assertTrue(deletedTodoOptional.isEmpty());
    }
}
