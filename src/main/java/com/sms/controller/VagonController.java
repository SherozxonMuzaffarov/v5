package com.sms.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.sms.payload.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.sms.dto.VagonDto;
import com.sms.model.VagonModel;
import com.sms.service.VagonService;
@Controller
public class VagonController {

	@Autowired
	private VagonService vagonService;

	//Yuklab olish uchun Malumot yigib beradi
	List<VagonModel> vagonsToDownload  = new ArrayList<>();
	List<Integer> vagonsToDownloadTables  = new ArrayList<>();

	public void getPlanFact(Model model){

		// to create new wagon
		model.addAttribute("vagon", new VagonDto());


		List<Integer> vagonsToDownloadTable  = new ArrayList<>();

		//Kritiylarni  hammasini olish
		int samKritiy =  vagonService.getVagonsCount("Yopiq vagon (крыт)","VCHD-6");
		int havKritiy =  vagonService.getVagonsCount("Yopiq vagon (крыт)","VCHD-3");
		int andjKritiy =  vagonService.getVagonsCount("Yopiq vagon (крыт)","VCHD-5");

		model.addAttribute("havKritiy", havKritiy);
		model.addAttribute("andjKritiy", andjKritiy);
		model.addAttribute("samKritiy",samKritiy);

		vagonsToDownloadTable.add(havKritiy);
		vagonsToDownloadTable.add(andjKritiy);
		vagonsToDownloadTable.add(samKritiy);
		vagonsToDownloadTable.add(samKritiy + havKritiy + andjKritiy);

		//Platformalarni  hammasini olish
		int samPlatforma =  vagonService.getVagonsCount("Platforma(пф)","VCHD-6");
		int havPlatforma =  vagonService.getVagonsCount("Platforma(пф)","VCHD-3");
		int andjPlatforma =  vagonService.getVagonsCount("Platforma(пф)","VCHD-5");

		model.addAttribute("samPlatforma",samPlatforma);
		model.addAttribute("havPlatforma", havPlatforma);
		model.addAttribute("andjPlatforma", andjPlatforma);

		vagonsToDownloadTable.add(havPlatforma);
		vagonsToDownloadTable.add(andjPlatforma);
		vagonsToDownloadTable.add(samPlatforma);
		vagonsToDownloadTable.add(havPlatforma + andjPlatforma + samPlatforma);

		//Poluvagonlarni  hammasini olish
		int samPoluvagon =  vagonService.getVagonsCount("Yarim ochiq vagon(пв)","VCHD-6");
		int havPoluvagon =  vagonService.getVagonsCount("Yarim ochiq vagon(пв)","VCHD-3");
		int andjPoluvagon =  vagonService.getVagonsCount("Yarim ochiq vagon(пв)","VCHD-5");

		model.addAttribute("samPoluvagon", samPoluvagon);
		model.addAttribute("havPoluvagon", havPoluvagon);
		model.addAttribute("andjPoluvagon", andjPoluvagon);

		vagonsToDownloadTable.add(havPoluvagon);
		vagonsToDownloadTable.add(andjPoluvagon);
		vagonsToDownloadTable.add(samPoluvagon);
		vagonsToDownloadTable.add(havPoluvagon + andjPoluvagon + samPoluvagon);

		//Tsisternalarni  hammasini olish
		int samTsisterna = vagonService.getVagonsCount("Sisterna(цс)","VCHD-6");
		int havTsisterna = vagonService.getVagonsCount("Sisterna(цс)","VCHD-3");
		int andjTsisterna = vagonService.getVagonsCount("Sisterna(цс)","VCHD-5");

		model.addAttribute("samTsisterna", samTsisterna);
		model.addAttribute("havTsisterna", havTsisterna);
		model.addAttribute("andjTsisterna", andjTsisterna);

		vagonsToDownloadTable.add(havTsisterna);
		vagonsToDownloadTable.add(andjTsisterna);
		vagonsToDownloadTable.add(samTsisterna);
		vagonsToDownloadTable.add(havTsisterna + andjTsisterna + samTsisterna);

		//Boshqalarni  hammasini olish
		int samBoshqa = vagonService.getVagonsCount("Boshqa turdagi(проч)","VCHD-6");
		int havBoshqa = vagonService.getVagonsCount("Boshqa turdagi(проч)","VCHD-3");
		int andjBoshqa = vagonService.getVagonsCount("Boshqa turdagi(проч)","VCHD-5");

		model.addAttribute("samBoshqa", samBoshqa);
		model.addAttribute("havBoshqa", havBoshqa);
		model.addAttribute("andjBoshqa", andjBoshqa);

		vagonsToDownloadTable.add(havBoshqa);
		vagonsToDownloadTable.add(andjBoshqa);
		vagonsToDownloadTable.add(samBoshqa);
		vagonsToDownloadTable.add(havBoshqa + andjBoshqa + samBoshqa);

		// Jaminini olish
		int hammasi = vagonService.getCount("VCHD-6") + vagonService.getCount("VCHD-3") + vagonService.getCount("VCHD-5");
		int sam = vagonService.getCount("VCHD-6") ;
		int hav = vagonService.getCount("VCHD-3") ;
		int andj =  vagonService.getCount("VCHD-5");

		model.addAttribute("hammasi", hammasi);
		model.addAttribute("sam", sam);
		model.addAttribute("hav", hav);
		model.addAttribute("andj", andj);

		vagonsToDownloadTable.add(hav);
		vagonsToDownloadTable.add(andj);
		vagonsToDownloadTable.add(sam);
		vagonsToDownloadTable.add(hammasi);

//		Vaqtni olib turadi
		model.addAttribute("samDate",vagonService.getSamDate());
		model.addAttribute("havDate", vagonService.getHavDate());
		model.addAttribute("andjDate",vagonService.getAndjDate());

		vagonsToDownloadTables = vagonsToDownloadTable;

	}

	@PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@GetMapping("/vagons/createExcel")
	public void pdfFile(HttpServletResponse response) throws IOException {
		vagonService.createPdf(vagonsToDownload,response);
	}

	@PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@GetMapping("/vagons/createTableExcel")
	public void pdfTableFile(HttpServletResponse response) throws IOException {
		vagonService.pdfTableFile(vagonsToDownloadTables,response);
	}

////Export to Excel
	//Table
//	@GetMapping("/vagons/createTableExcel")
//	public void exportTableToExcel(HttpServletResponse response) throws IOException {
//		vagonService.exportTableToExcel(vagonsToDownloadTables,response);
//	}
//	//List
//	@GetMapping("/vagons/createExcel")
//	public void exportListToExcel(HttpServletResponse response) throws IOException {
//		vagonService.exportToExcel(vagonsToDownload,response);
//
//	}

//Main
	@PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@GetMapping("")
	public String main(Model model) {
		vagonsToDownload = vagonService.findAll();
		model.addAttribute("vagons", vagonsToDownload);
		getPlanFact(model);
		return "vagons";
	}
	@PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@GetMapping("/vagons")
	public String listVagon(Model model) {

		vagonsToDownload = vagonService.findAll();
		model.addAttribute("vagons", vagonsToDownload);

		getPlanFact(model);
		return "vagons";
	}

//Save wagon
	@PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@PostMapping("/vagons")
	public String saveVagon(@ModelAttribute("vagon") @Valid VagonDto vagon, HttpServletRequest request, Model model ) {

		if (request.isUserInRole("DIRECTOR")) {
			ApiResponse apiResponse = vagonService.saveVagon(vagon);
			model.addAttribute("message", apiResponse.getMessage());
			model.addAttribute("isSuccess", apiResponse.isSuccess());
		}else if (request.isUserInRole("SAM")) {
			ApiResponse apiResponse = vagonService.saveVagonSam(vagon);
			model.addAttribute("message", apiResponse.getMessage());
			model.addAttribute("isSuccess", apiResponse.isSuccess());
		}else if (request.isUserInRole("HAV")) {
			ApiResponse apiResponse = vagonService.saveVagonHav(vagon);
			model.addAttribute("message", apiResponse.getMessage());
			model.addAttribute("isSuccess", apiResponse.isSuccess());
		}else if (request.isUserInRole("ANDJ")) {
			ApiResponse apiResponse = vagonService.saveVagonAndj(vagon);
			model.addAttribute("message", apiResponse.getMessage());
			model.addAttribute("isSuccess", apiResponse.isSuccess());
		}

		vagonsToDownload = vagonService.findAll();
		model.addAttribute("vagons", vagonsToDownload);

		getPlanFact(model);

		return "vagons";
	}


//Delete
	@PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@GetMapping("/vagons/delete/{id}")
	public String deleteVagonForm(@PathVariable("id") Long id, HttpServletRequest request,Model model ) throws NotFoundException {
		VagonModel model1 = vagonService.findById(id);
		if (request.isUserInRole("DIRECTOR")) {
			ApiResponse apiResponse = vagonService.deleteVagonById(id);
			model.addAttribute("message", apiResponse.getMessage());
			model.addAttribute("isSuccess", apiResponse.isSuccess());
		}else if (request.isUserInRole("SAM")) {
			if(model1.getDepoNomi().equalsIgnoreCase("VCHD-6")){
				ApiResponse apiResponse = vagonService.deleteVagonSam(id);
				model.addAttribute("message", apiResponse.getMessage());
				model.addAttribute("isSuccess", apiResponse.isSuccess());
			}else {
				model.addAttribute("message", "Siz faqat VCHD-6 dagi vagon ma'lumotlarni o'chirishingiz mumkin");
				model.addAttribute("isSuccess", false);
			}
		}else if (request.isUserInRole("HAV")) {
			if(model1.getDepoNomi().equalsIgnoreCase("VCHD-3")){
				ApiResponse apiResponse = vagonService.deleteVagonHav(id);
				model.addAttribute("message", apiResponse.getMessage());
				model.addAttribute("isSuccess", apiResponse.isSuccess());
			}else {
				model.addAttribute("message", "Siz faqat VCHD-3 dagi vagon ma'lumotlarni o'chirishingiz mumkin");
				model.addAttribute("isSuccess", false);
			}
		}else if (request.isUserInRole("ANDJ")) {
			if(model1.getDepoNomi().equalsIgnoreCase("VCHD-5")){
				ApiResponse apiResponse = vagonService.deleteVagonAndj(id);
				model.addAttribute("message", apiResponse.getMessage());
				model.addAttribute("isSuccess", apiResponse.isSuccess());
			}else {
				model.addAttribute("message", "Siz faqat VCHD-5 dagi vagon ma'lumotlarni o'chirishingiz mumkin");
				model.addAttribute("isSuccess", false);
			}
		}

		vagonsToDownload = vagonService.findAll();
		model.addAttribute("vagons", vagonsToDownload);

		getPlanFact(model);

		return "vagons";
	}

//Search
	@PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@GetMapping("/vagons/search")
	public String searchByNomer(Model model,  @RequestParam(value = "participant", required = false) Integer participant) {
		if(participant == null ) {
			vagonsToDownload = vagonService.findAll();
			model.addAttribute("vagons", vagonsToDownload);
		}else {
			ApiResponse apiResponse = vagonService.findByKeyword(participant);
			if (apiResponse.isSuccess()) {
				model.addAttribute("vagons", apiResponse.getVagonModel());
				vagonsToDownload = new ArrayList<>();
				vagonsToDownload.add(apiResponse.getVagonModel());
			}else {
				model.addAttribute("message", apiResponse.getMessage());
				model.addAttribute("isSuccess", apiResponse.isSuccess());
			}
		}

		getPlanFact(model);

		return "vagons";
	}

//Filter qilish
	@PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@GetMapping("/vagons/filter")
	public String filterByDepoNomi(Model model,  @RequestParam(value = "depoNomi", required = false) String depoNomi,
								   @RequestParam(value = "vagonTuri", required = false) String vagonTuri,
								   @RequestParam(value = "country", required = false) String country) {

		if(!depoNomi.equalsIgnoreCase("Hammasi") && !vagonTuri.equalsIgnoreCase("Hammasi") && !country.equalsIgnoreCase("Hammasi") ) {
			vagonsToDownload=vagonService.findAllByDepoNomiVagonTuriAndCountry(depoNomi, vagonTuri, country);
			model.addAttribute("vagons", vagonsToDownload);
		}else if(depoNomi.equalsIgnoreCase("Hammasi") && !vagonTuri.equalsIgnoreCase("Hammasi") && !country.equalsIgnoreCase("Hammasi")){
			vagonsToDownload=vagonService.findAllByVagonTuriAndCountry(vagonTuri , country);
			model.addAttribute("vagons", vagonsToDownload);
		}else if(depoNomi.equalsIgnoreCase("Hammasi") && vagonTuri.equalsIgnoreCase("Hammasi")&& !country.equalsIgnoreCase("Hammasi")){
			vagonsToDownload=vagonService.findAllBycountry(country );
			model.addAttribute("vagons", vagonsToDownload);
		}else if(!depoNomi.equalsIgnoreCase("Hammasi") && !vagonTuri.equalsIgnoreCase("Hammasi") && country.equalsIgnoreCase("Hammasi")){
			vagonsToDownload=vagonService.findAllByDepoNomiAndVagonTuri(depoNomi, vagonTuri);
			model.addAttribute("vagons", vagonsToDownload);
		}else if(!depoNomi.equalsIgnoreCase("Hammasi") && vagonTuri.equalsIgnoreCase("Hammasi") && !country.equalsIgnoreCase("Hammasi")){
			vagonsToDownload=vagonService.findAllByDepoNomiAndCountry(depoNomi, country);
			model.addAttribute("vagons", vagonsToDownload);
		}else if(depoNomi.equalsIgnoreCase("Hammasi") && !vagonTuri.equalsIgnoreCase("Hammasi") && country.equalsIgnoreCase("Hammasi")){
			vagonsToDownload=vagonService.findAllByVagonTuri(vagonTuri);
			model.addAttribute("vagons", vagonsToDownload);
		}else if(!depoNomi.equalsIgnoreCase("Hammasi") && vagonTuri.equalsIgnoreCase("Hammasi") && country.equalsIgnoreCase("Hammasi")){
			vagonsToDownload=vagonService.findAllByDepoNomi(depoNomi );
			model.addAttribute("vagons", vagonsToDownload);
		}else {
			vagonsToDownload=vagonService.findAll();
			model.addAttribute("vagons", vagonsToDownload);
		}

		getPlanFact(model);

		if (country.equalsIgnoreCase("Hammasi")) {

			List<Integer> vagonsToDownloadTable  = new ArrayList<>();

			//Kritiylarni  hammasini olish
			int samKritiy =  vagonService.getVagonsCount("Yopiq vagon (крыт)","VCHD-6");
			int havKritiy =  vagonService.getVagonsCount("Yopiq vagon (крыт)","VCHD-3");
			int andjKritiy =  vagonService.getVagonsCount("Yopiq vagon (крыт)","VCHD-5");

			model.addAttribute("havKritiy", havKritiy);
			model.addAttribute("andjKritiy", andjKritiy);
			model.addAttribute("samKritiy",samKritiy);

			vagonsToDownloadTable.add(havKritiy);
			vagonsToDownloadTable.add(andjKritiy);
			vagonsToDownloadTable.add(samKritiy);
			vagonsToDownloadTable.add(samKritiy + havKritiy + andjKritiy);

			//Platformalarni  hammasini olish
			int samPlatforma =  vagonService.getVagonsCount("Platforma(пф)","VCHD-6");
			int havPlatforma =  vagonService.getVagonsCount("Platforma(пф)","VCHD-3");
			int andjPlatforma =  vagonService.getVagonsCount("Platforma(пф)","VCHD-5");

			model.addAttribute("samPlatforma",samPlatforma);
			model.addAttribute("havPlatforma", havPlatforma);
			model.addAttribute("andjPlatforma", andjPlatforma);

			vagonsToDownloadTable.add(havPlatforma);
			vagonsToDownloadTable.add(andjPlatforma);
			vagonsToDownloadTable.add(samPlatforma);
			vagonsToDownloadTable.add(havPlatforma + andjPlatforma + samPlatforma);

			//Poluvagonlarni  hammasini olish
			int samPoluvagon =  vagonService.getVagonsCount("Yarim ochiq vagon(пв)","VCHD-6");
			int havPoluvagon =  vagonService.getVagonsCount("Yarim ochiq vagon(пв)","VCHD-3");
			int andjPoluvagon =  vagonService.getVagonsCount("Yarim ochiq vagon(пв)","VCHD-5");

			model.addAttribute("samPoluvagon", samPoluvagon);
			model.addAttribute("havPoluvagon", havPoluvagon);
			model.addAttribute("andjPoluvagon", andjPoluvagon);

			vagonsToDownloadTable.add(havPoluvagon);
			vagonsToDownloadTable.add(andjPoluvagon);
			vagonsToDownloadTable.add(samPoluvagon);
			vagonsToDownloadTable.add(havPoluvagon + andjPoluvagon + samPoluvagon);

			//Tsisternalarni  hammasini olish
			int samTsisterna = vagonService.getVagonsCount("Sisterna(цс)","VCHD-6");
			int havTsisterna = vagonService.getVagonsCount("Sisterna(цс)","VCHD-3");
			int andjTsisterna = vagonService.getVagonsCount("Sisterna(цс)","VCHD-5");

			model.addAttribute("samTsisterna", samTsisterna);
			model.addAttribute("havTsisterna", havTsisterna);
			model.addAttribute("andjTsisterna", andjTsisterna);

			vagonsToDownloadTable.add(havTsisterna);
			vagonsToDownloadTable.add(andjTsisterna);
			vagonsToDownloadTable.add(samTsisterna);
			vagonsToDownloadTable.add(havTsisterna + andjTsisterna + samTsisterna);

			//Boshqalarni  hammasini olish
			int samBoshqa = vagonService.getVagonsCount("Boshqa turdagi(проч)","VCHD-6");
			int havBoshqa = vagonService.getVagonsCount("Boshqa turdagi(проч)","VCHD-3");
			int andjBoshqa = vagonService.getVagonsCount("Boshqa turdagi(проч)","VCHD-5");

			model.addAttribute("samBoshqa", samBoshqa);
			model.addAttribute("havBoshqa", havBoshqa);
			model.addAttribute("andjBoshqa", andjBoshqa);

			vagonsToDownloadTable.add(havBoshqa);
			vagonsToDownloadTable.add(andjBoshqa);
			vagonsToDownloadTable.add(samBoshqa);
			vagonsToDownloadTable.add(havBoshqa + andjBoshqa + samBoshqa);

			// Jaminini olish
			int hammasi = vagonService.getCount("VCHD-6") + vagonService.getCount("VCHD-3") + vagonService.getCount("VCHD-5");
			int sam = vagonService.getCount("VCHD-6") ;
			int hav = vagonService.getCount("VCHD-3") ;
			int andj =  vagonService.getCount("VCHD-5");

			model.addAttribute("hammasi", hammasi);
			model.addAttribute("sam", sam);
			model.addAttribute("hav", hav);
			model.addAttribute("andj", andj);

			vagonsToDownloadTable.add(hav);
			vagonsToDownloadTable.add(andj);
			vagonsToDownloadTable.add(sam);
			vagonsToDownloadTable.add(hammasi);

			vagonsToDownloadTables = vagonsToDownloadTable;

		} else if (country.equalsIgnoreCase("O'TY(ГАЖК)")) {

			List<Integer> vagonsToDownloadTable  = new ArrayList<>();

			//Kritiylarni  hammasini olish
			int havKritiy = vagonService.getVagonsCount("Yopiq vagon (крыт)", "VCHD-3","O'TY(ГАЖК)");
			int andjKritiy = vagonService.getVagonsCount("Yopiq vagon (крыт)", "VCHD-5","O'TY(ГАЖК)");
			int samKritiy = vagonService.getVagonsCount("Yopiq vagon (крыт)", "VCHD-6","O'TY(ГАЖК)");

			model.addAttribute("samKritiy", samKritiy);
			model.addAttribute("havKritiy", havKritiy);
			model.addAttribute("andjKritiy", andjKritiy);

			vagonsToDownloadTable.add(havKritiy);
			vagonsToDownloadTable.add(andjKritiy);
			vagonsToDownloadTable.add(samKritiy);
			vagonsToDownloadTable.add(havKritiy + andjKritiy + samKritiy);

			//Platformalarni  hammasini olish
			int samPlatforma =  vagonService.getVagonsCount("Platforma(пф)", "VCHD-6","O'TY(ГАЖК)");
			int havPlatforma =  vagonService.getVagonsCount("Platforma(пф)", "VCHD-3","O'TY(ГАЖК)");
			int andjPlatforma =  vagonService.getVagonsCount("Platforma(пф)", "VCHD-5","O'TY(ГАЖК)");

			model.addAttribute("samPlatforma", samPlatforma);
			model.addAttribute("havPlatforma", havPlatforma);
			model.addAttribute("andjPlatforma", andjPlatforma);

			vagonsToDownloadTable.add(havPlatforma);
			vagonsToDownloadTable.add(andjPlatforma);
			vagonsToDownloadTable.add(samPlatforma);
			vagonsToDownloadTable.add(samPlatforma + andjPlatforma + havPlatforma);

			//Poluvagonlarni  hammasini olish
			int samPoluvagon = vagonService.getVagonsCount("Yarim ochiq vagon(пв)", "VCHD-6","O'TY(ГАЖК)");
			int havPoluvagon = vagonService.getVagonsCount("Yarim ochiq vagon(пв)", "VCHD-3","O'TY(ГАЖК)");
			int andjPoluvagon = vagonService.getVagonsCount("Yarim ochiq vagon(пв)", "VCHD-5","O'TY(ГАЖК)");

			model.addAttribute("samPoluvagon", samPoluvagon);
			model.addAttribute("havPoluvagon", havPoluvagon);
			model.addAttribute("andjPoluvagon", andjPoluvagon);

			vagonsToDownloadTable.add(havPoluvagon);
			vagonsToDownloadTable.add(andjPoluvagon);
			vagonsToDownloadTable.add(samPoluvagon);
			vagonsToDownloadTable.add(samPoluvagon + havPoluvagon + andjPoluvagon);

			//Tsisternalarni  hammasini olish
			int samTsisterna = vagonService.getVagonsCount("Sisterna(цс)", "VCHD-6","O'TY(ГАЖК)");
			int havTsisterna = vagonService.getVagonsCount("Sisterna(цс)", "VCHD-3","O'TY(ГАЖК)");
			int andjTsisterna = vagonService.getVagonsCount("Sisterna(цс)", "VCHD-5","O'TY(ГАЖК)");

			model.addAttribute("samTsisterna", samTsisterna);
			model.addAttribute("havTsisterna", havTsisterna);
			model.addAttribute("andjTsisterna", andjTsisterna);

			vagonsToDownloadTable.add(havTsisterna);
			vagonsToDownloadTable.add(andjTsisterna);
			vagonsToDownloadTable.add(samTsisterna);
			vagonsToDownloadTable.add(samTsisterna + andjTsisterna + havTsisterna);

			//Boshqalarni  hammasini olish
			int samBoshqa = vagonService.getVagonsCount("Boshqa turdagi(проч)", "VCHD-6","O'TY(ГАЖК)");
			int havBoshqa = vagonService.getVagonsCount("Boshqa turdagi(проч)", "VCHD-3","O'TY(ГАЖК)");
			int andjBoshqa = vagonService.getVagonsCount("Boshqa turdagi(проч)", "VCHD-5","O'TY(ГАЖК)");

			model.addAttribute("samBoshqa", samBoshqa);
			model.addAttribute("havBoshqa", havBoshqa);
			model.addAttribute("andjBoshqa", andjBoshqa);

			vagonsToDownloadTable.add(havBoshqa);
			vagonsToDownloadTable.add(andjBoshqa);
			vagonsToDownloadTable.add(samBoshqa);
			vagonsToDownloadTable.add(samBoshqa + andjBoshqa + havBoshqa);

			// Jaminini olish
			int hammasi = vagonService.getCount("VCHD-6","O'TY(ГАЖК)") + vagonService.getCount("VCHD-3","O'TY(ГАЖК)") + vagonService.getCount("VCHD-5","O'TY(ГАЖК)");
			int sam = vagonService.getCount("VCHD-6","O'TY(ГАЖК)");
			int hav = vagonService.getCount("VCHD-3","O'TY(ГАЖК)");
			int andj = vagonService.getCount("VCHD-5","O'TY(ГАЖК)");

			model.addAttribute("hammasi", hammasi);
			model.addAttribute("sam", sam);
			model.addAttribute("hav", hav);
			model.addAttribute("andj", andj);

			vagonsToDownloadTable.add(hav);
			vagonsToDownloadTable.add(andj);
			vagonsToDownloadTable.add(sam);
			vagonsToDownloadTable.add(sam + hav + andj);

			vagonsToDownloadTables = vagonsToDownloadTable;

		} else if (country.equalsIgnoreCase("MDH(СНГ)")) {

			List<Integer> vagonsToDownloadTable  = new ArrayList<>();

			//Kritiylarni  hammasini olish
			int havKritiy = vagonService.getVagonsCount("Yopiq vagon (крыт)", "VCHD-3","MDH(СНГ)");
			int andjKritiy = vagonService.getVagonsCount("Yopiq vagon (крыт)", "VCHD-5","MDH(СНГ)");
			int samKritiy = vagonService.getVagonsCount("Yopiq vagon (крыт)", "VCHD-6","MDH(СНГ)");

			model.addAttribute("samKritiy", samKritiy);
			model.addAttribute("havKritiy", havKritiy);
			model.addAttribute("andjKritiy", andjKritiy);


			vagonsToDownloadTable.add(havKritiy);
			vagonsToDownloadTable.add(andjKritiy);
			vagonsToDownloadTable.add(samKritiy);
			vagonsToDownloadTable.add(havKritiy + andjKritiy + samKritiy);

			//Platformalarni  hammasini olish
			int samPlatforma =  vagonService.getVagonsCount("Platforma(пф)", "VCHD-6","MDH(СНГ)");
			int havPlatforma =  vagonService.getVagonsCount("Platforma(пф)", "VCHD-3","MDH(СНГ)");
			int andjPlatforma =  vagonService.getVagonsCount("Platforma(пф)", "VCHD-5","MDH(СНГ)");

			model.addAttribute("samPlatforma", samPlatforma);
			model.addAttribute("havPlatforma", havPlatforma);
			model.addAttribute("andjPlatforma", andjPlatforma);

			vagonsToDownloadTable.add(havPlatforma);
			vagonsToDownloadTable.add(andjPlatforma);
			vagonsToDownloadTable.add(samPlatforma);
			vagonsToDownloadTable.add(samPlatforma + andjPlatforma + havPlatforma);

			//Poluvagonlarni  hammasini olish
			int samPoluvagon = vagonService.getVagonsCount("Yarim ochiq vagon(пв)", "VCHD-6","MDH(СНГ)");
			int havPoluvagon = vagonService.getVagonsCount("Yarim ochiq vagon(пв)", "VCHD-3","MDH(СНГ)");
			int andjPoluvagon = vagonService.getVagonsCount("Yarim ochiq vagon(пв)", "VCHD-5","MDH(СНГ)");

			model.addAttribute("samPoluvagon", samPoluvagon);
			model.addAttribute("havPoluvagon", havPoluvagon);
			model.addAttribute("andjPoluvagon", andjPoluvagon);

			vagonsToDownloadTable.add(havPoluvagon);
			vagonsToDownloadTable.add(andjPoluvagon);
			vagonsToDownloadTable.add(samPoluvagon);
			vagonsToDownloadTable.add(samPoluvagon + havPoluvagon + andjPoluvagon);

			//Tsisternalarni  hammasini olish
			int samTsisterna = vagonService.getVagonsCount("Sisterna(цс)", "VCHD-6","MDH(СНГ)");
			int havTsisterna = vagonService.getVagonsCount("Sisterna(цс)", "VCHD-3","MDH(СНГ)");
			int andjTsisterna = vagonService.getVagonsCount("Sisterna(цс)", "VCHD-5","MDH(СНГ)");

			model.addAttribute("samTsisterna", samTsisterna);
			model.addAttribute("havTsisterna", havTsisterna);
			model.addAttribute("andjTsisterna", andjTsisterna);

			vagonsToDownloadTable.add(havTsisterna);
			vagonsToDownloadTable.add(andjTsisterna);
			vagonsToDownloadTable.add(samTsisterna);
			vagonsToDownloadTable.add(samTsisterna + andjTsisterna + havTsisterna);

			//Boshqalarni  hammasini olish
			int samBoshqa = vagonService.getVagonsCount("Boshqa turdagi(проч)", "VCHD-6","MDH(СНГ)");
			int havBoshqa = vagonService.getVagonsCount("Boshqa turdagi(проч)", "VCHD-3","MDH(СНГ)");
			int andjBoshqa = vagonService.getVagonsCount("Boshqa turdagi(проч)", "VCHD-5","MDH(СНГ)");

			model.addAttribute("samBoshqa", samBoshqa);
			model.addAttribute("havBoshqa", havBoshqa);
			model.addAttribute("andjBoshqa", andjBoshqa);

			vagonsToDownloadTable.add(havBoshqa);
			vagonsToDownloadTable.add(andjBoshqa);
			vagonsToDownloadTable.add(samBoshqa);
			vagonsToDownloadTable.add(samBoshqa + andjBoshqa + havBoshqa);

			// Jaminini olish
			int hammasi = vagonService.getCount("VCHD-6","MDH(СНГ)") + vagonService.getCount("VCHD-3","MDH(СНГ)") + vagonService.getCount("VCHD-5","MDH(СНГ)");
			int sam = vagonService.getCount("VCHD-6","MDH(СНГ)");
			int hav = vagonService.getCount("VCHD-3","MDH(СНГ)");
			int andj = vagonService.getCount("VCHD-5","MDH(СНГ)");

			model.addAttribute("hammasi", hammasi);
			model.addAttribute("sam", sam);
			model.addAttribute("hav", hav);
			model.addAttribute("andj", andj);

			vagonsToDownloadTable.add(hav);
			vagonsToDownloadTable.add(andj);
			vagonsToDownloadTable.add(sam);
			vagonsToDownloadTable.add(sam + hav + andj);

			vagonsToDownloadTables = vagonsToDownloadTable;

//			//Kritiylarni  hammasini olish
//			model.addAttribute("samKritiy", vagonService.getVagonsCount("Yopiq vagon (крыт)", "VCHD-6","MDH(СНГ)"));
//			model.addAttribute("havKritiy", vagonService.getVagonsCount("Yopiq vagon (крыт)", "VCHD-3","MDH(СНГ)"));
//			model.addAttribute("andjKritiy", vagonService.getVagonsCount("Yopiq vagon (крыт)", "VCHD-5","MDH(СНГ)"));
//
//			//Platformalarni  hammasini olish
//			model.addAttribute("samPlatforma", vagonService.getVagonsCount("Platforma(пф)", "VCHD-6","MDH(СНГ)"));
//			model.addAttribute("havPlatforma", vagonService.getVagonsCount("Platforma(пф)", "VCHD-3","MDH(СНГ)"));
//			model.addAttribute("andjPlatforma", vagonService.getVagonsCount("Platforma(пф)", "VCHD-5","MDH(СНГ)"));
//
//			//Poluvagonlarni  hammasini olish
//			model.addAttribute("samPoluvagon", vagonService.getVagonsCount("Yarim ochiq vagon(пв)", "VCHD-6","MDH(СНГ)"));
//			model.addAttribute("havPoluvagon", vagonService.getVagonsCount("Yarim ochiq vagon(пв)", "VCHD-3","MDH(СНГ)"));
//			model.addAttribute("andjPoluvagon", vagonService.getVagonsCount("Yarim ochiq vagon(пв)", "VCHD-5","MDH(СНГ)"));
//
//			//Tsisternalarni  hammasini olish
//			model.addAttribute("samTsisterna", vagonService.getVagonsCount("Sisterna(цс)", "VCHD-6","MDH(СНГ)"));
//			model.addAttribute("havTsisterna", vagonService.getVagonsCount("Sisterna(цс)", "VCHD-3","MDH(СНГ)"));
//			model.addAttribute("andjTsisterna", vagonService.getVagonsCount("Sisterna(цс)", "VCHD-5","MDH(СНГ)"));
//
//			//Boshqalarni  hammasini olish
//			model.addAttribute("samBoshqa", vagonService.getVagonsCount("Boshqa turdagi(проч)", "VCHD-6","MDH(СНГ)"));
//			model.addAttribute("havBoshqa", vagonService.getVagonsCount("Boshqa turdagi(проч)", "VCHD-3","MDH(СНГ)"));
//			model.addAttribute("andjBoshqa", vagonService.getVagonsCount("Boshqa turdagi(проч)", "VCHD-5","MDH(СНГ)"));
//
//			// Jaminini olish
//			model.addAttribute("hammasi", vagonService.getCount("VCHD-6","MDH(СНГ)") +
//					vagonService.getCount("VCHD-3","MDH(СНГ)") +
//					vagonService.getCount("VCHD-5","MDH(СНГ)"));
//			model.addAttribute("sam", vagonService.getCount("VCHD-6","MDH(СНГ)"));
//			model.addAttribute("hav", vagonService.getCount("VCHD-3","MDH(СНГ)"));
//			model.addAttribute("andj", vagonService.getCount("VCHD-5","MDH(СНГ)"));
		} else if (country.equalsIgnoreCase("Sanoat(ПРОМ)")) {

			List<Integer> vagonsToDownloadTable  = new ArrayList<>();

			//Kritiylarni  hammasini olish
			int havKritiy = vagonService.getVagonsCount("Yopiq vagon (крыт)", "VCHD-3","Sanoat(ПРОМ)");
			int andjKritiy = vagonService.getVagonsCount("Yopiq vagon (крыт)", "VCHD-5","Sanoat(ПРОМ)");
			int samKritiy = vagonService.getVagonsCount("Yopiq vagon (крыт)", "VCHD-6","Sanoat(ПРОМ)");

			model.addAttribute("samKritiy", samKritiy);
			model.addAttribute("havKritiy", havKritiy);
			model.addAttribute("andjKritiy", andjKritiy);

			vagonsToDownloadTable.add(havKritiy);
			vagonsToDownloadTable.add(andjKritiy);
			vagonsToDownloadTable.add(samKritiy);
			vagonsToDownloadTable.add(havKritiy + andjKritiy + samKritiy);

			//Platformalarni  hammasini olish
			int samPlatforma =  vagonService.getVagonsCount("Platforma(пф)", "VCHD-6","Sanoat(ПРОМ)");
			int havPlatforma =  vagonService.getVagonsCount("Platforma(пф)", "VCHD-3","Sanoat(ПРОМ)");
			int andjPlatforma =  vagonService.getVagonsCount("Platforma(пф)", "VCHD-5","Sanoat(ПРОМ)");

			model.addAttribute("samPlatforma", samPlatforma);
			model.addAttribute("havPlatforma", havPlatforma);
			model.addAttribute("andjPlatforma", andjPlatforma);

			vagonsToDownloadTable.add(havPlatforma);
			vagonsToDownloadTable.add(andjPlatforma);
			vagonsToDownloadTable.add(samPlatforma);
			vagonsToDownloadTable.add(samPlatforma + andjPlatforma + havPlatforma);

			//Poluvagonlarni  hammasini olish
			int samPoluvagon = vagonService.getVagonsCount("Yarim ochiq vagon(пв)", "VCHD-6","Sanoat(ПРОМ)");
			int havPoluvagon = vagonService.getVagonsCount("Yarim ochiq vagon(пв)", "VCHD-3","Sanoat(ПРОМ)");
			int andjPoluvagon = vagonService.getVagonsCount("Yarim ochiq vagon(пв)", "VCHD-5","Sanoat(ПРОМ)");

			model.addAttribute("samPoluvagon", samPoluvagon);
			model.addAttribute("havPoluvagon", havPoluvagon);
			model.addAttribute("andjPoluvagon", andjPoluvagon);

			vagonsToDownloadTable.add(havPoluvagon);
			vagonsToDownloadTable.add(andjPoluvagon);
			vagonsToDownloadTable.add(samPoluvagon);
			vagonsToDownloadTable.add(samPoluvagon + havPoluvagon + andjPoluvagon);

			//Tsisternalarni  hammasini olish
			int samTsisterna = vagonService.getVagonsCount("Sisterna(цс)", "VCHD-6","Sanoat(ПРОМ)");
			int havTsisterna = vagonService.getVagonsCount("Sisterna(цс)", "VCHD-3","Sanoat(ПРОМ)");
			int andjTsisterna = vagonService.getVagonsCount("Sisterna(цс)", "VCHD-5","Sanoat(ПРОМ)");

			model.addAttribute("samTsisterna", samTsisterna);
			model.addAttribute("havTsisterna", havTsisterna);
			model.addAttribute("andjTsisterna", andjTsisterna);

			vagonsToDownloadTable.add(havTsisterna);
			vagonsToDownloadTable.add(andjTsisterna);
			vagonsToDownloadTable.add(samTsisterna);
			vagonsToDownloadTable.add(samTsisterna + andjTsisterna + havTsisterna);

			//Boshqalarni  hammasini olish
			int samBoshqa = vagonService.getVagonsCount("Boshqa turdagi(проч)", "VCHD-6","Sanoat(ПРОМ)");
			int havBoshqa = vagonService.getVagonsCount("Boshqa turdagi(проч)", "VCHD-3","Sanoat(ПРОМ)");
			int andjBoshqa = vagonService.getVagonsCount("Boshqa turdagi(проч)", "VCHD-5","Sanoat(ПРОМ)");

			model.addAttribute("samBoshqa", samBoshqa);
			model.addAttribute("havBoshqa", havBoshqa);
			model.addAttribute("andjBoshqa", andjBoshqa);

			vagonsToDownloadTable.add(havBoshqa);
			vagonsToDownloadTable.add(andjBoshqa);
			vagonsToDownloadTable.add(samBoshqa);
			vagonsToDownloadTable.add(samBoshqa + andjBoshqa + havBoshqa);

			// Jaminini olish
			int hammasi = vagonService.getCount("VCHD-6","Sanoat(ПРОМ)") + vagonService.getCount("VCHD-3","Sanoat(ПРОМ)") + vagonService.getCount("VCHD-5","Sanoat(ПРОМ)");
			int sam = vagonService.getCount("VCHD-6","Sanoat(ПРОМ)");
			int hav = vagonService.getCount("VCHD-3","Sanoat(ПРОМ)");
			int andj = vagonService.getCount("VCHD-5","Sanoat(ПРОМ)");

			model.addAttribute("hammasi", hammasi);
			model.addAttribute("sam", sam);
			model.addAttribute("hav", hav);
			model.addAttribute("andj", andj);

			vagonsToDownloadTable.add(hav);
			vagonsToDownloadTable.add(andj);
			vagonsToDownloadTable.add(sam);
			vagonsToDownloadTable.add(sam + hav + andj);

			vagonsToDownloadTables = vagonsToDownloadTable;

//			//Kritiylarni  hammasini olish
//			model.addAttribute("samKritiy", vagonService.getVagonsCount("Yopiq vagon (крыт)", "VCHD-6","Sanoat(ПРОМ)"));
//			model.addAttribute("havKritiy", vagonService.getVagonsCount("Yopiq vagon (крыт)", "VCHD-3","Sanoat(ПРОМ)"));
//			model.addAttribute("andjKritiy", vagonService.getVagonsCount("Yopiq vagon (крыт)", "VCHD-5","Sanoat(ПРОМ)"));
//
//			//Platformalarni  hammasini olish
//			model.addAttribute("samPlatforma", vagonService.getVagonsCount("Platforma(пф)", "VCHD-6","Sanoat(ПРОМ)"));
//			model.addAttribute("havPlatforma", vagonService.getVagonsCount("Platforma(пф)", "VCHD-3","Sanoat(ПРОМ)"));
//			model.addAttribute("andjPlatforma", vagonService.getVagonsCount("Platforma(пф)", "VCHD-5","Sanoat(ПРОМ)"));
//
//			//Poluvagonlarni  hammasini olish
//			model.addAttribute("samPoluvagon", vagonService.getVagonsCount("Yarim ochiq vagon(пв)", "VCHD-6","Sanoat(ПРОМ)"));
//			model.addAttribute("havPoluvagon", vagonService.getVagonsCount("Yarim ochiq vagon(пв)", "VCHD-3","Sanoat(ПРОМ)"));
//			model.addAttribute("andjPoluvagon", vagonService.getVagonsCount("Yarim ochiq vagon(пв)", "VCHD-5","Sanoat(ПРОМ)"));
//
//			//Tsisternalarni  hammasini olish
//			model.addAttribute("samTsisterna", vagonService.getVagonsCount("Sisterna(цс)", "VCHD-6","Sanoat(ПРОМ)"));
//			model.addAttribute("havTsisterna", vagonService.getVagonsCount("Sisterna(цс)", "VCHD-3","Sanoat(ПРОМ)"));
//			model.addAttribute("andjTsisterna", vagonService.getVagonsCount("Sisterna(цс)", "VCHD-5","Sanoat(ПРОМ)"));
//
//			//Boshqalarni  hammasini olish
//			model.addAttribute("samBoshqa", vagonService.getVagonsCount("Boshqa turdagi(проч)", "VCHD-6","Sanoat(ПРОМ)"));
//			model.addAttribute("havBoshqa", vagonService.getVagonsCount("Boshqa turdagi(проч)", "VCHD-3","Sanoat(ПРОМ)"));
//			model.addAttribute("andjBoshqa", vagonService.getVagonsCount("Boshqa turdagi(проч)", "VCHD-5","Sanoat(ПРОМ)"));
//
//			// Jaminini olish
//			model.addAttribute("hammasi", vagonService.getCount("VCHD-6","Sanoat(ПРОМ)") +
//					vagonService.getCount("VCHD-3","Sanoat(ПРОМ)") +
//					vagonService.getCount("VCHD-5","Sanoat(ПРОМ)"));
//			model.addAttribute("sam", vagonService.getCount("VCHD-6","Sanoat(ПРОМ)"));
//			model.addAttribute("hav", vagonService.getCount("VCHD-3","Sanoat(ПРОМ)"));
//			model.addAttribute("andj", vagonService.getCount("VCHD-5","Sanoat(ПРОМ)"));
		}
		return "vagons";
	}














    //tahrirlash uchun oyna
    @PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@GetMapping("/vagons/edit/{id}")
	public String editVagonForm(@PathVariable("id") Long id, Model model, HttpServletRequest request) {
		VagonModel vagonModel = vagonService.findById(id);
		if (request.isUserInRole("SAM")) {
			if (!vagonModel.getDepoNomi().equalsIgnoreCase("VCHD-6")) {
				model.addAttribute("message", "Siz faqat VCHD-6 dagi vagon ma'lumotlarini o'zgartirishingiz mumkin");
				model.addAttribute("isSuccess", false);

				vagonsToDownload = vagonService.findAll();
				model.addAttribute("vagons", vagonsToDownload);

				getPlanFact(model);

				return "vagons";
			}
		}else if (request.isUserInRole("HAV")) {
			if (!vagonModel.getDepoNomi().equalsIgnoreCase("VCHD-3")) {
				model.addAttribute("message", "Siz faqat VCHD-3 dagi vagon ma'lumotlarini o'zgartirishingiz mumkin");
				model.addAttribute("isSuccess", false);

				vagonsToDownload = vagonService.findAll();
				model.addAttribute("vagons", vagonsToDownload);

				getPlanFact(model);

				return "vagons";
			}
		}else if (request.isUserInRole("ANDJ")) {
			if (!vagonModel.getDepoNomi().equalsIgnoreCase("VCHD-5")) {
				model.addAttribute("message", "Siz faqat VCHD-5 dagi vagon ma'lumotlarini o'zgartirishingiz mumkin");
				model.addAttribute("isSuccess", false);

				vagonsToDownload = vagonService.findAll();
				model.addAttribute("vagons", vagonsToDownload);

				getPlanFact(model);

				return "vagons";
			}
		}
		model.addAttribute("wagon",vagonService.getVagonById(id));
		return "vagons";
	}

    //tahrirni saqlash
    @PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@PostMapping("/vagons/{id}")
	public String updateVagon(@ModelAttribute("wagon") @Valid VagonDto vagon,@PathVariable Long id,Model model, HttpServletRequest request) throws NotFoundException {
    	if (request.isUserInRole("DIRECTOR")) {
			ApiResponse apiResponse = vagonService.updateVagon(vagon, id);
			model.addAttribute("message", apiResponse.getMessage());
			model.addAttribute("isSuccess", apiResponse.isSuccess());
		}else if (request.isUserInRole("SAM")) {
			ApiResponse apiResponse = vagonService.updateVagonSam(vagon, id);
			model.addAttribute("message", apiResponse.getMessage());
			model.addAttribute("isSuccess", apiResponse.isSuccess());
		}else if (request.isUserInRole("HAV")) {
			ApiResponse apiResponse = vagonService.updateVagonHav(vagon, id);
			model.addAttribute("message", apiResponse.getMessage());
			model.addAttribute("isSuccess", apiResponse.isSuccess());
		}else if (request.isUserInRole("ANDJ")) {
			ApiResponse apiResponse = vagonService.updateVagonAndj(vagon, id);
			model.addAttribute("message", apiResponse.getMessage());
			model.addAttribute("isSuccess", apiResponse.isSuccess());
		}

		vagonsToDownload = vagonService.findAll();
		model.addAttribute("vagons", vagonsToDownload);

		getPlanFact(model);

		return "vagons";
	}
	

}
