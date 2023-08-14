package com.sms.repository;

import com.sms.model.PlanBiznes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PlanBiznesRepository extends JpaRepository<PlanBiznes, Integer> {
    @Query("SELECT u FROM PlanBiznes u WHERE u.month LIKE %:month% and u.year LIKE %:year%")
    Optional<PlanBiznes> findByYearAndMonth(@Param("month") String month, @Param("year") String year);

    List<PlanBiznes> findByYear(String year);
}
