package com.sms.serviceImp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.sms.model.LastActionTimes;
import com.sms.payload.ApiResponse;
import com.sms.repository.TimeRepository;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.sms.dto.VagonDto;
import com.sms.model.VagonModel;
import com.sms.repository.VagonRepository;
import com.sms.service.VagonService;
import org.springframework.util.FileCopyUtils;

@Service
public class VagonServiceImp implements VagonService{

	@Autowired
	private VagonRepository vagonRepository;
	@Autowired
	private TimeRepository utyTimeRepository;

	LocalDateTime today = LocalDateTime.now();
	LocalDateTime minusHours = today.plusHours(5);
	DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    String currentDate  = minusHours.format(myFormatObj);
	
	String samDate ;
	String havDate ;
	String andjDate;

//Last Action Timeni oladi
	public String getSamDate() {
		Optional<LastActionTimes> optionalQoldiqTime = utyTimeRepository.findById(1);
		if (!optionalQoldiqTime.isPresent())
			return currentDate;
		return optionalQoldiqTime.get().getSamQoldiqDate();
	}
	public String getHavDate() {
		Optional<LastActionTimes> optionalQoldiqTime = utyTimeRepository.findById(1);
		if (!optionalQoldiqTime.isPresent())
			return currentDate;
		return optionalQoldiqTime.get().getHavQoldiqDate();
	}
	public String getAndjDate() {
		Optional<LastActionTimes> optionalQoldiqTime = utyTimeRepository.findById(1);
		if (!optionalQoldiqTime.isPresent())
			return currentDate;
		return optionalQoldiqTime.get().getAndjQoldiqDate();
	}

	public void createPdf(List<VagonModel> vagons, HttpServletResponse response) throws IOException {

		String home = System.getProperty("user.home");
		File file = new File(home + "/Downloads" + "/Qoldiq.pdf");
		if (!file.getParentFile().exists())
			file.getParentFile().mkdirs();
		if (!file.exists())
			file.createNewFile();
		List<VagonModel> allVagons = vagons;
		try {
			response.setHeader("Content-Disposition",
					"attachment;fileName=\"" + "Qoldiq vagonlar.pdf" +"\"");
			response.setContentType("application/pdf");


			PdfWriter writer = new PdfWriter(file.getAbsolutePath());
			PdfDocument pdfDoc = new PdfDocument(writer);
			Document doc = new Document(pdfDoc);

			String FONT_FILENAME = "./src/main/resources/arial.ttf";
			PdfFont font = PdfFontFactory.createFont(FONT_FILENAME, PdfEncodings.IDENTITY_H);
			doc.setFont(font);

			Paragraph paragraph = new Paragraph("Qoldiq vagonlar");
			paragraph.setBackgroundColor(Color.DARK_GRAY);
			paragraph.setFontColor(Color.WHITE);// Setting background color to cell1
			paragraph.setBorder(Border.NO_BORDER);            // Setting border to cell1
			paragraph.setTextAlignment(TextAlignment.CENTER); // Setting text alignment to cell1
			paragraph.setFontSize(16);

			float[] columnWidth = {30f,200f,200f,200f,200f,200f,200f,200f,200f,200f};
			Table table = new Table(columnWidth);
			table.setTextAlignment(TextAlignment.CENTER);
			table.addCell(new Cell().add("\n № "));
			table.addCell(new Cell().add("\n Nomeri"));
			table.addCell(new Cell().add("\n Vagon turi"));
			table.addCell(new Cell().add("\n VCHD"));
			table.addCell(new Cell().add("\n Ta'mir turi"));
			table.addCell(new Cell().add("Ishlab chiqarilgan yili"));
			table.addCell(new Cell().add("Depoga kelgan vaqti"));
			table.addCell(new Cell().add("\n Saqlangan vaqti"));
			table.addCell(new Cell().add("\n Egasi"));
			table.addCell(new Cell().add("\n Izoh"));
			int i=0;
			for(VagonModel vagon:allVagons) {
				i++;
				table.addCell(new Cell().add(String.valueOf(i)));
				table.addCell(new Cell().add(String.valueOf(vagon.getNomer())));
				table.addCell(new Cell().add(vagon.getVagonTuri()));
				table.addCell(new Cell().add(vagon.getDepoNomi()));
				table.addCell(new Cell().add(vagon.getRemontTuri()));
				table.addCell(new Cell().add(String.valueOf(vagon.getIshlabChiqarilganYili())));
				table.addCell(new Cell().add(String.valueOf(vagon.getKelganVaqti())));
				table.addCell(new Cell().add(String.valueOf(vagon.getCreatedDate())));
				table.addCell(new Cell().add(vagon.getCountry()));
				table.addCell(new Cell().add(vagon.getIzoh()));
			}

			doc.add(paragraph);
			doc.add(table);
			doc.close();
			FileInputStream in = new FileInputStream(file.getAbsoluteFile());
			FileCopyUtils.copy(in, response.getOutputStream());

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void pdfTableFile(List<Integer> vagonsToDownloadTables, HttpServletResponse response) throws IOException {

		String home = System.getProperty("user.home");
		File file = new File(home + "/Downloads" + "/Qoldiq vagonlar (Jadval).pdf");
		if (!file.getParentFile().exists())
			file.getParentFile().mkdirs();
		if (!file.exists())
			file.createNewFile();
		List<Integer> allVagons = vagonsToDownloadTables;
		try {
			response.setHeader("Content-Disposition",
					"attachment;fileName=\"" + "Qoldiq vagonlar (Jadval).pdf" +"\"");
			response.setContentType("application/pdf");


			PdfWriter writer = new PdfWriter(file.getAbsolutePath());
			PdfDocument pdfDoc = new PdfDocument(writer);
			Document doc = new Document(pdfDoc);

			String FONT_FILENAME = "./src/main/resources/arial.ttf";
			PdfFont font = PdfFontFactory.createFont(FONT_FILENAME, PdfEncodings.IDENTITY_H);
			doc.setFont(font);

			Paragraph paragraph = new Paragraph("Qoldiq vagonlar");
			paragraph.setBackgroundColor(Color.DARK_GRAY);
			paragraph.setFontColor(Color.WHITE);// Setting background color to cell1
			paragraph.setBorder(Border.NO_BORDER);            // Setting border to cell1
			paragraph.setTextAlignment(TextAlignment.CENTER); // Setting text alignment to cell1
			paragraph.setFontSize(16);

			float[] columnWidth = {200f,200f,200f,200f,200f};
			Table table = new Table(columnWidth);
			table.setTextAlignment(TextAlignment.CENTER);
			table.addCell(new Cell().add("\n Vagon turi"));
			table.addCell(new Cell().add("\n VCHD-3"));
			table.addCell(new Cell().add("\n VCHD-5"));
			table.addCell(new Cell().add("\n VCHD-6"));
			table.addCell(new Cell().add("\n Jami"));

			List<Integer> x= allVagons;

			table.addCell(new Cell().add("Yopiq vagon (крыт)"));
			table.addCell(new Cell().add(String.valueOf(x.get(0))));
			table.addCell(new Cell().add(String.valueOf(x.get(1))));
			table.addCell(new Cell().add(String.valueOf(x.get(2))));
			table.addCell(new Cell().add(String.valueOf(x.get(3))));

			table.addCell(new Cell().add("Platforma(пф)"));
			table.addCell(new Cell().add(String.valueOf(x.get(4))));
			table.addCell(new Cell().add(String.valueOf(x.get(5))));
			table.addCell(new Cell().add(String.valueOf(x.get(6))));
			table.addCell(new Cell().add(String.valueOf(x.get(7))));

			table.addCell(new Cell().add("Yarim ochiq vagon(пв)"));
			table.addCell(new Cell().add(String.valueOf(x.get(8))));
			table.addCell(new Cell().add(String.valueOf(x.get(9))));
			table.addCell(new Cell().add(String.valueOf(x.get(10))));
			table.addCell(new Cell().add(String.valueOf(x.get(11))));

			table.addCell(new Cell().add("Sisterna(цс)"));
			table.addCell(new Cell().add(String.valueOf(x.get(12))));
			table.addCell(new Cell().add(String.valueOf(x.get(13))));
			table.addCell(new Cell().add(String.valueOf(x.get(14))));
			table.addCell(new Cell().add(String.valueOf(x.get(15))));

			table.addCell(new Cell().add("Boshqa turdagi(проч)"));
			table.addCell(new Cell().add(String.valueOf(x.get(16))));
			table.addCell(new Cell().add(String.valueOf(x.get(17))));
			table.addCell(new Cell().add(String.valueOf(x.get(18))));
			table.addCell(new Cell().add(String.valueOf(x.get(19))));

			table.addCell(new Cell().add("Jami"));
			table.addCell(new Cell().add(String.valueOf(x.get(20))));
			table.addCell(new Cell().add(String.valueOf(x.get(21))));
			table.addCell(new Cell().add(String.valueOf(x.get(22))));
			table.addCell(new Cell().add(String.valueOf(x.get(23))));

			doc.add(paragraph);
			doc.add(table);
			doc.close();
			FileInputStream in = new FileInputStream(file.getAbsoluteFile());
			FileCopyUtils.copy(in, response.getOutputStream());

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}


////Export to Excel
//	//Table
//	@Override
//	public void exportTableToExcel(List<Integer> vagonsToDownloadTables, HttpServletResponse response) throws IOException {
//		response.setContentType("application/octet-stream");
//		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
//		String currentDateTime = dateFormatter.format(new Date());
//
//		String headerKey = "Content-Disposition";
//		String headerValue = "attachment; filename=Qoldiq vagonlar (Jadval) " +
//				"" + currentDateTime + ".xlsx";
//		response.setHeader(headerKey, headerValue);
//
//		ExcelTableGenerator generator = new ExcelTableGenerator(vagonsToDownloadTables);
//		generator.generateExcelFile(response);
//	}
//	public static class ExcelTableGenerator {
//
//		private List <Integer> data;
//		private XSSFWorkbook workbook;
//		private XSSFSheet sheet;
//
//		public ExcelTableGenerator(List <Integer> data) {
//			this.data = data;
//			workbook = new XSSFWorkbook();
//		}
//
//		private void writeHeader() {
//			sheet = workbook.createSheet("(Jadval) Qoldiq vagonlar");
//			Row row = sheet.createRow(0);
//			CellStyle style = workbook.createCellStyle();
//			XSSFFont font = workbook.createFont();
//			font.setBold(true);
//			font.setFontHeight(14);
//			style.setFont(font);
//			createCell(row, 0, "Vagon turlari", style);
//			createCell(row, 1, "VCHD-3", style);
//			createCell(row, 2, "VCHD-5", style);
//			createCell(row, 3, "VCHD-6", style);
//			createCell(row, 4, "Jami", style);
//		}
//		private void createCell(Row row, int columnCount, Object valueOfCell, CellStyle style) {
//			sheet.autoSizeColumn(columnCount);
//			org.apache.poi.ss.usermodel.Cell cell = row.createCell(columnCount);
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
//		private void write() {
//			CellStyle style = workbook.createCellStyle();
//			XSSFFont font = workbook.createFont();
//			font.setFontHeight(12);
//			style.setFont(font);
//
//			Row row = sheet.createRow(1);
//			createCell(row, 0, "Yopiq vagon (крыт)", style);
//			createCell(row, 1, data.get(0), style);
//			createCell(row, 2, data.get(1), style);
//			createCell(row, 3, data.get(2), style);
//			createCell(row, 4, data.get(3), style);
//
//			Row row2 = sheet.createRow(2);
//			createCell(row2, 0, "Platforma(пф)", style);
//			createCell(row2, 1, data.get(4), style);
//			createCell(row2, 2, data.get(5), style);
//			createCell(row2, 3, data.get(6), style);
//			createCell(row2, 4, data.get(7), style);
//
//			Row row3 = sheet.createRow(3);
//			createCell(row3, 0, "Yarim ochiq vagon(пв)", style);
//			createCell(row3, 1, data.get(8), style);
//			createCell(row3, 2, data.get(9), style);
//			createCell(row3, 3, data.get(10), style);
//			createCell(row3, 4, data.get(11), style);
//
//			Row row4 = sheet.createRow(4);
//			createCell(row4, 0, "Sisterna(цс)", style);
//			createCell(row4, 1, data.get(12), style);
//			createCell(row4, 2, data.get(13), style);
//			createCell(row4, 3, data.get(14), style);
//			createCell(row4, 4, data.get(15), style);
//
//			Row row5 = sheet.createRow(5);
//			createCell(row5, 0, "Boshqa turdagi(проч)", style);
//			createCell(row5, 1, data.get(16), style);
//			createCell(row5, 2, data.get(17), style);
//			createCell(row5, 3, data.get(18), style);
//			createCell(row5, 4, data.get(19), style);
//
//			Row row6 = sheet.createRow(7 );
//			createCell(row6, 0, "Jami", style);
//			createCell(row6, 1, data.get(20), style);
//			createCell(row6, 2, data.get(21), style);
//			createCell(row6, 3, data.get(22), style);
//			createCell(row6, 4, data.get(23), style);
//		}
//		public void generateExcelFile(HttpServletResponse response) throws IOException {
//			writeHeader();
//			write();
//			ServletOutputStream outputStream = response.getOutputStream();
//			workbook.write(outputStream);
//			workbook.close();
//			outputStream.close();
//		}
//	}
//
//	//List
//	@Override
//	public void exportToExcel(List<VagonModel> vagonsToDownload, HttpServletResponse response) throws IOException {
//		response.setContentType("application/octet-stream");
//		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
//		String currentDateTime = dateFormatter.format(new Date());
//
//		String headerKey = "Content-Disposition";
//		String headerValue = "attachment; filename=Qoldiq vagonlar " +
//				"" + currentDateTime + ".xlsx";
//		response.setHeader(headerKey, headerValue);
//
//		ExcelGenerator generator = new ExcelGenerator(vagonsToDownload);
//		generator.generateExcelFile(response);
//	}
//	public static class ExcelGenerator {
//
//		private final List < VagonModel > vagonModelList;
//		private final XSSFWorkbook workbook;
//		private XSSFSheet sheet;
//
//		public ExcelGenerator(List < VagonModel > vagonModelList) {
//			this.vagonModelList = vagonModelList;
//			workbook = new XSSFWorkbook();
//		}
//		private void writeHeader() {
//			sheet = workbook.createSheet("Qoldiq vagonlar");
//			Row row = sheet.createRow(0);
//			CellStyle style = workbook.createCellStyle();
//			XSSFFont font = workbook.createFont();
//			font.setBold(true);
//			font.setFontHeight(14);
//			style.setFont(font);
////			createCell(row, 0, "№", style);
//			createCell(row, 0, "Nomeri", style);
//			createCell(row, 1, "Vagon turi", style);
//			createCell(row, 2, "VCHD", style);
//			createCell(row, 3, "Ta'mir turi", style);
//			createCell(row, 4, "Ishlab chiqarilgan yili", style);
//			createCell(row, 5, "Depoga kelgan vaqti", style);
//			createCell(row, 6, "Saqlangan vaqti", style);
//			createCell(row, 7, "Egasi", style);
//			createCell(row, 8, "Izoh", style);
//		}
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
//		private void write() {
//			int rowCount = 1;
//			CellStyle style = workbook.createCellStyle();
//			XSSFFont font = workbook.createFont();
//			font.setFontHeight(12);
//			style.setFont(font);
//			for (VagonModel vagon: vagonModelList) {
//				Row row = sheet.createRow(rowCount++);
//				int columnCount = 0;
//				createCell(row, columnCount++, vagon.getNomer(), style);
//				createCell(row, columnCount++, vagon.getVagonTuri(), style);
//				createCell(row, columnCount++, vagon.getDepoNomi(), style);
//				createCell(row, columnCount++, vagon.getRemontTuri(), style);
//				createCell(row, columnCount++, vagon.getIshlabChiqarilganYili(), style);
//				createCell(row, columnCount++, vagon.getKelganVaqti(), style);
//				createCell(row, columnCount++, vagon.getCreatedDate(), style);
//				createCell(row, columnCount++, vagon.getCountry(), style);
//				createCell(row, columnCount++, vagon.getIzoh(), style);
//
//			}
//		}
//		public void generateExcelFile(HttpServletResponse response) throws IOException {
//			writeHeader();
//			write();
//			ServletOutputStream outputStream = response.getOutputStream();
//			workbook.write(outputStream);
//			workbook.close();
//			outputStream.close();
//		}
//	}

//Listni toldirish uchun
	@Override
	public List<VagonModel> findAll() {
		Optional<LastActionTimes> byId = utyTimeRepository.findById(1);

		if (!byId.isPresent()) {
			LastActionTimes times = new LastActionTimes();

			times.setId(1);
			times.setSamQoldiqDate(currentDate);
			times.setHavQoldiqDate(currentDate);
			times.setAndjQoldiqDate(currentDate);

			times.setSamMalumotDate(currentDate);
			times.setHavMalumotDate(currentDate);
			times.setAndjMalumotDate(currentDate);

			times.setSamUtyDate(currentDate);
			times.setHavUtyDate(currentDate);
			times.setAndjUtyDate(currentDate);

			times.setSamBiznesDate(currentDate);
			times.setHavBiznesDate(currentDate);
			times.setAndjBiznesDate(currentDate);

			utyTimeRepository.save(times);
		}

		return vagonRepository.findAll(Sort.by(Sort.Direction.DESC,"id"));
	}

//Save wagon
	@Override
	public ApiResponse saveVagon(VagonDto vagon) {

		Optional<VagonModel> exist=	vagonRepository.findByNomer(vagon.getNomer());
		if(exist.isPresent())
			return new ApiResponse(exist.get().getNomer() + " nomerli vagon avval saqlangan", false);
		VagonModel savedVagon = new VagonModel();
		savedVagon.setNomer(vagon.getNomer());
		savedVagon.setDepoNomi(vagon.getDepoNomi());
		savedVagon.setRemontTuri(vagon.getRemontTuri());
		savedVagon.setVagonTuri(vagon.getVagonTuri());
		savedVagon.setIshlabChiqarilganYili(vagon.getIshlabChiqarilganYili());
		savedVagon.setKelganVaqti(vagon.getKelganVaqti());
		savedVagon.setIzoh(vagon.getIzoh());
		savedVagon.setCountry(vagon.getCountry());

		LocalDateTime today = LocalDateTime.now();
		LocalDateTime minusHours = today.plusHours(5);
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		String currentDate  = minusHours.format(myFormatObj);

		savedVagon.setCreatedDate(currentDate);

		vagonRepository.save(savedVagon);
		return new ApiResponse(vagon.getNomer() + " nomerli vagon ma'lumotlari saqlandi", true);
	}
	@Override
	public ApiResponse saveVagonSam(VagonDto vagon) {
		Optional<VagonModel> exist=	vagonRepository.findByNomer(vagon.getNomer());
		if(exist.isPresent())
			return new ApiResponse(exist.get().getNomer() + " nomerli vagon avval saqlangan", false);
		if(!vagon.getDepoNomi().equalsIgnoreCase("VCHD-6"))
			return new ApiResponse("Siz faqat VCHD-6 ga ma'lumot qo'shishingiz mumkin", false);
		VagonModel savedVagon = new VagonModel();
		savedVagon.setNomer(vagon.getNomer());
		savedVagon.setDepoNomi(vagon.getDepoNomi());
		savedVagon.setRemontTuri(vagon.getRemontTuri());
		savedVagon.setVagonTuri(vagon.getVagonTuri());
		savedVagon.setIshlabChiqarilganYili(vagon.getIshlabChiqarilganYili());
		savedVagon.setIzoh(vagon.getIzoh());
		savedVagon.setCountry(vagon.getCountry());
		savedVagon.setKelganVaqti(vagon.getKelganVaqti());
		
		LocalDateTime today = LocalDateTime.now();
		LocalDateTime minusHours = today.plusHours(5);
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
	    samDate = minusHours.format(myFormatObj);

		savedVagon.setCreatedDate(samDate);

		Optional<LastActionTimes> optionalQoldiqTime = utyTimeRepository.findById(1);
		optionalQoldiqTime.get().setSamQoldiqDate(samDate);
		utyTimeRepository.save(optionalQoldiqTime.get());

		vagonRepository.save(savedVagon);
		return new ApiResponse(vagon.getNomer() + " nomerli vagon ma'lumotlari saqlandi", true);
	}
	@Override
	public ApiResponse saveVagonHav(VagonDto vagon) {
		Optional<VagonModel> exist=	vagonRepository.findByNomer(vagon.getNomer());
		if(exist.isPresent())
			return new ApiResponse(exist.get().getNomer() + " nomerli vagon avval saqlangan", false);
		if(!vagon.getDepoNomi().equalsIgnoreCase("VCHD-3"))
			return new ApiResponse("Siz faqat VCHD-3 ga ma'lumot qo'shishingiz mumkin", false);
		VagonModel savedVagon = new VagonModel();
		savedVagon.setNomer(vagon.getNomer());
		savedVagon.setDepoNomi(vagon.getDepoNomi());
		savedVagon.setRemontTuri(vagon.getRemontTuri());
		savedVagon.setVagonTuri(vagon.getVagonTuri());
		savedVagon.setIshlabChiqarilganYili(vagon.getIshlabChiqarilganYili());
		savedVagon.setIzoh(vagon.getIzoh());
		savedVagon.setCountry(vagon.getCountry());
		savedVagon.setKelganVaqti(vagon.getKelganVaqti());
		
		LocalDateTime today = LocalDateTime.now();
		LocalDateTime minusHours = today.plusHours(5);
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		havDate = minusHours.format(myFormatObj);

		savedVagon.setCreatedDate(havDate);

		Optional<LastActionTimes> optionalQoldiqTime = utyTimeRepository.findById(1);
		optionalQoldiqTime.get().setHavQoldiqDate(havDate);
		utyTimeRepository.save(optionalQoldiqTime.get());

		vagonRepository.save(savedVagon);
		return new ApiResponse(vagon.getNomer() + " nomerli vagon ma'lumotlari saqlandi", true);
	}

	@Override
	public ApiResponse saveVagonAndj(VagonDto vagon) {
		Optional<VagonModel> exist=	vagonRepository.findByNomer(vagon.getNomer());
		if(exist.isPresent())
			return new ApiResponse(exist.get().getNomer() + " nomerli vagon avval saqlangan", false);
		if(!vagon.getDepoNomi().equalsIgnoreCase("VCHD-5"))
			return new ApiResponse("Siz faqat VCHD-5 ga ma'lumot qo'shishingiz mumkin", false);
		VagonModel savedVagon = new VagonModel();
		savedVagon.setNomer(vagon.getNomer());
		savedVagon.setDepoNomi(vagon.getDepoNomi());
		savedVagon.setRemontTuri(vagon.getRemontTuri());
		savedVagon.setVagonTuri(vagon.getVagonTuri());
		savedVagon.setIshlabChiqarilganYili(vagon.getIshlabChiqarilganYili());
		savedVagon.setIzoh(vagon.getIzoh());
		savedVagon.setCountry(vagon.getCountry());
		savedVagon.setCreatedDate(minusHours.format(myFormatObj));
		savedVagon.setKelganVaqti(vagon.getKelganVaqti());
		
		LocalDateTime today = LocalDateTime.now();
		LocalDateTime minusHours = today.plusHours(5);
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		andjDate = minusHours.format(myFormatObj);

		savedVagon.setCreatedDate(andjDate);

		Optional<LastActionTimes> optionalQoldiqTime = utyTimeRepository.findById(1);
		optionalQoldiqTime.get().setAndjQoldiqDate(andjDate);
		utyTimeRepository.save(optionalQoldiqTime.get());

		vagonRepository.save(savedVagon);
		return new ApiResponse(vagon.getNomer() + " nomerli vagon ma'lumotlari saqlandi", true);
	}

//Get by id
	@Override
	public VagonModel findById(Long id) {
		Optional<VagonModel> optional = vagonRepository.findById(id);
		if (optional.isPresent())
			return optional.get();
		return null;
	}

//Delete
	@Override
	public ApiResponse deleteVagonById(long id) throws NotFoundException {
		Optional<VagonModel> optional = vagonRepository.findById(id);
		if (optional.isPresent()) {
			VagonModel vagonModel = optional.get();
			vagonRepository.deleteById(id);
			return new ApiResponse(vagonModel.getNomer() + " nomerli vagon o'chirildi", true);
		}else
			return new ApiResponse( " Ma'lumot topilmadi", false);
	}
	@Override
	public ApiResponse deleteVagonSam(long id) throws NotFoundException {
		vagonRepository.deleteById(id);
		LocalDateTime today = LocalDateTime.now();
		LocalDateTime minusHours = today.plusHours(5);
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		samDate = minusHours.format(myFormatObj);

		Optional<LastActionTimes> optionalQoldiqTime = utyTimeRepository.findById(1);
		optionalQoldiqTime.get().setSamQoldiqDate(samDate);
		utyTimeRepository.save(optionalQoldiqTime.get());
		return new ApiResponse(vagonRepository.findById(id).get().getNomer() + " nomerli vagon o'chirildi", true);
	}
	@Override
	public ApiResponse deleteVagonHav(long id) throws NotFoundException{
		vagonRepository.deleteById(id);
		LocalDateTime today = LocalDateTime.now();
		LocalDateTime minusHours = today.plusHours(5);
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		havDate = minusHours.format(myFormatObj);

		Optional<LastActionTimes> optionalQoldiqTime = utyTimeRepository.findById(1);
		optionalQoldiqTime.get().setHavQoldiqDate(havDate);
		utyTimeRepository.save(optionalQoldiqTime.get());
		return new ApiResponse(vagonRepository.findById(id).get().getNomer() + " nomerli vagon o'chirildi", true);
	}
	@Override
	public ApiResponse deleteVagonAndj(long id) throws NotFoundException{
		vagonRepository.deleteById(id);
		LocalDateTime today = LocalDateTime.now();
		LocalDateTime minusHours = today.plusHours(5);
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		andjDate = minusHours.format(myFormatObj);

		Optional<LastActionTimes> optionalQoldiqTime = utyTimeRepository.findById(1);
		optionalQoldiqTime.get().setAndjQoldiqDate(andjDate);
		utyTimeRepository.save(optionalQoldiqTime.get());
		return new ApiResponse(vagonRepository.findById(id).get().getNomer() + " nomerli vagon o'chirildi", true);
	}

//Search
	@Override
	public ApiResponse findByKeyword(Integer participant) {

		Optional<VagonModel> exist=vagonRepository.findByNomer(participant );
		if(exist.isPresent()) {
			VagonModel vagonModel = exist.get();
			return new ApiResponse(vagonModel, true);
		}
		return new ApiResponse(participant + " nomerli vagon topilmadi", false);
	}


//Filter
	@Override
	public Integer getCount(String string) {
		return vagonRepository.getCount(string);
	}

	@Override
	public Integer getVagonsCount(String kriti, String depoNomi) {
		return vagonRepository.getVagonsCount(kriti, depoNomi);
	}

	@Override
	public Integer getCount(String string, String country) {
		return vagonRepository.getCount(string, country);
	}

	@Override
	public Integer getVagonsCount(String kriti, String depoNomi, String country) {
		return vagonRepository.getVagonsCount(kriti, depoNomi, country);
	}

	@Override
	public List<VagonModel> findAllByDepoNomiVagonTuriAndCountry(String depoNomi, String vagonTuri, String country) {
		return vagonRepository.findAllByDepoNomiVagonTuriAndCountry(depoNomi, vagonTuri, country, Sort.by(Sort.Direction.DESC, "createdDate"));
	}

	@Override
	public List<VagonModel> findAllByDepoNomiAndVagonTuri(String depoNomi, String vagonTuri) {
		return vagonRepository.findAllByDepoNomiAndVagonTuri(depoNomi, vagonTuri, Sort.by(Sort.Direction.DESC, "createdDate"));
	}

	@Override
	public List<VagonModel> findAllByDepoNomiAndCountry(String depoNomi, String country) {
		return vagonRepository.findAllByDepoNomiAndCountry(depoNomi, country, Sort.by(Sort.Direction.DESC, "createdDate"));
	}

	@Override
	public List<VagonModel> findAllByDepoNomi(String depoNomi) {
		return vagonRepository.findAllByDepoNomi(depoNomi, Sort.by(Sort.Direction.DESC, "createdDate"));
	}

	@Override
	public List<VagonModel> findAllByVagonTuriAndCountry(String vagonTuri, String country) {
		return vagonRepository.findAllByVagonTuriAndCountry(vagonTuri, country, Sort.by(Sort.Direction.DESC, "createdDate"));
	}

	@Override
	public List<VagonModel> findAllBycountry(String country) {
		return vagonRepository.findAllBycountry(country, Sort.by(Sort.Direction.DESC, "createdDate"));
	}

	@Override
	public List<VagonModel> findAllByVagonTuri(String vagonTuri) {
		return vagonRepository.findAllByVagonTuri(vagonTuri, Sort.by(Sort.Direction.DESC, "createdDate"));
	}










//Update
	@Override
	public VagonModel getVagonById(long id) {
		Optional<VagonModel> exist=	vagonRepository.findById(id);
		return exist.orElseGet(VagonModel::new);
	}

	@Override
	public ApiResponse updateVagon(VagonDto vagon, long id) {	
		 Optional<VagonModel> exist = vagonRepository.findById(id);
		 if(!exist.isPresent())
			 return new ApiResponse("Vagon topilmadi", false);
		 VagonModel savedVagon = exist.get();
		 savedVagon.setId(id);
		 savedVagon.setNomer(vagon.getNomer());
		 savedVagon.setVagonTuri(vagon.getVagonTuri());
		 savedVagon.setDepoNomi(vagon.getDepoNomi());
		 savedVagon.setRemontTuri(vagon.getRemontTuri());
		 savedVagon.setIshlabChiqarilganYili(vagon.getIshlabChiqarilganYili());
		 savedVagon.setKelganVaqti(vagon.getKelganVaqti());
		 savedVagon.setIzoh(vagon.getIzoh());
		 savedVagon.setCountry(vagon.getCountry());


		vagonRepository.save(savedVagon);
		return new ApiResponse(vagon.getNomer() + " nomerli vagon ma'lumotlari o'zgartirildi", true);
	}
	
	@Override
	public ApiResponse updateVagonSam(VagonDto vagon, long id) {

		 Optional<VagonModel> exist = vagonRepository.findById(id);
		 if(vagon.getDepoNomi().equalsIgnoreCase("VCHD-6")) {
			 VagonModel savedVagon = exist.get();
			 savedVagon.setId(id);
			 savedVagon.setNomer(vagon.getNomer());
			 savedVagon.setVagonTuri(vagon.getVagonTuri());
			 savedVagon.setDepoNomi(vagon.getDepoNomi());
			 savedVagon.setRemontTuri(vagon.getRemontTuri());
			 savedVagon.setIshlabChiqarilganYili(vagon.getIshlabChiqarilganYili());
			 savedVagon.setIzoh(vagon.getIzoh());
			 savedVagon.setCountry(vagon.getCountry());
			 savedVagon.setKelganVaqti(vagon.getKelganVaqti());
			 
			 LocalDateTime today = LocalDateTime.now();
			 LocalDateTime minusHours = today.plusHours(5);
			 DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
			 samDate = minusHours.format(myFormatObj);

			 Optional<LastActionTimes> optionalQoldiqTime = utyTimeRepository.findById(1);
			 optionalQoldiqTime.get().setSamQoldiqDate(samDate);
			 utyTimeRepository.save(optionalQoldiqTime.get());

			 vagonRepository.save(savedVagon);
			 return new ApiResponse(vagon.getNomer() + " nomerli vagon ma'lumotlari o'zgartirildi", true);
		 }else
			 return new ApiResponse("Siz faqat VCHD-6 ga ma'lumot kiritishingiz mumkin", false);

	}

	@Override
	public ApiResponse updateVagonHav(VagonDto vagon, long id) {
		 Optional<VagonModel> exist = vagonRepository.findById(id);
		 if(vagon.getDepoNomi().equalsIgnoreCase("VCHD-3")) {
			 
			 VagonModel savedVagon = exist.get();
			 savedVagon.setId(id);
			 savedVagon.setNomer(vagon.getNomer());
			 savedVagon.setVagonTuri(vagon.getVagonTuri());
			 savedVagon.setDepoNomi(vagon.getDepoNomi());
			 savedVagon.setRemontTuri(vagon.getRemontTuri());
			 savedVagon.setIshlabChiqarilganYili(vagon.getIshlabChiqarilganYili());
			 savedVagon.setIzoh(vagon.getIzoh());
			 savedVagon.setCountry(vagon.getCountry());
			 savedVagon.setKelganVaqti(vagon.getKelganVaqti());
			 
			 LocalDateTime today = LocalDateTime.now();
			 LocalDateTime minusHours = today.plusHours(5);
			 DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
			 havDate = minusHours.format(myFormatObj);

			 Optional<LastActionTimes> optionalQoldiqTime = utyTimeRepository.findById(1);
			 optionalQoldiqTime.get().setHavQoldiqDate(havDate);
			 utyTimeRepository.save(optionalQoldiqTime.get());

			 vagonRepository.save(savedVagon);
			 return new ApiResponse(vagon.getNomer() + " nomerli vagon ma'lumotlari o'zgartirildi", true);
		 }else
			 return new ApiResponse("Siz faqat VCHD-3 ga ma'lumot kiritishingiz mumkin", false);
	}

	@Override
	public ApiResponse updateVagonAndj(VagonDto vagon, long id) {

		 Optional<VagonModel> exist = vagonRepository.findById(id);
		 if(vagon.getDepoNomi().equalsIgnoreCase("VCHD-5")){
			 VagonModel savedVagon = exist.get();
			 savedVagon.setId(id);
			 savedVagon.setNomer(vagon.getNomer());
			 savedVagon.setVagonTuri(vagon.getVagonTuri());
			 savedVagon.setDepoNomi(vagon.getDepoNomi());
			 savedVagon.setRemontTuri(vagon.getRemontTuri());
			 savedVagon.setIshlabChiqarilganYili(vagon.getIshlabChiqarilganYili());
			 savedVagon.setIzoh(vagon.getIzoh());
			 savedVagon.setCountry(vagon.getCountry());
			 savedVagon.setKelganVaqti(vagon.getKelganVaqti());
			 
			 LocalDateTime today = LocalDateTime.now();
			 LocalDateTime minusHours = today.plusHours(5);
			 DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
			 andjDate = minusHours.format(myFormatObj);

			 Optional<LastActionTimes> optionalQoldiqTime = utyTimeRepository.findById(1);
			 optionalQoldiqTime.get().setAndjQoldiqDate(andjDate);
			 utyTimeRepository.save(optionalQoldiqTime.get());

			 vagonRepository.save(savedVagon);
			 return new ApiResponse(vagon.getNomer() + " nomerli vagon ma'lumotlari o'zgartirildi", true);
		 }else
			 return new ApiResponse("Siz faqat VCHD-5 ga ma'lumot kiritishingiz mumkin", false);
	}






}
