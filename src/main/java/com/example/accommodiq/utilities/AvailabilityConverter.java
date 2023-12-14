package com.example.accommodiq.utilities;

import com.example.accommodiq.domain.Availability;
import com.example.accommodiq.dtos.AvailabilityDto;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class AvailabilityConverter {
    public static Set<Availability> convertToEntities(Set<AvailabilityDto> availabilityDtos) {
        if (availabilityDtos == null) {
            return Set.of();
        }

        return availabilityDtos.stream()
                .map(Availability::new)
                .collect(Collectors.toSet());
    }
}
