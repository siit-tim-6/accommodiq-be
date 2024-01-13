package com.example.accommodiq.specifications;

import com.example.accommodiq.domain.Reservation;
import com.example.accommodiq.enums.ReservationStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class HostReservationSpecification {
    public static Specification<Reservation> searchAndFilter(long hostId, String title, Long startDate, Long endDate, ReservationStatus status) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.equal(root.get("accommodation").get("host").get("id"), hostId));

            if (title != null) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("accommodation").get("title")), "%" + title.toLowerCase() + "%"));
            }

            if (startDate != null && endDate != null) {
                predicates.add(criteriaBuilder.and(
                        criteriaBuilder.greaterThanOrEqualTo(root.get("startDate"), startDate),
                        criteriaBuilder.lessThanOrEqualTo(root.get("endDate"), endDate)
                ));
            }

            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}
