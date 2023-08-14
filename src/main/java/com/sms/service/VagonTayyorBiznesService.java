package com.sms.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.sms.dto.PlanBiznesDto;
import com.sms.model.PlanBiznes;
import com.sms.payload.ApiResponse;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

import com.sms.model.VagonTayyor;

public interface VagonTayyorBiznesService {

// Last Action Time
	String getSamDate();
	String getHavDate();
	String getAndjDate();

//Save Plan
	ApiResponse savePlan(PlanBiznesDto planDto);

//Listni toldirish uchun
	List<VagonTayyor> findAllByCreatedDate(String createdMonth);

// bir oylik plan
	PlanBiznes getPlanBiznes(String oy, String year);
// Jami oylik plan fact
	PlanBiznes getPlanBiznes(String year);

// bir oylik fact
	int countAllActiveByDepoNomiVagonTuriAndTamirTuri(String depoNomi, String vagnTuri, String tamirTuri, String oy);
// Jami oylik fact
	int countByDepoNomiVagonTuriAndTamirTuri(String depoNomi, String vagonTuri, String tamirTuri, String year);

//Save new Wagon (3ta)
	ApiResponse saveVagon(VagonTayyor vagon);
	ApiResponse saveVagonSam(VagonTayyor vagon);
	ApiResponse saveVagonHav(VagonTayyor vagon);
	ApiResponse saveVagonAndj(VagonTayyor vagon);

//Delete Wagon
	ApiResponse deleteVagonById(long id) throws NotFoundException;
	ApiResponse deleteVagonSam(long id) throws NotFoundException;
	ApiResponse deleteVagonHav(long id) throws NotFoundException;
	ApiResponse deleteVagonAndj(long id) throws NotFoundException;

//Search By number
	VagonTayyor searchByNomer(Integer nomer, String oy);

//Filter
	//Listni toldirish uchun
	List<VagonTayyor> findAllByDepoNomiVagonTuriAndCountry(String depoNomi, String vagonTuri, String country, String oy);
	List<VagonTayyor> findAllByVagonTuriAndCountry(String vagonTuri, String country, String oy);
	List<VagonTayyor> findAllBycountry(String country, String oy);
	List<VagonTayyor> findAllByDepoNomiAndVagonTuri(String depoNomi, String vagonTuri, String oy);
	List<VagonTayyor> findAllByDepoNomiAndCountry(String depoNomi, String country, String oy);
	List<VagonTayyor> findAllByVagonTuri(String vagonTuri, String oy);
	List<VagonTayyor> findAllByDepoNomi(String depoNomi, String oy);

	//Tableni toldirish uchun(1 oylik)
	int countAllActiveByDepoNomiVagonTuriAndTamirTuri(String depoNomi, String vagnTuri, String tamirTuri,String oy, String country);

	int countByDepoNomiVagonTuriTamirTuriAndCountry(String depoNomi, String vagonTuri, String tamirTuri, String country, String year);

//Generate excel file
//	void exportTableToExcel(List<Integer> drTable, List<Integer> krTable, List<Integer> krpTable,  List<Integer> yolovchiTable, HttpServletResponse response) throws IOException;
//Generate PDf
	void pdfFileTable(List<Integer> vagonsToDownloadAllTable, HttpServletResponse response) throws IOException;




	// Filter vaqtida Factni

	VagonTayyor getVagonById(long id);

	VagonTayyor updateVagon(VagonTayyor vagon, long id);
	VagonTayyor updateVagonSam(VagonTayyor vagon, long id);
	VagonTayyor updateVagonHav(VagonTayyor vagon, long id);
	VagonTayyor updateVagonAndj(VagonTayyor vagon, long id);

	VagonTayyor updateVagonMonths(VagonTayyor vagon, long id);
	VagonTayyor updateVagonSamMonths(VagonTayyor vagon, long id);
	VagonTayyor updateVagonHavMonths(VagonTayyor vagon, long id);
	VagonTayyor updateVagonAndjMonths(VagonTayyor vagon, long id);



	List<VagonTayyor> findAll();

	List<VagonTayyor> findAll(String oy);


	VagonTayyor findByNomer(Integer nomer);

	List<VagonTayyor> findByDepoNomiVagonTuriAndCountry(String depoNomi, String vagonTuri, String country);

	List<VagonTayyor> findByDepoNomiAndVagonTuri(String depoNomi, String vagonTuri);

	List<VagonTayyor> findByDepoNomiAndCountry(String depoNomi, String country);

	List<VagonTayyor> findByDepoNomi(String depoNomi);

	List<VagonTayyor> findByVagonTuriAndCountry(String vagonTuri, String country);

	List<VagonTayyor> findBycountry(String country);

	List<VagonTayyor> findByVagonTuri(String vagonTuri);


	VagonTayyor findById(Long id);





}
