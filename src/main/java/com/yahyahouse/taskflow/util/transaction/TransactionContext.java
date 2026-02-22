package com.yahyahouse.taskflow.util.transaction;

import java.util.UUID;
import org.slf4j.MDC;
import org.springframework.util.StringUtils;

public final class TransactionContext {

    public static final String HEADER_NAME = "X-Transaction-Id";
    public static final String MDC_KEY = "transactionId";

    private TransactionContext() {
    }

    public static void set(String transactionId) {
        if (StringUtils.hasText(transactionId)) {
            MDC.put(MDC_KEY, transactionId.trim());
        }
    }

    public static String current() {
        return MDC.get(MDC_KEY);
    }

    public static String currentOrGenerate() {
        String transactionId = current();
        if (!StringUtils.hasText(transactionId)) {
            transactionId = UUID.randomUUID().toString();
            set(transactionId);
        }
        return transactionId;
    }

    public static void clear() {
        MDC.remove(MDC_KEY);
    }
}
