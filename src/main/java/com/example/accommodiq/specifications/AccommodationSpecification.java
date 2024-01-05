package com.example.accommodiq.specifications;

import com.example.accommodiq.domain.Accommodation;
import com.example.accommodiq.enums.AccommodationStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AccommodationSpecification {
    public static Specification<Accommodation> searchAndFilter(String title, String location, Integer guests, String type, Set<String> benefits) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.equal((root.get("status")), AccommodationStatus.ACCEPTED));

            if (title != null) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
            }
            if (location != null) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("location").get("address")), "%" + location.toLowerCase() + "%"));
            }
            if (guests != null) {
                predicates.add(criteriaBuilder.and(
                        criteriaBuilder.lessThanOrEqualTo(root.get("minGuests"), guests),
                        criteriaBuilder.greaterThanOrEqualTo(root.get("maxGuests"), guests)
                ));
            }
            if (type != null) {
                predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("type")), type.toLowerCase()));
            }
            if (benefits != null) {
                for (String benefit : benefits) {
                    predicates.add(criteriaBuilder.isMember(benefit, root.get("benefits")));
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
