package com.example.accommodiq.repositories;

import com.example.accommodiq.domain.Host;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface HostRepository extends JpaRepository<Host, Long> {
    @Query(value = "SELECT * FROM user WHERE id IN (SELECT host_id FROM host_reviews WHERE reviews_id IN :reviewIds)", nativeQuery = true)

    Collection<Host> findHostsContainingReviews(@Param("reviewIds") Collection<Long> reviewIds);
}
