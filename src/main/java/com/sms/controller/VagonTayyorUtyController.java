package com.sms.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sms.dto.PlanUtyDto;
import com.sms.model.PlanUty;
import com.sms.payload.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sms.model.VagonTayyorUty;
import com.sms.service.VagonTayyorUtyService;

@Controller
public class VagonTayyorUtyController {
	
	@Autowired
	private VagonTayyorUtyService vagonTayyorUtyService;

//Excel Uchun
	List<VagonTayyorUty> vagonList  = new ArrayList<>();
	List<VagonTayyorUty> vagonListMonths  = new ArrayList<>();
	List<Integer> vagonsToDownloadAllTable = new ArrayList<>();
	List<Integer> vagonsToDownloadAllTableMonths = new ArrayList<>();
//
//	List<Integer> drTable  = new ArrayList<>();
//	List<Integer> krTable  = new ArrayList<>();
//	List<Integer> krpTable  = new ArrayList<>();
//	List<Integer> drTableM  = new ArrayList<>();
//	List<Integer> krTableM  = new ArrayList<>();
//	List<Integer> krpTableM  = new ArrayList<>();

//List
	@PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@GetMapping("/vagons/createExcelUty")
	public void pdfFile(Model model, HttpServletResponse response) throws IOException {
		vagonTayyorUtyService.createPdf(vagonList, response);
		model.addAttribute("vagons",vagonList);
	}
	@PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@GetMapping("/vagons/createExcelUtyAllMonth")
	public void createPdf(Model model,HttpServletResponse response) throws IOException {
		vagonTayyorUtyService.createPdf(vagonListMonths, response);
		model.addAttribute("vagons",vagonListMonths);
	}
//Table
	@PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@GetMapping("/vagons/createExcelTableUty")
	public void pdfFileTable(Model model, HttpServletResponse response) throws IOException {
		vagonTayyorUtyService.pdfFileTable(vagonsToDownloadAllTable, response);
		model.addAttribute("vagons",vagonsToDownloadAllTable);
	}
	@PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@GetMapping("/vagons/createExcelTableUtyMonth")
	public void pdfFileTableMonth(Model model, HttpServletResponse response) throws IOException {
		vagonTayyorUtyService.pdfFileTable(vagonsToDownloadAllTableMonths, response);
		model.addAttribute("vagons",vagonsToDownloadAllTableMonths);
	}


	LocalDate today = LocalDate.now();
	int month = today.getMonthValue();
	int year = today.getYear();

//	Plan factni yozadi table uchun
	public void getPlanFactForOneMonth(Model model, int year, int month){

	//Oy va Yillik yugmalardagi text uchun
		String oyNomi = null;
		switch (month) {
			case 1:
				oyNomi = "Yanvar";
				break;
			case 2:
				oyNomi = "Fevral";
				break;
			case 3:
				oyNomi = "Mart";
				break;
			case 4:
				oyNomi = "Aprel";
				break;
			case 5:
				oyNomi = "May";
				break;
			case 6:
				oyNomi = "Iyun";
				break;
			case 7:
				oyNomi = "Iyul";
				break;
			case 8:
				oyNomi = "Avgust";
				break;
			case 9:
				oyNomi = "Sentabr";
				break;
			case 10:
				oyNomi = "Oktabr";
				break;
			case 11:
				oyNomi = "Noyabr";
				break;
			case 12:
				oyNomi = "Dekabr";
				break;
		}

		model.addAttribute("month", oyNomi);
		model.addAttribute("year", month + " oylik");

		String oy=null;
		switch (month) {
			case 1:
				oy = "01";
				break;
			case 2:
				oy = "02";
				break;
			case 3:
				oy = "03";
				break;
			case 4:
				oy = "04";
				break;
			case 5:
				oy = "05";
				break;
			case 6:
				oy = "06";
				break;
			case 7:
				oy = "07";
				break;
			case 8:
				oy = "08";
				break;
			case 9:
				oy = "09";
				break;
			case 10:
				oy = "10";
				break;
			case 11:
				oy = "11";
				break;
			case 12:
				oy = "12";
				break;
		}

		//vaqtni olib turadi
		model.addAttribute("samDate",vagonTayyorUtyService.getSamDate());
		model.addAttribute("havDate", vagonTayyorUtyService.getHavDate());
		model.addAttribute("andjDate",vagonTayyorUtyService.getAndjDate());

		PlanUty planDto = vagonTayyorUtyService.getPlanuty(oy, String.valueOf(year));
		//planlar kiritish

		//havos hamma plan
		int HavDtHammaPlan = planDto.getHavDtKritiPlanUty() + planDto.getHavDtPlatformaPlanUty() + planDto.getHavDtPoluvagonPlanUty() + planDto.getHavDtSisternaPlanUty() + planDto.getHavDtBoshqaPlanUty();
		int HavDtKritiPlan = planDto.getHavDtKritiPlanUty();
		int HavDtPlatformaPlan = planDto.getHavDtPlatformaPlanUty();
		int HavDtPoluvagonPlan = planDto.getHavDtPoluvagonPlanUty();
		int HavDtSisternaPlan = planDto.getHavDtSisternaPlanUty();
		int HavDtBoshqaPlan = planDto.getHavDtBoshqaPlanUty();

		model.addAttribute("HavDtHammaPlan", HavDtHammaPlan);
		model.addAttribute("HavDtKritiPlan", HavDtKritiPlan);
		model.addAttribute("HavDtPlatformaPlan", HavDtPlatformaPlan);
		model.addAttribute("HavDtPoluvagonPlan", HavDtPoluvagonPlan);
		model.addAttribute("HavDtSisternaPlan", HavDtSisternaPlan);
		model.addAttribute("HavDtBoshqaPlan", HavDtBoshqaPlan);

		//andijon hamma plan depo tamir
		int AndjDtHammaPlan = planDto.getAndjDtKritiPlanUty() + planDto.getAndjDtPlatformaPlanUty() + planDto.getAndjDtPoluvagonPlanUty() + planDto.getAndjDtSisternaPlanUty() + planDto.getAndjDtBoshqaPlanUty();
		int AndjDtKritiPlan =  planDto.getAndjDtKritiPlanUty();
		int AndjDtPlatformaPlan =  planDto.getAndjDtPlatformaPlanUty();
		int AndjDtPoluvagonPlan =  planDto.getAndjDtPoluvagonPlanUty();
		int AndjDtSisternaPlan =  planDto.getAndjDtSisternaPlanUty();
		int AndjDtBoshqaPlan =  planDto.getAndjDtBoshqaPlanUty();

		model.addAttribute("AndjDtHammaPlan", AndjDtHammaPlan);
		model.addAttribute("AndjDtKritiPlan", AndjDtKritiPlan);
		model.addAttribute("AndjDtPlatformaPlan", AndjDtPlatformaPlan);
		model.addAttribute("AndjDtPoluvagonPlan", AndjDtPoluvagonPlan);
		model.addAttribute("AndjDtSisternaPlan", AndjDtSisternaPlan);
		model.addAttribute("AndjDtBoshqaPlan", AndjDtBoshqaPlan);

		//samarqand depo tamir
		int SamDtHammaPlan=planDto.getSamDtKritiPlanUty() + planDto.getSamDtPlatformaPlanUty() + planDto.getSamDtPoluvagonPlanUty() + planDto.getSamDtSisternaPlanUty() + planDto.getSamDtBoshqaPlanUty();
		int SamDtKritiPlan =  planDto.getSamDtKritiPlanUty();
		int SamDtPlatformaPlan =  planDto.getSamDtPlatformaPlanUty();
		int SamDtPoluvagonPlan =  planDto.getSamDtPoluvagonPlanUty();
		int SamDtSisternaPlan =  planDto.getSamDtSisternaPlanUty();
		int SamDtBoshqaPlan =  planDto.getSamDtBoshqaPlanUty();

		model.addAttribute("SamDtHammaPlan",SamDtHammaPlan);
		model.addAttribute("SamDtKritiPlan", SamDtKritiPlan);
		model.addAttribute("SamDtPlatformaPlan", SamDtPlatformaPlan);
		model.addAttribute("SamDtPoluvagonPlan", SamDtPoluvagonPlan);
		model.addAttribute("SamDtSisternaPlan", SamDtSisternaPlan);
		model.addAttribute("SamDtBoshqaPlan", SamDtBoshqaPlan);

		// Itogo planlar depo tamir
		int DtHammaPlan = AndjDtHammaPlan + HavDtHammaPlan + SamDtHammaPlan;
		int DtKritiPlan = planDto.getAndjDtKritiPlanUty() + planDto.getHavDtKritiPlanUty() + planDto.getSamDtKritiPlanUty();
		int DtPlatformaPlan = planDto.getAndjDtPlatformaPlanUty() + planDto.getHavDtPlatformaPlanUty() + planDto.getSamDtPlatformaPlanUty();
		int DtPoluvagonPlan = planDto.getAndjDtPoluvagonPlanUty() + planDto.getHavDtPoluvagonPlanUty() + planDto.getSamDtPoluvagonPlanUty();
		int DtSisternaPlan = planDto.getAndjDtSisternaPlanUty() + planDto.getHavDtSisternaPlanUty() + planDto.getSamDtSisternaPlanUty();
		int DtBoshqaPlan = planDto.getAndjDtBoshqaPlanUty() + planDto.getHavDtBoshqaPlanUty() + planDto.getSamDtBoshqaPlanUty();

		model.addAttribute("UDtHammaPlan", DtHammaPlan);
		model.addAttribute("UDtKritiPlan", DtKritiPlan);
		model.addAttribute("UDtPlatformaPlan", DtPlatformaPlan);
		model.addAttribute("UDtPoluvagonPlan", DtPoluvagonPlan);
		model.addAttribute("UDtSisternaPlan", DtSisternaPlan);
		model.addAttribute("UDtBoshqaPlan", DtBoshqaPlan);

		//havos kapital tamir uchun plan
		int HavKtHammaPlan = planDto.getHavKtKritiPlanUty() + planDto.getHavKtPlatformaPlanUty() + planDto.getHavKtPoluvagonPlanUty() + planDto.getHavKtSisternaPlanUty() + planDto.getHavKtBoshqaPlanUty();
		int HavKtKritiPlan = planDto.getHavKtKritiPlanUty();
		int HavKtPlatformaPlan = planDto.getHavKtPlatformaPlanUty();
		int HavKtPoluvagonPlan = planDto.getHavKtPoluvagonPlanUty();
		int HavKtSisternaPlan = planDto.getHavKtSisternaPlanUty();
		int HavKtBoshqaPlan = planDto.getHavKtBoshqaPlanUty();

		model.addAttribute("HavKtHammaPlan", HavKtHammaPlan);
		model.addAttribute("HavKtKritiPlan", HavKtKritiPlan);
		model.addAttribute("HavKtPlatformaPlan", HavKtPlatformaPlan);
		model.addAttribute("HavKtPoluvagonPlan", HavKtPoluvagonPlan);
		model.addAttribute("HavKtSisternaPlan", HavKtSisternaPlan);
		model.addAttribute("HavKtBoshqaPlan", HavKtBoshqaPlan);

		//VCHD-5 kapital tamir uchun plan
		int AndjKtHammaPlan = planDto.getAndjKtKritiPlanUty() + planDto.getAndjKtPlatformaPlanUty() + planDto.getAndjKtPoluvagonPlanUty() + planDto.getAndjKtSisternaPlanUty() + planDto.getAndjKtBoshqaPlanUty();
		int AndjKtKritiPlan =  planDto.getAndjKtKritiPlanUty();
		int AndjKtPlatformaPlan =  planDto.getAndjKtPlatformaPlanUty();
		int AndjKtPoluvagonPlan =  planDto.getAndjKtPoluvagonPlanUty();
		int AndjKtSisternaPlan =  planDto.getAndjKtSisternaPlanUty();
		int AndjKtBoshqaPlan =  planDto.getAndjKtBoshqaPlanUty();

		model.addAttribute("AndjKtHammaPlan", AndjKtHammaPlan);
		model.addAttribute("AndjKtKritiPlan", AndjKtKritiPlan);
		model.addAttribute("AndjKtPlatformaPlan", AndjKtPlatformaPlan);
		model.addAttribute("AndjKtPoluvagonPlan", AndjKtPoluvagonPlan);
		model.addAttribute("AndjKtSisternaPlan", AndjKtSisternaPlan);
		model.addAttribute("AndjKtBoshqaPlan", AndjKtBoshqaPlan);


		//VCHD-6 kapital tamir uchun plan
		int SamKtHammaPlan =  planDto.getSamKtKritiPlanUty() + planDto.getSamKtPlatformaPlanUty() + planDto.getSamKtPoluvagonPlanUty() + planDto.getSamKtSisternaPlanUty() + planDto.getSamKtBoshqaPlanUty();
		int SamKtKritiPlan =  planDto.getSamKtKritiPlanUty();
		int SamKtPlatformaPlan =  planDto.getSamKtPlatformaPlanUty();
		int SamKtPoluvagonPlan =  planDto.getSamKtPoluvagonPlanUty();
		int SamKtSisternaPlan =  planDto.getSamKtSisternaPlanUty();
		int SamKtBoshqaPlan =  planDto.getSamKtBoshqaPlanUty();

		model.addAttribute("SamKtHammaPlan", SamKtHammaPlan);
		model.addAttribute("SamKtKritiPlan", SamKtKritiPlan);
		model.addAttribute("SamKtPlatformaPlan", SamKtPlatformaPlan);
		model.addAttribute("SamKtPoluvagonPlan", SamKtPoluvagonPlan);
		model.addAttribute("SamKtSisternaPlan", SamKtSisternaPlan);
		model.addAttribute("SamKtBoshqaPlan", SamKtBoshqaPlan);

		//kapital itogo
		int KtHammaPlan = AndjKtHammaPlan + HavKtHammaPlan + SamKtHammaPlan;
		int KtKritiPlan = planDto.getAndjKtKritiPlanUty() + planDto.getHavKtKritiPlanUty() + planDto.getSamKtKritiPlanUty();
		int KtPlatformaPlan = planDto.getAndjKtPlatformaPlanUty() + planDto.getHavKtPlatformaPlanUty() + planDto.getSamKtPlatformaPlanUty();
		int KtPoluvagonPlan = planDto.getAndjKtPoluvagonPlanUty() + planDto.getHavKtPoluvagonPlanUty() + planDto.getSamKtPoluvagonPlanUty();
		int KtSisternaPlan = planDto.getAndjKtSisternaPlanUty() + planDto.getHavKtSisternaPlanUty() + planDto.getSamKtSisternaPlanUty();
		int KtBoshqaPlan = planDto.getAndjKtBoshqaPlanUty() + planDto.getHavKtBoshqaPlanUty() + planDto.getSamKtBoshqaPlanUty();

		model.addAttribute("UKtHammaPlan", KtHammaPlan);
		model.addAttribute("UKtKritiPlan", KtKritiPlan);
		model.addAttribute("UKtPlatformaPlan", KtPlatformaPlan);
		model.addAttribute("UKtPoluvagonPlan", KtPoluvagonPlan);
		model.addAttribute("UKtSisternaPlan", KtSisternaPlan);
		model.addAttribute("UKtBoshqaPlan", KtBoshqaPlan);

		//VCHD-3 KRP plan
		int HavKrpHammaPlan =  planDto.getHavKrpKritiPlanUty() + planDto.getHavKrpPlatformaPlanUty() + planDto.getHavKrpPoluvagonPlanUty() + planDto.getHavKrpSisternaPlanUty() + planDto.getHavKrpBoshqaPlanUty();
		int HavKrpKritiPlan = planDto.getHavKrpKritiPlanUty();
		int HavKrpPlatformaPlan = planDto.getHavKrpPlatformaPlanUty();
		int HavKrpPoluvagonPlan = planDto.getHavKrpPoluvagonPlanUty();
		int HavKrpSisternaPlan = planDto.getHavKrpSisternaPlanUty();
		int HavKrpBoshqaPlan = planDto.getHavKrpBoshqaPlanUty();

		model.addAttribute("HavKrpHammaPlan", HavKrpHammaPlan);
		model.addAttribute("HavKrpKritiPlan", HavKrpKritiPlan);
		model.addAttribute("HavKrpPlatformaPlan", HavKrpPlatformaPlan);
		model.addAttribute("HavKrpPoluvagonPlan", HavKrpPoluvagonPlan);
		model.addAttribute("HavKrpSisternaPlan", HavKrpSisternaPlan);
		model.addAttribute("HavKrpBoshqaPlan", HavKrpBoshqaPlan);

		//VCHD-5 Krp plan
		int AndjKrpHammaPlan =  planDto.getAndjKrpKritiPlanUty() + planDto.getAndjKrpPlatformaPlanUty() + planDto.getAndjKrpPoluvagonPlanUty() + planDto.getAndjKrpSisternaPlanUty() + planDto.getAndjKrpBoshqaPlanUty();
		int AndjKrpKritiPlan = planDto.getAndjKrpKritiPlanUty();
		int AndjKrpPlatformaPlan = planDto.getAndjKrpPlatformaPlanUty();
		int AndjKrpPoluvagonPlan = planDto.getAndjKrpPoluvagonPlanUty();
		int AndjKrpSisternaPlan = planDto.getAndjKrpSisternaPlanUty();
		int AndjKrpBoshqaPlan = planDto.getAndjKrpBoshqaPlanUty();

		model.addAttribute("AndjKrpHammaPlan", AndjKrpHammaPlan);
		model.addAttribute("AndjKrpKritiPlan", AndjKrpKritiPlan);
		model.addAttribute("AndjKrpPlatformaPlan", AndjKrpPlatformaPlan);
		model.addAttribute("AndjKrpPoluvagonPlan", AndjKrpPoluvagonPlan);
		model.addAttribute("AndjKrpSisternaPlan", AndjKrpSisternaPlan);
		model.addAttribute("AndjKrpBoshqaPlan", AndjKrpBoshqaPlan);

		//samarqand KRP plan
		int SamKrpHammaPlan = planDto.getSamKrpKritiPlanUty() + planDto.getSamKrpPlatformaPlanUty() + planDto.getSamKrpPoluvagonPlanUty() + planDto.getSamKrpSisternaPlanUty() + planDto.getSamKrpBoshqaPlanUty();
		int SamKrpKritiPlan = planDto.getSamKrpKritiPlanUty();
		int SamKrpPlatformaPlan = planDto.getSamKrpPlatformaPlanUty();
		int SamKrpPoluvagonPlan = planDto.getSamKrpPoluvagonPlanUty();
		int SamKrpSisternaPlan = planDto.getSamKrpSisternaPlanUty();
		int SamKrpBoshqaPlan = planDto.getSamKrpBoshqaPlanUty();

		model.addAttribute("SamKrpHammaPlan", SamKrpHammaPlan);
		model.addAttribute("SamKrpKritiPlan", SamKrpKritiPlan);
		model.addAttribute("SamKrpPlatformaPlan", SamKrpPlatformaPlan);
		model.addAttribute("SamKrpPoluvagonPlan", SamKrpPoluvagonPlan);
		model.addAttribute("SamKrpSisternaPlan", SamKrpSisternaPlan);
		model.addAttribute("SamKrpBoshqaPlan", SamKrpBoshqaPlan);

		//Krp itogo plan
		int KrpHammaPlan = AndjKrpHammaPlan + HavKrpHammaPlan + SamKrpHammaPlan;
		int KrpKritiPlan = planDto.getAndjKrpKritiPlanUty() + planDto.getHavKrpKritiPlanUty() + planDto.getSamKrpKritiPlanUty();
		int KrpPlatformaPlan = planDto.getAndjKrpPlatformaPlanUty() + planDto.getHavKrpPlatformaPlanUty() + planDto.getSamKrpPlatformaPlanUty();
		int KrpPoluvagonPlan = planDto.getAndjKrpPoluvagonPlanUty() + planDto.getHavKrpPoluvagonPlanUty() + planDto.getSamKrpPoluvagonPlanUty();
		int KrpSisternaPlan = planDto.getAndjKrpSisternaPlanUty() + planDto.getHavKrpSisternaPlanUty() + planDto.getSamKrpSisternaPlanUty();
		int KrpBoshqaPlan = planDto.getAndjKrpBoshqaPlanUty() + planDto.getHavKrpBoshqaPlanUty() + planDto.getSamKrpBoshqaPlanUty();

		model.addAttribute("UKrpHammaPlan", KrpHammaPlan);
		model.addAttribute("UKrpKritiPlan", KrpKritiPlan);
		model.addAttribute("UKrpPlatformaPlan", KrpPlatformaPlan);
		model.addAttribute("UKrpPoluvagonPlan", KrpPoluvagonPlan);
		model.addAttribute("UKrpSisternaPlan", KrpSisternaPlan);
		model.addAttribute("UKrpBoshqaPlan", KrpBoshqaPlan);


		// factlar
		//VCHD-3 uchun depli tamir
		int hdHamma = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yopiq vagon (крыт)","Depoli ta’mir(ДР)", oy + '-' + year + '-' + year) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Platforma(пф)","Depoli ta’mir(ДР)", oy + '-' + year) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yarim ochiq vagon(пв)","Depoli ta’mir(ДР)", oy + '-' + year) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Sisterna(цс)","Depoli ta’mir(ДР)", oy + '-' + year) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Boshqa turdagi(проч)","Depoli ta’mir(ДР)", oy + '-' + year);

		int hdKriti = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yopiq vagon (крыт)","Depoli ta’mir(ДР)", oy + '-' + year);
		int hdPlatforma = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Platforma(пф)","Depoli ta’mir(ДР)", oy + '-' + year);
		int hdPoluvagon = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yarim ochiq vagon(пв)","Depoli ta’mir(ДР)", oy + '-' + year);
		int hdSisterna = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Sisterna(цс)","Depoli ta’mir(ДР)", oy + '-' + year);
		int hdBoshqa = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Boshqa turdagi(проч)","Depoli ta’mir(ДР)", oy + '-' + year);

		model.addAttribute("hdHamma",hdHamma);
		model.addAttribute("hdKriti", hdKriti);
		model.addAttribute("hdPlatforma", hdPlatforma);
		model.addAttribute("hdPoluvagon", hdPoluvagon);
		model.addAttribute("hdSisterna", hdSisterna);
		model.addAttribute("hdBoshqa", hdBoshqa);


		//VCHD-5 uchun depli tamir
		int adHamma = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yopiq vagon (крыт)","Depoli ta’mir(ДР)", oy + '-' + year) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Platforma(пф)","Depoli ta’mir(ДР)", oy + '-' + year) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yarim ochiq vagon(пв)","Depoli ta’mir(ДР)", oy + '-' + year) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Sisterna(цс)","Depoli ta’mir(ДР)", oy + '-' + year) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Boshqa turdagi(проч)","Depoli ta’mir(ДР)", oy + '-' + year);

		int adKriti = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yopiq vagon (крыт)","Depoli ta’mir(ДР)", oy + '-' + year);
		int adPlatforma = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Platforma(пф)","Depoli ta’mir(ДР)", oy + '-' + year);
		int adPoluvagon = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yarim ochiq vagon(пв)","Depoli ta’mir(ДР)", oy + '-' + year);
		int adSisterna = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Sisterna(цс)","Depoli ta’mir(ДР)", oy + '-' + year);
		int adBoshqa = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Boshqa turdagi(проч)","Depoli ta’mir(ДР)", oy + '-' + year);

		model.addAttribute("adHamma",adHamma);
		model.addAttribute("adKriti", adKriti);
		model.addAttribute("adPlatforma", adPlatforma);
		model.addAttribute("adPoluvagon", adPoluvagon);
		model.addAttribute("adSisterna", adSisterna);
		model.addAttribute("adBoshqa", adBoshqa);

		//samarqand uchun depli tamir
		int sdHamma = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yopiq vagon (крыт)","Depoli ta’mir(ДР)", oy + '-' + year) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Platforma(пф)","Depoli ta’mir(ДР)", oy + '-' + year) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yarim ochiq vagon(пв)","Depoli ta’mir(ДР)", oy + '-' + year) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Sisterna(цс)","Depoli ta’mir(ДР)", oy + '-' + year) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Boshqa turdagi(проч)","Depoli ta’mir(ДР)", oy + '-' + year);

		int sdKriti = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yopiq vagon (крыт)","Depoli ta’mir(ДР)", oy + '-' + year);
		int sdPlatforma = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Platforma(пф)","Depoli ta’mir(ДР)", oy + '-' + year);
		int sdPoluvagon = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yarim ochiq vagon(пв)","Depoli ta’mir(ДР)", oy + '-' + year);
		int sdSisterna = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Sisterna(цс)","Depoli ta’mir(ДР)", oy + '-' + year);
		int sdBoshqa = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Boshqa turdagi(проч)","Depoli ta’mir(ДР)", oy + '-' + year);

		model.addAttribute("sdHamma",sdHamma);
		model.addAttribute("sdKriti", sdKriti);
		model.addAttribute("sdPlatforma", sdPlatforma);
		model.addAttribute("sdPoluvagon", sdPoluvagon);
		model.addAttribute("sdSisterna", sdSisterna);
		model.addAttribute("sdBoshqa", sdBoshqa);


		// itogo Fact uchun depli tamir
		int uvtdhamma = sdHamma + hdHamma + adHamma;
		int uvtdKriti = sdKriti + hdKriti + adKriti;
		int uvtdPlatforma = sdPlatforma + adPlatforma + hdPlatforma;
		int uvtdPoluvagon = sdPoluvagon + hdPoluvagon + adPoluvagon;
		int uvtdSisterna = adSisterna + hdSisterna + sdSisterna;
		int uvtdBoshqa = adBoshqa + hdBoshqa + sdBoshqa;

		model.addAttribute("uvtdhamma",uvtdhamma);
		model.addAttribute("uvtdKriti",uvtdKriti);
		model.addAttribute("uvtdPlatforma",uvtdPlatforma);
		model.addAttribute("uvtdPoluvagon",uvtdPoluvagon);
		model.addAttribute("uvtdSisterna",uvtdSisterna);
		model.addAttribute("uvtdBoshqa",uvtdBoshqa);


		//VCHD-3 uchun kapital tamir
		int hkHamma = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yopiq vagon (крыт)","Kapital ta’mir(КР)", oy + '-' + year) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Platforma(пф)","Kapital ta’mir(КР)", oy + '-' + year) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yarim ochiq vagon(пв)","Kapital ta’mir(КР)", oy + '-' + year) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Sisterna(цс)","Kapital ta’mir(КР)", oy + '-' + year) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Boshqa turdagi(проч)","Kapital ta’mir(КР)", oy + '-' + year);

		int hkKriti = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yopiq vagon (крыт)"," Kapital ta’mir(КР)", oy + '-' + year);
		int hkPlatforma = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Platforma(пф)","Kapital ta’mir(КР)", oy + '-' + year);
		int hkPoluvagon = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yarim ochiq vagon(пв)","Kapital ta’mir(КР)", oy + '-' + year);
		int hkSisterna = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Sisterna(цс)","Kapital ta’mir(КР)", oy + '-' + year);
		int hkBoshqa = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Boshqa turdagi(проч)","Kapital ta’mir(КР)", oy + '-' + year);

		model.addAttribute("hkHamma",hkHamma);
		model.addAttribute("hkKriti", hkKriti);
		model.addAttribute("hkPlatforma", hkPlatforma);
		model.addAttribute("hkPoluvagon", hkPoluvagon);
		model.addAttribute("hkSisterna", hkSisterna);
		model.addAttribute("hkBoshqa", hkBoshqa);


		//VCHD-5 uchun kapital tamir
		int akHamma = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yopiq vagon (крыт)","Kapital ta’mir(КР)", oy + '-' + year) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Platforma(пф)","Kapital ta’mir(КР)", oy + '-' + year) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yarim ochiq vagon(пв)","Kapital ta’mir(КР)", oy + '-' + year) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Sisterna(цс)","Kapital ta’mir(КР)", oy + '-' + year) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Boshqa turdagi(проч)","Kapital ta’mir(КР)", oy + '-' + year);

		int akKriti = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yopiq vagon (крыт)","Kapital ta’mir(КР)", oy + '-' + year);
		int akPlatforma = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Platforma(пф)","Kapital ta’mir(КР)", oy + '-' + year);
		int akPoluvagon = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yarim ochiq vagon(пв)","Kapital ta’mir(КР)", oy + '-' + year);
		int akSisterna = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Sisterna(цс)","Kapital ta’mir(КР)", oy + '-' + year);
		int akBoshqa = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Boshqa turdagi(проч)","Kapital ta’mir(КР)", oy + '-' + year);

		model.addAttribute("akHamma",akHamma);
		model.addAttribute("akKriti", akKriti);
		model.addAttribute("akPlatforma", akPlatforma);
		model.addAttribute("akPoluvagon", akPoluvagon);
		model.addAttribute("akSisterna", akSisterna);
		model.addAttribute("akBoshqa", akBoshqa);

		//samarqand uchun Kapital tamir
		int skHamma = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yopiq vagon (крыт)","Kapital ta’mir(КР)", oy + '-' + year) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Platforma(пф)","Kapital ta’mir(КР)", oy + '-' + year) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yarim ochiq vagon(пв)","Kapital ta’mir(КР)", oy + '-' + year) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Sisterna(цс)","Kapital ta’mir(КР)", oy + '-' + year) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Boshqa turdagi(проч)","Kapital ta’mir(КР)", oy + '-' + year);

		int skKriti = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yopiq vagon (крыт)","Kapital ta’mir(КР)", oy + '-' + year);
		int skPlatforma = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Platforma(пф)","Kapital ta’mir(КР)", oy + '-' + year);
		int skPoluvagon = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yarim ochiq vagon(пв)","Kapital ta’mir(КР)", oy + '-' + year);
		int skSisterna = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Sisterna(цс)","Kapital ta’mir(КР)", oy + '-' + year);
		int skBoshqa = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Boshqa turdagi(проч)","Kapital ta’mir(КР)", oy + '-' + year);

		model.addAttribute("skHamma",skHamma);
		model.addAttribute("skKriti", skKriti);
		model.addAttribute("skPlatforma", skPlatforma);
		model.addAttribute("skPoluvagon", skPoluvagon);
		model.addAttribute("skSisterna", skSisterna);
		model.addAttribute("skBoshqa", skBoshqa);

		// itogo Fact uchun kapital tamir
		int uvtkhamma = skHamma + hkHamma + akHamma;
		int uvtkKriti = skKriti + hkKriti + akKriti;
		int uvtkPlatforma = skPlatforma + akPlatforma + hkPlatforma;
		int uvtkPoluvagon = skPoluvagon + hkPoluvagon + akPoluvagon;
		int uvtkSisterna = akSisterna + hkSisterna + skSisterna;
		int uvtkBoshqa = akBoshqa + hkBoshqa + skBoshqa;

		model.addAttribute("uvtkhamma",uvtkhamma);
		model.addAttribute("uvtkKriti",uvtkKriti);
		model.addAttribute("uvtkPlatforma",uvtkPlatforma);
		model.addAttribute("uvtkPoluvagon",uvtkPoluvagon);
		model.addAttribute("uvtkSisterna",uvtkSisterna);
		model.addAttribute("uvtkBoshqa",uvtkBoshqa);

		//VCHD-3 uchun KRP
		int hkrHamma = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yopiq vagon (крыт)","KRP(КРП)", oy + '-' + year) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Platforma(пф)","KRP(КРП)", oy + '-' + year) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yarim ochiq vagon(пв)","KRP(КРП)", oy + '-' + year) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Sisterna(цс)","KRP(КРП)", oy + '-' + year) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Boshqa turdagi(проч)","KRP(КРП)", oy + '-' + year);

		int hkrKriti = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yopiq vagon (крыт)"," KRP(КРП)", oy + '-' + year);
		int hkrPlatforma = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Platforma(пф)","KRP(КРП)", oy + '-' + year);
		int hkrPoluvagon = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yarim ochiq vagon(пв)","KRP(КРП)", oy + '-' + year);
		int hkrSisterna = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Sisterna(цс)","KRP(КРП)", oy + '-' + year);
		int hkrBoshqa = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Boshqa turdagi(проч)","KRP(КРП)", oy + '-' + year);

		model.addAttribute("hkrHamma",hkrHamma);
		model.addAttribute("hkrKriti", hkrKriti);
		model.addAttribute("hkrPlatforma", hkrPlatforma);
		model.addAttribute("hkrPoluvagon", hkrPoluvagon);
		model.addAttribute("hkrSisterna", hkrSisterna);
		model.addAttribute("hkrBoshqa", hkrBoshqa);

		//VCHD-5 uchun KRP
		int akrHamma = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yopiq vagon (крыт)","KRP(КРП)", oy + '-' + year) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Platforma(пф)","KRP(КРП)", oy + '-' + year) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yarim ochiq vagon(пв)","KRP(КРП)", oy + '-' + year) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Sisterna(цс)","KRP(КРП)", oy + '-' + year) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Boshqa turdagi(проч)","KRP(КРП)", oy + '-' + year);

		int akrKriti = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yopiq vagon (крыт)","KRP(КРП)", oy + '-' + year);
		int akrPlatforma = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Platforma(пф)","KRP(КРП)", oy + '-' + year);
		int akrPoluvagon = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yarim ochiq vagon(пв)","KRP(КРП)", oy + '-' + year);
		int akrSisterna = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Sisterna(цс)","KRP(КРП)", oy + '-' + year);
		int akrBoshqa = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Boshqa turdagi(проч)","KRP(КРП)", oy + '-' + year);

		model.addAttribute("akrHamma",akrHamma);
		model.addAttribute("akrKriti", akrKriti);
		model.addAttribute("akrPlatforma", akrPlatforma);
		model.addAttribute("akrPoluvagon", akrPoluvagon);
		model.addAttribute("akrSisterna", akrSisterna);
		model.addAttribute("akrBoshqa", akrBoshqa);

		//samarqand uchun KRP tamir
		int skrHamma = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yopiq vagon (крыт)","KRP(КРП)", oy + '-' + year) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Platforma(пф)","KRP(КРП)", oy + '-' + year) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yarim ochiq vagon(пв)","KRP(КРП)", oy + '-' + year) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Sisterna(цс)","KRP(КРП)", oy + '-' + year) +
				vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Boshqa turdagi(проч)","KRP(КРП)", oy + '-' + year);

		int skrKriti = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yopiq vagon (крыт)","KRP(КРП)", oy + '-' + year);
		int skrPlatforma = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Platforma(пф)","KRP(КРП)", oy + '-' + year);
		int skrPoluvagon = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yarim ochiq vagon(пв)","KRP(КРП)", oy + '-' + year);
		int skrSisterna = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Sisterna(цс)","KRP(КРП)", oy + '-' + year);
		int skrBoshqa = vagonTayyorUtyService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Boshqa turdagi(проч)","KRP(КРП)", oy + '-' + year);

		model.addAttribute("skrHamma",skrHamma);
		model.addAttribute("skrKriti", skrKriti);
		model.addAttribute("skrPlatforma", skrPlatforma);
		model.addAttribute("skrPoluvagon", skrPoluvagon);
		model.addAttribute("skrSisterna", skrSisterna);
		model.addAttribute("skrBoshqa", skrBoshqa);

		// itogo Fact uchun KRP
		int uvtkrhamma = skrHamma + hkrHamma + akrHamma;
		int uvtkrKriti = skrKriti + hkrKriti + akrKriti;
		int uvtkrPlatforma = skrPlatforma + akrPlatforma + hkrPlatforma;
		int uvtkrPoluvagon = skrPoluvagon + hkrPoluvagon + akrPoluvagon;
		int uvtkrSisterna = akrSisterna + hkrSisterna + skrSisterna;
		int uvtkrBoshqa = akrBoshqa + hkrBoshqa + skrBoshqa;

		model.addAttribute("uvtkrhamma",uvtkrhamma);
		model.addAttribute("uvtkrKriti",uvtkrKriti);
		model.addAttribute("uvtkrPlatforma",uvtkrPlatforma);
		model.addAttribute("uvtkrPoluvagon",uvtkrPoluvagon);
		model.addAttribute("uvtkrSisterna",uvtkrSisterna);
		model.addAttribute("uvtkrBoshqa",uvtkrBoshqa);


		//yuklab olish uchun list
		List<Integer> vagonsToDownloadTable = new ArrayList<>();
//Depoli tamir
		vagonsToDownloadTable.add(HavDtHammaPlan);
		vagonsToDownloadTable.add(hdHamma);
//		vagonsToDownloadTable.add(hdHamma - HavDtHammaPlan);
		vagonsToDownloadTable.add(HavDtKritiPlan);
		vagonsToDownloadTable.add(hdKriti);
//		vagonsToDownloadTable.add(hdKriti - HavDtKritiPlan);
		vagonsToDownloadTable.add(HavDtPlatformaPlan);
		vagonsToDownloadTable.add(hdPlatforma);
//		vagonsToDownloadTable.add(hdPlatforma - HavDtPlatformaPlan);
		vagonsToDownloadTable.add(HavDtPoluvagonPlan);
		vagonsToDownloadTable.add(hdPoluvagon);
//		vagonsToDownloadTable.add(hdPoluvagon - HavDtPoluvagonPlan);
		vagonsToDownloadTable.add(HavDtSisternaPlan);
		vagonsToDownloadTable.add(hdSisterna);
//		vagonsToDownloadTable.add(hdSisterna - HavDtSisternaPlan);
		vagonsToDownloadTable.add(HavDtBoshqaPlan);
		vagonsToDownloadTable.add(hdBoshqa);
//		vagonsToDownloadTable.add(hdBoshqa - HavDtBoshqaPlan);

		vagonsToDownloadTable.add(AndjDtHammaPlan);
		vagonsToDownloadTable.add(adHamma);
//		vagonsToDownloadTable.add(adHamma - AndjDtHammaPlan);
		vagonsToDownloadTable.add(AndjDtKritiPlan);
		vagonsToDownloadTable.add(adKriti);
//		vagonsToDownloadTable.add(adKriti - AndjDtKritiPlan);
		vagonsToDownloadTable.add(AndjDtPlatformaPlan);
		vagonsToDownloadTable.add(adPlatforma);
//		vagonsToDownloadTable.add(adPlatforma - AndjDtPlatformaPlan);
		vagonsToDownloadTable.add(AndjDtPoluvagonPlan);
		vagonsToDownloadTable.add(adPoluvagon);
//		vagonsToDownloadTable.add(adPoluvagon - AndjDtPoluvagonPlan);
		vagonsToDownloadTable.add(AndjDtSisternaPlan);
		vagonsToDownloadTable.add(adSisterna);
//		vagonsToDownloadTable.add(adSisterna - AndjDtSisternaPlan);
		vagonsToDownloadTable.add(AndjDtBoshqaPlan);
		vagonsToDownloadTable.add(adBoshqa);
//		vagonsToDownloadTable.add(adBoshqa - AndjDtBoshqaPlan);

		vagonsToDownloadTable.add(SamDtHammaPlan);
		vagonsToDownloadTable.add(sdHamma);
//		vagonsToDownloadTable.add(sdHamma - SamDtHammaPlan);
		vagonsToDownloadTable.add(SamDtKritiPlan);
		vagonsToDownloadTable.add(sdKriti);
//		vagonsToDownloadTable.add(sdKriti - SamDtKritiPlan);
		vagonsToDownloadTable.add(SamDtPlatformaPlan);
		vagonsToDownloadTable.add(sdPlatforma);
//		vagonsToDownloadTable.add(sdPlatforma - SamDtPlatformaPlan);
		vagonsToDownloadTable.add(SamDtPoluvagonPlan);
		vagonsToDownloadTable.add(sdPoluvagon);
//		vagonsToDownloadTable.add(sdPoluvagon - SamDtPoluvagonPlan);
		vagonsToDownloadTable.add(SamDtSisternaPlan);
		vagonsToDownloadTable.add(sdSisterna);
//		vagonsToDownloadTable.add(sdSisterna - SamDtSisternaPlan);
		vagonsToDownloadTable.add(SamDtBoshqaPlan);
		vagonsToDownloadTable.add(sdBoshqa);
//		vagonsToDownloadTable.add(sdBoshqa - SamDtBoshqaPlan);

		vagonsToDownloadTable.add(DtHammaPlan);
		vagonsToDownloadTable.add(uvtdhamma);
//		vagonsToDownloadTable.add(uvtdhamma - DtHammaPlan);
		vagonsToDownloadTable.add(DtKritiPlan);
		vagonsToDownloadTable.add(uvtdKriti);
//		vagonsToDownloadTable.add(uvtdKriti - DtKritiPlan);
		vagonsToDownloadTable.add(DtPlatformaPlan);
		vagonsToDownloadTable.add(uvtdPlatforma);
//		vagonsToDownloadTable.add(uvtdPlatforma - DtPlatformaPlan);
		vagonsToDownloadTable.add(DtPoluvagonPlan);
		vagonsToDownloadTable.add(uvtdPoluvagon);
//		vagonsToDownloadTable.add(uvtdPoluvagon - DtPoluvagonPlan);
		vagonsToDownloadTable.add(DtSisternaPlan);
		vagonsToDownloadTable.add(uvtdSisterna);
//		vagonsToDownloadTable.add(uvtdSisterna - DtSisternaPlan);
		vagonsToDownloadTable.add(DtBoshqaPlan);
		vagonsToDownloadTable.add(uvtdBoshqa);
//		vagonsToDownloadTable.add(uvtdBoshqa - DtBoshqaPlan);

//		drTable = vagonsToDownloadTable;
//		vagonsToDownloadTable = new ArrayList<>();

//kapital tamir
		vagonsToDownloadTable.add(HavKtHammaPlan);
		vagonsToDownloadTable.add(hkHamma);
//		vagonsToDownloadTable.add(hkHamma - HavKtHammaPlan);
		vagonsToDownloadTable.add(HavKtKritiPlan);
		vagonsToDownloadTable.add(hkKriti);
//		vagonsToDownloadTable.add(hkKriti - HavKtKritiPlan);
		vagonsToDownloadTable.add(HavKtPlatformaPlan);
		vagonsToDownloadTable.add(hkPlatforma);
//		vagonsToDownloadTable.add(hkPlatforma - HavKtPlatformaPlan);
		vagonsToDownloadTable.add(HavKtPoluvagonPlan);
		vagonsToDownloadTable.add(hkPoluvagon);
//		vagonsToDownloadTable.add(hkPoluvagon - HavKtPoluvagonPlan);
		vagonsToDownloadTable.add(HavKtSisternaPlan);
		vagonsToDownloadTable.add(hkSisterna);
//		vagonsToDownloadTable.add(hkSisterna - HavKtSisternaPlan);
		vagonsToDownloadTable.add(HavKtBoshqaPlan);
		vagonsToDownloadTable.add(hkBoshqa);
//		vagonsToDownloadTable.add(hkBoshqa - HavKtBoshqaPlan);

		vagonsToDownloadTable.add(AndjKtHammaPlan);
		vagonsToDownloadTable.add(akHamma);
//		vagonsToDownloadTable.add(akHamma - AndjKtHammaPlan);
		vagonsToDownloadTable.add(AndjKtKritiPlan);
		vagonsToDownloadTable.add(akKriti);
//		vagonsToDownloadTable.add(akKriti - AndjKtKritiPlan);
		vagonsToDownloadTable.add(AndjKtPlatformaPlan);
		vagonsToDownloadTable.add(akPlatforma);
//		vagonsToDownloadTable.add(akPlatforma - AndjKtPlatformaPlan);
		vagonsToDownloadTable.add(AndjKtPoluvagonPlan);
		vagonsToDownloadTable.add(akPoluvagon);
//		vagonsToDownloadTable.add(akPoluvagon - AndjKtPoluvagonPlan);
		vagonsToDownloadTable.add(AndjKtSisternaPlan);
		vagonsToDownloadTable.add(akSisterna);
//		vagonsToDownloadTable.add(akSisterna -AndjKtSisternaPlan);
		vagonsToDownloadTable.add(AndjKtBoshqaPlan);
		vagonsToDownloadTable.add(akBoshqa);
//		vagonsToDownloadTable.add(akBoshqa - AndjKtBoshqaPlan);

		vagonsToDownloadTable.add(SamKtHammaPlan);
		vagonsToDownloadTable.add(skHamma);
//		vagonsToDownloadTable.add(skHamma - SamKtHammaPlan);
		vagonsToDownloadTable.add(SamKtKritiPlan);
		vagonsToDownloadTable.add(skKriti);
//		vagonsToDownloadTable.add(skKriti - SamKtKritiPlan);
		vagonsToDownloadTable.add(SamKtPlatformaPlan);
		vagonsToDownloadTable.add(skPlatforma);
//		vagonsToDownloadTable.add(skPlatforma - SamKtPlatformaPlan);
		vagonsToDownloadTable.add(SamKtPoluvagonPlan);
		vagonsToDownloadTable.add(skPoluvagon);
//		vagonsToDownloadTable.add(skPoluvagon - SamKtPoluvagonPlan);
		vagonsToDownloadTable.add(SamKtSisternaPlan);
		vagonsToDownloadTable.add(skSisterna);
//		vagonsToDownloadTable.add(skSisterna - SamKtSisternaPlan);
		vagonsToDownloadTable.add(SamKtBoshqaPlan);
		vagonsToDownloadTable.add(skBoshqa);
//		vagonsToDownloadTable.add(skBoshqa - SamKtBoshqaPlan);

		vagonsToDownloadTable.add(KtHammaPlan);
		vagonsToDownloadTable.add(uvtkhamma);
//		vagonsToDownloadTable.add(uvtkhamma - KtHammaPlan);
		vagonsToDownloadTable.add(KtKritiPlan);
		vagonsToDownloadTable.add(uvtkKriti);
//		vagonsToDownloadTable.add(uvtkKriti - KtKritiPlan);
		vagonsToDownloadTable.add(KtPlatformaPlan);
		vagonsToDownloadTable.add(uvtkPlatforma);
//		vagonsToDownloadTable.add(uvtkPlatforma - KtPlatformaPlan);
		vagonsToDownloadTable.add(KtPoluvagonPlan);
		vagonsToDownloadTable.add(uvtkPoluvagon);
//		vagonsToDownloadTable.add(uvtkPoluvagon - KtPoluvagonPlan);
		vagonsToDownloadTable.add(KtSisternaPlan);
		vagonsToDownloadTable.add(uvtkSisterna);
//		vagonsToDownloadTable.add(uvtkSisterna - KtSisternaPlan);
		vagonsToDownloadTable.add(KtBoshqaPlan);
		vagonsToDownloadTable.add(uvtkBoshqa);
//		vagonsToDownloadTable.add(uvtkBoshqa - KtBoshqaPlan);

//		krTable = vagonsToDownloadTable;
//		vagonsToDownloadTable = new ArrayList<>();
//krp
		vagonsToDownloadTable.add(HavKrpHammaPlan);
		vagonsToDownloadTable.add(hkrHamma);
//		vagonsToDownloadTable.add(hkrHamma - HavKrpHammaPlan);
		vagonsToDownloadTable.add(HavKrpKritiPlan);
		vagonsToDownloadTable.add(hkrKriti);
//		vagonsToDownloadTable.add(hkrKriti - HavKrpKritiPlan);
		vagonsToDownloadTable.add(HavKrpPlatformaPlan);
		vagonsToDownloadTable.add(hkrPlatforma);
//		vagonsToDownloadTable.add(hkrPlatforma - HavKrpPlatformaPlan);
		vagonsToDownloadTable.add(HavKrpPoluvagonPlan);
		vagonsToDownloadTable.add(hkrPoluvagon);
//		vagonsToDownloadTable.add(hkrPoluvagon - HavKrpPoluvagonPlan);
		vagonsToDownloadTable.add(HavKrpSisternaPlan);
		vagonsToDownloadTable.add(hkrSisterna);
//		vagonsToDownloadTable.add(hkrSisterna - HavKrpSisternaPlan);
		vagonsToDownloadTable.add(HavKrpBoshqaPlan);
		vagonsToDownloadTable.add(hkrBoshqa);
//		vagonsToDownloadTable.add(hkrBoshqa - HavKrpBoshqaPlan);

		vagonsToDownloadTable.add(AndjKrpHammaPlan);
		vagonsToDownloadTable.add(akrHamma);
//		vagonsToDownloadTable.add(akrHamma - AndjKrpHammaPlan);
		vagonsToDownloadTable.add(AndjKrpKritiPlan);
		vagonsToDownloadTable.add(akrKriti);
//		vagonsToDownloadTable.add(akrKriti - AndjKrpKritiPlan);
		vagonsToDownloadTable.add(AndjKrpPlatformaPlan);
		vagonsToDownloadTable.add(akrPlatforma);
//		vagonsToDownloadTable.add(akrPlatforma - AndjKrpPlatformaPlan);
		vagonsToDownloadTable.add(AndjKrpPoluvagonPlan);
		vagonsToDownloadTable.add(akrPoluvagon);
//		vagonsToDownloadTable.add(akrPoluvagon - AndjKrpPoluvagonPlan);
		vagonsToDownloadTable.add(AndjKrpSisternaPlan);
		vagonsToDownloadTable.add(akrSisterna);
//		vagonsToDownloadTable.add(akrSisterna - AndjKrpSisternaPlan);
		vagonsToDownloadTable.add(AndjKrpBoshqaPlan);
		vagonsToDownloadTable.add(akrBoshqa);
//		vagonsToDownloadTable.add(akrBoshqa - AndjKrpBoshqaPlan);

		vagonsToDownloadTable.add(SamKrpHammaPlan);
		vagonsToDownloadTable.add(skrHamma);
//		vagonsToDownloadTable.add(skrHamma - SamKrpHammaPlan);
		vagonsToDownloadTable.add(SamKrpKritiPlan);
		vagonsToDownloadTable.add(skrKriti);
//		vagonsToDownloadTable.add(skrKriti - SamKrpKritiPlan);
		vagonsToDownloadTable.add(SamKrpPlatformaPlan);
		vagonsToDownloadTable.add(skrPlatforma);
//		vagonsToDownloadTable.add(skrPlatforma - SamKrpPlatformaPlan);
		vagonsToDownloadTable.add(SamKrpPoluvagonPlan);
		vagonsToDownloadTable.add(skrPoluvagon);
//		vagonsToDownloadTable.add(skrPoluvagon - SamKrpPoluvagonPlan);
		vagonsToDownloadTable.add(SamKrpSisternaPlan);
		vagonsToDownloadTable.add(skrSisterna);
//		vagonsToDownloadTable.add(skrSisterna - SamKrpSisternaPlan);
		vagonsToDownloadTable.add(SamKrpBoshqaPlan);
		vagonsToDownloadTable.add(skrBoshqa);
//		vagonsToDownloadTable.add(skrBoshqa - SamKrpBoshqaPlan);

		vagonsToDownloadTable.add(KrpHammaPlan);
		vagonsToDownloadTable.add(uvtkrhamma);
//		vagonsToDownloadTable.add(uvtkrhamma - KrpHammaPlan);
		vagonsToDownloadTable.add(KrpKritiPlan);
		vagonsToDownloadTable.add(uvtkrKriti);
//		vagonsToDownloadTable.add(uvtkrKriti - KrpKritiPlan);
		vagonsToDownloadTable.add(KrpPlatformaPlan);
		vagonsToDownloadTable.add(uvtkrPlatforma);
//		vagonsToDownloadTable.add(uvtkrPlatforma - KrpPlatformaPlan);
		vagonsToDownloadTable.add(KrpPoluvagonPlan);
		vagonsToDownloadTable.add(uvtkrPoluvagon);
//		vagonsToDownloadTable.add(uvtkrPoluvagon - KrpPoluvagonPlan);
		vagonsToDownloadTable.add(KrpSisternaPlan);
		vagonsToDownloadTable.add(uvtkrSisterna);
//		vagonsToDownloadTable.add(uvtkrSisterna - KrpSisternaPlan);
		vagonsToDownloadTable.add(KrpBoshqaPlan);
		vagonsToDownloadTable.add(uvtkrBoshqa);
//		vagonsToDownloadTable.add(uvtkrBoshqa - KrpBoshqaPlan);

//		krpTable = vagonsToDownloadTable;
//		vagonsToDownloadTable = new ArrayList<>();
		vagonsToDownloadAllTable = vagonsToDownloadTable;
	}
	
//		Plan factlar jami oy uchun
	public void getPlanFactforAllMonth(Model model, int year, int month){

		String oy=null;
		switch (month) {
			case 1:
				oy = "01";
				break;
			case 2:
				oy = "02";
				break;
			case 3:
				oy = "03";
				break;
			case 4:
				oy = "04";
				break;
			case 5:
				oy = "05";
				break;
			case 6:
				oy = "06";
				break;
			case 7:
				oy = "07";
				break;
			case 8:
				oy = "08";
				break;
			case 9:
				oy = "09";
				break;
			case 10:
				oy = "10";
				break;
			case 11:
				oy = "11";
				break;
			case 12:
				oy = "12";
				break;
		}

		PlanUty planDto = vagonTayyorUtyService.getPlanutyForYear(String.valueOf(year));
		//planlar kiritish

		//havos depo tamir hamma plan
		int hdKriti = planDto.getHavDtKritiPlanUty();
		int hdPlatforma=planDto.getHavDtPlatformaPlanUty();
		int hdPoluvagon=planDto.getHavDtPoluvagonPlanUty();
		int hdSisterna=planDto.getHavDtSisternaPlanUty();
		int hdBoshqa=planDto.getHavDtBoshqaPlanUty();
		int HavDtHammaPlan = hdKriti + hdPlatforma + hdPoluvagon + hdSisterna + hdBoshqa;

		model.addAttribute("HDtHammaPlan", HavDtHammaPlan);
		model.addAttribute("HDtKritiPlan", hdKriti);
		model.addAttribute("HDtPlatformaPlan", hdPlatforma);
		model.addAttribute("HDtPoluvagonPlan", hdPoluvagon);
		model.addAttribute("HDtSisternaPlan", hdSisterna);
		model.addAttribute("HDtBoshqaPlan", hdBoshqa);

		//VCHD-5 depo tamir plan
		int adKriti = planDto.getAndjDtKritiPlanUty();
		int adPlatforma=planDto.getAndjDtPlatformaPlanUty();
		int adPoluvagon=planDto.getAndjDtPoluvagonPlanUty();
		int adSisterna=planDto.getAndjDtSisternaPlanUty();
		int adBoshqa=planDto.getAndjDtBoshqaPlanUty();
		int AndjDtHammaPlan = adKriti + adPlatforma + adPoluvagon + adSisterna + adBoshqa;

		model.addAttribute("ADtHammaPlan", AndjDtHammaPlan);
		model.addAttribute("ADtKritiPlan", adKriti);
		model.addAttribute("ADtPlatformaPlan",adPlatforma);
		model.addAttribute("ADtPoluvagonPlan", adPoluvagon);
		model.addAttribute("ADtSisternaPlan", adSisterna);
		model.addAttribute("ADtBoshqaPlan", adBoshqa);

		//samarqand depo tamir plan
		int sdKriti = planDto.getSamDtKritiPlanUty();
		int sdPlatforma=planDto.getSamDtPlatformaPlanUty();
		int sdPoluvagon=planDto.getSamDtPoluvagonPlanUty();
		int sdSisterna=planDto.getSamDtSisternaPlanUty();
		int sdBoshqa=planDto.getSamDtBoshqaPlanUty();
		int SamDtHammaPlan=sdKriti+sdPlatforma+sdPoluvagon+sdSisterna+sdBoshqa;

		model.addAttribute("SDtHammaPlan",SamDtHammaPlan);
		model.addAttribute("SDtKritiPlan", sdKriti);
		model.addAttribute("SDtPlatformaPlan", sdPlatforma);
		model.addAttribute("SDtPoluvagonPlan", sdPoluvagon);
		model.addAttribute("SDtSisternaPlan", sdSisterna);
		model.addAttribute("SDtBoshqaPlan", sdBoshqa);

		// Itogo planlar depo tamir
		int DtHammaPlan = AndjDtHammaPlan + HavDtHammaPlan + SamDtHammaPlan;
		int DtKritiPlan = sdKriti + hdKriti + adKriti;
		int DtPlatformaPlan = sdPlatforma + hdPlatforma + adPlatforma;
		int DtPoluvagonPlan = sdPoluvagon + hdPoluvagon + adPoluvagon;
		int DtSisternaPlan = sdSisterna + hdSisterna + adSisterna;
		int DtBoshqaPlan = sdBoshqa + hdBoshqa + adBoshqa;

		model.addAttribute("DtHammaPlan", DtHammaPlan);
		model.addAttribute("DtKritiPlan", DtKritiPlan);
		model.addAttribute("DtPlatformaPlan", DtPlatformaPlan);
		model.addAttribute("DtPoluvagonPlan", DtPoluvagonPlan);
		model.addAttribute("DtSisternaPlan", DtSisternaPlan);
		model.addAttribute("DtBoshqaPlan", DtBoshqaPlan);

		//Samrqand kapital plan
		int skKriti = planDto.getSamKtKritiPlanUty();
		int skPlatforma=planDto.getSamKtPlatformaPlanUty();
		int skPoluvagon=planDto.getSamKtPoluvagonPlanUty();
		int skSisterna=planDto.getSamKtSisternaPlanUty();
		int skBoshqa=planDto.getSamKtBoshqaPlanUty();
		int SamKtHammaPlan=skKriti+skPlatforma+skPoluvagon+skSisterna+skBoshqa;

		model.addAttribute("SKtHammaPlan",SamKtHammaPlan);
		model.addAttribute("SKtKritiPlan", skKriti);
		model.addAttribute("SKtPlatformaPlan", skPlatforma);
		model.addAttribute("SKtPoluvagonPlan", skPoluvagon);
		model.addAttribute("SKtSisternaPlan", skSisterna);
		model.addAttribute("SKtBoshqaPlan", skBoshqa);

		//hovos kapital plan
		int hkKriti = planDto.getHavKtKritiPlanUty();
		int hkPlatforma=planDto.getHavKtPlatformaPlanUty();
		int hkPoluvagon=planDto.getHavKtPoluvagonPlanUty();
		int hkSisterna=planDto.getHavKtSisternaPlanUty();
		int hkBoshqa=planDto.getHavKtBoshqaPlanUty();
		int HavKtHammaPlan = hkKriti + hkPlatforma + hkPoluvagon + hkSisterna + hkBoshqa;

		model.addAttribute("HKtHammaPlan", HavKtHammaPlan);
		model.addAttribute("HKtKritiPlan", hkKriti);
		model.addAttribute("HKtPlatformaPlan", hkPlatforma);
		model.addAttribute("HKtPoluvagonPlan", hkPoluvagon);
		model.addAttribute("HKtSisternaPlan", hkSisterna);
		model.addAttribute("HKtBoshqaPlan", hkBoshqa);

		//ANDIJON kapital plan
		int akKriti = planDto.getAndjKtKritiPlanUty();
		int akPlatforma=planDto.getAndjKtPlatformaPlanUty();
		int akPoluvagon=planDto.getAndjKtPoluvagonPlanUty();
		int akSisterna=planDto.getAndjKtSisternaPlanUty();
		int akBoshqa=planDto.getAndjKtBoshqaPlanUty();
		int AndjKtHammaPlan = akKriti + akPlatforma + akPoluvagon + akSisterna + akBoshqa;


		model.addAttribute("AKtHammaPlan", AndjKtHammaPlan);
		model.addAttribute("AKtKritiPlan", akKriti);
		model.addAttribute("AKtPlatformaPlan", akPlatforma);
		model.addAttribute("AKtPoluvagonPlan", akPoluvagon);
		model.addAttribute("AKtSisternaPlan", akSisterna);
		model.addAttribute("AKtBoshqaPlan", akBoshqa);

		//Itogo kapital plan
		int KtHammaPlan = AndjKtHammaPlan + HavKtHammaPlan + SamKtHammaPlan;
		int KtKritiPlan = skKriti + hkKriti + akKriti;
		int KtPlatformaPlan = skPlatforma + hkPlatforma + akPlatforma;
		int KtPoluvagonPlan =skPoluvagon + hkPoluvagon + akPoluvagon;
		int KtSisternaPlan = skSisterna + hkSisterna + akSisterna;
		int KtBoshqaPlan = skBoshqa + hkBoshqa + akBoshqa;

		model.addAttribute("KtHammaPlan", KtHammaPlan);
		model.addAttribute("KtKritiPlan", KtKritiPlan);
		model.addAttribute("KtPlatformaPlan", KtPlatformaPlan);
		model.addAttribute("KtPoluvagonPlan", KtPoluvagonPlan);
		model.addAttribute("KtSisternaPlan", KtSisternaPlan);
		model.addAttribute("KtBoshqaPlan", KtBoshqaPlan);

		//Samarqankr Krp plan
		int skrKriti = planDto.getSamKrpKritiPlanUty();
		int skrPlatforma=planDto.getSamKrpPlatformaPlanUty();
		int skrPoluvagon=planDto.getSamKrpPoluvagonPlanUty();
		int skrSisterna=planDto.getSamKrpSisternaPlanUty();
		int skrBoshqa=planDto.getSamKrpBoshqaPlanUty();
		int SamKrpHammaPlan=skrKriti+skrPlatforma+skrPoluvagon+skrSisterna+skrBoshqa;

		model.addAttribute("SKrpHammaPlan", SamKrpHammaPlan);
		model.addAttribute("SKrpKritiPlan", skrKriti);
		model.addAttribute("SKrpPlatformaPlan", skrPlatforma);
		model.addAttribute("SKrpPoluvagonPlan", skrPoluvagon);
		model.addAttribute("SKrpSisternaPlan", skrSisterna);
		model.addAttribute("SKrpBoshqaPlan", skrBoshqa);

		//Hovos krp plan
		int hkrKriti = planDto.getHavKrpKritiPlanUty();
		int hkrPlatforma=planDto.getHavKrpPlatformaPlanUty();
		int hkrPoluvagon=planDto.getHavKrpPoluvagonPlanUty();
		int hkrSisterna=planDto.getHavKrpSisternaPlanUty();
		int hkrBoshqa=planDto.getHavKrpBoshqaPlanUty();
		int HavKrpHammaPlan = hkrKriti + hkrPlatforma + hkrPoluvagon + hkrSisterna + hkrBoshqa;

		model.addAttribute("HKrpHammaPlan",HavKrpHammaPlan);
		model.addAttribute("HKrpKritiPlan", hkrKriti);
		model.addAttribute("HKrpPlatformaPlan", hkrPlatforma);
		model.addAttribute("HKrpPoluvagonPlan", hkrPoluvagon);
		model.addAttribute("HKrpSisternaPlan", hkrSisterna);
		model.addAttribute("HKrpBoshqaPlan", hkrBoshqa);

		//andijon krp plan
		int akrKriti = planDto.getAndjKrpKritiPlanUty();
		int akrPlatforma=planDto.getAndjKrpPlatformaPlanUty();
		int akrPoluvagon=planDto.getAndjKrpPoluvagonPlanUty();
		int akrSisterna=planDto.getAndjKrpSisternaPlanUty();
		int akrBoshqa=planDto.getAndjKrpBoshqaPlanUty();
		int AndjKrpHammaPlan = akrKriti + akrPlatforma + akrPoluvagon + akrSisterna + akrBoshqa;

		model.addAttribute("AKrpHammaPlan",AndjKrpHammaPlan);
		model.addAttribute("AKrpKritiPlan", akrKriti);
		model.addAttribute("AKrpPlatformaPlan", akrPlatforma);
		model.addAttribute("AKrpPoluvagonPlan", akrPoluvagon);
		model.addAttribute("AKrpSisternaPlan", akrSisterna);
		model.addAttribute("AKrpBoshqaPlan", akrBoshqa);

		//itogo krp
		int KrpHammaPlan = AndjKrpHammaPlan + HavKrpHammaPlan + SamKrpHammaPlan;
		int KrpKritiPlan = skrKriti + hkrKriti + akrKriti;
		int KrpPlatformaPlan = skrPlatforma + hkrPlatforma + akrPlatforma;
		int KrpPoluvagonPlan = akrPoluvagon + hkrPoluvagon + skrPoluvagon;
		int KrpSisternaPlan = skrSisterna + hkrSisterna + akrSisterna;
		int KrpBoshqaPlan = skrBoshqa + hkrBoshqa + akrBoshqa;

		model.addAttribute("KrpHammaPlan", KrpHammaPlan);
		model.addAttribute("KrpKritiPlan", KrpKritiPlan);
		model.addAttribute("KrpPlatformaPlan", KrpPlatformaPlan);
		model.addAttribute("KrpPoluvagonPlan",KrpPoluvagonPlan);
		model.addAttribute("KrpSisternaPlan", KrpSisternaPlan);
		model.addAttribute("KrpBoshqaPlan", KrpBoshqaPlan);


		//		 VCHD-3 depo tamir hamma false vagonlar soni

		//		2 oylikniki
		int hdKritiFalse= vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yopiq vagon (крыт)","Depoli ta’mir(ДР)", String.valueOf(year));
		int hdPlatformaFalse= vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Platforma(пф)","Depoli ta’mir(ДР)", String.valueOf(year));
		int hdPoluvagonFalse= vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yarim ochiq vagon(пв)","Depoli ta’mir(ДР)", String.valueOf(year));
		int hdSisternaFalse= vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Sisterna(цс)","Depoli ta’mir(ДР)", String.valueOf(year));
		int hdBoshqaFalse= vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Boshqa turdagi(проч)","Depoli ta’mir(ДР)", String.valueOf(year));
		int hdHammaFalse = hdKritiFalse + hdPlatformaFalse+ hdPoluvagonFalse+ hdSisternaFalse + hdBoshqaFalse;

		model.addAttribute("hdHammaFalse",hdHammaFalse);
		model.addAttribute("hdKritiFalse",hdKritiFalse);
		model.addAttribute("hdPlatformaFalse",hdPlatformaFalse);
		model.addAttribute("hdPoluvagonFalse",hdPoluvagonFalse);
		model.addAttribute("hdSisternaFalse",hdSisternaFalse);
		model.addAttribute("hdBoshqaFalse",hdBoshqaFalse);

		// VCHD-5 depo tamir hamma false vagonlar soni
		int adKritiFalse= vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yopiq vagon (крыт)","Depoli ta’mir(ДР)", String.valueOf(year));
		int adPlatformaFalse= vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Platforma(пф)","Depoli ta’mir(ДР)", String.valueOf(year));
		int adPoluvagonFalse= vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yarim ochiq vagon(пв)","Depoli ta’mir(ДР)", String.valueOf(year));
		int adSisternaFalse= vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Sisterna(цс)","Depoli ta’mir(ДР)", String.valueOf(year));
		int adBoshqaFalse= vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Boshqa turdagi(проч)","Depoli ta’mir(ДР)", String.valueOf(year));
		int adHammaFalse = adKritiFalse + adPlatformaFalse+ adPoluvagonFalse+ adSisternaFalse + adBoshqaFalse;

		model.addAttribute("adHammaFalse",adHammaFalse);
		model.addAttribute("adKritiFalse",adKritiFalse);
		model.addAttribute("adPlatformaFalse",adPlatformaFalse);
		model.addAttribute("adPoluvagonFalse",adPoluvagonFalse);
		model.addAttribute("adSisternaFalse",adSisternaFalse);
		model.addAttribute("adBoshqaFalse",adBoshqaFalse);

		// samarqand depo tamir hamma false vagonlar soni
		int sdKritiFalse= vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yopiq vagon (крыт)","Depoli ta’mir(ДР)", String.valueOf(year));
		int sdPlatformaFalse= vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Platforma(пф)","Depoli ta’mir(ДР)", String.valueOf(year));
		int sdPoluvagonFalse= vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yarim ochiq vagon(пв)","Depoli ta’mir(ДР)", String.valueOf(year));
		int sdSisternaFalse= vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Sisterna(цс)","Depoli ta’mir(ДР)", String.valueOf(year));
		int sdBoshqaFalse= vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Boshqa turdagi(проч)","Depoli ta’mir(ДР)", String.valueOf(year));
		int sdHammaFalse = sdKritiFalse + sdPlatformaFalse+ sdPoluvagonFalse+ sdSisternaFalse + sdBoshqaFalse;

		model.addAttribute("sdHammaFalse",sdHammaFalse);
		model.addAttribute("sdKritiFalse",sdKritiFalse);
		model.addAttribute("sdPlatformaFalse",sdPlatformaFalse);
		model.addAttribute("sdPoluvagonFalse",sdPoluvagonFalse);
		model.addAttribute("sdSisternaFalse",sdSisternaFalse);
		model.addAttribute("sdBoshqaFalse",sdBoshqaFalse);

		// depoli tamir itogo uchun
		int dHammaFalse =  adHammaFalse + hdHammaFalse+sdHammaFalse;
		int dKritiFalse = sdKritiFalse + hdKritiFalse + adKritiFalse;
		int dPlatforma = adPlatformaFalse + sdPlatformaFalse + hdPlatformaFalse;
		int dPoluvagon  = adPoluvagonFalse + sdPoluvagonFalse + hdPoluvagonFalse;
		int dSisterna = adSisternaFalse + hdSisternaFalse + sdSisternaFalse;
		int dBoshqa = adBoshqaFalse + hdBoshqaFalse + sdBoshqaFalse;

		model.addAttribute("dHammaFalse",dHammaFalse);
		model.addAttribute("dKritiFalse",dKritiFalse);
		model.addAttribute("dPlatforma",dPlatforma);
		model.addAttribute("dPoluvagon",dPoluvagon);
		model.addAttribute("dSisterna",dSisterna);
		model.addAttribute("dBoshqa",dBoshqa);

		// samarqand KApital tamir hamma false vagonlar soni
		int skKritiFalse= vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yopiq vagon (крыт)","Kapital ta’mir(КР)", String.valueOf(year));
		int skPlatformaFalse= vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Platforma(пф)","Kapital ta’mir(КР)", String.valueOf(year));
		int skPoluvagonFalse= vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yarim ochiq vagon(пв)","Kapital ta’mir(КР)", String.valueOf(year));
		int skSisternaFalse= vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Sisterna(цс)","Kapital ta’mir(КР)", String.valueOf(year));
		int skBoshqaFalse= vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Boshqa turdagi(проч)","Kapital ta’mir(КР)", String.valueOf(year));
		int skHammaFalse = skKritiFalse + skPlatformaFalse+ skPoluvagonFalse+ skSisternaFalse + skBoshqaFalse;

		model.addAttribute("skHammaFalse",skHammaFalse);
		model.addAttribute("skKritiFalse",skKritiFalse);
		model.addAttribute("skPlatformaFalse",skPlatformaFalse);
		model.addAttribute("skPoluvagonFalse",skPoluvagonFalse);
		model.addAttribute("skSisternaFalse",skSisternaFalse);
		model.addAttribute("skBoshqaFalse",skBoshqaFalse);

		// VCHD-3 kapital tamir hamma false vagonlar soni
		int hkKritiFalse= vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yopiq vagon (крыт)","Kapital ta’mir(КР)", String.valueOf(year));
		int hkPlatformaFalse= vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Platforma(пф)","Kapital ta’mir(КР)", String.valueOf(year));
		int hkPoluvagonFalse= vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yarim ochiq vagon(пв)","Kapital ta’mir(КР)", String.valueOf(year));
		int hkSisternaFalse= vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Sisterna(цс)","Kapital ta’mir(КР)", String.valueOf(year));
		int hkBoshqaFalse= vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Boshqa turdagi(проч)","Kapital ta’mir(КР)", String.valueOf(year));
		int hkHammaFalse = hkKritiFalse + hkPlatformaFalse+ hkPoluvagonFalse+ hkSisternaFalse + hkBoshqaFalse;

		model.addAttribute("hkHammaFalse",hkHammaFalse);
		model.addAttribute("hkKritiFalse",hkKritiFalse);
		model.addAttribute("hkPlatformaFalse",hkPlatformaFalse);
		model.addAttribute("hkPoluvagonFalse",hkPoluvagonFalse);
		model.addAttribute("hkSisternaFalse",hkSisternaFalse);
		model.addAttribute("hkBoshqaFalse",hkBoshqaFalse);

		// VCHD-5 kapital tamir hamma false vagonlar soni
		int akKritiFalse= vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yopiq vagon (крыт)","Kapital ta’mir(КР)", String.valueOf(year));
		int akPlatformaFalse= vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Platforma(пф)","Kapital ta’mir(КР)", String.valueOf(year));
		int akPoluvagonFalse= vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yarim ochiq vagon(пв)","Kapital ta’mir(КР)", String.valueOf(year));
		int akSisternaFalse= vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Sisterna(цс)","Kapital ta’mir(КР)", String.valueOf(year));
		int akBoshqaFalse= vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Boshqa turdagi(проч)","Kapital ta’mir(КР)", String.valueOf(year));
		int akHammaFalse = akKritiFalse + akPlatformaFalse+ akPoluvagonFalse+ akSisternaFalse + akBoshqaFalse;

		model.addAttribute("akHammaFalse",akHammaFalse);
		model.addAttribute("akKritiFalse",akKritiFalse);
		model.addAttribute("akPlatformaFalse",akPlatformaFalse);
		model.addAttribute("akPoluvagonFalse",akPoluvagonFalse);
		model.addAttribute("akSisternaFalse",akSisternaFalse);
		model.addAttribute("akBoshqaFalse",akBoshqaFalse);

		// Kapital tamir itogo uchun
		int kHammaFalse =  akHammaFalse + hkHammaFalse+skHammaFalse;
		int kKritiFalse = skKritiFalse + hkKritiFalse + akKritiFalse;
		int kPlatforma = akPlatformaFalse + skPlatformaFalse + hkPlatformaFalse;
		int kPoluvagon  = akPoluvagonFalse + skPoluvagonFalse + hkPoluvagonFalse;
		int kSisterna = akSisternaFalse + hkSisternaFalse + skSisternaFalse;
		int kBoshqa = akBoshqaFalse + hkBoshqaFalse + skBoshqaFalse;

		model.addAttribute("kHammaFalse",kHammaFalse);
		model.addAttribute("kKritiFalse",kKritiFalse);
		model.addAttribute("kPlatforma",kPlatforma);
		model.addAttribute("kPoluvagon",kPoluvagon);
		model.addAttribute("kSisterna",kSisterna);
		model.addAttribute("kBoshqa",kBoshqa);

		//**
		// samarqand KRP tamir hamma false vagonlar soni
		int skrKritiFalse= vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yopiq vagon (крыт)","KRP(КРП)", String.valueOf(year));
		int skrPlatformaFalse= vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Platforma(пф)","KRP(КРП)", String.valueOf(year));
		int skrPoluvagonFalse= vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yarim ochiq vagon(пв)","KRP(КРП)", String.valueOf(year));
		int skrSisternaFalse= vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Sisterna(цс)","KRP(КРП)", String.valueOf(year));
		int skrBoshqaFalse= vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Boshqa turdagi(проч)","KRP(КРП)", String.valueOf(year));
		int skrHammaFalse = skrKritiFalse + skrPlatformaFalse+ skrPoluvagonFalse+ skrSisternaFalse + skrBoshqaFalse;

		model.addAttribute("skrHammaFalse",skrHammaFalse);
		model.addAttribute("skrKritiFalse",skrKritiFalse);
		model.addAttribute("skrPlatformaFalse",skrPlatformaFalse);
		model.addAttribute("skrPoluvagonFalse",skrPoluvagonFalse);
		model.addAttribute("skrSisternaFalse",skrSisternaFalse);
		model.addAttribute("skrBoshqaFalse",skrBoshqaFalse);

		// VCHD-3 KRP tamir hamma false vagonlar soni
		int hkrKritiFalse= vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yopiq vagon (крыт)","KRP(КРП)", String.valueOf(year));
		int hkrPlatformaFalse= vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Platforma(пф)","KRP(КРП)", String.valueOf(year));
		int hkrPoluvagonFalse= vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yarim ochiq vagon(пв)","KRP(КРП)", String.valueOf(year));
		int hkrSisternaFalse= vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Sisterna(цс)","KRP(КРП)", String.valueOf(year));
		int hkrBoshqaFalse= vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Boshqa turdagi(проч)","KRP(КРП)", String.valueOf(year));
		int hkrHammaFalse = hkrKritiFalse + hkrPlatformaFalse+ hkrPoluvagonFalse+ hkrSisternaFalse + hkrBoshqaFalse;

		model.addAttribute("hkrHammaFalse",hkrHammaFalse);
		model.addAttribute("hkrKritiFalse",hkrKritiFalse);
		model.addAttribute("hkrPlatformaFalse",hkrPlatformaFalse);
		model.addAttribute("hkrPoluvagonFalse",hkrPoluvagonFalse);
		model.addAttribute("hkrSisternaFalse",hkrSisternaFalse);
		model.addAttribute("hkrBoshqaFalse",hkrBoshqaFalse);

		// VCHD-5 KRP tamir hamma false vagonlar soni
		int akrKritiFalse= vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yopiq vagon (крыт)","KRP(КРП)", String.valueOf(year));
		int akrPlatformaFalse= vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Platforma(пф)","KRP(КРП)", String.valueOf(year));
		int akrPoluvagonFalse= vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yarim ochiq vagon(пв)","KRP(КРП)", String.valueOf(year));
		int akrSisternaFalse= vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Sisterna(цс)","KRP(КРП)", String.valueOf(year));
		int akrBoshqaFalse= vagonTayyorUtyService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Boshqa turdagi(проч)","KRP(КРП)", String.valueOf(year));
		int akrHammaFalse = akrKritiFalse + akrPlatformaFalse+ akrPoluvagonFalse+ akrSisternaFalse + akrBoshqaFalse;

		model.addAttribute("akrHammaFalse",akrHammaFalse);
		model.addAttribute("akrKritiFalse",akrKritiFalse);
		model.addAttribute("akrPlatformaFalse",akrPlatformaFalse);
		model.addAttribute("akrPoluvagonFalse",akrPoluvagonFalse);
		model.addAttribute("akrSisternaFalse",akrSisternaFalse);
		model.addAttribute("akBoshqaFalse",akBoshqaFalse);

		// Krp itogo uchun
		int krHammaFalse =  akrHammaFalse + hkrHammaFalse+skrHammaFalse;
		int krKritiFalse = skrKritiFalse + hkrKritiFalse + akrKritiFalse;
		int krPlatforma = akrPlatformaFalse + skrPlatformaFalse + hkrPlatformaFalse;
		int krPoluvagon  = akrPoluvagonFalse + skrPoluvagonFalse + hkrPoluvagonFalse;
		int krSisterna = akrSisternaFalse + hkrSisternaFalse + skrSisternaFalse;
		int krBoshqa = akrBoshqaFalse + hkrBoshqaFalse + skrBoshqaFalse;

		model.addAttribute("krHammaFalse",krHammaFalse);
		model.addAttribute("krKritiFalse",krKritiFalse);
		model.addAttribute("krPlatforma",krPlatforma);
		model.addAttribute("krPoluvagon",krPoluvagon);
		model.addAttribute("krSisterna",krSisterna);
		model.addAttribute("krBoshqa",krBoshqa);

	//yuklab olish uchun list
		List<Integer> vagonsToDownloadTable = new ArrayList<>();

	//Depoli tamir
		vagonsToDownloadTable.add(HavDtHammaPlan);
		vagonsToDownloadTable.add(hdHammaFalse);
//		vagonsToDownloadTable.add(hdHammaFalse - HavDtHammaPlan);
		vagonsToDownloadTable.add(hdKriti);
		vagonsToDownloadTable.add(hdKritiFalse);
//		vagonsToDownloadTable.add(hdKritiFalse - hdKriti);
		vagonsToDownloadTable.add(hdPlatforma);
		vagonsToDownloadTable.add(hdPlatformaFalse );
//		vagonsToDownloadTable.add(hdPlatformaFalse - hdPlatforma);
		vagonsToDownloadTable.add(hdPoluvagon);
		vagonsToDownloadTable.add(hdPoluvagonFalse);
//		vagonsToDownloadTable.add(hdPoluvagonFalse - hdPoluvagon);
		vagonsToDownloadTable.add(hdSisterna);
		vagonsToDownloadTable.add(hdSisternaFalse);
//		vagonsToDownloadTable.add(hdSisternaFalse - hdSisterna);
		vagonsToDownloadTable.add(hdBoshqa);
		vagonsToDownloadTable.add(hdBoshqaFalse);
//		vagonsToDownloadTable.add(hdBoshqaFalse - hdBoshqa);

		vagonsToDownloadTable.add(AndjDtHammaPlan);
		vagonsToDownloadTable.add(adHammaFalse);
//		vagonsToDownloadTable.add(adHammaFalse - AndjDtHammaPlan);
		vagonsToDownloadTable.add(adKriti);
		vagonsToDownloadTable.add(adKritiFalse );
//		vagonsToDownloadTable.add(adKritiFalse - adKriti);
		vagonsToDownloadTable.add(adPlatforma);
		vagonsToDownloadTable.add(adPlatformaFalse );
//		vagonsToDownloadTable.add(adPlatformaFalse - adPlatforma);
		vagonsToDownloadTable.add(adPoluvagon);
		vagonsToDownloadTable.add(adPoluvagonFalse);
//		vagonsToDownloadTable.add(adPoluvagonFalse - adPoluvagon);
		vagonsToDownloadTable.add(adSisterna);
		vagonsToDownloadTable.add(adSisternaFalse);
//		vagonsToDownloadTable.add(adSisternaFalse - adSisterna);
		vagonsToDownloadTable.add(adBoshqa);
		vagonsToDownloadTable.add(adBoshqaFalse );
//		vagonsToDownloadTable.add(adBoshqaFalse - adBoshqa);

		vagonsToDownloadTable.add(SamDtHammaPlan);
		vagonsToDownloadTable.add(sdHammaFalse);
//		vagonsToDownloadTable.add(sdHammaFalse - SamDtHammaPlan);
		vagonsToDownloadTable.add(sdKriti);
		vagonsToDownloadTable.add(sdKritiFalse);
//		vagonsToDownloadTable.add(sdKritiFalse - sdKriti);
		vagonsToDownloadTable.add(sdPlatforma);
		vagonsToDownloadTable.add(sdPlatformaFalse);
//		vagonsToDownloadTable.add(sdPlatformaFalse - sdPlatforma);
		vagonsToDownloadTable.add(sdPoluvagon);
		vagonsToDownloadTable.add(sdPoluvagonFalse);
//		vagonsToDownloadTable.add(sdPoluvagonFalse - sdPoluvagon);
		vagonsToDownloadTable.add(sdSisterna);
		vagonsToDownloadTable.add(sdSisternaFalse);
//		vagonsToDownloadTable.add(sdSisternaFalse - sdSisterna);
		vagonsToDownloadTable.add(sdBoshqa);
		vagonsToDownloadTable.add(sdBoshqaFalse);;
//		vagonsToDownloadTable.add(sdBoshqaFalse - sdBoshqa);;

		vagonsToDownloadTable.add(DtHammaPlan);
		vagonsToDownloadTable.add(dHammaFalse);
//		vagonsToDownloadTable.add(dHammaFalse - DtHammaPlan);
		vagonsToDownloadTable.add(DtKritiPlan);
		vagonsToDownloadTable.add(dKritiFalse);
//		vagonsToDownloadTable.add(dKritiFalse - DtKritiPlan);
		vagonsToDownloadTable.add(DtPlatformaPlan);
		vagonsToDownloadTable.add(dPlatforma);
//		vagonsToDownloadTable.add(dPlatforma - DtPlatformaPlan);
		vagonsToDownloadTable.add(DtPoluvagonPlan);
		vagonsToDownloadTable.add(dPoluvagon);
//		vagonsToDownloadTable.add(dPoluvagon - DtPoluvagonPlan);
		vagonsToDownloadTable.add(DtSisternaPlan);
		vagonsToDownloadTable.add(dSisterna );
//		vagonsToDownloadTable.add(dSisterna - DtSisternaPlan);
		vagonsToDownloadTable.add(DtBoshqaPlan);
		vagonsToDownloadTable.add(dBoshqa);
//		vagonsToDownloadTable.add(dBoshqa - DtBoshqaPlan);

//		drTableM = vagonsToDownloadTable;
//		vagonsToDownloadTable = new ArrayList<>();

		//kapital tamir
		vagonsToDownloadTable.add(HavKtHammaPlan);
		vagonsToDownloadTable.add(hkHammaFalse );
//		vagonsToDownloadTable.add(hkHammaFalse - HavKtHammaPlan);
		vagonsToDownloadTable.add(hkKriti);
		vagonsToDownloadTable.add(hkKritiFalse);
//		vagonsToDownloadTable.add(hkKritiFalse - hkKriti);
		vagonsToDownloadTable.add(hkPlatforma);
		vagonsToDownloadTable.add(hkPlatformaFalse);
//		vagonsToDownloadTable.add(hkPlatformaFalse - hkPlatforma);
		vagonsToDownloadTable.add(hkPoluvagon);
		vagonsToDownloadTable.add(hkPoluvagonFalse);
//		vagonsToDownloadTable.add(hkPoluvagonFalse - hkPoluvagon);
		vagonsToDownloadTable.add(hkSisterna);
		vagonsToDownloadTable.add(hkSisternaFalse);
//		vagonsToDownloadTable.add(hkSisternaFalse - hkSisterna);
		vagonsToDownloadTable.add(hkBoshqa);
		vagonsToDownloadTable.add(hkBoshqaFalse );
//		vagonsToDownloadTable.add(hkBoshqaFalse - hkBoshqa);

		vagonsToDownloadTable.add(AndjKtHammaPlan);
		vagonsToDownloadTable.add(akHammaFalse );
//		vagonsToDownloadTable.add(akHammaFalse - AndjKtHammaPlan);
		vagonsToDownloadTable.add(akKriti);
		vagonsToDownloadTable.add(akKritiFalse);
//		vagonsToDownloadTable.add(akKritiFalse - akKriti);
		vagonsToDownloadTable.add(akPlatforma);
		vagonsToDownloadTable.add(akPlatformaFalse);
//		vagonsToDownloadTable.add(akPlatformaFalse - akPlatforma);
		vagonsToDownloadTable.add(akPoluvagon);
		vagonsToDownloadTable.add(akPoluvagonFalse );
//		vagonsToDownloadTable.add(akPoluvagonFalse - akPoluvagon);
		vagonsToDownloadTable.add(akSisterna);
		vagonsToDownloadTable.add(akSisternaFalse);
//		vagonsToDownloadTable.add(akSisternaFalse - akSisterna);
		vagonsToDownloadTable.add(akBoshqa);
		vagonsToDownloadTable.add(akBoshqaFalse);
//		vagonsToDownloadTable.add(akBoshqaFalse - akBoshqa);

		vagonsToDownloadTable.add(SamKtHammaPlan);
		vagonsToDownloadTable.add(skHammaFalse );
//		vagonsToDownloadTable.add(skHammaFalse - SamKtHammaPlan);
		vagonsToDownloadTable.add(skKriti);
		vagonsToDownloadTable.add(skKritiFalse);
//		vagonsToDownloadTable.add(skKritiFalse - skKriti);
		vagonsToDownloadTable.add(skPlatforma);
		vagonsToDownloadTable.add(skPlatformaFalse);
//		vagonsToDownloadTable.add(skPlatformaFalse - skPlatforma);
		vagonsToDownloadTable.add(skPoluvagon);
		vagonsToDownloadTable.add(skPoluvagonFalse);
//		vagonsToDownloadTable.add(skPoluvagonFalse - skPoluvagon);
		vagonsToDownloadTable.add(skSisterna);
		vagonsToDownloadTable.add(skSisternaFalse );
//		vagonsToDownloadTable.add(skSisternaFalse - skSisterna);
		vagonsToDownloadTable.add(skBoshqa);
		vagonsToDownloadTable.add(skBoshqaFalse);
//		vagonsToDownloadTable.add(skBoshqaFalse - skBoshqa);

		vagonsToDownloadTable.add(KtHammaPlan);
		vagonsToDownloadTable.add(kHammaFalse );
//		vagonsToDownloadTable.add(kHammaFalse  -KtHammaPlan);
		vagonsToDownloadTable.add(KtKritiPlan);
		vagonsToDownloadTable.add(kKritiFalse );
//		vagonsToDownloadTable.add(kKritiFalse - KtKritiPlan);
		vagonsToDownloadTable.add(KtPlatformaPlan);
		vagonsToDownloadTable.add(kPlatforma);
//		vagonsToDownloadTable.add(kPlatforma - KtPlatformaPlan);
		vagonsToDownloadTable.add(KtPoluvagonPlan);
		vagonsToDownloadTable.add(kPoluvagon );
//		vagonsToDownloadTable.add(kPoluvagon - KtPoluvagonPlan);
		vagonsToDownloadTable.add(KtSisternaPlan);
		vagonsToDownloadTable.add(kSisterna);
//		vagonsToDownloadTable.add(kSisterna - KtSisternaPlan);
		vagonsToDownloadTable.add(KtBoshqaPlan);
		vagonsToDownloadTable.add(kBoshqa );
//		vagonsToDownloadTable.add(kBoshqa - KtBoshqaPlan);

//		krTableM = vagonsToDownloadTable;
//		vagonsToDownloadTable = new ArrayList<>();

		//KRPP
		vagonsToDownloadTable.add(HavKrpHammaPlan);
		vagonsToDownloadTable.add(hkrHammaFalse );
//		vagonsToDownloadTable.add(hkrHammaFalse - HavKrpHammaPlan);
		vagonsToDownloadTable.add(hkrKriti);
		vagonsToDownloadTable.add(hkrKritiFalse);
//		vagonsToDownloadTable.add(hkrKritiFalse - hkrKriti);
		vagonsToDownloadTable.add(hkrPlatforma);
		vagonsToDownloadTable.add(hkrPlatformaFalse);
//		vagonsToDownloadTable.add(hkrPlatformaFalse - hkrPlatforma);
		vagonsToDownloadTable.add(hkrPoluvagon);
		vagonsToDownloadTable.add(hkrPoluvagonFalse );
//		vagonsToDownloadTable.add(hkrPoluvagonFalse - hkrPoluvagon);
		vagonsToDownloadTable.add(hkrSisterna);
		vagonsToDownloadTable.add(hkrSisternaFalse);
//		vagonsToDownloadTable.add(hkrSisternaFalse - hkrSisterna);
		vagonsToDownloadTable.add(hkrBoshqa);
		vagonsToDownloadTable.add(hkrBoshqaFalse);
//		vagonsToDownloadTable.add(hkrBoshqaFalse - hkrBoshqa);

		vagonsToDownloadTable.add(AndjKrpHammaPlan);
		vagonsToDownloadTable.add(akrHammaFalse);
//		vagonsToDownloadTable.add(akrHammaFalse - AndjKrpHammaPlan);
		vagonsToDownloadTable.add(akrKriti);
		vagonsToDownloadTable.add(akrKritiFalse);
//		vagonsToDownloadTable.add(akrKritiFalse - akrKriti);
		vagonsToDownloadTable.add(akrPlatforma);
		vagonsToDownloadTable.add(akrPlatformaFalse);
//		vagonsToDownloadTable.add(akrPlatformaFalse - akrPlatforma);
		vagonsToDownloadTable.add(akrPoluvagon);
		vagonsToDownloadTable.add(akrPoluvagonFalse);
//		vagonsToDownloadTable.add(akrPoluvagonFalse - akrPoluvagon);
		vagonsToDownloadTable.add(akrSisterna);
		vagonsToDownloadTable.add(akrSisternaFalse);
//		vagonsToDownloadTable.add(akrSisternaFalse - akrSisterna);
		vagonsToDownloadTable.add(akrBoshqa);
		vagonsToDownloadTable.add(akrBoshqaFalse);
//		vagonsToDownloadTable.add(akrBoshqaFalse - akrBoshqa);

		vagonsToDownloadTable.add(SamKrpHammaPlan);
		vagonsToDownloadTable.add(skrHammaFalse);
//		vagonsToDownloadTable.add(skrHammaFalse - SamKrpHammaPlan);
		vagonsToDownloadTable.add(skrKriti);
		vagonsToDownloadTable.add(skrKritiFalse);
//		vagonsToDownloadTable.add(skrKritiFalse - skrKriti);
		vagonsToDownloadTable.add(skrPlatforma);
		vagonsToDownloadTable.add(skrPlatformaFalse);
//		vagonsToDownloadTable.add(skrPlatformaFalse - skrPlatforma);
		vagonsToDownloadTable.add(skrPoluvagon);
		vagonsToDownloadTable.add(skrPoluvagonFalse);
//		vagonsToDownloadTable.add(skrPoluvagonFalse - skrPoluvagon);
		vagonsToDownloadTable.add(skrSisterna);
		vagonsToDownloadTable.add(skrSisternaFalse);
//		vagonsToDownloadTable.add(skrSisternaFalse - skrSisterna);
		vagonsToDownloadTable.add(skrBoshqa);
		vagonsToDownloadTable.add(skrBoshqaFalse);
//		vagonsToDownloadTable.add(skrBoshqaFalse - skrBoshqa);

		vagonsToDownloadTable.add(KrpHammaPlan);
		vagonsToDownloadTable.add(krHammaFalse);
//		vagonsToDownloadTable.add(krHammaFalse - KrpHammaPlan);
		vagonsToDownloadTable.add(KrpKritiPlan);
		vagonsToDownloadTable.add(krKritiFalse);
//		vagonsToDownloadTable.add(krKritiFalse - KrpKritiPlan);
		vagonsToDownloadTable.add(KrpPlatformaPlan);
		vagonsToDownloadTable.add(krPlatforma);
//		vagonsToDownloadTable.add(krPlatforma - KrpPlatformaPlan);
		vagonsToDownloadTable.add(KrpPoluvagonPlan);
		vagonsToDownloadTable.add(krPoluvagon);
//		vagonsToDownloadTable.add(krPoluvagon - KrpPoluvagonPlan);
		vagonsToDownloadTable.add(KrpSisternaPlan);
		vagonsToDownloadTable.add(krSisterna);
//		vagonsToDownloadTable.add(krSisterna - KrpSisternaPlan);
		vagonsToDownloadTable.add(KrpBoshqaPlan);
		vagonsToDownloadTable.add(krBoshqa);
//		vagonsToDownloadTable.add(krBoshqa - KrpBoshqaPlan);

//		krpTableM = vagonsToDownloadTable;
//		vagonsToDownloadTable = new ArrayList<>();
		vagonsToDownloadAllTableMonths = vagonsToDownloadTable;
	}

//Export Table date to Excel
	//One month
//	@PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
//	@GetMapping("/vagons/createExcelTableUty")
//	public void exportTableToExcel(Model model, HttpServletResponse response) throws IOException {
//		vagonTayyorUtyService.exportTableToExcel(drTable, krTable, krpTable, response);
//	//		model.addAttribute("vagons",vagonsToDownloadAllTable);
//	}
//
//	//All month
//	@PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
//	@GetMapping("/vagons/createExcelUtyAllMonth")
//	public void exportTableToExcelMonth(Model model,HttpServletResponse response) throws IOException {
//		vagonTayyorUtyService.exportTableToExcel(drTableM, krTableM, krpTableM, response);
////		model.addAttribute("vagons",vagonsToDownloadAllTable);
//	}

//MAIN
	@PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@GetMapping("/vagons/AllPlanTableUty")
	public String getAllPlan(Model model) {

		String oy = null;
		switch (month) {
			case 1:
				oy = "-1";
				break;
			case 2:
				oy = "-2";
				break;
			case 3:
				oy = "-3";
				break;
			case 4:
				oy = "-04";
				break;
			case 5:
				oy = "-05";
				break;
			case 6:
				oy = "-06";
				break;
			case 7:
				oy = "-07";
				break;
			case 8:
				oy = "-08";
				break;
			case 9:
				oy = "-09";
				break;
			case 10:
				oy = "-10";
				break;
			case 11:
				oy = "-11";
				break;
			case 12:
				oy = "-12";
				break;
		}

		//yangi vagon qoshish
		model.addAttribute("vagon", new VagonTayyorUty());

		//yangi plan qoshish
		model.addAttribute("planDto", new PlanUtyDto());

		//Bir Oylik listni toldirish uchun Oylik
		vagonList = vagonTayyorUtyService.findAllByCreatedDate(oy + "-" + 2022);
		model.addAttribute("vagons", vagonList);

		//Jami Oylik listni toldirish uchun
		vagonListMonths = vagonTayyorUtyService.findAllByCreatedDate(String.valueOf(year));
		model.addAttribute("wagons", vagonListMonths);

		// Table larni toldirish uchun
		getPlanFactForOneMonth(model, year, month);
		getPlanFactforAllMonth(model, year, month);

		return "AllPlanTableUty";
	}

//Search
	@PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@GetMapping("/vagons/searchTayyorUty")
	public String searchByNomer(Model model,  @RequestParam(value = "nomeri", required = false) Integer nomeri) {

		String oy=null;
		switch (month) {
			case 1:
				oy = "-01";
				break;
			case 2:
				oy = "-02";
				break;
			case 3:
				oy = "-03";
				break;
			case 4:
				oy = "-04";
				break;
			case 5:
				oy = "-05";
				break;
			case 6:
				oy = "-06";
				break;
			case 7:
				oy = "-07";
				break;
			case 8:
				oy = "-08";
				break;
			case 9:
				oy = "-09";
				break;
			case 10:
				oy = "-10";
				break;
			case 11:
				oy = "-11";
				break;
			case 12:
				oy = "-12";
				break;
		}

		if(nomeri == null) {
			//Bir Oylik listni toldirish uchun Oylik
			vagonList =  vagonTayyorUtyService.findAllByCreatedDate(oy + "-" + year);
			model.addAttribute("vagons", vagonList);

			//Jami Oylik listni toldirish uchun
			vagonListMonths =  vagonTayyorUtyService.findAllByCreatedDate(String.valueOf(year));
			model.addAttribute("wagons", vagonListMonths);

		}else {
			List<VagonTayyorUty> emptyList = new ArrayList<>();
			vagonList = emptyList;
			vagonListMonths = emptyList;

		//bir oylik vagonlar ichidan qidiradi (Jamini ichidan qidirganligi uchun commentga olingan)
//			VagonTayyorUty vagonTayyorUty = vagonTayyorUtyService.searchByNomer(nomeri, oy + "-" + year);
//			vagonList.add(vagonTayyorUty);

			VagonTayyorUty vagonTayyorUty1 = vagonTayyorUtyService.searchByNomer(nomeri, String.valueOf(year));
			vagonListMonths.add(vagonTayyorUty1);
		//Jami tamirlangan vagonlar ichidan qidirib bir oylikka ham shuni chiqarada
			vagonList.add(vagonTayyorUty1);

			//Bir Oylik listni toldirish uchun Oylik
			model.addAttribute("vagons", new HashSet<>(vagonList));

			//Jami Oylik listni toldirish uchun
			model.addAttribute("wagons", new HashSet<>(vagonListMonths));
		}

		//yangi vagon qoshish
		model.addAttribute("vagon", new VagonTayyorUty());

		//yangi plan qoshish
		model.addAttribute("planDto", new PlanUtyDto());

		// Table larni toldirish uchun
		getPlanFactForOneMonth(model, year, month);
		getPlanFactforAllMonth(model, year, month);

		return "AllPlanTableUty";
	}

//Filter
	@PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@GetMapping("/vagons/filterOneMonthUty")
	public String filterByDepoNomi(Model model,  @RequestParam(value = "depoNomi", required = false) String depoNomi,
								   @RequestParam(value = "vagonTuri", required = false) String vagonTuri) {
		String oy=null;
		switch (month) {
			case 1:
				oy = "-01";
				break;
			case 2:
				oy = "-02";
				break;
			case 3:
				oy = "-03";
				break;
			case 4:
				oy = "-04";
				break;
			case 5:
				oy = "-05";
				break;
			case 6:
				oy = "-06";
				break;
			case 7:
				oy = "-07";
				break;
			case 8:
				oy = "-08";
				break;
			case 9:
				oy = "-09";
				break;
			case 10:
				oy = "-10";
				break;
			case 11:
				oy = "-11";
				break;
			case 12:
				oy = "-12";
				break;
		}

   		if(!depoNomi.equalsIgnoreCase("Hammasi") && !vagonTuri.equalsIgnoreCase("Hammasi") ) {

			//Bir Oylik listni toldirish uchun Oylik
			vagonList =  vagonTayyorUtyService.findAllByDepoNomiAndVagonTuri(depoNomi, vagonTuri, oy + "-" + year);
			model.addAttribute("vagons", vagonList);

			//Jami Oylik listni toldirish uchun
			vagonListMonths =  vagonTayyorUtyService.findAllByDepoNomiAndVagonTuri(depoNomi, vagonTuri, String.valueOf(year));
			model.addAttribute("wagons", vagonListMonths);

   		}else if(depoNomi.equalsIgnoreCase("Hammasi") && !vagonTuri.equalsIgnoreCase("Hammasi") ){

			//Bir Oylik listni toldirish uchun Oylik
			vagonList =  vagonTayyorUtyService.findAllByVagonTuri(vagonTuri, oy + "-" + year);
			model.addAttribute("vagons", vagonList);

			//Jami Oylik listni toldirish uchun
			vagonListMonths =  vagonTayyorUtyService.findAllByVagonTuri(vagonTuri, String.valueOf(year));
			model.addAttribute("wagons", vagonListMonths);


   		}else if(!depoNomi.equalsIgnoreCase("Hammasi") && vagonTuri.equalsIgnoreCase("Hammasi") ){

			//Bir Oylik listni toldirish uchun Oylik
			vagonList =  vagonTayyorUtyService.findAllByDepoNomi(depoNomi , oy + "-" + year);
			model.addAttribute("vagons", vagonList);

			//Jami Oylik listni toldirish uchun
			vagonListMonths =  vagonTayyorUtyService.findAllByDepoNomi(depoNomi , String.valueOf(year));
			model.addAttribute("wagons", vagonListMonths);

   		}else {
			//Bir Oylik listni toldirish uchun Oylik
			vagonList =  vagonTayyorUtyService.findAllByCreatedDate(oy + "-" + year);
			model.addAttribute("vagons", vagonList);

			//Jami Oylik listni toldirish uchun
			vagonListMonths =  vagonTayyorUtyService.findAllByCreatedDate(String.valueOf(year));
			model.addAttribute("wagons", vagonListMonths);
   		}

	    //yangi vagon qoshish
		model.addAttribute("vagon", new VagonTayyorUty());

		//yangi plan qoshish
		model.addAttribute("planDto", new PlanUtyDto());

		// Table larni toldirish uchun
		getPlanFactForOneMonth(model, year, month);
		getPlanFactforAllMonth(model, year, month);
		return "AllPlanTableUty";
	}

//Save new vagon
	@PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@PostMapping("/vagons/saveTayyorUty")
	public String saveVagon(@ModelAttribute("vagon") VagonTayyorUty vagon, HttpServletRequest request, Model model) {
		if (request.isUserInRole("DIRECTOR")) {
			ApiResponse apiResponse = vagonTayyorUtyService.saveVagon(vagon);
			model.addAttribute("message", apiResponse.getMessage());
			model.addAttribute("isSuccess", apiResponse.isSuccess());
		}else if (request.isUserInRole("SAM")) {
			ApiResponse apiResponse = vagonTayyorUtyService.saveVagonSam(vagon);
			model.addAttribute("message", apiResponse.getMessage());
			model.addAttribute("isSuccess", apiResponse.isSuccess());
		}else if (request.isUserInRole("HAV")) {
			ApiResponse apiResponse = vagonTayyorUtyService.saveVagonHav(vagon);
			model.addAttribute("message", apiResponse.getMessage());
			model.addAttribute("isSuccess", apiResponse.isSuccess());
		}else if (request.isUserInRole("ANDJ")) {
			ApiResponse apiResponse = vagonTayyorUtyService.saveVagonAndj(vagon);
			model.addAttribute("message", apiResponse.getMessage());
			model.addAttribute("isSuccess", apiResponse.isSuccess());
		}

		String oy = null;
		switch (month) {
			case 1:
				oy = "-01";
				break;
			case 2:
				oy = "-02";
				break;
			case 3:
				oy = "-03";
				break;
			case 4:
				oy = "-04";
				break;
			case 5:
				oy = "-05";
				break;
			case 6:
				oy = "-06";
				break;
			case 7:
				oy = "-07";
				break;
			case 8:
				oy = "-08";
				break;
			case 9:
				oy = "-09";
				break;
			case 10:
				oy = "-10";
				break;
			case 11:
				oy = "-11";
				break;
			case 12:
				oy = "-12";
				break;
		}

		//yangi vagon qoshish
		model.addAttribute("vagon", new VagonTayyorUty());

		//yangi plan qoshish
		model.addAttribute("planDto", new PlanUtyDto());

		//Bir Oylik listni toldirish uchun Oylik
		vagonList = vagonTayyorUtyService.findAllByCreatedDate(oy + "-" + year);
		model.addAttribute("vagons", vagonList);

		//Jami Oylik listni toldirish uchun
		vagonListMonths = vagonTayyorUtyService.findAllByCreatedDate(String.valueOf(year));
		model.addAttribute("wagons", vagonListMonths);

		// Table larni toldirish uchun
		getPlanFactForOneMonth(model, year, month);
		getPlanFactforAllMonth(model, year, month);

		return "AllPlanTableUty";
	}

//Delete
	@PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@GetMapping("/vagons/deleteTayyorUty/{id}")
	public String deleteVagonForm(@PathVariable("id") Long id, HttpServletRequest request, Model model) throws NotFoundException {
		if (request.isUserInRole("DIRECTOR")) {
			ApiResponse apiResponse = vagonTayyorUtyService.deleteVagonById(id);
			model.addAttribute("message", apiResponse.getMessage());
			model.addAttribute("isSuccess", apiResponse.isSuccess());
		}else if (request.isUserInRole("SAM")) {
			ApiResponse apiResponse = vagonTayyorUtyService.deleteVagonSam(id);
			model.addAttribute("message", apiResponse.getMessage());
			model.addAttribute("isSuccess", apiResponse.isSuccess());
		}else if (request.isUserInRole("HAV")) {
			ApiResponse apiResponse = vagonTayyorUtyService.deleteVagonHav(id);
			model.addAttribute("message", apiResponse.getMessage());
			model.addAttribute("isSuccess", apiResponse.isSuccess());
		}else if (request.isUserInRole("ANDJ")) {
			ApiResponse apiResponse = vagonTayyorUtyService.deleteVagonAndj(id);
			model.addAttribute("message", apiResponse.getMessage());
			model.addAttribute("isSuccess", apiResponse.isSuccess());
		}

		String oy = null;
		switch (month) {
			case 1:
				oy = "-01";
				break;
			case 2:
				oy = "-02";
				break;
			case 3:
				oy = "-03";
				break;
			case 4:
				oy = "-04";
				break;
			case 5:
				oy = "-05";
				break;
			case 6:
				oy = "-06";
				break;
			case 7:
				oy = "-07";
				break;
			case 8:
				oy = "-08";
				break;
			case 9:
				oy = "-09";
				break;
			case 10:
				oy = "-10";
				break;
			case 11:
				oy = "-11";
				break;
			case 12:
				oy = "-12";
				break;
		}

		//yangi vagon qoshish
		model.addAttribute("vagon", new VagonTayyorUty());

		//yangi plan qoshish
		model.addAttribute("planDto", new PlanUtyDto());

		//Bir Oylik listni toldirish uchun Oylik
		vagonList = vagonTayyorUtyService.findAllByCreatedDate(oy + "-" + year);
		model.addAttribute("vagons", vagonList);

		//Jami Oylik listni toldirish uchun
		vagonListMonths = vagonTayyorUtyService.findAllByCreatedDate(String.valueOf(year));
		model.addAttribute("wagons", vagonListMonths);

		// Table larni toldirish uchun
		getPlanFactForOneMonth(model, year, month);
		getPlanFactforAllMonth(model, year, month);

		return "AllPlanTableUty";
	}


//Save Plan
	@PreAuthorize(value = "hasRole('DIRECTOR')")
	@PostMapping("/vagons/savePlanUty")
	public String savePlan(@ModelAttribute("planDto") PlanUtyDto planDto, Model model) {
		ApiResponse apiResponse = vagonTayyorUtyService.savePlan(planDto);
		model.addAttribute("message", apiResponse.getMessage());
		model.addAttribute("isSuccess", apiResponse.isSuccess());

		String oy = null;
		switch (month) {
			case 1:
				oy = "-01";
				break;
			case 2:
				oy = "-02";
				break;
			case 3:
				oy = "-03";
				break;
			case 4:
				oy = "-04";
				break;
			case 5:
				oy = "-05";
				break;
			case 6:
				oy = "-06";
				break;
			case 7:
				oy = "-07";
				break;
			case 8:
				oy = "-08";
				break;
			case 9:
				oy = "-09";
				break;
			case 10:
				oy = "-10";
				break;
			case 11:
				oy = "-11";
				break;
			case 12:
				oy = "-12";
				break;
		}

		//yangi vagon qoshish
		model.addAttribute("vagon", new VagonTayyorUty());

		//yangi plan qoshish
		model.addAttribute("planDto", new PlanUtyDto());

		//Bir Oylik listni toldirish uchun Oylik
		vagonList = vagonTayyorUtyService.findAllByCreatedDate(oy + "-" + year);
		model.addAttribute("vagons", vagonList);

		//Jami Oylik listni toldirish uchun
		vagonListMonths = vagonTayyorUtyService.findAllByCreatedDate(String.valueOf(year));
		model.addAttribute("wagons", vagonListMonths);

		// Table larni toldirish uchun
		getPlanFactForOneMonth(model, year, month);
		getPlanFactforAllMonth(model, year, month);

		return "AllPlanTableUty";
	}





//	@PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
//	@GetMapping("/vagons/filterAllMonthUty")
//	public String filterByDepoNomiForAllMonths(Model model,  @RequestParam(value = "depoNomi", required = false) String depoNomi,
//											   @RequestParam(value = "vagonTuri", required = false) String vagonTuri) {
////   		if(!depoNomi.equalsIgnoreCase("Hammasi") && !vagonTuri.equalsIgnoreCase("Hammasi")){
////   			vagonList = vagonTayyorUtyService.findByDepoNomiAndVagonTuri(depoNomi, vagonTuri);
////   			model.addAttribute("vagons", vagonTayyorUtyService.findByDepoNomiAndVagonTuri(depoNomi, vagonTuri));
////   		}else if(depoNomi.equalsIgnoreCase("Hammasi") && !vagonTuri.equalsIgnoreCase("Hammasi")){
////   			vagonList = vagonTayyorUtyService.findByVagonTuri(vagonTuri);
////   			model.addAttribute("vagons", vagonTayyorUtyService.findByVagonTuri(vagonTuri));
////   		}else if(!depoNomi.equalsIgnoreCase("Hammasi") && vagonTuri.equalsIgnoreCase("Hammasi")){
////   			vagonList = vagonTayyorUtyService.findByDepoNomi(depoNomi );
////   			model.addAttribute("vagons", vagonTayyorUtyService.findByDepoNomi(depoNomi ));
////   		}else {
////   			vagonList = vagonTayyorUtyService.findAll();
////   			model.addAttribute("vagons", vagonTayyorUtyService.findAll());
////   		}
//
//		return "planTableForMonthsUty";
//	}


    //Tayyor yangi vagon qoshish uchun oyna
    @PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@GetMapping("/vagons/newTayyorUty")
	public String createVagonForm(Model model) {
		VagonTayyorUty vagonTayyor = new VagonTayyorUty();
		model.addAttribute("vagon", vagonTayyor);
		return "create_tayyorvagonUty";
	}

    //tahrirlash uchun oyna bir oy
    @PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@GetMapping("/vagons/editTayyorUty/{id}")
	public String editVagonForm(@PathVariable("id") Long id, Model model) {
		model.addAttribute("vagon",vagonTayyorUtyService.getVagonById(id));
		return "edit_tayyorvagonUty";
	}

    //tahrirni saqlash bir oy
    @PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@PostMapping("/vagons/updateTayyorUty/{id}")
	public String updateVagon(@ModelAttribute("vagon") VagonTayyorUty vagon, @PathVariable Long id, HttpServletRequest request) throws NotFoundException {
    	if (request.isUserInRole("DIRECTOR")) {
    		vagonTayyorUtyService.updateVagon(vagon, id);
        }else if (request.isUserInRole("SAM")) {
        	vagonTayyorUtyService.updateVagonSam(vagon, id);
        }else if (request.isUserInRole("HAV")) {
        	vagonTayyorUtyService.updateVagonHav(vagon, id);
        }else if (request.isUserInRole("ANDJ")) {
        	vagonTayyorUtyService.updateVagonAndj(vagon, id);
        }
		return "redirect:/vagons/AllPlanTableUty";
	}
    
    //** oylar uchun update
    //tahrirlash uchun oyna 
    @PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@GetMapping("/vagons/editTayyorUtyMonths/{id}")
	public String editVagonFormMonths(@PathVariable("id") Long id, Model model) {
		model.addAttribute("vagon",vagonTayyorUtyService.getVagonById(id));
		return "edit_tayyorvagonUtyMonths";
	}

    //tahrirni saqlash 
    @PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@PostMapping("/vagons/updateTayyorUtyMonths/{id}")
	public String updateVagonMonths(@ModelAttribute("vagon") VagonTayyorUty vagon, @PathVariable Long id, HttpServletRequest request) throws NotFoundException {
    	if (request.isUserInRole("DIRECTOR")) {
    		vagonTayyorUtyService.updateVagonMonths(vagon, id);
        }else if (request.isUserInRole("SAM")) {
        	vagonTayyorUtyService.updateVagonSamMonths(vagon, id);
        }else if (request.isUserInRole("HAV")) {
        	vagonTayyorUtyService.updateVagonHavMonths(vagon, id);
        }else if (request.isUserInRole("ANDJ")) {
        	vagonTayyorUtyService.updateVagonAndjMonths(vagon, id);
        }
    	return "redirect:/vagons/planTableForMonthsUty";
	}


	//All planlar uchun
    @PreAuthorize(value = "hasRole('DIRECTOR')")
   	@GetMapping("/vagons/newPlanUty")
   	public String addPlan(Model model) {
   		PlanUtyDto planDto = new PlanUtyDto();
   		model.addAttribute("planDto", planDto);
   		return "add_planUty";
   	}

    @PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
   	@GetMapping("/vagons/planTableForMonthsUty")
   	public String getPlanForAllMonths(Model model) {

    	return "planTableForMonthsUty";
    }

//    // wagon nomer orqali qidirish shu oygacha hammasidan
//    @PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
//	@GetMapping("/vagons/searchTayyorAllMonthsUty")
//	public String search(Model model,  @RequestParam(value = "nomeri", required = false) Integer nomeri) {
//		if(nomeri==null  ) {
//			vagonList = vagonTayyorUtyService.findAll();
//			model.addAttribute("vagons", vagonTayyorUtyService.findAll());
//		}else {
//			List<VagonTayyorUty> emptyList = new ArrayList<>();
//			vagonList = emptyList;
//			vagonList.add( vagonTayyorUtyService.findByNomer(nomeri));
//			model.addAttribute("vagons", vagonTayyorUtyService.findByNomer(nomeri));
//		}
//
//    	return "planTableForMonthsUty";
//    }
    


}
