package com.yahyahouse.taskflow.model.response;

import com.yahyahouse.taskflow.util.transaction.TransactionAware;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReportSummaryResponse implements TransactionAware {

    private String transactionId;

    private long total;
    private long todo;
    private long inProgress;
    private long done;
    private double completionRate;

}
