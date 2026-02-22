package com.yahyahouse.taskflow.util;

import com.yahyahouse.taskflow.model.request.CreateTaskRequest;
import com.yahyahouse.taskflow.model.request.UpdateTaskRequest;
import com.yahyahouse.taskflow.model.request.UpdateTaskStatusRequest;
import org.springframework.http.HttpStatus;

public final class ValidationUtil {

    private ValidationUtil() {
    }

    public static void validateTransactionId(String transactionId) {
        validateRequiredText(transactionId, "transactionId");
    }

    public static void validateTaskId(Long id) {
        if (id == null || id <= 0) {
            throw badRequest("id is required");
        }
    }

    public static void validateCreateTaskRequest(CreateTaskRequest request, String transactionId) {
        if (request == null) {
            throw badRequest("request is required");
        }
        validateRequiredText(transactionId, "transactionId");
        validateRequiredText(request.getTitle(), "title");
        validateRequiredText(request.getDescription(), "description");
        validateRequiredObject(request.getStatus(), "status");
        validateRequiredObject(request.getPriority(), "priority");
        validateRequiredObject(request.getDueDate(), "dueDate");
    }

    public static void validateUpdateTaskRequest(UpdateTaskRequest request, String transactionId) {
        if (request == null) {
            throw badRequest("request is required");
        }
        validateRequiredText(transactionId, "transactionId");
        validateRequiredText(request.getTitle(), "title");
    }

    public static void validateUpdateTaskStatusRequest(UpdateTaskStatusRequest request, String transactionId) {
        if (request == null) {
            throw badRequest("request is required");
        }
        validateRequiredText(transactionId, "transactionId");
        if (request.getStatus() == null) {
            throw badRequest("status is required");
        }
    }

    public static void validateRequiredText(String value, String fieldName) {
        if (!CommonUtil.hasText(value)) {
            throw badRequest(fieldName + " is required");
        }
    }

    public static void validateRequiredObject(Object value, String fieldName) {
        if (value == null) {
            throw badRequest(fieldName + " is required");
        }
    }

    private static TaskFlowException badRequest(String message) {
        return new TaskFlowException(message, null, HttpStatus.BAD_REQUEST.value());
    }
}
