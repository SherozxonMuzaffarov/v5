package com.sms.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.sms.dto.PlanUtyDto;
import com.sms.model.PlanUty;
import com.sms.payload.ApiResponse;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

import com.sms.model.VagonTayyorUty;


public interface VagonTayyorUtyService {
//Last Action Time larni oladi
	String getSamDate();
	String getHavDate();
	String getAndjDate();

//Bir oylik Listni toldirish uchun
	List<VagonTayyorUty> findAllByCreatedDate(String createdMonth);

//Bir oylik Plan
	PlanUty getPlanuty(String month, String year);

// jami oy uchun plan
	PlanUty getPlanutyForYear(String year);

//Bir oylik Fact
	int countAllActiveByDepoNomiVagonTuriAndTamirTuri(String depoNomi, String vagnTuri, String tamirTuri, String oy);

//JAmi oylik Fact
	int countByDepoNomiVagonTuriAndTamirTuri(String depoNomi, String vagonTuri, String tamirTuri, String year);

//Search
	VagonTayyorUty searchByNomer(Integer nomer, String oy);

//filter
	List<VagonTayyorUty> findAllByDepoNomiAndVagonTuri(String depoNomi, String vagonTuri, String oy);
	List<VagonTayyorUty> findAllByDepoNomi(String depoNomi, String oy);
	List<VagonTayyorUty> findAllByVagonTuri(String vagonTuri, String oy);

//Save vagon
	ApiResponse saveVagon(VagonTayyorUty vagon);
	ApiResponse saveVagonSam(VagonTayyorUty vagon);
	ApiResponse saveVagonHav(VagonTayyorUty vagon);
	ApiResponse saveVagonAndj(VagonTayyorUty vagon);

//Delete
	ApiResponse deleteVagonById(long id) throws NotFoundException;
	ApiResponse deleteVagonSam(long id) throws NotFoundException;
	ApiResponse deleteVagonHav(long id) throws NotFoundException;
	ApiResponse deleteVagonAndj(long id) throws NotFoundException;

//Save Plan
	ApiResponse savePlan(PlanUtyDto planDto);







	

	VagonTayyorUty getVagonById(long id);
	
	VagonTayyorUty updateVagon(VagonTayyorUty vagon, long id);
	VagonTayyorUty updateVagonSam(VagonTayyorUty vagon, long id);
	VagonTayyorUty updateVagonHav(VagonTayyorUty vagon, long id);
	VagonTayyorUty updateVagonAndj(VagonTayyorUty vagon, long id);
	
	VagonTayyorUty updateVagonMonths(VagonTayyorUty vagon, long id);
	VagonTayyorUty updateVagonSamMonths(VagonTayyorUty vagon, long id);
	VagonTayyorUty updateVagonHavMonths(VagonTayyorUty vagon, long id);
	VagonTayyorUty updateVagonAndjMonths(VagonTayyorUty vagon, long id);

	
	List<VagonTayyorUty> findAll();

	List<VagonTayyorUty> findAll(String oy);
	





	VagonTayyorUty findByNomer(Integer nomer);
	


	
	// hamma oylar uchun filterniki
	
	List<VagonTayyorUty> findByDepoNomiAndVagonTuri(String depoNomi, String vagonTuri);

	List<VagonTayyorUty> findByDepoNomi(String depoNomi);

	List<VagonTayyorUty> findByVagonTuri(String vagonTuri);

	VagonTayyorUty findById(Long id);
    void nextTayyor(VagonTayyorUty vagon, Long id);

//	void exportTableToExcel(List<Integer> drTable, List<Integer> krTable, List<Integer> krpTable, HttpServletResponse response) throws IOException;


	void createPdf(List<VagonTayyorUty> vagonListMonths, HttpServletResponse response) throws IOException;

	void pdfFileTable(List<Integer> vagonsToDownloadAllTable, HttpServletResponse response) throws IOException;
}
