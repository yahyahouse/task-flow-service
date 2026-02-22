package com.yahyahouse.taskflow.controller;

import com.yahyahouse.taskflow.model.request.CreateTaskRequest;
import com.yahyahouse.taskflow.model.request.UpdateTaskRequest;
import com.yahyahouse.taskflow.model.request.UpdateTaskStatusRequest;
import com.yahyahouse.taskflow.model.response.TaskResponse;
import com.yahyahouse.taskflow.model.enums.TaskPriority;
import com.yahyahouse.taskflow.model.enums.TaskStatus;
import com.yahyahouse.taskflow.service.TaskService;
import com.yahyahouse.taskflow.util.transaction.TransactionContext;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(
            @RequestHeader(value = TransactionContext.HEADER_NAME, required = false) String transactionId,
            @Valid @RequestBody CreateTaskRequest request
    ) {
        TaskResponse response = taskService.createTask(transactionId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getTasks(
            @RequestHeader(value = TransactionContext.HEADER_NAME, required = false) String transactionId,
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) TaskPriority priority,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueDateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueDateTo
    ) {
        return ResponseEntity.ok(taskService.getTasks(transactionId, status, priority, keyword, dueDateFrom, dueDateTo));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(
            @RequestHeader(value = TransactionContext.HEADER_NAME, required = false) String transactionId,
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(taskService.getTaskById(transactionId, id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(
            @RequestHeader(value = TransactionContext.HEADER_NAME, required = false) String transactionId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateTaskRequest request
    ) {
        return ResponseEntity.ok(taskService.updateTask(transactionId, id, request));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TaskResponse> updateTaskStatus(
            @RequestHeader(value = TransactionContext.HEADER_NAME, required = false) String transactionId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateTaskStatusRequest request
    ) {
        return ResponseEntity.ok(taskService.updateTaskStatus(transactionId, id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
            @RequestHeader(value = TransactionContext.HEADER_NAME, required = false) String transactionId,
            @PathVariable Long id
    ) {
        taskService.deleteTask(transactionId, id);
        return ResponseEntity.noContent().build();
    }
}
