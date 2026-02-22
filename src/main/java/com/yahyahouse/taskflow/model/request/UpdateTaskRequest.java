package com.yahyahouse.taskflow.model.request;

import com.yahyahouse.taskflow.model.enums.TaskPriority;
import com.yahyahouse.taskflow.model.enums.TaskStatus;
import com.yahyahouse.taskflow.util.transaction.TransactionAware;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UpdateTaskRequest implements TransactionAware {

    private String transactionId;

    @NotBlank(message = "title is required")
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private LocalDate dueDate;

}
