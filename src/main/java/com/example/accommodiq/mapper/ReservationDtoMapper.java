package com.example.accommodiq.mapper;

import com.example.accommodiq.domain.Reservation;
import com.example.accommodiq.dtos.ReservationDto;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Date;

@Component
public class ReservationDtoMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public ReservationDtoMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ReservationDto toDto(Reservation reservation) {
        return modelMapper.map(reservation, ReservationDto.class);
    }

    public Reservation toEntity(ReservationDto dto) {
        return modelMapper.map(dto, Reservation.class);
    }
}
