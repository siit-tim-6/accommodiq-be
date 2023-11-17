package com.example.accommodiq.services;

import com.example.accommodiq.domain.Apartment;
import com.example.accommodiq.domain.Apartment;
import com.example.accommodiq.repositories.ApartmentRepository;
import com.example.accommodiq.repositories.ApartmentRepository;
import com.example.accommodiq.services.interfaces.IApartmentService;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.Optional;
import java.util.ResourceBundle;

@Service
public class ApartmentServiceImpl implements IApartmentService {

    final
    ApartmentRepository allApartments;
    private final ResourceBundle bundle = ResourceBundle.getBundle("ValidationMessages", LocaleContextHolder.getLocale());

    public ApartmentServiceImpl(ApartmentRepository allApartments) {
        this.allApartments = allApartments;
    }

    @Override
    public Collection<Apartment> getAll() {
        return allApartments.findAll();
    }

    @Override
    public Apartment findApartment(Long apartmentId) {
        Optional<Apartment> found = allApartments.findById(apartmentId);
        if (found.isEmpty()) {
            String value = bundle.getString("notFound");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, value);
        }
        return found.get();
    }

    @Override
    public Apartment insert(Apartment apartment) {
        try{
            allApartments.save(apartment);
            allApartments.flush();
            return apartment;
        } catch (ConstraintViolationException e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "error");
        }
    }

    @Override
    public Apartment update(Apartment apartment) {
        try {
            findApartment(apartment.getId()); // this will throw ResponseStatusException if Apartment is not found
            allApartments.save(apartment);
            allApartments.flush();
            return apartment;
        } catch (RuntimeException ex) {
            Throwable e = ex;
            Throwable c = null;
            while ((e != null) && !((c = e.getCause()) instanceof ConstraintViolationException) ) {
                e = (RuntimeException) c;
            }
            if ((c != null) && (c instanceof ConstraintViolationException)) {

                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "error2");
            }
            throw ex;
        }
    }

    @Override
    public Apartment delete(Long apartmentId) {
        Apartment apartment = findApartment(apartmentId);
        allApartments.delete(apartment);
        allApartments.flush();
        return apartment;
    }

    @Override
    public void deleteAll() {
        allApartments.deleteAll();
        allApartments.flush();
    }
}
