package com.thesun4sky.todoparty.comment;

import com.thesun4sky.todoparty.todo.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
