package com.example.accommodiq.services;

import com.example.accommodiq.domain.Accommodation;
import com.example.accommodiq.services.interfaces.IAccommodationService;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class AccommodationServiceImpl implements IAccommodationService {
    @Override
    public Collection<Accommodation> findAll() {
        return null;
    }

    @Override
    public Accommodation findById(Long accommodationId) {
        return null;
    }
}
