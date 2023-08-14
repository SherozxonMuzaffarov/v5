package com.sms.serviceImp;

import com.sms.dto.PlanBiznesDto;
import com.sms.model.LastActionTimes;
import com.sms.model.PlanBiznes;
import com.sms.model.VagonTayyor;
import com.sms.model.VagonTayyorUty;
import com.sms.payload.ApiResponse;
import com.sms.repository.PlanBiznesRepository;
import com.sms.repository.TimeRepository;
import com.sms.repository.VagonTayyorBiznesRepository;
import com.sms.service.VagonTayyorBiznesService;
//import org.apache.poi.ss.usermodel.Cell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import org.springframework.util.FileCopyUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class VagonTayyorBiznesServiceImp implements VagonTayyorBiznesService{

	@Autowired
	private VagonTayyorBiznesRepository vagonTayyorRepository;
	@Autowired 
	private PlanBiznesRepository planBiznesRepository;
	@Autowired
	private TimeRepository utyTimeRepository;

	String samDate ;
	String havDate ;
	String andjDate ;

	LocalDateTime today = LocalDateTime.now();
	LocalDateTime minusHours = today.plusHours(5);
	DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    String currentDate = minusHours.format(myFormatObj);

	public String getSamDate() {
		Optional<LastActionTimes> optionalBiznesTime = utyTimeRepository.findById(1);
		if (!optionalBiznesTime.isPresent())
			return currentDate;
		return optionalBiznesTime.get().getSamBiznesDate();
	}

	public String getHavDate() {
		Optional<LastActionTimes> optionalBiznesTime = utyTimeRepository.findById(1);
		if (!optionalBiznesTime.isPresent())
			return currentDate;
		return optionalBiznesTime.get().getHavBiznesDate();
	}

	public String getAndjDate() {
		Optional<LastActionTimes> optionalBiznesTime = utyTimeRepository.findById(1);
		if (!optionalBiznesTime.isPresent())
			return currentDate;
		return optionalBiznesTime.get().getAndjBiznesDate();
	}

//Generate Pdf
	@Override
	public void pdfFileTable(List<Integer> vagonsToDownloadAllTable, HttpServletResponse response) throws IOException {

		String home = System.getProperty("user.home");
		File file = new File(home + "/Downloads" + "/Biznes reja boyicha ta'mir ma'lumot (Jadval).pdf");
		if (!file.getParentFile().exists())
			file.getParentFile().mkdirs();
		if (!file.exists())
			file.createNewFile();
		List<Integer> allVagons = vagonsToDownloadAllTable;
		try {
			response.setHeader("Content-Disposition",
					"attachment;fileName=\"" + "Biznes reja boyicha ta'mir ma'lumot (Jadval).pdf" +"\"");
			response.setContentType("application/pdf");

			PdfWriter writer = new PdfWriter(file.getAbsolutePath());
			PdfDocument pdfDoc = new PdfDocument(writer);
			pdfDoc.setDefaultPageSize(PageSize.A4.rotate());
			Document doc = new Document(pdfDoc);

			String FONT_FILENAME = "./src/main/resources/arial.ttf";
			PdfFont font = PdfFontFactory.createFont(FONT_FILENAME, PdfEncodings.IDENTITY_H);
			doc.setFont(font);

			Paragraph paragraph = new Paragraph("Ta'mir bo'yicha ma'lumot(Biznes rejasi bo'yicha)");
			paragraph.setBackgroundColor(Color.DARK_GRAY);
			paragraph.setFontColor(Color.WHITE);// Setting background color to cell1
			paragraph.setBorder(Border.NO_BORDER);            // Setting border to cell1
			paragraph.setTextAlignment(TextAlignment.CENTER); // Setting text alignment to cell1
			paragraph.setFontSize(12);

			Paragraph paragraphDr = new Paragraph("Depo ta'mir(ДР)");
			paragraphDr.setTextAlignment(TextAlignment.CENTER); // Setting text alignment to cell1
			paragraphDr.setFontSize(10);



			float[] columnWidth = {200f,200f,200f,200f,200f,200f, 200f};
			Table table = new Table(columnWidth);
			table.setTextAlignment(TextAlignment.CENTER);
			table.setFontSize(5);

			table.addCell(new Cell().add("\n\n VCHD "));

	//			Jami
			float[] columnWidth1 = {200f};
			Table table1 = new Table(columnWidth1);
			table1.addCell(new Cell().add("Jami "));

			float[] columnWidth2 = {200f, 200f, 200f};
			Table table2 = new Table(columnWidth2);
			table2.addCell(new Cell().add("Plan"));
			table2.addCell(new Cell().add("Fact"));
			table2.addCell(new Cell().add("+/-"));

			table1.addCell(table2);
			table.addCell(table1);

	//			Yopiq vagon
			Table table3 = new Table(columnWidth1);
			table3.addCell(new Cell().add("Yopiq  vagon (крыт)"));

			Table table4 = new Table(columnWidth2);
			table4.addCell(new Cell().add("Plan"));
			table4.addCell(new Cell().add("Fact"));
			table4.addCell(new Cell().add("+/-"));

			table3.addCell(table4);
			table.addCell(table3);

	//			Platforma(пф)
			Table table5 = new Table(columnWidth1);
			table5.addCell(new Cell().add("Platforma (пф) "));

			Table table6 = new Table(columnWidth2);
			table6.addCell(new Cell().add("Plan"));
			table6.addCell(new Cell().add("Fact"));
			table6.addCell(new Cell().add("+/-"));

			table5.addCell(table6);
			table.addCell(table5);

	//			Yarim ochiq vagon(пв)
			Table table7 = new Table(columnWidth1);
			table7.addCell(new Cell().add("Yarim ochiq vagon (пв)"));

			Table table8 = new Table(columnWidth2);
			table8.addCell(new Cell().add("Plan"));
			table8.addCell(new Cell().add("Fact"));
			table8.addCell(new Cell().add("+/-"));

			table7.addCell(table8);
			table.addCell(table7);

	//			Sisterna(цс)
			Table table9 = new Table(columnWidth1);
			table9.addCell(new Cell().add("Sisterna (цс) "));

			Table table10 = new Table(columnWidth2);
			table10.addCell(new Cell().add("Plan"));
			table10.addCell(new Cell().add("Fact"));
			table10.addCell(new Cell().add("+/-"));

			table9.addCell(table10);
			table.addCell(table9);

	//			Boshqa turdagi(проч)
			Table table11 = new Table(columnWidth1);
			table11.addCell(new Cell().add("Boshqa turdagi (проч)"));

			Table table12 = new Table(columnWidth2);
			table12.addCell(new Cell().add("Plan"));
			table12.addCell(new Cell().add("Fact"));
			table12.addCell(new Cell().add("+/-"));

			table11.addCell(table12);
			table.addCell(table11);

	//VALUE LAR
			table.addCell("VCHD-3");
			//JAmi
			float[] columnWidth3 = {200f,200f,200f};
			Table table13 = new Table(columnWidth3);
			table13.addCell(new Cell().add(String.valueOf(allVagons.get(0))));
			table13.addCell(new Cell().add(String.valueOf(allVagons.get(1))));
			table13.addCell(new Cell().add(String.valueOf(allVagons.get(1) - allVagons.get(0))));

			table.addCell(table13);

			//Yopiq vagon (крыт)
			Table table14 = new Table(columnWidth3);
			table14.addCell(new Cell().add(String.valueOf(allVagons.get(2))));
			table14.addCell(new Cell().add(String.valueOf(allVagons.get(3))));
			table14.addCell(new Cell().add(String.valueOf(allVagons.get(3) - allVagons.get(2))));

			table.addCell(table14);

			//Platforma(пф)
			Table table15 = new Table(columnWidth3);
			table15.addCell(new Cell().add(String.valueOf(allVagons.get(4))));
			table15.addCell(new Cell().add(String.valueOf(allVagons.get(5))));
			table15.addCell(new Cell().add(String.valueOf(allVagons.get(5) - allVagons.get(4))));

			table.addCell(table15);

			//Yarim ochiq vagon(пв)
			Table table16 = new Table(columnWidth3);
			table16.addCell(new Cell().add(String.valueOf(allVagons.get(6))));
			table16.addCell(new Cell().add(String.valueOf(allVagons.get(7))));
			table16.addCell(new Cell().add(String.valueOf(allVagons.get(7) - allVagons.get(6))));

			table.addCell(table16);

			//Sisterna(цс)
			Table table17 = new Table(columnWidth3);
			table17.addCell(new Cell().add(String.valueOf(allVagons.get(8))));
			table17.addCell(new Cell().add(String.valueOf(allVagons.get(9))));
			table17.addCell(new Cell().add(String.valueOf(allVagons.get(9) - allVagons.get(8))));

			table.addCell(table17);

			//Boshqa turdagi(проч)
			Table table18 = new Table(columnWidth3);
			table18.addCell(new Cell().add(String.valueOf(allVagons.get(10))));
			table18.addCell(new Cell().add(String.valueOf(allVagons.get(11))));
			table18.addCell(new Cell().add(String.valueOf(allVagons.get(11) - allVagons.get(10))));

			table.addCell(table18);


			table.addCell("VCHD-5");
			//JAmi
			Table table19 = new Table(columnWidth3);
			table19.addCell(new Cell().add(String.valueOf(allVagons.get(12))));
			table19.addCell(new Cell().add(String.valueOf(allVagons.get(13))));
			table19.addCell(new Cell().add(String.valueOf(allVagons.get(13) - allVagons.get(12))));

			table.addCell(table19);

			//Yopiq vagon (крыт)
			Table table20 = new Table(columnWidth3);
			table20.addCell(new Cell().add(String.valueOf(allVagons.get(14))));
			table20.addCell(new Cell().add(String.valueOf(allVagons.get(15))));
			table20.addCell(new Cell().add(String.valueOf(allVagons.get(15) - allVagons.get(14))));

			table.addCell(table20);

			//Platforma(пф)
			Table table21 = new Table(columnWidth3);
			table21.addCell(new Cell().add(String.valueOf(allVagons.get(16))));
			table21.addCell(new Cell().add(String.valueOf(allVagons.get(17))));
			table21.addCell(new Cell().add(String.valueOf(allVagons.get(17) - allVagons.get(16))));

			table.addCell(table21);

			//Yarim ochiq vagon(пв)
			Table table22 = new Table(columnWidth3);
			table22.addCell(new Cell().add(String.valueOf(allVagons.get(18))));
			table22.addCell(new Cell().add(String.valueOf(allVagons.get(19))));
			table22.addCell(new Cell().add(String.valueOf(allVagons.get(19) - allVagons.get(18))));

			table.addCell(table22);

			//Sisterna(цс)
			Table table23 = new Table(columnWidth3);
			table23.addCell(new Cell().add(String.valueOf(allVagons.get(20))));
			table23.addCell(new Cell().add(String.valueOf(allVagons.get(21))));
			table23.addCell(new Cell().add(String.valueOf(allVagons.get(21) - allVagons.get(20))));

			table.addCell(table23);

			//Boshqa turdagi(проч)
			Table table24 = new Table(columnWidth3);
			table24.addCell(new Cell().add(String.valueOf(allVagons.get(22))));
			table24.addCell(new Cell().add(String.valueOf(allVagons.get(23))));
			table24.addCell(new Cell().add(String.valueOf(allVagons.get(23) - allVagons.get(22))));

			table.addCell(table24);


			table.addCell("VCHD-6");
			//JAmi
			Table table25 = new Table(columnWidth3);
			table25.addCell(new Cell().add(String.valueOf(allVagons.get(24))));
			table25.addCell(new Cell().add(String.valueOf(allVagons.get(25))));
			table25.addCell(new Cell().add(String.valueOf(allVagons.get(25) - allVagons.get(24))));

			table.addCell(table25);

			//Yopiq vagon (крыт)
			Table table26 = new Table(columnWidth3);
			table26.addCell(new Cell().add(String.valueOf(allVagons.get(26))));
			table26.addCell(new Cell().add(String.valueOf(allVagons.get(27))));
			table26.addCell(new Cell().add(String.valueOf(allVagons.get(27) - allVagons.get(26))));

			table.addCell(table26);

			//Platforma(пф)
			Table table27 = new Table(columnWidth3);
			table27.addCell(new Cell().add(String.valueOf(allVagons.get(28))));
			table27.addCell(new Cell().add(String.valueOf(allVagons.get(29))));
			table27.addCell(new Cell().add(String.valueOf(allVagons.get(29) - allVagons.get(28))));

			table.addCell(table27);

			//Yarim ochiq vagon(пв)
			Table table28 = new Table(columnWidth3);
			table28.addCell(new Cell().add(String.valueOf(allVagons.get(30))));
			table28.addCell(new Cell().add(String.valueOf(allVagons.get(31))));
			table28.addCell(new Cell().add(String.valueOf(allVagons.get(31) - allVagons.get(30))));

			table.addCell(table28);

			//Sisterna(цс)
			Table table29 = new Table(columnWidth3);
			table29.addCell(new Cell().add(String.valueOf(allVagons.get(32))));
			table29.addCell(new Cell().add(String.valueOf(allVagons.get(33))));
			table29.addCell(new Cell().add(String.valueOf(allVagons.get(33) - allVagons.get(32))));

			table.addCell(table29);

			//Boshqa turdagi(проч)
			Table table30 = new Table(columnWidth3);
			table30.addCell(new Cell().add(String.valueOf(allVagons.get(34))));
			table30.addCell(new Cell().add(String.valueOf(allVagons.get(35))));
			table30.addCell(new Cell().add(String.valueOf(allVagons.get(35) - allVagons.get(34))));

			table.addCell(table30);

			table.addCell("O'zvagonta'mir");
			//JAmi
			Table table31 = new Table(columnWidth3);
			table31.addCell(new Cell().add(String.valueOf(allVagons.get(36))));
			table31.addCell(new Cell().add(String.valueOf(allVagons.get(37))));
			table31.addCell(new Cell().add(String.valueOf(allVagons.get(37) - allVagons.get(36))));

			table.addCell(table31);

			//Yopiq vagon (крыт)
			Table table32 = new Table(columnWidth3);
			table32.addCell(new Cell().add(String.valueOf(allVagons.get(38))));
			table32.addCell(new Cell().add(String.valueOf(allVagons.get(39))));
			table32.addCell(new Cell().add(String.valueOf(allVagons.get(39) - allVagons.get(38))));

			table.addCell(table32);

			//Platforma(пф)
			Table table33 = new Table(columnWidth3);
			table33.addCell(new Cell().add(String.valueOf(allVagons.get(40))));
			table33.addCell(new Cell().add(String.valueOf(allVagons.get(41))));
			table33.addCell(new Cell().add(String.valueOf(allVagons.get(41) - allVagons.get(40))));

			table.addCell(table33);

			//Yarim ochiq vagon(пв)
			Table table34 = new Table(columnWidth3);
			table34.addCell(new Cell().add(String.valueOf(allVagons.get(42))));
			table34.addCell(new Cell().add(String.valueOf(allVagons.get(43))));
			table34.addCell(new Cell().add(String.valueOf(allVagons.get(43) - allVagons.get(42))));

			table.addCell(table34);

			//Sisterna(цс)
			Table table35 = new Table(columnWidth3);
			table35.addCell(new Cell().add(String.valueOf(allVagons.get(44))));
			table35.addCell(new Cell().add(String.valueOf(allVagons.get(45))));
			table35.addCell(new Cell().add(String.valueOf(allVagons.get(45) - allVagons.get(44))));

			table.addCell(table35);

			//Boshqa turdagi(проч)
			Table table36 = new Table(columnWidth3);
			table36.addCell(new Cell().add(String.valueOf(allVagons.get(46))));
			table36.addCell(new Cell().add(String.valueOf(allVagons.get(47))));
			table36.addCell(new Cell().add(String.valueOf(allVagons.get(47) - allVagons.get(46))));

			table.addCell(table36);


			Paragraph paragraphKr = new Paragraph("Kapital ta'mir(КР)");
			paragraphKr.setTextAlignment(TextAlignment.CENTER); // Setting text alignment to cell1
			paragraphKr.setFontSize(10);



			Table table37 = new Table(columnWidth);
			table37.setTextAlignment(TextAlignment.CENTER);
			table37.setFontSize(5);
			table37.addCell(new Cell().add("\n\n VCHD "));

	//			Jami
			Table table38 = new Table(columnWidth1);
			table38.addCell(new Cell().add("Jami "));

			Table table39 = new Table(columnWidth2);
			table39.addCell(new Cell().add("Plan"));
			table39.addCell(new Cell().add("Fact"));
			table39.addCell(new Cell().add("+/-"));

			table38.addCell(table39);
			table37.addCell(table38);

	//			Yopiq vagon
			Table table40 = new Table(columnWidth1);
			table40.addCell(new Cell().add("Yopiq  vagon (крыт)"));

			Table table41 = new Table(columnWidth2);
			table41.addCell(new Cell().add("Plan"));
			table41.addCell(new Cell().add("Fact"));
			table41.addCell(new Cell().add("+/-"));

			table40.addCell(table41);
			table37.addCell(table40);

	//			Platforma(пф)
			Table table42 = new Table(columnWidth1);
			table42.addCell(new Cell().add("Platforma (пф) "));

			Table table43 = new Table(columnWidth2);
			table43.addCell(new Cell().add("Plan"));
			table43.addCell(new Cell().add("Fact"));
			table43.addCell(new Cell().add("+/-"));

			table42.addCell(table43);
			table37.addCell(table42);

	//			Yarim ochiq vagon(пв)
			Table table44 = new Table(columnWidth1);
			table44.addCell(new Cell().add("Yarim ochiq vagon (пв)"));

			Table table45 = new Table(columnWidth2);
			table45.addCell(new Cell().add("Plan"));
			table45.addCell(new Cell().add("Fact"));
			table45.addCell(new Cell().add("+/-"));

			table44.addCell(table45);
			table37.addCell(table44);

	//			Sisterna(цс)
			Table table46 = new Table(columnWidth1);
			table46.addCell(new Cell().add("Sisterna (цс) "));

			Table table47 = new Table(columnWidth2);
			table47.addCell(new Cell().add("Plan"));
			table47.addCell(new Cell().add("Fact"));
			table47.addCell(new Cell().add("+/-"));

			table46.addCell(table47);
			table37.addCell(table46);

	//			Boshqa turdagi(проч)
			Table table48 = new Table(columnWidth1);
			table48.addCell(new Cell().add("Boshqa turdagi (проч)"));

			Table table49 = new Table(columnWidth2);
			table49.addCell(new Cell().add("Plan"));
			table49.addCell(new Cell().add("Fact"));
			table49.addCell(new Cell().add("+/-"));

			table48.addCell(table49);
			table37.addCell(table48);

	//VALUE LAR
			table37.addCell("VCHD-3");
			//JAmi
			Table table50 = new Table(columnWidth3);
			table50.addCell(new Cell().add(String.valueOf(allVagons.get(48))));
			table50.addCell(new Cell().add(String.valueOf(allVagons.get(49))));
			table50.addCell(new Cell().add(String.valueOf(allVagons.get(49) - allVagons.get(48))));

			table37.addCell(table50);

			//Yopiq vagon (крыт)
			Table table51 = new Table(columnWidth3);
			table51.addCell(new Cell().add(String.valueOf(allVagons.get(50))));
			table51.addCell(new Cell().add(String.valueOf(allVagons.get(51))));
			table51.addCell(new Cell().add(String.valueOf(allVagons.get(51) - allVagons.get(50))));

			table37.addCell(table51);

			//Platforma(пф)
			Table table52 = new Table(columnWidth3);
			table52.addCell(new Cell().add(String.valueOf(allVagons.get(52))));
			table52.addCell(new Cell().add(String.valueOf(allVagons.get(53))));
			table52.addCell(new Cell().add(String.valueOf(allVagons.get(53) - allVagons.get(52))));

			table37.addCell(table52);

			//Yarim ochiq vagon(пв)
			Table table53 = new Table(columnWidth3);
			table53.addCell(new Cell().add(String.valueOf(allVagons.get(54))));
			table53.addCell(new Cell().add(String.valueOf(allVagons.get(55))));
			table53.addCell(new Cell().add(String.valueOf(allVagons.get(55) - allVagons.get(54))));

			table37.addCell(table53);

			//Sisterna(цс)
			Table table54 = new Table(columnWidth3);
			table54.addCell(new Cell().add(String.valueOf(allVagons.get(56))));
			table54.addCell(new Cell().add(String.valueOf(allVagons.get(57))));
			table54.addCell(new Cell().add(String.valueOf(allVagons.get(57) - allVagons.get(56))));

			table37.addCell(table54);

			//Boshqa turdagi(проч)
			Table table55 = new Table(columnWidth3);
			table55.addCell(new Cell().add(String.valueOf(allVagons.get(58))));
			table55.addCell(new Cell().add(String.valueOf(allVagons.get(59))));
			table55.addCell(new Cell().add(String.valueOf(allVagons.get(59) - allVagons.get(58))));

			table37.addCell(table55);


			table37.addCell("VCHD-5");
			//JAmi
			Table table56 = new Table(columnWidth3);
			table56.addCell(new Cell().add(String.valueOf(allVagons.get(60))));
			table56.addCell(new Cell().add(String.valueOf(allVagons.get(61))));
			table56.addCell(new Cell().add(String.valueOf(allVagons.get(61) - allVagons.get(60))));

			table37.addCell(table56);

			//Yopiq vagon (крыт)
			Table table57 = new Table(columnWidth3);
			table57.addCell(new Cell().add(String.valueOf(allVagons.get(62))));
			table57.addCell(new Cell().add(String.valueOf(allVagons.get(63))));
			table57.addCell(new Cell().add(String.valueOf(allVagons.get(63) - allVagons.get(62))));

			table37.addCell(table57);

			//Platforma(пф)
			Table table58 = new Table(columnWidth3);
			table58.addCell(new Cell().add(String.valueOf(allVagons.get(64))));
			table58.addCell(new Cell().add(String.valueOf(allVagons.get(65))));
			table58.addCell(new Cell().add(String.valueOf(allVagons.get(65) - allVagons.get(64))));

			table37.addCell(table58);

			//Yarim ochiq vagon(пв)
			Table table59 = new Table(columnWidth3);
			table59.addCell(new Cell().add(String.valueOf(allVagons.get(66))));
			table59.addCell(new Cell().add(String.valueOf(allVagons.get(67))));
			table59.addCell(new Cell().add(String.valueOf(allVagons.get(67) - allVagons.get(66))));

			table37.addCell(table59);

			//Sisterna(цс)
			Table table60 = new Table(columnWidth3);
			table60.addCell(new Cell().add(String.valueOf(allVagons.get(68))));
			table60.addCell(new Cell().add(String.valueOf(allVagons.get(69))));
			table60.addCell(new Cell().add(String.valueOf(allVagons.get(69) - allVagons.get(68))));

			table37.addCell(table60);

			//Boshqa turdagi(проч)
			Table table61 = new Table(columnWidth3);
			table61.addCell(new Cell().add(String.valueOf(allVagons.get(70))));
			table61.addCell(new Cell().add(String.valueOf(allVagons.get(71))));
			table61.addCell(new Cell().add(String.valueOf(allVagons.get(71) - allVagons.get(70))));

			table37.addCell(table61);


			table37.addCell("VCHD-6");

			//JAmi
			Table table62 = new Table(columnWidth3);
			table62.addCell(new Cell().add(String.valueOf(allVagons.get(72))));
			table62.addCell(new Cell().add(String.valueOf(allVagons.get(73))));
			table62.addCell(new Cell().add(String.valueOf(allVagons.get(73) - allVagons.get(72))));

			table37.addCell(table62);

			//Yopiq vagon (крыт)
			Table table63 = new Table(columnWidth3);
			table63.addCell(new Cell().add(String.valueOf(allVagons.get(74))));
			table63.addCell(new Cell().add(String.valueOf(allVagons.get(75))));
			table63.addCell(new Cell().add(String.valueOf(allVagons.get(75) - allVagons.get(74))));

			table37.addCell(table63);

			//Platforma(пф)
			Table table64 = new Table(columnWidth3);
			table64.addCell(new Cell().add(String.valueOf(allVagons.get(76))));
			table64.addCell(new Cell().add(String.valueOf(allVagons.get(77))));
			table64.addCell(new Cell().add(String.valueOf(allVagons.get(77) - allVagons.get(76))));

			table37.addCell(table64);

			//Yarim ochiq vagon(пв)
			Table table65 = new Table(columnWidth3);
			table65.addCell(new Cell().add(String.valueOf(allVagons.get(78))));
			table65.addCell(new Cell().add(String.valueOf(allVagons.get(79))));
			table65.addCell(new Cell().add(String.valueOf(allVagons.get(79) - allVagons.get(78))));

			table37.addCell(table65);

			//Sisterna(цс)
			Table table66 = new Table(columnWidth3);
			table66.addCell(new Cell().add(String.valueOf(allVagons.get(80))));
			table66.addCell(new Cell().add(String.valueOf(allVagons.get(81))));
			table66.addCell(new Cell().add(String.valueOf(allVagons.get(81) - allVagons.get(80))));

			table37.addCell(table66);

			//Boshqa turdagi(проч)
			Table table67 = new Table(columnWidth3);
			table67.addCell(new Cell().add(String.valueOf(allVagons.get(82))));
			table67.addCell(new Cell().add(String.valueOf(allVagons.get(83))));
			table67.addCell(new Cell().add(String.valueOf(allVagons.get(83) - allVagons.get(82))));

			table37.addCell(table67);

			table37.addCell("O'zvagonta'mir");

			//JAmi
			Table table68 = new Table(columnWidth3);
			table68.addCell(new Cell().add(String.valueOf(allVagons.get(84))));
			table68.addCell(new Cell().add(String.valueOf(allVagons.get(85))));
			table68.addCell(new Cell().add(String.valueOf(allVagons.get(85) - allVagons.get(84))));

			table37.addCell(table68);

			//Yopiq vagon (крыт)
			Table table69 = new Table(columnWidth3);
			table69.addCell(new Cell().add(String.valueOf(allVagons.get(86))));
			table69.addCell(new Cell().add(String.valueOf(allVagons.get(87))));
			table69.addCell(new Cell().add(String.valueOf(allVagons.get(87) - allVagons.get(86))));

			table37.addCell(table69);

			//Platforma(пф)
			Table table70 = new Table(columnWidth3);
			table70.addCell(new Cell().add(String.valueOf(allVagons.get(88))));
			table70.addCell(new Cell().add(String.valueOf(allVagons.get(89))));
			table70.addCell(new Cell().add(String.valueOf(allVagons.get(89) - allVagons.get(88))));

			table37.addCell(table70);

			//Yarim ochiq vagon(пв)
			Table table71 = new Table(columnWidth3);
			table71.addCell(new Cell().add(String.valueOf(allVagons.get(90))));
			table71.addCell(new Cell().add(String.valueOf(allVagons.get(91))));
			table71.addCell(new Cell().add(String.valueOf(allVagons.get(91) - allVagons.get(90))));

			table37.addCell(table71);

			//Sisterna(цс)
			Table table72 = new Table(columnWidth3);
			table72.addCell(new Cell().add(String.valueOf(allVagons.get(92))));
			table72.addCell(new Cell().add(String.valueOf(allVagons.get(93))));
			table72.addCell(new Cell().add(String.valueOf(allVagons.get(93) - allVagons.get(92))));

			table37.addCell(table72);

			//Boshqa turdagi(проч)
			Table table73 = new Table(columnWidth3);
			table73.addCell(new Cell().add(String.valueOf(allVagons.get(94))));
			table73.addCell(new Cell().add(String.valueOf(allVagons.get(95))));
			table73.addCell(new Cell().add(String.valueOf(allVagons.get(95) - allVagons.get(94))));

			table37.addCell(table73);

			Paragraph paragraphKrp = new Paragraph("KRP(КРП)");
			paragraphKrp.setTextAlignment(TextAlignment.CENTER); // Setting text alignment to cell1
			paragraphKrp.setFontSize(10);



			Table table74 = new Table(columnWidth);
			table74.setTextAlignment(TextAlignment.CENTER);
			table74.setFontSize(5);
			table74.addCell(new Cell().add("\n\n VCHD "));

	//			Jami
			Table table75 = new Table(columnWidth1);
			table75.addCell(new Cell().add("Jami "));

			Table table76 = new Table(columnWidth2);
			table76.addCell(new Cell().add("Plan"));
			table76.addCell(new Cell().add("Fact"));
			table76.addCell(new Cell().add("+/-"));

			table75.addCell(table76);
			table74.addCell(table75);

	//			Yopiq vagon
			Table table77 = new Table(columnWidth1);
			table77.addCell(new Cell().add("Yopiq  vagon (крыт)"));

			Table table78 = new Table(columnWidth2);
			table78.addCell(new Cell().add("Plan"));
			table78.addCell(new Cell().add("Fact"));
			table78.addCell(new Cell().add("+/-"));

			table77.addCell(table78);
			table74.addCell(table77);

	//			Platforma(пф)
			Table table79 = new Table(columnWidth1);
			table79.addCell(new Cell().add("Platforma (пф) "));

			Table table80 = new Table(columnWidth2);
			table80.addCell(new Cell().add("Plan"));
			table80.addCell(new Cell().add("Fact"));
			table80.addCell(new Cell().add("+/-"));

			table79.addCell(table80);
			table74.addCell(table79);

	//			Yarim ochiq vagon(пв)
			Table table81 = new Table(columnWidth1);
			table81.addCell(new Cell().add("Yarim ochiq vagon (пв)"));

			Table table82 = new Table(columnWidth2);
			table82.addCell(new Cell().add("Plan"));
			table82.addCell(new Cell().add("Fact"));
			table82.addCell(new Cell().add("+/-"));

			table81.addCell(table82);
			table74.addCell(table81);

	//			Sisterna(цс)
			Table table83 = new Table(columnWidth1);
			table83.addCell(new Cell().add("Sisterna (цс) "));

			Table table84 = new Table(columnWidth2);
			table84.addCell(new Cell().add("Plan"));
			table84.addCell(new Cell().add("Fact"));
			table84.addCell(new Cell().add("+/-"));

			table83.addCell(table84);
			table74.addCell(table83);

	//			Boshqa turdagi(проч)
			Table table85 = new Table(columnWidth1);
			table85.addCell(new Cell().add("Boshqa turdagi (проч)"));

			Table table86 = new Table(columnWidth2);
			table86.addCell(new Cell().add("Plan"));
			table86.addCell(new Cell().add("Fact"));
			table86.addCell(new Cell().add("+/-"));

			table85.addCell(table86);
			table74.addCell(table85);

	//VALUE LAR
			table74.addCell("VCHD-3");
			//JAmi
			Table table87 = new Table(columnWidth3);
			table87.addCell(new Cell().add(String.valueOf(allVagons.get(96))));
			table87.addCell(new Cell().add(String.valueOf(allVagons.get(97))));
			table87.addCell(new Cell().add(String.valueOf(allVagons.get(97) - allVagons.get(96))));

			table74.addCell(table87);

			//Yopiq vagon (крыт)
			Table table88 = new Table(columnWidth3);
			table88.addCell(new Cell().add(String.valueOf(allVagons.get(98))));
			table88.addCell(new Cell().add(String.valueOf(allVagons.get(99))));
			table88.addCell(new Cell().add(String.valueOf(allVagons.get(99) - allVagons.get(98))));

			table74.addCell(table88);

			//Platforma(пф)
			Table table89 = new Table(columnWidth3);
			table89.addCell(new Cell().add(String.valueOf(allVagons.get(100))));
			table89.addCell(new Cell().add(String.valueOf(allVagons.get(101))));
			table89.addCell(new Cell().add(String.valueOf(allVagons.get(101) - allVagons.get(100))));

			table74.addCell(table89);

			//Yarim ochiq vagon(пв)
			Table table90 = new Table(columnWidth3);
			table90.addCell(new Cell().add(String.valueOf(allVagons.get(102))));
			table90.addCell(new Cell().add(String.valueOf(allVagons.get(103))));
			table90.addCell(new Cell().add(String.valueOf(allVagons.get(103) - allVagons.get(102))));

			table74.addCell(table90);

			//Sisterna(цс)
			Table table91 = new Table(columnWidth3);
			table91.addCell(new Cell().add(String.valueOf(allVagons.get(104))));
			table91.addCell(new Cell().add(String.valueOf(allVagons.get(105))));
			table91.addCell(new Cell().add(String.valueOf(allVagons.get(105) - allVagons.get(104))));

			table74.addCell(table91);

			//Boshqa turdagi(проч)
			Table table92 = new Table(columnWidth3);
			table92.addCell(new Cell().add(String.valueOf(allVagons.get(106))));
			table92.addCell(new Cell().add(String.valueOf(allVagons.get(107))));
			table92.addCell(new Cell().add(String.valueOf(allVagons.get(107) - allVagons.get(106))));

			table74.addCell(table92);


			table74.addCell("VCHD-5");
			//JAmi
			Table table93 = new Table(columnWidth3);
			table93.addCell(new Cell().add(String.valueOf(allVagons.get(108))));
			table93.addCell(new Cell().add(String.valueOf(allVagons.get(109))));
			table93.addCell(new Cell().add(String.valueOf(allVagons.get(109) - allVagons.get(108))));

			table74.addCell(table93);

			//Yopiq vagon (крыт)
			Table table94 = new Table(columnWidth3);
			table94.addCell(new Cell().add(String.valueOf(allVagons.get(110))));
			table94.addCell(new Cell().add(String.valueOf(allVagons.get(111))));
			table94.addCell(new Cell().add(String.valueOf(allVagons.get(111) - allVagons.get(110))));

			table74.addCell(table94);

			//Platforma(пф)
			Table table95 = new Table(columnWidth3);
			table95.addCell(new Cell().add(String.valueOf(allVagons.get(112))));
			table95.addCell(new Cell().add(String.valueOf(allVagons.get(113))));
			table95.addCell(new Cell().add(String.valueOf(allVagons.get(113) - allVagons.get(112))));

			table74.addCell(table95);

			//Yarim ochiq vagon(пв)
			Table table96 = new Table(columnWidth3);
			table96.addCell(new Cell().add(String.valueOf(allVagons.get(114))));
			table96.addCell(new Cell().add(String.valueOf(allVagons.get(115))));
			table96.addCell(new Cell().add(String.valueOf(allVagons.get(115) - allVagons.get(114))));

			table74.addCell(table96);

			//Sisterna(цс)
			Table table97 = new Table(columnWidth3);
			table97.addCell(new Cell().add(String.valueOf(allVagons.get(116))));
			table97.addCell(new Cell().add(String.valueOf(allVagons.get(117))));
			table97.addCell(new Cell().add(String.valueOf(allVagons.get(117) - allVagons.get(116))));

			table74.addCell(table97);

			//Boshqa turdagi(проч)
			Table table98 = new Table(columnWidth3);
			table98.addCell(new Cell().add(String.valueOf(allVagons.get(118))));
			table98.addCell(new Cell().add(String.valueOf(allVagons.get(119))));
			table98.addCell(new Cell().add(String.valueOf(allVagons.get(119) - allVagons.get(118))));

			table74.addCell(table98);


			table74.addCell("VCHD-6");

			//JAmi
			Table table99 = new Table(columnWidth3);
			table99.addCell(new Cell().add(String.valueOf(allVagons.get(120))));
			table99.addCell(new Cell().add(String.valueOf(allVagons.get(121))));
			table99.addCell(new Cell().add(String.valueOf(allVagons.get(121) - allVagons.get(120))));

			table74.addCell(table99);

			//Yopiq vagon (крыт)
			Table table100 = new Table(columnWidth3);
			table100.addCell(new Cell().add(String.valueOf(allVagons.get(122))));
			table100.addCell(new Cell().add(String.valueOf(allVagons.get(123))));
			table100.addCell(new Cell().add(String.valueOf(allVagons.get(123) - allVagons.get(122))));

			table74.addCell(table100);

			//Platforma(пф)
			Table table101 = new Table(columnWidth3);
			table101.addCell(new Cell().add(String.valueOf(allVagons.get(124))));
			table101.addCell(new Cell().add(String.valueOf(allVagons.get(125))));
			table101.addCell(new Cell().add(String.valueOf(allVagons.get(125) - allVagons.get(124))));

			table74.addCell(table101);

			//Yarim ochiq vagon(пв)
			Table table102 = new Table(columnWidth3);
			table102.addCell(new Cell().add(String.valueOf(allVagons.get(126))));
			table102.addCell(new Cell().add(String.valueOf(allVagons.get(127))));
			table102.addCell(new Cell().add(String.valueOf(allVagons.get(127) - allVagons.get(126))));

			table74.addCell(table102);

			//Sisterna(цс)
			Table table103 = new Table(columnWidth3);
			table103.addCell(new Cell().add(String.valueOf(allVagons.get(128))));
			table103.addCell(new Cell().add(String.valueOf(allVagons.get(129))));
			table103.addCell(new Cell().add(String.valueOf(allVagons.get(129) - allVagons.get(128))));

			table74.addCell(table103);

			//Boshqa turdagi(проч)
			Table table104 = new Table(columnWidth3);
			table104.addCell(new Cell().add(String.valueOf(allVagons.get(130))));
			table104.addCell(new Cell().add(String.valueOf(allVagons.get(131))));
			table104.addCell(new Cell().add(String.valueOf(allVagons.get(131) - allVagons.get(130))));

			table74.addCell(table104);

			table74.addCell("O'zvagonta'mir");

			//JAmi
			Table table105 = new Table(columnWidth3);
			table105.addCell(new Cell().add(String.valueOf(allVagons.get(132))));
			table105.addCell(new Cell().add(String.valueOf(allVagons.get(133))));
			table105.addCell(new Cell().add(String.valueOf(allVagons.get(133) - allVagons.get(132))));

			table74.addCell(table105);

			//Yopiq vagon (крыт)
			Table table106 = new Table(columnWidth3);
			table106.addCell(new Cell().add(String.valueOf(allVagons.get(134))));
			table106.addCell(new Cell().add(String.valueOf(allVagons.get(135))));
			table106.addCell(new Cell().add(String.valueOf(allVagons.get(135) - allVagons.get(134))));

			table74.addCell(table106);

			//Platforma(пф)
			Table table107 = new Table(columnWidth3);
			table107.addCell(new Cell().add(String.valueOf(allVagons.get(136))));
			table107.addCell(new Cell().add(String.valueOf(allVagons.get(137))));
			table107.addCell(new Cell().add(String.valueOf(allVagons.get(137) - allVagons.get(136))));

			table74.addCell(table107);

			//Yarim ochiq vagon(пв)
			Table table108 = new Table(columnWidth3);
			table108.addCell(new Cell().add(String.valueOf(allVagons.get(138))));
			table108.addCell(new Cell().add(String.valueOf(allVagons.get(139))));
			table108.addCell(new Cell().add(String.valueOf(allVagons.get(139) - allVagons.get(138))));

			table74.addCell(table108);

			//Sisterna(цс)
			Table table109 = new Table(columnWidth3);
			table109.addCell(new Cell().add(String.valueOf(allVagons.get(140))));
			table109.addCell(new Cell().add(String.valueOf(allVagons.get(141))));
			table109.addCell(new Cell().add(String.valueOf(allVagons.get(141) - allVagons.get(140))));

			table74.addCell(table109);

			//Boshqa turdagi(проч)
			Table table110 = new Table(columnWidth3);
			table110.addCell(new Cell().add(String.valueOf(allVagons.get(142))));
			table110.addCell(new Cell().add(String.valueOf(allVagons.get(143))));
			table110.addCell(new Cell().add(String.valueOf(allVagons.get(143) - allVagons.get(142))));

			table74.addCell(table110);

			Paragraph paragraphYolovchi = new Paragraph("Yo'lovchi vagonlar");
			paragraphYolovchi.setTextAlignment(TextAlignment.CENTER); // Setting text alignment to cell1
			paragraphYolovchi.setFontSize(10);

			float[] columnWidthYolovchi = {200f,200f,200f,200f};
			Table table111 = new Table(columnWidthYolovchi);
			table111.setFontSize(5);
			table111.setTextAlignment(TextAlignment.CENTER);
			table111.addCell(new Cell().add("VCHD-5"));
			table111.addCell(new Cell().add("Plan"));
			table111.addCell(new Cell().add("Fact"));
			table111.addCell(new Cell().add("+/-"));

			table111.addCell(new Cell().add("TO-3"));
			table111.addCell(new Cell().add(String.valueOf(allVagons.get(144))));
			table111.addCell(new Cell().add(String.valueOf(allVagons.get(145))));
			table111.addCell(new Cell().add(String.valueOf(allVagons.get(145) - allVagons.get(144))));

			table111.addCell(new Cell().add("Depo ta'mir(ДР)"));
			table111.addCell(new Cell().add(String.valueOf(allVagons.get(146))));
			table111.addCell(new Cell().add(String.valueOf(allVagons.get(147))));
			table111.addCell(new Cell().add(String.valueOf(allVagons.get(147) - allVagons.get(146))));

			doc.add(paragraph);
			doc.add(paragraphDr);
			doc.add(table);
			doc.add(paragraphKr);
			doc.add(table37);
			doc.add(paragraphKrp);
			doc.add(table74);
			doc.add(paragraphYolovchi);
			doc.add(table111);
			doc.close();
			FileInputStream in = new FileInputStream(file.getAbsoluteFile());
			FileCopyUtils.copy(in, response.getOutputStream());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}




//Generate Excel
//	@Override
//	public void exportTableToExcel(List<Integer> drTable, List<Integer> krTable, List<Integer> krpTable, List<Integer> yolovchiTable, HttpServletResponse response) throws IOException {
//		response.setContentType("application/octet-stream");
//		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
//		String currentDateTime = dateFormatter.format(new Date());
//
//		String headerKey = "Content-Disposition";
//		String headerValue = "attachment; filename=Biznes ta'mir boyicha ma'lumot " +
//				"" + currentDateTime + ".xlsx";
//		response.setHeader(headerKey, headerValue);
//
//		ExcelGenerator generator = new ExcelGenerator(drTable, krTable, krpTable, yolovchiTable);
//		generator.generateExcelFile(response);
//	}
//	public static class ExcelGenerator {
//
//		//		private final List < Integer > data;
//		private final List < Integer > drTable;
//		private final List < Integer > krTable;
//		private final List < Integer > krpTable;
//		private final List < Integer > yolovchiTable;
//		private final XSSFWorkbook workbook;
//		private XSSFSheet sheet;
//
//		public ExcelGenerator(List<Integer> drTable, List<Integer> krTable, List<Integer> krpTable, List<Integer> yolovchiTable) {
//			this.drTable = drTable;
//			this.krTable = krTable;
//			this.krpTable = krpTable;
//			this.yolovchiTable = yolovchiTable;
//			workbook = new XSSFWorkbook();
//		}
//
//		private void createCell(Row row, int columnCount, Object valueOfCell, CellStyle style) {
//			sheet.autoSizeColumn(columnCount);
//			Cell cell = row.createCell(columnCount);
//			if (valueOfCell instanceof Integer) {
//				cell.setCellValue((Integer) valueOfCell);
//			} else if (valueOfCell instanceof Long) {
//				cell.setCellValue((Long) valueOfCell);
//			} else if (valueOfCell instanceof Date) {
//				cell.setCellValue((Date) valueOfCell);
//			}else if (valueOfCell instanceof String) {
//				cell.setCellValue((String) valueOfCell);
//			} else {
//				cell.setCellValue((Boolean) valueOfCell);
//			}
//			cell.setCellStyle(style);
//		}
//
//		private void writeHeader() {
//			sheet = workbook.createSheet("Biznes reja - Tamir boyicha malumot");
//
//			Row row4 = sheet.createRow(0);
//			CellStyle style = workbook.createCellStyle();
//			XSSFFont font = workbook.createFont();
//			//			font.setBold(true);
//			font.setFontHeight(12);
//			style.setFont(font);
//
//			sheet.addMergedRegion(CellRangeAddress.valueOf("B1:D1"));
//			sheet.addMergedRegion(CellRangeAddress.valueOf("E1:G1"));
//			sheet.addMergedRegion(CellRangeAddress.valueOf("H1:J1"));
//			sheet.addMergedRegion(CellRangeAddress.valueOf("K1:M1"));
//			sheet.addMergedRegion(CellRangeAddress.valueOf("N1:P1"));
//			sheet.addMergedRegion(CellRangeAddress.valueOf("Q1:S1"));
//
//			createCell(row4, 0, "Depo ta'mir(ДР)", style);
//			createCell(row4, 1, "Jami", style);
//			createCell(row4, 4, "Yopiq vagon (крыт)", style);
//			createCell(row4, 7, "Platforma(пф)", style);
//			createCell(row4, 10, "Yarim ochiq vagon(пв)", style);
//			createCell(row4, 13, "Sisterna(цс)", style);
//			createCell(row4, 16, "Boshqa turdagi(проч)", style);
//
//			Row row = sheet.createRow(1);
//
//			font.setFontHeight(12);
//
//			style.setFont(font);
//
//			createCell(row, 0, "VCHD", style);
//
//			createCell(row, 1, "Plan", style);
//			createCell(row, 2, "Fakt", style);
//			createCell(row, 3, "+/-", style);
//
//			createCell(row, 4, "Plan", style);
//			createCell(row, 5, "Fakt", style);
//			createCell(row, 6, "+/-", style);
//
//			createCell(row, 7, "Plan", style);
//			createCell(row, 8, "Fakt", style);
//			createCell(row, 9, "+/-", style);
//
//			createCell(row, 10, "Plan", style);
//			createCell(row, 11, "Fakt", style);
//			createCell(row, 12, "+/-", style);
//
//			createCell(row, 13, "Plan", style);
//			createCell(row, 14, "Fakt", style);
//			createCell(row, 15, "+/-", style);
//
//			createCell(row, 16, "Plan", style);
//			createCell(row, 17, "Fakt", style);
//			createCell(row, 18, "+/-", style);
//
//
//		}
//		private void write() {
//			CellStyle style = workbook.createCellStyle();
//			XSSFFont font = workbook.createFont();
//			font.setFontHeight(12);
//			style.setFont(font);
//
//			Row row = sheet.createRow(2);
//			createCell(row, 0, "VCHD-3", style);
//
//			int colC = 1;
//			for (int i=0; i<18; i++){
//				createCell(row, colC++, drTable.get(i), style);
//				if (colC ==19)
//					colC=1;
//			}
//
//			Row row1 = sheet.createRow(3);
//			createCell(row1, 0, "VCHD-5", style);
//			for (int i=18; i<36; i++){
//				createCell(row1, colC++, drTable.get(i), style);
//				if (colC ==19)
//					colC=1;
//			}
//
//			Row row2 = sheet.createRow(4);
//			createCell(row2, 0, "VCHD-6", style);
//			for (int i=36; i<54; i++){
//				createCell(row2, colC++, drTable.get(i), style);
//				if (colC ==19)
//					colC=1;
//			}
//
//			Row row3 = sheet.createRow(5);
//			createCell(row3, 0, "O'zvagonta'mir", style);
//			for (int i=54; i<72; i++){
//				createCell(row3, colC++, drTable.get(i), style);
//				if (colC ==19)
//					colC=1;
//			}
//
//		}
//
//		private void writeHeaderKr() {
//			Row row7 = sheet.createRow(7);
//			CellStyle style = workbook.createCellStyle();
//			XSSFFont font = workbook.createFont();
//			font.setFontHeight(12);
//			style.setFont(font);
//
//			sheet.addMergedRegion(CellRangeAddress.valueOf("B8:D8"));
//			sheet.addMergedRegion(CellRangeAddress.valueOf("E8:G8"));
//			sheet.addMergedRegion(CellRangeAddress.valueOf("H8:J8"));
//			sheet.addMergedRegion(CellRangeAddress.valueOf("K8:M8"));
//			sheet.addMergedRegion(CellRangeAddress.valueOf("N8:P8"));
//			sheet.addMergedRegion(CellRangeAddress.valueOf("Q8:S8"));
//
//			createCell(row7, 0, "Kapital ta'mir(КР)", style);
//			createCell(row7, 1, "Jami", style);
//			createCell(row7, 4, "Yopiq vagon (крыт)", style);
//			createCell(row7, 7, "Platforma(пф)", style);
//			createCell(row7, 10, "Yarim ochiq vagon(пв)", style);
//			createCell(row7, 13, "Sisterna(цс)", style);
//			createCell(row7, 16, "Boshqa turdagi(проч)", style);
//
//			Row row8 = sheet.createRow(8);
//			font.setFontHeight(12);
//			style.setFont(font);
//
//			createCell(row8, 0, "VCHD", style);
//
//			createCell(row8, 1, "Plan", style);
//			createCell(row8, 2, "Fakt", style);
//			createCell(row8, 3, "+/-", style);
//
//			createCell(row8, 4, "Plan", style);
//			createCell(row8, 5, "Fakt", style);
//			createCell(row8, 6, "+/-", style);
//
//			createCell(row8, 7, "Plan", style);
//			createCell(row8, 8, "Fakt", style);
//			createCell(row8, 9, "+/-", style);
//
//			createCell(row8, 10, "Plan", style);
//			createCell(row8, 11, "Fakt", style);
//			createCell(row8, 12, "+/-", style);
//
//			createCell(row8, 13, "Plan", style);
//			createCell(row8, 14, "Fakt", style);
//			createCell(row8, 15, "+/-", style);
//
//			createCell(row8, 16, "Plan", style);
//			createCell(row8, 17, "Fakt", style);
//			createCell(row8, 18, "+/-", style);
//		}
//		private void writeKr() {
//			CellStyle style = workbook.createCellStyle();
//			XSSFFont font = workbook.createFont();
//			font.setFontHeight(12);
//			style.setFont(font);
//
//			Row row9 = sheet.createRow(9);
//			createCell(row9, 0, "VCHD-3", style);
//
//			int colC = 1;
//			for (int i=0; i<18; i++){
//				createCell(row9, colC++, krTable.get(i), style);
//				if (colC ==19)
//					colC=1;
//			}
//
//			Row row10 = sheet.createRow(10);
//			createCell(row10, 0, "VCHD-5", style);
//			for (int i=18; i<36; i++){
//				createCell(row10, colC++, krTable.get(i), style);
//				if (colC ==19)
//					colC=1;
//			}
//
//			Row row11 = sheet.createRow(11);
//			createCell(row11, 0, "VCHD-6", style);
//			for (int i=36; i<54; i++){
//				createCell(row11, colC++, krTable.get(i), style);
//				if (colC ==19)
//					colC=1;
//			}
//
//			Row row12 = sheet.createRow(12);
//			createCell(row12, 0, "O'zvagonta'mir", style);
//			for (int i=54; i<72; i++){
//				createCell(row12, colC++, krTable.get(i), style);
//				if (colC ==19)
//					colC=1;
//			}
//
//		}
//
//		private void writeHeaderKrp() {
//			Row row15 = sheet.createRow(15);
//			CellStyle style = workbook.createCellStyle();
//			XSSFFont font = workbook.createFont();
//			font.setFontHeight(12);
//			style.setFont(font);
//
//			sheet.addMergedRegion(CellRangeAddress.valueOf("B16:D16"));
//			sheet.addMergedRegion(CellRangeAddress.valueOf("E16:G16"));
//			sheet.addMergedRegion(CellRangeAddress.valueOf("H16:J16"));
//			sheet.addMergedRegion(CellRangeAddress.valueOf("K16:M16"));
//			sheet.addMergedRegion(CellRangeAddress.valueOf("N16:P16"));
//			sheet.addMergedRegion(CellRangeAddress.valueOf("Q16:S16"));
//
//			createCell(row15, 0, "KRP(КРП)", style);
//			createCell(row15, 1, "Jami", style);
//			createCell(row15, 4, "Yopiq vagon (крыт)", style);
//			createCell(row15, 7, "Platforma(пф)", style);
//			createCell(row15, 10, "Yarim ochiq vagon(пв)", style);
//			createCell(row15, 13, "Sisterna(цс)", style);
//			createCell(row15, 16, "Boshqa turdagi(проч)", style);
//
//			Row row16 = sheet.createRow(16);
//			font.setFontHeight(12);
//			style.setFont(font);
//
//			createCell(row16, 0, "VCHD", style);
//
//			createCell(row16, 1, "Plan", style);
//			createCell(row16, 2, "Fakt", style);
//			createCell(row16, 3, "+/-", style);
//
//			createCell(row16, 4, "Plan", style);
//			createCell(row16, 5, "Fakt", style);
//			createCell(row16, 6, "+/-", style);
//
//			createCell(row16, 7, "Plan", style);
//			createCell(row16, 8, "Fakt", style);
//			createCell(row16, 9, "+/-", style);
//
//			createCell(row16, 10, "Plan", style);
//			createCell(row16, 11, "Fakt", style);
//			createCell(row16, 12, "+/-", style);
//
//			createCell(row16, 13, "Plan", style);
//			createCell(row16, 14, "Fakt", style);
//			createCell(row16, 15, "+/-", style);
//
//			createCell(row16, 16, "Plan", style);
//			createCell(row16, 17, "Fakt", style);
//			createCell(row16, 18, "+/-", style);
//		}
//		private void writeKrp() {
//			CellStyle style = workbook.createCellStyle();
//			XSSFFont font = workbook.createFont();
//			font.setFontHeight(12);
//			style.setFont(font);
//
//			Row row17 = sheet.createRow(17);
//			createCell(row17, 0, "VCHD-3", style);
//
//			int colC = 1;
//			for (int i=0; i<18; i++){
//				createCell(row17, colC++, krpTable.get(i), style);
//				if (colC ==19)
//					colC=1;
//			}
//
//			Row row18 = sheet.createRow(18);
//			createCell(row18, 0, "VCHD-5", style);
//			for (int i=18; i<36; i++){
//				createCell(row18, colC++, krpTable.get(i), style);
//				if (colC ==19)
//					colC=1;
//			}
//
//			Row row19 = sheet.createRow(19);
//			createCell(row19, 0, "VCHD-6", style);
//			for (int i=36; i<54; i++){
//				createCell(row19, colC++, krpTable.get(i), style);
//				if (colC ==19)
//					colC=1;
//			}
//
//			Row row20 = sheet.createRow(20);
//			createCell(row20, 0, "O'zvagonta'mir", style);
//			for (int i=54; i<72; i++){
//				createCell(row20, colC++, krpTable.get(i), style);
//				if (colC ==19)
//					colC=1;
//			}
//
//		}
//		private void writeHeaderYolovchi() {
//			Row row23 = sheet.createRow(23);
//			CellStyle style = workbook.createCellStyle();
//			XSSFFont font = workbook.createFont();
//			font.setFontHeight(12);
//			style.setFont(font);
//
//			createCell(row23, 0, "VCHD", style);
//
//			createCell(row23, 1, "Plan", style);
//			createCell(row23, 2, "Fakt", style);
//			createCell(row23, 3, "+/-", style);
//
//
//		}
//		private void writeYolovchi() {
//			CellStyle style = workbook.createCellStyle();
//			XSSFFont font = workbook.createFont();
//			font.setFontHeight(12);
//			style.setFont(font);
//
//			Row row24 = sheet.createRow(24);
//			createCell(row24, 0, "TO-3", style);
//
//			int colC = 1;
//			for (int i=0; i<3; i++){
//				createCell(row24, colC++, yolovchiTable.get(i), style);
//				if (colC ==4)
//					colC=1;
//			}
//
//			Row row25 = sheet.createRow(25);
//			createCell(row25, 0, "Depo ta'mir(ДР)", style);
//			for (int i=3; i<6; i++){
//				createCell(row25, colC++, yolovchiTable.get(i), style);
//				if (colC ==4)
//					colC=1;
//			}
//
//		}
//
//		public void generateExcelFile(HttpServletResponse response) throws IOException {
//			writeHeader();
//			write();
//			writeHeaderKr();
//			writeKr();
//			writeHeaderKrp();
//			writeKrp();
//			writeHeaderYolovchi();
//			writeYolovchi();
//			ServletOutputStream outputStream = response.getOutputStream();
//			workbook.write(outputStream);
//			workbook.close();
//			outputStream.close();
//		}
//	}

//Save Plan
	@Override
	public ApiResponse savePlan(PlanBiznesDto planDto) {

		Optional<PlanBiznes> existsPlan = planBiznesRepository.findByYearAndMonth(planDto.getMonth(), planDto.getYear() );

		if (!existsPlan.isPresent()) {

			PlanBiznes biznesPlan = new PlanBiznes();

			biznesPlan.setYear(planDto.getYear());
			biznesPlan.setMonth(planDto.getMonth());

		//Depoli tamir
			biznesPlan.setSamDtKritiPlanBiznes(planDto.getSamDtKritiPlanBiznes());
			biznesPlan.setSamDtPlatformaPlanBiznes(planDto.getSamDtPlatformaPlanBiznes());
			biznesPlan.setSamDtPoluvagonPlanBiznes(planDto.getSamDtPoluvagonPlanBiznes());
			biznesPlan.setSamDtSisternaPlanBiznes(planDto.getSamDtSisternaPlanBiznes());
			biznesPlan.setSamDtBoshqaPlanBiznes(planDto.getSamDtBoshqaPlanBiznes());

			biznesPlan.setHavDtKritiPlanBiznes(planDto.getHavDtKritiPlanBiznes());
			biznesPlan.setHavDtPlatformaPlanBiznes(planDto.getHavDtPlatformaPlanBiznes());
			biznesPlan.setHavDtPoluvagonPlanBiznes(planDto.getHavDtPoluvagonPlanBiznes());
			biznesPlan.setHavDtSisternaPlanBiznes(planDto.getHavDtSisternaPlanBiznes());
			biznesPlan.setHavDtBoshqaPlanBiznes(planDto.getHavDtBoshqaPlanBiznes());

			biznesPlan.setAndjDtKritiPlanBiznes(planDto.getAndjDtKritiPlanBiznes());
			biznesPlan.setAndjDtPlatformaPlanBiznes(planDto.getAndjDtPlatformaPlanBiznes());
			biznesPlan.setAndjDtPoluvagonPlanBiznes(planDto.getAndjDtPoluvagonPlanBiznes());
			biznesPlan.setAndjDtSisternaPlanBiznes(planDto.getAndjDtSisternaPlanBiznes());
			biznesPlan.setAndjDtBoshqaPlanBiznes(planDto.getAndjDtBoshqaPlanBiznes());

		//Yolovchi
			biznesPlan.setAndjDtYolovchiPlanBiznes(planDto.getAndjDtYolovchiPlanBiznes());
			biznesPlan.setAndjTYolovchiPlanBiznes(planDto.getAndjTYolovchiPlanBiznes());

		//kapital tamir
			biznesPlan.setSamKtKritiPlanBiznes(planDto.getSamKtKritiPlanBiznes());
			biznesPlan.setSamKtPlatformaPlanBiznes(planDto.getSamKtPlatformaPlanBiznes());
			biznesPlan.setSamKtPoluvagonPlanBiznes(planDto.getSamKtPoluvagonPlanBiznes());
			biznesPlan.setSamKtSisternaPlanBiznes(planDto.getSamKtSisternaPlanBiznes());
			biznesPlan.setSamKtBoshqaPlanBiznes(planDto.getSamKtBoshqaPlanBiznes());

			biznesPlan.setHavKtKritiPlanBiznes(planDto.getHavKtKritiPlanBiznes());
			biznesPlan.setHavKtPlatformaPlanBiznes(planDto.getHavKtPlatformaPlanBiznes());
			biznesPlan.setHavKtPoluvagonPlanBiznes(planDto.getHavKtPoluvagonPlanBiznes());
			biznesPlan.setHavKtSisternaPlanBiznes(planDto.getHavKtSisternaPlanBiznes());
			biznesPlan.setHavKtBoshqaPlanBiznes(planDto.getHavKtBoshqaPlanBiznes());

			biznesPlan.setAndjKtKritiPlanBiznes(planDto.getAndjKtKritiPlanBiznes());
			biznesPlan.setAndjKtPlatformaPlanBiznes(planDto.getAndjKtPlatformaPlanBiznes());
			biznesPlan.setAndjKtPoluvagonPlanBiznes(planDto.getAndjKtPoluvagonPlanBiznes());
			biznesPlan.setAndjKtSisternaPlanBiznes(planDto.getAndjKtSisternaPlanBiznes());
			biznesPlan.setAndjKtBoshqaPlanBiznes(planDto.getAndjKtBoshqaPlanBiznes());

		//KRP tamir
			biznesPlan.setSamKrpKritiPlanBiznes(planDto.getSamKrpKritiPlanBiznes());
			biznesPlan.setSamKrpPlatformaPlanBiznes(planDto.getSamKrpPlatformaPlanBiznes());
			biznesPlan.setSamKrpPoluvagonPlanBiznes(planDto.getSamKrpPoluvagonPlanBiznes());
			biznesPlan.setSamKrpSisternaPlanBiznes(planDto.getSamKrpSisternaPlanBiznes());
			biznesPlan.setSamKrpBoshqaPlanBiznes(planDto.getSamKrpBoshqaPlanBiznes());

			biznesPlan.setHavKrpKritiPlanBiznes(planDto.getHavKrpKritiPlanBiznes());
			biznesPlan.setHavKrpPlatformaPlanBiznes(planDto.getHavKrpPlatformaPlanBiznes());
			biznesPlan.setHavKrpPoluvagonPlanBiznes(planDto.getHavKrpPoluvagonPlanBiznes());
			biznesPlan.setHavKrpSisternaPlanBiznes(planDto.getHavKrpSisternaPlanBiznes());
			biznesPlan.setHavKrpBoshqaPlanBiznes(planDto.getHavKrpBoshqaPlanBiznes());

			biznesPlan.setAndjKrpKritiPlanBiznes(planDto.getAndjKrpKritiPlanBiznes());
			biznesPlan.setAndjKrpPlatformaPlanBiznes(planDto.getAndjKrpPlatformaPlanBiznes());
			biznesPlan.setAndjKrpPoluvagonPlanBiznes(planDto.getAndjKrpPoluvagonPlanBiznes());
			biznesPlan.setAndjKrpSisternaPlanBiznes(planDto.getAndjKrpSisternaPlanBiznes());
			biznesPlan.setAndjKrpBoshqaPlanBiznes(planDto.getAndjKrpBoshqaPlanBiznes());

			planBiznesRepository.save(biznesPlan);

			return new ApiResponse("Ta'mir re'jasi muvaffaqiyatli saqlandi", true);
		}
		return new ApiResponse(planDto.getMonth()+ "-" + planDto.getYear() + " uchun ta'mir rejasi avval saqlangan", false);
	}

//Listni toldirish uchun
	@Override
	public List<VagonTayyor> findAllByCreatedDate(String createdMonth) {
//		List<VagonTayyor> all = vagonTayyorRepository.findAll();
//		for (VagonTayyor v:all){
//			String keralli;
//			String createdDate = v.getCreatedDate();
//			String boshi = createdDate.substring(0, 3);
//			String substring = createdDate.substring(3, 5);
//			int oxiri = Integer.parseInt(createdDate.substring(6,10));
//			String engOxiri = createdDate.substring(10);
//			int i = Integer.parseInt(substring);
//			++i;
//			if (i==13) {
//				i = 01;
//				String s = String.valueOf(i);
//				++oxiri;
//				keralli = boshi + s +"-" +oxiri + engOxiri;
//				v.setCreatedDate(keralli);
//				vagonTayyorRepository.save(v);
//			}else {
//				keralli = boshi + i +"-" +oxiri + engOxiri;
//				v.setCreatedDate(keralli);
//				vagonTayyorRepository.save(v);
//			}
//		}
		return vagonTayyorRepository.findAllByCreatedDate(createdMonth, Sort.by(Sort.Direction.DESC, "createdDate"));
	}
//Bir oylik Planni toldiradi
	@Override
	public PlanBiznes getPlanBiznes(String month, String year) {
		Optional<PlanBiznes> optionalPlanBiznes = planBiznesRepository.findByYearAndMonth(month, year);
		return optionalPlanBiznes.orElseGet(PlanBiznes::new);
	}
//Jami oylik Planni toldiradi
	@Override
	public PlanBiznes getPlanBiznes(String year) {

		int samDtKritiPlanBiznes = 0;
		int samDtPlatformaPlanBiznes = 0;
		int samDtPoluvagonPlanBiznes = 0;
		int samDtSisternaPlanBiznes = 0;
		int samDtBoshqaPlanBiznes = 0;

		int havDtKritiPlanBiznes = 0;
		int havDtPlatformaPlanBiznes = 0;
		int havDtPoluvagonPlanBiznes = 0;
		int havDtSisternaPlanBiznes = 0;
		int havDtBoshqaPlanBiznes = 0;

		int andjDtKritiPlanBiznes = 0;
		int andjDtPlatformaPlanBiznes = 0;
		int andjDtPoluvagonPlanBiznes = 0;
		int andjDtSisternaPlanBiznes = 0;
		int andjDtBoshqaPlanBiznes = 0;

		int andjDtYolovchiPlanBiznes = 0;
		int andjTYolovchiPlanBiznes = 0;

		int samKtKritiPlanBiznes = 0;
		int samKtPlatformaPlanBiznes = 0;
		int samKtPoluvagonPlanBiznes = 0;
		int samKtSisternaPlanBiznes = 0;
		int samKtBoshqaPlanBiznes = 0;

		int havKtKritiPlanBiznes = 0;
		int havKtPlatformaPlanBiznes = 0;
		int havKtPoluvagonPlanBiznes = 0;
		int havKtSisternaPlanBiznes = 0;
		int havKtBoshqaPlanBiznes = 0;

		int andjKtKritiPlanBiznes = 0;
		int andjKtPlatformaPlanBiznes = 0;
		int andjKtPoluvagonPlanBiznes = 0;
		int andjKtSisternaPlanBiznes = 0;
		int andjKtBoshqaPlanBiznes = 0;

		int samKrpKritiPlanBiznes = 0;
		int samKrpPlatformaPlanBiznes = 0;
		int samKrpPoluvagonPlanBiznes = 0;
		int samKrpSisternaPlanBiznes = 0;
		int samKrpBoshqaPlanBiznes = 0;

		int havKrpKritiPlanBiznes = 0;
		int havKrpPlatformaPlanBiznes = 0;
		int havKrpPoluvagonPlanBiznes = 0;
		int havKrpSisternaPlanBiznes = 0;
		int havKrpBoshqaPlanBiznes = 0;

		int andjKrpKritiPlanBiznes = 0;
		int andjKrpPlatformaPlanBiznes = 0;
		int andjKrpPoluvagonPlanBiznes = 0;
		int andjKrpSisternaPlanBiznes = 0;
		int andjKrpBoshqaPlanBiznes = 0;


		List<PlanBiznes> planList = planBiznesRepository.findByYear(year);
		
		for (PlanBiznes planBiznes : planList){
			samDtKritiPlanBiznes += planBiznes.getSamDtKritiPlanBiznes();
			samDtPlatformaPlanBiznes += planBiznes.getSamDtPlatformaPlanBiznes();
			samDtPoluvagonPlanBiznes += planBiznes.getSamDtPoluvagonPlanBiznes();
			samDtSisternaPlanBiznes += planBiznes.getSamDtSisternaPlanBiznes();
			samDtBoshqaPlanBiznes += planBiznes.getSamDtBoshqaPlanBiznes();

			havDtKritiPlanBiznes += planBiznes.getHavDtKritiPlanBiznes();
			havDtPlatformaPlanBiznes += planBiznes.getHavDtPlatformaPlanBiznes();
			havDtPoluvagonPlanBiznes += planBiznes.getHavDtPoluvagonPlanBiznes();
			havDtSisternaPlanBiznes += planBiznes.getHavDtSisternaPlanBiznes();
			havDtBoshqaPlanBiznes += planBiznes.getHavDtBoshqaPlanBiznes();

			andjDtKritiPlanBiznes += planBiznes.getAndjDtKritiPlanBiznes();
			andjDtPlatformaPlanBiznes += planBiznes.getAndjDtPlatformaPlanBiznes();
			andjDtPoluvagonPlanBiznes += planBiznes.getAndjDtPoluvagonPlanBiznes();
			andjDtSisternaPlanBiznes += planBiznes.getAndjDtSisternaPlanBiznes();
			andjDtBoshqaPlanBiznes += planBiznes.getAndjDtBoshqaPlanBiznes();

			andjDtYolovchiPlanBiznes += planBiznes.getAndjDtYolovchiPlanBiznes();
			andjTYolovchiPlanBiznes += planBiznes.getAndjTYolovchiPlanBiznes();

			samKtKritiPlanBiznes += planBiznes.getSamKtKritiPlanBiznes();
			samKtPlatformaPlanBiznes += planBiznes.getSamKtPlatformaPlanBiznes();
			samKtPoluvagonPlanBiznes += planBiznes.getSamKtPoluvagonPlanBiznes();
			samKtSisternaPlanBiznes += planBiznes.getSamKtSisternaPlanBiznes();
			samKtBoshqaPlanBiznes += planBiznes.getSamKtBoshqaPlanBiznes();

			havKtKritiPlanBiznes += planBiznes.getHavKtKritiPlanBiznes();
			havKtPlatformaPlanBiznes += planBiznes.getHavKtPlatformaPlanBiznes();
			havKtPoluvagonPlanBiznes += planBiznes.getHavKtPoluvagonPlanBiznes();
			havKtSisternaPlanBiznes += planBiznes.getHavKtSisternaPlanBiznes();
			havKtBoshqaPlanBiznes += planBiznes.getHavKtBoshqaPlanBiznes();

			andjKtKritiPlanBiznes += planBiznes.getAndjKtKritiPlanBiznes();
			andjKtPlatformaPlanBiznes += planBiznes.getAndjKtPlatformaPlanBiznes();
			andjKtPoluvagonPlanBiznes += planBiznes.getAndjKtPoluvagonPlanBiznes();
			andjKtSisternaPlanBiznes += planBiznes.getAndjKtSisternaPlanBiznes();
			andjKtBoshqaPlanBiznes += planBiznes.getAndjKtBoshqaPlanBiznes();

			samKrpKritiPlanBiznes += planBiznes.getSamKrpKritiPlanBiznes();
			samKrpPlatformaPlanBiznes += planBiznes.getSamKrpPlatformaPlanBiznes();
			samKrpPoluvagonPlanBiznes += planBiznes.getSamKrpPoluvagonPlanBiznes();
			samKrpSisternaPlanBiznes += planBiznes.getSamKrpSisternaPlanBiznes();
			samKrpBoshqaPlanBiznes += planBiznes.getSamKrpBoshqaPlanBiznes();

			havKrpKritiPlanBiznes += planBiznes.getHavKrpKritiPlanBiznes();
			havKrpPlatformaPlanBiznes += planBiznes.getHavKrpPlatformaPlanBiznes();
			havKrpPoluvagonPlanBiznes += planBiznes.getHavKrpPoluvagonPlanBiznes();
			havKrpSisternaPlanBiznes += planBiznes.getHavKrpSisternaPlanBiznes();
			havKrpBoshqaPlanBiznes += planBiznes.getHavKrpBoshqaPlanBiznes();

			andjKrpKritiPlanBiznes += planBiznes.getAndjKrpKritiPlanBiznes();
			andjKrpPlatformaPlanBiznes += planBiznes.getAndjKrpPlatformaPlanBiznes();
			andjKrpPoluvagonPlanBiznes += planBiznes.getAndjKrpPoluvagonPlanBiznes();
			andjKrpSisternaPlanBiznes += planBiznes.getAndjKrpSisternaPlanBiznes();
			andjKrpBoshqaPlanBiznes += planBiznes.getAndjKrpBoshqaPlanBiznes();
		}

		PlanBiznes plan = new PlanBiznes();

		plan.setSamDtKritiPlanBiznes(samDtKritiPlanBiznes);
		plan.setSamDtPlatformaPlanBiznes(samDtPlatformaPlanBiznes);
		plan.setSamDtPoluvagonPlanBiznes(samDtPoluvagonPlanBiznes);
		plan.setSamDtSisternaPlanBiznes(samDtSisternaPlanBiznes);
		plan.setSamDtBoshqaPlanBiznes(samDtBoshqaPlanBiznes);

		plan.setHavDtKritiPlanBiznes(havDtKritiPlanBiznes);
		plan.setHavDtPlatformaPlanBiznes(havDtPlatformaPlanBiznes);
		plan.setHavDtPoluvagonPlanBiznes(havDtPoluvagonPlanBiznes);
		plan.setHavDtSisternaPlanBiznes(havDtSisternaPlanBiznes);
		plan.setHavDtBoshqaPlanBiznes(havDtBoshqaPlanBiznes);

		plan.setAndjDtKritiPlanBiznes(andjDtKritiPlanBiznes);
		plan.setAndjDtPlatformaPlanBiznes(andjDtPlatformaPlanBiznes);
		plan.setAndjDtPoluvagonPlanBiznes(andjDtPoluvagonPlanBiznes);
		plan.setAndjDtSisternaPlanBiznes(andjDtSisternaPlanBiznes);
		plan.setAndjDtBoshqaPlanBiznes(andjDtBoshqaPlanBiznes);

		plan.setAndjDtYolovchiPlanBiznes(andjDtYolovchiPlanBiznes);
		plan.setAndjTYolovchiPlanBiznes(andjTYolovchiPlanBiznes);

		plan.setSamKtKritiPlanBiznes(samKtKritiPlanBiznes);
		plan.setSamKtPlatformaPlanBiznes(samKtPlatformaPlanBiznes);
		plan.setSamKtPoluvagonPlanBiznes(samKtPoluvagonPlanBiznes);
		plan.setSamKtSisternaPlanBiznes(samKtSisternaPlanBiznes);
		plan.setSamKtBoshqaPlanBiznes(samKtBoshqaPlanBiznes);

		plan.setHavKtKritiPlanBiznes(havKtKritiPlanBiznes);
		plan.setHavKtPlatformaPlanBiznes(havKtPlatformaPlanBiznes);
		plan.setHavKtPoluvagonPlanBiznes(havKtPoluvagonPlanBiznes);
		plan.setHavKtSisternaPlanBiznes(havKtSisternaPlanBiznes);
		plan.setHavKtBoshqaPlanBiznes(havKtBoshqaPlanBiznes);

		plan.setAndjKtKritiPlanBiznes(andjKtKritiPlanBiznes);
		plan.setAndjKtPlatformaPlanBiznes(andjKtPlatformaPlanBiznes);
		plan.setAndjKtPoluvagonPlanBiznes(andjKtPoluvagonPlanBiznes);
		plan.setAndjKtSisternaPlanBiznes(andjKtSisternaPlanBiznes);
		plan.setAndjKtBoshqaPlanBiznes(andjKtBoshqaPlanBiznes);

		plan.setSamKrpKritiPlanBiznes(samKrpKritiPlanBiznes);
		plan.setSamKrpPlatformaPlanBiznes(samKrpPlatformaPlanBiznes);
		plan.setSamKrpPoluvagonPlanBiznes(samKrpPoluvagonPlanBiznes);
		plan.setSamKrpSisternaPlanBiznes(samKrpSisternaPlanBiznes);
		plan.setSamKrpBoshqaPlanBiznes(samKrpBoshqaPlanBiznes);

		plan.setHavKrpKritiPlanBiznes(havKrpKritiPlanBiznes);
		plan.setHavKrpPlatformaPlanBiznes(havKrpPlatformaPlanBiznes);
		plan.setHavKrpPoluvagonPlanBiznes(havKrpPoluvagonPlanBiznes);
		plan.setHavKrpSisternaPlanBiznes(havKrpSisternaPlanBiznes);
		plan.setHavKrpBoshqaPlanBiznes(havKrpBoshqaPlanBiznes);

		plan.setAndjKrpKritiPlanBiznes(andjKrpKritiPlanBiznes);
		plan.setAndjKrpPlatformaPlanBiznes(andjKrpPlatformaPlanBiznes);
		plan.setAndjKrpPoluvagonPlanBiznes(andjKrpPoluvagonPlanBiznes);
		plan.setAndjKrpSisternaPlanBiznes(andjKrpSisternaPlanBiznes);
		plan.setAndjKrpBoshqaPlanBiznes(andjKrpBoshqaPlanBiznes);

		return plan;
	}

// bir oylik fact
	@Override
	public int countAllActiveByDepoNomiVagonTuriAndTamirTuri(String depoNomi, String vagnTuri,
															 String tamirTuri, String oy) {
		return vagonTayyorRepository.countAllActiveByDepoNomiVagonTuriAndTamirTuri(depoNomi, vagnTuri, tamirTuri, oy);
	}

// Jami oylik fact
	@Override
	public int countByDepoNomiVagonTuriAndTamirTuri(String depoNomi, String vagonTuri, String tamirTuri, String year) {
		return vagonTayyorRepository.countByDepoNomiVagonTuriAndTamirTuri(depoNomi, vagonTuri, tamirTuri, year);
	}

// Save new wagon
	@Override
	public ApiResponse saveVagon(VagonTayyor vagon) {
		if(vagon.getNomer() == null)
			return new ApiResponse("Vagon nomeri kiritilishi shart", false);
		Optional<VagonTayyor> exist=vagonTayyorRepository.findByNomer(vagon.getNomer());
		if(exist.isPresent())
			return new ApiResponse(vagon.getNomer() + " nomerili vagon avval saqlangan", false);
		VagonTayyor savedVagon = new VagonTayyor();
		savedVagon.setNomer(vagon.getNomer());
		savedVagon.setDepoNomi(vagon.getDepoNomi());
		savedVagon.setRemontTuri(vagon.getRemontTuri());
		savedVagon.setVagonTuri(vagon.getVagonTuri());
		savedVagon.setIshlabChiqarilganYili(vagon.getIshlabChiqarilganYili());
		savedVagon.setIzoh(vagon.getIzoh());
		savedVagon.setCountry(vagon.getCountry());
		savedVagon.setChiqganVaqti(vagon.getChiqganVaqti());
		savedVagon.setActive(true);

//		String currentDate = minusHours.format(myFormatObj);
//		savedVagon.setCreatedDate(currentDate);
		savedVagon.setCreatedDate(vagon.getCreatedDate());

		vagonTayyorRepository.save(savedVagon);

		return new ApiResponse(vagon.getNomer() + " nomerili vagon saqlandi", true);
	}

	@Override
	public ApiResponse saveVagonSam(VagonTayyor vagon) {
		if(vagon.getNomer() == null)
			return new ApiResponse("Vagon nomeri kiritilishi shart", false);
		Optional<VagonTayyor> exist=	vagonTayyorRepository.findByNomer(vagon.getNomer());
		if(exist.isPresent() )
			return new ApiResponse(vagon.getNomer() + " nomerili vagon avval saqlangan", false);
		if(!vagon.getDepoNomi().equalsIgnoreCase("VCHD-6"))
			return new ApiResponse("Siz faqat VCHD-6 ga ma'lumot saqlashingiz mumkin", false);
		VagonTayyor savedVagon = new VagonTayyor();
		savedVagon.setNomer(vagon.getNomer());
		savedVagon.setDepoNomi(vagon.getDepoNomi());
		savedVagon.setRemontTuri(vagon.getRemontTuri());
		savedVagon.setVagonTuri(vagon.getVagonTuri());
		savedVagon.setIshlabChiqarilganYili(vagon.getIshlabChiqarilganYili());
		savedVagon.setIzoh(vagon.getIzoh());
		savedVagon.setCountry(vagon.getCountry());
		savedVagon.setChiqganVaqti(vagon.getChiqganVaqti());
		savedVagon.setActive(true);

		LocalDateTime today = LocalDateTime.now();
		LocalDateTime minusHours = today.plusHours(5);
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		samDate = minusHours.format(myFormatObj);

//		savedVagon.setCreatedDate(samDate);

		savedVagon.setCreatedDate(vagon.getCreatedDate());

		Optional<LastActionTimes> optionalBiznesTime = utyTimeRepository.findById(1);
		optionalBiznesTime.get().setSamBiznesDate(samDate);
		utyTimeRepository.save(optionalBiznesTime.get());

		vagonTayyorRepository.save(savedVagon);
		return new ApiResponse(vagon.getNomer() + " nomerili vagon saqlandi", true);
	}

	@Override
	public ApiResponse saveVagonHav(VagonTayyor vagon) {
		if(vagon.getNomer() == null)
			return new ApiResponse("Vagon nomeri kiritilishi shart", false);
		Optional<VagonTayyor> exist=	vagonTayyorRepository.findByNomer(vagon.getNomer());
		if(exist.isPresent() )
			return new ApiResponse(vagon.getNomer() + " nomerili vagon avval saqlangan", false);
		if(!vagon.getDepoNomi().equalsIgnoreCase("VCHD-3"))
			return new ApiResponse("Siz faqat VCHD-3 ga ma'lumot saqlashingiz mumkin", false);
		VagonTayyor savedVagon = new VagonTayyor();
		savedVagon.setNomer(vagon.getNomer());
		savedVagon.setDepoNomi(vagon.getDepoNomi());
		savedVagon.setRemontTuri(vagon.getRemontTuri());
		savedVagon.setVagonTuri(vagon.getVagonTuri());
		savedVagon.setIshlabChiqarilganYili(vagon.getIshlabChiqarilganYili());
		savedVagon.setIzoh(vagon.getIzoh());
		savedVagon.setCountry(vagon.getCountry());
		savedVagon.setChiqganVaqti(vagon.getChiqganVaqti());
		savedVagon.setActive(true);

		LocalDateTime today = LocalDateTime.now();
		LocalDateTime minusHours = today.plusHours(5);
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		havDate = minusHours.format(myFormatObj);

//		savedVagon.setCreatedDate(havDate);

		savedVagon.setCreatedDate(vagon.getCreatedDate());

		Optional<LastActionTimes> optionalBiznesTime = utyTimeRepository.findById(1);
		optionalBiznesTime.get().setHavBiznesDate(havDate);
		utyTimeRepository.save(optionalBiznesTime.get());

		vagonTayyorRepository.save(savedVagon);

		return new ApiResponse(vagon.getNomer() + " nomerili vagon saqlandi", true);
	}

	@Override
	public ApiResponse saveVagonAndj(VagonTayyor vagon) {
		if(vagon.getNomer() == null)
			return new ApiResponse("Vagon nomeri kiritilishi shart", false);
		Optional<VagonTayyor> exist=	vagonTayyorRepository.findByNomer(vagon.getNomer());
		if(exist.isPresent() )
			return new ApiResponse(vagon.getNomer() + " nomerili vagon avval saqlangan", false);
		if(!vagon.getDepoNomi().equalsIgnoreCase("VCHD-5"))
			return new ApiResponse("Siz faqat VCHD-5 ga ma'lumot saqlashingiz mumkin", false);
		VagonTayyor savedVagon = new VagonTayyor();
		savedVagon.setNomer(vagon.getNomer());
		savedVagon.setDepoNomi(vagon.getDepoNomi());
		savedVagon.setRemontTuri(vagon.getRemontTuri());
		savedVagon.setVagonTuri(vagon.getVagonTuri());
		savedVagon.setIshlabChiqarilganYili(vagon.getIshlabChiqarilganYili());
		savedVagon.setIzoh(vagon.getIzoh());
		savedVagon.setCountry(vagon.getCountry());
		savedVagon.setChiqganVaqti(vagon.getChiqganVaqti());
		savedVagon.setActive(true);

		LocalDateTime today = LocalDateTime.now();
		LocalDateTime minusHours = today.plusHours(5);
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		andjDate = minusHours.format(myFormatObj);

//		savedVagon.setCreatedDate(andjDate);

		savedVagon.setCreatedDate(vagon.getCreatedDate());

		Optional<LastActionTimes> optionalBiznesTime = utyTimeRepository.findById(1);
		optionalBiznesTime.get().setAndjBiznesDate(andjDate);
		utyTimeRepository.save(optionalBiznesTime.get());

		vagonTayyorRepository.save(savedVagon);

		return new ApiResponse(vagon.getNomer() + " nomerili vagon saqlandi", true);
	}

//Delete wagon
	@Override
	public ApiResponse deleteVagonById(long id) throws NotFoundException {
	Optional<VagonTayyor> exist=	vagonTayyorRepository.findById(id);
	if(exist.isPresent()) {
		vagonTayyorRepository.deleteById(id);
		return new ApiResponse(exist.get().getNomer() + " nomerli vagon o'chirildi", true);
	}
		return new ApiResponse("Vagon topilmadi", false);

}

	@Override
	public ApiResponse deleteVagonSam(long id) throws NotFoundException {
		VagonTayyor exist=	vagonTayyorRepository.findById(id).get();
		if(exist.getDepoNomi().equals("VCHD-6") ) {
			vagonTayyorRepository.deleteById(id);
			LocalDateTime today = LocalDateTime.now();
			LocalDateTime minusHours = today.plusHours(5);
			DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
			samDate = minusHours.format(myFormatObj);

			Optional<LastActionTimes> optionalBiznesTime = utyTimeRepository.findById(1);
			optionalBiznesTime.get().setSamBiznesDate(samDate);
			utyTimeRepository.save(optionalBiznesTime.get());
			return new ApiResponse(exist.getNomer() + " nomerli vagon o'chirildi", true);
		}
		return new ApiResponse("Siz faqat VCHD-6 ga tegishli ma'lumotlarni o'chirishingiz mumkin", false);
	}

	@Override
	public ApiResponse deleteVagonHav(long id) throws NotFoundException{
		VagonTayyor exist=	vagonTayyorRepository.findById(id).get();
		if(exist.getDepoNomi().equals("VCHD-3") ) {
			vagonTayyorRepository.deleteById(id);
			LocalDateTime today = LocalDateTime.now();
			LocalDateTime minusHours = today.plusHours(5);
			DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
			havDate = minusHours.format(myFormatObj);

			Optional<LastActionTimes> optionalBiznesTime = utyTimeRepository.findById(1);
			optionalBiznesTime.get().setHavBiznesDate(havDate);
			utyTimeRepository.save(optionalBiznesTime.get());
			return new ApiResponse(exist.getNomer() + " nomerli vagon o'chirildi", true);
		}
		return new ApiResponse("Siz faqat VCHD-3 ga tegishli ma'lumotlarni o'chirishingiz mumkin", false);
	}

	@Override
	public ApiResponse deleteVagonAndj(long id) throws NotFoundException{
		VagonTayyor exist=	vagonTayyorRepository.findById(id).get();
		if(exist.getDepoNomi().equals("VCHD-5") ) {
			vagonTayyorRepository.deleteById(id);
			LocalDateTime today = LocalDateTime.now();
			LocalDateTime minusHours = today.plusHours(5);
			DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
			andjDate = minusHours.format(myFormatObj);

			Optional<LastActionTimes> optionalBiznesTime = utyTimeRepository.findById(1);
			optionalBiznesTime.get().setAndjBiznesDate(andjDate);
			utyTimeRepository.save(optionalBiznesTime.get());
			return new ApiResponse(exist.getNomer() + " nomerli vagon o'chirildi", true);
		}
		return new ApiResponse("Siz faqat VCHD-3 ga tegishli ma'lumotlarni o'chirishingiz mumkin", false);
	}

//Search By number
	@Override
	public VagonTayyor searchByNomer(Integer nomer, String oy) {
		return vagonTayyorRepository.findByNomerAndCreatedDateContaining(nomer, oy);
	}

//Filter
	//Listni toldirish uchun
	@Override
	public List<VagonTayyor> findAllByDepoNomiVagonTuriAndCountry(String depoNomi, String vagonTuri, String country, String oy) {
		return vagonTayyorRepository.findAllByDepoNomiVagonTuriAndCountry(depoNomi, vagonTuri, country, oy, Sort.by(Sort.Direction.DESC, "createdDate"));
	}
	@Override
	public List<VagonTayyor> findAllByVagonTuriAndCountry(String vagonTuri, String country, String oy) {
		return vagonTayyorRepository.findAllByVagonTuriAndCountry(vagonTuri, country, oy, Sort.by(Sort.Direction.DESC, "createdDate"));
	}
	@Override
	public List<VagonTayyor> findAllBycountry(String country, String oy) {
		return vagonTayyorRepository.findAllBycountry(country, oy, Sort.by(Sort.Direction.DESC, "createdDate"));
	}
	@Override
	public List<VagonTayyor> findAllByDepoNomiAndVagonTuri(String depoNomi, String vagonTuri, String oy) {
		return vagonTayyorRepository.findAllByDepoNomiAndVagonTuri(depoNomi, vagonTuri, oy, Sort.by(Sort.Direction.DESC, "createdDate"));
	}
	@Override
	public List<VagonTayyor> findAllByDepoNomiAndCountry(String depoNomi, String country, String oy) {
		return vagonTayyorRepository.findAllByDepoNomiAndCountry(depoNomi, country, oy, Sort.by(Sort.Direction.DESC, "createdDate"));
	}
	@Override
	public List<VagonTayyor> findAllByVagonTuri(String vagonTuri, String oy) {
		return vagonTayyorRepository.findAllByVagonTuri(vagonTuri, oy, Sort.by(Sort.Direction.DESC, "createdDate"));
	}

	@Override
	public List<VagonTayyor> findAllByDepoNomi(String depoNomi, String oy) {
		return vagonTayyorRepository.findAllByDepoNomi(depoNomi, oy, Sort.by(Sort.Direction.DESC, "createdDate"));
	}
	//tableni toldirish uchun
	@Override
	public int countAllActiveByDepoNomiVagonTuriAndTamirTuri(String depoNomi, String vagnTuri,
															 String tamirTuri, String oy, String country) {
		return vagonTayyorRepository.countAllActiveByDepoNomiVagonTuriAndTamirTuri(depoNomi, vagnTuri, tamirTuri, oy, country);
	}
	@Override
	public int countByDepoNomiVagonTuriTamirTuriAndCountry(String depoNomi, String vagonTuri, String tamirTuri,String country, String year) {
		return vagonTayyorRepository.countByDepoNomiVagonTuriTamirTuriAndCountry(depoNomi, vagonTuri, tamirTuri,country, year);
	}
















	@Override
	public List<VagonTayyor> findAll() {
		return vagonTayyorRepository.findAll( );
	}

	@Override
	public List<VagonTayyor> findAll(String oy) {
		return vagonTayyorRepository.findAll(oy);
	}

	@Override
	public VagonTayyor updateVagon(VagonTayyor vagon, long id) {
		if(vagon.getNomer() == null)
			return null;
		 Optional<VagonTayyor> exist = vagonTayyorRepository.findById(id);
		 if(!exist.isPresent())
			 return null;
		 VagonTayyor savedVagon = exist.get();
		 savedVagon.setId(id);
		 savedVagon.setNomer(vagon.getNomer());
		 savedVagon.setVagonTuri(vagon.getVagonTuri());
		 savedVagon.setDepoNomi(vagon.getDepoNomi());
		 savedVagon.setRemontTuri(vagon.getRemontTuri());
		 savedVagon.setIshlabChiqarilganYili(vagon.getIshlabChiqarilganYili());
		 savedVagon.setIzoh(vagon.getIzoh());
		 savedVagon.setCountry(vagon.getCountry());
		 savedVagon.setChiqganVaqti(vagon.getChiqganVaqti());


		 return vagonTayyorRepository.save(savedVagon);
	}

	@Override
	public VagonTayyor updateVagonSam(VagonTayyor vagon, long id) {
		if(vagon.getNomer() == null)
			return null;
		 Optional<VagonTayyor> exist = vagonTayyorRepository.findById(id);
		 if(exist.get().getDepoNomi().equalsIgnoreCase("VCHD-6") && vagon.getDepoNomi().equalsIgnoreCase("VCHD-6")) {
			 VagonTayyor savedVagon = exist.get();
			 savedVagon.setId(id);
			 savedVagon.setNomer(vagon.getNomer());
			 savedVagon.setVagonTuri(vagon.getVagonTuri());
			 savedVagon.setDepoNomi(vagon.getDepoNomi());
			 savedVagon.setRemontTuri(vagon.getRemontTuri());
			 savedVagon.setIshlabChiqarilganYili(vagon.getIshlabChiqarilganYili());
			 savedVagon.setIzoh(vagon.getIzoh());
			 savedVagon.setCountry(vagon.getCountry());
			 savedVagon.setChiqganVaqti(vagon.getChiqganVaqti());

			 LocalDateTime today = LocalDateTime.now();
			 LocalDateTime minusHours = today.plusHours(5);
			 DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
			 samDate = minusHours.format(myFormatObj);

			 Optional<LastActionTimes> optionalBiznesTime = utyTimeRepository.findById(1);
			 optionalBiznesTime.get().setSamBiznesDate(samDate);
			 utyTimeRepository.save(optionalBiznesTime.get());


			 return vagonTayyorRepository.save(savedVagon);
		 }else
			return null;

	}

	@Override
	public VagonTayyor updateVagonHav(VagonTayyor vagon, long id) {
		if(vagon.getNomer() == null)
			return null;
		 Optional<VagonTayyor> exist = vagonTayyorRepository.findById(id);
		 if(exist.get().getDepoNomi().equalsIgnoreCase("VCHD-3") && vagon.getDepoNomi().equalsIgnoreCase("VCHD-3")) {

			 VagonTayyor savedVagon = exist.get();
			 savedVagon.setId(id);
			 savedVagon.setNomer(vagon.getNomer());
			 savedVagon.setVagonTuri(vagon.getVagonTuri());
			 savedVagon.setDepoNomi(vagon.getDepoNomi());
			 savedVagon.setRemontTuri(vagon.getRemontTuri());
			 savedVagon.setIshlabChiqarilganYili(vagon.getIshlabChiqarilganYili());
			 savedVagon.setIzoh(vagon.getIzoh());
			 savedVagon.setCountry(vagon.getCountry());
			 savedVagon.setChiqganVaqti(vagon.getChiqganVaqti());

			 LocalDateTime today = LocalDateTime.now();
			 LocalDateTime minusHours = today.plusHours(5);
			 DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
			 havDate = minusHours.format(myFormatObj);

			 Optional<LastActionTimes> optionalBiznesTime = utyTimeRepository.findById(1);
			 optionalBiznesTime.get().setHavBiznesDate(havDate);
			 utyTimeRepository.save(optionalBiznesTime.get());

			 return vagonTayyorRepository.save(savedVagon);
		 }else
			 return null;
	}

	@Override
	public VagonTayyor updateVagonAndj(VagonTayyor vagon, long id) {
		if(vagon.getNomer() == null)
			return null;
		 Optional<VagonTayyor> exist = vagonTayyorRepository.findById(id);
		 if( exist.get().getDepoNomi().equalsIgnoreCase("VCHD-5") && vagon.getDepoNomi().equalsIgnoreCase("VCHD-5")){
			 VagonTayyor savedVagon = exist.get();
			 savedVagon.setId(id);
			 savedVagon.setNomer(vagon.getNomer());
			 savedVagon.setVagonTuri(vagon.getVagonTuri());
			 savedVagon.setDepoNomi(vagon.getDepoNomi());
			 savedVagon.setRemontTuri(vagon.getRemontTuri());
			 savedVagon.setIshlabChiqarilganYili(vagon.getIshlabChiqarilganYili());
			 savedVagon.setIzoh(vagon.getIzoh());
			 savedVagon.setCountry(vagon.getCountry());
			 savedVagon.setChiqganVaqti(vagon.getChiqganVaqti());

			 LocalDateTime today = LocalDateTime.now();
			 LocalDateTime minusHours = today.plusHours(5);
			 DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
			 andjDate = minusHours.format(myFormatObj);

			 Optional<LastActionTimes> optionalBiznesTime = utyTimeRepository.findById(1);
			 optionalBiznesTime.get().setAndjBiznesDate(andjDate);
			 utyTimeRepository.save(optionalBiznesTime.get());

			 return vagonTayyorRepository.save(savedVagon);
		}else {
				 return null;
		}
	}

	//hamma oy uchun
	@Override
	public VagonTayyor updateVagonMonths(VagonTayyor vagon, long id) {
		if(vagon.getNomer() == null)
			return null;
		 Optional<VagonTayyor> exist = vagonTayyorRepository.findById(id);
		 if(!exist.isPresent())
			 return null;
		 VagonTayyor savedVagon = exist.get();
		 savedVagon.setId(id);
		 savedVagon.setNomer(vagon.getNomer());
		 savedVagon.setVagonTuri(vagon.getVagonTuri());
		 savedVagon.setDepoNomi(vagon.getDepoNomi());
		 savedVagon.setRemontTuri(vagon.getRemontTuri());
		 savedVagon.setIshlabChiqarilganYili(vagon.getIshlabChiqarilganYili());
		 savedVagon.setIzoh(vagon.getIzoh());
		 savedVagon.setCountry(vagon.getCountry());
		 savedVagon.setChiqganVaqti(vagon.getChiqganVaqti());


		 return vagonTayyorRepository.save(savedVagon);
	}

	@Override
	public VagonTayyor updateVagonSamMonths(VagonTayyor vagon, long id) {
		if(vagon.getNomer() == null)
			return null;
		 Optional<VagonTayyor> exist = vagonTayyorRepository.findById(id);
		 if(exist.get().getDepoNomi().equalsIgnoreCase("VCHD-6") && vagon.getDepoNomi().equalsIgnoreCase("VCHD-6")) {
			 VagonTayyor savedVagon = exist.get();
			 savedVagon.setId(id);
			 savedVagon.setNomer(vagon.getNomer());
			 savedVagon.setVagonTuri(vagon.getVagonTuri());
			 savedVagon.setDepoNomi(vagon.getDepoNomi());
			 savedVagon.setRemontTuri(vagon.getRemontTuri());
			 savedVagon.setIshlabChiqarilganYili(vagon.getIshlabChiqarilganYili());
			 savedVagon.setIzoh(vagon.getIzoh());
			 savedVagon.setCountry(vagon.getCountry());
			 savedVagon.setChiqganVaqti(vagon.getChiqganVaqti());

			 return vagonTayyorRepository.save(savedVagon);
		 }else
			return null;

	}

	@Override
	public VagonTayyor updateVagonHavMonths(VagonTayyor vagon, long id) {
		if(vagon.getNomer() == null)
			return null;
		 Optional<VagonTayyor> exist = vagonTayyorRepository.findById(id);
		 if(exist.get().getDepoNomi().equalsIgnoreCase("VCHD-3") && vagon.getDepoNomi().equalsIgnoreCase("VCHD-3")) {

			 VagonTayyor savedVagon = exist.get();
			 savedVagon.setId(id);
			 savedVagon.setNomer(vagon.getNomer());
			 savedVagon.setVagonTuri(vagon.getVagonTuri());
			 savedVagon.setDepoNomi(vagon.getDepoNomi());
			 savedVagon.setRemontTuri(vagon.getRemontTuri());
			 savedVagon.setIshlabChiqarilganYili(vagon.getIshlabChiqarilganYili());
			 savedVagon.setIzoh(vagon.getIzoh());
			 savedVagon.setCountry(vagon.getCountry());
			 savedVagon.setChiqganVaqti(vagon.getChiqganVaqti());

			 return vagonTayyorRepository.save(savedVagon);
		 }else
			 return null;
	}

	@Override
	public VagonTayyor updateVagonAndjMonths(VagonTayyor vagon, long id) {
		if(vagon.getNomer() == null)
			return null;
		 Optional<VagonTayyor> exist = vagonTayyorRepository.findById(id);
		 if( exist.get().getDepoNomi().equalsIgnoreCase("VCHD-5") && vagon.getDepoNomi().equalsIgnoreCase("VCHD-5")){
			 VagonTayyor savedVagon = exist.get();
			 savedVagon.setId(id);
			 savedVagon.setNomer(vagon.getNomer());
			 savedVagon.setVagonTuri(vagon.getVagonTuri());
			 savedVagon.setDepoNomi(vagon.getDepoNomi());
			 savedVagon.setRemontTuri(vagon.getRemontTuri());
			 savedVagon.setIshlabChiqarilganYili(vagon.getIshlabChiqarilganYili());
			 savedVagon.setIzoh(vagon.getIzoh());
			 savedVagon.setCountry(vagon.getCountry());
			 savedVagon.setChiqganVaqti(vagon.getChiqganVaqti());

			 return vagonTayyorRepository.save(savedVagon);
		}else {
				 return null;
		}
	}


	@Override
	public VagonTayyor getVagonById(long id) {
	Optional<VagonTayyor> exist=	vagonTayyorRepository.findById(id);
	if(!exist.isPresent())
		return null;
	return exist.get();
	}

	@Override
	public VagonTayyor findByNomer(Integer nomer) {
		Optional<VagonTayyor> optional =  vagonTayyorRepository.findByNomer(nomer);
		return optional.orElse(null);
	}

	// filterniki
	@Override
	public List<VagonTayyor> findByDepoNomiVagonTuriAndCountry(String depoNomi, String vagonTuri, String country) {
		return vagonTayyorRepository.findByDepoNomiVagonTuriAndCountry(depoNomi, vagonTuri, country);
	}


	@Override
	public List<VagonTayyor> findByDepoNomiAndVagonTuri(String depoNomi, String vagonTuri) {
		return vagonTayyorRepository.findByDepoNomiAndVagonTuri(depoNomi, vagonTuri);
	}

	@Override
	public List<VagonTayyor> findByDepoNomiAndCountry(String depoNomi, String country) {
		return vagonTayyorRepository.findByDepoNomiAndCountry(depoNomi, country);
	}

	@Override
	public List<VagonTayyor> findByDepoNomi(String depoNomi) {
		return vagonTayyorRepository.findByDepoNomi(depoNomi);
	}

	@Override
	public List<VagonTayyor> findByVagonTuriAndCountry(String vagonTuri, String country) {
		return vagonTayyorRepository.findByVagonTuriAndCountry(vagonTuri, country);
	}

	@Override
	public List<VagonTayyor> findBycountry(String country) {
		return vagonTayyorRepository.findBycountry(country);
	}

	@Override
	public List<VagonTayyor> findByVagonTuri(String vagonTuri) {
		return vagonTayyorRepository.findByVagonTuri(vagonTuri);
	}





	@Override
	public VagonTayyor findById(Long id) {
		return vagonTayyorRepository.findById(id).get();
	}


}
