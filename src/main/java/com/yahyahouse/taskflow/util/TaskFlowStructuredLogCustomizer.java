package com.yahyahouse.taskflow.util;

import com.yahya.commonlogger.StructuredLogCustomizer;
import java.util.Map;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class TaskFlowStructuredLogCustomizer implements StructuredLogCustomizer {

    @Override
    public void customize(
            Map<String, Object> payload,
            ProceedingJoinPoint joinPoint,
            Object result,
            long processTime,
            boolean success,
            Throwable throwable
    ) {
        Integer resolvedHttpStatusCode = resolveHttpStatusCode(throwable);
        if (resolvedHttpStatusCode != null) {
            payload.put("httpStatusCode", resolvedHttpStatusCode);
        }
    }

    private Integer resolveHttpStatusCode(Throwable throwable) {
        TaskFlowException taskFlowException = findTaskFlowException(throwable);
        if (taskFlowException != null) {
            return taskFlowException.getHttpStatusCode();
        }

        ResourceNotFoundException resourceNotFoundException = findResourceNotFoundException(throwable);
        if (resourceNotFoundException != null) {
            return HttpStatus.BAD_REQUEST.value();
        }

        return null;
    }

    private TaskFlowException findTaskFlowException(Throwable throwable) {
        Throwable current = throwable;
        int depth = 0;
        while (current != null && depth < 10) {
            if (current instanceof TaskFlowException taskFlowException) {
                return taskFlowException;
            }
            current = current.getCause();
            depth++;
        }
        return null;
    }

    private ResourceNotFoundException findResourceNotFoundException(Throwable throwable) {
        Throwable current = throwable;
        int depth = 0;
        while (current != null && depth < 10) {
            if (current instanceof ResourceNotFoundException resourceNotFoundException) {
                return resourceNotFoundException;
            }
            current = current.getCause();
            depth++;
        }
        return null;
    }
}
