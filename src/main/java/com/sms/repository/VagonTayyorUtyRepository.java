package com.sms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sms.model.VagonTayyorUty;

@Repository
public interface VagonTayyorUtyRepository extends JpaRepository<VagonTayyorUty, Long> {

//Bir oylik Listni toldirish uchun
	@Query("SELECT u FROM VagonTayyorUty u WHERE u.createdDate LIKE %:createdMonth% order by u.createdDate desc")
	List<VagonTayyorUty> findAllByCreatedDate(String createdMonth, Sort createdDate);

//Bir oylik Fact
	@Query("SELECT count(*) FROM VagonTayyorUty u WHERE u.depoNomi LIKE %:depoNomi% and u.vagonTuri LIKE %:vagonTuri% and u.remontTuri LIKE %:tamirTuri% and u.createdDate LIKE %:oy%")
	int countAllActiveByDepoNomiVagonTuriAndTamirTuri(@Param("depoNomi") String depoNomi,@Param("vagonTuri") String vagonTuri,@Param("tamirTuri") String tamirTuri,@Param("oy") String oy);

//Jami oylik Fact
	@Query("SELECT count(*) FROM VagonTayyorUty u WHERE u.depoNomi LIKE %:depoNomi% and u.vagonTuri LIKE %:vagonTuri% and u.remontTuri LIKE %:tamirTuri% and u.createdDate LIKE %:year%")
	int countByDepoNomiVagonTuriAndTamirTuri(@Param("depoNomi") String depoNomi,@Param("vagonTuri") String vagonTuri,@Param("tamirTuri") String tamirTuri,@Param("year") String year);

//Search nomer orqali qidirish
	VagonTayyorUty findByNomerAndCreatedDateContaining(Integer nomer, String oy);

//filterniki 3 ta
	@Query("SELECT u FROM VagonTayyorUty u WHERE u.depoNomi LIKE %:depoNomi% and u.vagonTuri LIKE %:vagonTuri% and u.createdDate LIKE %:oy% ")
	List<VagonTayyorUty> findAllByDepoNomiAndVagonTuri(@Param("depoNomi") String depoNomi, @Param("vagonTuri")  String vagonTuri, @Param("oy")  String oy, Sort createdDate);

	@Query("SELECT u FROM VagonTayyorUty u WHERE u.depoNomi LIKE %:depoNomi% and u.createdDate LIKE %:oy%")
	List<VagonTayyorUty> findAllByDepoNomi(@Param("depoNomi") String depoNomi, @Param("oy")  String oy, Sort createdDate);

	@Query("SELECT u FROM VagonTayyorUty u WHERE u.vagonTuri LIKE %:vagonTuri% and u.createdDate LIKE %:oy%")
	List<VagonTayyorUty> findAllByVagonTuri(@Param("vagonTuri") String vagonTuri, @Param("oy") String oy, Sort createdDate);






	@Query("SELECT u FROM VagonTayyorUty u WHERE u.createdDate LIKE %:oy% ")
	List<VagonTayyorUty> findAll(@Param("oy") String oy);

	@Query("SELECT u FROM VagonTayyorUty u WHERE u.nomer = ?1")
	Optional<VagonTayyorUty> findByNomer(Integer nomer);





	//filterniki
	@Query("SELECT u FROM VagonTayyorUty u WHERE u.depoNomi = ?1 and u.vagonTuri = ?2")
	List<VagonTayyorUty> findByDepoNomiAndVagonTuri(String depoNomi, String vagonTuri);

	@Query("SELECT u FROM VagonTayyorUty u WHERE u.depoNomi = ?1")
	List<VagonTayyorUty> findByDepoNomi(String depoNomi);

	@Query("SELECT u FROM VagonTayyorUty u WHERE u.vagonTuri = ?1")
	List<VagonTayyorUty> findByVagonTuri(String vagonTuri);


}
