package com.example.userservice.grpcservice;

import io.grpc.Status;
import org.lognet.springboot.grpc.recovery.GRpcExceptionHandler;
import org.lognet.springboot.grpc.recovery.GRpcExceptionScope;
import org.lognet.springboot.grpc.recovery.GRpcServiceAdvice;

import java.util.NoSuchElementException;

@GRpcServiceAdvice
public class UserServiceAdvice {

    @GRpcExceptionHandler
    public Status noSuchElementExceptionHandler(NoSuchElementException exception, GRpcExceptionScope scope) {
        return Status.NOT_FOUND;
    }
}
