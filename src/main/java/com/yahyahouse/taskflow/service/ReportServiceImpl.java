package com.yahyahouse.taskflow.service;

import com.yahya.commonlogger.Loggable;
import com.yahyahouse.taskflow.model.response.ReportSummaryResponse;
import com.yahyahouse.taskflow.model.response.StatusCountResponse;
import com.yahyahouse.taskflow.model.response.TaskResponse;
import com.yahyahouse.taskflow.model.TaskMapper;
import com.yahyahouse.taskflow.model.entity.Task;
import com.yahyahouse.taskflow.model.enums.TaskStatus;
import com.yahyahouse.taskflow.repository.StatusCountProjection;
import com.yahyahouse.taskflow.repository.TaskRepository;
import com.yahyahouse.taskflow.util.TaskFlowException;
import com.yahyahouse.taskflow.util.ValidationUtil;
import com.yahyahouse.taskflow.util.transaction.TransactionContext;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Loggable
public class ReportServiceImpl implements ReportService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public ReportServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    @Override
    public ReportSummaryResponse getSummary(String transactionId) {
        try {
            ValidationUtil.validateTransactionId(transactionId);
            applyTransactionId(transactionId);
            List<Task> tasks = taskRepository.findAll();
            long total = tasks.size();

            Map<TaskStatus, Long> counts = tasks.stream()
                    .collect(Collectors.groupingBy(Task::getStatus, Collectors.counting()));

            long todo = counts.getOrDefault(TaskStatus.TODO, 0L);
            long inProgress = counts.getOrDefault(TaskStatus.IN_PROGRESS, 0L);
            long done = counts.getOrDefault(TaskStatus.DONE, 0L);

            double completionRate = total == 0
                    ? 0.0
                    : tasks.stream().filter(task -> task.getStatus() == TaskStatus.DONE).count() / (double) total;

            ReportSummaryResponse response = new ReportSummaryResponse();
            response.setTotal(total);
            response.setTodo(todo);
            response.setInProgress(inProgress);
            response.setDone(done);
            response.setCompletionRate(completionRate);
            response.setTransactionId(transactionId);
            return response;
        } catch (TaskFlowException ex) {
            throw ex;
        } catch (DataAccessException ex) {
            throw new TaskFlowException("Database error", ex, HttpStatus.INTERNAL_SERVER_ERROR.value());
        } catch (Exception ex) {
            throw new TaskFlowException("Internal server error", ex, HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @Override
    public List<StatusCountResponse> getStatusCount(String transactionId) {
        try {
            ValidationUtil.validateTransactionId(transactionId);
            applyTransactionId(transactionId);
            return taskRepository.countByStatusNative().stream()
                    .sorted(Comparator.comparing(item -> TaskStatus.valueOf(item.getStatus()).ordinal()))
                    .map(item -> toStatusCountResponse(item, transactionId))
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
    public List<TaskResponse> getOverdueTasks(String transactionId) {
        try {
            ValidationUtil.validateTransactionId(transactionId);
            applyTransactionId(transactionId);
            return taskRepository.findOverdueTasks(LocalDate.now()).stream()
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

    private StatusCountResponse toStatusCountResponse(StatusCountProjection projection, String transactionId) {
        return new StatusCountResponse(
                transactionId,
                TaskStatus.valueOf(projection.getStatus()),
                projection.getTotal() == null ? 0 : projection.getTotal()


        );
    }

    private void applyTransactionId(String transactionId) {
        TransactionContext.set(transactionId);
    }
}
