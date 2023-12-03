package com.thesun4sky.todoparty.todo;

import com.thesun4sky.todoparty.CommonResponseDto;
import com.thesun4sky.todoparty.user.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/todos")
@RestController
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @PostMapping
    public ResponseEntity<TodoResponseDTO> postTodo(@RequestBody TodoRequestDTO todoRequestDTO, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        TodoResponseDTO responseDTO = todoService.createPost(todoRequestDTO, userDetails.getUser());

        return ResponseEntity.status(201).body(responseDTO);
    }


    @GetMapping
    public ResponseEntity<Void> getTodoList() {
        return ResponseEntity.ok().build();
    }

}
