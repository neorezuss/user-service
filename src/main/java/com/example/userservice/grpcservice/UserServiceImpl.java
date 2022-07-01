package com.example.userservice.grpcservice;

import com.example.userservice.User;
import com.example.userservice.UserDto;
import com.example.userservice.UserId;
import com.example.userservice.UserServiceGrpc;
import com.example.userservice.Users;
import com.example.userservice.model.UserModel;
import com.example.userservice.repository.UserModelRepository;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.lognet.springboot.grpc.GRpcService;

import java.util.List;
import java.util.NoSuchElementException;

@GRpcService
@RequiredArgsConstructor
public class UserServiceImpl extends UserServiceGrpc.UserServiceImplBase {
    private final UserModelRepository userModelRepository;


    @Override
    public void getUser(UserId request, StreamObserver<User> responseObserver) {
        UserModel userModel = userModelRepository.findById(request.getId())
                .orElseThrow(() -> new NoSuchElementException("User with id " + request.getId() + " not found!"));

        User user = convertToUser(userModel);
        responseObserver.onNext(user);
        responseObserver.onCompleted();
    }

    @Override
    public void getUsers(Empty request, StreamObserver<Users> responseObserver) {
        List<UserModel> userModels = userModelRepository.findAll();

        Users users = convertToUsers(userModels);
        responseObserver.onNext(users);
        responseObserver.onCompleted();
    }

    @Override
    public void createUser(UserDto request, StreamObserver<User> responseObserver) {
        UserModel userModel = UserModel.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .age(request.getAge())
                .build();

        userModelRepository.saveAndFlush(userModel);

        User user = convertToUser(userModel);
        responseObserver.onNext(user);
        responseObserver.onCompleted();
    }

    @Override
    public void updateUser(User request, StreamObserver<User> responseObserver) {
        if (!userModelRepository.existsById(request.getId())) {
            throw new NoSuchElementException("User with id " + request.getId() + " not found!");
        }

        UserModel userModel = UserModel.builder()
                .id(request.getId())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .age(request.getAge())
                .build();

        userModelRepository.saveAndFlush(userModel);

        User user = convertToUser(userModel);
        responseObserver.onNext(user);
        responseObserver.onCompleted();
    }

    @Override
    public void deleteUser(UserId request, StreamObserver<UserId> responseObserver) {
        if (!userModelRepository.existsById(request.getId())) {
            throw new NoSuchElementException("User with id " + request.getId() + " not found!");
        }

        userModelRepository.deleteById(request.getId());

        responseObserver.onNext(request);
        responseObserver.onCompleted();
    }

    private User convertToUser(UserModel userModel) {
        return User.newBuilder()
                .setId(userModel.getId())
                .setFirstName(userModel.getFirstName())
                .setLastName(userModel.getLastName())
                .setAge(userModel.getAge())
                .build();
    }

    private Users convertToUsers(List<UserModel> userModels) {
        return Users.newBuilder()
                .addAllUsers(userModels.stream().map(this::convertToUser).toList())
                .build();
    }
}
