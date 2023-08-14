package com.sms.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.sms.payload.ApiResponse;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

import com.sms.dto.VagonDto;
import com.sms.model.VagonModel;

public interface VagonService {

//Last Action Timeni oladi
	String getSamDate();
	String getHavDate();
	String getAndjDate();

////Export to Excel
//	//Table
//	void exportTableToExcel(List<Integer> vagonsToDownloadTables, HttpServletResponse response) throws IOException;
//	//List
//	void exportToExcel(List<VagonModel> vagonsToDownload, HttpServletResponse response) throws IOException;


//Listni toldirish uchun
	List<VagonModel> findAll();

//Save wagon
	ApiResponse saveVagon(VagonDto vagon);
	ApiResponse saveVagonSam(VagonDto vagon);
	ApiResponse saveVagonHav(VagonDto vagon);
	ApiResponse saveVagonAndj(VagonDto vagon);

//Get by id
	VagonModel findById(Long id);

//Delete
	ApiResponse deleteVagonById(long id) throws NotFoundException;
	ApiResponse deleteVagonSam(long id) throws NotFoundException;
	ApiResponse deleteVagonHav(long id) throws NotFoundException;
	ApiResponse deleteVagonAndj(long id) throws NotFoundException;

//Search
	ApiResponse findByKeyword(Integer participant);

//Filter
	Integer getCount(String string);
	Integer getVagonsCount(String kriti, String depoNomi);
	Integer getCount(String string,String country);
	Integer getVagonsCount(String kriti, String depoNomi,String country);

	List<VagonModel> findAllByDepoNomiVagonTuriAndCountry(String depoNomi, String vagonTuri, String country);

	List<VagonModel> findAllByDepoNomiAndVagonTuri(String depoNomi, String vagonTuri);

	List<VagonModel> findAllByDepoNomiAndCountry(String depoNomi, String country);

	List<VagonModel> findAllByDepoNomi(String depoNomi);

	List<VagonModel> findAllByVagonTuriAndCountry(String vagonTuri, String country);

	List<VagonModel> findAllBycountry(String country);

	List<VagonModel> findAllByVagonTuri(String vagonTuri);









//Update
	VagonModel getVagonById(long id);

	ApiResponse updateVagon(VagonDto vagon, long id);
	ApiResponse updateVagonSam(VagonDto vagon, long id);
	ApiResponse updateVagonHav(VagonDto vagon, long id);
	ApiResponse updateVagonAndj(VagonDto vagon, long id);


	void createPdf(List<VagonModel> vagonsToDownload, HttpServletResponse response) throws IOException;

	void pdfTableFile(List<Integer> vagonsToDownloadTables, HttpServletResponse response) throws IOException;
}
