package com.sms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sms.model.VagonModel;



public interface VagonRepository extends JpaRepository<VagonModel,Long>{

//Update
	Optional<VagonModel> findByNomer(Integer keyword);

//Filter
	@Query("SELECT  count(*) FROM VagonModel u WHERE u.depoNomi = ?1 ")
	Integer getCount(String depoNomi);

	@Query("SELECT  count(*) FROM VagonModel u WHERE u.vagonTuri = ?1 and u.depoNomi = ?2 ")
	Integer getVagonsCount(String vagonTuri, String depoNomi);

	@Query("SELECT  count(*) FROM VagonModel u WHERE u.depoNomi = ?1 and u.country = ?2 ")
	Integer getCount(String depoNomi, String country);

	@Query("SELECT  count(*) FROM VagonModel u WHERE u.vagonTuri = ?1 and u.depoNomi = ?2  and u.country = ?3")
	Integer getVagonsCount(String vagonTuri, String depoNomi, String country);

	
	@Query("SELECT u FROM VagonModel u WHERE u.depoNomi = ?1 and u.vagonTuri = ?2 and u.country = ?3 ")
	List<VagonModel> findAllByDepoNomiVagonTuriAndCountry(String depoNomi, String vagonTuri, String country, Sort createdDate);

	@Query("SELECT u FROM VagonModel u WHERE u.depoNomi = ?1 and u.vagonTuri = ?2 ")
	List<VagonModel> findAllByDepoNomiAndVagonTuri(String depoNomi, String vagonTuri, Sort createdDate);

	@Query("SELECT u FROM VagonModel u WHERE u.depoNomi = ?1 and u.country = ?2 ")
	List<VagonModel> findAllByDepoNomiAndCountry(String depoNomi, String country, Sort createdDate);

	@Query("SELECT u FROM VagonModel u WHERE u.depoNomi = ?1 ")
	List<VagonModel> findAllByDepoNomi(String depoNomi, Sort createdDate);

	@Query("SELECT u FROM VagonModel u WHERE u.vagonTuri = ?1 and u.country = ?2 ")
	List<VagonModel> findAllByVagonTuriAndCountry(String vagonTuri, String country, Sort createdDate);

	@Query("SELECT u FROM VagonModel u WHERE u.country = ?1 ")
	List<VagonModel> findAllBycountry(String country, Sort createdDate);

	@Query("SELECT u FROM VagonModel u WHERE u.vagonTuri = ?1 ")
	List<VagonModel> findAllByVagonTuri(String vagonTuri, Sort createdDate);



}
