package com.yahyahouse.taskflow.util.transaction;

public interface TransactionAware {

    String getTransactionId();

    void setTransactionId(String transactionId);
}
