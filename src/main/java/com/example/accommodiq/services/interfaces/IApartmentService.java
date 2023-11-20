package com.example.accommodiq.services.interfaces;

import com.example.accommodiq.domain.Apartment;

import java.util.Collection;
import java.util.List;

public interface IApartmentService {
    public Collection<Apartment> getAll();

    public Apartment findApartment(Long apartmentId);

    public Apartment insert(Apartment apartment);

    public Apartment update(Apartment apartment);

    public Apartment delete(Long apartmentId);

    public void deleteAll();

    List<Apartment> findApartmentsByOwnerId(Long ownerId);

    public Apartment setApartmentAcceptance(Long apartmentId, boolean isAccepted);
}
