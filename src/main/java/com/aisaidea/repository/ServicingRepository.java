package com.aisaidea.repository;

import com.aisaidea.model.ServicingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ServicingRepository extends JpaRepository<ServicingRecord, Long> {

    List<ServicingRecord> findByUserIdOrderByServiceDateDesc(Long userId);

    @Query("SELECT SUM(s.serviceCost) FROM ServicingRecord s WHERE s.user.id = :uid")
    Double totalCost(@Param("uid") Long uid);

    @Query("SELECT MAX(s.odometerAtService) FROM ServicingRecord s WHERE s.user.id = :uid")
    Double lastOdo(@Param("uid") Long uid);

    @Query("SELECT COUNT(s) FROM ServicingRecord s WHERE s.user.id = :uid")
    Long countSvc(@Param("uid") Long uid);
}
