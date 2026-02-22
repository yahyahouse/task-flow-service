package com.yahyahouse.taskflow.util.transaction;

import com.yahyahouse.taskflow.util.CommonUtil;
import java.util.UUID;
import org.slf4j.MDC;

public final class TransactionContext {

    public static final String HEADER_NAME = "X-Transaction-Id";
    public static final String MDC_KEY = "transactionId";

    private TransactionContext() {
    }

    public static void set(String transactionId) {
        String normalizedTransactionId = CommonUtil.trimToNull(transactionId);
        if (normalizedTransactionId != null) {
            MDC.put(MDC_KEY, normalizedTransactionId);
        }
    }

    public static String current() {
        return MDC.get(MDC_KEY);
    }

    public static String currentOrGenerate() {
        String transactionId = current();
        if (!CommonUtil.hasText(transactionId)) {
            transactionId = UUID.randomUUID().toString();
            set(transactionId);
        }
        return transactionId;
    }

    public static void clear() {
        MDC.remove(MDC_KEY);
    }
}
