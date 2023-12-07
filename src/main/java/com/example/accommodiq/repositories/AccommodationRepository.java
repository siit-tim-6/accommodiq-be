package com.example.accommodiq.repositories;

import com.example.accommodiq.domain.Accommodation;
import com.example.accommodiq.dtos.AccommodationListDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {

    Collection<Accommodation> findByHostId(Long hostId);
}
