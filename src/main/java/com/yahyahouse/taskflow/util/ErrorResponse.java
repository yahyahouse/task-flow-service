package com.yahyahouse.taskflow.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yahyahouse.taskflow.util.transaction.TransactionAware;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse implements TransactionAware {

    private int httpStatus;

    @JsonProperty("ErrorMessage")
    private String errorMessage;

    private String transactionId;
}
