package com.example.accommodiq.services.interfaces.feedback;

import com.example.accommodiq.domain.Report;
import com.example.accommodiq.dtos.MessageDto;
import com.example.accommodiq.dtos.ReportCardDto;
import com.example.accommodiq.dtos.ReportDto;
import com.example.accommodiq.dtos.ReportModificationDto;
import com.example.accommodiq.enums.AccountStatus;

import java.util.Collection;

public interface IReportService {
    Collection<ReportCardDto> getAll();

    Report findReport(Long reportId);

    Report insert(Report report);

    ReportModificationDto insert(ReportModificationDto report);

    ReportModificationDto update(ReportModificationDto report);

    MessageDto delete(Long reportId);

    void deleteAll();

    MessageDto reportUser(Long reportedUserId, ReportDto reportDto);

    void deleteByReportedUserId(Long id);

    void deleteByReportingUserId(Long id);

    MessageDto changeUserStatus(Long userId, AccountStatus status);
}
