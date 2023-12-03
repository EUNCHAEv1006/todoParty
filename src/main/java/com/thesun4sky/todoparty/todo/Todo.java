package com.thesun4sky.todoparty.todo;

import com.thesun4sky.todoparty.comment.Comment;
import com.thesun4sky.todoparty.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String content;

    @Column
    private LocalDateTime createDate;

    @Column
    private Boolean iscompleted;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "todo")
    private List<Comment> comments;

    public Todo(TodoRequestDTO dto) {
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.createDate = LocalDateTime.now();
        this.iscompleted = false;
    }

    // 연관관계 메서드
    public void setUser(User user) {
        this.user = user;
    }

    // 서비스 메서드
    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void complete() {
        this.iscompleted = true;
    }
}
