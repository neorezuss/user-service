package com.example.userservice.client;

import com.example.userservice.User;
import com.example.userservice.UserDto;
import com.example.userservice.UserId;
import com.example.userservice.UserServiceGrpc;
import com.example.userservice.Users;
import com.google.protobuf.Empty;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class UserServiceClient {
    private UserServiceGrpc.UserServiceBlockingStub userServiceBlockingStub;

    @PostConstruct
    private void init() {
        ManagedChannel managedChannel = ManagedChannelBuilder
                .forAddress("localhost", 7777).usePlaintext().build();

        userServiceBlockingStub =
                UserServiceGrpc.newBlockingStub(managedChannel);
    }

    public User createUser(UserDto userDto) {
        return userServiceBlockingStub.createUser(userDto);
    }

    public User getUser(UserId id) {
        return userServiceBlockingStub.getUser(id);
    }

    public Users getUsers() {
        return userServiceBlockingStub.getUsers(Empty.newBuilder().build());
    }

    public User updateUser(User user) {
        return userServiceBlockingStub.updateUser(user);
    }

    public UserId deleteUser(UserId id) {
        return userServiceBlockingStub.deleteUser(id);
    }
}
