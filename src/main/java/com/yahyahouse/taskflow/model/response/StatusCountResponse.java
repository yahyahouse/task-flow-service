package com.yahyahouse.taskflow.model.response;

import com.yahyahouse.taskflow.model.enums.TaskStatus;
import com.yahyahouse.taskflow.util.transaction.TransactionAware;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class StatusCountResponse implements TransactionAware {

    private String transactionId;

    private TaskStatus status;
    private long total;


}
