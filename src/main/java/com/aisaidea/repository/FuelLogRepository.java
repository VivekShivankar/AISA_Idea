package com.aisaidea.repository;

import com.aisaidea.model.FuelLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FuelLogRepository extends JpaRepository<FuelLog, Long> {

    List<FuelLog> findByUserIdOrderByDateDesc(Long userId);
    List<FuelLog> findTop5ByUserIdOrderByDateDesc(Long userId);

    @Query("SELECT AVG(f.mileage) FROM FuelLog f WHERE f.user.id = :uid")
    Double avgMileage(@Param("uid") Long uid);

    @Query("SELECT SUM(f.totalCost) FROM FuelLog f WHERE f.user.id = :uid")
    Double totalCost(@Param("uid") Long uid);

    @Query("SELECT SUM(f.distance) FROM FuelLog f WHERE f.user.id = :uid")
    Double totalDist(@Param("uid") Long uid);

    @Query("SELECT MAX(f.mileage) FROM FuelLog f WHERE f.user.id = :uid")
    Double maxMileage(@Param("uid") Long uid);

    @Query("SELECT MIN(f.mileage) FROM FuelLog f WHERE f.user.id = :uid")
    Double minMileage(@Param("uid") Long uid);

    @Query("SELECT COUNT(f) FROM FuelLog f WHERE f.user.id = :uid")
    Long countLogs(@Param("uid") Long uid);

    @Query("SELECT MAX(f.currOdometer) FROM FuelLog f WHERE f.user.id = :uid")
    Double latestOdo(@Param("uid") Long uid);

    @Query("SELECT AVG(f.mileage) FROM FuelLog f WHERE f.user.id = :uid AND f.roadType = :r")
    Double avgByRoad(@Param("uid") Long uid, @Param("r") String r);

    @Query("SELECT AVG(f.mileage) FROM FuelLog f WHERE f.user.id = :uid AND f.trafficLevel = :t")
    Double avgByTraffic(@Param("uid") Long uid, @Param("t") String t);

    @Query("SELECT SUM(f.fuelLiters) FROM FuelLog f WHERE f.user.id = :uid")
    Double totalFuel(@Param("uid") Long uid);
}
