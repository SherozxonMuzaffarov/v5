package com.sms.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.sms.dto.VagonMalumotChiqishDto;
import com.sms.dto.VagonMalumotDto;
import com.sms.dto.VagonMalumotUpdateDto;
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

import com.sms.model.VagonMalumot;

import com.sms.service.VagonMalumotService;

@Controller
public class VagonMalumotController {
	
	@Autowired
	VagonMalumotService malumotService;

	//Yuklab olish uchun Malumot yigib beradi
	List<VagonMalumot> vagonsToDownload  = new ArrayList<>();

//LAst Action Timeni
	public void getLastActionTime(Model model){
		//yangi vagon qoshish uchun
		model.addAttribute("vagon", new VagonMalumot());

		//vaqtni olib turadi
		model.addAttribute("samDate",malumotService.getSamDate());
		model.addAttribute("havDate", malumotService.getHavDate());
		model.addAttribute("andjDate",malumotService.getAndjDate());
	}

//Main
	@PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@GetMapping("/vagons/chiqishMalumot")
	public String main(Model model) {

		vagonsToDownload = malumotService.findAll();
		model.addAttribute("vagons", vagonsToDownload);

		getLastActionTime(model);

		return "chiqishMalumot";
	}

//Save Wagon(Kirish)
	@PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@PostMapping("/vagons/saveVagonMalumot")
	public String saveVagon(@ModelAttribute("vagon") @Valid VagonMalumotDto vagon, HttpServletRequest request, Model model) {
		if (request.isUserInRole("DIRECTOR"))
			malumotService.saveVagon(vagon);
		else if (request.isUserInRole("SAM"))
			malumotService.saveVagonSam(vagon);
		else if (request.isUserInRole("HAV"))
			malumotService.saveVagonHav(vagon);
		else if (request.isUserInRole("ANDJ"))
			malumotService.saveVagonAndj(vagon);

		return "redirect:/vagons/chiqishMalumot";
	}

//Chiqish malumotlarini kiritish uchun Oyna
	@PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@GetMapping("/vagons/newVagonMalumotChiqish/{id}")
	public String createVagonFormChiqish( Model model,@PathVariable Long id, HttpServletRequest request) {
		VagonMalumot vagonMalumot = malumotService.findById(id);
		if (request.isUserInRole("SAM")) {
			if (!vagonMalumot.getDepoNomi().equalsIgnoreCase("VCHD-6")){
				model.addAttribute("message", "Siz faqat VCHD-6 dagi vagonlar chiqish ma'lumotlarini kiritishingiz mumkin");
				model.addAttribute("isSuccess", false);

				getLastActionTime(model);

				return "chiqishMalumot";
			}
		}else if (request.isUserInRole("HAV")) {
			if (!vagonMalumot.getDepoNomi().equalsIgnoreCase("VCHD-3")){
				model.addAttribute("message", "Siz faqat VCHD-3 dagi vagonlar chiqish ma'lumotlarini kiritishingiz mumkin");
				model.addAttribute("isSuccess", false);

				getLastActionTime(model);

				return "chiqishMalumot";
			}
		}else if (request.isUserInRole("ANDJ")) {
			if (!vagonMalumot.getDepoNomi().equalsIgnoreCase("VCHD-5")){
				model.addAttribute("message", "Siz faqat VCHD-5 dagi vagonlar chiqish ma'lumotlarini kiritishingiz mumkin");
				model.addAttribute("isSuccess", false);

				getLastActionTime(model);

				return "chiqishMalumot";
			}
		}
		if (vagonMalumot.getRamaOng1Nomeri() != null){
			model.addAttribute("message", vagonMalumot.getNomer() + " nomerli vagon chiqish ma'lumotlari kiritilgan, uni tahrirlashingiz mumkin");
			model.addAttribute("isSuccess", false);

			getLastActionTime(model);

			return "chiqishMalumot";
		}else
			model.addAttribute("vagon", malumotService.getVagonById(id));

		return "create_vagonMalumotChiqish";
	}

//Save Wagon(Chiqish)
	@PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@PostMapping("/vagons/saveVagonMalumotChiqish/{id}")
	public String saveVagonChiqish(@ModelAttribute("vagon") @Valid VagonMalumotChiqishDto vagon, @PathVariable Long id, HttpServletRequest request, Model model) {
	if (request.isUserInRole("DIRECTOR")) {
		ApiResponse apiResponse = malumotService.saveVagonChiqish(vagon, id);
		model.addAttribute("message", apiResponse.getMessage());
		model.addAttribute("isSuccess", apiResponse.isSuccess());
		}else if (request.isUserInRole("SAM")) {
			ApiResponse apiResponse = malumotService.saveVagonSamChiqish(vagon, id);
			model.addAttribute("message", apiResponse.getMessage());
			model.addAttribute("isSuccess", apiResponse.isSuccess());
		}else if (request.isUserInRole("HAV")) {
			ApiResponse apiResponse = malumotService.saveVagonHavChiqish(vagon, id);
			model.addAttribute("message", apiResponse.getMessage());
			model.addAttribute("isSuccess", apiResponse.isSuccess());
		}else if (request.isUserInRole("ANDJ")) {
			ApiResponse apiResponse = malumotService.saveVagonAndjChiqish(vagon, id);
			model.addAttribute("message", apiResponse.getMessage());
			model.addAttribute("isSuccess", apiResponse.isSuccess());
		}

		vagonsToDownload = malumotService.findAll();
		model.addAttribute("vagons", vagonsToDownload);

		getLastActionTime(model);

		return "chiqishMalumot";
	}

//Delete Wagon
	@PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@GetMapping("/vagons/deleteMalumot/{id}")
	public String deleteVagonForm(@PathVariable("id") Long id, HttpServletRequest request, Model model ) throws NotFoundException {
		if (request.isUserInRole("DIRECTOR")) {
			ApiResponse apiResponse = malumotService.deleteVagonById(id);
			model.addAttribute("message", apiResponse.getMessage());
			model.addAttribute("isSuccess", apiResponse.isSuccess());
		}else if (request.isUserInRole("SAM")) {
			ApiResponse apiResponse = malumotService.deleteVagonSam(id);
			model.addAttribute("message", apiResponse.getMessage());
			model.addAttribute("isSuccess", apiResponse.isSuccess());
		}else if (request.isUserInRole("HAV")) {
			ApiResponse apiResponse = malumotService.deleteVagonHav(id);
			model.addAttribute("message", apiResponse.getMessage());
			model.addAttribute("isSuccess", apiResponse.isSuccess());
		}else if (request.isUserInRole("ANDJ")) {
			ApiResponse apiResponse = malumotService.deleteVagonAndj(id);
			model.addAttribute("message", apiResponse.getMessage());
			model.addAttribute("isSuccess", apiResponse.isSuccess());
		}

		vagonsToDownload = malumotService.findAll();
		model.addAttribute("vagons", vagonsToDownload);

		getLastActionTime(model);

		return "chiqishMalumot";
	}


//Search
	@PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@GetMapping("/vagons/searchMalumot")
	public String searchByNomer(Model model, @RequestParam(value = "nomer", required = false) Integer nomer) {
		if(nomer==null ) {
			model.addAttribute("vagons", malumotService.findAll());
			vagonsToDownload = malumotService.findAll();
		}else {
			ApiResponse apiResponse = malumotService.searchByNomer(nomer);
			model.addAttribute("vagons", apiResponse.getObject());
			vagonsToDownload= new ArrayList<>();
			vagonsToDownload.add((VagonMalumot) apiResponse.getObject()) ;

			model.addAttribute("message", apiResponse.getMessage());
			model.addAttribute("isSuccess", apiResponse.isSuccess());
		}
		
		getLastActionTime(model);
		
		return "chiqishMalumot";
	}

//Filter
	@PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@GetMapping("/vagons/filterMalumot")
	public String filterByDate(Model model,  @RequestParam(value = "depoNomi") String depoNomi,
							   @RequestParam(value = "country", required = false) String country,
							   @RequestParam(value = "saqlanganVaqti", defaultValue = "2200-01-01") String saqlanganVaqti){

		String saqlanganVaqt = saqlanganVaqti.substring(8) + "-" + saqlanganVaqti.substring(5,7) + "-" + saqlanganVaqti.substring(0,4);

		if(depoNomi.equalsIgnoreCase("Hammasi") && country.equalsIgnoreCase("Hammasi") && !saqlanganVaqt.equalsIgnoreCase("01-01-2200")){
			vagonsToDownload = malumotService.filterByDate(saqlanganVaqt);
			model.addAttribute("vagons", vagonsToDownload);
		}else if(depoNomi.equalsIgnoreCase("Hammasi") && !country.equalsIgnoreCase("Hammasi") && saqlanganVaqt.equalsIgnoreCase("01-01-2200")){
			vagonsToDownload = malumotService.filterByCountry(country);
			model.addAttribute("vagons", vagonsToDownload);
		}else if(!depoNomi.equalsIgnoreCase("Hammasi") && country.equalsIgnoreCase("Hammasi") && saqlanganVaqt.equalsIgnoreCase("01-01-2200")){
			vagonsToDownload = malumotService.filterByDepoNomi(depoNomi);
			model.addAttribute("vagons", vagonsToDownload);
		}else if(depoNomi.equalsIgnoreCase("Hammasi") && !country.equalsIgnoreCase("Hammasi") && !saqlanganVaqt.equalsIgnoreCase("01-01-2200")){
			vagonsToDownload = malumotService.filterByCountryAndDate(country, saqlanganVaqt);
			model.addAttribute("vagons", vagonsToDownload);
		}else if(!depoNomi.equalsIgnoreCase("Hammasi") && !country.equalsIgnoreCase("Hammasi") && saqlanganVaqt.equalsIgnoreCase("01-01-2200")){
			vagonsToDownload = malumotService.filterByDepoNomiAndCountry(depoNomi, country);
			model.addAttribute("vagons", vagonsToDownload);
		}else if(!depoNomi.equalsIgnoreCase("Hammasi") && country.equalsIgnoreCase("Hammasi") && !saqlanganVaqt.equalsIgnoreCase("01-01-2200")){
			vagonsToDownload = malumotService.filterByDepoNomiAndDate(depoNomi, saqlanganVaqt);
			model.addAttribute("vagons", vagonsToDownload);
		}else if(!depoNomi.equalsIgnoreCase("Hammasi") && !country.equalsIgnoreCase("Hammasi") && !saqlanganVaqt.equalsIgnoreCase("01-01-2200")){
			vagonsToDownload =  malumotService.filterByDepoNomiCountryAndDate(depoNomi, country, saqlanganVaqt);
			model.addAttribute("vagons", vagonsToDownload);
		}else {
			vagonsToDownload = malumotService.findAll();
			model.addAttribute("vagons", vagonsToDownload);
		}

		getLastActionTime(model);

		return "chiqishMalumot";
	}















//	@PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
//	@GetMapping("/vagons/createExcelMalumot")
//	public void pdfFile(HttpServletResponse response) throws IOException {
//		malumotService.createPdf(vagonsToDownload,response);
//	 }

	
	 //yangi vagon qoshish uchun oyna
    @PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@GetMapping("/vagons/newVagonMalumot")
	public String createVagonForm(Model model) {
    	VagonMalumot vagon = new VagonMalumot();
		model.addAttribute("vagon", vagon);
		return "create_vagonMalumot";
	}

    
  //tahrirlash uchun oyna
    @PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@GetMapping("/vagons/editMalumot/{id}")
	public String editVagonForm(@PathVariable("id") Long id, Model model, HttpServletRequest request) {
		VagonMalumot vagonMalumot = malumotService.getVagonById(id);
		if (request.isUserInRole("SAM")) {
			if (!vagonMalumot.getDepoNomi().equalsIgnoreCase("VCHD-6")){
				model.addAttribute("message", "Siz faqat VCHD-6 dagi vagon ma'lumotlarini o'zgartirishingiz mumkin");
				model.addAttribute("isSuccess", false);

				vagonsToDownload = malumotService.findAll();
				model.addAttribute("vagons", malumotService.findAll());

				//vaqtni olib turadi
				model.addAttribute("samDate",malumotService.getSamDate());
				model.addAttribute("havDate", malumotService.getHavDate());
				model.addAttribute("andjDate",malumotService.getAndjDate());

				return "chiqishMalumot";
			}
		}else if (request.isUserInRole("HAV")) {
			if (!vagonMalumot.getDepoNomi().equalsIgnoreCase("VCHD-3")){
				model.addAttribute("message", "Siz faqat VCHD-3 dagi vagon ma'lumotlarini o'zgartirishingiz mumkin");
				model.addAttribute("isSuccess", false);

				vagonsToDownload = malumotService.findAll();
				model.addAttribute("vagons", malumotService.findAll());

				//vaqtni olib turadi
				model.addAttribute("samDate",malumotService.getSamDate());
				model.addAttribute("havDate", malumotService.getHavDate());
				model.addAttribute("andjDate",malumotService.getAndjDate());

				return "chiqishMalumot";
			}
		}else if (request.isUserInRole("ANDJ")) {
			if (!vagonMalumot.getDepoNomi().equalsIgnoreCase("VCHD-5")){
				model.addAttribute("message", "Siz faqat VCHD-5 dagi vagon ma'lumotlarini o'zgartirishingiz mumkin");
				model.addAttribute("isSuccess", false);

				vagonsToDownload = malumotService.findAll();
				model.addAttribute("vagons", malumotService.findAll());

				//vaqtni olib turadi
				model.addAttribute("samDate",malumotService.getSamDate());
				model.addAttribute("havDate", malumotService.getHavDate());
				model.addAttribute("andjDate",malumotService.getAndjDate());

				return "chiqishMalumot";
			}
		}
		model.addAttribute("vagon",malumotService.getVagonById(id));
		return "edit_vagonMalumot";
	}
  //tahrirlash uchun oyna chiqish
    @PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@GetMapping("/vagons/editMalumotChiqish/{id}")
	public String editVagonFormChiqish(@PathVariable("id") Long id, Model model, HttpServletRequest  request) {
		VagonMalumot vagonMalumot = malumotService.findById(id);
		if (request.isUserInRole("SAM")) {
			if (!vagonMalumot.getDepoNomi().equalsIgnoreCase("VCHD-6")){
				model.addAttribute("message", "Siz faqat VCHD-6 dagi vagon ma'lumotlarini o'zgartirishingiz mumkin");
				model.addAttribute("isSuccess", false);

				vagonsToDownload = malumotService.findAll();
				model.addAttribute("vagons", malumotService.findAll());

				//vaqtni olib turadi
				model.addAttribute("samDate",malumotService.getSamDate());
				model.addAttribute("havDate", malumotService.getHavDate());
				model.addAttribute("andjDate",malumotService.getAndjDate());

				return "chiqishMalumot";
			}
		}else if (request.isUserInRole("HAV")) {
			if (!vagonMalumot.getDepoNomi().equalsIgnoreCase("VCHD-3")){
				model.addAttribute("message", "Siz faqat VCHD-3 dagi vagon ma'lumotlarini o'zgartirishingiz mumkin");
				model.addAttribute("isSuccess", false);

				vagonsToDownload = malumotService.findAll();
				model.addAttribute("vagons", malumotService.findAll());

				//vaqtni olib turadi
				model.addAttribute("samDate",malumotService.getSamDate());
				model.addAttribute("havDate", malumotService.getHavDate());
				model.addAttribute("andjDate",malumotService.getAndjDate());

				return "chiqishMalumot";
			}
		}else if (request.isUserInRole("ANDJ")) {
			if (!vagonMalumot.getDepoNomi().equalsIgnoreCase("VCHD-5")){
				model.addAttribute("message", "Siz faqat VCHD-5 dagi vagon ma'lumotlarini o'zgartirishingiz mumkin");
				model.addAttribute("isSuccess", false);

				vagonsToDownload = malumotService.findAll();
				model.addAttribute("vagons", malumotService.findAll());

				//vaqtni olib turadi
				model.addAttribute("samDate",malumotService.getSamDate());
				model.addAttribute("havDate", malumotService.getHavDate());
				model.addAttribute("andjDate",malumotService.getAndjDate());

				return "chiqishMalumot";
			}
		}
		if (vagonMalumot.getRamaOng1Nomeri() == null){
			model.addAttribute("message", vagonMalumot.getNomer() + " nomerli vagon chiqish ma'lumotlari kiritilmagan");
			model.addAttribute("isSuccess", false);

			vagonsToDownload = malumotService.findAll();
			model.addAttribute("vagons", malumotService.findAll());

			//vaqtni olib turadi
			model.addAttribute("samDate",malumotService.getSamDate());
			model.addAttribute("havDate", malumotService.getHavDate());
			model.addAttribute("andjDate",malumotService.getAndjDate());

			return "chiqishMalumot";
		}else
			model.addAttribute("vagon",malumotService.getVagonById(id));
		return "edit_vagonMalumotChiqish";
	}

    //tahrirni saqlash
    @PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@PostMapping("/vagons/updateMalumot/{id}")
	public String updateVagon(@ModelAttribute("vagon") @Valid VagonMalumotUpdateDto vagon, @PathVariable Long id, Model model, HttpServletRequest request) throws NotFoundException {
    	if (request.isUserInRole("DIRECTOR")) {
			ApiResponse apiResponse = malumotService.updateVagon(vagon, id);
			model.addAttribute("message", apiResponse.getMessage());
			model.addAttribute("isSuccess", apiResponse.isSuccess());
		}else if (request.isUserInRole("SAM")) {
			ApiResponse apiResponse = malumotService.updateVagonSam(vagon, id);
			model.addAttribute("message", apiResponse.getMessage());
			model.addAttribute("isSuccess", apiResponse.isSuccess());
		}else if (request.isUserInRole("HAV")) {
			ApiResponse apiResponse = malumotService.updateVagonHav(vagon, id);
			model.addAttribute("message", apiResponse.getMessage());
			model.addAttribute("isSuccess", apiResponse.isSuccess());
		}else if (request.isUserInRole("ANDJ")) {
			ApiResponse apiResponse = malumotService.updateVagonAndj(vagon, id);
			model.addAttribute("message", apiResponse.getMessage());
			model.addAttribute("isSuccess", apiResponse.isSuccess());
		}

		vagonsToDownload = malumotService.findAll();
		model.addAttribute("vagons", malumotService.findAll());

		//vaqtni olib turadi
		model.addAttribute("samDate",malumotService.getSamDate());
		model.addAttribute("havDate", malumotService.getHavDate());
		model.addAttribute("andjDate",malumotService.getAndjDate());

		return "chiqishMalumot";
	}
    //tahrirni saqlash Chiqish
    @PreAuthorize(value = "hasAnyRole('DIRECTOR', 'SAM','HAV','ANDJ')")
	@PostMapping("/vagons/updateMalumotChiqish/{id}")
	public String updateVagonChiqish(@ModelAttribute("vagon") @Valid VagonMalumotChiqishDto vagon,Model model,@PathVariable Long id, HttpServletRequest request)  {

		if (request.isUserInRole("DIRECTOR")) {
			ApiResponse apiResponse = malumotService.updateVagonChiqish(vagon, id);
			model.addAttribute("message", apiResponse.getMessage());
			model.addAttribute("isSuccess", apiResponse.isSuccess());
		}else if (request.isUserInRole("SAM")) {
			ApiResponse apiResponse = malumotService.updateVagonSamChiqish(vagon, id);
			model.addAttribute("message", apiResponse.getMessage());
			model.addAttribute("isSuccess", apiResponse.isSuccess());
		}else if (request.isUserInRole("HAV")) {
			ApiResponse apiResponse = malumotService.updateVagonHavChiqish(vagon, id);
			model.addAttribute("message", apiResponse.getMessage());
			model.addAttribute("isSuccess", apiResponse.isSuccess());
		}else if (request.isUserInRole("ANDJ")) {
			ApiResponse apiResponse = malumotService.updateVagonAndjChiqish(vagon, id);
			model.addAttribute("message", apiResponse.getMessage());
			model.addAttribute("isSuccess", apiResponse.isSuccess());
		}

		vagonsToDownload = malumotService.findAll();
		model.addAttribute("vagons", malumotService.findAll());

		//vaqtni olib turadi
		model.addAttribute("samDate",malumotService.getSamDate());
		model.addAttribute("havDate", malumotService.getHavDate());
		model.addAttribute("andjDate",malumotService.getAndjDate());

		return "chiqishMalumot";
	}
	


}
