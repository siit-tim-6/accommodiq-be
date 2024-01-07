package com.example.accommodiq.specifications;

import com.example.accommodiq.domain.Reservation;
import com.example.accommodiq.enums.ReservationStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ReservationSpecification {
    public static Specification<Reservation> searchAndFilter(long guestId, String title, Long startDate, Long endDate, ReservationStatus status) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.equal(root.get("guest").get("id"), guestId));

            if (title != null) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("accommodation").get("title")), "%" + title.toLowerCase() + "%"));
            }

            if (startDate != null && endDate != null) {
                predicates.add(criteriaBuilder.and(
                        criteriaBuilder.lessThanOrEqualTo(root.get("startDate"), startDate),
                        criteriaBuilder.greaterThanOrEqualTo(root.get("endDate"), endDate)
                ));
            }

            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}
