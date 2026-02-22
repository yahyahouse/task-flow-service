package com.yahyahouse.taskflow.service;

import com.yahyahouse.taskflow.model.request.CreateTaskRequest;
import com.yahyahouse.taskflow.model.request.UpdateTaskRequest;
import com.yahyahouse.taskflow.model.request.UpdateTaskStatusRequest;
import com.yahyahouse.taskflow.model.response.TaskResponse;
import com.yahyahouse.taskflow.model.enums.TaskPriority;
import com.yahyahouse.taskflow.model.enums.TaskStatus;
import java.time.LocalDate;
import java.util.List;

public interface TaskService {

    TaskResponse createTask(String transactionId, CreateTaskRequest request);

    List<TaskResponse> getTasks(
            String transactionId,
            TaskStatus status,
            TaskPriority priority,
            String keyword,
            LocalDate dueDateFrom,
            LocalDate dueDateTo
    );

    TaskResponse getTaskById(String transactionId, Long id);

    TaskResponse updateTask(String transactionId, Long id, UpdateTaskRequest request);

    TaskResponse updateTaskStatus(String transactionId, Long id, UpdateTaskStatusRequest request);

    void deleteTask(String transactionId, Long id);
}
