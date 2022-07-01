package com.example.userservice.grpcservice;

import com.example.userservice.User;
import com.example.userservice.UserDto;
import com.example.userservice.UserId;
import com.example.userservice.Users;
import com.example.userservice.client.UserServiceClient;
import io.grpc.StatusRuntimeException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestPropertySource("/application-test.yml")
@Sql(value = {"/sql/refresh-db.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Autowired
    private UserServiceClient userServiceClient;

    @Test
    void getUserByValidId() {
        User actualUser = userServiceClient.getUser(UserId.newBuilder().setId(2L).build());
        User expectedUser = User.newBuilder()
                .setId(2L)
                .setFirstName("Ivan")
                .setLastName("Ivanov")
                .setAge(23)
                .build();

        assertEquals(expectedUser, actualUser);
    }

    @Test
    void getUserByInvalidId() {
        assertThatThrownBy(() -> userServiceClient.getUser(UserId.newBuilder().setId(5L).build()))
                .isInstanceOf(StatusRuntimeException.class)
                .hasMessage("NOT_FOUND");
    }

    @Test
    void getUsers() {
        Users actualUsers = userServiceClient.getUsers();
        Users expectedUsers = Users.newBuilder()
                .addUsers(User.newBuilder()
                        .setId(1L)
                        .setFirstName("Dima")
                        .setLastName("Dimov")
                        .setAge(16)
                        .build())
                .addUsers(User.newBuilder()
                        .setId(2L)
                        .setFirstName("Ivan")
                        .setLastName("Ivanov")
                        .setAge(23)
                        .build())
                .addUsers(User.newBuilder()
                        .setId(3L)
                        .setFirstName("Petr")
                        .setLastName("Petrov")
                        .setAge(19)
                        .build())
                .build();

        assertEquals(expectedUsers, actualUsers);
    }

    @Test
    void createUser() {
        UserDto userDto = UserDto.newBuilder()
                .setFirstName("Oleg")
                .setLastName("Olegov")
                .setAge(33)
                .build();

        User actualUser = userServiceClient.createUser(userDto);

        User expectedUser = User.newBuilder()
                .setId(4L)
                .setFirstName("Oleg")
                .setLastName("Olegov")
                .setAge(33)
                .build();

        assertEquals(expectedUser, actualUser);
        assertEquals(userServiceClient.getUsers().getUsersList().size(), 4);
    }

    @Test
    void updateUserWithValidId() {
        User expectedUser = User.newBuilder()
                .setId(2L)
                .setFirstName("NeIvan")
                .setLastName("NeIvanov")
                .setAge(15)
                .build();

        User actualUser = userServiceClient.updateUser(expectedUser);

        assertEquals(expectedUser, actualUser);
    }

    @Test
    void updateUserWithInvalidId() {
        assertThatThrownBy(() -> userServiceClient.updateUser(User.newBuilder().setId(5L).build()))
                .isInstanceOf(StatusRuntimeException.class)
                .hasMessage("NOT_FOUND");
    }

    @Test
    void deleteUserWithValidId() {
        UserId expectedUserId = UserId.newBuilder()
                .setId(2L)
                .build();

        UserId actualUserId = userServiceClient.deleteUser(expectedUserId);

        assertEquals(expectedUserId, actualUserId);
        assertEquals(userServiceClient.getUsers().getUsersList().size(), 2);
    }

    @Test
    void deleteUserWithInvalidId() {
        assertThatThrownBy(() -> userServiceClient.deleteUser(UserId.newBuilder().setId(5L).build()))
                .isInstanceOf(StatusRuntimeException.class)
                .hasMessage("NOT_FOUND");
    }
}