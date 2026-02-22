package com.yahyahouse.taskflow.model.response;

import com.yahyahouse.taskflow.model.enums.TaskPriority;
import com.yahyahouse.taskflow.model.enums.TaskStatus;
import com.yahyahouse.taskflow.util.transaction.TransactionAware;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class TaskResponse implements TransactionAware {

    private String transactionId;

    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private LocalDate dueDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
