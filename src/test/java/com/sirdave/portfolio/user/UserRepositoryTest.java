package com.sirdave.portfolio.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void findByEmail() {
        User user = new User("David", "Abiola",
                "dave1234@gmail.com", "+23412345678", "test1234");

        underTest.save(user);

        String email = "dave1234@gmail.com";
        boolean expected = underTest.findByEmail(email).isPresent();
        assertThat(expected).isTrue();
    }

    @Test
    void findByEmailNotExist() {
        String email = "dave1234@gmail.com";
        boolean expected = underTest.findByEmail(email).isPresent();
        assertThat(expected).isFalse();

    }
}