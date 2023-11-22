package com.example.accommodiq.controllers;

import com.example.accommodiq.domain.Report;
import com.example.accommodiq.services.interfaces.IReportService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Collection<Report> getReports() {
        return service.getAll();
    }

    @PostMapping
    public Report insertReport(@RequestBody Report report) {
        return service.insert(report);
    }

    @PutMapping
    public Report updateReport(@RequestBody Report report) {
        return service.update(report);
    }

    @DeleteMapping("/{reportId}")
    public Report deleteReport(@PathVariable Long reportId) {
        return service.delete(reportId);
    }

    @DeleteMapping
    public void deleteAll() {
        service.deleteAll();
    }

}
