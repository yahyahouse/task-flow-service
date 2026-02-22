package com.yahyahouse.taskflow.service;

import com.yahyahouse.taskflow.model.response.ReportSummaryResponse;
import com.yahyahouse.taskflow.model.response.StatusCountResponse;
import com.yahyahouse.taskflow.model.response.TaskResponse;
import java.util.List;

public interface ReportService {

    ReportSummaryResponse getSummary(String transactionId);

    List<StatusCountResponse> getStatusCount(String transactionId);

    List<TaskResponse> getOverdueTasks(String transactionId);
}
