package com.sms.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.sms.dto.VagonMalumotChiqishDto;
import com.sms.dto.VagonMalumotDto;
import com.sms.dto.VagonMalumotUpdateDto;
import com.sms.payload.ApiResponse;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

import com.sms.model.VagonMalumot;

public interface VagonMalumotService {

//Listni toldirish uchun
	List<VagonMalumot> findAll();

//Save Wagon(Kirish)
	void saveVagon(VagonMalumotDto vagon);
	void saveVagonSam(VagonMalumotDto vagon);
	void saveVagonHav(VagonMalumotDto vagon);
	void saveVagonAndj(VagonMalumotDto vagon);

//Chiqish malumotini kiritish uchun vagonni olish
	VagonMalumot getVagonById(long id);

//Save Wagon(Chiqish)
	ApiResponse saveVagonChiqish(VagonMalumotChiqishDto vagon, long id);

	ApiResponse saveVagonSamChiqish(VagonMalumotChiqishDto vagon, long id);

	ApiResponse saveVagonHavChiqish(VagonMalumotChiqishDto vagon, long id);

	ApiResponse saveVagonAndjChiqish(VagonMalumotChiqishDto vagon, long id);


//Delete Wagon
	ApiResponse deleteVagonById(long id) throws NotFoundException;
	ApiResponse deleteVagonSam(long id) throws NotFoundException;
	ApiResponse deleteVagonHav(long id) throws NotFoundException;
	ApiResponse deleteVagonAndj(long id) throws NotFoundException;

//Search
	ApiResponse searchByNomer(Integer nomer);

//Filter
	List<VagonMalumot> filterByDate(String saqlanganVaqt);
	List<VagonMalumot> filterByCountry(String country);
	List<VagonMalumot> filterByDepoNomi(String depoNomi);
	List<VagonMalumot> filterByCountryAndDate(String country, String saqlanganVaqt);
	List<VagonMalumot> filterByDepoNomiAndCountry(String depoNomi, String country);
	List<VagonMalumot> filterByDepoNomiAndDate(String depoNomi, String saqlanganVaqt);
	List<VagonMalumot> filterByDepoNomiCountryAndDate(String depoNomi, String country, String saqlanganVaqt);







	ApiResponse updateVagon(VagonMalumotUpdateDto vagon, long id);
	ApiResponse updateVagonSam(VagonMalumotUpdateDto vagon, long id);
	ApiResponse updateVagonHav(VagonMalumotUpdateDto vagon, long id);
	ApiResponse updateVagonAndj(VagonMalumotUpdateDto vagon, long id);

	


	
	String getSamDate();
	String getHavDate();
	String getAndjDate();
	
//	void createPdf(List<VagonMalumot> vagonsToDownload, HttpServletResponse response) throws IOException;



	ApiResponse updateVagonChiqish(VagonMalumotChiqishDto vagon, Long id);

	ApiResponse updateVagonSamChiqish(VagonMalumotChiqishDto vagon, Long id);

	ApiResponse updateVagonHavChiqish(VagonMalumotChiqishDto vagon, Long id);

	ApiResponse updateVagonAndjChiqish(VagonMalumotChiqishDto vagon, Long id);

	VagonMalumot findById(Long id);
}
