package com.example.accommodiq.dtos;

import java.util.Date;

public class HostReservationDto {
    private Long id;
    private String accommodationTitle;
    private String guestName;
    private Date fromDate;
    private Date toDate;

    public HostReservationDto(Long id, String accommodationTitle, String guestName, Date fromDate, Date toDate) {
        this.id = id;
        this.accommodationTitle = accommodationTitle;
        this.guestName = guestName;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccommodationTitle() {
        return accommodationTitle;
    }

    public void setAccommodationTitle(String accommodationTitle) {
        this.accommodationTitle = accommodationTitle;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }
}
