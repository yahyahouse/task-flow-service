package com.yahyahouse.taskflow.controller;

import com.yahyahouse.taskflow.model.response.ReportSummaryResponse;
import com.yahyahouse.taskflow.model.response.StatusCountResponse;
import com.yahyahouse.taskflow.model.response.TaskResponse;
import com.yahyahouse.taskflow.service.ReportService;
import com.yahyahouse.taskflow.util.transaction.TransactionContext;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/summary")
    public ResponseEntity<ReportSummaryResponse> getSummary(
            @RequestHeader(value = TransactionContext.HEADER_NAME, required = false) String transactionId
    ) {
        return ResponseEntity.ok(reportService.getSummary(transactionId));
    }

    @GetMapping("/status-count")
    public ResponseEntity<List<StatusCountResponse>> getStatusCount(
            @RequestHeader(value = TransactionContext.HEADER_NAME, required = false) String transactionId
    ) {
        return ResponseEntity.ok(reportService.getStatusCount(transactionId));
    }

    @GetMapping("/overdue")
    public ResponseEntity<List<TaskResponse>> getOverdueTasks(
            @RequestHeader(value = TransactionContext.HEADER_NAME, required = false) String transactionId
    ) {
        return ResponseEntity.ok(reportService.getOverdueTasks(transactionId));
    }
}
