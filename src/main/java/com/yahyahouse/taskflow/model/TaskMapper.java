package com.yahyahouse.taskflow.model;

import com.yahyahouse.taskflow.model.response.TaskResponse;
import com.yahyahouse.taskflow.model.entity.Task;
import com.yahyahouse.taskflow.util.CommonUtil;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public TaskResponse toResponse(Task task, String transactionId) {
        TaskResponse response = new TaskResponse();
        response.setId(task.getId());
        response.setTitle(task.getTitle());
        response.setDescription(task.getDescription());
        response.setStatus(task.getStatus());
        response.setPriority(task.getPriority());
        response.setDueDate(CommonUtil.formatDate(task.getDueDate()));
        response.setCreatedAt(CommonUtil.formatDateTime(task.getCreatedAt()));
        response.setUpdatedAt(CommonUtil.formatDateTime(task.getUpdatedAt()));
        response.setTransactionId(transactionId);
        return response;
    }
}

