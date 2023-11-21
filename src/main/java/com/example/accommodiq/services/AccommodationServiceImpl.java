package com.example.accommodiq.services;

import com.example.accommodiq.domain.Accommodation;
import com.example.accommodiq.enums.PricingType;
import com.example.accommodiq.repositories.AccommodationRepository;
import com.example.accommodiq.services.interfaces.IAccommodationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.ResourceBundle;

@Service
public class AccommodationServiceImpl implements IAccommodationService {
    AccommodationRepository accommodationRepository;
    ResourceBundle bundle = ResourceBundle.getBundle("ValidationMessages", LocaleContextHolder.getLocale());

    @Autowired
    public AccommodationServiceImpl(AccommodationRepository accommodationRepository) {
        this.accommodationRepository = accommodationRepository;
    }

    @Override
    public Collection<Accommodation> findAll() {
        return null;
    }

    @Override
    public Accommodation findById(Long accommodationId) {
        Long id = 1L; // Replace with an actual ID
        String title = "Sample Accommodation";
        String description = "A cozy place to stay";
        String location = "City Center";
        String image = "sample_image.jpg";
        int minGuests = 1;
        int maxGuests = 4;
        String type = "Apartment";
        boolean accepted = true;
        PricingType pricingType = PricingType.PER_GUEST;
        int cancellationDeadline = 7;

        return new Accommodation(id, title, description, location, image, minGuests, maxGuests, type, accepted, pricingType, true, cancellationDeadline);
    }

    @Override
    public Accommodation acceptIncomingChanges(Long accommodationId) {
        return null;
    }
}
