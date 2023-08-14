package com.sms.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sms.dto.PlanBiznesDto;
import com.sms.model.PlanBiznes;
import com.sms.model.VagonTayyorUty;
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

import com.sms.model.VagonTayyor;
import com.sms.service.VagonTayyorBiznesService;

@Controller
public class VagonTayyorBiznesController {

	@Autowired
	private VagonTayyorBiznesService vagonTayyorService;

	LocalDate today = LocalDate.now();
	int month = today.getMonthValue();
	int year = today.getYear();

//Yuklab olish uchun Malumot yigib beradi
	List<VagonTayyor> vagonList = new ArrayList<>();
	List<VagonTayyor> vagonListMonths = new ArrayList<>();
//Yuklab olish uchun Malumot yigib beradi JAdval uchun
	List<Integer> drTable  = new ArrayList<>();
	List<Integer> krTable  = new ArrayList<>();
	List<Integer> krpTable  = new ArrayList<>();
	List<Integer> yolovchiTable  = new ArrayList<>();
	List<Integer> drTableM  = new ArrayList<>();
	List<Integer> krTableM  = new ArrayList<>();
	List<Integer> krpTableM  = new ArrayList<>();
	List<Integer> yolovchiTableM  = new ArrayList<>();

	List<Integer> vagonsToDownloadAllTable = new ArrayList<>();
	List<Integer> vagonsToDownloadAllTableMonths = new ArrayList<>();


	public void getPlanFactForOneMonth(Model model, int month, int year){
	//Oy va Yillik Tugmalardagi text uchun
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
		model.addAttribute("samDate",vagonTayyorService.getSamDate());
		model.addAttribute("havDate", vagonTayyorService.getHavDate());
		model.addAttribute("andjDate",vagonTayyorService.getAndjDate());

		PlanBiznes planDto = vagonTayyorService.getPlanBiznes(oy, String.valueOf(year));

	//havos hamma plan
		int HavDtHammaPlan = planDto.getHavDtKritiPlanBiznes() + planDto.getHavDtPlatformaPlanBiznes() + planDto.getHavDtPoluvagonPlanBiznes() + planDto.getHavDtSisternaPlanBiznes() + planDto.getHavDtBoshqaPlanBiznes();
		int HavDtKritiPlan = planDto.getHavDtKritiPlanBiznes();
		int HavDtPlatformaPlan = planDto.getHavDtPlatformaPlanBiznes();
		int HavDtPoluvagonPlan = planDto.getHavDtPoluvagonPlanBiznes();
		int HavDtSisternaPlan = planDto.getHavDtSisternaPlanBiznes();
		int HavDtBoshqaPlan = planDto.getHavDtBoshqaPlanBiznes();

		model.addAttribute("HavDtHammaPlan", HavDtHammaPlan);
		model.addAttribute("HavDtKritiPlan", HavDtKritiPlan);
		model.addAttribute("HavDtPlatformaPlan", HavDtPlatformaPlan);
		model.addAttribute("HavDtPoluvagonPlan", HavDtPoluvagonPlan);
		model.addAttribute("HavDtSisternaPlan", HavDtSisternaPlan);
		model.addAttribute("HavDtBoshqaPlan", HavDtBoshqaPlan);

		//andijon hamma plan depo tamir
		int AndjDtHammaPlan = planDto.getAndjDtKritiPlanBiznes() + planDto.getAndjDtPlatformaPlanBiznes() + planDto.getAndjDtPoluvagonPlanBiznes() + planDto.getAndjDtSisternaPlanBiznes() + planDto.getAndjDtBoshqaPlanBiznes();
		int AndjDtKritiPlan =  planDto.getAndjDtKritiPlanBiznes();
		int AndjDtPlatformaPlan =  planDto.getAndjDtPlatformaPlanBiznes();
		int AndjDtPoluvagonPlan =  planDto.getAndjDtPoluvagonPlanBiznes();
		int AndjDtSisternaPlan =  planDto.getAndjDtSisternaPlanBiznes();
		int AndjDtBoshqaPlan =  planDto.getAndjDtBoshqaPlanBiznes();

		model.addAttribute("AndjDtHammaPlan", AndjDtHammaPlan);
		model.addAttribute("AndjDtKritiPlan", AndjDtKritiPlan);
		model.addAttribute("AndjDtPlatformaPlan", AndjDtPlatformaPlan);
		model.addAttribute("AndjDtPoluvagonPlan", AndjDtPoluvagonPlan);
		model.addAttribute("AndjDtSisternaPlan", AndjDtSisternaPlan);
		model.addAttribute("AndjDtBoshqaPlan", AndjDtBoshqaPlan);

		//samarqand depo tamir
		int SamDtHammaPlan=planDto.getSamDtKritiPlanBiznes() + planDto.getSamDtPlatformaPlanBiznes() + planDto.getSamDtPoluvagonPlanBiznes() + planDto.getSamDtSisternaPlanBiznes() + planDto.getSamDtBoshqaPlanBiznes();
		int SamDtKritiPlan =  planDto.getSamDtKritiPlanBiznes();
		int SamDtPlatformaPlan =  planDto.getSamDtPlatformaPlanBiznes();
		int SamDtPoluvagonPlan =  planDto.getSamDtPoluvagonPlanBiznes();
		int SamDtSisternaPlan =  planDto.getSamDtSisternaPlanBiznes();
		int SamDtBoshqaPlan =  planDto.getSamDtBoshqaPlanBiznes();


		model.addAttribute("SamDtHammaPlan",SamDtHammaPlan);
		model.addAttribute("SamDtKritiPlan", SamDtKritiPlan);
		model.addAttribute("SamDtPlatformaPlan", SamDtPlatformaPlan);
		model.addAttribute("SamDtPoluvagonPlan", SamDtPoluvagonPlan);
		model.addAttribute("SamDtSisternaPlan", SamDtSisternaPlan);
		model.addAttribute("SamDtBoshqaPlan", SamDtBoshqaPlan);

		// Itogo planlar depo tamir
		int DtHammaPlan = AndjDtHammaPlan + HavDtHammaPlan + SamDtHammaPlan;
		int DtKritiPlan = planDto.getAndjDtKritiPlanBiznes() + planDto.getHavDtKritiPlanBiznes() + planDto.getSamDtKritiPlanBiznes();
		int DtPlatformaPlan = planDto.getAndjDtPlatformaPlanBiznes() + planDto.getHavDtPlatformaPlanBiznes() + planDto.getSamDtPlatformaPlanBiznes();
		int DtPoluvagonPlan = planDto.getAndjDtPoluvagonPlanBiznes() + planDto.getHavDtPoluvagonPlanBiznes() + planDto.getSamDtPoluvagonPlanBiznes();
		int DtSisternaPlan = planDto.getAndjDtSisternaPlanBiznes() + planDto.getHavDtSisternaPlanBiznes() + planDto.getSamDtSisternaPlanBiznes();
		int DtBoshqaPlan = planDto.getAndjDtBoshqaPlanBiznes() + planDto.getHavDtBoshqaPlanBiznes() + planDto.getSamDtBoshqaPlanBiznes();

		model.addAttribute("DtHammaPlan", DtHammaPlan);
		model.addAttribute("DtKritiPlan", DtKritiPlan);
		model.addAttribute("DtPlatformaPlan", DtPlatformaPlan);
		model.addAttribute("DtPoluvagonPlan", DtPoluvagonPlan);
		model.addAttribute("DtSisternaPlan", DtSisternaPlan);
		model.addAttribute("DtBoshqaPlan", DtBoshqaPlan);

		//yolovchi vagonlar plan
		int AndjToYolovchiPlan = planDto.getAndjTYolovchiPlanBiznes();
		int AndjDtYolovchiPlan = planDto.getAndjDtYolovchiPlanBiznes();

		model.addAttribute("AndjToYolovchiPlan", AndjToYolovchiPlan);
		model.addAttribute("AndjDtYolovchiPlan", AndjDtYolovchiPlan);


		//havos kapital tamir uchun plan
		int HavKtHammaPlan = planDto.getHavKtKritiPlanBiznes() + planDto.getHavKtPlatformaPlanBiznes() + planDto.getHavKtPoluvagonPlanBiznes() + planDto.getHavKtSisternaPlanBiznes() + planDto.getHavKtBoshqaPlanBiznes();
		int HavKtKritiPlan = planDto.getHavKtKritiPlanBiznes();
		int HavKtPlatformaPlan = planDto.getHavKtPlatformaPlanBiznes();
		int HavKtPoluvagonPlan = planDto.getHavKtPoluvagonPlanBiznes();
		int HavKtSisternaPlan = planDto.getHavKtSisternaPlanBiznes();
		int HavKtBoshqaPlan = planDto.getHavKtBoshqaPlanBiznes();

		model.addAttribute("HavKtHammaPlan", HavKtHammaPlan);
		model.addAttribute("HavKtKritiPlan", HavKtKritiPlan);
		model.addAttribute("HavKtPlatformaPlan", HavKtPlatformaPlan);
		model.addAttribute("HavKtPoluvagonPlan", HavKtPoluvagonPlan);
		model.addAttribute("HavKtSisternaPlan", HavKtSisternaPlan);
		model.addAttribute("HavKtBoshqaPlan", HavKtBoshqaPlan);

		//VCHD-5 kapital tamir uchun plan
		int AndjKtHammaPlan = planDto.getAndjKtKritiPlanBiznes() + planDto.getAndjKtPlatformaPlanBiznes() + planDto.getAndjKtPoluvagonPlanBiznes() + planDto.getAndjKtSisternaPlanBiznes() + planDto.getAndjKtBoshqaPlanBiznes();
		int AndjKtKritiPlan = planDto.getAndjKtKritiPlanBiznes();
		int AndjKtPlatformaPlan = planDto.getAndjKtPlatformaPlanBiznes();
		int AndjKtPoluvagonPlan = planDto.getAndjKtPoluvagonPlanBiznes();
		int AndjKtSisternaPlan = planDto.getAndjKtSisternaPlanBiznes();
		int AndjKtBoshqaPlan = planDto.getAndjKtBoshqaPlanBiznes();

		model.addAttribute("AndjKtHammaPlan", AndjKtHammaPlan);
		model.addAttribute("AndjKtKritiPlan", AndjKtKritiPlan);
		model.addAttribute("AndjKtPlatformaPlan", AndjKtPlatformaPlan);
		model.addAttribute("AndjKtPoluvagonPlan", AndjKtPoluvagonPlan);
		model.addAttribute("AndjKtSisternaPlan", AndjKtSisternaPlan);
		model.addAttribute("AndjKtBoshqaPlan", AndjKtBoshqaPlan);

		//VCHD-6 kapital tamir uchun plan
		int SamKtHammaPlan = planDto.getSamKtKritiPlanBiznes() + planDto.getSamKtPlatformaPlanBiznes() + planDto.getSamKtPoluvagonPlanBiznes() + planDto.getSamKtSisternaPlanBiznes() + planDto.getSamKtBoshqaPlanBiznes();
		int SamKtKritiPlan = planDto.getSamKtKritiPlanBiznes();
		int SamKtPlatformaPlan = planDto.getSamKtPlatformaPlanBiznes();
		int SamKtPoluvagonPlan = planDto.getSamKtPoluvagonPlanBiznes();
		int SamKtSisternaPlan = planDto.getSamKtSisternaPlanBiznes();
		int SamKtBoshqaPlan = planDto.getSamKtBoshqaPlanBiznes();

		model.addAttribute("SamKtHammaPlan", SamKtHammaPlan);
		model.addAttribute("SamKtKritiPlan", SamKtKritiPlan);
		model.addAttribute("SamKtPlatformaPlan", SamKtPlatformaPlan);
		model.addAttribute("SamKtPoluvagonPlan", SamKtPoluvagonPlan);
		model.addAttribute("SamKtSisternaPlan", SamKtSisternaPlan);
		model.addAttribute("SamKtBoshqaPlan", SamKtBoshqaPlan);

		//kapital itogo
		int KtHammaPlan = AndjKtHammaPlan + HavKtHammaPlan + SamKtHammaPlan;
		int KtKritiPlan = planDto.getAndjKtKritiPlanBiznes() + planDto.getHavKtKritiPlanBiznes() + planDto.getSamKtKritiPlanBiznes();
		int KtPlatformaPlan = planDto.getAndjKtPlatformaPlanBiznes() + planDto.getHavKtPlatformaPlanBiznes() + planDto.getSamKtPlatformaPlanBiznes();
		int KtPoluvagonPlan = planDto.getAndjKtPoluvagonPlanBiznes() + planDto.getHavKtPoluvagonPlanBiznes() + planDto.getSamKtPoluvagonPlanBiznes();
		int KtSisternaPlan = planDto.getAndjKtSisternaPlanBiznes() + planDto.getHavKtSisternaPlanBiznes() + planDto.getSamKtSisternaPlanBiznes();
		int KtBoshqaPlan = planDto.getAndjKtBoshqaPlanBiznes() + planDto.getHavKtBoshqaPlanBiznes() + planDto.getSamKtBoshqaPlanBiznes();

		model.addAttribute("KtHammaPlan", KtHammaPlan);
		model.addAttribute("KtKritiPlan", KtKritiPlan);
		model.addAttribute("KtPlatformaPlan", KtPlatformaPlan);
		model.addAttribute("KtPoluvagonPlan", KtPoluvagonPlan);
		model.addAttribute("KtSisternaPlan", KtSisternaPlan);
		model.addAttribute("KtBoshqaPlan", KtBoshqaPlan);

		//VCHD-3 KRP plan
		int HavKrpHammaPlan =  planDto.getHavKrpKritiPlanBiznes() + planDto.getHavKrpPlatformaPlanBiznes() + planDto.getHavKrpPoluvagonPlanBiznes() + planDto.getHavKrpSisternaPlanBiznes() + planDto.getHavKrpBoshqaPlanBiznes();
		int HavKrpKritiPlan = planDto.getHavKrpKritiPlanBiznes();
		int HavKrpPlatformaPlan = planDto.getHavKrpPlatformaPlanBiznes();
		int HavKrpPoluvagonPlan = planDto.getHavKrpPoluvagonPlanBiznes();
		int HavKrpSisternaPlan = planDto.getHavKrpSisternaPlanBiznes();
		int HavKrpBoshqaPlan = planDto.getHavKrpBoshqaPlanBiznes();

		model.addAttribute("HavKrpHammaPlan", HavKrpHammaPlan);
		model.addAttribute("HavKrpKritiPlan", HavKrpKritiPlan);
		model.addAttribute("HavKrpPlatformaPlan", HavKrpPlatformaPlan);
		model.addAttribute("HavKrpPoluvagonPlan", HavKrpPoluvagonPlan);
		model.addAttribute("HavKrpSisternaPlan", HavKrpSisternaPlan);
		model.addAttribute("HavKrpBoshqaPlan", HavKrpBoshqaPlan);

		//VCHD-5 Krp plan
		int AndjKrpHammaPlan =  planDto.getAndjKrpKritiPlanBiznes() + planDto.getAndjKrpPlatformaPlanBiznes() + planDto.getAndjKrpPoluvagonPlanBiznes() + planDto.getAndjKrpSisternaPlanBiznes() + planDto.getAndjKrpBoshqaPlanBiznes();
		int AndjKrpKritiPlan = planDto.getAndjKrpKritiPlanBiznes();
		int AndjKrpPlatformaPlan = planDto.getAndjKrpPlatformaPlanBiznes();
		int AndjKrpPoluvagonPlan = planDto.getAndjKrpPoluvagonPlanBiznes();
		int AndjKrpSisternaPlan = planDto.getAndjKrpSisternaPlanBiznes();
		int AndjKrpBoshqaPlan = planDto.getAndjKrpBoshqaPlanBiznes();

		model.addAttribute("AndjKrpHammaPlan", AndjKrpHammaPlan);
		model.addAttribute("AndjKrpKritiPlan", AndjKrpKritiPlan);
		model.addAttribute("AndjKrpPlatformaPlan", AndjKrpPlatformaPlan);
		model.addAttribute("AndjKrpPoluvagonPlan", AndjKrpPoluvagonPlan);
		model.addAttribute("AndjKrpSisternaPlan", AndjKrpSisternaPlan);
		model.addAttribute("AndjKrpBoshqaPlan", AndjKrpBoshqaPlan);


		//samarqand KRP plan
		int SamKrpHammaPlan = planDto.getSamKrpKritiPlanBiznes() + planDto.getSamKrpPlatformaPlanBiznes() + planDto.getSamKrpPoluvagonPlanBiznes() + planDto.getSamKrpSisternaPlanBiznes() + planDto.getSamKrpBoshqaPlanBiznes();
		int SamKrpKritiPlan = planDto.getSamKrpKritiPlanBiznes();
		int SamKrpPlatformaPlan = planDto.getSamKrpPlatformaPlanBiznes();
		int SamKrpPoluvagonPlan = planDto.getSamKrpPoluvagonPlanBiznes();
		int SamKrpSisternaPlan = planDto.getSamKrpSisternaPlanBiznes();
		int SamKrpBoshqaPlan = planDto.getSamKrpBoshqaPlanBiznes();

		model.addAttribute("SamKrpHammaPlan", SamKrpHammaPlan);
		model.addAttribute("SamKrpKritiPlan", SamKrpKritiPlan);
		model.addAttribute("SamKrpPlatformaPlan", SamKrpPlatformaPlan);
		model.addAttribute("SamKrpPoluvagonPlan", SamKrpPoluvagonPlan);
		model.addAttribute("SamKrpSisternaPlan", SamKrpSisternaPlan);
		model.addAttribute("SamKrpBoshqaPlan", SamKrpBoshqaPlan);

		//Krp itogo plan
		int KrpHammaPlan = AndjKrpHammaPlan + HavKrpHammaPlan + SamKrpHammaPlan;
		int KrpKritiPlan = planDto.getAndjKrpKritiPlanBiznes() + planDto.getHavKrpKritiPlanBiznes() + planDto.getSamKrpKritiPlanBiznes();
		int KrpPlatformaPlan = planDto.getAndjKrpPlatformaPlanBiznes() + planDto.getHavKrpPlatformaPlanBiznes() + planDto.getSamKrpPlatformaPlanBiznes();
		int KrpPoluvagonPlan = planDto.getAndjKrpPoluvagonPlanBiznes() + planDto.getHavKrpPoluvagonPlanBiznes() + planDto.getSamKrpPoluvagonPlanBiznes();
		int KrpSisternaPlan = planDto.getAndjKrpSisternaPlanBiznes() + planDto.getHavKrpSisternaPlanBiznes() + planDto.getSamKrpSisternaPlanBiznes();
		int KrpBoshqaPlan = planDto.getAndjKrpBoshqaPlanBiznes() + planDto.getHavKrpBoshqaPlanBiznes() + planDto.getSamKrpBoshqaPlanBiznes();

		model.addAttribute("KrpHammaPlan", KrpHammaPlan);
		model.addAttribute("KrpKritiPlan", KrpKritiPlan);
		model.addAttribute("KrpPlatformaPlan", KrpPlatformaPlan);
		model.addAttribute("KrpPoluvagonPlan", KrpPoluvagonPlan);
		model.addAttribute("KrpSisternaPlan", KrpSisternaPlan);
		model.addAttribute("KrpBoshqaPlan", KrpBoshqaPlan);

		// factlar
		
		//VCHD-3 uchun depli tamir
		int hdHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yopiq vagon (крыт)","Depoli ta’mir(ДР)", oy + '-' + year) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Platforma(пф)","Depoli ta’mir(ДР)", oy + '-' + year) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yarim ochiq vagon(пв)","Depoli ta’mir(ДР)", oy + '-' + year) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Sisterna(цс)","Depoli ta’mir(ДР)", oy + '-' + year) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Boshqa turdagi(проч)","Depoli ta’mir(ДР)", oy + '-' + year);
		int hdKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yopiq vagon (крыт)","Depoli ta’mir(ДР)", oy + '-' + year);
		int hdPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Platforma(пф)","Depoli ta’mir(ДР)", oy + '-' + year);
		int hdPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yarim ochiq vagon(пв)","Depoli ta’mir(ДР)", oy + '-' + year);
		int hdSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Sisterna(цс)","Depoli ta’mir(ДР)", oy + '-' + year);
		int hdBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Boshqa turdagi(проч)","Depoli ta’mir(ДР)", oy + '-' + year);

		model.addAttribute("hdHamma",hdHamma);
		model.addAttribute("hdKriti", hdKriti);
		model.addAttribute("hdPlatforma", hdPlatforma);
		model.addAttribute("hdPoluvagon", hdPoluvagon);
		model.addAttribute("hdSisterna", hdSisterna);
		model.addAttribute("hdBoshqa", hdBoshqa);

		//VCHD-5 uchun depli tamir
		int adHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yopiq vagon (крыт)","Depoli ta’mir(ДР)", oy + '-' + year) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Platforma(пф)","Depoli ta’mir(ДР)", oy + '-' + year) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yarim ochiq vagon(пв)","Depoli ta’mir(ДР)", oy + '-' + year) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Boshqa turdagi(проч)","Depoli ta’mir(ДР)", oy + '-' + year) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Sisterna(цс)","Depoli ta’mir(ДР)", oy + '-' + year);
		int adKriti =  vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yopiq vagon (крыт)","Depoli ta’mir(ДР)", oy + '-' + year);
		int adPlatforma =  vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Platforma(пф)","Depoli ta’mir(ДР)", oy + '-' + year);
		int adPoluvagon =  vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yarim ochiq vagon(пв)","Depoli ta’mir(ДР)", oy + '-' + year);
		int adSisterna =  vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Sisterna(цс)","Depoli ta’mir(ДР)", oy + '-' + year);
		int adBoshqa =  vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Boshqa turdagi(проч)","Depoli ta’mir(ДР)", oy + '-' + year);

		model.addAttribute("adHamma",adHamma);
		model.addAttribute("adKriti", adKriti);
		model.addAttribute("adPlatforma", adPlatforma);
		model.addAttribute("adPoluvagon", adPoluvagon);
		model.addAttribute("adSisterna", adSisterna);
		model.addAttribute("adBoshqa", adBoshqa);

		//samarqand uchun depli tamir
		int sdHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yopiq vagon (крыт)","Depoli ta’mir(ДР)", oy + '-' + year) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Platforma(пф)","Depoli ta’mir(ДР)", oy + '-' + year) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yarim ochiq vagon(пв)","Depoli ta’mir(ДР)", oy + '-' + year) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Sisterna(цс)","Depoli ta’mir(ДР)", oy + '-' + year) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Boshqa turdagi(проч)","Depoli ta’mir(ДР)", oy + '-' + year);
		int sdKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yopiq vagon (крыт)","Depoli ta’mir(ДР)", oy + '-' + year);
		int sdPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Platforma(пф)","Depoli ta’mir(ДР)", oy + '-' + year);
		int sdPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yarim ochiq vagon(пв)","Depoli ta’mir(ДР)", oy + '-' + year);
		int sdSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Sisterna(цс)","Depoli ta’mir(ДР)", oy + '-' + year);
		int sdBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Boshqa turdagi(проч)","Depoli ta’mir(ДР)", oy + '-' + year);

		model.addAttribute("sdHamma",sdHamma);
		model.addAttribute("sdKriti", sdKriti);
		model.addAttribute("sdPlatforma", sdPlatforma);
		model.addAttribute("sdPoluvagon", sdPoluvagon);
		model.addAttribute("sdSisterna", sdSisterna);
		model.addAttribute("sdBoshqa", sdBoshqa);

		// itogo Fact uchun depli tamir
		int uvtdHamma = sdHamma + hdHamma + adHamma;
		int uvtdKriti = sdKriti + hdKriti + adKriti;
		int uvtdPlatforma = sdPlatforma + adPlatforma + hdPlatforma;
		int uvtdPoluvagon = sdPoluvagon + hdPoluvagon + adPoluvagon;
		int uvtdSisterna = adSisterna + hdSisterna + sdSisterna;
		int uvtdBoshqa = adBoshqa + hdBoshqa + sdBoshqa;

		model.addAttribute("uvtdHamma",uvtdHamma);
		model.addAttribute("uvtdKriti",uvtdKriti);
		model.addAttribute("uvtdPlatforma",uvtdPlatforma);
		model.addAttribute("uvtdPoluvagon",uvtdPoluvagon);
		model.addAttribute("uvtdSisterna",uvtdSisterna);
		model.addAttribute("uvtdBoshqa",uvtdBoshqa);


		//Yolovchi vagon Fact
		int atYolovchi = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yo'lovchi vagon(пассжир)","TO-3", oy + '-' + year);
		int adYolovchi = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yo'lovchi vagon(пассжир)","Depoli ta’mir(ДР)", oy + '-' + year);

		model.addAttribute("atYolovchi", atYolovchi);
		model.addAttribute("adYolovchi", adYolovchi);

		//VCHD-3 uchun kapital tamir
		int hkHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yopiq vagon (крыт)","Kapital ta’mir(КР)", oy + '-' + year) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Platforma(пф)","Kapital ta’mir(КР)", oy + '-' + year) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yarim ochiq vagon(пв)","Kapital ta’mir(КР)", oy + '-' + year) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Sisterna(цс)","Kapital ta’mir(КР)", oy + '-' + year) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Boshqa turdagi(проч)","Kapital ta’mir(КР)", oy + '-' + year);
		int hkKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yopiq vagon (крыт)","Kapital ta’mir(КР)", oy + '-' + year);
		int hkPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Platforma(пф)","Kapital ta’mir(КР)", oy + '-' + year);
		int hkPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yarim ochiq vagon(пв)","Kapital ta’mir(КР)", oy + '-' + year);
		int hkSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Sisterna(цс)","Kapital ta’mir(КР)", oy + '-' + year);
		int hkBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Boshqa turdagi(проч)","Kapital ta’mir(КР)", oy + '-' + year);

		model.addAttribute("hkHamma",hkHamma);
		model.addAttribute("hkKriti", hkKriti);
		model.addAttribute("hkPlatforma", hkPlatforma);
		model.addAttribute("hkPoluvagon", hkPoluvagon);
		model.addAttribute("hkSisterna", hkSisterna);
		model.addAttribute("hkBoshqa", hkBoshqa);

		//VCHD-3 uchun kapital tamir
		int akHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yopiq vagon (крыт)","Kapital ta’mir(КР)", oy + '-' + year) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Platforma(пф)","Kapital ta’mir(КР)", oy + '-' + year) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yarim ochiq vagon(пв)","Kapital ta’mir(КР)", oy + '-' + year) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Boshqa turdagi(проч)","Kapital ta’mir(КР)", oy + '-' + year) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Sisterna(цс)","Kapital ta’mir(КР)", oy + '-' + year);
		int akKriti =  vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yopiq vagon (крыт)","Kapital ta’mir(КР)", oy + '-' + year);
		int akPlatforma =  vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Platforma(пф)","Kapital ta’mir(КР)", oy + '-' + year);
		int akPoluvagon =  vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yarim ochiq vagon(пв)","Kapital ta’mir(КР)", oy + '-' + year);
		int akSisterna =  vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Sisterna(цс)","Kapital ta’mir(КР)", oy + '-' + year);
		int akBoshqa =  vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Boshqa turdagi(проч)","Kapital ta’mir(КР)", oy + '-' + year);

		model.addAttribute( "akHamma", akHamma);
		model.addAttribute( "akKriti", akKriti);
		model.addAttribute( "akPlatforma", akPlatforma);
		model.addAttribute( "akPoluvagon", akPoluvagon);
		model.addAttribute( "akSisterna", akSisterna);
		model.addAttribute( "akBoshqa", akBoshqa);

		//samarqand uchun Kapital tamir
		int skHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yopiq vagon (крыт)","Kapital ta’mir(КР)", oy + '-' + year) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Platforma(пф)","Kapital ta’mir(КР)", oy + '-' + year) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yarim ochiq vagon(пв)","Kapital ta’mir(КР)", oy + '-' + year) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Sisterna(цс)","Kapital ta’mir(КР)", oy + '-' + year) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Boshqa turdagi(проч)","Kapital ta’mir(КР)", oy + '-' + year);
		int skKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yopiq vagon (крыт)","Kapital ta’mir(КР)", oy + '-' + year);
		int skPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Platforma(пф)","Kapital ta’mir(КР)", oy + '-' + year);
		int skPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yarim ochiq vagon(пв)","Kapital ta’mir(КР)", oy + '-' + year);
		int skSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Sisterna(цс)","Kapital ta’mir(КР)", oy + '-' + year);
		int skBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Boshqa turdagi(проч)","Kapital ta’mir(КР)", oy + '-' + year);

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


		//VCHD-3 uchun kapital tamir
		int hkrHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yopiq vagon (крыт)","KRP(КРП)", oy + '-' + year) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Platforma(пф)","KRP(КРП)", oy + '-' + year) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yarim ochiq vagon(пв)","KRP(КРП)", oy + '-' + year) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Sisterna(цс)","KRP(КРП)", oy + '-' + year) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Boshqa turdagi(проч)","KRP(КРП)", oy + '-' + year);
		int hkrKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yopiq vagon (крыт)","KRP(КРП)", oy + '-' + year);
		int hkrPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Platforma(пф)","KRP(КРП)", oy + '-' + year);
		int hkrPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yarim ochiq vagon(пв)","KRP(КРП)", oy + '-' + year);
		int hkrSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Sisterna(цс)","KRP(КРП)", oy + '-' + year);
		int hkrBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Boshqa turdagi(проч)","KRP(КРП)", oy + '-' + year);

		model.addAttribute("hkrHamma",hkrHamma);
		model.addAttribute("hkrKriti", hkrKriti);
		model.addAttribute("hkrPlatforma", hkrPlatforma);
		model.addAttribute("hkrPoluvagon", hkrPoluvagon);
		model.addAttribute("hkrSisterna", hkrSisterna);
		model.addAttribute("hkrBoshqa", hkrBoshqa);

		//VCHD-3 uchun kapital tamir
		int akrHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yopiq vagon (крыт)","KRP(КРП)", oy + '-' + year) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Platforma(пф)","KRP(КРП)", oy + '-' + year) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yarim ochiq vagon(пв)","KRP(КРП)", oy + '-' + year) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Boshqa turdagi(проч)","KRP(КРП)", oy + '-' + year) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Sisterna(цс)","KRP(КРП)", oy + '-' + year);
		int akrKriti =  vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yopiq vagon (крыт)","KRP(КРП)", oy + '-' + year);
		int akrPlatforma =  vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Platforma(пф)","KRP(КРП)", oy + '-' + year);
		int akrPoluvagon =  vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yarim ochiq vagon(пв)","KRP(КРП)", oy + '-' + year);
		int akrSisterna =  vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Sisterna(цс)","KRP(КРП)", oy + '-' + year);
		int akrBoshqa =  vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Boshqa turdagi(проч)","KRP(КРП)", oy + '-' + year);

		model.addAttribute( "akrHamma", akrHamma);
		model.addAttribute( "akrKriti", akrKriti);
		model.addAttribute( "akrPlatforma", akrPlatforma);
		model.addAttribute( "akrPoluvagon", akrPoluvagon);
		model.addAttribute( "akrSisterna", akrSisterna);
		model.addAttribute( "akrBoshqa", akrBoshqa);

		//samarqand uchun Kapital tamir
		int skrHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yopiq vagon (крыт)","KRP(КРП)", oy + '-' + year) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Platforma(пф)","KRP(КРП)", oy + '-' + year) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yarim ochiq vagon(пв)","KRP(КРП)", oy + '-' + year) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Sisterna(цс)","KRP(КРП)", oy + '-' + year) +
				vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Boshqa turdagi(проч)","KRP(КРП)", oy + '-' + year);
		int skrKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yopiq vagon (крыт)","KRP(КРП)", oy + '-' + year);
		int skrPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Platforma(пф)","KRP(КРП)", oy + '-' + year);
		int skrPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yarim ochiq vagon(пв)","KRP(КРП)", oy + '-' + year);
		int skrSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Sisterna(цс)","KRP(КРП)", oy + '-' + year);
		int skrBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Boshqa turdagi(проч)","KRP(КРП)", oy + '-' + year);

		model.addAttribute("skrHamma",skrHamma);
		model.addAttribute("skrKriti", skrKriti);
		model.addAttribute("skrPlatforma", skrPlatforma);
		model.addAttribute("skrPoluvagon", skrPoluvagon);
		model.addAttribute("skrSisterna", skrSisterna);
		model.addAttribute("skrBoshqa", skrBoshqa);

		// itogo Fact uchun KRP tamir
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
//		drTable = new ArrayList<>();
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
		vagonsToDownloadTable.add(uvtdHamma);
//		vagonsToDownloadTable.add(uvtdHamma - DtHammaPlan);
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
//		krTable = new ArrayList<>();
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
//		krpTable = new ArrayList<>();
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

//Yolovchi
//		yolovchiTable = new ArrayList<>();
		vagonsToDownloadTable.add(AndjToYolovchiPlan);
		vagonsToDownloadTable.add(atYolovchi);
//		vagonsToDownloadTable.add(atYolovchi - AndjToYolovchiPlan);
		vagonsToDownloadTable.add(AndjDtYolovchiPlan);
		vagonsToDownloadTable.add(adYolovchi);
//		vagonsToDownloadTable.add(adYolovchi - AndjDtYolovchiPlan);

//		yolovchiTable = vagonsToDownloadTable;
		vagonsToDownloadAllTable = vagonsToDownloadTable;
	}

	public void getPlanFactForAllMonth(Model model, int month, int year) {
		
		PlanBiznes planDto = vagonTayyorService.getPlanBiznes(String.valueOf(year));
		//planlar kiritish

		//havos depo tamir hamma plan
		int HavDtKritiPlan = planDto.getHavDtKritiPlanBiznes();
		int HavDtPlatformaPlan = planDto.getHavDtPlatformaPlanBiznes();
		int HavDtPoluvagonPlan = planDto.getHavDtPoluvagonPlanBiznes();
		int HavDtSisternaPlan = planDto.getHavDtSisternaPlanBiznes();
		int HavDtBoshqaPlan = planDto.getHavDtBoshqaPlanBiznes();
		int HavDtHammaPlan = HavDtKritiPlan + HavDtPlatformaPlan + HavDtPoluvagonPlan + HavDtSisternaPlan + HavDtBoshqaPlan;

		model.addAttribute("HDtHammaPlan", HavDtHammaPlan);
		model.addAttribute("HDtKritiPlan", HavDtKritiPlan);
		model.addAttribute("HDtPlatformaPlan", HavDtPlatformaPlan);
		model.addAttribute("HDtPoluvagonPlan", HavDtPoluvagonPlan);
		model.addAttribute("HDtSisternaPlan", HavDtSisternaPlan);
		model.addAttribute("HDtBoshqaPlan", HavDtBoshqaPlan);

		//VCHD-5 depo tamir plan
		int AndjDtKritiPlan = planDto.getAndjDtKritiPlanBiznes();
		int AndjDtPlatformaPlan =planDto.getAndjDtPlatformaPlanBiznes();
		int AndjDtPoluvagonPlan =planDto.getAndjDtPoluvagonPlanBiznes();
		int AndjDtSisternaPlan =planDto.getAndjDtSisternaPlanBiznes();
		int AndjDtBoshqaPlan=planDto.getAndjDtBoshqaPlanBiznes();
		int AndjDtHammaPlan = AndjDtKritiPlan + AndjDtPlatformaPlan + AndjDtPoluvagonPlan + AndjDtSisternaPlan + AndjDtBoshqaPlan;

		model.addAttribute("ADtHammaPlan", AndjDtHammaPlan);
		model.addAttribute("ADtKritiPlan", AndjDtKritiPlan);
		model.addAttribute("ADtPlatformaPlan",AndjDtPlatformaPlan);
		model.addAttribute("ADtPoluvagonPlan", AndjDtPoluvagonPlan);
		model.addAttribute("ADtSisternaPlan", AndjDtSisternaPlan);
		model.addAttribute("ADtBoshqaPlan", AndjDtBoshqaPlan);

		//samarqand depo tamir plan
		int SamDtKritiPlan = planDto.getSamDtKritiPlanBiznes();
		int SamDtPlatformaPlan = planDto.getSamDtPlatformaPlanBiznes();
		int SamDtPoluvagonPlan =  planDto.getSamDtPoluvagonPlanBiznes();
		int SamDtSisternaPlan = planDto.getSamDtSisternaPlanBiznes();
		int SamDtBoshqaPlan = planDto.getSamDtBoshqaPlanBiznes();
		int SamDtHammaPlan=SamDtKritiPlan + SamDtPlatformaPlan + SamDtPoluvagonPlan + SamDtSisternaPlan + SamDtBoshqaPlan;

		model.addAttribute("SDtHammaPlan",SamDtHammaPlan);
		model.addAttribute("SDtKritiPlan", SamDtKritiPlan);
		model.addAttribute("SDtPlatformaPlan", SamDtPlatformaPlan);
		model.addAttribute("SDtPoluvagonPlan", SamDtPoluvagonPlan);
		model.addAttribute("SDtSisternaPlan", SamDtSisternaPlan);
		model.addAttribute("SDtBoshqaPlan", SamDtBoshqaPlan);


		// Itogo planlar depo tamir
		int DtHammaPlan = AndjDtHammaPlan + HavDtHammaPlan + SamDtHammaPlan;
		int DtKritiPlan = SamDtKritiPlan + HavDtKritiPlan + AndjDtKritiPlan;
		int DtPlatformaPlan = SamDtPlatformaPlan + HavDtPlatformaPlan + AndjDtPlatformaPlan;
		int DtPoluvagonPlan = SamDtPoluvagonPlan + HavDtPoluvagonPlan + AndjDtPoluvagonPlan;
		int DtSisternaPlan = SamDtSisternaPlan + HavDtSisternaPlan + AndjDtSisternaPlan;
		int DtBoshqaPlan = SamDtBoshqaPlan + HavDtBoshqaPlan + AndjDtBoshqaPlan;

		model.addAttribute("UDtHammaPlan", DtHammaPlan);
		model.addAttribute("UDtKritiPlan", DtKritiPlan);
		model.addAttribute("UDtPlatformaPlan", DtPlatformaPlan);
		model.addAttribute("UDtPoluvagonPlan",DtPoluvagonPlan);
		model.addAttribute("UDtSisternaPlan", DtSisternaPlan);
		model.addAttribute("UDtBoshqaPlan", DtBoshqaPlan);


		//Yolovchi vagon Plan
		int AndjToYolovchiPlan = planDto.getAndjTYolovchiPlanBiznes();
		int AndjDtYolovchiPlan = planDto.getAndjDtYolovchiPlanBiznes();

		model.addAttribute("AToYolovchiPlan", AndjToYolovchiPlan);
		model.addAttribute("ADtYolovchiPlan", AndjDtYolovchiPlan);

		//hovos kapital plan
		int HavKtKritiPlan = planDto.getHavKtKritiPlanBiznes();
		int HavKtPlatformaPlan = planDto.getHavKtPlatformaPlanBiznes();
		int HavKtPoluvagonPlan = planDto.getHavKtPoluvagonPlanBiznes();
		int HavKtSisternaPlan = planDto.getHavKtSisternaPlanBiznes();
		int HavKtBoshqaPlan = planDto.getHavKtBoshqaPlanBiznes();
		int HavKtHammaPlan = HavKtKritiPlan + HavKtPlatformaPlan + HavKtPoluvagonPlan + HavKtSisternaPlan + HavKtBoshqaPlan;

		model.addAttribute("HKtHammaPlan", HavKtHammaPlan);
		model.addAttribute("HKtKritiPlan", HavKtKritiPlan);
		model.addAttribute("HKtPlatformaPlan", HavKtPlatformaPlan);
		model.addAttribute("HKtPoluvagonPlan", HavKtPoluvagonPlan);
		model.addAttribute("HKtSisternaPlan", HavKtSisternaPlan);
		model.addAttribute("HKtBoshqaPlan", HavKtBoshqaPlan);

		//ANDIJON kapital plan
		int AndjKtKritiPlan = planDto.getAndjKtKritiPlanBiznes();
		int AndjKtPlatformaPlan=planDto.getAndjKtPlatformaPlanBiznes();
		int AndjKtPoluvagonPlan=planDto.getAndjKtPoluvagonPlanBiznes();
		int AndjKtSisternaPlan=planDto.getAndjKtSisternaPlanBiznes();
		int AndjKtBoshqaPlan=planDto.getAndjKtBoshqaPlanBiznes();
		int AndjKtHammaPlan = AndjKtKritiPlan + AndjKtPlatformaPlan + AndjKtPoluvagonPlan + AndjKtSisternaPlan + AndjKtBoshqaPlan;

		model.addAttribute("AKtHammaPlan", AndjKtHammaPlan);
		model.addAttribute("AKtKritiPlan", AndjKtKritiPlan);
		model.addAttribute("AKtPlatformaPlan", AndjKtPlatformaPlan);
		model.addAttribute("AKtPoluvagonPlan", AndjKtPoluvagonPlan);
		model.addAttribute("AKtSisternaPlan", AndjKtSisternaPlan);
		model.addAttribute("AKtBoshqaPlan", AndjKtBoshqaPlan);

		//Samrqand kapital plan
		int SamKtKritiPlan = planDto.getSamKtKritiPlanBiznes();
		int SamKtPlatformaPlan = planDto.getSamKtPlatformaPlanBiznes();
		int SamKtPoluvagonPlan = planDto.getSamKtPoluvagonPlanBiznes();
		int SamKtSisternaPlan = planDto.getSamKtSisternaPlanBiznes();
		int SamKtBoshqaPlan = planDto.getSamKtBoshqaPlanBiznes();
		int SamKtHammaPlan = SamKtKritiPlan + SamKtPlatformaPlan + SamKtPoluvagonPlan + SamKtSisternaPlan +SamKtBoshqaPlan;

		model.addAttribute("SKtHammaPlan",SamKtHammaPlan);
		model.addAttribute("SKtKritiPlan", SamKtKritiPlan);
		model.addAttribute("SKtPlatformaPlan", SamKtPlatformaPlan);
		model.addAttribute("SKtPoluvagonPlan", SamKtPoluvagonPlan);
		model.addAttribute("SKtSisternaPlan", SamKtSisternaPlan);
		model.addAttribute("SKtBoshqaPlan", SamKtBoshqaPlan);


		//Itogo kapital plan
		int KtHammaPlan = AndjKtHammaPlan + HavKtHammaPlan + SamKtHammaPlan;
		int KtKritiPlan = SamKtKritiPlan + HavKtKritiPlan + AndjKtKritiPlan;
		int KtPlatformaPlan = SamKtPlatformaPlan + HavKtPlatformaPlan + AndjKtPlatformaPlan;
		int KtPoluvagonPlan = SamKtPoluvagonPlan + HavKtPoluvagonPlan + AndjKtPoluvagonPlan;
		int KtSisternaPlan = SamKtSisternaPlan + HavKtSisternaPlan + AndjKtSisternaPlan;
		int KtBoshqaPlan = SamKtBoshqaPlan + HavKtBoshqaPlan + AndjKtBoshqaPlan;

		model.addAttribute("UKtHammaPlan", KtHammaPlan);
		model.addAttribute("UKtKritiPlan", KtKritiPlan);
		model.addAttribute("UKtPlatformaPlan", KtPlatformaPlan);
		model.addAttribute("UKtPoluvagonPlan",KtPoluvagonPlan);
		model.addAttribute("UKtSisternaPlan", KtSisternaPlan);
		model.addAttribute("UKtBoshqaPlan", KtBoshqaPlan);


		//Hovos krp plan
		int HavKrpKritiPlan = planDto.getHavKrpKritiPlanBiznes();
		int HavKrpPlatformaPlan = planDto.getHavKrpPlatformaPlanBiznes();
		int HavKrpPoluvagonPlan = planDto.getHavKrpPoluvagonPlanBiznes();
		int HavKrpSisternaPlan = planDto.getHavKrpSisternaPlanBiznes();
		int HavKrpBoshqaPlan = planDto.getHavKrpBoshqaPlanBiznes();
		int HavKrpHammaPlan = HavKrpKritiPlan + HavKrpPlatformaPlan + HavKrpPoluvagonPlan + HavKrpSisternaPlan + HavKrpBoshqaPlan;

		model.addAttribute("HKrpHammaPlan",HavKrpHammaPlan);
		model.addAttribute("HKrpKritiPlan", HavKrpKritiPlan);
		model.addAttribute("HKrpPlatformaPlan", HavKrpPlatformaPlan);
		model.addAttribute("HKrpPoluvagonPlan", HavKrpPoluvagonPlan);
		model.addAttribute("HKrpSisternaPlan", HavKrpSisternaPlan);
		model.addAttribute("HKrpBoshqaPlan", HavKrpBoshqaPlan);

		//andijon krp plan
		int AndjKrpKritiPlan = planDto.getAndjKrpKritiPlanBiznes();
		int AndjKrpPlatformaPlan = planDto.getAndjKrpPlatformaPlanBiznes();
		int AndjKrpPoluvagonPlan = planDto.getAndjKrpPoluvagonPlanBiznes();
		int AndjKrpSisternaPlan = planDto.getAndjKrpSisternaPlanBiznes();
		int AndjKrpBoshqaPlan = planDto.getAndjKrpBoshqaPlanBiznes();
		int AndjKrpHammaPlan = AndjKrpKritiPlan + AndjKrpPlatformaPlan + AndjKrpPoluvagonPlan + AndjKrpSisternaPlan + AndjKrpBoshqaPlan;

		model.addAttribute("AKrpHammaPlan",AndjKrpHammaPlan);
		model.addAttribute("AKrpKritiPlan", AndjKrpKritiPlan);
		model.addAttribute("AKrpPlatformaPlan", AndjKrpPlatformaPlan);
		model.addAttribute("AKrpPoluvagonPlan", AndjKrpPoluvagonPlan);
		model.addAttribute("AKrpSisternaPlan", AndjKrpSisternaPlan);
		model.addAttribute("AKrpBoshqaPlan", AndjKrpBoshqaPlan);

		//Samarqankr Krp plan
		int SamKrpKritiPlan = planDto.getSamKrpKritiPlanBiznes();
		int SamKrpPlatformaPlan = planDto.getSamKrpPlatformaPlanBiznes();
		int SamKrpPoluvagonPlan = planDto.getSamKrpPoluvagonPlanBiznes();
		int SamKrpSisternaPlan = planDto.getSamKrpSisternaPlanBiznes();
		int SamKrpBoshqaPlan = planDto.getSamKrpBoshqaPlanBiznes();
		int SamKrpHammaPlan = SamKrpKritiPlan + SamKrpPlatformaPlan + SamKrpPoluvagonPlan + SamKrpSisternaPlan + SamKrpBoshqaPlan;

		model.addAttribute("SKrpHammaPlan", SamKrpHammaPlan);
		model.addAttribute("SKrpKritiPlan", SamKrpKritiPlan);
		model.addAttribute("SKrpPlatformaPlan", SamKrpPlatformaPlan);
		model.addAttribute("SKrpPoluvagonPlan", SamKrpPoluvagonPlan);
		model.addAttribute("SKrpSisternaPlan", SamKrpSisternaPlan);
		model.addAttribute("SKrpBoshqaPlan", SamKrpBoshqaPlan);


		//itogo krp

		int KrpHammaPlan = AndjKrpHammaPlan + HavKrpHammaPlan + SamKrpHammaPlan;
		int KrpKritiPlan = SamKrpKritiPlan + HavKrpKritiPlan + AndjKrpKritiPlan;
		int KrpPlatformaPlan = SamKrpPlatformaPlan + HavKrpPlatformaPlan + AndjKrpPlatformaPlan;
		int KrpPoluvagonPlan = SamKrpPoluvagonPlan + HavKrpPoluvagonPlan + AndjKrpPoluvagonPlan;
		int KrpSisternaPlan = SamKrpSisternaPlan + HavKrpSisternaPlan + AndjKrpSisternaPlan;
		int KrpBoshqaPlan = SamKrpBoshqaPlan + HavKrpBoshqaPlan + AndjKrpBoshqaPlan;

		model.addAttribute("UKrpHammaPlan", KrpHammaPlan);
		model.addAttribute("UKrpKritiPlan", KrpKritiPlan);
		model.addAttribute("UKrpPlatformaPlan", KrpPlatformaPlan);
		model.addAttribute("UKrpPoluvagonPlan",KrpPoluvagonPlan);
		model.addAttribute("UKrpSisternaPlan", KrpSisternaPlan);
		model.addAttribute("UKrpBoshqaPlan", KrpBoshqaPlan);

		//**//
		// VCHD-3 depo tamir hamma false vagonlar soni
		int hdKriti=vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yopiq vagon (крыт)","Depoli ta’mir(ДР)", String.valueOf(year));
		int hdPlatforma=vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Platforma(пф)","Depoli ta’mir(ДР)", String.valueOf(year));
		int hdPoluvagon=vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yarim ochiq vagon(пв)","Depoli ta’mir(ДР)", String.valueOf(year));
		int hdSisterna=vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Sisterna(цс)","Depoli ta’mir(ДР)", String.valueOf(year));
		int hdBoshqa=vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Boshqa turdagi(проч)","Depoli ta’mir(ДР)", String.valueOf(year));
		int hdHamma = hdKriti + hdPlatforma + hdPoluvagon + hdSisterna + hdBoshqa ;

		model.addAttribute("hdHammaFalse", hdHamma );
		model.addAttribute("hdKritiFalse", hdKriti );
		model.addAttribute("hdPlatformaFalse", hdPlatforma);
		model.addAttribute("hdPoluvagonFalse", hdPoluvagon );
		model.addAttribute("hdSisternaFalse", hdSisterna);
		model.addAttribute("hdBoshqaFalse", hdBoshqa);

		// VCHD-5 depo tamir hamma false vagonlar soni
		int adKriti = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yopiq vagon (крыт)","Depoli ta’mir(ДР)", String.valueOf(year));
		int adPlatforma = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Platforma(пф)","Depoli ta’mir(ДР)", String.valueOf(year));
		int adPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yarim ochiq vagon(пв)","Depoli ta’mir(ДР)", String.valueOf(year));
		int adSisterna = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Sisterna(цс)","Depoli ta’mir(ДР)", String.valueOf(year));
		int adBoshqa = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Boshqa turdagi(проч)","Depoli ta’mir(ДР)", String.valueOf(year));
		int adHamma = adKriti + adPlatforma+ adPoluvagon+ adSisterna + adBoshqa;

		model.addAttribute("adHammaFalse", adHamma );
		model.addAttribute("adKritiFalse", adKriti);
		model.addAttribute("adPlatformaFalse", adPlatforma);
		model.addAttribute("adPoluvagonFalse", adPoluvagon);
		model.addAttribute("adSisternaFalse", adSisterna);
		model.addAttribute("adBoshqaFalse", adBoshqa);

		// samarqand depo tamir hamma false vagonlar soni
		int sdKriti = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yopiq vagon (крыт)","Depoli ta’mir(ДР)", String.valueOf(year));
		int sdPlatforma = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Platforma(пф)","Depoli ta’mir(ДР)", String.valueOf(year));
		int sdPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yarim ochiq vagon(пв)","Depoli ta’mir(ДР)", String.valueOf(year));
		int sdSisterna = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Sisterna(цс)","Depoli ta’mir(ДР)", String.valueOf(year));
		int sdBoshqa = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Boshqa turdagi(проч)","Depoli ta’mir(ДР)", String.valueOf(year));
		int sdHamma = sdKriti + sdPlatforma+ sdPoluvagon+ sdSisterna + sdBoshqa;

		model.addAttribute("sdHammaFalse", sdHamma );
		model.addAttribute("sdKritiFalse", sdKriti );
		model.addAttribute("sdPlatformaFalse", sdPlatforma);
		model.addAttribute("sdPoluvagonFalse", sdPoluvagon);
		model.addAttribute("sdSisternaFalse", sdSisterna);
		model.addAttribute("sdBoshqaFalse", sdBoshqa);

		// depoli tamir itogo uchun
		int uvtdHamma =  adHamma + hdHamma+sdHamma ;
		int uvtdKriti = sdKriti + hdKriti + adKriti ;
		int uvtdPlatforma = adPlatforma + sdPlatforma + hdPlatforma ;
		int uvtdPoluvagon  = adPoluvagon + sdPoluvagon + hdPoluvagon;
		int uvtdSisterna = adSisterna + hdSisterna + sdSisterna;
		int uvtdBoshqa = adBoshqa + hdBoshqa + sdBoshqa;

		model.addAttribute("uvtdHammaFalse", uvtdHamma );
		model.addAttribute("uvtdKritiFalse", uvtdKriti );
		model.addAttribute("uvtdPlatformaFalse", uvtdPlatforma );
		model.addAttribute("uvtdPoluvagonFalse", uvtdPoluvagon );
		model.addAttribute("uvtdSisternaFalse", uvtdSisterna);
		model.addAttribute("uvtdBoshqaFalse", uvtdBoshqa );

		//Yolovchi Andijon fact
		int atYolovchi = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yo'lovchi vagon(пассжир)","TO-3", String.valueOf(year));
		int adYolovchi = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yo'lovchi vagon(пассжир)","Depoli ta’mir(ДР)", String.valueOf(year));

		model.addAttribute("atYolovchiFalse", atYolovchi);
		model.addAttribute("adYolovchiFalse", adYolovchi);


		// VCHD-3 kapital tamir hamma false vagonlar soni
		int hkKriti = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yopiq vagon (крыт)","Kapital ta’mir(КР)", String.valueOf(year));
		int hkPlatforma = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Platforma(пф)","Kapital ta’mir(КР)", String.valueOf(year));
		int hkPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yarim ochiq vagon(пв)","Kapital ta’mir(КР)", String.valueOf(year));
		int hkSisterna = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Sisterna(цс)","Kapital ta’mir(КР)", String.valueOf(year));
		int hkBoshqa = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Boshqa turdagi(проч)","Kapital ta’mir(КР)", String.valueOf(year));
		int hkHamma = hkKriti + hkPlatforma+ hkPoluvagon+ hkSisterna + hkBoshqa;

		model.addAttribute("hkHammaFalse", hkHamma);
		model.addAttribute("hkKritiFalse", hkKriti);
		model.addAttribute("hkPlatformaFalse", hkPlatforma);
		model.addAttribute("hkPoluvagonFalse", hkPoluvagon );
		model.addAttribute("hkSisternaFalse", hkSisterna );
		model.addAttribute("hkBoshqaFalse", hkBoshqa);

		// VCHD-5 kapital tamir hamma false vagonlar soni
		int akKriti = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yopiq vagon (крыт)","Kapital ta’mir(КР)", String.valueOf(year));
		int akPlatforma = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Platforma(пф)","Kapital ta’mir(КР)", String.valueOf(year));
		int akPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yarim ochiq vagon(пв)","Kapital ta’mir(КР)", String.valueOf(year));
		int akSisterna = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Sisterna(цс)","Kapital ta’mir(КР)", String.valueOf(year));
		int akBoshqa = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Boshqa turdagi(проч)","Kapital ta’mir(КР)", String.valueOf(year));
		int akHamma = akKriti + akPlatforma+ akPoluvagon+ akSisterna + akBoshqa;

		model.addAttribute("akHammaFalse", akHamma);
		model.addAttribute("akKritiFalse", akKriti);
		model.addAttribute("akPlatformaFalse", akPlatforma);
		model.addAttribute("akPoluvagonFalse", akPoluvagon);
		model.addAttribute("akSisternaFalse", akSisterna);
		model.addAttribute("akBoshqaFalse", akBoshqa);

		// samarqand KApital tamir hamma false vagonlar soni
		int skKriti = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yopiq vagon (крыт)","Kapital ta’mir(КР)", String.valueOf(year));
		int skPlatforma = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Platforma(пф)","Kapital ta’mir(КР)", String.valueOf(year));
		int skPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yarim ochiq vagon(пв)","Kapital ta’mir(КР)", String.valueOf(year));
		int skSisterna = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Sisterna(цс)","Kapital ta’mir(КР)", String.valueOf(year));
		int skBoshqa = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Boshqa turdagi(проч)","Kapital ta’mir(КР)", String.valueOf(year)) ;
		int skHamma = skKriti + skPlatforma+ skPoluvagon+ skSisterna + skBoshqa;

		model.addAttribute("skHammaFalse", skHamma);
		model.addAttribute("skKritiFalse", skKriti);
		model.addAttribute("skPlatformaFalse", skPlatforma);
		model.addAttribute("skPoluvagonFalse", skPoluvagon);
		model.addAttribute("skSisternaFalse", skSisterna);
		model.addAttribute("skBoshqaFalse", skBoshqa);

		// Kapital tamir itogo uchun
		int uvtkHamma =  akHamma + hkHamma+skHamma;
		int uvtkKriti = skKriti + hkKriti + akKriti;
		int uvtkPlatforma = akPlatforma + skPlatforma + hkPlatforma;
		int uvtkPoluvagon  = akPoluvagon + skPoluvagon + hkPoluvagon;
		int uvtkSisterna = akSisterna + hkSisterna + skSisterna;
		int uvtkBoshqa = akBoshqa + hkBoshqa + skBoshqa;

		model.addAttribute("uvtkHammaFalse", uvtkHamma );
		model.addAttribute("uvtkKritiFalse", uvtkKriti );
		model.addAttribute("uvtkPlatformaFalse", uvtkPlatforma);
		model.addAttribute("uvtkPoluvagonFalse", uvtkPoluvagon);
		model.addAttribute("uvtkSisternaFalse", uvtkSisterna );
		model.addAttribute("uvtkBoshqaFalse", uvtkBoshqa );

		//**

		// VCHD-3 KRP tamir hamma false vagonlar soni
		int hkrKriti = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yopiq vagon (крыт)","KRP(КРП)", String.valueOf(year));
		int hkrPlatforma = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Platforma(пф)","KRP(КРП)", String.valueOf(year));
		int hkrPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yarim ochiq vagon(пв)","KRP(КРП)", String.valueOf(year));
		int hkrSisterna = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Sisterna(цс)","KRP(КРП)", String.valueOf(year));
		int hkrBoshqa = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Boshqa turdagi(проч)","KRP(КРП)", String.valueOf(year));
		int hkrHamma = hkrKriti + hkrPlatforma+ hkrPoluvagon+ hkrSisterna + hkrBoshqa;

		model.addAttribute("hkrHammaFalse", hkrHamma);
		model.addAttribute("hkrKritiFalse", hkrKriti);
		model.addAttribute("hkrPlatformaFalse", hkrPlatforma);
		model.addAttribute("hkrPoluvagonFalse", hkrPoluvagon);
		model.addAttribute("hkrSisternaFalse", hkrSisterna);
		model.addAttribute("hkrBoshqaFalse", hkrBoshqa);

		// VCHD-5 KRP tamir hamma false vagonlar soni
		int akrKriti=vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yopiq vagon (крыт)","KRP(КРП)", String.valueOf(year));
		int akrPlatforma=vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Platforma(пф)","KRP(КРП)", String.valueOf(year));
		int akrPoluvagon=vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yarim ochiq vagon(пв)","KRP(КРП)", String.valueOf(year));
		int akrSisterna=vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Sisterna(цс)","KRP(КРП)", String.valueOf(year));
		int akrBoshqa=vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Boshqa turdagi(проч)","KRP(КРП)", String.valueOf(year));
		int akrHamma = akrKriti + akrPlatforma+ akrPoluvagon+ akrSisterna + akrBoshqa;

		model.addAttribute("akrHammaFalse", akrHamma);
		model.addAttribute("akrKritiFalse", akrKriti);
		model.addAttribute("akrPlatformaFalse", akrPlatforma);
		model.addAttribute("akrPoluvagonFalse", akrPoluvagon);
		model.addAttribute("akrSisternaFalse", akrSisterna);
		model.addAttribute("akBoshqaFalse", akrBoshqa);

		// samarqand KRP tamir hamma false vagonlar soni
		int skrKriti=vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yopiq vagon (крыт)","KRP(КРП)", String.valueOf(year));
		int skrPlatforma=vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Platforma(пф)","KRP(КРП)", String.valueOf(year));
		int skrPoluvagon=vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yarim ochiq vagon(пв)","KRP(КРП)", String.valueOf(year));
		int skrSisterna=vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Sisterna(цс)","KRP(КРП)", String.valueOf(year));
		int skrBoshqa=vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Boshqa turdagi(проч)","KRP(КРП)", String.valueOf(year));
		int skrHamma = skrKriti + skrPlatforma+ skrPoluvagon+ skrSisterna + skrBoshqa;

		model.addAttribute("skrHammaFalse", skrHamma);
		model.addAttribute("skrKritiFalse", skrKriti);
		model.addAttribute("skrPlatformaFalse", skrPlatforma);
		model.addAttribute("skrPoluvagonFalse", skrPoluvagon);
		model.addAttribute("skrSisternaFalse", skrSisterna);
		model.addAttribute("skrBoshqaFalse", skrBoshqa);
// Krp itogo uchun
		int uvtkrHamma =  akrHamma + hkrHamma+skrHamma;
		int uvtkrKriti = skrKriti + hkrKriti + akrKriti;
		int uvtkrPlatforma = akrPlatforma + skrPlatforma + hkrPlatforma;
		int uvtkrPoluvagon  = akrPoluvagon + skrPoluvagon + hkrPoluvagon;
		int uvtkrSisterna = akrSisterna + hkrSisterna + skrSisterna;
		int uvtkrBoshqa = akrBoshqa + hkrBoshqa + skrBoshqa;

		model.addAttribute("uvtkrHammaFalse", uvtkrHamma );
		model.addAttribute("uvtkrKritiFalse", uvtkrKriti);
		model.addAttribute("uvtkrPlatformaFalse", uvtkrPlatforma);
		model.addAttribute("uvtkrPoluvagonFalse", uvtkrPoluvagon);
		model.addAttribute("uvtkrSisternaFalse", uvtkrSisterna);
		model.addAttribute("uvtkrBoshqaFalse", uvtkrBoshqa);


		//yuklab olish uchun list
		List<Integer> vagonsToDownloadTable = new ArrayList<>();
//Depoli tamir
//		krTableM = new ArrayList<>();
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
		vagonsToDownloadTable.add(uvtdHamma);
//		vagonsToDownloadTable.add(uvtdHamma - DtHammaPlan);
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

//		drTableM = vagonsToDownloadTable;
//		vagonsToDownloadTable = new ArrayList<>();

//kapital tamir
//		krTableM = new ArrayList<>();
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
		vagonsToDownloadTable.add(uvtkHamma);
//		vagonsToDownloadTable.add(uvtkHamma - KtHammaPlan);
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

//		krTableM = vagonsToDownloadTable;
//		vagonsToDownloadTable = new ArrayList<>();
//krp
//		krpTableM = new ArrayList<>();
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
		vagonsToDownloadTable.add(uvtkrHamma);
//		vagonsToDownloadTable.add(uvtkrHamma - KrpHammaPlan);
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

//		krpTableM = vagonsToDownloadTable;
//		vagonsToDownloadTable = new ArrayList<>();
//
////yolovchi
//		yolovchiTableM = new ArrayList<>();
		vagonsToDownloadTable.add(AndjToYolovchiPlan);
		vagonsToDownloadTable.add(atYolovchi);
//		vagonsToDownloadTable.add(atYolovchi - AndjToYolovchiPlan);
		vagonsToDownloadTable.add(AndjDtYolovchiPlan);
		vagonsToDownloadTable.add(adYolovchi);
//		vagonsToDownloadTable.add(adYolovchi - AndjDtYolovchiPlan);
//		yolovchiTableM = vagonsToDownloadTable;

		vagonsToDownloadAllTableMonths = vagonsToDownloadTable;

	}

	public void getPlanFactForOneMonthInFilter(Model model, int month, int year, String country){

		//Oy va Yillik Tugmalardagi text uchun
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
		model.addAttribute("samDate",vagonTayyorService.getSamDate());
		model.addAttribute("havDate", vagonTayyorService.getHavDate());
		model.addAttribute("andjDate",vagonTayyorService.getAndjDate());

		PlanBiznes planDto = vagonTayyorService.getPlanBiznes(oy, String.valueOf(year));
		//planlar kiritish

		//havos hamma plan
		int HavDtHammaPlan = planDto.getHavDtKritiPlanBiznes() + planDto.getHavDtPlatformaPlanBiznes() + planDto.getHavDtPoluvagonPlanBiznes() + planDto.getHavDtSisternaPlanBiznes() + planDto.getHavDtBoshqaPlanBiznes();
		int HavDtKritiPlan = planDto.getHavDtKritiPlanBiznes();
		int HavDtPlatformaPlan = planDto.getHavDtPlatformaPlanBiznes();
		int HavDtPoluvagonPlan = planDto.getHavDtPoluvagonPlanBiznes();
		int HavDtSisternaPlan = planDto.getHavDtSisternaPlanBiznes();
		int HavDtBoshqaPlan = planDto.getHavDtBoshqaPlanBiznes();

		model.addAttribute("HavDtHammaPlan", HavDtHammaPlan);
		model.addAttribute("HavDtKritiPlan", HavDtKritiPlan);
		model.addAttribute("HavDtPlatformaPlan", HavDtPlatformaPlan);
		model.addAttribute("HavDtPoluvagonPlan", HavDtPoluvagonPlan);
		model.addAttribute("HavDtSisternaPlan", HavDtSisternaPlan);
		model.addAttribute("HavDtBoshqaPlan", HavDtBoshqaPlan);

		//andijon hamma plan depo tamir
		int AndjDtHammaPlan = planDto.getAndjDtKritiPlanBiznes() + planDto.getAndjDtPlatformaPlanBiznes() + planDto.getAndjDtPoluvagonPlanBiznes() + planDto.getAndjDtSisternaPlanBiznes() + planDto.getAndjDtBoshqaPlanBiznes();
		int AndjDtKritiPlan =  planDto.getAndjDtKritiPlanBiznes();
		int AndjDtPlatformaPlan =  planDto.getAndjDtPlatformaPlanBiznes();
		int AndjDtPoluvagonPlan =  planDto.getAndjDtPoluvagonPlanBiznes();
		int AndjDtSisternaPlan =  planDto.getAndjDtSisternaPlanBiznes();
		int AndjDtBoshqaPlan =  planDto.getAndjDtBoshqaPlanBiznes();

		model.addAttribute("AndjDtHammaPlan", AndjDtHammaPlan);
		model.addAttribute("AndjDtKritiPlan", AndjDtKritiPlan);
		model.addAttribute("AndjDtPlatformaPlan", AndjDtPlatformaPlan);
		model.addAttribute("AndjDtPoluvagonPlan", AndjDtPoluvagonPlan);
		model.addAttribute("AndjDtSisternaPlan", AndjDtSisternaPlan);
		model.addAttribute("AndjDtBoshqaPlan", AndjDtBoshqaPlan);

		//samarqand depo tamir
		int SamDtHammaPlan=planDto.getSamDtKritiPlanBiznes() + planDto.getSamDtPlatformaPlanBiznes() + planDto.getSamDtPoluvagonPlanBiznes() + planDto.getSamDtSisternaPlanBiznes() + planDto.getSamDtBoshqaPlanBiznes();
		int SamDtKritiPlan =  planDto.getSamDtKritiPlanBiznes();
		int SamDtPlatformaPlan =  planDto.getSamDtPlatformaPlanBiznes();
		int SamDtPoluvagonPlan =  planDto.getSamDtPoluvagonPlanBiznes();
		int SamDtSisternaPlan =  planDto.getSamDtSisternaPlanBiznes();
		int SamDtBoshqaPlan =  planDto.getSamDtBoshqaPlanBiznes();

		model.addAttribute("SamDtHammaPlan",SamDtHammaPlan);
		model.addAttribute("SamDtKritiPlan", SamDtKritiPlan);
		model.addAttribute("SamDtPlatformaPlan", SamDtPlatformaPlan);
		model.addAttribute("SamDtPoluvagonPlan", SamDtPoluvagonPlan);
		model.addAttribute("SamDtSisternaPlan", SamDtSisternaPlan);
		model.addAttribute("SamDtBoshqaPlan", SamDtBoshqaPlan);

		// Itogo planlar depo tamir
		int DtHammaPlan = AndjDtHammaPlan + HavDtHammaPlan + SamDtHammaPlan;
		int DtKritiPlan = planDto.getAndjDtKritiPlanBiznes() + planDto.getHavDtKritiPlanBiznes() + planDto.getSamDtKritiPlanBiznes();
		int DtPlatformaPlan = planDto.getAndjDtPlatformaPlanBiznes() + planDto.getHavDtPlatformaPlanBiznes() + planDto.getSamDtPlatformaPlanBiznes();
		int DtPoluvagonPlan = planDto.getAndjDtPoluvagonPlanBiznes() + planDto.getHavDtPoluvagonPlanBiznes() + planDto.getSamDtPoluvagonPlanBiznes();
		int DtSisternaPlan = planDto.getAndjDtSisternaPlanBiznes() + planDto.getHavDtSisternaPlanBiznes() + planDto.getSamDtSisternaPlanBiznes();
		int DtBoshqaPlan = planDto.getAndjDtBoshqaPlanBiznes() + planDto.getHavDtBoshqaPlanBiznes() + planDto.getSamDtBoshqaPlanBiznes();

		model.addAttribute("DtHammaPlan", DtHammaPlan);
		model.addAttribute("DtKritiPlan", DtKritiPlan);
		model.addAttribute("DtPlatformaPlan", DtPlatformaPlan);
		model.addAttribute("DtPoluvagonPlan", DtPoluvagonPlan);
		model.addAttribute("DtSisternaPlan", DtSisternaPlan);
		model.addAttribute("DtBoshqaPlan", DtBoshqaPlan);

		//yolovchi vagonlar plan
		int AndjToYolovchiPlan = planDto.getAndjTYolovchiPlanBiznes();
		int AndjDtYolovchiPlan = planDto.getAndjDtYolovchiPlanBiznes();

		model.addAttribute("AndjToYolovchiPlan", AndjToYolovchiPlan);
		model.addAttribute("AndjDtYolovchiPlan", AndjDtYolovchiPlan);
		
		//havos kapital tamir uchun plan
		int HavKtHammaPlan = planDto.getHavKtKritiPlanBiznes() + planDto.getHavKtPlatformaPlanBiznes() + planDto.getHavKtPoluvagonPlanBiznes() + planDto.getHavKtSisternaPlanBiznes() + planDto.getHavKtBoshqaPlanBiznes();
		int HavKtKritiPlan = planDto.getHavKtKritiPlanBiznes();
		int HavKtPlatformaPlan = planDto.getHavKtPlatformaPlanBiznes();
		int HavKtPoluvagonPlan = planDto.getHavKtPoluvagonPlanBiznes();
		int HavKtSisternaPlan = planDto.getHavKtSisternaPlanBiznes();
		int HavKtBoshqaPlan = planDto.getHavKtBoshqaPlanBiznes();

		model.addAttribute("HavKtHammaPlan", HavKtHammaPlan);
		model.addAttribute("HavKtKritiPlan", HavKtKritiPlan);
		model.addAttribute("HavKtPlatformaPlan", HavKtPlatformaPlan);
		model.addAttribute("HavKtPoluvagonPlan", HavKtPoluvagonPlan);
		model.addAttribute("HavKtSisternaPlan", HavKtSisternaPlan);
		model.addAttribute("HavKtBoshqaPlan", HavKtBoshqaPlan);

		//VCHD-5 kapital tamir uchun plan
		int AndjKtHammaPlan = planDto.getAndjKtKritiPlanBiznes() + planDto.getAndjKtPlatformaPlanBiznes() + planDto.getAndjKtPoluvagonPlanBiznes() + planDto.getAndjKtSisternaPlanBiznes() + planDto.getAndjKtBoshqaPlanBiznes();
		int AndjKtKritiPlan = planDto.getAndjKtKritiPlanBiznes();
		int AndjKtPlatformaPlan = planDto.getAndjKtPlatformaPlanBiznes();
		int AndjKtPoluvagonPlan = planDto.getAndjKtPoluvagonPlanBiznes();
		int AndjKtSisternaPlan = planDto.getAndjKtSisternaPlanBiznes();
		int AndjKtBoshqaPlan = planDto.getAndjKtBoshqaPlanBiznes();

		model.addAttribute("AndjKtHammaPlan", AndjKtHammaPlan);
		model.addAttribute("AndjKtKritiPlan", AndjKtKritiPlan);
		model.addAttribute("AndjKtPlatformaPlan", AndjKtPlatformaPlan);
		model.addAttribute("AndjKtPoluvagonPlan", AndjKtPoluvagonPlan);
		model.addAttribute("AndjKtSisternaPlan", AndjKtSisternaPlan);
		model.addAttribute("AndjKtBoshqaPlan", AndjKtBoshqaPlan);

		//VCHD-6 kapital tamir uchun plan
		int SamKtHammaPlan = planDto.getSamKtKritiPlanBiznes() + planDto.getSamKtPlatformaPlanBiznes() + planDto.getSamKtPoluvagonPlanBiznes() + planDto.getSamKtSisternaPlanBiznes() + planDto.getSamKtBoshqaPlanBiznes();
		int SamKtKritiPlan = planDto.getSamKtKritiPlanBiznes();
		int SamKtPlatformaPlan = planDto.getSamKtPlatformaPlanBiznes();
		int SamKtPoluvagonPlan = planDto.getSamKtPoluvagonPlanBiznes();
		int SamKtSisternaPlan = planDto.getSamKtSisternaPlanBiznes();
		int SamKtBoshqaPlan = planDto.getSamKtBoshqaPlanBiznes();

		model.addAttribute("SamKtHammaPlan", SamKtHammaPlan);
		model.addAttribute("SamKtKritiPlan", SamKtKritiPlan);
		model.addAttribute("SamKtPlatformaPlan", SamKtPlatformaPlan);
		model.addAttribute("SamKtPoluvagonPlan", SamKtPoluvagonPlan);
		model.addAttribute("SamKtSisternaPlan", SamKtSisternaPlan);
		model.addAttribute("SamKtBoshqaPlan", SamKtBoshqaPlan);

		//kapital itogo
		int KtHammaPlan = AndjKtHammaPlan + HavKtHammaPlan + SamKtHammaPlan;
		int KtKritiPlan = planDto.getAndjKtKritiPlanBiznes() + planDto.getHavKtKritiPlanBiznes() + planDto.getSamKtKritiPlanBiznes();
		int KtPlatformaPlan = planDto.getAndjKtPlatformaPlanBiznes() + planDto.getHavKtPlatformaPlanBiznes() + planDto.getSamKtPlatformaPlanBiznes();
		int KtPoluvagonPlan = planDto.getAndjKtPoluvagonPlanBiznes() + planDto.getHavKtPoluvagonPlanBiznes() + planDto.getSamKtPoluvagonPlanBiznes();
		int KtSisternaPlan = planDto.getAndjKtSisternaPlanBiznes() + planDto.getHavKtSisternaPlanBiznes() + planDto.getSamKtSisternaPlanBiznes();
		int KtBoshqaPlan = planDto.getAndjKtBoshqaPlanBiznes() + planDto.getHavKtBoshqaPlanBiznes() + planDto.getSamKtBoshqaPlanBiznes();

		model.addAttribute("KtHammaPlan", KtHammaPlan);
		model.addAttribute("KtKritiPlan", KtKritiPlan);
		model.addAttribute("KtPlatformaPlan", KtPlatformaPlan);
		model.addAttribute("KtPoluvagonPlan", KtPoluvagonPlan);
		model.addAttribute("KtSisternaPlan", KtSisternaPlan);
		model.addAttribute("KtBoshqaPlan", KtBoshqaPlan);

		//VCHD-3 KRP plan
		int HavKrpHammaPlan =  planDto.getHavKrpKritiPlanBiznes() + planDto.getHavKrpPlatformaPlanBiznes() + planDto.getHavKrpPoluvagonPlanBiznes() + planDto.getHavKrpSisternaPlanBiznes() + planDto.getHavKrpBoshqaPlanBiznes();
		int HavKrpKritiPlan = planDto.getHavKrpKritiPlanBiznes();
		int HavKrpPlatformaPlan = planDto.getHavKrpPlatformaPlanBiznes();
		int HavKrpPoluvagonPlan = planDto.getHavKrpPoluvagonPlanBiznes();
		int HavKrpSisternaPlan = planDto.getHavKrpSisternaPlanBiznes();
		int HavKrpBoshqaPlan = planDto.getHavKrpBoshqaPlanBiznes();

		model.addAttribute("HavKrpHammaPlan", HavKrpHammaPlan);
		model.addAttribute("HavKrpKritiPlan", HavKrpKritiPlan);
		model.addAttribute("HavKrpPlatformaPlan", HavKrpPlatformaPlan);
		model.addAttribute("HavKrpPoluvagonPlan", HavKrpPoluvagonPlan);
		model.addAttribute("HavKrpSisternaPlan", HavKrpSisternaPlan);
		model.addAttribute("HavKrpBoshqaPlan", HavKrpBoshqaPlan);

		//VCHD-5 Krp plan
		int AndjKrpHammaPlan =  planDto.getAndjKrpKritiPlanBiznes() + planDto.getAndjKrpPlatformaPlanBiznes() + planDto.getAndjKrpPoluvagonPlanBiznes() + planDto.getAndjKrpSisternaPlanBiznes() + planDto.getAndjKrpBoshqaPlanBiznes();
		int AndjKrpKritiPlan = planDto.getAndjKrpKritiPlanBiznes();
		int AndjKrpPlatformaPlan = planDto.getAndjKrpPlatformaPlanBiznes();
		int AndjKrpPoluvagonPlan = planDto.getAndjKrpPoluvagonPlanBiznes();
		int AndjKrpSisternaPlan = planDto.getAndjKrpSisternaPlanBiznes();
		int AndjKrpBoshqaPlan = planDto.getAndjKrpBoshqaPlanBiznes();

		model.addAttribute("AndjKrpHammaPlan", AndjKrpHammaPlan);
		model.addAttribute("AndjKrpKritiPlan", AndjKrpKritiPlan);
		model.addAttribute("AndjKrpPlatformaPlan", AndjKrpPlatformaPlan);
		model.addAttribute("AndjKrpPoluvagonPlan", AndjKrpPoluvagonPlan);
		model.addAttribute("AndjKrpSisternaPlan", AndjKrpSisternaPlan);
		model.addAttribute("AndjKrpBoshqaPlan", AndjKrpBoshqaPlan);


		//samarqand KRP plan
		int SamKrpHammaPlan = planDto.getSamKrpKritiPlanBiznes() + planDto.getSamKrpPlatformaPlanBiznes() + planDto.getSamKrpPoluvagonPlanBiznes() + planDto.getSamKrpSisternaPlanBiznes() + planDto.getSamKrpBoshqaPlanBiznes();
		int SamKrpKritiPlan = planDto.getSamKrpKritiPlanBiznes();
		int SamKrpPlatformaPlan = planDto.getSamKrpPlatformaPlanBiznes();
		int SamKrpPoluvagonPlan = planDto.getSamKrpPoluvagonPlanBiznes();
		int SamKrpSisternaPlan = planDto.getSamKrpSisternaPlanBiznes();
		int SamKrpBoshqaPlan = planDto.getSamKrpBoshqaPlanBiznes();

		model.addAttribute("SamKrpHammaPlan", SamKrpHammaPlan);
		model.addAttribute("SamKrpKritiPlan", SamKrpKritiPlan);
		model.addAttribute("SamKrpPlatformaPlan", SamKrpPlatformaPlan);
		model.addAttribute("SamKrpPoluvagonPlan", SamKrpPoluvagonPlan);
		model.addAttribute("SamKrpSisternaPlan", SamKrpSisternaPlan);
		model.addAttribute("SamKrpBoshqaPlan", SamKrpBoshqaPlan);

		//Krp itogo plan
		int KrpHammaPlan = AndjKrpHammaPlan + HavKrpHammaPlan + SamKrpHammaPlan;
		int KrpKritiPlan = planDto.getAndjKrpKritiPlanBiznes() + planDto.getHavKrpKritiPlanBiznes() + planDto.getSamKrpKritiPlanBiznes();
		int KrpPlatformaPlan = planDto.getAndjKrpPlatformaPlanBiznes() + planDto.getHavKrpPlatformaPlanBiznes() + planDto.getSamKrpPlatformaPlanBiznes();
		int KrpPoluvagonPlan = planDto.getAndjKrpPoluvagonPlanBiznes() + planDto.getHavKrpPoluvagonPlanBiznes() + planDto.getSamKrpPoluvagonPlanBiznes();
		int KrpSisternaPlan = planDto.getAndjKrpSisternaPlanBiznes() + planDto.getHavKrpSisternaPlanBiznes() + planDto.getSamKrpSisternaPlanBiznes();
		int KrpBoshqaPlan = planDto.getAndjKrpBoshqaPlanBiznes() + planDto.getHavKrpBoshqaPlanBiznes() + planDto.getSamKrpBoshqaPlanBiznes();

		model.addAttribute("KrpHammaPlan", KrpHammaPlan);
		model.addAttribute("KrpKritiPlan", KrpKritiPlan);
		model.addAttribute("KrpPlatformaPlan", KrpPlatformaPlan);
		model.addAttribute("KrpPoluvagonPlan", KrpPoluvagonPlan);
		model.addAttribute("KrpSisternaPlan", KrpSisternaPlan);
		model.addAttribute("KrpBoshqaPlan", KrpBoshqaPlan);

	//Factlarni hisoblash uchun
		//VCHD-3 uchun depli tamir
		int hdHamma = 0;
		int hdKriti = 0;
		int hdPlatforma = 0;
		int hdPoluvagon = 0;
		int hdSisterna = 0;
		int hdBoshqa = 0;

		//VCHD-5 uchun depli tamir
		int adHamma = 0;
		int adKriti = 0;
		int adPlatforma = 0;
		int adPoluvagon = 0;
		int adSisterna = 0;
		int adBoshqa = 0;

		//samarqand uchun depli tamir
		int sdHamma = 0;
		int sdKriti = 0;
		int sdPlatforma = 0;
		int sdPoluvagon = 0;
		int sdSisterna = 0;
		int sdBoshqa = 0;


		// itogo Fact uchun depli tamir
		int uvtdHamma = 0;
		int uvtdKriti = 0;
		int uvtdPlatforma = 0;
		int uvtdPoluvagon = 0;
		int uvtdSisterna = 0;
		int uvtdBoshqa = 0;

		//Yolovchi vagon Fact
		int atYolovchi = 0;
		int adYolovchi = 0;

		//VCHD-3 uchun kapital tamir
		int hkHamma = 0;
		int hkKriti = 0;
		int hkPlatforma = 0;
		int hkPoluvagon = 0;
		int hkSisterna = 0;
		int hkBoshqa = 0;

		//VCHD-3 uchun kapital tamir
		int akHamma = 0;
		int akKriti = 0;
		int akPlatforma = 0;
		int akPoluvagon = 0;
		int akSisterna = 0;
		int akBoshqa = 0;

		//samarqand uchun Kapital tamir
		int skHamma = 0;
		int skKriti = 0;
		int skPlatforma = 0;
		int skPoluvagon = 0;
		int skSisterna = 0;
		int skBoshqa = 0;

		// itogo Fact uchun kapital tamir
		int uvtkhamma = 0;
		int uvtkKriti = 0;
		int uvtkPlatforma = 0;
		int uvtkPoluvagon = 0;
		int uvtkSisterna = 0;
		int uvtkBoshqa = 0;

		//VCHD-3 uchun kapital tamir
		int hkrHamma = 0;
		int hkrKriti = 0;
		int hkrPlatforma = 0;
		int hkrPoluvagon = 0;
		int hkrSisterna = 0;
		int hkrBoshqa = 0;

		//VCHD-3 uchun kapital tamir
		int akrHamma = 0;
		int akrKriti = 0;
		int akrPlatforma = 0;
		int akrPoluvagon = 0;
		int akrSisterna = 0;
		int akrBoshqa = 0;

		//samarqand uchun Kapital tamir
		int skrHamma = 0;
		int skrKriti = 0;
		int skrPlatforma = 0;
		int skrPoluvagon = 0;
		int skrSisterna = 0;
		int skrBoshqa = 0;


		// itogo Fact uchun KRP tamir
		int uvtkrhamma = 0;
		int uvtkrKriti = 0;
		int uvtkrPlatforma = 0;
		int uvtkrPoluvagon = 0;
		int uvtkrSisterna = 0;
		int uvtkrBoshqa = 0;
		
		// factlar

		if (country.equalsIgnoreCase("Hammasi")) {
			//VCHD-3 uchun depli tamir
			hdHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yopiq vagon (крыт)", "Depoli ta’mir(ДР)", oy + '-' + year) +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Platforma(пф)", "Depoli ta’mir(ДР)", oy + '-' + year) +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yarim ochiq vagon(пв)", "Depoli ta’mir(ДР)", oy + '-' + year) +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Sisterna(цс)", "Depoli ta’mir(ДР)", oy + '-' + year) +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Boshqa turdagi(проч)", "Depoli ta’mir(ДР)", oy + '-' + year);
			hdKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yopiq vagon (крыт)", "Depoli ta’mir(ДР)", oy + '-' + year);
			hdPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Platforma(пф)", "Depoli ta’mir(ДР)", oy + '-' + year);
			hdPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yarim ochiq vagon(пв)", "Depoli ta’mir(ДР)", oy + '-' + year);
			hdSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Sisterna(цс)", "Depoli ta’mir(ДР)", oy + '-' + year);
			hdBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Boshqa turdagi(проч)", "Depoli ta’mir(ДР)", oy + '-' + year);

			model.addAttribute("hdHamma", hdHamma);
			model.addAttribute("hdKriti", hdKriti);
			model.addAttribute("hdPlatforma", hdPlatforma);
			model.addAttribute("hdPoluvagon", hdPoluvagon);
			model.addAttribute("hdSisterna", hdSisterna);
			model.addAttribute("hdBoshqa", hdBoshqa);

			//VCHD-5 uchun depli tamir
			adHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yopiq vagon (крыт)", "Depoli ta’mir(ДР)", oy + '-' + year) +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Platforma(пф)", "Depoli ta’mir(ДР)", oy + '-' + year) +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yarim ochiq vagon(пв)", "Depoli ta’mir(ДР)", oy + '-' + year) +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Boshqa turdagi(проч)", "Depoli ta’mir(ДР)", oy + '-' + year) +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Sisterna(цс)", "Depoli ta’mir(ДР)", oy + '-' + year);
			adKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yopiq vagon (крыт)", "Depoli ta’mir(ДР)", oy + '-' + year);
			adPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Platforma(пф)", "Depoli ta’mir(ДР)", oy + '-' + year);
			adPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yarim ochiq vagon(пв)", "Depoli ta’mir(ДР)", oy + '-' + year);
			adSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Sisterna(цс)", "Depoli ta’mir(ДР)", oy + '-' + year);
			adBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Boshqa turdagi(проч)", "Depoli ta’mir(ДР)", oy + '-' + year);

			model.addAttribute("adHamma", adHamma);
			model.addAttribute("adKriti", adKriti);
			model.addAttribute("adPlatforma", adPlatforma);
			model.addAttribute("adPoluvagon", adPoluvagon);
			model.addAttribute("adSisterna", adSisterna);
			model.addAttribute("adBoshqa", adBoshqa);

			//samarqand uchun depli tamir
			sdHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yopiq vagon (крыт)", "Depoli ta’mir(ДР)", oy + '-' + year) +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Platforma(пф)", "Depoli ta’mir(ДР)", oy + '-' + year) +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yarim ochiq vagon(пв)", "Depoli ta’mir(ДР)", oy + '-' + year) +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Sisterna(цс)", "Depoli ta’mir(ДР)", oy + '-' + year) +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Boshqa turdagi(проч)", "Depoli ta’mir(ДР)", oy + '-' + year);
			sdKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yopiq vagon (крыт)", "Depoli ta’mir(ДР)", oy + '-' + year);
			sdPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Platforma(пф)", "Depoli ta’mir(ДР)", oy + '-' + year);
			sdPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yarim ochiq vagon(пв)", "Depoli ta’mir(ДР)", oy + '-' + year);
			sdSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Sisterna(цс)", "Depoli ta’mir(ДР)", oy + '-' + year);
			sdBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Boshqa turdagi(проч)", "Depoli ta’mir(ДР)", oy + '-' + year);

			model.addAttribute("sdHamma", sdHamma);
			model.addAttribute("sdKriti", sdKriti);
			model.addAttribute("sdPlatforma", sdPlatforma);
			model.addAttribute("sdPoluvagon", sdPoluvagon);
			model.addAttribute("sdSisterna", sdSisterna);
			model.addAttribute("sdBoshqa", sdBoshqa);

			// itogo Fact uchun depli tamir
			uvtdHamma = sdHamma + hdHamma + adHamma;
			uvtdKriti = sdKriti + hdKriti + adKriti;
			uvtdPlatforma = sdPlatforma + adPlatforma + hdPlatforma;
			uvtdPoluvagon = sdPoluvagon + hdPoluvagon + adPoluvagon;
			uvtdSisterna = adSisterna + hdSisterna + sdSisterna;
			uvtdBoshqa = adBoshqa + hdBoshqa + sdBoshqa;

			model.addAttribute("uvtdHamma", uvtdHamma);
			model.addAttribute("uvtdKriti", uvtdKriti);
			model.addAttribute("uvtdPlatforma", uvtdPlatforma);
			model.addAttribute("uvtdPoluvagon", uvtdPoluvagon);
			model.addAttribute("uvtdSisterna", uvtdSisterna);
			model.addAttribute("uvtdBoshqa", uvtdBoshqa);


			//Yolovchi vagon Fact
			atYolovchi = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yo'lovchi vagon(пассжир)", "TO-3", oy + '-' + year);
			adYolovchi = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yo'lovchi vagon(пассжир)", "Depoli ta’mir(ДР)", oy + '-' + year);

			model.addAttribute("atYolovchi", atYolovchi);
			model.addAttribute("adYolovchi", adYolovchi);

			//VCHD-3 uchun kapital tamir
			hkHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yopiq vagon (крыт)", "Kapital ta’mir(КР)", oy + '-' + year) +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Platforma(пф)", "Kapital ta’mir(КР)", oy + '-' + year) +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yarim ochiq vagon(пв)", "Kapital ta’mir(КР)", oy + '-' + year) +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Sisterna(цс)", "Kapital ta’mir(КР)", oy + '-' + year) +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Boshqa turdagi(проч)", "Kapital ta’mir(КР)", oy + '-' + year);
			hkKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yopiq vagon (крыт)", "Kapital ta’mir(КР)", oy + '-' + year);
			hkPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Platforma(пф)", "Kapital ta’mir(КР)", oy + '-' + year);
			hkPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yarim ochiq vagon(пв)", "Kapital ta’mir(КР)", oy + '-' + year);
			hkSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Sisterna(цс)", "Kapital ta’mir(КР)", oy + '-' + year);
			hkBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Boshqa turdagi(проч)", "Kapital ta’mir(КР)", oy + '-' + year);

			model.addAttribute("hkHamma", hkHamma);
			model.addAttribute("hkKriti", hkKriti);
			model.addAttribute("hkPlatforma", hkPlatforma);
			model.addAttribute("hkPoluvagon", hkPoluvagon);
			model.addAttribute("hkSisterna", hkSisterna);
			model.addAttribute("hkBoshqa", hkBoshqa);

			//VCHD-3 uchun kapital tamir
			akHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yopiq vagon (крыт)", "Kapital ta’mir(КР)", oy + '-' + year) +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Platforma(пф)", "Kapital ta’mir(КР)", oy + '-' + year) +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yarim ochiq vagon(пв)", "Kapital ta’mir(КР)", oy + '-' + year) +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Boshqa turdagi(проч)", "Kapital ta’mir(КР)", oy + '-' + year) +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Sisterna(цс)", "Kapital ta’mir(КР)", oy + '-' + year);
			akKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yopiq vagon (крыт)", "Kapital ta’mir(КР)", oy + '-' + year);
			akPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Platforma(пф)", "Kapital ta’mir(КР)", oy + '-' + year);
			akPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yarim ochiq vagon(пв)", "Kapital ta’mir(КР)", oy + '-' + year);
			akSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Sisterna(цс)", "Kapital ta’mir(КР)", oy + '-' + year);
			akBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Boshqa turdagi(проч)", "Kapital ta’mir(КР)", oy + '-' + year);

			model.addAttribute("akHamma", akHamma);
			model.addAttribute("akKriti", akKriti);
			model.addAttribute("akPlatforma", akPlatforma);
			model.addAttribute("akPoluvagon", akPoluvagon);
			model.addAttribute("akSisterna", akSisterna);
			model.addAttribute("akBoshqa", akBoshqa);

			//samarqand uchun Kapital tamir
			skHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yopiq vagon (крыт)", "Kapital ta’mir(КР)", oy + '-' + year) +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Platforma(пф)", "Kapital ta’mir(КР)", oy + '-' + year) +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yarim ochiq vagon(пв)", "Kapital ta’mir(КР)", oy + '-' + year) +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Sisterna(цс)", "Kapital ta’mir(КР)", oy + '-' + year) +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Boshqa turdagi(проч)", "Kapital ta’mir(КР)", oy + '-' + year);
			skKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yopiq vagon (крыт)", "Kapital ta’mir(КР)", oy + '-' + year);
			skPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Platforma(пф)", "Kapital ta’mir(КР)", oy + '-' + year);
			skPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yarim ochiq vagon(пв)", "Kapital ta’mir(КР)", oy + '-' + year);
			skSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Sisterna(цс)", "Kapital ta’mir(КР)", oy + '-' + year);
			skBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Boshqa turdagi(проч)", "Kapital ta’mir(КР)", oy + '-' + year);

			model.addAttribute("skHamma", skHamma);
			model.addAttribute("skKriti", skKriti);
			model.addAttribute("skPlatforma", skPlatforma);
			model.addAttribute("skPoluvagon", skPoluvagon);
			model.addAttribute("skSisterna", skSisterna);
			model.addAttribute("skBoshqa", skBoshqa);

			// itogo Fact uchun kapital tamir
			uvtkhamma = skHamma + hkHamma + akHamma;
			uvtkKriti = skKriti + hkKriti + akKriti;
			uvtkPlatforma = skPlatforma + akPlatforma + hkPlatforma;
			uvtkPoluvagon = skPoluvagon + hkPoluvagon + akPoluvagon;
			uvtkSisterna = akSisterna + hkSisterna + skSisterna;
			uvtkBoshqa = akBoshqa + hkBoshqa + skBoshqa;

			model.addAttribute("uvtkhamma", uvtkhamma);
			model.addAttribute("uvtkKriti", uvtkKriti);
			model.addAttribute("uvtkPlatforma", uvtkPlatforma);
			model.addAttribute("uvtkPoluvagon", uvtkPoluvagon);
			model.addAttribute("uvtkSisterna", uvtkSisterna);
			model.addAttribute("uvtkBoshqa", uvtkBoshqa);


			//VCHD-3 uchun kapital tamir
			hkrHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yopiq vagon (крыт)", "KRP(КРП)", oy + '-' + year) +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Platforma(пф)", "KRP(КРП)", oy + '-' + year) +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yarim ochiq vagon(пв)", "KRP(КРП)", oy + '-' + year) +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Sisterna(цс)", "KRP(КРП)", oy + '-' + year) +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Boshqa turdagi(проч)", "KRP(КРП)", oy + '-' + year);
			hkrKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yopiq vagon (крыт)", "KRP(КРП)", oy + '-' + year);
			hkrPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Platforma(пф)", "KRP(КРП)", oy + '-' + year);
			hkrPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yarim ochiq vagon(пв)", "KRP(КРП)", oy + '-' + year);
			hkrSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Sisterna(цс)", "KRP(КРП)", oy + '-' + year);
			hkrBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Boshqa turdagi(проч)", "KRP(КРП)", oy + '-' + year);

			model.addAttribute("hkrHamma", hkrHamma);
			model.addAttribute("hkrKriti", hkrKriti);
			model.addAttribute("hkrPlatforma", hkrPlatforma);
			model.addAttribute("hkrPoluvagon", hkrPoluvagon);
			model.addAttribute("hkrSisterna", hkrSisterna);
			model.addAttribute("hkrBoshqa", hkrBoshqa);

			//VCHD-3 uchun kapital tamir
			akrHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yopiq vagon (крыт)", "KRP(КРП)", oy + '-' + year) +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Platforma(пф)", "KRP(КРП)", oy + '-' + year) +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yarim ochiq vagon(пв)", "KRP(КРП)", oy + '-' + year) +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Boshqa turdagi(проч)", "KRP(КРП)", oy + '-' + year) +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Sisterna(цс)", "KRP(КРП)", oy + '-' + year);
			akrKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yopiq vagon (крыт)", "KRP(КРП)", oy + '-' + year);
			akrPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Platforma(пф)", "KRP(КРП)", oy + '-' + year);
			akrPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yarim ochiq vagon(пв)", "KRP(КРП)", oy + '-' + year);
			akrSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Sisterna(цс)", "KRP(КРП)", oy + '-' + year);
			akrBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Boshqa turdagi(проч)", "KRP(КРП)", oy + '-' + year);

			model.addAttribute("akrHamma", akrHamma);
			model.addAttribute("akrKriti", akrKriti);
			model.addAttribute("akrPlatforma", akrPlatforma);
			model.addAttribute("akrPoluvagon", akrPoluvagon);
			model.addAttribute("akrSisterna", akrSisterna);
			model.addAttribute("akrBoshqa", akrBoshqa);

			//samarqand uchun Kapital tamir
			skrHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yopiq vagon (крыт)", "KRP(КРП)", oy + '-' + year) +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Platforma(пф)", "KRP(КРП)", oy + '-' + year) +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yarim ochiq vagon(пв)", "KRP(КРП)", oy + '-' + year) +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Sisterna(цс)", "KRP(КРП)", oy + '-' + year) +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Boshqa turdagi(проч)", "KRP(КРП)", oy + '-' + year);
			skrKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yopiq vagon (крыт)", "KRP(КРП)", oy + '-' + year);
			skrPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Platforma(пф)", "KRP(КРП)", oy + '-' + year);
			skrPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yarim ochiq vagon(пв)", "KRP(КРП)", oy + '-' + year);
			skrSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Sisterna(цс)", "KRP(КРП)", oy + '-' + year);
			skrBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Boshqa turdagi(проч)", "KRP(КРП)", oy + '-' + year);

			model.addAttribute("skrHamma", skrHamma);
			model.addAttribute("skrKriti", skrKriti);
			model.addAttribute("skrPlatforma", skrPlatforma);
			model.addAttribute("skrPoluvagon", skrPoluvagon);
			model.addAttribute("skrSisterna", skrSisterna);
			model.addAttribute("skrBoshqa", skrBoshqa);

			// itogo Fact uchun KRP tamir
			uvtkrhamma = skrHamma + hkrHamma + akrHamma;
			uvtkrKriti = skrKriti + hkrKriti + akrKriti;
			uvtkrPlatforma = skrPlatforma + akrPlatforma + hkrPlatforma;
			uvtkrPoluvagon = skrPoluvagon + hkrPoluvagon + akrPoluvagon;
			uvtkrSisterna = akrSisterna + hkrSisterna + skrSisterna;
			uvtkrBoshqa = akrBoshqa + hkrBoshqa + skrBoshqa;

			model.addAttribute("uvtkrhamma", uvtkrhamma);
			model.addAttribute("uvtkrKriti", uvtkrKriti);
			model.addAttribute("uvtkrPlatforma", uvtkrPlatforma);
			model.addAttribute("uvtkrPoluvagon", uvtkrPoluvagon);
			model.addAttribute("uvtkrSisterna", uvtkrSisterna);
			model.addAttribute("uvtkrBoshqa", uvtkrBoshqa);

		}else if (country.equalsIgnoreCase("O'TY(ГАЖК)")) {

			//VCHD-3 uchun depli tamir
			hdHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yopiq vagon (крыт)", "Depoli ta’mir(ДР)", oy + '-' + year, "O'TY(ГАЖК)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Platforma(пф)", "Depoli ta’mir(ДР)", oy + '-' + year, "O'TY(ГАЖК)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yarim ochiq vagon(пв)", "Depoli ta’mir(ДР)", oy + '-' + year, "O'TY(ГАЖК)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Sisterna(цс)", "Depoli ta’mir(ДР)", oy + '-' + year, "O'TY(ГАЖК)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Boshqa turdagi(проч)", "Depoli ta’mir(ДР)", oy + '-' + year, "O'TY(ГАЖК)");
			hdKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yopiq vagon (крыт)", "Depoli ta’mir(ДР)", oy + '-' + year, "O'TY(ГАЖК)");
			hdPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Platforma(пф)", "Depoli ta’mir(ДР)", oy + '-' + year, "O'TY(ГАЖК)");
			hdPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yarim ochiq vagon(пв)", "Depoli ta’mir(ДР)", oy + '-' + year, "O'TY(ГАЖК)");
			hdSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Sisterna(цс)", "Depoli ta’mir(ДР)", oy + '-' + year, "O'TY(ГАЖК)");
			hdBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Boshqa turdagi(проч)", "Depoli ta’mir(ДР)", oy + '-' + year, "O'TY(ГАЖК)");

			model.addAttribute("hdHamma", hdHamma);
			model.addAttribute("hdKriti", hdKriti);
			model.addAttribute("hdPlatforma", hdPlatforma);
			model.addAttribute("hdPoluvagon", hdPoluvagon);
			model.addAttribute("hdSisterna", hdSisterna);
			model.addAttribute("hdBoshqa", hdBoshqa);

			//VCHD-5 uchun depli tamir
			adHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yopiq vagon (крыт)", "Depoli ta’mir(ДР)", oy + '-' + year, "O'TY(ГАЖК)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Platforma(пф)", "Depoli ta’mir(ДР)", oy + '-' + year, "O'TY(ГАЖК)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yarim ochiq vagon(пв)", "Depoli ta’mir(ДР)", oy + '-' + year, "O'TY(ГАЖК)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Boshqa turdagi(проч)", "Depoli ta’mir(ДР)", oy + '-' + year, "O'TY(ГАЖК)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Sisterna(цс)", "Depoli ta’mir(ДР)", oy + '-' + year, "O'TY(ГАЖК)");
			adKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yopiq vagon (крыт)", "Depoli ta’mir(ДР)", oy + '-' + year, "O'TY(ГАЖК)");
			adPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Platforma(пф)", "Depoli ta’mir(ДР)", oy + '-' + year, "O'TY(ГАЖК)");
			adPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yarim ochiq vagon(пв)", "Depoli ta’mir(ДР)", oy + '-' + year, "O'TY(ГАЖК)");
			adSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Sisterna(цс)", "Depoli ta’mir(ДР)", oy + '-' + year, "O'TY(ГАЖК)");
			adBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Boshqa turdagi(проч)", "Depoli ta’mir(ДР)", oy + '-' + year, "O'TY(ГАЖК)");

			model.addAttribute("adHamma", adHamma);
			model.addAttribute("adKriti", adKriti);
			model.addAttribute("adPlatforma", adPlatforma);
			model.addAttribute("adPoluvagon", adPoluvagon);
			model.addAttribute("adSisterna", adSisterna);
			model.addAttribute("adBoshqa", adBoshqa);

			//samarqand uchun depli tamir
			sdHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yopiq vagon (крыт)", "Depoli ta’mir(ДР)", oy + '-' + year, "O'TY(ГАЖК)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Platforma(пф)", "Depoli ta’mir(ДР)", oy + '-' + year, "O'TY(ГАЖК)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yarim ochiq vagon(пв)", "Depoli ta’mir(ДР)", oy + '-' + year, "O'TY(ГАЖК)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Sisterna(цс)", "Depoli ta’mir(ДР)", oy + '-' + year, "O'TY(ГАЖК)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Boshqa turdagi(проч)", "Depoli ta’mir(ДР)", oy + '-' + year, "O'TY(ГАЖК)");
			sdKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yopiq vagon (крыт)", "Depoli ta’mir(ДР)", oy + '-' + year, "O'TY(ГАЖК)");
			sdPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Platforma(пф)", "Depoli ta’mir(ДР)", oy + '-' + year, "O'TY(ГАЖК)");
			sdPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yarim ochiq vagon(пв)", "Depoli ta’mir(ДР)", oy + '-' + year, "O'TY(ГАЖК)");
			sdSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Sisterna(цс)", "Depoli ta’mir(ДР)", oy + '-' + year, "O'TY(ГАЖК)");
			sdBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Boshqa turdagi(проч)", "Depoli ta’mir(ДР)", oy + '-' + year, "O'TY(ГАЖК)");

			model.addAttribute("sdHamma", sdHamma);
			model.addAttribute("sdKriti", sdKriti);
			model.addAttribute("sdPlatforma", sdPlatforma);
			model.addAttribute("sdPoluvagon", sdPoluvagon);
			model.addAttribute("sdSisterna", sdSisterna);
			model.addAttribute("sdBoshqa", sdBoshqa);

			// itogo Fact uchun depli tamir
			uvtdHamma = sdHamma + hdHamma + adHamma;
			uvtdKriti = sdKriti + hdKriti + adKriti;
			uvtdPlatforma = sdPlatforma + adPlatforma + hdPlatforma;
			uvtdPoluvagon = sdPoluvagon + hdPoluvagon + adPoluvagon;
			uvtdSisterna = adSisterna + hdSisterna + sdSisterna;
			uvtdBoshqa = adBoshqa + hdBoshqa + sdBoshqa;

			model.addAttribute("uvtdHamma", uvtdHamma);
			model.addAttribute("uvtdKriti", uvtdKriti);
			model.addAttribute("uvtdPlatforma", uvtdPlatforma);
			model.addAttribute("uvtdPoluvagon", uvtdPoluvagon);
			model.addAttribute("uvtdSisterna", uvtdSisterna);
			model.addAttribute("uvtdBoshqa", uvtdBoshqa);


			//Yolovchi vagon Fact
			atYolovchi = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yo'lovchi vagon(пассжир)", "TO-3", oy + '-' + year, "O'TY(ГАЖК)");
			adYolovchi = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yo'lovchi vagon(пассжир)", "Depoli ta’mir(ДР)", oy + '-' + year, "O'TY(ГАЖК)");

			model.addAttribute("atYolovchi", atYolovchi);
			model.addAttribute("adYolovchi", adYolovchi);

			//VCHD-3 uchun kapital tamir
			hkHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yopiq vagon (крыт)", "Kapital ta’mir(КР)", oy + '-' + year, "O'TY(ГАЖК)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Platforma(пф)", "Kapital ta’mir(КР)", oy + '-' + year, "O'TY(ГАЖК)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yarim ochiq vagon(пв)", "Kapital ta’mir(КР)", oy + '-' + year, "O'TY(ГАЖК)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Sisterna(цс)", "Kapital ta’mir(КР)", oy + '-' + year, "O'TY(ГАЖК)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Boshqa turdagi(проч)", "Kapital ta’mir(КР)", oy + '-' + year, "O'TY(ГАЖК)");
			hkKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yopiq vagon (крыт)", "Kapital ta’mir(КР)", oy + '-' + year, "O'TY(ГАЖК)");
			hkPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Platforma(пф)", "Kapital ta’mir(КР)", oy + '-' + year, "O'TY(ГАЖК)");
			hkPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yarim ochiq vagon(пв)", "Kapital ta’mir(КР)", oy + '-' + year, "O'TY(ГАЖК)");
			hkSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Sisterna(цс)", "Kapital ta’mir(КР)", oy + '-' + year, "O'TY(ГАЖК)");
			hkBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Boshqa turdagi(проч)", "Kapital ta’mir(КР)", oy + '-' + year, "O'TY(ГАЖК)");

			model.addAttribute("hkHamma", hkHamma);
			model.addAttribute("hkKriti", hkKriti);
			model.addAttribute("hkPlatforma", hkPlatforma);
			model.addAttribute("hkPoluvagon", hkPoluvagon);
			model.addAttribute("hkSisterna", hkSisterna);
			model.addAttribute("hkBoshqa", hkBoshqa);

			//VCHD-3 uchun kapital tamir
			akHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yopiq vagon (крыт)", "Kapital ta’mir(КР)", oy + '-' + year, "O'TY(ГАЖК)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Platforma(пф)", "Kapital ta’mir(КР)", oy + '-' + year, "O'TY(ГАЖК)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yarim ochiq vagon(пв)", "Kapital ta’mir(КР)", oy + '-' + year, "O'TY(ГАЖК)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Boshqa turdagi(проч)", "Kapital ta’mir(КР)", oy + '-' + year, "O'TY(ГАЖК)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Sisterna(цс)", "Kapital ta’mir(КР)", oy + '-' + year, "O'TY(ГАЖК)");
			akKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yopiq vagon (крыт)", "Kapital ta’mir(КР)", oy + '-' + year, "O'TY(ГАЖК)");
			akPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Platforma(пф)", "Kapital ta’mir(КР)", oy + '-' + year, "O'TY(ГАЖК)");
			akPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yarim ochiq vagon(пв)", "Kapital ta’mir(КР)", oy + '-' + year, "O'TY(ГАЖК)");
			akSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Sisterna(цс)", "Kapital ta’mir(КР)", oy + '-' + year, "O'TY(ГАЖК)");
			akBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Boshqa turdagi(проч)", "Kapital ta’mir(КР)", oy + '-' + year, "O'TY(ГАЖК)");

			model.addAttribute("akHamma", akHamma);
			model.addAttribute("akKriti", akKriti);
			model.addAttribute("akPlatforma", akPlatforma);
			model.addAttribute("akPoluvagon", akPoluvagon);
			model.addAttribute("akSisterna", akSisterna);
			model.addAttribute("akBoshqa", akBoshqa);

			//samarqand uchun Kapital tamir
			skHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yopiq vagon (крыт)", "Kapital ta’mir(КР)", oy + '-' + year, "O'TY(ГАЖК)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Platforma(пф)", "Kapital ta’mir(КР)", oy + '-' + year, "O'TY(ГАЖК)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yarim ochiq vagon(пв)", "Kapital ta’mir(КР)", oy + '-' + year, "O'TY(ГАЖК)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Sisterna(цс)", "Kapital ta’mir(КР)", oy + '-' + year, "O'TY(ГАЖК)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Boshqa turdagi(проч)", "Kapital ta’mir(КР)", oy + '-' + year, "O'TY(ГАЖК)");
			skKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yopiq vagon (крыт)", "Kapital ta’mir(КР)", oy + '-' + year, "O'TY(ГАЖК)");
			skPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Platforma(пф)", "Kapital ta’mir(КР)", oy + '-' + year, "O'TY(ГАЖК)");
			skPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yarim ochiq vagon(пв)", "Kapital ta’mir(КР)", oy + '-' + year, "O'TY(ГАЖК)");
			skSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Sisterna(цс)", "Kapital ta’mir(КР)", oy + '-' + year, "O'TY(ГАЖК)");
			skBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Boshqa turdagi(проч)", "Kapital ta’mir(КР)", oy + '-' + year, "O'TY(ГАЖК)");

			model.addAttribute("skHamma", skHamma);
			model.addAttribute("skKriti", skKriti);
			model.addAttribute("skPlatforma", skPlatforma);
			model.addAttribute("skPoluvagon", skPoluvagon);
			model.addAttribute("skSisterna", skSisterna);
			model.addAttribute("skBoshqa", skBoshqa);

			// itogo Fact uchun kapital tamir
			uvtkhamma = skHamma + hkHamma + akHamma;
			uvtkKriti = skKriti + hkKriti + akKriti;
			uvtkPlatforma = skPlatforma + akPlatforma + hkPlatforma;
			uvtkPoluvagon = skPoluvagon + hkPoluvagon + akPoluvagon;
			uvtkSisterna = akSisterna + hkSisterna + skSisterna;
			uvtkBoshqa = akBoshqa + hkBoshqa + skBoshqa;

			model.addAttribute("uvtkhamma", uvtkhamma);
			model.addAttribute("uvtkKriti", uvtkKriti);
			model.addAttribute("uvtkPlatforma", uvtkPlatforma);
			model.addAttribute("uvtkPoluvagon", uvtkPoluvagon);
			model.addAttribute("uvtkSisterna", uvtkSisterna);
			model.addAttribute("uvtkBoshqa", uvtkBoshqa);

			//VCHD-3 uchun kapital tamir
			hkrHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yopiq vagon (крыт)", "KRP(КРП)", oy + '-' + year, "O'TY(ГАЖК)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Platforma(пф)", "KRP(КРП)", oy + '-' + year, "O'TY(ГАЖК)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yarim ochiq vagon(пв)", "KRP(КРП)", oy + '-' + year, "O'TY(ГАЖК)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Sisterna(цс)", "KRP(КРП)", oy + '-' + year, "O'TY(ГАЖК)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Boshqa turdagi(проч)", "KRP(КРП)", oy + '-' + year, "O'TY(ГАЖК)");
			hkrKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yopiq vagon (крыт)", "KRP(КРП)", oy + '-' + year, "O'TY(ГАЖК)");
			hkrPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Platforma(пф)", "KRP(КРП)", oy + '-' + year, "O'TY(ГАЖК)");
			hkrPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yarim ochiq vagon(пв)", "KRP(КРП)", oy + '-' + year, "O'TY(ГАЖК)");
			hkrSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Sisterna(цс)", "KRP(КРП)", oy + '-' + year, "O'TY(ГАЖК)");
			hkrBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Boshqa turdagi(проч)", "KRP(КРП)", oy + '-' + year, "O'TY(ГАЖК)");

			model.addAttribute("hkrHamma", hkrHamma);
			model.addAttribute("hkrKriti", hkrKriti);
			model.addAttribute("hkrPlatforma", hkrPlatforma);
			model.addAttribute("hkrPoluvagon", hkrPoluvagon);
			model.addAttribute("hkrSisterna", hkrSisterna);
			model.addAttribute("hkrBoshqa", hkrBoshqa);

			//VCHD-3 uchun kapital tamir
			akrHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yopiq vagon (крыт)", "KRP(КРП)", oy + '-' + year, "O'TY(ГАЖК)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Platforma(пф)", "KRP(КРП)", oy + '-' + year, "O'TY(ГАЖК)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yarim ochiq vagon(пв)", "KRP(КРП)", oy + '-' + year, "O'TY(ГАЖК)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Boshqa turdagi(проч)", "KRP(КРП)", oy + '-' + year, "O'TY(ГАЖК)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Sisterna(цс)", "KRP(КРП)", oy + '-' + year, "O'TY(ГАЖК)");
			akrKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yopiq vagon (крыт)", "KRP(КРП)", oy + '-' + year, "O'TY(ГАЖК)");
			akrPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Platforma(пф)", "KRP(КРП)", oy + '-' + year, "O'TY(ГАЖК)");
			akrPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yarim ochiq vagon(пв)", "KRP(КРП)", oy + '-' + year, "O'TY(ГАЖК)");
			akrSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Sisterna(цс)", "KRP(КРП)", oy + '-' + year, "O'TY(ГАЖК)");
			akrBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Boshqa turdagi(проч)", "KRP(КРП)", oy + '-' + year, "O'TY(ГАЖК)");

			model.addAttribute("akrHamma", akrHamma);
			model.addAttribute("akrKriti", akrKriti);
			model.addAttribute("akrPlatforma", akrPlatforma);
			model.addAttribute("akrPoluvagon", akrPoluvagon);
			model.addAttribute("akrSisterna", akrSisterna);
			model.addAttribute("akrBoshqa", akrBoshqa);

			//samarqand uchun Kapital tamir
			skrHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yopiq vagon (крыт)", "KRP(КРП)", oy + '-' + year, "O'TY(ГАЖК)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Platforma(пф)", "KRP(КРП)", oy + '-' + year, "O'TY(ГАЖК)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yarim ochiq vagon(пв)", "KRP(КРП)", oy + '-' + year, "O'TY(ГАЖК)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Sisterna(цс)", "KRP(КРП)", oy + '-' + year, "O'TY(ГАЖК)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Boshqa turdagi(проч)", "KRP(КРП)", oy + '-' + year, "O'TY(ГАЖК)");
			skrKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yopiq vagon (крыт)", "KRP(КРП)", oy + '-' + year, "O'TY(ГАЖК)");
			skrPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Platforma(пф)", "KRP(КРП)", oy + '-' + year, "O'TY(ГАЖК)");
			skrPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yarim ochiq vagon(пв)", "KRP(КРП)", oy + '-' + year, "O'TY(ГАЖК)");
			skrSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Sisterna(цс)", "KRP(КРП)", oy + '-' + year, "O'TY(ГАЖК)");
			skrBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Boshqa turdagi(проч)", "KRP(КРП)", oy + '-' + year, "O'TY(ГАЖК)");

			model.addAttribute("skrHamma", skrHamma);
			model.addAttribute("skrKriti", skrKriti);
			model.addAttribute("skrPlatforma", skrPlatforma);
			model.addAttribute("skrPoluvagon", skrPoluvagon);
			model.addAttribute("skrSisterna", skrSisterna);
			model.addAttribute("skrBoshqa", skrBoshqa);

			// itogo Fact uchun KRP tamir
			uvtkrhamma = skrHamma + hkrHamma + akrHamma;
			uvtkrKriti = skrKriti + hkrKriti + akrKriti;
			uvtkrPlatforma = skrPlatforma + akrPlatforma + hkrPlatforma;
			uvtkrPoluvagon = skrPoluvagon + hkrPoluvagon + akrPoluvagon;
			uvtkrSisterna = akrSisterna + hkrSisterna + skrSisterna;
			uvtkrBoshqa = akrBoshqa + hkrBoshqa + skrBoshqa;

			model.addAttribute("uvtkrhamma", uvtkrhamma);
			model.addAttribute("uvtkrKriti", uvtkrKriti);
			model.addAttribute("uvtkrPlatforma", uvtkrPlatforma);
			model.addAttribute("uvtkrPoluvagon", uvtkrPoluvagon);
			model.addAttribute("uvtkrSisterna", uvtkrSisterna);
			model.addAttribute("uvtkrBoshqa", uvtkrBoshqa);

		}else if (country.equalsIgnoreCase("MDH(СНГ)")) {

			//VCHD-3 uchun depli tamir
			hdHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yopiq vagon (крыт)", "Depoli ta’mir(ДР)", oy + '-' + year, "MDH(СНГ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Platforma(пф)", "Depoli ta’mir(ДР)", oy + '-' + year, "MDH(СНГ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yarim ochiq vagon(пв)", "Depoli ta’mir(ДР)", oy + '-' + year, "MDH(СНГ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Sisterna(цс)", "Depoli ta’mir(ДР)", oy + '-' + year, "MDH(СНГ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Boshqa turdagi(проч)", "Depoli ta’mir(ДР)", oy + '-' + year, "MDH(СНГ)");
			hdKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yopiq vagon (крыт)", "Depoli ta’mir(ДР)", oy + '-' + year, "MDH(СНГ)");
			hdPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Platforma(пф)", "Depoli ta’mir(ДР)", oy + '-' + year, "MDH(СНГ)");
			hdPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yarim ochiq vagon(пв)", "Depoli ta’mir(ДР)", oy + '-' + year, "MDH(СНГ)");
			hdSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Sisterna(цс)", "Depoli ta’mir(ДР)", oy + '-' + year, "MDH(СНГ)");
			hdBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Boshqa turdagi(проч)", "Depoli ta’mir(ДР)", oy + '-' + year, "MDH(СНГ)");

			model.addAttribute("hdHamma", hdHamma);
			model.addAttribute("hdKriti", hdKriti);
			model.addAttribute("hdPlatforma", hdPlatforma);
			model.addAttribute("hdPoluvagon", hdPoluvagon);
			model.addAttribute("hdSisterna", hdSisterna);
			model.addAttribute("hdBoshqa", hdBoshqa);

			//VCHD-5 uchun depli tamir
			adHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yopiq vagon (крыт)", "Depoli ta’mir(ДР)", oy + '-' + year, "MDH(СНГ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Platforma(пф)", "Depoli ta’mir(ДР)", oy + '-' + year, "MDH(СНГ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yarim ochiq vagon(пв)", "Depoli ta’mir(ДР)", oy + '-' + year, "MDH(СНГ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Boshqa turdagi(проч)", "Depoli ta’mir(ДР)", oy + '-' + year, "MDH(СНГ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Sisterna(цс)", "Depoli ta’mir(ДР)", oy + '-' + year, "MDH(СНГ)");
			adKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yopiq vagon (крыт)", "Depoli ta’mir(ДР)", oy + '-' + year, "MDH(СНГ)");
			adPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Platforma(пф)", "Depoli ta’mir(ДР)", oy + '-' + year, "MDH(СНГ)");
			adPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yarim ochiq vagon(пв)", "Depoli ta’mir(ДР)", oy + '-' + year, "MDH(СНГ)");
			adSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Sisterna(цс)", "Depoli ta’mir(ДР)", oy + '-' + year, "MDH(СНГ)");
			adBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Boshqa turdagi(проч)", "Depoli ta’mir(ДР)", oy + '-' + year, "MDH(СНГ)");

			model.addAttribute("adHamma", adHamma);
			model.addAttribute("adKriti", adKriti);
			model.addAttribute("adPlatforma", adPlatforma);
			model.addAttribute("adPoluvagon", adPoluvagon);
			model.addAttribute("adSisterna", adSisterna);
			model.addAttribute("adBoshqa", adBoshqa);

			//samarqand uchun depli tamir
			sdHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yopiq vagon (крыт)", "Depoli ta’mir(ДР)", oy + '-' + year, "MDH(СНГ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Platforma(пф)", "Depoli ta’mir(ДР)", oy + '-' + year, "MDH(СНГ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yarim ochiq vagon(пв)", "Depoli ta’mir(ДР)", oy + '-' + year, "MDH(СНГ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Sisterna(цс)", "Depoli ta’mir(ДР)", oy + '-' + year, "MDH(СНГ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Boshqa turdagi(проч)", "Depoli ta’mir(ДР)", oy + '-' + year, "MDH(СНГ)");
			sdKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yopiq vagon (крыт)", "Depoli ta’mir(ДР)", oy + '-' + year, "MDH(СНГ)");
			sdPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Platforma(пф)", "Depoli ta’mir(ДР)", oy + '-' + year, "MDH(СНГ)");
			sdPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yarim ochiq vagon(пв)", "Depoli ta’mir(ДР)", oy + '-' + year, "MDH(СНГ)");
			sdSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Sisterna(цс)", "Depoli ta’mir(ДР)", oy + '-' + year, "MDH(СНГ)");
			sdBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Boshqa turdagi(проч)", "Depoli ta’mir(ДР)", oy + '-' + year, "MDH(СНГ)");

			model.addAttribute("sdHamma", sdHamma);
			model.addAttribute("sdKriti", sdKriti);
			model.addAttribute("sdPlatforma", sdPlatforma);
			model.addAttribute("sdPoluvagon", sdPoluvagon);
			model.addAttribute("sdSisterna", sdSisterna);
			model.addAttribute("sdBoshqa", sdBoshqa);

			// itogo Fact uchun depli tamir
			uvtdHamma = sdHamma + hdHamma + adHamma;
			uvtdKriti = sdKriti + hdKriti + adKriti;
			uvtdPlatforma = sdPlatforma + adPlatforma + hdPlatforma;
			uvtdPoluvagon = sdPoluvagon + hdPoluvagon + adPoluvagon;
			uvtdSisterna = adSisterna + hdSisterna + sdSisterna;
			uvtdBoshqa = adBoshqa + hdBoshqa + sdBoshqa;

			model.addAttribute("uvtdHamma", uvtdHamma);
			model.addAttribute("uvtdKriti", uvtdKriti);
			model.addAttribute("uvtdPlatforma", uvtdPlatforma);
			model.addAttribute("uvtdPoluvagon", uvtdPoluvagon);
			model.addAttribute("uvtdSisterna", uvtdSisterna);
			model.addAttribute("uvtdBoshqa", uvtdBoshqa);


			//Yolovchi vagon Fact
			atYolovchi = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yo'lovchi vagon(пассжир)", "TO-3", oy + '-' + year, "MDH(СНГ)");
			adYolovchi = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yo'lovchi vagon(пассжир)", "Depoli ta’mir(ДР)", oy + '-' + year, "MDH(СНГ)");

			model.addAttribute("atYolovchi", atYolovchi);
			model.addAttribute("adYolovchi", adYolovchi);

			//VCHD-3 uchun kapital tamir
			hkHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yopiq vagon (крыт)", "Kapital ta’mir(КР)", oy + '-' + year, "MDH(СНГ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Platforma(пф)", "Kapital ta’mir(КР)", oy + '-' + year, "MDH(СНГ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yarim ochiq vagon(пв)", "Kapital ta’mir(КР)", oy + '-' + year, "MDH(СНГ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Sisterna(цс)", "Kapital ta’mir(КР)", oy + '-' + year, "MDH(СНГ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Boshqa turdagi(проч)", "Kapital ta’mir(КР)", oy + '-' + year, "MDH(СНГ)");
			hkKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yopiq vagon (крыт)", "Kapital ta’mir(КР)", oy + '-' + year, "MDH(СНГ)");
			hkPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Platforma(пф)", "Kapital ta’mir(КР)", oy + '-' + year, "MDH(СНГ)");
			hkPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yarim ochiq vagon(пв)", "Kapital ta’mir(КР)", oy + '-' + year, "MDH(СНГ)");
			hkSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Sisterna(цс)", "Kapital ta’mir(КР)", oy + '-' + year, "MDH(СНГ)");
			hkBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Boshqa turdagi(проч)", "Kapital ta’mir(КР)", oy + '-' + year, "MDH(СНГ)");

			model.addAttribute("hkHamma", hkHamma);
			model.addAttribute("hkKriti", hkKriti);
			model.addAttribute("hkPlatforma", hkPlatforma);
			model.addAttribute("hkPoluvagon", hkPoluvagon);
			model.addAttribute("hkSisterna", hkSisterna);
			model.addAttribute("hkBoshqa", hkBoshqa);

			//VCHD-3 uchun kapital tamir
			akHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yopiq vagon (крыт)", "Kapital ta’mir(КР)", oy + '-' + year, "MDH(СНГ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Platforma(пф)", "Kapital ta’mir(КР)", oy + '-' + year, "MDH(СНГ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yarim ochiq vagon(пв)", "Kapital ta’mir(КР)", oy + '-' + year, "MDH(СНГ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Boshqa turdagi(проч)", "Kapital ta’mir(КР)", oy + '-' + year, "MDH(СНГ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Sisterna(цс)", "Kapital ta’mir(КР)", oy + '-' + year, "MDH(СНГ)");
			akKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yopiq vagon (крыт)", "Kapital ta’mir(КР)", oy + '-' + year, "MDH(СНГ)");
			akPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Platforma(пф)", "Kapital ta’mir(КР)", oy + '-' + year, "MDH(СНГ)");
			akPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yarim ochiq vagon(пв)", "Kapital ta’mir(КР)", oy + '-' + year, "MDH(СНГ)");
			akSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Sisterna(цс)", "Kapital ta’mir(КР)", oy + '-' + year, "MDH(СНГ)");
			akBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Boshqa turdagi(проч)", "Kapital ta’mir(КР)", oy + '-' + year, "MDH(СНГ)");

			model.addAttribute("akHamma", akHamma);
			model.addAttribute("akKriti", akKriti);
			model.addAttribute("akPlatforma", akPlatforma);
			model.addAttribute("akPoluvagon", akPoluvagon);
			model.addAttribute("akSisterna", akSisterna);
			model.addAttribute("akBoshqa", akBoshqa);

			//samarqand uchun Kapital tamir
			skHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yopiq vagon (крыт)", "Kapital ta’mir(КР)", oy + '-' + year, "MDH(СНГ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Platforma(пф)", "Kapital ta’mir(КР)", oy + '-' + year, "MDH(СНГ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yarim ochiq vagon(пв)", "Kapital ta’mir(КР)", oy + '-' + year, "MDH(СНГ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Sisterna(цс)", "Kapital ta’mir(КР)", oy + '-' + year, "MDH(СНГ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Boshqa turdagi(проч)", "Kapital ta’mir(КР)", oy + '-' + year, "MDH(СНГ)");
			skKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yopiq vagon (крыт)", "Kapital ta’mir(КР)", oy + '-' + year, "MDH(СНГ)");
			skPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Platforma(пф)", "Kapital ta’mir(КР)", oy + '-' + year, "MDH(СНГ)");
			skPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yarim ochiq vagon(пв)", "Kapital ta’mir(КР)", oy + '-' + year, "MDH(СНГ)");
			skSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Sisterna(цс)", "Kapital ta’mir(КР)", oy + '-' + year, "MDH(СНГ)");
			skBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Boshqa turdagi(проч)", "Kapital ta’mir(КР)", oy + '-' + year, "MDH(СНГ)");

			model.addAttribute("skHamma", skHamma);
			model.addAttribute("skKriti", skKriti);
			model.addAttribute("skPlatforma", skPlatforma);
			model.addAttribute("skPoluvagon", skPoluvagon);
			model.addAttribute("skSisterna", skSisterna);
			model.addAttribute("skBoshqa", skBoshqa);

			// itogo Fact uchun kapital tamir
			uvtkhamma = skHamma + hkHamma + akHamma;
			uvtkKriti = skKriti + hkKriti + akKriti;
			uvtkPlatforma = skPlatforma + akPlatforma + hkPlatforma;
			uvtkPoluvagon = skPoluvagon + hkPoluvagon + akPoluvagon;
			uvtkSisterna = akSisterna + hkSisterna + skSisterna;
			uvtkBoshqa = akBoshqa + hkBoshqa + skBoshqa;

			model.addAttribute("uvtkhamma", uvtkhamma);
			model.addAttribute("uvtkKriti", uvtkKriti);
			model.addAttribute("uvtkPlatforma", uvtkPlatforma);
			model.addAttribute("uvtkPoluvagon", uvtkPoluvagon);
			model.addAttribute("uvtkSisterna", uvtkSisterna);
			model.addAttribute("uvtkBoshqa", uvtkBoshqa);

			//VCHD-3 uchun kapital tamir
			hkrHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yopiq vagon (крыт)", "KRP(КРП)", oy + '-' + year, "MDH(СНГ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Platforma(пф)", "KRP(КРП)", oy + '-' + year, "MDH(СНГ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yarim ochiq vagon(пв)", "KRP(КРП)", oy + '-' + year, "MDH(СНГ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Sisterna(цс)", "KRP(КРП)", oy + '-' + year, "MDH(СНГ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Boshqa turdagi(проч)", "KRP(КРП)", oy + '-' + year, "MDH(СНГ)");
			hkrKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yopiq vagon (крыт)", "KRP(КРП)", oy + '-' + year, "MDH(СНГ)");
			hkrPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Platforma(пф)", "KRP(КРП)", oy + '-' + year, "MDH(СНГ)");
			hkrPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yarim ochiq vagon(пв)", "KRP(КРП)", oy + '-' + year, "MDH(СНГ)");
			hkrSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Sisterna(цс)", "KRP(КРП)", oy + '-' + year, "MDH(СНГ)");
			hkrBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Boshqa turdagi(проч)", "KRP(КРП)", oy + '-' + year, "MDH(СНГ)");

			model.addAttribute("hkrHamma", hkrHamma);
			model.addAttribute("hkrKriti", hkrKriti);
			model.addAttribute("hkrPlatforma", hkrPlatforma);
			model.addAttribute("hkrPoluvagon", hkrPoluvagon);
			model.addAttribute("hkrSisterna", hkrSisterna);
			model.addAttribute("hkrBoshqa", hkrBoshqa);

			//VCHD-3 uchun kapital tamir
			akrHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yopiq vagon (крыт)", "KRP(КРП)", oy + '-' + year, "MDH(СНГ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Platforma(пф)", "KRP(КРП)", oy + '-' + year, "MDH(СНГ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yarim ochiq vagon(пв)", "KRP(КРП)", oy + '-' + year, "MDH(СНГ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Boshqa turdagi(проч)", "KRP(КРП)", oy + '-' + year, "MDH(СНГ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Sisterna(цс)", "KRP(КРП)", oy + '-' + year, "MDH(СНГ)");
			akrKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yopiq vagon (крыт)", "KRP(КРП)", oy + '-' + year, "MDH(СНГ)");
			akrPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Platforma(пф)", "KRP(КРП)", oy + '-' + year, "MDH(СНГ)");
			akrPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yarim ochiq vagon(пв)", "KRP(КРП)", oy + '-' + year, "MDH(СНГ)");
			akrSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Sisterna(цс)", "KRP(КРП)", oy + '-' + year, "MDH(СНГ)");
			akrBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Boshqa turdagi(проч)", "KRP(КРП)", oy + '-' + year, "MDH(СНГ)");

			model.addAttribute("akrHamma", akrHamma);
			model.addAttribute("akrKriti", akrKriti);
			model.addAttribute("akrPlatforma", akrPlatforma);
			model.addAttribute("akrPoluvagon", akrPoluvagon);
			model.addAttribute("akrSisterna", akrSisterna);
			model.addAttribute("akrBoshqa", akrBoshqa);

			//samarqand uchun Kapital tamir
			skrHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yopiq vagon (крыт)", "KRP(КРП)", oy + '-' + year, "MDH(СНГ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Platforma(пф)", "KRP(КРП)", oy + '-' + year, "MDH(СНГ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yarim ochiq vagon(пв)", "KRP(КРП)", oy + '-' + year, "MDH(СНГ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Sisterna(цс)", "KRP(КРП)", oy + '-' + year, "MDH(СНГ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Boshqa turdagi(проч)", "KRP(КРП)", oy + '-' + year, "MDH(СНГ)");
			skrKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yopiq vagon (крыт)", "KRP(КРП)", oy + '-' + year, "MDH(СНГ)");
			skrPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Platforma(пф)", "KRP(КРП)", oy + '-' + year, "MDH(СНГ)");
			skrPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yarim ochiq vagon(пв)", "KRP(КРП)", oy + '-' + year, "MDH(СНГ)");
			skrSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Sisterna(цс)", "KRP(КРП)", oy + '-' + year, "MDH(СНГ)");
			skrBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Boshqa turdagi(проч)", "KRP(КРП)", oy + '-' + year, "MDH(СНГ)");

			model.addAttribute("skrHamma", skrHamma);
			model.addAttribute("skrKriti", skrKriti);
			model.addAttribute("skrPlatforma", skrPlatforma);
			model.addAttribute("skrPoluvagon", skrPoluvagon);
			model.addAttribute("skrSisterna", skrSisterna);
			model.addAttribute("skrBoshqa", skrBoshqa);

			// itogo Fact uchun KRP tamir
			uvtkrhamma = skrHamma + hkrHamma + akrHamma;
			uvtkrKriti = skrKriti + hkrKriti + akrKriti;
			uvtkrPlatforma = skrPlatforma + akrPlatforma + hkrPlatforma;
			uvtkrPoluvagon = skrPoluvagon + hkrPoluvagon + akrPoluvagon;
			uvtkrSisterna = akrSisterna + hkrSisterna + skrSisterna;
			uvtkrBoshqa = akrBoshqa + hkrBoshqa + skrBoshqa;

			model.addAttribute("uvtkrhamma", uvtkrhamma);
			model.addAttribute("uvtkrKriti", uvtkrKriti);
			model.addAttribute("uvtkrPlatforma", uvtkrPlatforma);
			model.addAttribute("uvtkrPoluvagon", uvtkrPoluvagon);
			model.addAttribute("uvtkrSisterna", uvtkrSisterna);
			model.addAttribute("uvtkrBoshqa", uvtkrBoshqa);

		}else if (country.equalsIgnoreCase("Sanoat(ПРОМ)")) {

			//VCHD-3 uchun depli tamir
			hdHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yopiq vagon (крыт)", "Depoli ta’mir(ДР)", oy + '-' + year, "Sanoat(ПРОМ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Platforma(пф)", "Depoli ta’mir(ДР)", oy + '-' + year, "Sanoat(ПРОМ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yarim ochiq vagon(пв)", "Depoli ta’mir(ДР)", oy + '-' + year, "Sanoat(ПРОМ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Sisterna(цс)", "Depoli ta’mir(ДР)", oy + '-' + year, "Sanoat(ПРОМ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Boshqa turdagi(проч)", "Depoli ta’mir(ДР)", oy + '-' + year, "Sanoat(ПРОМ)");
			hdKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yopiq vagon (крыт)", "Depoli ta’mir(ДР)", oy + '-' + year, "Sanoat(ПРОМ)");
			hdPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Platforma(пф)", "Depoli ta’mir(ДР)", oy + '-' + year, "Sanoat(ПРОМ)");
			hdPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yarim ochiq vagon(пв)", "Depoli ta’mir(ДР)", oy + '-' + year, "Sanoat(ПРОМ)");
			hdSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Sisterna(цс)", "Depoli ta’mir(ДР)", oy + '-' + year, "Sanoat(ПРОМ)");
			hdBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Boshqa turdagi(проч)", "Depoli ta’mir(ДР)", oy + '-' + year, "Sanoat(ПРОМ)");

			model.addAttribute("hdHamma", hdHamma);
			model.addAttribute("hdKriti", hdKriti);
			model.addAttribute("hdPlatforma", hdPlatforma);
			model.addAttribute("hdPoluvagon", hdPoluvagon);
			model.addAttribute("hdSisterna", hdSisterna);
			model.addAttribute("hdBoshqa", hdBoshqa);

			//VCHD-5 uchun depli tamir
			adHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yopiq vagon (крыт)", "Depoli ta’mir(ДР)", oy + '-' + year, "Sanoat(ПРОМ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Platforma(пф)", "Depoli ta’mir(ДР)", oy + '-' + year, "Sanoat(ПРОМ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yarim ochiq vagon(пв)", "Depoli ta’mir(ДР)", oy + '-' + year, "Sanoat(ПРОМ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Boshqa turdagi(проч)", "Depoli ta’mir(ДР)", oy + '-' + year, "Sanoat(ПРОМ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Sisterna(цс)", "Depoli ta’mir(ДР)", oy + '-' + year, "Sanoat(ПРОМ)");
			adKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yopiq vagon (крыт)", "Depoli ta’mir(ДР)", oy + '-' + year, "Sanoat(ПРОМ)");
			adPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Platforma(пф)", "Depoli ta’mir(ДР)", oy + '-' + year, "Sanoat(ПРОМ)");
			adPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yarim ochiq vagon(пв)", "Depoli ta’mir(ДР)", oy + '-' + year, "Sanoat(ПРОМ)");
			adSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Sisterna(цс)", "Depoli ta’mir(ДР)", oy + '-' + year, "Sanoat(ПРОМ)");
			adBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Boshqa turdagi(проч)", "Depoli ta’mir(ДР)", oy + '-' + year, "Sanoat(ПРОМ)");

			model.addAttribute("adHamma", adHamma);
			model.addAttribute("adKriti", adKriti);
			model.addAttribute("adPlatforma", adPlatforma);
			model.addAttribute("adPoluvagon", adPoluvagon);
			model.addAttribute("adSisterna", adSisterna);
			model.addAttribute("adBoshqa", adBoshqa);

			//samarqand uchun depli tamir
			sdHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yopiq vagon (крыт)", "Depoli ta’mir(ДР)", oy + '-' + year, "Sanoat(ПРОМ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Platforma(пф)", "Depoli ta’mir(ДР)", oy + '-' + year, "Sanoat(ПРОМ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yarim ochiq vagon(пв)", "Depoli ta’mir(ДР)", oy + '-' + year, "Sanoat(ПРОМ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Sisterna(цс)", "Depoli ta’mir(ДР)", oy + '-' + year, "Sanoat(ПРОМ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Boshqa turdagi(проч)", "Depoli ta’mir(ДР)", oy + '-' + year, "Sanoat(ПРОМ)");
			sdKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yopiq vagon (крыт)", "Depoli ta’mir(ДР)", oy + '-' + year, "Sanoat(ПРОМ)");
			sdPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Platforma(пф)", "Depoli ta’mir(ДР)", oy + '-' + year, "Sanoat(ПРОМ)");
			sdPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yarim ochiq vagon(пв)", "Depoli ta’mir(ДР)", oy + '-' + year, "Sanoat(ПРОМ)");
			sdSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Sisterna(цс)", "Depoli ta’mir(ДР)", oy + '-' + year, "Sanoat(ПРОМ)");
			sdBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Boshqa turdagi(проч)", "Depoli ta’mir(ДР)", oy + '-' + year, "Sanoat(ПРОМ)");

			model.addAttribute("sdHamma", sdHamma);
			model.addAttribute("sdKriti", sdKriti);
			model.addAttribute("sdPlatforma", sdPlatforma);
			model.addAttribute("sdPoluvagon", sdPoluvagon);
			model.addAttribute("sdSisterna", sdSisterna);
			model.addAttribute("sdBoshqa", sdBoshqa);

			// itogo Fact uchun depli tamir
			uvtdHamma = sdHamma + hdHamma + adHamma;
			uvtdKriti = sdKriti + hdKriti + adKriti;
			uvtdPlatforma = sdPlatforma + adPlatforma + hdPlatforma;
			uvtdPoluvagon = sdPoluvagon + hdPoluvagon + adPoluvagon;
			uvtdSisterna = adSisterna + hdSisterna + sdSisterna;
			uvtdBoshqa = adBoshqa + hdBoshqa + sdBoshqa;

			model.addAttribute("uvtdHamma", uvtdHamma);
			model.addAttribute("uvtdKriti", uvtdKriti);
			model.addAttribute("uvtdPlatforma", uvtdPlatforma);
			model.addAttribute("uvtdPoluvagon", uvtdPoluvagon);
			model.addAttribute("uvtdSisterna", uvtdSisterna);
			model.addAttribute("uvtdBoshqa", uvtdBoshqa);


			//Yolovchi vagon Fact
			atYolovchi = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yo'lovchi vagon(пассжир)", "TO-3", oy + '-' + year, "Sanoat(ПРОМ)");
			adYolovchi = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yo'lovchi vagon(пассжир)", "Depoli ta’mir(ДР)", oy + '-' + year, "Sanoat(ПРОМ)");

			model.addAttribute("atYolovchi", atYolovchi);
			model.addAttribute("adYolovchi", adYolovchi);

			//VCHD-3 uchun kapital tamir
			hkHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yopiq vagon (крыт)", "Kapital ta’mir(КР)", oy + '-' + year, "Sanoat(ПРОМ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Platforma(пф)", "Kapital ta’mir(КР)", oy + '-' + year, "Sanoat(ПРОМ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yarim ochiq vagon(пв)", "Kapital ta’mir(КР)", oy + '-' + year, "Sanoat(ПРОМ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Sisterna(цс)", "Kapital ta’mir(КР)", oy + '-' + year, "Sanoat(ПРОМ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Boshqa turdagi(проч)", "Kapital ta’mir(КР)", oy + '-' + year, "Sanoat(ПРОМ)");
			hkKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yopiq vagon (крыт)", "Kapital ta’mir(КР)", oy + '-' + year, "Sanoat(ПРОМ)");
			hkPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Platforma(пф)", "Kapital ta’mir(КР)", oy + '-' + year, "Sanoat(ПРОМ)");
			hkPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yarim ochiq vagon(пв)", "Kapital ta’mir(КР)", oy + '-' + year, "Sanoat(ПРОМ)");
			hkSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Sisterna(цс)", "Kapital ta’mir(КР)", oy + '-' + year, "Sanoat(ПРОМ)");
			hkBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Boshqa turdagi(проч)", "Kapital ta’mir(КР)", oy + '-' + year, "Sanoat(ПРОМ)");

			model.addAttribute("hkHamma", hkHamma);
			model.addAttribute("hkKriti", hkKriti);
			model.addAttribute("hkPlatforma", hkPlatforma);
			model.addAttribute("hkPoluvagon", hkPoluvagon);
			model.addAttribute("hkSisterna", hkSisterna);
			model.addAttribute("hkBoshqa", hkBoshqa);

			//VCHD-3 uchun kapital tamir
			akHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yopiq vagon (крыт)", "Kapital ta’mir(КР)", oy + '-' + year, "Sanoat(ПРОМ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Platforma(пф)", "Kapital ta’mir(КР)", oy + '-' + year, "Sanoat(ПРОМ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yarim ochiq vagon(пв)", "Kapital ta’mir(КР)", oy + '-' + year, "Sanoat(ПРОМ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Boshqa turdagi(проч)", "Kapital ta’mir(КР)", oy + '-' + year, "Sanoat(ПРОМ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Sisterna(цс)", "Kapital ta’mir(КР)", oy + '-' + year, "Sanoat(ПРОМ)");
			akKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yopiq vagon (крыт)", "Kapital ta’mir(КР)", oy + '-' + year, "Sanoat(ПРОМ)");
			akPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Platforma(пф)", "Kapital ta’mir(КР)", oy + '-' + year, "Sanoat(ПРОМ)");
			akPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yarim ochiq vagon(пв)", "Kapital ta’mir(КР)", oy + '-' + year, "Sanoat(ПРОМ)");
			akSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Sisterna(цс)", "Kapital ta’mir(КР)", oy + '-' + year, "Sanoat(ПРОМ)");
			akBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Boshqa turdagi(проч)", "Kapital ta’mir(КР)", oy + '-' + year, "Sanoat(ПРОМ)");

			model.addAttribute("akHamma", akHamma);
			model.addAttribute("akKriti", akKriti);
			model.addAttribute("akPlatforma", akPlatforma);
			model.addAttribute("akPoluvagon", akPoluvagon);
			model.addAttribute("akSisterna", akSisterna);
			model.addAttribute("akBoshqa", akBoshqa);

			//samarqand uchun Kapital tamir
			skHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yopiq vagon (крыт)", "Kapital ta’mir(КР)", oy + '-' + year, "Sanoat(ПРОМ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Platforma(пф)", "Kapital ta’mir(КР)", oy + '-' + year, "Sanoat(ПРОМ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yarim ochiq vagon(пв)", "Kapital ta’mir(КР)", oy + '-' + year, "Sanoat(ПРОМ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Sisterna(цс)", "Kapital ta’mir(КР)", oy + '-' + year, "Sanoat(ПРОМ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Boshqa turdagi(проч)", "Kapital ta’mir(КР)", oy + '-' + year, "Sanoat(ПРОМ)");
			skKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yopiq vagon (крыт)", "Kapital ta’mir(КР)", oy + '-' + year, "Sanoat(ПРОМ)");
			skPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Platforma(пф)", "Kapital ta’mir(КР)", oy + '-' + year, "Sanoat(ПРОМ)");
			skPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yarim ochiq vagon(пв)", "Kapital ta’mir(КР)", oy + '-' + year, "Sanoat(ПРОМ)");
			skSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Sisterna(цс)", "Kapital ta’mir(КР)", oy + '-' + year, "Sanoat(ПРОМ)");
			skBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Boshqa turdagi(проч)", "Kapital ta’mir(КР)", oy + '-' + year, "Sanoat(ПРОМ)");

			model.addAttribute("skHamma", skHamma);
			model.addAttribute("skKriti", skKriti);
			model.addAttribute("skPlatforma", skPlatforma);
			model.addAttribute("skPoluvagon", skPoluvagon);
			model.addAttribute("skSisterna", skSisterna);
			model.addAttribute("skBoshqa", skBoshqa);

			// itogo Fact uchun kapital tamir
			uvtkhamma = skHamma + hkHamma + akHamma;
			uvtkKriti = skKriti + hkKriti + akKriti;
			uvtkPlatforma = skPlatforma + akPlatforma + hkPlatforma;
			uvtkPoluvagon = skPoluvagon + hkPoluvagon + akPoluvagon;
			uvtkSisterna = akSisterna + hkSisterna + skSisterna;
			uvtkBoshqa = akBoshqa + hkBoshqa + skBoshqa;

			model.addAttribute("uvtkhamma", uvtkhamma);
			model.addAttribute("uvtkKriti", uvtkKriti);
			model.addAttribute("uvtkPlatforma", uvtkPlatforma);
			model.addAttribute("uvtkPoluvagon", uvtkPoluvagon);
			model.addAttribute("uvtkSisterna", uvtkSisterna);
			model.addAttribute("uvtkBoshqa", uvtkBoshqa);

			//VCHD-3 uchun kapital tamir
			hkrHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yopiq vagon (крыт)", "KRP(КРП)", oy + '-' + year, "Sanoat(ПРОМ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Platforma(пф)", "KRP(КРП)", oy + '-' + year, "Sanoat(ПРОМ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yarim ochiq vagon(пв)", "KRP(КРП)", oy + '-' + year, "Sanoat(ПРОМ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Sisterna(цс)", "KRP(КРП)", oy + '-' + year, "Sanoat(ПРОМ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Boshqa turdagi(проч)", "KRP(КРП)", oy + '-' + year, "Sanoat(ПРОМ)");
			hkrKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yopiq vagon (крыт)", "KRP(КРП)", oy + '-' + year, "Sanoat(ПРОМ)");
			hkrPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Platforma(пф)", "KRP(КРП)", oy + '-' + year, "Sanoat(ПРОМ)");
			hkrPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Yarim ochiq vagon(пв)", "KRP(КРП)", oy + '-' + year, "Sanoat(ПРОМ)");
			hkrSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Sisterna(цс)", "KRP(КРП)", oy + '-' + year, "Sanoat(ПРОМ)");
			hkrBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-3", "Boshqa turdagi(проч)", "KRP(КРП)", oy + '-' + year, "Sanoat(ПРОМ)");

			model.addAttribute("hkrHamma", hkrHamma);
			model.addAttribute("hkrKriti", hkrKriti);
			model.addAttribute("hkrPlatforma", hkrPlatforma);
			model.addAttribute("hkrPoluvagon", hkrPoluvagon);
			model.addAttribute("hkrSisterna", hkrSisterna);
			model.addAttribute("hkrBoshqa", hkrBoshqa);

			//VCHD-3 uchun kapital tamir
			akrHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yopiq vagon (крыт)", "KRP(КРП)", oy + '-' + year, "Sanoat(ПРОМ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Platforma(пф)", "KRP(КРП)", oy + '-' + year, "Sanoat(ПРОМ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yarim ochiq vagon(пв)", "KRP(КРП)", oy + '-' + year, "Sanoat(ПРОМ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Boshqa turdagi(проч)", "KRP(КРП)", oy + '-' + year, "Sanoat(ПРОМ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Sisterna(цс)", "KRP(КРП)", oy + '-' + year, "Sanoat(ПРОМ)");
			akrKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yopiq vagon (крыт)", "KRP(КРП)", oy + '-' + year, "Sanoat(ПРОМ)");
			akrPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Platforma(пф)", "KRP(КРП)", oy + '-' + year, "Sanoat(ПРОМ)");
			akrPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Yarim ochiq vagon(пв)", "KRP(КРП)", oy + '-' + year, "Sanoat(ПРОМ)");
			akrSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Sisterna(цс)", "KRP(КРП)", oy + '-' + year, "Sanoat(ПРОМ)");
			akrBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-5", "Boshqa turdagi(проч)", "KRP(КРП)", oy + '-' + year, "Sanoat(ПРОМ)");

			model.addAttribute("akrHamma", akrHamma);
			model.addAttribute("akrKriti", akrKriti);
			model.addAttribute("akrPlatforma", akrPlatforma);
			model.addAttribute("akrPoluvagon", akrPoluvagon);
			model.addAttribute("akrSisterna", akrSisterna);
			model.addAttribute("akrBoshqa", akrBoshqa);

			//samarqand uchun Kapital tamir
			skrHamma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yopiq vagon (крыт)", "KRP(КРП)", oy + '-' + year, "Sanoat(ПРОМ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Platforma(пф)", "KRP(КРП)", oy + '-' + year, "Sanoat(ПРОМ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yarim ochiq vagon(пв)", "KRP(КРП)", oy + '-' + year, "Sanoat(ПРОМ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Sisterna(цс)", "KRP(КРП)", oy + '-' + year, "Sanoat(ПРОМ)") +
					vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Boshqa turdagi(проч)", "KRP(КРП)", oy + '-' + year, "Sanoat(ПРОМ)");
			skrKriti = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yopiq vagon (крыт)", "KRP(КРП)", oy + '-' + year, "Sanoat(ПРОМ)");
			skrPlatforma = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Platforma(пф)", "KRP(КРП)", oy + '-' + year, "Sanoat(ПРОМ)");
			skrPoluvagon = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Yarim ochiq vagon(пв)", "KRP(КРП)", oy + '-' + year, "Sanoat(ПРОМ)");
			skrSisterna = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Sisterna(цс)", "KRP(КРП)", oy + '-' + year, "Sanoat(ПРОМ)");
			skrBoshqa = vagonTayyorService.countAllActiveByDepoNomiVagonTuriAndTamirTuri("VCHD-6", "Boshqa turdagi(проч)", "KRP(КРП)", oy + '-' + year, "Sanoat(ПРОМ)");

			model.addAttribute("skrHamma", skrHamma);
			model.addAttribute("skrKriti", skrKriti);
			model.addAttribute("skrPlatforma", skrPlatforma);
			model.addAttribute("skrPoluvagon", skrPoluvagon);
			model.addAttribute("skrSisterna", skrSisterna);
			model.addAttribute("skrBoshqa", skrBoshqa);

			// itogo Fact uchun KRP tamir
			uvtkrhamma = skrHamma + hkrHamma + akrHamma;
			uvtkrKriti = skrKriti + hkrKriti + akrKriti;
			uvtkrPlatforma = skrPlatforma + akrPlatforma + hkrPlatforma;
			uvtkrPoluvagon = skrPoluvagon + hkrPoluvagon + akrPoluvagon;
			uvtkrSisterna = akrSisterna + hkrSisterna + skrSisterna;
			uvtkrBoshqa = akrBoshqa + hkrBoshqa + skrBoshqa;

			model.addAttribute("uvtkrhamma", uvtkrhamma);
			model.addAttribute("uvtkrKriti", uvtkrKriti);
			model.addAttribute("uvtkrPlatforma", uvtkrPlatforma);
			model.addAttribute("uvtkrPoluvagon", uvtkrPoluvagon);
			model.addAttribute("uvtkrSisterna", uvtkrSisterna);
			model.addAttribute("uvtkrBoshqa", uvtkrBoshqa);

		}



		//yuklab olish uchun list
		List<Integer> vagonsToDownloadTable = new ArrayList<>();
//Depoli tamir
//		drTable = new ArrayList<>();
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
		vagonsToDownloadTable.add(uvtdHamma);
//		vagonsToDownloadTable.add(uvtdHamma - DtHammaPlan);
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
//		krTable = new ArrayList<>();
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
//		krpTable = new ArrayList<>();
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

//Yolovchi
//		yolovchiTable = new ArrayList<>();
		vagonsToDownloadTable.add(AndjToYolovchiPlan);
		vagonsToDownloadTable.add(atYolovchi);
//		vagonsToDownloadTable.add(atYolovchi - AndjToYolovchiPlan);
		vagonsToDownloadTable.add(AndjDtYolovchiPlan);
		vagonsToDownloadTable.add(adYolovchi);
//		vagonsToDownloadTable.add(adYolovchi - AndjDtYolovchiPlan);

//		yolovchiTable = vagonsToDownloadTable;
		vagonsToDownloadAllTable = vagonsToDownloadTable;

	}

	private void getPlanFactForAllMonthInFilter(Model model, int month, int year, String country) {

		PlanBiznes planDto = vagonTayyorService.getPlanBiznes(String.valueOf(year));
		//planlar kiritish


		//havos depo tamir hamma plan
		int HavDtKritiPlan = planDto.getHavDtKritiPlanBiznes();
		int HavDtPlatformaPlan = planDto.getHavDtPlatformaPlanBiznes();
		int HavDtPoluvagonPlan = planDto.getHavDtPoluvagonPlanBiznes();
		int HavDtSisternaPlan = planDto.getHavDtSisternaPlanBiznes();
		int HavDtBoshqaPlan = planDto.getHavDtBoshqaPlanBiznes();
		int HavDtHammaPlan = HavDtKritiPlan + HavDtPlatformaPlan + HavDtPoluvagonPlan + HavDtSisternaPlan + HavDtBoshqaPlan;

		model.addAttribute("HDtHammaPlan", HavDtHammaPlan);
		model.addAttribute("HDtKritiPlan", HavDtKritiPlan);
		model.addAttribute("HDtPlatformaPlan", HavDtPlatformaPlan);
		model.addAttribute("HDtPoluvagonPlan", HavDtPoluvagonPlan);
		model.addAttribute("HDtSisternaPlan", HavDtSisternaPlan);
		model.addAttribute("HDtBoshqaPlan", HavDtBoshqaPlan);


		//VCHD-5 depo tamir plan
		int AndjDtKritiPlan = planDto.getAndjDtKritiPlanBiznes();
		int AndjDtPlatformaPlan =planDto.getAndjDtPlatformaPlanBiznes();
		int AndjDtPoluvagonPlan =planDto.getAndjDtPoluvagonPlanBiznes();
		int AndjDtSisternaPlan =planDto.getAndjDtSisternaPlanBiznes();
		int AndjDtBoshqaPlan=planDto.getAndjDtBoshqaPlanBiznes();
		int AndjDtHammaPlan = AndjDtKritiPlan + AndjDtPlatformaPlan + AndjDtPoluvagonPlan + AndjDtSisternaPlan + AndjDtBoshqaPlan;

		model.addAttribute("ADtHammaPlan", AndjDtHammaPlan);
		model.addAttribute("ADtKritiPlan", AndjDtKritiPlan);
		model.addAttribute("ADtPlatformaPlan",AndjDtPlatformaPlan);
		model.addAttribute("ADtPoluvagonPlan", AndjDtPoluvagonPlan);
		model.addAttribute("ADtSisternaPlan", AndjDtSisternaPlan);
		model.addAttribute("ADtBoshqaPlan", AndjDtBoshqaPlan);

		//samarqand depo tamir plan
		int SamDtKritiPlan = planDto.getSamDtKritiPlanBiznes();
		int SamDtPlatformaPlan = planDto.getSamDtPlatformaPlanBiznes();
		int SamDtPoluvagonPlan =  planDto.getSamDtPoluvagonPlanBiznes();
		int SamDtSisternaPlan = planDto.getSamDtSisternaPlanBiznes();
		int SamDtBoshqaPlan = planDto.getSamDtBoshqaPlanBiznes();
		int SamDtHammaPlan=SamDtKritiPlan + SamDtPlatformaPlan + SamDtPoluvagonPlan + SamDtSisternaPlan + SamDtBoshqaPlan;

		model.addAttribute("SDtHammaPlan",SamDtHammaPlan);
		model.addAttribute("SDtKritiPlan", SamDtKritiPlan);
		model.addAttribute("SDtPlatformaPlan", SamDtPlatformaPlan);
		model.addAttribute("SDtPoluvagonPlan", SamDtPoluvagonPlan);
		model.addAttribute("SDtSisternaPlan", SamDtSisternaPlan);
		model.addAttribute("SDtBoshqaPlan", SamDtBoshqaPlan);


		// Itogo planlar depo tamir
		int DtHammaPlan = AndjDtHammaPlan + HavDtHammaPlan + SamDtHammaPlan;
		int DtKritiPlan = SamDtKritiPlan + HavDtKritiPlan + AndjDtKritiPlan;
		int DtPlatformaPlan = SamDtPlatformaPlan + HavDtPlatformaPlan + AndjDtPlatformaPlan;
		int DtPoluvagonPlan = SamDtPoluvagonPlan + HavDtPoluvagonPlan + AndjDtPoluvagonPlan;
		int DtSisternaPlan = SamDtSisternaPlan + HavDtSisternaPlan + AndjDtSisternaPlan;
		int DtBoshqaPlan = SamDtBoshqaPlan + HavDtBoshqaPlan + AndjDtBoshqaPlan;

		model.addAttribute("UDtHammaPlan", DtHammaPlan);
		model.addAttribute("UDtKritiPlan", DtKritiPlan);
		model.addAttribute("UDtPlatformaPlan", DtPlatformaPlan);
		model.addAttribute("UDtPoluvagonPlan",DtPoluvagonPlan);
		model.addAttribute("UDtSisternaPlan", DtSisternaPlan);
		model.addAttribute("UDtBoshqaPlan", DtBoshqaPlan);


		//Yolovchi vagon Plan
		int AndjToYolovchiPlan = planDto.getAndjTYolovchiPlanBiznes();
		int AndjDtYolovchiPlan = planDto.getAndjDtYolovchiPlanBiznes();

		model.addAttribute("AToYolovchiPlan", AndjToYolovchiPlan);
		model.addAttribute("ADtYolovchiPlan", AndjDtYolovchiPlan);

		//hovos kapital plan
		int HavKtKritiPlan = planDto.getHavKtKritiPlanBiznes();
		int HavKtPlatformaPlan = planDto.getHavKtPlatformaPlanBiznes();
		int HavKtPoluvagonPlan = planDto.getHavKtPoluvagonPlanBiznes();
		int HavKtSisternaPlan = planDto.getHavKtSisternaPlanBiznes();
		int HavKtBoshqaPlan = planDto.getHavKtBoshqaPlanBiznes();
		int HavKtHammaPlan = HavKtKritiPlan + HavKtPlatformaPlan + HavKtPoluvagonPlan + HavKtSisternaPlan + HavKtBoshqaPlan;

		model.addAttribute("HKtHammaPlan", HavKtHammaPlan);
		model.addAttribute("HKtKritiPlan", HavKtKritiPlan);
		model.addAttribute("HKtPlatformaPlan", HavKtPlatformaPlan);
		model.addAttribute("HKtPoluvagonPlan", HavKtPoluvagonPlan);
		model.addAttribute("HKtSisternaPlan", HavKtSisternaPlan);
		model.addAttribute("HKtBoshqaPlan", HavKtBoshqaPlan);

		//ANDIJON kapital plan
		int AndjKtKritiPlan = planDto.getAndjKtKritiPlanBiznes();
		int AndjKtPlatformaPlan=planDto.getAndjKtPlatformaPlanBiznes();
		int AndjKtPoluvagonPlan=planDto.getAndjKtPoluvagonPlanBiznes();
		int AndjKtSisternaPlan=planDto.getAndjKtSisternaPlanBiznes();
		int AndjKtBoshqaPlan=planDto.getAndjKtBoshqaPlanBiznes();
		int AndjKtHammaPlan = AndjKtKritiPlan + AndjKtPlatformaPlan + AndjKtPoluvagonPlan + AndjKtSisternaPlan + AndjKtBoshqaPlan;

		model.addAttribute("AKtHammaPlan", AndjKtHammaPlan);
		model.addAttribute("AKtKritiPlan", AndjKtKritiPlan);
		model.addAttribute("AKtPlatformaPlan", AndjKtPlatformaPlan);
		model.addAttribute("AKtPoluvagonPlan", AndjKtPoluvagonPlan);
		model.addAttribute("AKtSisternaPlan", AndjKtSisternaPlan);
		model.addAttribute("AKtBoshqaPlan", AndjKtBoshqaPlan);

		//Samrqand kapital plan
		int SamKtKritiPlan = planDto.getSamKtKritiPlanBiznes();
		int SamKtPlatformaPlan = planDto.getSamKtPlatformaPlanBiznes();
		int SamKtPoluvagonPlan = planDto.getSamKtPoluvagonPlanBiznes();
		int SamKtSisternaPlan = planDto.getSamKtSisternaPlanBiznes();
		int SamKtBoshqaPlan = planDto.getSamKtBoshqaPlanBiznes();
		int SamKtHammaPlan = SamKtKritiPlan + SamKtPlatformaPlan + SamKtPoluvagonPlan + SamKtSisternaPlan +SamKtBoshqaPlan;

		model.addAttribute("SKtHammaPlan",SamKtHammaPlan);
		model.addAttribute("SKtKritiPlan", SamKtKritiPlan);
		model.addAttribute("SKtPlatformaPlan", SamKtPlatformaPlan);
		model.addAttribute("SKtPoluvagonPlan", SamKtPoluvagonPlan);
		model.addAttribute("SKtSisternaPlan", SamKtSisternaPlan);
		model.addAttribute("SKtBoshqaPlan", SamKtBoshqaPlan);


		//Itogo kapital plan
		int KtHammaPlan = AndjKtHammaPlan + HavKtHammaPlan + SamKtHammaPlan;
		int KtKritiPlan = SamKtKritiPlan + HavKtKritiPlan + AndjKtKritiPlan;
		int KtPlatformaPlan = SamKtPlatformaPlan + HavKtPlatformaPlan + AndjKtPlatformaPlan;
		int KtPoluvagonPlan = SamKtPoluvagonPlan + HavKtPoluvagonPlan + AndjKtPoluvagonPlan;
		int KtSisternaPlan = SamKtSisternaPlan + HavKtSisternaPlan + AndjKtSisternaPlan;
		int KtBoshqaPlan = SamKtBoshqaPlan + HavKtBoshqaPlan + AndjKtBoshqaPlan;

		model.addAttribute("UKtHammaPlan", KtHammaPlan);
		model.addAttribute("UKtKritiPlan", KtKritiPlan);
		model.addAttribute("UKtPlatformaPlan", KtPlatformaPlan);
		model.addAttribute("UKtPoluvagonPlan",KtPoluvagonPlan);
		model.addAttribute("UKtSisternaPlan", KtSisternaPlan);
		model.addAttribute("UKtBoshqaPlan", KtBoshqaPlan);


		//Hovos krp plan
		int HavKrpKritiPlan = planDto.getHavKrpKritiPlanBiznes();
		int HavKrpPlatformaPlan = planDto.getHavKrpPlatformaPlanBiznes();
		int HavKrpPoluvagonPlan = planDto.getHavKrpPoluvagonPlanBiznes();
		int HavKrpSisternaPlan = planDto.getHavKrpSisternaPlanBiznes();
		int HavKrpBoshqaPlan = planDto.getHavKrpBoshqaPlanBiznes();
		int HavKrpHammaPlan = HavKrpKritiPlan + HavKrpPlatformaPlan + HavKrpPoluvagonPlan + HavKrpSisternaPlan + HavKrpBoshqaPlan;

		model.addAttribute("HKrpHammaPlan",HavKrpHammaPlan);
		model.addAttribute("HKrpKritiPlan", HavKrpKritiPlan);
		model.addAttribute("HKrpPlatformaPlan", HavKrpPlatformaPlan);
		model.addAttribute("HKrpPoluvagonPlan", HavKrpPoluvagonPlan);
		model.addAttribute("HKrpSisternaPlan", HavKrpSisternaPlan);
		model.addAttribute("HKrpBoshqaPlan", HavKrpBoshqaPlan);

		//andijon krp plan
		int AndjKrpKritiPlan = planDto.getAndjKrpKritiPlanBiznes();
		int AndjKrpPlatformaPlan = planDto.getAndjKrpPlatformaPlanBiznes();
		int AndjKrpPoluvagonPlan = planDto.getAndjKrpPoluvagonPlanBiznes();
		int AndjKrpSisternaPlan = planDto.getAndjKrpSisternaPlanBiznes();
		int AndjKrpBoshqaPlan = planDto.getAndjKrpBoshqaPlanBiznes();
		int AndjKrpHammaPlan = AndjKrpKritiPlan + AndjKrpPlatformaPlan + AndjKrpPoluvagonPlan + AndjKrpSisternaPlan + AndjKrpBoshqaPlan;

		model.addAttribute("AKrpHammaPlan",AndjKrpHammaPlan);
		model.addAttribute("AKrpKritiPlan", AndjKrpKritiPlan);
		model.addAttribute("AKrpPlatformaPlan", AndjKrpPlatformaPlan);
		model.addAttribute("AKrpPoluvagonPlan", AndjKrpPoluvagonPlan);
		model.addAttribute("AKrpSisternaPlan", AndjKrpSisternaPlan);
		model.addAttribute("AKrpBoshqaPlan", AndjKrpBoshqaPlan);

		//Samarqankr Krp plan
		int SamKrpKritiPlan = planDto.getSamKrpKritiPlanBiznes();
		int SamKrpPlatformaPlan = planDto.getSamKrpPlatformaPlanBiznes();
		int SamKrpPoluvagonPlan = planDto.getSamKrpPoluvagonPlanBiznes();
		int SamKrpSisternaPlan = planDto.getSamKrpSisternaPlanBiznes();
		int SamKrpBoshqaPlan = planDto.getSamKrpBoshqaPlanBiznes();
		int SamKrpHammaPlan = SamKrpKritiPlan + SamKrpPlatformaPlan + SamKrpPoluvagonPlan + SamKrpSisternaPlan + SamKrpBoshqaPlan;

		model.addAttribute("SKrpHammaPlan", SamKrpHammaPlan);
		model.addAttribute("SKrpKritiPlan", SamKrpKritiPlan);
		model.addAttribute("SKrpPlatformaPlan", SamKrpPlatformaPlan);
		model.addAttribute("SKrpPoluvagonPlan", SamKrpPoluvagonPlan);
		model.addAttribute("SKrpSisternaPlan", SamKrpSisternaPlan);
		model.addAttribute("SKrpBoshqaPlan", SamKrpBoshqaPlan);


		//itogo krp

		int KrpHammaPlan = AndjKrpHammaPlan + HavKrpHammaPlan + SamKrpHammaPlan;
		int KrpKritiPlan = SamKrpKritiPlan + HavKrpKritiPlan + AndjKrpKritiPlan;
		int KrpPlatformaPlan = SamKrpPlatformaPlan + HavKrpPlatformaPlan + AndjKrpPlatformaPlan;
		int KrpPoluvagonPlan = SamKrpPoluvagonPlan + HavKrpPoluvagonPlan + AndjKrpPoluvagonPlan;
		int KrpSisternaPlan = SamKrpSisternaPlan + HavKrpSisternaPlan + AndjKrpSisternaPlan;
		int KrpBoshqaPlan = SamKrpBoshqaPlan + HavKrpBoshqaPlan + AndjKrpBoshqaPlan;

		model.addAttribute("UKrpHammaPlan", KrpHammaPlan);
		model.addAttribute("UKrpKritiPlan", KrpKritiPlan);
		model.addAttribute("UKrpPlatformaPlan", KrpPlatformaPlan);
		model.addAttribute("UKrpPoluvagonPlan",KrpPoluvagonPlan);
		model.addAttribute("UKrpSisternaPlan", KrpSisternaPlan);
		model.addAttribute("UKrpBoshqaPlan", KrpBoshqaPlan);

		//VCHD-3 uchun depli tamir
		int hdHamma = 0;
		int hdKriti = 0;
		int hdPlatforma = 0;
		int hdPoluvagon = 0;
		int hdSisterna = 0;
		int hdBoshqa = 0;

		//VCHD-5 uchun depli tamir
		int adHamma = 0;
		int adKriti = 0;
		int adPlatforma = 0;
		int adPoluvagon = 0;
		int adSisterna = 0;
		int adBoshqa = 0;

		//samarqand uchun depli tamir
		int sdHamma = 0;
		int sdKriti = 0;
		int sdPlatforma = 0;
		int sdPoluvagon = 0;
		int sdSisterna = 0;
		int sdBoshqa = 0;


		// itogo Fact uchun depli tamir
		int uvtdHamma = 0;
		int uvtdKriti = 0;
		int uvtdPlatforma = 0;
		int uvtdPoluvagon = 0;
		int uvtdSisterna = 0;
		int uvtdBoshqa = 0;

		//Yolovchi vagon Fact
		int atYolovchi = 0;
		int adYolovchi = 0;

		//VCHD-3 uchun kapital tamir
		int hkHamma = 0;
		int hkKriti = 0;
		int hkPlatforma = 0;
		int hkPoluvagon = 0;
		int hkSisterna = 0;
		int hkBoshqa = 0;

		//VCHD-3 uchun kapital tamir
		int akHamma = 0;
		int akKriti = 0;
		int akPlatforma = 0;
		int akPoluvagon = 0;
		int akSisterna = 0;
		int akBoshqa = 0;

		//samarqand uchun Kapital tamir
		int skHamma = 0;
		int skKriti = 0;
		int skPlatforma = 0;
		int skPoluvagon = 0;
		int skSisterna = 0;
		int skBoshqa = 0;

		// itogo Fact uchun kapital tamir
		int uvtkHamma = 0;
		int uvtkKriti = 0;
		int uvtkPlatforma = 0;
		int uvtkPoluvagon = 0;
		int uvtkSisterna = 0;
		int uvtkBoshqa = 0;

		//VCHD-3 uchun kapital tamir
		int hkrHamma = 0;
		int hkrKriti = 0;
		int hkrPlatforma = 0;
		int hkrPoluvagon = 0;
		int hkrSisterna = 0;
		int hkrBoshqa = 0;

		//VCHD-3 uchun kapital tamir
		int akrHamma = 0;
		int akrKriti = 0;
		int akrPlatforma = 0;
		int akrPoluvagon = 0;
		int akrSisterna = 0;
		int akrBoshqa = 0;

		//samarqand uchun Kapital tamir
		int skrHamma = 0;
		int skrKriti = 0;
		int skrPlatforma = 0;
		int skrPoluvagon = 0;
		int skrSisterna = 0;
		int skrBoshqa = 0;


		// itogo Fact uchun KRP tamir
		int uvtkrHamma = 0;
		int uvtkrKriti = 0;
		int uvtkrPlatforma = 0;
		int uvtkrPoluvagon = 0;
		int uvtkrSisterna = 0;
		int uvtkrBoshqa = 0;

		if (country.equalsIgnoreCase("Hammasi")) {

			// VCHD-3 depo tamir hamma false vagonlar soni
			hdKriti=vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yopiq vagon (крыт)","Depoli ta’mir(ДР)", String.valueOf(year));
			hdPlatforma=vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Platforma(пф)","Depoli ta’mir(ДР)", String.valueOf(year));
			hdPoluvagon=vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yarim ochiq vagon(пв)","Depoli ta’mir(ДР)", String.valueOf(year));
			hdSisterna=vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Sisterna(цс)","Depoli ta’mir(ДР)", String.valueOf(year));
			hdBoshqa=vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Boshqa turdagi(проч)","Depoli ta’mir(ДР)", String.valueOf(year));
			hdHamma = hdKriti + hdPlatforma + hdPoluvagon + hdSisterna + hdBoshqa ;
	
			model.addAttribute("hdHammaFalse",hdHamma );
			model.addAttribute("hdKritiFalse",hdKriti );
			model.addAttribute("hdPlatformaFalse",hdPlatforma);
			model.addAttribute("hdPoluvagonFalse",hdPoluvagon );
			model.addAttribute("hdSisternaFalse",hdSisterna);
			model.addAttribute("hdBoshqaFalse",hdBoshqa);
	
			// VCHD-5 depo tamir hamma false vagonlar soni
			adKriti = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yopiq vagon (крыт)","Depoli ta’mir(ДР)", String.valueOf(year));
			adPlatforma = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Platforma(пф)","Depoli ta’mir(ДР)", String.valueOf(year));
			adPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yarim ochiq vagon(пв)","Depoli ta’mir(ДР)", String.valueOf(year));
			adSisterna = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Sisterna(цс)","Depoli ta’mir(ДР)", String.valueOf(year));
			adBoshqa = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Boshqa turdagi(проч)","Depoli ta’mir(ДР)", String.valueOf(year));
			adHamma = adKriti + adPlatforma+ adPoluvagon+ adSisterna + adBoshqa;
	
			model.addAttribute("adHammaFalse",adHamma );
			model.addAttribute("adKritiFalse",adKriti);
			model.addAttribute("adPlatformaFalse",adPlatforma);
			model.addAttribute("adPoluvagonFalse",adPoluvagon);
			model.addAttribute("adSisternaFalse",adSisterna);
			model.addAttribute("adBoshqaFalse",adBoshqa);
	
			// samarqand depo tamir hamma false vagonlar soni
			sdKriti = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yopiq vagon (крыт)","Depoli ta’mir(ДР)", String.valueOf(year));
			sdPlatforma = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Platforma(пф)","Depoli ta’mir(ДР)", String.valueOf(year));
			sdPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yarim ochiq vagon(пв)","Depoli ta’mir(ДР)", String.valueOf(year));
			sdSisterna = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Sisterna(цс)","Depoli ta’mir(ДР)", String.valueOf(year));
			sdBoshqa = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Boshqa turdagi(проч)","Depoli ta’mir(ДР)", String.valueOf(year));
			sdHamma = sdKriti + sdPlatforma+ sdPoluvagon+ sdSisterna + sdBoshqa;
	
			model.addAttribute("sdHammaFalse",sdHamma );
			model.addAttribute("sdKritiFalse",sdKriti );
			model.addAttribute("sdPlatformaFalse",sdPlatforma);
			model.addAttribute("sdPoluvagonFalse",sdPoluvagon);
			model.addAttribute("sdSisternaFalse",sdSisterna);
			model.addAttribute("sdBoshqaFalse",sdBoshqa);
	
			// depoli tamir itogo uchun
			uvtdHamma =  adHamma + hdHamma+sdHamma ;
			uvtdKriti = sdKriti + hdKriti + adKriti ;
			uvtdPlatforma = adPlatforma + sdPlatforma + hdPlatforma ;
			uvtdPoluvagon  = adPoluvagon + sdPoluvagon + hdPoluvagon;
			uvtdSisterna = adSisterna + hdSisterna + sdSisterna;
			uvtdBoshqa = adBoshqa + hdBoshqa + sdBoshqa;
	
			model.addAttribute("uvtdHammaFalse",uvtdHamma );
			model.addAttribute("uvtdKritiFalse",uvtdKriti );
			model.addAttribute("uvtdPlatformaFalse",uvtdPlatforma );
			model.addAttribute("uvtdPoluvagonFalse",uvtdPoluvagon );
			model.addAttribute("uvtdSisternaFalse",uvtdSisterna);
			model.addAttribute("uvtdBoshqaFalse",uvtdBoshqa );
	
			//Yolovchi Andijon fact
			atYolovchi=vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yo'lovchi vagon(пассжир)","TO-3", String.valueOf(year));
			adYolovchi=vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yo'lovchi vagon(пассжир)","Depoli ta’mir(ДР)", String.valueOf(year));
	
			model.addAttribute("atYolovchiFalse",atYolovchi );
			model.addAttribute("adYolovchiFalse",adYolovchi );
	
	
			// VCHD-3 kapital tamir hamma false vagonlar soni
			hkKriti = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yopiq vagon (крыт)","Kapital ta’mir(КР)", String.valueOf(year));
			hkPlatforma = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Platforma(пф)","Kapital ta’mir(КР)", String.valueOf(year));
			hkPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yarim ochiq vagon(пв)","Kapital ta’mir(КР)", String.valueOf(year));
			hkSisterna = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Sisterna(цс)","Kapital ta’mir(КР)", String.valueOf(year));
			hkBoshqa = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Boshqa turdagi(проч)","Kapital ta’mir(КР)", String.valueOf(year));
			hkHamma = hkKriti + hkPlatforma+ hkPoluvagon+ hkSisterna + hkBoshqa;
	
			model.addAttribute("hkHammaFalse",hkHamma);
			model.addAttribute("hkKritiFalse",hkKriti);
			model.addAttribute("hkPlatformaFalse",hkPlatforma);
			model.addAttribute("hkPoluvagonFalse",hkPoluvagon );
			model.addAttribute("hkSisternaFalse",hkSisterna );
			model.addAttribute("hkBoshqaFalse",hkBoshqa);
	
			// VCHD-5 kapital tamir hamma false vagonlar soni
			akKriti = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yopiq vagon (крыт)","Kapital ta’mir(КР)", String.valueOf(year));
			akPlatforma = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Platforma(пф)","Kapital ta’mir(КР)", String.valueOf(year));
			akPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yarim ochiq vagon(пв)","Kapital ta’mir(КР)", String.valueOf(year));
			akSisterna = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Sisterna(цс)","Kapital ta’mir(КР)", String.valueOf(year));
			akBoshqa = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Boshqa turdagi(проч)","Kapital ta’mir(КР)", String.valueOf(year));
			akHamma = akKriti + akPlatforma+ akPoluvagon+ akSisterna + akBoshqa;
	
			model.addAttribute("akHammaFalse",akHamma);
			model.addAttribute("akKritiFalse",akKriti);
			model.addAttribute("akPlatformaFalse",akPlatforma);
			model.addAttribute("akPoluvagonFalse",akPoluvagon);
			model.addAttribute("akSisternaFalse",akSisterna);
			model.addAttribute("akBoshqaFalse",akBoshqa);
	
			// samarqand KApital tamir hamma false vagonlar soni
			skKriti = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yopiq vagon (крыт)","Kapital ta’mir(КР)", String.valueOf(year));
			skPlatforma = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Platforma(пф)","Kapital ta’mir(КР)", String.valueOf(year));
			skPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yarim ochiq vagon(пв)","Kapital ta’mir(КР)", String.valueOf(year));
			skSisterna = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Sisterna(цс)","Kapital ta’mir(КР)", String.valueOf(year));
			skBoshqa = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Boshqa turdagi(проч)","Kapital ta’mir(КР)", String.valueOf(year));
			skHamma = skKriti + skPlatforma+ skPoluvagon+ skSisterna + skBoshqa;
	
			model.addAttribute("skHammaFalse",skHamma);
			model.addAttribute("skKritiFalse",skKriti);
			model.addAttribute("skPlatformaFalse",skPlatforma);
			model.addAttribute("skPoluvagonFalse",skPoluvagon);
			model.addAttribute("skSisternaFalse",skSisterna);
			model.addAttribute("skBoshqaFalse",skBoshqa);
	
			// Kapital tamir itogo uchun
			uvtkHamma =  akHamma + hkHamma+skHamma;
			uvtkKriti = skKriti + hkKriti + akKriti;
			uvtkPlatforma = akPlatforma + skPlatforma + hkPlatforma;
			uvtkPoluvagon  = akPoluvagon + skPoluvagon + hkPoluvagon;
			uvtkSisterna = akSisterna + hkSisterna + skSisterna;
			uvtkBoshqa = akBoshqa + hkBoshqa + skBoshqa;
	
			model.addAttribute("uvtkHammaFalse",uvtkHamma );
			model.addAttribute("uvtkKritiFalse",uvtkKriti );
			model.addAttribute("uvtkPlatformaFalse",uvtkPlatforma);
			model.addAttribute("uvtkPoluvagonFalse",uvtkPoluvagon);
			model.addAttribute("uvtkSisternaFalse",uvtkSisterna );
			model.addAttribute("uvtkBoshqaFalse",uvtkBoshqa );
	
			//**
	
			// VCHD-3 KRP tamir hamma false vagonlar soni
			hkrKriti = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yopiq vagon (крыт)","KRP(КРП)", String.valueOf(year));
			hkrPlatforma = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Platforma(пф)","KRP(КРП)", String.valueOf(year));
			hkrPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Yarim ochiq vagon(пв)","KRP(КРП)", String.valueOf(year));
			hkrSisterna = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Sisterna(цс)","KRP(КРП)", String.valueOf(year));
			hkrBoshqa = vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-3","Boshqa turdagi(проч)","KRP(КРП)", String.valueOf(year));
			hkrHamma = hkrKriti + hkrPlatforma+ hkrPoluvagon+ hkrSisterna + hkrBoshqa;
	
			model.addAttribute("hkrHammaFalse",hkrHamma);
			model.addAttribute("hkrKritiFalse",hkrKriti);
			model.addAttribute("hkrPlatformaFalse",hkrPlatforma);
			model.addAttribute("hkrPoluvagonFalse",hkrPoluvagon);
			model.addAttribute("hkrSisternaFalse",hkrSisterna);
			model.addAttribute("hkrBoshqaFalse",hkrBoshqa);
	
			// VCHD-5 KRP tamir hamma false vagonlar soni
			akrKriti=vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yopiq vagon (крыт)","KRP(КРП)", String.valueOf(year));
			akrPlatforma=vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Platforma(пф)","KRP(КРП)", String.valueOf(year));
			akrPoluvagon=vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Yarim ochiq vagon(пв)","KRP(КРП)", String.valueOf(year));
			akrSisterna=vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Sisterna(цс)","KRP(КРП)", String.valueOf(year));
			akrBoshqa=vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-5","Boshqa turdagi(проч)","KRP(КРП)", String.valueOf(year));
			akrHamma = akrKriti + akrPlatforma+ akrPoluvagon+ akrSisterna + akrBoshqa;
	
			model.addAttribute("akrHammaFalse",akrHamma);
			model.addAttribute("akrKritiFalse",akrKriti);
			model.addAttribute("akrPlatformaFalse",akrPlatforma);
			model.addAttribute("akrPoluvagonFalse",akrPoluvagon);
			model.addAttribute("akrSisternaFalse",akrSisterna);
			model.addAttribute("akBoshqaFalse",akrBoshqa);
	
			// samarqand KRP tamir hamma false vagonlar soni
			skrKriti=vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yopiq vagon (крыт)","KRP(КРП)", String.valueOf(year));
			skrPlatforma=vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Platforma(пф)","KRP(КРП)", String.valueOf(year));
			skrPoluvagon=vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Yarim ochiq vagon(пв)","KRP(КРП)", String.valueOf(year));
			skrSisterna=vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Sisterna(цс)","KRP(КРП)", String.valueOf(year));
			skrBoshqa=vagonTayyorService.countByDepoNomiVagonTuriAndTamirTuri("VCHD-6","Boshqa turdagi(проч)","KRP(КРП)", String.valueOf(year));
			skrHamma = skrKriti + skrPlatforma+ skrPoluvagon+ skrSisterna + skrBoshqa;
	
			model.addAttribute("skrHammaFalse",skrHamma);
			model.addAttribute("skrKritiFalse",skrKriti);
			model.addAttribute("skrPlatformaFalse",skrPlatforma);
			model.addAttribute("skrPoluvagonFalse",skrPoluvagon);
			model.addAttribute("skrSisternaFalse",skrSisterna);
			model.addAttribute("skrBoshqaFalse",skrBoshqa);
	// Krp itogo uchun
			uvtkrHamma =  akrHamma + hkrHamma+skrHamma;
			uvtkrKriti = skrKriti + hkrKriti + akrKriti;
			uvtkrPlatforma = akrPlatforma + skrPlatforma + hkrPlatforma;
			uvtkrPoluvagon  = akrPoluvagon + skrPoluvagon + hkrPoluvagon;
			uvtkrSisterna = akrSisterna + hkrSisterna + skrSisterna;
			uvtkrBoshqa = akrBoshqa + hkrBoshqa + skrBoshqa;
	
			model.addAttribute("uvtkrHammaFalse",uvtkrHamma );
			model.addAttribute("uvtkrKritiFalse",uvtkrKriti);
			model.addAttribute("uvtkrPlatformaFalse",uvtkrPlatforma);
			model.addAttribute("uvtkrPoluvagonFalse",uvtkrPoluvagon);
			model.addAttribute("uvtkrSisternaFalse",uvtkrSisterna);
			model.addAttribute("uvtkrBoshqaFalse",uvtkrBoshqa);



//			ishlaydi avvalgi sonlari nomalumligi uchun commentfa olindi
		}else if (country.equalsIgnoreCase("O'TY(ГАЖК)")) {
			//**//
			// VCHD-3 depo tamir hamma false vagonlar soni
			hdKriti = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Yopiq vagon (крыт)", "Depoli ta’mir(ДР)", "O'TY(ГАЖК)", String.valueOf(year));
			hdPlatforma = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Platforma(пф)", "Depoli ta’mir(ДР)", "O'TY(ГАЖК)", String.valueOf(year));
			hdPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Yarim ochiq vagon(пв)", "Depoli ta’mir(ДР)", "O'TY(ГАЖК)", String.valueOf(year));
			hdSisterna = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Sisterna(цс)", "Depoli ta’mir(ДР)", "O'TY(ГАЖК)", String.valueOf(year));
			hdBoshqa = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Boshqa turdagi(проч)", "Depoli ta’mir(ДР)", "O'TY(ГАЖК)", String.valueOf(year));
			hdHamma = hdKriti + hdPlatforma + hdPoluvagon + hdSisterna + hdBoshqa;

			model.addAttribute("hdHammaFalse", hdHamma);
			model.addAttribute("hdKritiFalse", hdKriti);
			model.addAttribute("hdPlatformaFalse", hdPlatforma);
			model.addAttribute("hdPoluvagonFalse", hdPoluvagon);
			model.addAttribute("hdSisternaFalse", hdSisterna);
			model.addAttribute("hdBoshqaFalse", hdBoshqa);

			// VCHD-5 depo tamir hamma false vagonlar soni
			adKriti = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Yopiq vagon (крыт)", "Depoli ta’mir(ДР)", "O'TY(ГАЖК)", String.valueOf(year));
			adPlatforma = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Platforma(пф)", "Depoli ta’mir(ДР)", "O'TY(ГАЖК)", String.valueOf(year));
			adPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Yarim ochiq vagon(пв)", "Depoli ta’mir(ДР)", "O'TY(ГАЖК)", String.valueOf(year));
			adSisterna = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Sisterna(цс)", "Depoli ta’mir(ДР)", "O'TY(ГАЖК)", String.valueOf(year));
			adBoshqa = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Boshqa turdagi(проч)", "Depoli ta’mir(ДР)", "O'TY(ГАЖК)", String.valueOf(year));
			adHamma = adKriti + adPlatforma + adPoluvagon + adSisterna + adBoshqa;

			model.addAttribute("adHammaFalse", adHamma);
			model.addAttribute("adKritiFalse", adKriti);
			model.addAttribute("adPlatformaFalse", adPlatforma);
			model.addAttribute("adPoluvagonFalse", adPoluvagon);
			model.addAttribute("adSisternaFalse", adSisterna);
			model.addAttribute("adBoshqaFalse", adBoshqa);

			// samarqand depo tamir hamma false vagonlar soni
			sdKriti = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Yopiq vagon (крыт)", "Depoli ta’mir(ДР)", "O'TY(ГАЖК)", String.valueOf(year));
			sdPlatforma = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Platforma(пф)", "Depoli ta’mir(ДР)", "O'TY(ГАЖК)", String.valueOf(year));
			sdPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Yarim ochiq vagon(пв)", "Depoli ta’mir(ДР)", "O'TY(ГАЖК)", String.valueOf(year));
			sdSisterna = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Sisterna(цс)", "Depoli ta’mir(ДР)", "O'TY(ГАЖК)", String.valueOf(year));
			sdBoshqa = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Boshqa turdagi(проч)", "Depoli ta’mir(ДР)", "O'TY(ГАЖК)", String.valueOf(year));
			sdHamma = sdKriti + sdPlatforma + sdPoluvagon + sdSisterna + sdBoshqa;

			model.addAttribute("sdHammaFalse", sdHamma);
			model.addAttribute("sdKritiFalse", sdKriti);
			model.addAttribute("sdPlatformaFalse", sdPlatforma);
			model.addAttribute("sdPoluvagonFalse", sdPoluvagon);
			model.addAttribute("sdSisternaFalse", sdSisterna);
			model.addAttribute("sdBoshqaFalse", sdBoshqa);

			// depoli tamir itogo uchun
			uvtdHamma = adHamma + hdHamma + sdHamma;
			uvtdKriti = sdKriti + hdKriti + adKriti;
			uvtdPlatforma = adPlatforma + sdPlatforma + hdPlatforma;
			uvtdPoluvagon = adPoluvagon + sdPoluvagon + hdPoluvagon;
			uvtdSisterna = adSisterna + hdSisterna + sdSisterna;
			uvtdBoshqa = adBoshqa + hdBoshqa + sdBoshqa;

			model.addAttribute("uvtdHammaFalse", uvtdHamma);
			model.addAttribute("uvtdKritiFalse", uvtdKriti);
			model.addAttribute("uvtdPlatformaFalse", uvtdPlatforma);
			model.addAttribute("uvtdPoluvagonFalse", uvtdPoluvagon);
			model.addAttribute("uvtdSisternaFalse", uvtdSisterna);
			model.addAttribute("uvtdBoshqaFalse", uvtdBoshqa);

			//Yolovchi Andijon fact
			atYolovchi = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Yo'lovchi vagon(пассжир)", "TO-3", "O'TY(ГАЖК)", String.valueOf(year));
			adYolovchi = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Yo'lovchi vagon(пассжир)", "Depoli ta’mir(ДР)","O'TY(ГАЖК)", String.valueOf(year));

			model.addAttribute("atYolovchiFalse", atYolovchi);
			model.addAttribute("adYolovchiFalse", adYolovchi);


			// VCHD-3 kapital tamir hamma false vagonlar soni
			hkKriti = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Yopiq vagon (крыт)", "Kapital ta’mir(КР)", "O'TY(ГАЖК)", String.valueOf(year));
			hkPlatforma = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Platforma(пф)", "Kapital ta’mir(КР)", "O'TY(ГАЖК)", String.valueOf(year));
			hkPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Yarim ochiq vagon(пв)", "Kapital ta’mir(КР)", "O'TY(ГАЖК)", String.valueOf(year));
			hkSisterna = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Sisterna(цс)", "Kapital ta’mir(КР)", "O'TY(ГАЖК)", String.valueOf(year));
			hkBoshqa = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Boshqa turdagi(проч)", "Kapital ta’mir(КР)", "O'TY(ГАЖК)", String.valueOf(year));
			hkHamma = hkKriti + hkPlatforma + hkPoluvagon + hkSisterna + hkBoshqa;

			model.addAttribute("hkHammaFalse", hkHamma);
			model.addAttribute("hkKritiFalse", hkKriti);
			model.addAttribute("hkPlatformaFalse", hkPlatforma);
			model.addAttribute("hkPoluvagonFalse", hkPoluvagon);
			model.addAttribute("hkSisternaFalse", hkSisterna);
			model.addAttribute("hkBoshqaFalse", hkBoshqa);

			// VCHD-5 kapital tamir hamma false vagonlar soni
			akKriti = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Yopiq vagon (крыт)", "Kapital ta’mir(КР)", "O'TY(ГАЖК)", String.valueOf(year));
			akPlatforma = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Platforma(пф)", "Kapital ta’mir(КР)", "O'TY(ГАЖК)", String.valueOf(year));
			akPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Yarim ochiq vagon(пв)", "Kapital ta’mir(КР)", "O'TY(ГАЖК)", String.valueOf(year));
			akSisterna = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Sisterna(цс)", "Kapital ta’mir(КР)", "O'TY(ГАЖК)", String.valueOf(year));
			akBoshqa = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Boshqa turdagi(проч)", "Kapital ta’mir(КР)", "O'TY(ГАЖК)", String.valueOf(year));
			akHamma = akKriti + akPlatforma + akPoluvagon + akSisterna + akBoshqa;

			model.addAttribute("akHammaFalse", akHamma);
			model.addAttribute("akKritiFalse", akKriti);
			model.addAttribute("akPlatformaFalse", akPlatforma);
			model.addAttribute("akPoluvagonFalse", akPoluvagon);
			model.addAttribute("akSisternaFalse", akSisterna);
			model.addAttribute("akBoshqaFalse", akBoshqa);

			// samarqand KApital tamir hamma false vagonlar soni
			skKriti = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Yopiq vagon (крыт)", "Kapital ta’mir(КР)", "O'TY(ГАЖК)", String.valueOf(year));
			skPlatforma = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Platforma(пф)", "Kapital ta’mir(КР)", "O'TY(ГАЖК)", String.valueOf(year));
			skPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Yarim ochiq vagon(пв)", "Kapital ta’mir(КР)", "O'TY(ГАЖК)", String.valueOf(year));
			skSisterna = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Sisterna(цс)", "Kapital ta’mir(КР)", "O'TY(ГАЖК)", String.valueOf(year));
			skBoshqa = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Boshqa turdagi(проч)", "Kapital ta’mir(КР)", "O'TY(ГАЖК)", String.valueOf(year));
			skHamma = skKriti + skPlatforma + skPoluvagon + skSisterna + skBoshqa;

			model.addAttribute("skHammaFalse", skHamma);
			model.addAttribute("skKritiFalse", skKriti);
			model.addAttribute("skPlatformaFalse", skPlatforma);
			model.addAttribute("skPoluvagonFalse", skPoluvagon);
			model.addAttribute("skSisternaFalse", skSisterna);
			model.addAttribute("skBoshqaFalse", skBoshqa);

			// Kapital tamir itogo uchun
			uvtkHamma = akHamma + hkHamma + skHamma;
			uvtkKriti = skKriti + hkKriti + akKriti;
			uvtkPlatforma = akPlatforma + skPlatforma + hkPlatforma;
			uvtkPoluvagon = akPoluvagon + skPoluvagon + hkPoluvagon;
			uvtkSisterna = akSisterna + hkSisterna + skSisterna;
			uvtkBoshqa = akBoshqa + hkBoshqa + skBoshqa;

			model.addAttribute("uvtkHammaFalse", uvtkHamma);
			model.addAttribute("uvtkKritiFalse", uvtkKriti);
			model.addAttribute("uvtkPlatformaFalse", uvtkPlatforma);
			model.addAttribute("uvtkPoluvagonFalse", uvtkPoluvagon);
			model.addAttribute("uvtkSisternaFalse", uvtkSisterna);
			model.addAttribute("uvtkBoshqaFalse", uvtkBoshqa);

			//**

			// VCHD-3 KRP tamir hamma false vagonlar soni
			hkrKriti = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Yopiq vagon (крыт)", "KRP(КРП)", "O'TY(ГАЖК)", String.valueOf(year));
			hkrPlatforma = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Platforma(пф)", "KRP(КРП)", "O'TY(ГАЖК)", String.valueOf(year));
			hkrPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Yarim ochiq vagon(пв)", "KRP(КРП)", "O'TY(ГАЖК)", String.valueOf(year));
			hkrSisterna = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Sisterna(цс)", "KRP(КРП)", "O'TY(ГАЖК)", String.valueOf(year));
			hkrBoshqa = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Boshqa turdagi(проч)", "KRP(КРП)", "O'TY(ГАЖК)", String.valueOf(year));
			hkrHamma = hkrKriti + hkrPlatforma + hkrPoluvagon + hkrSisterna + hkrBoshqa;

			model.addAttribute("hkrHammaFalse", hkrHamma);
			model.addAttribute("hkrKritiFalse", hkrKriti);
			model.addAttribute("hkrPlatformaFalse", hkrPlatforma);
			model.addAttribute("hkrPoluvagonFalse", hkrPoluvagon);
			model.addAttribute("hkrSisternaFalse", hkrSisterna);
			model.addAttribute("hkrBoshqaFalse", hkrBoshqa);

			// VCHD-5 KRP tamir hamma false vagonlar soni
			akrKriti = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Yopiq vagon (крыт)", "KRP(КРП)", "O'TY(ГАЖК)", String.valueOf(year));
			akrPlatforma = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Platforma(пф)", "KRP(КРП)", "O'TY(ГАЖК)", String.valueOf(year));
			akrPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Yarim ochiq vagon(пв)", "KRP(КРП)", "O'TY(ГАЖК)", String.valueOf(year));
			akrSisterna = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Sisterna(цс)", "KRP(КРП)", "O'TY(ГАЖК)", String.valueOf(year));
			akrBoshqa = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Boshqa turdagi(проч)", "KRP(КРП)", "O'TY(ГАЖК)", String.valueOf(year));
			akrHamma = akrKriti + akrPlatforma + akrPoluvagon + akrSisterna + akrBoshqa;

			model.addAttribute("akrHammaFalse", akrHamma);
			model.addAttribute("akrKritiFalse", akrKriti);
			model.addAttribute("akrPlatformaFalse", akrPlatforma);
			model.addAttribute("akrPoluvagonFalse", akrPoluvagon);
			model.addAttribute("akrSisternaFalse", akrSisterna);
			model.addAttribute("akBoshqaFalse", akrBoshqa);

			// samarqand KRP tamir hamma false vagonlar soni
			skrKriti = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Yopiq vagon (крыт)", "KRP(КРП)", "O'TY(ГАЖК)", String.valueOf(year));
			skrPlatforma = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Platforma(пф)", "KRP(КРП)", "O'TY(ГАЖК)", String.valueOf(year));
			skrPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Yarim ochiq vagon(пв)", "KRP(КРП)", "O'TY(ГАЖК)", String.valueOf(year));
			skrSisterna = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Sisterna(цс)", "KRP(КРП)", "O'TY(ГАЖК)", String.valueOf(year));
			skrBoshqa = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Boshqa turdagi(проч)", "KRP(КРП)", "O'TY(ГАЖК)", String.valueOf(year));
			skrHamma = skrKriti + skrPlatforma + skrPoluvagon + skrSisterna + skrBoshqa;

			model.addAttribute("skrHammaFalse", skrHamma);
			model.addAttribute("skrKritiFalse", skrKriti);
			model.addAttribute("skrPlatformaFalse", skrPlatforma);
			model.addAttribute("skrPoluvagonFalse", skrPoluvagon);
			model.addAttribute("skrSisternaFalse", skrSisterna);
			model.addAttribute("skrBoshqaFalse", skrBoshqa);
// Krp itogo uchun
			uvtkrHamma = akrHamma + hkrHamma + skrHamma;
			uvtkrKriti = skrKriti + hkrKriti + akrKriti;
			uvtkrPlatforma = akrPlatforma + skrPlatforma + hkrPlatforma;
			uvtkrPoluvagon = akrPoluvagon + skrPoluvagon + hkrPoluvagon;
			uvtkrSisterna = akrSisterna + hkrSisterna + skrSisterna;
			uvtkrBoshqa = akrBoshqa + hkrBoshqa + skrBoshqa;

			model.addAttribute("uvtkrHammaFalse", uvtkrHamma);
			model.addAttribute("uvtkrKritiFalse", uvtkrKriti);
			model.addAttribute("uvtkrPlatformaFalse", uvtkrPlatforma);
			model.addAttribute("uvtkrPoluvagonFalse", uvtkrPoluvagon);
			model.addAttribute("uvtkrSisternaFalse", uvtkrSisterna);
			model.addAttribute("uvtkrBoshqaFalse", uvtkrBoshqa);

		}else if (country.equalsIgnoreCase("MDH(СНГ)")) {
			//**//
			// VCHD-3 depo tamir hamma false vagonlar soni
			hdKriti = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Yopiq vagon (крыт)", "Depoli ta’mir(ДР)", "MDH(СНГ)", String.valueOf(year));
			hdPlatforma = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Platforma(пф)", "Depoli ta’mir(ДР)", "MDH(СНГ)", String.valueOf(year));
			hdPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Yarim ochiq vagon(пв)", "Depoli ta’mir(ДР)", "MDH(СНГ)", String.valueOf(year));
			hdSisterna = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Sisterna(цс)", "Depoli ta’mir(ДР)", "MDH(СНГ)", String.valueOf(year));
			hdBoshqa = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Boshqa turdagi(проч)", "Depoli ta’mir(ДР)", "MDH(СНГ)", String.valueOf(year));
			hdHamma = hdKriti + hdPlatforma + hdPoluvagon + hdSisterna + hdBoshqa;

			model.addAttribute("hdHammaFalse", hdHamma);
			model.addAttribute("hdKritiFalse", hdKriti);
			model.addAttribute("hdPlatformaFalse", hdPlatforma);
			model.addAttribute("hdPoluvagonFalse", hdPoluvagon);
			model.addAttribute("hdSisternaFalse", hdSisterna);
			model.addAttribute("hdBoshqaFalse", hdBoshqa);

			// VCHD-5 depo tamir hamma false vagonlar soni
			adKriti = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Yopiq vagon (крыт)", "Depoli ta’mir(ДР)", "MDH(СНГ)", String.valueOf(year));
			adPlatforma = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Platforma(пф)", "Depoli ta’mir(ДР)", "MDH(СНГ)", String.valueOf(year));
			adPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Yarim ochiq vagon(пв)", "Depoli ta’mir(ДР)", "MDH(СНГ)", String.valueOf(year));
			adSisterna = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Sisterna(цс)", "Depoli ta’mir(ДР)", "MDH(СНГ)", String.valueOf(year));
			adBoshqa = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Boshqa turdagi(проч)", "Depoli ta’mir(ДР)", "MDH(СНГ)", String.valueOf(year));
			adHamma = adKriti + adPlatforma + adPoluvagon + adSisterna + adBoshqa;

			model.addAttribute("adHammaFalse", adHamma);
			model.addAttribute("adKritiFalse", adKriti);
			model.addAttribute("adPlatformaFalse", adPlatforma);
			model.addAttribute("adPoluvagonFalse", adPoluvagon);
			model.addAttribute("adSisternaFalse", adSisterna);
			model.addAttribute("adBoshqaFalse", adBoshqa);

			// samarqand depo tamir hamma false vagonlar soni
			sdKriti = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Yopiq vagon (крыт)", "Depoli ta’mir(ДР)", "MDH(СНГ)", String.valueOf(year));
			sdPlatforma = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Platforma(пф)", "Depoli ta’mir(ДР)", "MDH(СНГ)", String.valueOf(year));
			sdPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Yarim ochiq vagon(пв)", "Depoli ta’mir(ДР)", "MDH(СНГ)", String.valueOf(year));
			sdSisterna = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Sisterna(цс)", "Depoli ta’mir(ДР)", "MDH(СНГ)", String.valueOf(year));
			sdBoshqa = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Boshqa turdagi(проч)", "Depoli ta’mir(ДР)", "MDH(СНГ)", String.valueOf(year));
			sdHamma = sdKriti + sdPlatforma + sdPoluvagon + sdSisterna + sdBoshqa;

			model.addAttribute("sdHammaFalse", sdHamma);
			model.addAttribute("sdKritiFalse", sdKriti);
			model.addAttribute("sdPlatformaFalse", sdPlatforma);
			model.addAttribute("sdPoluvagonFalse", sdPoluvagon);
			model.addAttribute("sdSisternaFalse", sdSisterna);
			model.addAttribute("sdBoshqaFalse", sdBoshqa);

			// depoli tamir itogo uchun
			uvtdHamma = adHamma + hdHamma + sdHamma;
			uvtdKriti = sdKriti + hdKriti + adKriti;
			uvtdPlatforma = adPlatforma + sdPlatforma + hdPlatforma;
			uvtdPoluvagon = adPoluvagon + sdPoluvagon + hdPoluvagon;
			uvtdSisterna = adSisterna + hdSisterna + sdSisterna;
			uvtdBoshqa = adBoshqa + hdBoshqa + sdBoshqa;

			model.addAttribute("uvtdHammaFalse", uvtdHamma);
			model.addAttribute("uvtdKritiFalse", uvtdKriti);
			model.addAttribute("uvtdPlatformaFalse", uvtdPlatforma);
			model.addAttribute("uvtdPoluvagonFalse", uvtdPoluvagon);
			model.addAttribute("uvtdSisternaFalse", uvtdSisterna);
			model.addAttribute("uvtdBoshqaFalse", uvtdBoshqa);

			//Yolovchi Andijon fact
			atYolovchi = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Yo'lovchi vagon(пассжир)", "TO-3", "MDH(СНГ)", String.valueOf(year));
			adYolovchi = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Yo'lovchi vagon(пассжир)", "Depoli ta’mir(ДР)", "MDH(СНГ)", String.valueOf(year));

			model.addAttribute("atYolovchiFalse", atYolovchi);
			model.addAttribute("adYolovchiFalse", adYolovchi);


			// VCHD-3 kapital tamir hamma false vagonlar soni
			hkKriti = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Yopiq vagon (крыт)", "Kapital ta’mir(КР)", "MDH(СНГ)", String.valueOf(year));
			hkPlatforma = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Platforma(пф)", "Kapital ta’mir(КР)", "MDH(СНГ)", String.valueOf(year));
			hkPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Yarim ochiq vagon(пв)", "Kapital ta’mir(КР)", "MDH(СНГ)", String.valueOf(year));
			hkSisterna = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Sisterna(цс)", "Kapital ta’mir(КР)", "MDH(СНГ)", String.valueOf(year));
			hkBoshqa = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Boshqa turdagi(проч)", "Kapital ta’mir(КР)", "MDH(СНГ)", String.valueOf(year));
			hkHamma = hkKriti + hkPlatforma + hkPoluvagon + hkSisterna + hkBoshqa;

			model.addAttribute("hkHammaFalse", hkHamma);
			model.addAttribute("hkKritiFalse", hkKriti);
			model.addAttribute("hkPlatformaFalse", hkPlatforma);
			model.addAttribute("hkPoluvagonFalse", hkPoluvagon);
			model.addAttribute("hkSisternaFalse", hkSisterna);
			model.addAttribute("hkBoshqaFalse", hkBoshqa);

			// VCHD-5 kapital tamir hamma false vagonlar soni
			akKriti = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Yopiq vagon (крыт)", "Kapital ta’mir(КР)", "MDH(СНГ)", String.valueOf(year));
			akPlatforma = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Platforma(пф)", "Kapital ta’mir(КР)", "MDH(СНГ)", String.valueOf(year));
			akPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Yarim ochiq vagon(пв)", "Kapital ta’mir(КР)", "MDH(СНГ)", String.valueOf(year));
			akSisterna = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Sisterna(цс)", "Kapital ta’mir(КР)", "MDH(СНГ)", String.valueOf(year));
			akBoshqa = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Boshqa turdagi(проч)", "Kapital ta’mir(КР)", "MDH(СНГ)", String.valueOf(year));
			akHamma = akKriti + akPlatforma + akPoluvagon + akSisterna + akBoshqa;

			model.addAttribute("akHammaFalse", akHamma);
			model.addAttribute("akKritiFalse", akKriti);
			model.addAttribute("akPlatformaFalse", akPlatforma);
			model.addAttribute("akPoluvagonFalse", akPoluvagon);
			model.addAttribute("akSisternaFalse", akSisterna);
			model.addAttribute("akBoshqaFalse", akBoshqa);

			// samarqand KApital tamir hamma false vagonlar soni
			skKriti = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Yopiq vagon (крыт)", "Kapital ta’mir(КР)", "MDH(СНГ)", String.valueOf(year));
			skPlatforma = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Platforma(пф)", "Kapital ta’mir(КР)", "MDH(СНГ)", String.valueOf(year));
			skPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Yarim ochiq vagon(пв)", "Kapital ta’mir(КР)", "MDH(СНГ)", String.valueOf(year));
			skSisterna = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Sisterna(цс)", "Kapital ta’mir(КР)", "MDH(СНГ)", String.valueOf(year));
			skBoshqa = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Boshqa turdagi(проч)", "Kapital ta’mir(КР)", "MDH(СНГ)", String.valueOf(year));
			skHamma = skKriti + skPlatforma + skPoluvagon + skSisterna + skBoshqa;

			model.addAttribute("skHammaFalse", skHamma);
			model.addAttribute("skKritiFalse", skKriti);
			model.addAttribute("skPlatformaFalse", skPlatforma );
			model.addAttribute("skPoluvagonFalse", skPoluvagon);
			model.addAttribute("skSisternaFalse", skSisterna);
			model.addAttribute("skBoshqaFalse", skBoshqa);

			// Kapital tamir itogo uchun
			uvtkHamma = akHamma + hkHamma + skHamma;
			uvtkKriti = skKriti + hkKriti + akKriti;
			uvtkPlatforma = akPlatforma + skPlatforma + hkPlatforma;
			uvtkPoluvagon = akPoluvagon + skPoluvagon + hkPoluvagon;
			uvtkSisterna = akSisterna + hkSisterna + skSisterna;
			uvtkBoshqa = akBoshqa + hkBoshqa + skBoshqa;

			model.addAttribute("uvtkHammaFalse", uvtkHamma);
			model.addAttribute("uvtkKritiFalse", uvtkKriti);
			model.addAttribute("uvtkPlatformaFalse", uvtkPlatforma);
			model.addAttribute("uvtkPoluvagonFalse", uvtkPoluvagon);
			model.addAttribute("uvtkSisternaFalse", uvtkSisterna);
			model.addAttribute("uvtkBoshqaFalse", uvtkBoshqa);

			//**

			// VCHD-3 KRP tamir hamma false vagonlar soni
			hkrKriti = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Yopiq vagon (крыт)", "KRP(КРП)", "MDH(СНГ)", String.valueOf(year));
			hkrPlatforma = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Platforma(пф)", "KRP(КРП)", "MDH(СНГ)", String.valueOf(year));
			hkrPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Yarim ochiq vagon(пв)", "KRP(КРП)", "MDH(СНГ)", String.valueOf(year));
			hkrSisterna = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Sisterna(цс)", "KRP(КРП)", "MDH(СНГ)", String.valueOf(year));
			hkrBoshqa = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Boshqa turdagi(проч)", "KRP(КРП)", "MDH(СНГ)", String.valueOf(year));
			hkrHamma = hkrKriti + hkrPlatforma + hkrPoluvagon + hkrSisterna + hkrBoshqa;

			model.addAttribute("hkrHammaFalse", hkrHamma);
			model.addAttribute("hkrKritiFalse", hkrKriti);
			model.addAttribute("hkrPlatformaFalse", hkrPlatforma);
			model.addAttribute("hkrPoluvagonFalse", hkrPoluvagon);
			model.addAttribute("hkrSisternaFalse", hkrSisterna);
			model.addAttribute("hkrBoshqaFalse", hkrBoshqa);

			// VCHD-5 KRP tamir hamma false vagonlar soni
			akrKriti = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Yopiq vagon (крыт)", "KRP(КРП)", "MDH(СНГ)", String.valueOf(year));
			akrPlatforma = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Platforma(пф)", "KRP(КРП)", "MDH(СНГ)", String.valueOf(year));
			akrPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Yarim ochiq vagon(пв)", "KRP(КРП)", "MDH(СНГ)", String.valueOf(year));
			akrSisterna = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Sisterna(цс)", "KRP(КРП)", "MDH(СНГ)", String.valueOf(year));
			akrBoshqa = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Boshqa turdagi(проч)", "KRP(КРП)", "MDH(СНГ)", String.valueOf(year));
			akrHamma = akrKriti + akrPlatforma + akrPoluvagon + akrSisterna + akrBoshqa;

			model.addAttribute("akrHammaFalse", akrHamma);
			model.addAttribute("akrKritiFalse", akrKriti);
			model.addAttribute("akrPlatformaFalse", akrPlatforma);
			model.addAttribute("akrPoluvagonFalse", akrPoluvagon);
			model.addAttribute("akrSisternaFalse", akrSisterna);
			model.addAttribute("akBoshqaFalse", akrBoshqa);

			// samarqand KRP tamir hamma false vagonlar soni
			skrKriti = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Yopiq vagon (крыт)", "KRP(КРП)", "MDH(СНГ)", String.valueOf(year));
			skrPlatforma = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Platforma(пф)", "KRP(КРП)", "MDH(СНГ)", String.valueOf(year));
			skrPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Yarim ochiq vagon(пв)", "KRP(КРП)", "MDH(СНГ)", String.valueOf(year));
			skrSisterna = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Sisterna(цс)", "KRP(КРП)", "MDH(СНГ)", String.valueOf(year));
			skrBoshqa = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Boshqa turdagi(проч)", "KRP(КРП)", "MDH(СНГ)", String.valueOf(year));
			skrHamma = skrKriti + skrPlatforma + skrPoluvagon + skrSisterna + skrBoshqa;

			model.addAttribute("skrHammaFalse", skrHamma);
			model.addAttribute("skrKritiFalse", skrKriti);
			model.addAttribute("skrPlatformaFalse", skrPlatforma);
			model.addAttribute("skrPoluvagonFalse", skrPoluvagon);
			model.addAttribute("skrSisternaFalse", skrSisterna);
			model.addAttribute("skrBoshqaFalse", skrBoshqa);
// Krp itogo uchun
			uvtkrHamma = akrHamma + hkrHamma + skrHamma;
			uvtkrKriti = skrKriti + hkrKriti + akrKriti;
			uvtkrPlatforma = akrPlatforma + skrPlatforma + hkrPlatforma;
			uvtkrPoluvagon = akrPoluvagon + skrPoluvagon + hkrPoluvagon;
			uvtkrSisterna = akrSisterna + hkrSisterna + skrSisterna;
			uvtkrBoshqa = akrBoshqa + hkrBoshqa + skrBoshqa;

			model.addAttribute("uvtkrHammaFalse", uvtkrHamma);
			model.addAttribute("uvtkrKritiFalse", uvtkrKriti);
			model.addAttribute("uvtkrPlatformaFalse", uvtkrPlatforma);
			model.addAttribute("uvtkrPoluvagonFalse", uvtkrPoluvagon);
			model.addAttribute("uvtkrSisternaFalse", uvtkrSisterna);
			model.addAttribute("uvtkrBoshqaFalse", uvtkrBoshqa);

		}else if (country.equalsIgnoreCase("Sanoat(ПРОМ)")) {
			//**//
			// VCHD-3 depo tamir hamma false vagonlar soni
			hdKriti = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Yopiq vagon (крыт)", "Depoli ta’mir(ДР)", "Sanoat(ПРОМ)", String.valueOf(year));
			hdPlatforma = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Platforma(пф)", "Depoli ta’mir(ДР)", "Sanoat(ПРОМ)", String.valueOf(year));
			hdPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Yarim ochiq vagon(пв)", "Depoli ta’mir(ДР)", "Sanoat(ПРОМ)", String.valueOf(year));
			hdSisterna = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Sisterna(цс)", "Depoli ta’mir(ДР)", "Sanoat(ПРОМ)", String.valueOf(year));
			hdBoshqa = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Boshqa turdagi(проч)", "Depoli ta’mir(ДР)", "Sanoat(ПРОМ)", String.valueOf(year));
			hdHamma = hdKriti + hdPlatforma + hdPoluvagon + hdSisterna + hdBoshqa;

			model.addAttribute("hdHammaFalse", hdHamma);
			model.addAttribute("hdKritiFalse", hdKriti);
			model.addAttribute("hdPlatformaFalse", hdPlatforma);
			model.addAttribute("hdPoluvagonFalse", hdPoluvagon);
			model.addAttribute("hdSisternaFalse", hdSisterna);
			model.addAttribute("hdBoshqaFalse", hdBoshqa);

			// VCHD-5 depo tamir hamma false vagonlar soni
			adKriti = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Yopiq vagon (крыт)", "Depoli ta’mir(ДР)", "Sanoat(ПРОМ)", String.valueOf(year));
			adPlatforma = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Platforma(пф)", "Depoli ta’mir(ДР)", "Sanoat(ПРОМ)", String.valueOf(year));
			adPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Yarim ochiq vagon(пв)", "Depoli ta’mir(ДР)", "Sanoat(ПРОМ)", String.valueOf(year));
			adSisterna = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Sisterna(цс)", "Depoli ta’mir(ДР)", "Sanoat(ПРОМ)", String.valueOf(year));
			adBoshqa = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Boshqa turdagi(проч)", "Depoli ta’mir(ДР)", "Sanoat(ПРОМ)", String.valueOf(year));
			adHamma = adKriti + adPlatforma + adPoluvagon + adSisterna + adBoshqa;

			model.addAttribute("adHammaFalse", adHamma);
			model.addAttribute("adKritiFalse", adKriti);
			model.addAttribute("adPlatformaFalse", adPlatforma);
			model.addAttribute("adPoluvagonFalse", adPoluvagon);
			model.addAttribute("adSisternaFalse", adSisterna);
			model.addAttribute("adBoshqaFalse", adBoshqa);

			// samarqand depo tamir hamma false vagonlar soni
			sdKriti = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Yopiq vagon (крыт)", "Depoli ta’mir(ДР)", "Sanoat(ПРОМ)", String.valueOf(year));
			sdPlatforma = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Platforma(пф)", "Depoli ta’mir(ДР)", "Sanoat(ПРОМ)", String.valueOf(year));
			sdPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Yarim ochiq vagon(пв)", "Depoli ta’mir(ДР)", "Sanoat(ПРОМ)", String.valueOf(year));
			sdSisterna = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Sisterna(цс)", "Depoli ta’mir(ДР)", "Sanoat(ПРОМ)", String.valueOf(year));
			sdBoshqa = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Boshqa turdagi(проч)", "Depoli ta’mir(ДР)", "Sanoat(ПРОМ)", String.valueOf(year));
			sdHamma = sdKriti + sdPlatforma + sdPoluvagon + sdSisterna + sdBoshqa;

			model.addAttribute("sdHammaFalse", sdHamma);
			model.addAttribute("sdKritiFalse", sdKriti);
			model.addAttribute("sdPlatformaFalse", sdPlatforma);
			model.addAttribute("sdPoluvagonFalse", sdPoluvagon);
			model.addAttribute("sdSisternaFalse", sdSisterna);
			model.addAttribute("sdBoshqaFalse", sdBoshqa);

			// depoli tamir itogo uchun
			uvtdHamma = adHamma + hdHamma + sdHamma;
			uvtdKriti = sdKriti + hdKriti + adKriti;
			uvtdPlatforma = adPlatforma + sdPlatforma + hdPlatforma;
			uvtdPoluvagon = adPoluvagon + sdPoluvagon + hdPoluvagon;
			uvtdSisterna = adSisterna + hdSisterna + sdSisterna;
			uvtdBoshqa = adBoshqa + hdBoshqa + sdBoshqa;

			model.addAttribute("uvtdHammaFalse", uvtdHamma);
			model.addAttribute("uvtdKritiFalse", uvtdKriti);
			model.addAttribute("uvtdPlatformaFalse", uvtdPlatforma);
			model.addAttribute("uvtdPoluvagonFalse", uvtdPoluvagon);
			model.addAttribute("uvtdSisternaFalse", uvtdSisterna);
			model.addAttribute("uvtdBoshqaFalse", uvtdBoshqa);

			//Yolovchi Andijon fact
			atYolovchi = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Yo'lovchi vagon(пассжир)", "TO-3", "Sanoat(ПРОМ)", String.valueOf(year));
			adYolovchi = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Yo'lovchi vagon(пассжир)", "Depoli ta’mir(ДР)", "Sanoat(ПРОМ)", String.valueOf(year));

			model.addAttribute("atYolovchiFalse", atYolovchi);
			model.addAttribute("adYolovchiFalse", adYolovchi);


			// VCHD-3 kapital tamir hamma false vagonlar soni
			hkKriti = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Yopiq vagon (крыт)", "Kapital ta’mir(КР)", "Sanoat(ПРОМ)", String.valueOf(year));
			hkPlatforma = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Platforma(пф)", "Kapital ta’mir(КР)", "Sanoat(ПРОМ)", String.valueOf(year));
			hkPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Yarim ochiq vagon(пв)", "Kapital ta’mir(КР)", "Sanoat(ПРОМ)", String.valueOf(year));
			hkSisterna = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Sisterna(цс)", "Kapital ta’mir(КР)", "Sanoat(ПРОМ)", String.valueOf(year));
			hkBoshqa = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Boshqa turdagi(проч)", "Kapital ta’mir(КР)", "Sanoat(ПРОМ)", String.valueOf(year));
			hkHamma = hkKriti + hkPlatforma + hkPoluvagon + hkSisterna + hkBoshqa;

			model.addAttribute("hkHammaFalse", hkHamma);
			model.addAttribute("hkKritiFalse", hkKriti);
			model.addAttribute("hkPlatformaFalse", hkPlatforma);
			model.addAttribute("hkPoluvagonFalse", hkPoluvagon);
			model.addAttribute("hkSisternaFalse", hkSisterna);
			model.addAttribute("hkBoshqaFalse", hkBoshqa);

			// VCHD-5 kapital tamir hamma false vagonlar soni
			akKriti = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Yopiq vagon (крыт)", "Kapital ta’mir(КР)", "Sanoat(ПРОМ)", String.valueOf(year));
			akPlatforma = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Platforma(пф)", "Kapital ta’mir(КР)", "Sanoat(ПРОМ)", String.valueOf(year));
			akPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Yarim ochiq vagon(пв)", "Kapital ta’mir(КР)", "Sanoat(ПРОМ)", String.valueOf(year));
			akSisterna = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Sisterna(цс)", "Kapital ta’mir(КР)", "Sanoat(ПРОМ)", String.valueOf(year));
			akBoshqa = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Boshqa turdagi(проч)", "Kapital ta’mir(КР)", "Sanoat(ПРОМ)", String.valueOf(year));
			akHamma = akKriti + akPlatforma + akPoluvagon + akSisterna + akBoshqa;

			model.addAttribute("akHammaFalse", akHamma);
			model.addAttribute("akKritiFalse", akKriti);
			model.addAttribute("akPlatformaFalse", akPlatforma);
			model.addAttribute("akPoluvagonFalse", akPoluvagon);
			model.addAttribute("akSisternaFalse", akSisterna);
			model.addAttribute("akBoshqaFalse", akBoshqa);

			// samarqand KApital tamir hamma false vagonlar soni
			skKriti = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Yopiq vagon (крыт)", "Kapital ta’mir(КР)", "Sanoat(ПРОМ)", String.valueOf(year));
			skPlatforma = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Platforma(пф)", "Kapital ta’mir(КР)", "Sanoat(ПРОМ)", String.valueOf(year));
			skPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Yarim ochiq vagon(пв)", "Kapital ta’mir(КР)", "Sanoat(ПРОМ)", String.valueOf(year));
			skSisterna = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Sisterna(цс)", "Kapital ta’mir(КР)", "Sanoat(ПРОМ)", String.valueOf(year));
			skBoshqa = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Boshqa turdagi(проч)", "Kapital ta’mir(КР)", "Sanoat(ПРОМ)", String.valueOf(year));
			skHamma = skKriti + skPlatforma + skPoluvagon + skSisterna + skBoshqa;

			model.addAttribute("skHammaFalse", skHamma);
			model.addAttribute("skKritiFalse", skKriti);
			model.addAttribute("skPlatformaFalse", skPlatforma);
			model.addAttribute("skPoluvagonFalse", skPoluvagon);
			model.addAttribute("skSisternaFalse", skSisterna);
			model.addAttribute("skBoshqaFalse", skBoshqa);

			// Kapital tamir itogo uchun
			uvtkHamma = akHamma + hkHamma + skHamma;
			uvtkKriti = skKriti + hkKriti + akKriti;
			uvtkPlatforma = akPlatforma + skPlatforma + hkPlatforma;
			uvtkPoluvagon = akPoluvagon + skPoluvagon + hkPoluvagon;
			uvtkSisterna = akSisterna + hkSisterna + skSisterna;
			uvtkBoshqa = akBoshqa + hkBoshqa + skBoshqa;

			model.addAttribute("uvtkHammaFalse", uvtkHamma);
			model.addAttribute("uvtkKritiFalse", uvtkKriti);
			model.addAttribute("uvtkPlatformaFalse", uvtkPlatforma);
			model.addAttribute("uvtkPoluvagonFalse", uvtkPoluvagon);
			model.addAttribute("uvtkSisternaFalse", uvtkSisterna);
			model.addAttribute("uvtkBoshqaFalse", uvtkBoshqa);

			//**

			// VCHD-3 KRP tamir hamma false vagonlar soni
			hkrKriti = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Yopiq vagon (крыт)", "KRP(КРП)", "Sanoat(ПРОМ)", String.valueOf(year));
			hkrPlatforma = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Platforma(пф)", "KRP(КРП)", "Sanoat(ПРОМ)", String.valueOf(year));
			hkrPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Yarim ochiq vagon(пв)", "KRP(КРП)", "Sanoat(ПРОМ)", String.valueOf(year));
			hkrSisterna = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Sisterna(цс)", "KRP(КРП)", "Sanoat(ПРОМ)", String.valueOf(year));
			hkrBoshqa = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-3", "Boshqa turdagi(проч)", "KRP(КРП)", "Sanoat(ПРОМ)", String.valueOf(year));
			hkrHamma = hkrKriti + hkrPlatforma + hkrPoluvagon + hkrSisterna + hkrBoshqa;

			model.addAttribute("hkrHammaFalse", hkrHamma);
			model.addAttribute("hkrKritiFalse", hkrKriti);
			model.addAttribute("hkrPlatformaFalse", hkrPlatforma);
			model.addAttribute("hkrPoluvagonFalse", hkrPoluvagon);
			model.addAttribute("hkrSisternaFalse", hkrSisterna);
			model.addAttribute("hkrBoshqaFalse", hkrBoshqa);

			// VCHD-5 KRP tamir hamma false vagonlar soni
			akrKriti = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Yopiq vagon (крыт)", "KRP(КРП)", "Sanoat(ПРОМ)", String.valueOf(year));
			akrPlatforma = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Platforma(пф)", "KRP(КРП)", "Sanoat(ПРОМ)", String.valueOf(year));
			akrPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Yarim ochiq vagon(пв)", "KRP(КРП)", "Sanoat(ПРОМ)", String.valueOf(year));
			akrSisterna = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Sisterna(цс)", "KRP(КРП)", "Sanoat(ПРОМ)", String.valueOf(year));
			akrBoshqa = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-5", "Boshqa turdagi(проч)", "KRP(КРП)", "Sanoat(ПРОМ)", String.valueOf(year));
			akrHamma = akrKriti + akrPlatforma + akrPoluvagon + akrSisterna + akrBoshqa;

			model.addAttribute("akrHammaFalse", akrHamma);
			model.addAttribute("akrKritiFalse", akrKriti);
			model.addAttribute("akrPlatformaFalse", akrPlatforma);
			model.addAttribute("akrPoluvagonFalse", akrPoluvagon);
			model.addAttribute("akrSisternaFalse", akrSisterna);
			model.addAttribute("akBoshqaFalse", akrBoshqa);

			// samarqand KRP tamir hamma false vagonlar soni
			skrKriti = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Yopiq vagon (крыт)", "KRP(КРП)", "Sanoat(ПРОМ)", String.valueOf(year));
			skrPlatforma = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Platforma(пф)", "KRP(КРП)", "Sanoat(ПРОМ)", String.valueOf(year));
			skrPoluvagon = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Yarim ochiq vagon(пв)", "KRP(КРП)", "Sanoat(ПРОМ)", String.valueOf(year));
			skrSisterna = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Sisterna(цс)", "KRP(КРП)", "Sanoat(ПРОМ)", String.valueOf(year));
			skrBoshqa = vagonTayyorService.countByDepoNomiVagonTuriTamirTuriAndCountry("VCHD-6", "Boshqa turdagi(проч)", "KRP(КРП)", "Sanoat(ПРОМ)", String.valueOf(year));
			skrHamma = skrKriti + skrPlatforma + skrPoluvagon + skrSisterna + skrBoshqa;

			model.addAttribute("skrHammaFalse", skrHamma);
			model.addAttribute("skrKritiFalse", skrKriti);
			model.addAttribute("skrPlatformaFalse", skrPlatforma);
			model.addAttribute("skrPoluvagonFalse", skrPoluvagon);
			model.addAttribute("skrSisternaFalse", skrSisterna);
			model.addAttribute("skrBoshqaFalse", skrBoshqa);
// Krp itogo uchun
			uvtkrHamma = akrHamma + hkrHamma + skrHamma;
			uvtkrKriti = skrKriti + hkrKriti + akrKriti;
			uvtkrPlatforma = akrPlatforma + skrPlatforma + hkrPlatforma;
			uvtkrPoluvagon = akrPoluvagon + skrPoluvagon + hkrPoluvagon;
			uvtkrSisterna = akrSisterna + hkrSisterna + skrSisterna;
			uvtkrBoshqa = akrBoshqa + hkrBoshqa + skrBoshqa;

			model.addAttribute("uvtkrHammaFalse", uvtkrHamma);
			model.addAttribute("uvtkrKritiFalse", uvtkrKriti);
			model.addAttribute("uvtkrPlatformaFalse", uvtkrPlatforma);
			model.addAttribute("uvtkrPoluvagonFalse", uvtkrPoluvagon);
			model.addAttribute("uvtkrSisternaFalse", uvtkrSisterna);
			model.addAttribute("uvtkrBoshqaFalse", uvtkrBoshqa);
		}



		//yuklab olish uchun list
		List<Integer> vagonsToDownloadTable = new ArrayList<>();
//Depoli tamir
//		krTableM = new ArrayList<>();
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
		vagonsToDownloadTable.add(uvtdHamma);
//		vagonsToDownloadTable.add(uvtdHamma - DtHammaPlan);
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

//		drTableM = vagonsToDownloadTable;
//		vagonsToDownloadTable = new ArrayList<>();

//kapital tamir
//		krTableM = new ArrayList<>();
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
		vagonsToDownloadTable.add(uvtkHamma);
//		vagonsToDownloadTable.add(uvtkHamma - KtHammaPlan);
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

//		krTableM = vagonsToDownloadTable;
//		vagonsToDownloadTable = new ArrayList<>();
//krp
//		krpTableM = new ArrayList<>();
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
		vagonsToDownloadTable.add(uvtkrHamma);
//		vagonsToDownloadTable.add(uvtkrHamma - KrpHammaPlan);
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

//		krpTableM = vagonsToDownloadTable;
//		vagonsToDownloadTable = new ArrayList<>();
//
////yolovchi
//		yolovchiTableM = new ArrayList<>();
		vagonsToDownloadTable.add(AndjToYolovchiPlan);
		vagonsToDownloadTable.add(atYolovchi);
//		vagonsToDownloadTable.add(atYolovchi - AndjToYolovchiPlan);
		vagonsToDownloadTable.add(AndjDtYolovchiPlan);
		vagonsToDownloadTable.add(adYolovchi);
//		vagonsToDownloadTable.add(adYolovchi - AndjDtYolovchiPlan);
//		yolovchiTableM = vagonsToDownloadTable;

		vagonsToDownloadAllTableMonths = vagonsToDownloadTable;
	}

//Export Table date to PDF
//	//One month
	@PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@GetMapping("/vagons/createExcelTableBiznes")
	public void exportTableToExcel(HttpServletResponse response) throws IOException {
		vagonTayyorService.pdfFileTable(vagonsToDownloadAllTable, response);
	}

	//All month
	@PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@GetMapping("/vagons/createExcelBiznesAllMonth")
	public void exportTableToExcelMonth(HttpServletResponse response) throws IOException {
		vagonTayyorService.pdfFileTable(vagonsToDownloadAllTableMonths, response);
	}


//Export Table date to Excel
//	//One month
//	@PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
//	@GetMapping("/vagons/createExcelTableBiznes")
//	public void exportTableToExcel(HttpServletResponse response) throws IOException {
//		vagonTayyorService.exportTableToExcel(drTable, krTable, krpTable, yolovchiTable, response);
//	}
//
//	//All month
//	@PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
//	@GetMapping("/vagons/createExcelBiznesAllMonth")
//	public void exportTableToExcelMonth(Model model, HttpServletResponse response) throws IOException {
//		vagonTayyorService.exportTableToExcel(drTableM, krTableM, krpTableM, yolovchiTableM, response);
//	}

//MAIN
	@PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@GetMapping("/vagons/AllPlanTable")
	public String getAllPlan(Model model) {
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

	//yangi Plan kiritish
		model.addAttribute("planDto", new PlanBiznesDto());

	//yangi vagon kiritish uchun
		model.addAttribute("vagon", new VagonTayyor());

	//Bir Oylik listni toldirish uchun Oylik
		vagonList = vagonTayyorService.findAllByCreatedDate(oy + "-" + year);
		model.addAttribute("vagons", vagonList);

	//Jami Oylik listni toldirish uchun
		vagonListMonths = vagonTayyorService.findAllByCreatedDate(String.valueOf(year));
		model.addAttribute("wagons", vagonListMonths);

	// Tableni toldirish uchun
		getPlanFactForOneMonth(model, month, year);
		getPlanFactForAllMonth(model, month, year);

		return "AllPlanTable";
	}

//Save Plan
	@PreAuthorize(value = "hasAnyRole('DIRECTOR')")
	@PostMapping("/vagons/savePlan")
	public String savePlan(@ModelAttribute("planDto") PlanBiznesDto planDto, Model model) {
		ApiResponse apiResponse = vagonTayyorService.savePlan(planDto);
		model.addAttribute("message", apiResponse.getMessage());
		model.addAttribute("isSuccess", apiResponse.isSuccess());

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

		//yangi Plan kiritish
		model.addAttribute("planDto", new PlanBiznesDto());

		//yangi vagon kiritish uchun
		model.addAttribute("vagon", new VagonTayyor());

		//Bir Oylik listni toldirish uchun Oylik
		vagonList = vagonTayyorService.findAllByCreatedDate(oy + "-" + year);
		model.addAttribute("vagons", vagonList);

		//Jami Oylik listni toldirish uchun
		vagonListMonths = vagonTayyorService.findAllByCreatedDate(String.valueOf(year));
		model.addAttribute("wagons", vagonListMonths);

		// Tableni toldirish uchun
		getPlanFactForOneMonth(model, month, year);
		getPlanFactForAllMonth(model, month, year);

		return "AllPlanTable";
	}

//Save new wagon
	@PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@PostMapping("/vagons/saveTayyor")
	public String saveVagon(@ModelAttribute("vagon") VagonTayyor vagon, HttpServletRequest request, Model model) {
		if (request.isUserInRole("DIRECTOR")) {
			ApiResponse apiResponse = vagonTayyorService.saveVagon(vagon);
			model.addAttribute("message", apiResponse.getMessage());
			model.addAttribute("isSuccess", apiResponse.isSuccess());
		}else if (request.isUserInRole("SAM")) {
			ApiResponse apiResponse = vagonTayyorService.saveVagonSam(vagon);
			model.addAttribute("message", apiResponse.getMessage());
			model.addAttribute("isSuccess", apiResponse.isSuccess());
		}else if (request.isUserInRole("HAV")) {
			ApiResponse apiResponse = vagonTayyorService.saveVagonHav(vagon);
			model.addAttribute("message", apiResponse.getMessage());
			model.addAttribute("isSuccess", apiResponse.isSuccess());
		}else if (request.isUserInRole("ANDJ")) {
			ApiResponse apiResponse = vagonTayyorService.saveVagonAndj(vagon);
			model.addAttribute("message", apiResponse.getMessage());
			model.addAttribute("isSuccess", apiResponse.isSuccess());
		}
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

		//yangi Plan kiritish
		model.addAttribute("planDto", new PlanBiznesDto());

		//yangi vagon kiritish uchun
		model.addAttribute("vagon", new VagonTayyor());

		//Bir Oylik listni toldirish uchun Oylik
		vagonList = vagonTayyorService.findAllByCreatedDate(oy + "-" + year);
		model.addAttribute("vagons", vagonList);

		//Jami Oylik listni toldirish uchun
		vagonListMonths = vagonTayyorService.findAllByCreatedDate(String.valueOf(year));
		model.addAttribute("wagons", vagonListMonths);

		// Tableni toldirish uchun
		getPlanFactForOneMonth(model, month, year);
		getPlanFactForAllMonth(model, month, year);

		return "AllPlanTable";
	}

//Delete Wagon
	@PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@GetMapping("/vagons/deleteTayyor/{id}")
	public String deleteVagonForm(@PathVariable("id") Long id, HttpServletRequest request, Model model) throws NotFoundException {
		if (request.isUserInRole("DIRECTOR")) {
			ApiResponse apiResponse = vagonTayyorService.deleteVagonById(id);
			model.addAttribute("message", apiResponse.getMessage());
			model.addAttribute("isSuccess", apiResponse.isSuccess());
		}else if (request.isUserInRole("SAM")) {
			ApiResponse apiResponse = vagonTayyorService.deleteVagonSam(id);
			model.addAttribute("message", apiResponse.getMessage());
			model.addAttribute("isSuccess", apiResponse.isSuccess());
		}else if (request.isUserInRole("HAV")) {
			ApiResponse apiResponse = vagonTayyorService.deleteVagonHav(id);
			model.addAttribute("message", apiResponse.getMessage());
			model.addAttribute("isSuccess", apiResponse.isSuccess());
		}else if (request.isUserInRole("ANDJ")) {
			ApiResponse apiResponse = vagonTayyorService.deleteVagonAndj(id);
			model.addAttribute("message", apiResponse.getMessage());
			model.addAttribute("isSuccess", apiResponse.isSuccess());
		}
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

		//yangi Plan kiritish
		model.addAttribute("planDto", new PlanBiznesDto());

		//yangi vagon kiritish uchun

		model.addAttribute("vagon", new VagonTayyor());

		//Bir Oylik listni toldirish uchun Oylik
		vagonList = vagonTayyorService.findAllByCreatedDate(oy + "-" + year);
		model.addAttribute("vagons", vagonList);

		//Jami Oylik listni toldirish uchun
		vagonListMonths = vagonTayyorService.findAllByCreatedDate(String.valueOf(year));
		model.addAttribute("wagons", vagonListMonths);

		// Tableni toldirish uchun
		getPlanFactForOneMonth(model, month, year);
		getPlanFactForAllMonth(model, month, year);

		return "AllPlanTable";
	}

//Search by number
	@PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@GetMapping("/vagons/searchTayyor")
	public String searchByNomer(Model model,  @RequestParam(value = "nomer", required = false) Integer nomer) {
	
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
	
		if(nomer==null  ) {
			//Bir Oylik listni toldirish uchun Oylik
			vagonList = vagonTayyorService.findAllByCreatedDate(oy + "-" + year);
			model.addAttribute("vagons", vagonList);

			//Jami Oylik listni toldirish uchun
			vagonListMonths = vagonTayyorService.findAllByCreatedDate(String.valueOf(year));
			model.addAttribute("wagons", vagonListMonths);
		}else {
			List<VagonTayyor> emptyList = new ArrayList<>();
			vagonList = emptyList;
			vagonListMonths = emptyList;

		//bir oylik vagonlar ichidan qidiradi (Jamini ichidan qidirganligi uchun commentga olingan)
//			VagonTayyor vagonTayyor = vagonTayyorService.searchByNomer(nomer, oy + "-" + year);
//			vagonList.add(vagonTayyor);

			VagonTayyor vagonTayyor1 = vagonTayyorService.searchByNomer(nomer, String.valueOf(year));
			vagonListMonths.add(vagonTayyor1);
		//Jami tamirlangan vagonlar ichidan qidirib bir oylikka ham shuni chiqarada
			vagonList.add(vagonTayyor1);

			//Bir Oylik listni toldirish uchun Oylik
			model.addAttribute("vagons", new HashSet<>(vagonList));

			//Jami Oylik listni toldirish uchun
			model.addAttribute("wagons", new HashSet<>(vagonListMonths));
		}
		//yangi Plan kiritish
		model.addAttribute("planDto", new PlanBiznesDto());

		//yangi vagon kiritish uchun
		model.addAttribute("vagon", new VagonTayyor());

		// Tableni toldirish uchun
		getPlanFactForOneMonth(model, month, year);
		getPlanFactForAllMonth(model, month, year);
	
		return "AllPlanTable";
	}

//Filter
	@PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@GetMapping("/vagons/filterOneMonth")
	public String filterByDepoNomi(Model model,  @RequestParam(value = "depoNomi", required = false) String depoNomi,
								   @RequestParam(value = "vagonTuri", required = false) String vagonTuri,
								   @RequestParam(value = "country", required = false) String country) {
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

		if(!depoNomi.equalsIgnoreCase("Hammasi") && !vagonTuri.equalsIgnoreCase("Hammasi") && !country.equalsIgnoreCase("Hammasi") ) {

			//Bir Oylik listni toldirish uchun Oylik
			vagonList = vagonTayyorService.findAllByDepoNomiVagonTuriAndCountry(depoNomi, vagonTuri, country, oy + "-" + year);
			model.addAttribute("vagons", vagonList);

			//Jami Oylik listni toldirish uchun
			vagonListMonths = vagonTayyorService.findAllByDepoNomiVagonTuriAndCountry(depoNomi, vagonTuri, country, String.valueOf(year));
			model.addAttribute("wagons", vagonListMonths);

		}else if(depoNomi.equalsIgnoreCase("Hammasi") && !vagonTuri.equalsIgnoreCase("Hammasi") && !country.equalsIgnoreCase("Hammasi")){

			//Bir Oylik listni toldirish uchun Oylik
			vagonList = vagonTayyorService.findAllByVagonTuriAndCountry(vagonTuri , country, oy + "-" + year);
			model.addAttribute("vagons", vagonList);

			//Jami Oylik listni toldirish uchun
			vagonListMonths = vagonTayyorService.findAllByVagonTuriAndCountry(vagonTuri , country, String.valueOf(year));
			model.addAttribute("wagons", vagonListMonths);

		}else if(depoNomi.equalsIgnoreCase("Hammasi") && vagonTuri.equalsIgnoreCase("Hammasi")&& !country.equalsIgnoreCase("Hammasi")){

			//Bir Oylik listni toldirish uchun Oylik
			vagonList = vagonTayyorService.findAllBycountry(country, oy + "-" + year );
			model.addAttribute("vagons", vagonList);

			//Jami Oylik listni toldirish uchun
			vagonListMonths = vagonTayyorService.findAllBycountry(country, String.valueOf(year));
			model.addAttribute("wagons", vagonListMonths);

		}else if(!depoNomi.equalsIgnoreCase("Hammasi") && !vagonTuri.equalsIgnoreCase("Hammasi") && country.equalsIgnoreCase("Hammasi")){

			//Bir Oylik listni toldirish uchun Oylik
			vagonList = vagonTayyorService.findAllByDepoNomiAndVagonTuri(depoNomi, vagonTuri, oy + "-" + year);
			model.addAttribute("vagons", vagonList);

			//Jami Oylik listni toldirish uchun
			vagonListMonths = vagonTayyorService.findAllByDepoNomiAndVagonTuri(depoNomi, vagonTuri, String.valueOf(year));
			model.addAttribute("wagons", vagonListMonths);

		}else if(!depoNomi.equalsIgnoreCase("Hammasi") && vagonTuri.equalsIgnoreCase("Hammasi") && !country.equalsIgnoreCase("Hammasi")){

			//Bir Oylik listni toldirish uchun Oylik
			vagonList = vagonTayyorService.findAllByDepoNomiAndCountry(depoNomi, country, oy + "-" + year);
			model.addAttribute("vagons", vagonList);

			//Jami Oylik listni toldirish uchun
			vagonListMonths = vagonTayyorService.findAllByDepoNomiAndCountry(depoNomi, country, String.valueOf(year));
			model.addAttribute("wagons", vagonListMonths);

		}else if(depoNomi.equalsIgnoreCase("Hammasi") && !vagonTuri.equalsIgnoreCase("Hammasi") && country.equalsIgnoreCase("Hammasi")){

			//Bir Oylik listni toldirish uchun Oylik
			vagonList = vagonTayyorService.findAllByVagonTuri(vagonTuri, oy + "-" + year);
			model.addAttribute("vagons", vagonList);

			//Jami Oylik listni toldirish uchun
			vagonListMonths = vagonTayyorService.findAllByVagonTuri(vagonTuri, String.valueOf(year));
			model.addAttribute("wagons", vagonListMonths);

		}else if(!depoNomi.equalsIgnoreCase("Hammasi") && vagonTuri.equalsIgnoreCase("Hammasi") && country.equalsIgnoreCase("Hammasi")){

			//Bir Oylik listni toldirish uchun Oylik
			vagonList = vagonTayyorService.findAllByDepoNomi(depoNomi, oy + "-" + year);
			model.addAttribute("vagons", vagonList);

			//Jami Oylik listni toldirish uchun
			vagonListMonths = vagonTayyorService.findAllByDepoNomi(depoNomi, String.valueOf(year));
			model.addAttribute("wagons", vagonListMonths);

		}else {
			//Bir Oylik listni toldirish uchun Oylik
			vagonList = vagonTayyorService.findAllByCreatedDate(oy + "-" + year);
			model.addAttribute("vagons", vagonList);

			//Jami Oylik listni toldirish uchun
			vagonListMonths = vagonTayyorService.findAllByCreatedDate(String.valueOf(year));
			model.addAttribute("wagons", vagonListMonths);
		}

		//yangi Plan kiritish
		model.addAttribute("planDto", new PlanBiznesDto());

		//yangi vagon kiritish uchun
		model.addAttribute("vagon", new VagonTayyor());

		// Tableni toldirish uchun
		getPlanFactForOneMonthInFilter(model, month, year, country);
		getPlanFactForAllMonthInFilter(model, month, year, country);

		return "AllPlanTable";
	}























    //Tayyor yangi vagon qoshish uchun oyna
    @PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@GetMapping("/vagons/newTayyor")
	public String createVagonForm(Model model) {
		VagonTayyor vagonTayyor = new VagonTayyor();
		model.addAttribute("vagon", vagonTayyor);
		return "create_tayyorvagon";
	}

    //tahrirlash uchun oyna
    @PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@GetMapping("/vagons/editTayyor/{id}")
	public String editVagonForm(@PathVariable("id") Long id, Model model) {
		model.addAttribute("vagon",vagonTayyorService.getVagonById(id));
		return "edit_tayyorvagon";
	}


    //tahrirlash jami oylar uchun
  //tahrirlash uchun oyna
//    @PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
//	@GetMapping("/vagons/editTayyor/{id}")
//	public String editVagonForm(@PathVariable("id") Long id, Model model) {
//		model.addAttribute("vagon",vagonTayyorService.getVagonById(id));
//		return "edit_tayyorvagon";
//	}

    //tahrirni saqlash
    @PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@PostMapping("/vagons/updateTayyor/{id}")
	public String updateVagon(@ModelAttribute("vagon") VagonTayyor vagon, @PathVariable Long id, HttpServletRequest request) throws NotFoundException {
    	if (request.isUserInRole("DIRECTOR")) {
    		vagonTayyorService.updateVagon(vagon, id);
        }else if (request.isUserInRole("SAM")) {
        	vagonTayyorService.updateVagonSam(vagon, id);
        }else if (request.isUserInRole("HAV")) {
        	vagonTayyorService.updateVagonHav(vagon, id);
        }else if (request.isUserInRole("ANDJ")) {
        	vagonTayyorService.updateVagonAndj(vagon, id);
        }
    	return "redirect:/vagons/planTableFor";
	}


    // wagon nomer orqali qidirish shu oygacha hammasidan
    @PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@GetMapping("/vagons/searchTayyorAll")
	public String search(Model model,  @RequestParam(value = "nomer", required = false) Integer nomer) {
		if(nomer==null  ) {
			model.addAttribute("vagons", vagonTayyorService.findAll());
			vagonList= vagonTayyorService.findAll();
		}else {
			model.addAttribute("vagons", vagonTayyorService.findByNomer(nomer));
			List<VagonTayyor> emptyList = new ArrayList<>();
			vagonList= emptyList;
			vagonList.add( vagonTayyorService.findByNomer(nomer));
		}
		PlanBiznes planDto = vagonTayyorService.getPlanBiznes("","");
		//planlar kiritish

		//havos depo tamir hamma plan
		int HavDtKritiPlan = planDto.getHavDtKritiPlanBiznes();
		int HavDtPlatformaPlan = planDto.getHavDtPlatformaPlanBiznes();
		int HavDtPoluvagonPlan = planDto.getHavDtPoluvagonPlanBiznes();
		int HavDtSisternaPlan = planDto.getHavDtSisternaPlanBiznes();
		int HavDtBoshqaPlan = planDto.getHavDtBoshqaPlanBiznes();
		int HavDtHammaPlan = HavDtKritiPlan + HavDtPlatformaPlan + HavDtPoluvagonPlan + HavDtSisternaPlan + HavDtBoshqaPlan;

		model.addAttribute("HavDtHammaPlan", HavDtHammaPlan);
		model.addAttribute("HavDtKritiPlan", HavDtKritiPlan);
		model.addAttribute("HavDtPlatformaPlan", HavDtPlatformaPlan);
		model.addAttribute("HavDtPoluvagonPlan", HavDtPoluvagonPlan);
		model.addAttribute("HavDtSisternaPlan", HavDtSisternaPlan);
		model.addAttribute("HavDtBoshqaPlan", HavDtBoshqaPlan);


		//VCHD-5 depo tamir plan
		int AndjDtKritiPlan = planDto.getAndjDtKritiPlanBiznes();
		int AndjDtPlatformaPlan =planDto.getAndjDtPlatformaPlanBiznes();
		int AndjDtPoluvagonPlan =planDto.getAndjDtPoluvagonPlanBiznes();
		int AndjDtSisternaPlan =planDto.getAndjDtSisternaPlanBiznes();
		int AndjDtBoshqaPlan=planDto.getAndjDtBoshqaPlanBiznes();
		int AndjDtHammaPlan = AndjDtKritiPlan + AndjDtPlatformaPlan + AndjDtPoluvagonPlan + AndjDtSisternaPlan + AndjDtBoshqaPlan;

		model.addAttribute("AndjDtHammaPlan", AndjDtHammaPlan);
		model.addAttribute("AndjDtKritiPlan", AndjDtKritiPlan);
		model.addAttribute("AndjDtPlatformaPlan",AndjDtPlatformaPlan);
		model.addAttribute("AndjDtPoluvagonPlan", AndjDtPoluvagonPlan);
		model.addAttribute("AndjDtSisternaPlan", AndjDtSisternaPlan);
		model.addAttribute("AndjDtBoshqaPlan", AndjDtBoshqaPlan);

		//samarqand depo tamir plan
		int SamDtKritiPlan = planDto.getSamDtKritiPlanBiznes();
		int SamDtPlatformaPlan = planDto.getSamDtPlatformaPlanBiznes();
		int SamDtPoluvagonPlan =  planDto.getSamDtPoluvagonPlanBiznes();
		int SamDtSisternaPlan = planDto.getSamDtSisternaPlanBiznes();
		int SamDtBoshqaPlan = planDto.getSamDtBoshqaPlanBiznes();
		int SamDtHammaPlan=SamDtKritiPlan + SamDtPlatformaPlan + SamDtPoluvagonPlan + SamDtSisternaPlan + SamDtBoshqaPlan;

		model.addAttribute("SamDtHammaPlan",SamDtHammaPlan);
		model.addAttribute("SamDtKritiPlan", SamDtKritiPlan);
		model.addAttribute("SamDtPlatformaPlan", SamDtPlatformaPlan);
		model.addAttribute("SamDtPoluvagonPlan", SamDtPoluvagonPlan);
		model.addAttribute("SamDtSisternaPlan", SamDtSisternaPlan);
		model.addAttribute("SamDtBoshqaPlan", SamDtBoshqaPlan);


		// Itogo planlar depo tamir
		int DtHammaPlan = AndjDtHammaPlan + HavDtHammaPlan + SamDtHammaPlan;
		int DtKritiPlan = SamDtKritiPlan + HavDtKritiPlan + AndjDtKritiPlan;
		int DtPlatformaPlan = SamDtPlatformaPlan + HavDtPlatformaPlan + AndjDtPlatformaPlan;
		int DtPoluvagonPlan = SamDtPoluvagonPlan + HavDtPoluvagonPlan + AndjDtPoluvagonPlan;
		int DtSisternaPlan = SamDtSisternaPlan + HavDtSisternaPlan + AndjDtSisternaPlan;
		int DtBoshqaPlan = SamDtBoshqaPlan + HavDtBoshqaPlan + AndjDtBoshqaPlan;

		model.addAttribute("DtHammaPlan", DtHammaPlan);
		model.addAttribute("DtKritiPlan", DtKritiPlan);
		model.addAttribute("DtPlatformaPlan", DtPlatformaPlan);
		model.addAttribute("DtPoluvagonPlan",DtPoluvagonPlan);
		model.addAttribute("DtSisternaPlan", DtSisternaPlan);
		model.addAttribute("DtBoshqaPlan", DtBoshqaPlan);


		//Yolovchi vagon Plan
		int AndjToYolovchiPlan = planDto.getAndjTYolovchiPlanBiznes();
		int AndjDtYolovchiPlan = planDto.getAndjDtYolovchiPlanBiznes();

		model.addAttribute("AndjToYolovchiPlan", AndjToYolovchiPlan);
		model.addAttribute("AndjDtYolovchiPlan", AndjDtYolovchiPlan);

		//hovos kapital plan
		int HavKtKritiPlan = planDto.getHavKtKritiPlanBiznes();
		int HavKtPlatformaPlan = planDto.getHavKtPlatformaPlanBiznes();
		int HavKtPoluvagonPlan = planDto.getHavKtPoluvagonPlanBiznes();
		int HavKtSisternaPlan = planDto.getHavKtSisternaPlanBiznes();
		int HavKtBoshqaPlan = planDto.getHavKtBoshqaPlanBiznes();
		int HavKtHammaPlan = HavKtKritiPlan + HavKtPlatformaPlan + HavKtPoluvagonPlan + HavKtSisternaPlan + HavKtBoshqaPlan;

		model.addAttribute("HavKtHammaPlan", HavKtHammaPlan);
		model.addAttribute("HavKtKritiPlan", HavKtKritiPlan);
		model.addAttribute("HavKtPlatformaPlan", HavKtPlatformaPlan);
		model.addAttribute("HavKtPoluvagonPlan", HavKtPoluvagonPlan);
		model.addAttribute("HavKtSisternaPlan", HavKtSisternaPlan);
		model.addAttribute("HavKtBoshqaPlan", HavKtBoshqaPlan);

		//ANDIJON kapital plan
		int AndjKtKritiPlan = planDto.getAndjKtKritiPlanBiznes();
		int AndjKtPlatformaPlan=planDto.getAndjKtPlatformaPlanBiznes();
		int AndjKtPoluvagonPlan=planDto.getAndjKtPoluvagonPlanBiznes();
		int AndjKtSisternaPlan=planDto.getAndjKtSisternaPlanBiznes();
		int AndjKtBoshqaPlan=planDto.getAndjKtBoshqaPlanBiznes();
		int AndjKtHammaPlan = AndjKtKritiPlan + AndjKtPlatformaPlan + AndjKtPoluvagonPlan + AndjKtSisternaPlan + AndjKtBoshqaPlan;

		model.addAttribute("AndjKtHammaPlan", AndjKtHammaPlan);
		model.addAttribute("AndjKtKritiPlan", AndjKtKritiPlan);
		model.addAttribute("AndjKtPlatformaPlan", AndjKtPlatformaPlan);
		model.addAttribute("AndjKtPoluvagonPlan", AndjKtPoluvagonPlan);
		model.addAttribute("AndjKtSisternaPlan", AndjKtSisternaPlan);
		model.addAttribute("AndjKtBoshqaPlan", AndjKtBoshqaPlan);

		//Samrqand kapital plan
		int SamKtKritiPlan = planDto.getSamKtKritiPlanBiznes();
		int SamKtPlatformaPlan = planDto.getSamKtPlatformaPlanBiznes();
		int SamKtPoluvagonPlan = planDto.getSamKtPoluvagonPlanBiznes();
		int SamKtSisternaPlan = planDto.getSamKtSisternaPlanBiznes();
		int SamKtBoshqaPlan = planDto.getSamKtBoshqaPlanBiznes();
		int SamKtHammaPlan = SamKtKritiPlan + SamKtPlatformaPlan + SamKtPoluvagonPlan + SamKtSisternaPlan +SamKtBoshqaPlan;

		model.addAttribute("SamKtHammaPlan",SamKtHammaPlan);
		model.addAttribute("SamKtKritiPlan", SamKtKritiPlan);
		model.addAttribute("SamKtPlatformaPlan", SamKtPlatformaPlan);
		model.addAttribute("SamKtPoluvagonPlan", SamKtPoluvagonPlan);
		model.addAttribute("SamKtSisternaPlan", SamKtSisternaPlan);
		model.addAttribute("SamKtBoshqaPlan", SamKtBoshqaPlan);


		//Itogo kapital plan
		int KtHammaPlan = AndjKtHammaPlan + HavKtHammaPlan + SamKtHammaPlan;
		int KtKritiPlan = SamKtKritiPlan + HavKtKritiPlan + AndjKtKritiPlan;
		int KtPlatformaPlan = SamKtPlatformaPlan + HavKtPlatformaPlan + AndjKtPlatformaPlan;
		int KtPoluvagonPlan = SamKtPoluvagonPlan + HavKtPoluvagonPlan + AndjKtPoluvagonPlan;
		int KtSisternaPlan = SamKtSisternaPlan + HavKtSisternaPlan + AndjKtSisternaPlan;
		int KtBoshqaPlan = SamKtBoshqaPlan + HavKtBoshqaPlan + AndjKtBoshqaPlan;

		model.addAttribute("KtHammaPlan", KtHammaPlan);
		model.addAttribute("KtKritiPlan", KtKritiPlan);
		model.addAttribute("KtPlatformaPlan", KtPlatformaPlan);
		model.addAttribute("KtPoluvagonPlan",KtPoluvagonPlan);
		model.addAttribute("KtSisternaPlan", KtSisternaPlan);
		model.addAttribute("KtBoshqaPlan", KtBoshqaPlan);


		//Hovos krp plan
		int HavKrpKritiPlan = planDto.getHavKrpKritiPlanBiznes();
		int HavKrpPlatformaPlan = planDto.getHavKrpPlatformaPlanBiznes();
		int HavKrpPoluvagonPlan = planDto.getHavKrpPoluvagonPlanBiznes();
		int HavKrpSisternaPlan = planDto.getHavKrpSisternaPlanBiznes();
		int HavKrpBoshqaPlan = planDto.getHavKrpBoshqaPlanBiznes();
		int HavKrpHammaPlan = HavKrpKritiPlan + HavKrpPlatformaPlan + HavKrpPoluvagonPlan + HavKrpSisternaPlan + HavKrpBoshqaPlan;

		model.addAttribute("HavKrpHammaPlan",HavKrpHammaPlan);
		model.addAttribute("HavKrpKritiPlan", HavKrpKritiPlan);
		model.addAttribute("HavKrpPlatformaPlan", HavKrpPlatformaPlan);
		model.addAttribute("HavKrpPoluvagonPlan", HavKrpPoluvagonPlan);
		model.addAttribute("HavKrpSisternaPlan", HavKrpSisternaPlan);
		model.addAttribute("HavKrpBoshqaPlan", HavKrpBoshqaPlan);

		//andijon krp plan
		int AndjKrpKritiPlan = planDto.getAndjKrpKritiPlanBiznes();
		int AndjKrpPlatformaPlan = planDto.getAndjKrpPlatformaPlanBiznes();
		int AndjKrpPoluvagonPlan = planDto.getAndjKrpPoluvagonPlanBiznes();
		int AndjKrpSisternaPlan = planDto.getAndjKrpSisternaPlanBiznes();
		int AndjKrpBoshqaPlan = planDto.getAndjKrpBoshqaPlanBiznes();
		int AndjKrpHammaPlan = AndjKrpKritiPlan + AndjKrpPlatformaPlan + AndjKrpPoluvagonPlan + AndjKrpSisternaPlan + AndjKrpBoshqaPlan;

		model.addAttribute("AndjKrpHammaPlan",AndjKrpHammaPlan);
		model.addAttribute("AndjKrpKritiPlan", AndjKrpKritiPlan);
		model.addAttribute("AndjKrpPlatformaPlan", AndjKrpPlatformaPlan);
		model.addAttribute("AndjKrpPoluvagonPlan", AndjKrpPoluvagonPlan);
		model.addAttribute("AndjKrpSisternaPlan", AndjKrpSisternaPlan);
		model.addAttribute("AndjKrpBoshqaPlan", AndjKrpBoshqaPlan);

		//Samarqankr Krp plan
		int SamKrpKritiPlan = planDto.getSamKrpKritiPlanBiznes();
		int SamKrpPlatformaPlan = planDto.getSamKrpPlatformaPlanBiznes();
		int SamKrpPoluvagonPlan = planDto.getSamKrpPoluvagonPlanBiznes();
		int SamKrpSisternaPlan = planDto.getSamKrpSisternaPlanBiznes();
		int SamKrpBoshqaPlan = planDto.getSamKrpBoshqaPlanBiznes();
		int SamKrpHammaPlan = SamKrpKritiPlan + SamKrpPlatformaPlan + SamKrpPoluvagonPlan + SamKrpSisternaPlan + SamKrpBoshqaPlan;

		model.addAttribute("SamKrpHammaPlan", SamKrpHammaPlan);
		model.addAttribute("SamKrpKritiPlan", SamKrpKritiPlan);
		model.addAttribute("SamKrpPlatformaPlan", SamKrpPlatformaPlan);
		model.addAttribute("SamKrpPoluvagonPlan", SamKrpPoluvagonPlan);
		model.addAttribute("SamKrpSisternaPlan", SamKrpSisternaPlan);
		model.addAttribute("SamKrpBoshqaPlan", SamKrpBoshqaPlan);


		//itogo krp

		int KrpHammaPlan = AndjKrpHammaPlan + HavKrpHammaPlan + SamKrpHammaPlan;
		int KrpKritiPlan = SamKrpKritiPlan + HavKrpKritiPlan + AndjKrpKritiPlan;
		int KrpPlatformaPlan = SamKrpPlatformaPlan + HavKrpPlatformaPlan + AndjKrpPlatformaPlan;
		int KrpPoluvagonPlan = SamKrpPoluvagonPlan + HavKrpPoluvagonPlan + AndjKrpPoluvagonPlan;
		int KrpSisternaPlan = SamKrpSisternaPlan + HavKrpSisternaPlan + AndjKrpSisternaPlan;
		int KrpBoshqaPlan = SamKrpBoshqaPlan + HavKrpBoshqaPlan + AndjKrpBoshqaPlan;

		model.addAttribute("KrpHammaPlan", KrpHammaPlan);
		model.addAttribute("KrpKritiPlan", KrpKritiPlan);
		model.addAttribute("KrpPlatformaPlan", KrpPlatformaPlan);
		model.addAttribute("KrpPoluvagonPlan",KrpPoluvagonPlan);
		model.addAttribute("KrpSisternaPlan", KrpSisternaPlan);
		model.addAttribute("KrpBoshqaPlan", KrpBoshqaPlan);

		

		return "planTableFor";
    }


    @PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
   	@GetMapping("/vagons/filterAllMonth")
   	public String filterByDepoNomiForAll(Model model,  @RequestParam(value = "depoNomi", required = false) String depoNomi,
   												@RequestParam(value = "vagonTuri", required = false) String vagonTuri,
   												@RequestParam(value = "country", required = false) String country) {
   		if(!depoNomi.equalsIgnoreCase("Hammasi") && !vagonTuri.equalsIgnoreCase("Hammasi") && !country.equalsIgnoreCase("Hammasi") ) {
   			vagonList= vagonTayyorService.findByDepoNomiVagonTuriAndCountry(depoNomi, vagonTuri, country);
   			model.addAttribute("vagons", vagonTayyorService.findByDepoNomiVagonTuriAndCountry(depoNomi, vagonTuri, country));
   		}else if(depoNomi.equalsIgnoreCase("Hammasi") && !vagonTuri.equalsIgnoreCase("Hammasi") && !country.equalsIgnoreCase("Hammasi")){
   			vagonList= vagonTayyorService.findByVagonTuriAndCountry(vagonTuri , country);
   			model.addAttribute("vagons", vagonTayyorService.findByVagonTuriAndCountry(vagonTuri , country));
   		}else if(depoNomi.equalsIgnoreCase("Hammasi") && vagonTuri.equalsIgnoreCase("Hammasi")&& !country.equalsIgnoreCase("Hammasi")){
   			vagonList= vagonTayyorService.findBycountry(country );
   			model.addAttribute("vagons", vagonTayyorService.findBycountry(country ));
   		}else if(!depoNomi.equalsIgnoreCase("Hammasi") && !vagonTuri.equalsIgnoreCase("Hammasi") && country.equalsIgnoreCase("Hammasi")){
   			vagonList= vagonTayyorService.findByDepoNomiAndVagonTuri(depoNomi, vagonTuri);
   			model.addAttribute("vagons", vagonTayyorService.findByDepoNomiAndVagonTuri(depoNomi, vagonTuri));
   		}else if(!depoNomi.equalsIgnoreCase("Hammasi") && vagonTuri.equalsIgnoreCase("Hammasi") && !country.equalsIgnoreCase("Hammasi")){
   			vagonList= vagonTayyorService.findByDepoNomiAndCountry(depoNomi, country);
   			model.addAttribute("vagons", vagonTayyorService.findByDepoNomiAndCountry(depoNomi, country));
   		}else if(depoNomi.equalsIgnoreCase("Hammasi") && !vagonTuri.equalsIgnoreCase("Hammasi") && country.equalsIgnoreCase("Hammasi")){
   			vagonList= vagonTayyorService.findByVagonTuri(vagonTuri);
   			model.addAttribute("vagons", vagonTayyorService.findByVagonTuri(vagonTuri));
   		}else if(!depoNomi.equalsIgnoreCase("Hammasi") && vagonTuri.equalsIgnoreCase("Hammasi") && country.equalsIgnoreCase("Hammasi")){
   			vagonList= vagonTayyorService.findByDepoNomi(depoNomi );
   			model.addAttribute("vagons", vagonTayyorService.findByDepoNomi(depoNomi ));
   		}else {
   			vagonList= vagonTayyorService.findAll( );
   			model.addAttribute("vagons", vagonTayyorService.findAll());
   		}


		return "planTableFor";
    }

}
