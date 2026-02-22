package com.yahyahouse.taskflow.model.request;

import com.yahyahouse.taskflow.model.enums.TaskStatus;
import com.yahyahouse.taskflow.util.transaction.TransactionAware;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateTaskStatusRequest implements TransactionAware {

    private String transactionId;

    @NotNull(message = "status is required")
    private TaskStatus status;

}
