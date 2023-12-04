package com.thesun4sky.todoparty.comment;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Test
    void testCommentRepository() {
        // CommentRepository의 기능에 대한 테스트 코드 작성
        // Assertions.assertEquals(expected, actual);
    }

}