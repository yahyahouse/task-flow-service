package com.yahyahouse.taskflow.controller;

import com.yahyahouse.taskflow.model.request.CreateTaskRequest;
import com.yahyahouse.taskflow.model.request.UpdateTaskRequest;
import com.yahyahouse.taskflow.model.request.UpdateTaskStatusRequest;
import com.yahyahouse.taskflow.model.response.TaskResponse;
import com.yahyahouse.taskflow.model.enums.TaskPriority;
import com.yahyahouse.taskflow.model.enums.TaskStatus;
import com.yahyahouse.taskflow.service.TaskService;
import com.yahyahouse.taskflow.util.transaction.TransactionContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Tasks", description = "Task management endpoints")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    @Operation(
            summary = "Create Task",
            description = "Create a new task. All create fields are mandatory and transactionId is taken from request header."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task created",
                    content = @Content(schema = @Schema(implementation = TaskResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(schema = @Schema(implementation = com.yahyahouse.taskflow.util.ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = com.yahyahouse.taskflow.util.ErrorResponse.class)))
    })
    public ResponseEntity<TaskResponse> createTask(
            @Parameter(description = "Transaction id for tracing", example = "TEST202602220001", required = true)
            @RequestHeader(value = TransactionContext.HEADER_NAME, required = false) String transactionId,
            @Valid @RequestBody CreateTaskRequest request
    ) {
        TaskResponse response = taskService.createTask(transactionId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Get Tasks", description = "Get list of tasks with optional filters.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task list",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = TaskResponse.class)))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(schema = @Schema(implementation = com.yahyahouse.taskflow.util.ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = com.yahyahouse.taskflow.util.ErrorResponse.class)))
    })
    public ResponseEntity<List<TaskResponse>> getTasks(
            @Parameter(description = "Transaction id for tracing", example = "TEST202602220001", required = true)
            @RequestHeader(value = TransactionContext.HEADER_NAME, required = false) String transactionId,
            @Parameter(description = "Filter by status", example = "TODO")
            @RequestParam(required = false) TaskStatus status,
            @Parameter(description = "Filter by priority", example = "HIGH")
            @RequestParam(required = false) TaskPriority priority,
            @Parameter(description = "Filter by keyword in title", example = "auth")
            @RequestParam(required = false) String keyword,
            @Parameter(description = "Filter due date from (yyyy-MM-dd)", example = "2026-02-20")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueDateFrom,
            @Parameter(description = "Filter due date to (yyyy-MM-dd)", example = "2026-03-10")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueDateTo
    ) {
        return ResponseEntity.ok(taskService.getTasks(transactionId, status, priority, keyword, dueDateFrom, dueDateTo));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Task By Id", description = "Get detail task by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task found",
                    content = @Content(schema = @Schema(implementation = TaskResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(schema = @Schema(implementation = com.yahyahouse.taskflow.util.ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Task not found",
                    content = @Content(schema = @Schema(implementation = com.yahyahouse.taskflow.util.ErrorResponse.class)))
    })
    public ResponseEntity<TaskResponse> getTaskById(
            @Parameter(description = "Transaction id for tracing", example = "TEST202602220001", required = true)
            @RequestHeader(value = TransactionContext.HEADER_NAME, required = false) String transactionId,
            @Parameter(description = "Task id", example = "1", required = true)
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(taskService.getTaskById(transactionId, id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Task", description = "Update task data by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task updated",
                    content = @Content(schema = @Schema(implementation = TaskResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(schema = @Schema(implementation = com.yahyahouse.taskflow.util.ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Task not found",
                    content = @Content(schema = @Schema(implementation = com.yahyahouse.taskflow.util.ErrorResponse.class)))
    })
    public ResponseEntity<TaskResponse> updateTask(
            @Parameter(description = "Transaction id for tracing", example = "TEST202602220001", required = true)
            @RequestHeader(value = TransactionContext.HEADER_NAME, required = false) String transactionId,
            @Parameter(description = "Task id", example = "1", required = true)
            @PathVariable Long id,
            @Valid @RequestBody UpdateTaskRequest request
    ) {
        return ResponseEntity.ok(taskService.updateTask(transactionId, id, request));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update Task Status", description = "Update only task status by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task status updated",
                    content = @Content(schema = @Schema(implementation = TaskResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(schema = @Schema(implementation = com.yahyahouse.taskflow.util.ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Task not found",
                    content = @Content(schema = @Schema(implementation = com.yahyahouse.taskflow.util.ErrorResponse.class)))
    })
    public ResponseEntity<TaskResponse> updateTaskStatus(
            @Parameter(description = "Transaction id for tracing", example = "TEST202602220001", required = true)
            @RequestHeader(value = TransactionContext.HEADER_NAME, required = false) String transactionId,
            @Parameter(description = "Task id", example = "1", required = true)
            @PathVariable Long id,
            @Valid @RequestBody UpdateTaskStatusRequest request
    ) {
        return ResponseEntity.ok(taskService.updateTaskStatus(transactionId, id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Task", description = "Delete task by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Task deleted"),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(schema = @Schema(implementation = com.yahyahouse.taskflow.util.ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Task not found",
                    content = @Content(schema = @Schema(implementation = com.yahyahouse.taskflow.util.ErrorResponse.class)))
    })
    public ResponseEntity<Void> deleteTask(
            @Parameter(description = "Transaction id for tracing", example = "TEST202602220001", required = true)
            @RequestHeader(value = TransactionContext.HEADER_NAME, required = false) String transactionId,
            @Parameter(description = "Task id", example = "1", required = true)
            @PathVariable Long id
    ) {
        taskService.deleteTask(transactionId, id);
        return ResponseEntity.noContent().build();
    }
}
