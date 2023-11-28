package com.example.accommodiq.controllers;

import com.example.accommodiq.domain.Report;
import com.example.accommodiq.dtos.ReportRequestDto;
import com.example.accommodiq.dtos.ReportResponseDto;
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
    public ReportResponseDto insertReport(@RequestBody ReportRequestDto reportRequestDto) {
        return service.insert(reportRequestDto);
    }

    @PutMapping
    public ReportResponseDto updateReport(@RequestBody ReportRequestDto reportRequestDto) {
        return service.update(reportRequestDto);
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
