package com.thesun4sky.todoparty.todo;

import com.thesun4sky.todoparty.CommonResponseDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TodoResponseDTO extends CommonResponseDto {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createDate;

    public TodoResponseDTO(Todo todo) {
        this.id = todo.getId();
        this.title = todo.getTitle();
        this.content = todo.getContent();
        this.createDate = todo.getCreateDate();
    }
}
