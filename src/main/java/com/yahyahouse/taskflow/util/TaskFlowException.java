package com.yahyahouse.taskflow.util;

import lombok.Getter;

import java.io.Serial;

@Getter
public class TaskFlowException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;
    private final int httpStatusCode;


    public TaskFlowException(String message, Throwable throwable, int httpStatusCode) {
        super(message, throwable);
        this.httpStatusCode = httpStatusCode;
    }
}
