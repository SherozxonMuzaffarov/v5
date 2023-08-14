package com.sms.repository;

import com.sms.model.PlanUty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PlanUtyRepository extends JpaRepository<PlanUty, Integer> {
    //Bir oylik Plan
    @Query("SELECT u FROM PlanUty u WHERE u.month LIKE %:month% and u.year LIKE %:year%")
    Optional<PlanUty> findByYearAndMonth(@Param("month") String month, @Param("year") String year);

    //Jami oylik Plan (qoshib bitta qilib beradi)
//    @Query("SELECT sum(samDtKritiPlanUty) as samDtKritiPlanUty,sum(samDtPlatformaPlanUty) as samDtPlatformaPlanUty,sum(samDtPoluvagonPlanUty) as samDtPoluvagonPlanUty,sum(samDtSisternaPlanUty) as samDtSisternaPlanUty,sum(samDtBoshqaPlanUty) as samDtBoshqaPlanUty,sum(havDtKritiPlanUty) as havDtKritiPlanUty,sum(havDtPlatformaPlanUty) as havDtPlatformaPlanUty,sum(havDtPoluvagonPlanUty) as havDtPoluvagonPlanUty,sum(havDtSisternaPlanUty) as havDtSisternaPlanUty,sum(havDtBoshqaPlanUty) as havDtBoshqaPlanUty,sum(andjDtKritiPlanUty) as andjDtKritiPlanUty,sum(andjDtPlatformaPlanUty) as andjDtPlatformaPlanUty,sum(andjDtPoluvagonPlanUty) as andjDtPoluvagonPlanUty,sum(andjDtSisternaPlanUty) as andjDtSisternaPlanUty,sum(andjDtBoshqaPlanUty) as andjDtBoshqaPlanUty,sum(samKtKritiPlanUty) as samKtKritiPlanUty,sum(samKtPlatformaPlanUty) as samKtPlatformaPlanUty,sum(samKtPoluvagonPlanUty) as samKtPoluvagonPlanUty,sum(samKtSisternaPlanUty) as samKtSisternaPlanUty,sum(samKtBoshqaPlanUty) as samKtBoshqaPlanUty,sum(havKtKritiPlanUty) as havKtKritiPlanUty,sum(havKtPlatformaPlanUty) as havKtPlatformaPlanUty,sum(havKtPoluvagonPlanUty) as havKtPoluvagonPlanUty,sum(havKtSisternaPlanUty) as havKtSisternaPlanUty,sum(havKtBoshqaPlanUty) as havKtBoshqaPlanUty,sum(andjKtKritiPlanUty) as andjKtKritiPlanUty,sum(andjKtPlatformaPlanUty) as andjKtPlatformaPlanUty,sum(andjKtPoluvagonPlanUty) as andjKtPoluvagonPlanUty,sum(andjKtSisternaPlanUty) as andjKtSisternaPlanUty,sum(andjKtBoshqaPlanUty) as andjKtBoshqaPlanUty,sum(samKrpKritiPlanUty) as samKrpKritiPlanUty,sum(samKrpPlatformaPlanUty) as samKrpPlatformaPlanUty,sum(samKrpPoluvagonPlanUty) as samKrpPoluvagonPlanUty,sum(samKrpSisternaPlanUty) as samKrpSisternaPlanUty,sum(samKrpBoshqaPlanUty) as samKrpBoshqaPlanUty,sum(havKrpKritiPlanUty) as havKrpKritiPlanUty,sum(havKrpPlatformaPlanUty) as havKrpPlatformaPlanUty,sum(havKrpPoluvagonPlanUty) as havKrpPoluvagonPlanUty,sum(havKrpSisternaPlanUty) as havKrpSisternaPlanUty,sum(havKrpBoshqaPlanUty) as havKrpBoshqaPlanUty,sum(andjKrpKritiPlanUty) as andjKrpKritiPlanUty,sum(andjKrpPlatformaPlanUty) as andjKrpPlatformaPlanUty,sum(andjKrpPoluvagonPlanUty) as andjKrpPoluvagonPlanUty,sum(andjKrpSisternaPlanUty) as andjKrpSisternaPlanUty,sum(andjKrpBoshqaPlanUty) as andjKrpBoshqa FROM PlanUty u WHERE u.year LIKE %:year%")
//    @Query("SELECT " +
//            "sum(samDtKritiPlanUty) as samDtKritiPlanUty,sum(samDtPlatformaPlanUty) as samDtPlatformaPlanUty,sum(samDtPoluvagonPlanUty) as samDtPoluvagonPlanUty," +
//            "sum(samDtSisternaPlanUty) as samDtSisternaPlanUty,sum(samDtBoshqaPlanUty) as samDtBoshqaPlanUty,sum(havDtKritiPlanUty) as havDtKritiPlanUty," +
//            "sum(havDtPlatformaPlanUty) as havDtPlatformaPlanUty,sum(havDtPoluvagonPlanUty) as havDtPoluvagonPlanUty,sum(havDtSisternaPlanUty) as havDtSisternaPlanUty," +
//            "sum(havDtBoshqaPlanUty) as havDtBoshqaPlanUty,sum(andjDtKritiPlanUty) as andjDtKritiPlanUty,sum(andjDtPlatformaPlanUty) as andjDtPlatformaPlanUty," +
//            "sum(andjDtPoluvagonPlanUty) as andjDtPoluvagonPlanUty,sum(andjDtSisternaPlanUty) as andjDtSisternaPlanUty,sum(andjDtBoshqaPlanUty) as andjDtBoshqaPlanUty," +
//            "sum(samKtKritiPlanUty) as samKtKritiPlanUty,sum(samKtPlatformaPlanUty) as samKtPlatformaPlanUty,sum(samKtPoluvagonPlanUty) as samKtPoluvagonPlanUty," +
//            "sum(samKtSisternaPlanUty) as samKtSisternaPlanUty,sum(samKtBoshqaPlanUty) as samKtBoshqaPlanUty,sum(havKtKritiPlanUty) as havKtKritiPlanUty," +
//            "sum(havKtPlatformaPlanUty) as havKtPlatformaPlanUty,sum(havKtPoluvagonPlanUty) as havKtPoluvagonPlanUty,sum(havKtSisternaPlanUty) as havKtSisternaPlanUty," +
//            "sum(havKtBoshqaPlanUty) as havKtBoshqaPlanUty,sum(andjKtKritiPlanUty) as andjKtKritiPlanUty,sum(andjKtPlatformaPlanUty) as andjKtPlatformaPlanUty," +
//            "sum(andjKtPoluvagonPlanUty) as andjKtPoluvagonPlanUty,sum(andjKtSisternaPlanUty) as andjKtSisternaPlanUty,sum(andjKtBoshqaPlanUty) as andjKtBoshqaPlanUty," +
//            "sum(samKrpKritiPlanUty) as samKrpKritiPlanUty,sum(samKrpPlatformaPlanUty) as samKrpPlatformaPlanUty,sum(samKrpPoluvagonPlanUty) as samKrpPoluvagonPlanUty," +
//            "sum(samKrpSisternaPlanUty) as samKrpSisternaPlanUty,sum(samKrpBoshqaPlanUty) as samKrpBoshqaPlanUty,sum(havKrpKritiPlanUty) as havKrpKritiPlanUty," +
//            "sum(havKrpPlatformaPlanUty) as havKrpPlatformaPlanUty,sum(havKrpPoluvagonPlanUty) as havKrpPoluvagonPlanUty,sum(havKrpSisternaPlanUty) as havKrpSisternaPlanUty," +
//            "sum(havKrpBoshqaPlanUty) as havKrpBoshqaPlanUty,sum(andjKrpKritiPlanUty) as andjKrpKritiPlanUty,sum(andjKrpPlatformaPlanUty) as andjKrpPlatformaPlanUty," +
//            "sum(andjKrpPoluvagonPlanUty) as andjKrpPoluvagonPlanUty,sum(andjKrpSisternaPlanUty) as andjKrpSisternaPlanUty,sum(andjKrpBoshqaPlanUty) as andjKrpBoshqa " +
//        "FROM " +
//            "PlanUty u " +
//        "WHERE u.year LIKE %:year%")
    List<PlanUty> findByYear(String year);

}
