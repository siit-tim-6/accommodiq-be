package com.example.accommodiq.services.interfaces;

import com.example.accommodiq.domain.Accommodation;

import java.util.Collection;

public interface IAccommodationService {
    public Collection<Accommodation> findAll();
}
