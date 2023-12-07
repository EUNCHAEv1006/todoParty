package com.thesun4sky.todoparty.user;

import static org.assertj.core.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.thesun4sky.todoparty.test.CommonTest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class UserRequestDtoTest implements CommonTest {

    @DisplayName("유저 요청 DTO 생성")
    @Nested
    class createUserRequestDTO {

        @DisplayName("유저 요청 DTO 생성 성공")
        @Test
        void createUserRequestDTO_success() {
            // given
            UserRequestDTO userRequestDTO = new UserRequestDTO();
            userRequestDTO.setUsername(TEST_USER_NAME);
            userRequestDTO.setPassword(TEST_USER_PASSWORD);

            // when
            Set<ConstraintViolation<UserRequestDTO>> violations = validate(userRequestDTO);

            // then
            assertThat(violations).isEmpty();
        }

        @DisplayName("유저 요청 DTO 생성 실패 - 잘못된 username")
        @Test
        void createUserRequestDTO_fail_wrongUserName() {
            // given
            UserRequestDTO userRequestDTO = new UserRequestDTO();
            userRequestDTO.setUsername("Invalid user name");
            userRequestDTO.setPassword(TEST_USER_PASSWORD);

            // when
            Set<ConstraintViolation<UserRequestDTO>> violations = validate(userRequestDTO);

            // then
            assertThat(violations).hasSize(1);
            assertThat(violations)
                    .extracting("message")
                    .contains("\"^[a-z0-9]{4,10}$\"와 일치해야 합니다");
        }

        @DisplayName("유저 요청 DTO 생성 실패 - 잘못된 password")
        @Test
        void createUserRequestDTO_wrongPassword(){
            // given
            UserRequestDTO userRequestDTO = new UserRequestDTO();
            userRequestDTO.setUsername(TEST_USER_NAME);
            userRequestDTO.setPassword("Invalid password");

            // when
            Set<ConstraintViolation<UserRequestDTO>> violations = validate(userRequestDTO);


            // then

            // 오류가 몇 개 틀렸는지 알려줌
            assertThat(violations).hasSize(1);

            assertThat(violations)

                    // 메시지에 있는 값을 뽑아옴
                    .extracting("message")

                    // 그 값에 (~~)안에 있는 패턴을 만족하는지 검증
                    .contains("\"^[a-zA-Z0-9]{8,15}$\"와 일치해야 합니다");
        }



        // @Valid와 동일
        private Set<ConstraintViolation<UserRequestDTO>> validate(UserRequestDTO userRequestDTO) {
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Validator validator = factory.getValidator();
            return validator.validate(userRequestDTO);
        }

    }

}