package com.thesun4sky.todoparty.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private String username;

    // 기본 생성자
    public UserDTO() {
    }

    public UserDTO(User user) {
        this.username = user.getUsername();
    }
}
