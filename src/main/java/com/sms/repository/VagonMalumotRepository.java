package com.sms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sms.model.VagonMalumot;


public interface VagonMalumotRepository  extends JpaRepository<VagonMalumot,Long>{

//Search
	Optional<VagonMalumot> findByNomer(Integer keyword);

//Filter(7 ta)
	@Query("SELECT u FROM VagonMalumot u WHERE u.saqlanganVaqti LIKE %:saqlanganVaqt%")
	List<VagonMalumot> filterByDate(String saqlanganVaqt, Sort saqlanganVaqti);

	@Query("SELECT u FROM VagonMalumot u WHERE u.country LIKE %:country%")
	List<VagonMalumot> filterByCountry(String country, Sort saqlanganVaqti);

	@Query("SELECT u FROM VagonMalumot u WHERE u.depoNomi LIKE %:depoNomi%")
	List<VagonMalumot> filterByDepoNomi(String depoNomi, Sort saqlanganVaqti);

	@Query("SELECT u FROM VagonMalumot u WHERE u.country LIKE %:country% and u.saqlanganVaqti LIKE %:saqlanganVaqt%")
	List<VagonMalumot> filterByCountryAndDate(String country, String saqlanganVaqt, Sort saqlanganVaqti);

	@Query("SELECT u FROM VagonMalumot u WHERE u.depoNomi LIKE %:depoNomi% and u.country LIKE %:country%")
	List<VagonMalumot> filterByDepoNomiAndCountry(String depoNomi, String country, Sort saqlanganVaqti);

	@Query("SELECT u FROM VagonMalumot u WHERE u.depoNomi LIKE %:depoNomi% and u.saqlanganVaqti LIKE %:saqlanganVaqt%")
	List<VagonMalumot> filterByDepoNomiAndDate(String depoNomi, String saqlanganVaqt, Sort saqlanganVaqti);

	@Query("SELECT u FROM VagonMalumot u WHERE u.depoNomi LIKE %:depoNomi% and u.country LIKE %:country% and u.saqlanganVaqti LIKE %:saqlanganVaqt%")
	List<VagonMalumot> filterByDepoNomiCountryAndDate(String depoNomi, String country, String saqlanganVaqt, Sort saqlanganVaqti);








	@Query("SELECT u FROM VagonMalumot u WHERE u.country LIKE %:country1% or u.country LIKE %:country2% ")
	List<VagonMalumot> findAllByEgasi(String country1, String country2);
}
