package com.yahyahouse.taskflow.controller;

import com.yahyahouse.taskflow.model.response.ReportSummaryResponse;
import com.yahyahouse.taskflow.model.response.StatusCountResponse;
import com.yahyahouse.taskflow.model.response.TaskResponse;
import com.yahyahouse.taskflow.service.ReportService;
import com.yahyahouse.taskflow.util.transaction.TransactionContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
@Tag(name = "Reports", description = "Reporting endpoints")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/summary")
    @Operation(summary = "Summary Report", description = "Get task summary report.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Summary report",
                    content = @Content(schema = @Schema(implementation = ReportSummaryResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(schema = @Schema(implementation = com.yahyahouse.taskflow.util.ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = com.yahyahouse.taskflow.util.ErrorResponse.class)))
    })
    public ResponseEntity<ReportSummaryResponse> getSummary(
            @Parameter(description = "Transaction id for tracing", example = "TEST202602220001", required = true)
            @RequestHeader(value = TransactionContext.HEADER_NAME, required = false) String transactionId
    ) {
        return ResponseEntity.ok(reportService.getSummary(transactionId));
    }

    @GetMapping("/status-count")
    @Operation(summary = "Status Count Report", description = "Get total tasks by status.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status count report",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = StatusCountResponse.class)))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(schema = @Schema(implementation = com.yahyahouse.taskflow.util.ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = com.yahyahouse.taskflow.util.ErrorResponse.class)))
    })
    public ResponseEntity<List<StatusCountResponse>> getStatusCount(
            @Parameter(description = "Transaction id for tracing", example = "TEST202602220001", required = true)
            @RequestHeader(value = TransactionContext.HEADER_NAME, required = false) String transactionId
    ) {
        return ResponseEntity.ok(reportService.getStatusCount(transactionId));
    }

    @GetMapping("/overdue")
    @Operation(summary = "Overdue Tasks Report", description = "Get overdue task list.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Overdue tasks",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = TaskResponse.class)))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(schema = @Schema(implementation = com.yahyahouse.taskflow.util.ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = com.yahyahouse.taskflow.util.ErrorResponse.class)))
    })
    public ResponseEntity<List<TaskResponse>> getOverdueTasks(
            @Parameter(description = "Transaction id for tracing", example = "TEST202602220001", required = true)
            @RequestHeader(value = TransactionContext.HEADER_NAME, required = false) String transactionId
    ) {
        return ResponseEntity.ok(reportService.getOverdueTasks(transactionId));
    }
}
