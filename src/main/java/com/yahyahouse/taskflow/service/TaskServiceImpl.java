package com.yahyahouse.taskflow.service;

import com.yahya.commonlogger.Loggable;
import com.yahyahouse.taskflow.model.request.CreateTaskRequest;
import com.yahyahouse.taskflow.model.request.UpdateTaskRequest;
import com.yahyahouse.taskflow.model.request.UpdateTaskStatusRequest;
import com.yahyahouse.taskflow.model.response.TaskResponse;
import com.yahyahouse.taskflow.model.TaskMapper;
import com.yahyahouse.taskflow.model.entity.Task;
import com.yahyahouse.taskflow.model.enums.TaskPriority;
import com.yahyahouse.taskflow.model.enums.TaskStatus;
import com.yahyahouse.taskflow.repository.TaskRepository;
import com.yahyahouse.taskflow.util.ResourceNotFoundException;
import com.yahyahouse.taskflow.util.TaskFlowException;
import com.yahyahouse.taskflow.util.ValidationUtil;
import com.yahyahouse.taskflow.util.transaction.TransactionContext;

import java.time.LocalDate;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Loggable
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    @Override
    public TaskResponse createTask(String transactionId, CreateTaskRequest request) {
        try {
            ValidationUtil.validateTransactionId(transactionId);
            applyTransactionId(transactionId);
            ValidationUtil.validateCreateTaskRequest(request, transactionId);
            Task task = new Task();
            task.setTitle(request.getTitle());
            task.setDescription(request.getDescription());
            task.setStatus(request.getStatus());
            task.setPriority(request.getPriority());
            task.setDueDate(request.getDueDate());
            Task savedTask = taskRepository.save(task);
            return taskMapper.toResponse(savedTask, transactionId);
        } catch (TaskFlowException ex) {
            throw ex;
        } catch (DataAccessException ex) {
            throw new TaskFlowException("Database error", ex, HttpStatus.INTERNAL_SERVER_ERROR.value());
        } catch (Exception ex) {
            throw new TaskFlowException("Internal server error", ex, HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @Override
    public List<TaskResponse> getTasks(
            String transactionId,
            TaskStatus status,
            TaskPriority priority,
            String keyword,
            LocalDate dueDateFrom,
            LocalDate dueDateTo
    ) {
        try {
            ValidationUtil.validateTransactionId(transactionId);
            applyTransactionId(transactionId);
            String normalizedKeyword = (keyword == null || keyword.isBlank()) ? null : keyword.trim();
            return taskRepository.findByFilters(status, priority, normalizedKeyword, dueDateFrom, dueDateTo)
                    .stream()
                    .map(task -> taskMapper.toResponse(task, transactionId))
                    .toList();
        } catch (TaskFlowException ex) {
            throw ex;
        } catch (DataAccessException ex) {
            throw new TaskFlowException("Database error", ex, HttpStatus.INTERNAL_SERVER_ERROR.value());
        } catch (Exception ex) {
            throw new TaskFlowException("Internal server error", ex, HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @Override
    public TaskResponse getTaskById(String transactionId, Long id) {
        try {
            ValidationUtil.validateTransactionId(transactionId);
            applyTransactionId(transactionId);
            ValidationUtil.validateTaskId(id);
            Task task = findTaskOrThrow(id);
            return taskMapper.toResponse(task, transactionId);
        } catch (TaskFlowException ex) {
            throw ex;
        } catch (ResourceNotFoundException ex) {
            throw ex;
        } catch (DataAccessException ex) {
            throw new TaskFlowException("Database error", ex, HttpStatus.INTERNAL_SERVER_ERROR.value());
        } catch (Exception ex) {
            throw new TaskFlowException("Internal server error", ex, HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @Override
    public TaskResponse updateTask(String transactionId, Long id, UpdateTaskRequest request) {
        try {
            ValidationUtil.validateTransactionId(transactionId);
            applyTransactionId(transactionId);
            ValidationUtil.validateTaskId(id);
            ValidationUtil.validateUpdateTaskRequest(request, transactionId);
            request.setTransactionId(transactionId);
            Task task = findTaskOrThrow(id);
            task.setTitle(request.getTitle());
            task.setDescription(request.getDescription());
            task.setStatus(request.getStatus() != null ? request.getStatus() : task.getStatus());
            task.setPriority(request.getPriority() != null ? request.getPriority() : task.getPriority());
            task.setDueDate(request.getDueDate());
            Task updatedTask = taskRepository.save(task);
            return taskMapper.toResponse(updatedTask, transactionId);
        } catch (TaskFlowException ex) {
            throw ex;
        } catch (ResourceNotFoundException ex) {
            throw ex;
        } catch (DataAccessException ex) {
            throw new TaskFlowException("Database error", ex, HttpStatus.INTERNAL_SERVER_ERROR.value());
        } catch (Exception ex) {
            throw new TaskFlowException("Internal server error", ex, HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @Override
    public TaskResponse updateTaskStatus(String transactionId, Long id, UpdateTaskStatusRequest request) {
        try {
            ValidationUtil.validateTransactionId(transactionId);
            applyTransactionId(transactionId);
            ValidationUtil.validateTaskId(id);
            ValidationUtil.validateUpdateTaskStatusRequest(request, transactionId);
            request.setTransactionId(transactionId);
            Task task = findTaskOrThrow(id);
            task.setStatus(request.getStatus());
            Task updatedTask = taskRepository.save(task);
            return taskMapper.toResponse(updatedTask, transactionId);
        } catch (TaskFlowException ex) {
            throw ex;
        } catch (ResourceNotFoundException ex) {
            throw ex;
        } catch (DataAccessException ex) {
            throw new TaskFlowException("Database error", ex, HttpStatus.INTERNAL_SERVER_ERROR.value());
        } catch (Exception ex) {
            throw new TaskFlowException("Internal server error", ex, HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @Override
    public void deleteTask(String transactionId, Long id) {
        try {
            ValidationUtil.validateTransactionId(transactionId);
            applyTransactionId(transactionId);
            ValidationUtil.validateTaskId(id);
            Task task = findTaskOrThrow(id);
            taskRepository.delete(task);
        } catch (TaskFlowException ex) {
            throw ex;
        } catch (ResourceNotFoundException ex) {
            throw ex;
        } catch (DataAccessException ex) {
            throw new TaskFlowException("Database error", ex, HttpStatus.INTERNAL_SERVER_ERROR.value());
        } catch (Exception ex) {
            throw new TaskFlowException("Internal server error", ex, HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    private Task findTaskOrThrow(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
    }

    private void applyTransactionId(String transactionId) {
        TransactionContext.set(transactionId);
    }
}
