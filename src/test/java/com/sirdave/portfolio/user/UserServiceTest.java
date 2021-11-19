package com.sirdave.portfolio.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    UserService underTest;
    @Mock private UserRepository userRepository;
    @Mock private BCryptPasswordEncoder bCryptPasswordEncoder;

    @BeforeEach
    void setUp() {
        underTest = new UserService(userRepository, bCryptPasswordEncoder);
    }

    @Test
    @Disabled
    void loadUserByUsername() {
    }

    @Test
    void canSaveUser() {
        User user = new User("David", "Abiola",
                "dave1234@gmail.com", "+23412345678", "test1234");

        underTest.saveUser(user);
        ArgumentCaptor<User> argumentCaptor =
                ArgumentCaptor.forClass(User.class);

        verify(userRepository).save(argumentCaptor.capture());

        User capturedUser = argumentCaptor.getValue();

        assertThat(capturedUser).isEqualTo(user);
    }

    @Test
    void willThrowException() {
        User user = new User("David", "Abiola",
                "dave1234@gmail.com", "+23412345678", "test1234");

        assertThat(underTest.isUserExistsByEmail(user.getEmail()));
    }

    @Test
    void getUserById() {
        User user = new User("David", "Abiola",
                "dave1234@gmail.com", "+23412345678", "test1234");

        underTest.saveUser(user);
        ArgumentCaptor<User> argumentCaptor =
                ArgumentCaptor.forClass(User.class);

        verify(userRepository).save(argumentCaptor.capture());

        Long capturedUserId = argumentCaptor.getValue().getId();

        assertThat(capturedUserId).isEqualTo(user.getId());
    }

    @Test
    @Disabled
    void isUserExistsByEmail() {
    }

    @Test
    @Disabled
    void doPasswordsMatch() {
    }


    @Test
    @Disabled
    void updateUserProfile() {
    }

    @Test
    @Disabled
    void arePasswordsSame() {
    }

    @Test
    @Disabled
    void changeUserPassword() {
    }
}