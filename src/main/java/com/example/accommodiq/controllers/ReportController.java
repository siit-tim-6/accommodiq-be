package com.example.accommodiq.controllers;

import com.example.accommodiq.domain.Report;
import com.example.accommodiq.dtos.ReportModificationDto;
import com.example.accommodiq.services.interfaces.feedback.IReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/reports")
public class ReportController {

    final
    IReportService service;

    @Autowired
    public ReportController(IReportService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Get all reports")
    public Collection<Report> getReports() {
        return service.getAll();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('HOST') or hasAuthority('GUEST')")
    @Operation(summary = "Create report")
    public ReportModificationDto insertReport(@RequestBody ReportModificationDto reportModificationDto) {
        return service.insert(reportModificationDto);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Update report")
    public ReportModificationDto updateReport(@RequestBody ReportModificationDto reportModificationDto) {
        return service.update(reportModificationDto);
    }

    @DeleteMapping("/{reportId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Delete report")
    public Report deleteReport(@Parameter(description = "Id of report to be deleted") @PathVariable Long reportId) {
        return service.delete(reportId);
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Delete all reports")
    public void deleteAll() {
        service.deleteAll();
    }

}
