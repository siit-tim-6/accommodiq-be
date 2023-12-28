package com.example.accommodiq.controllers;

import com.example.accommodiq.domain.Report;
import com.example.accommodiq.dtos.ReportModificationDto;
import com.example.accommodiq.services.interfaces.feedback.IReportService;
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
    public Collection<Report> getReports() {
        return service.getAll();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('HOST') or hasAuthority('GUEST')")
    public ReportModificationDto insertReport(@RequestBody ReportModificationDto reportModificationDto) {
        return service.insert(reportModificationDto);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ReportModificationDto updateReport(@RequestBody ReportModificationDto reportModificationDto) {
        return service.update(reportModificationDto);
    }

    @DeleteMapping("/{reportId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Report deleteReport(@PathVariable Long reportId) {
        return service.delete(reportId);
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteAll() {
        service.deleteAll();
    }

}
