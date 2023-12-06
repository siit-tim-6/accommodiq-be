package com.example.accommodiq.specifications;

import com.example.accommodiq.domain.Accommodation;
import com.example.accommodiq.enums.PriceSearch;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class AccommodationSpecification {
    public static Specification<Accommodation> searchAndFilter(String title, String location, Integer guests) {
        return new Specification<Accommodation>() {
            @Override
            public Predicate toPredicate(Root<Accommodation> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (title != null) {
                    predicates.add(criteriaBuilder.like(root.get("title"), title));
                }
                if (location != null) {
                    predicates.add(criteriaBuilder.like(root.get("location"), location));
                }
                if (guests != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(root.get("minGuests"), guests), criteriaBuilder.lessThanOrEqualTo(root.get("minGuests"), guests)));
                }

                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }
        };
    }
}
