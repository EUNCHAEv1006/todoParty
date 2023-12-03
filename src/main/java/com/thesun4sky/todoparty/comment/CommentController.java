package com.thesun4sky.todoparty.comment;

import com.thesun4sky.todoparty.CommonResponseDTO;
import com.thesun4sky.todoparty.todo.TodoListResponseDTO;
import com.thesun4sky.todoparty.todo.TodoRequestDTO;
import com.thesun4sky.todoparty.todo.TodoResponseDTO;
import com.thesun4sky.todoparty.user.UserDTO;
import com.thesun4sky.todoparty.user.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.RejectedExecutionException;

@RequestMapping("/api/comments")
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponseDTO> postComment(@RequestBody CommentRequestDTO commentRequestDTO, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        CommentResponseDTO responseDTO = commentService.createComment(commentRequestDTO, userDetails.getUser());

        return ResponseEntity.status(201).body(responseDTO);
    }

    // Put은 전체를 수정할 때 많이사용, Patch는 일부분에 많이 사용
    @PutMapping("/{commentId}")
    public ResponseEntity<CommonResponseDTO> putComment(@PathVariable Long commentId, @RequestBody CommentRequestDTO commentRequestDTO, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            CommentResponseDTO responseDTO = commentService.updateComment(commentId, commentRequestDTO, userDetails.getUser());
            return ResponseEntity.ok().body(responseDTO);
        } catch (RejectedExecutionException | IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(new CommonResponseDTO(ex.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<CommonResponseDTO> deleteComment(@PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            commentService.deleteComment(commentId, userDetails.getUser());
            return ResponseEntity.ok().body(new CommonResponseDTO("정상적으로 삭제 되었습니다.", HttpStatus.OK.value()));
        } catch (RejectedExecutionException | IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(new CommonResponseDTO(ex.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }
}
