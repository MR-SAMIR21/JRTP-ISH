package com.smr.service;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.openpdf.text.Document;
import org.openpdf.text.Font;
import org.openpdf.text.FontFactory;
import org.openpdf.text.PageSize;
import org.openpdf.text.Paragraph;
import org.openpdf.text.Phrase;
import org.openpdf.text.pdf.PdfPCell;
import org.openpdf.text.pdf.PdfPTable;
import org.openpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smr.binding.COSummary;
import com.smr.entity.CitizenAppRegistrationEntity;
import com.smr.entity.CoTriggersEntity;
import com.smr.entity.DcCaseEntity;
import com.smr.entity.ElgibilityDetailsEntity;
import com.smr.repository.IApplicationRegistrationRepository;
import com.smr.repository.ICoTriggerRepository;
import com.smr.repository.IDcCaseRepository;
import com.smr.repository.IElgibilityDeterminationRepository;
import com.smr.utils.EmailUtils;

@Service
public class CorrespondanceMgmtServiceImpl implements ICorrespondanceMgmtService {

	@Autowired
	private ICoTriggerRepository triggerRepo;
	@Autowired
	private IElgibilityDeterminationRepository elgiRepo;
	@Autowired
	private IDcCaseRepository caseRepo;
	@Autowired
	private IApplicationRegistrationRepository citizenRepo;
	@Autowired
	private EmailUtils mailUtil;
	
	
	int pendingTriggers = 0;
	int succesTrigger = 0;

	@Override
	public COSummary processPendingTriggers() {
		CitizenAppRegistrationEntity citizenEntity = null;
		ElgibilityDetailsEntity eligiEntity = null;
//		int pendingTriggers = 0;
//		int succesTrigger = 0;

		// get all pending triggers
		List<CoTriggersEntity> triggersList = triggerRepo.findByTriggerStatus("Pending...");

		// prepare COSummary Report
		COSummary summary = new COSummary();
		summary.setTotalTriggers(triggersList.size());
		
		
		// Process the triggers in multithreded env... using Executor Framework
		ExecutorService executorService = Executors.newFixedThreadPool(10);
		ExecutorCompletionService<Object> pool = new ExecutorCompletionService<Object>(executorService);
		
		

		// process each pending trigger
		for (CoTriggersEntity triggerEntity : triggersList) {
			
			pool.submit(()-> {
				
				
					try {
						processTriggeer(summary, triggerEntity);
						succesTrigger++;
					}
					catch (Exception e) {
						e.printStackTrace();
						pendingTriggers++;
					}
					
					// TODO Auto-generated method stub
					return null;
				
			});
			
			
		} // for

		// prepare COSummary Report
		
		summary.setPendingTriggers(pendingTriggers);
		summary.setSuccessTriggers(succesTrigger);

		return summary;

	}

	//helper Method
	private CitizenAppRegistrationEntity processTriggeer(COSummary summary, CoTriggersEntity triggerEntity) throws Exception {
		CitizenAppRegistrationEntity citizenEntity = null;
		// get Eligibility details based on caseNo
		ElgibilityDetailsEntity eligiEntity = elgiRepo.findByCaseNo(triggerEntity.getCaseNo());

		// get appId based on CaseNo
		Optional<DcCaseEntity> optCaseEntity = caseRepo.findById(triggerEntity.getCaseNo());
		if (optCaseEntity.isPresent()) {
			DcCaseEntity caseEntity = optCaseEntity.get();
			Integer appId = caseEntity.getAppId();
			// get the Citizen details based on the appId
			Optional<CitizenAppRegistrationEntity> optCitizenEntity = citizenRepo.findById(appId);
			if (optCitizenEntity.isPresent()) {
				citizenEntity = optCitizenEntity.get();
			}

		}

		// generate pdf doc having Eligibility details and send that pdf doc as email

		generatePdfAndSendMail(eligiEntity, citizenEntity);
		return citizenEntity;

	}

	// healper method to generate the pdf doc
	private void generatePdfAndSendMail(ElgibilityDetailsEntity eligiEntity, CitizenAppRegistrationEntity citizenEntity)
			throws Exception {

		// create Document obj (openPdf)
		Document document = new Document(PageSize.A4);

		// create pdf file to write content to it
		File file = new File(eligiEntity.getCaseNo() + ".pdf");
		FileOutputStream fos = new FileOutputStream(file);

		// get PdfWriter to write to the document and response obj
		PdfWriter.getInstance(document, fos);

		// open the document
		document.open();

		// Define Font for the Paragraph
		org.openpdf.text.Font font = FontFactory.getFont(FontFactory.TIMES_BOLD);
		font.setSize(30);
		font.setColor(Color.CYAN);

		// create the paragraph having content and above font style
		Paragraph para = new Paragraph("Plan Approval/Denial Communication", font);
		para.setAlignment(Paragraph.ALIGN_CENTER);

		// add paragraph to document
		document.add(para);

		// Display search results as the pdf table
		PdfPTable table = new PdfPTable(10);
		table.setWidthPercentage(70);
		table.setWidths(new float[] { 3.0f, 3.0f, 3.0f, 3.0f, 3.0f, 3.0f, 3.0f, 3.0f, 3.0f, 3.0f });
		table.setSpacingBefore(2.0f);

		// prepare heading row cells in the pdf table
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(Color.gray);
		cell.setPadding(5);
		Font cellFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		cellFont.setColor(Color.BLACK);

		cell.setPhrase(new Phrase("TraceID", cellFont));
		table.addCell(cell);
		cell.setPhrase(new Phrase("CaseNo", cellFont));
		table.addCell(cell);
		cell.setPhrase(new Phrase("HolderName", cellFont));
		table.addCell(cell);
		cell.setPhrase(new Phrase("HolderSSN", cellFont));
		table.addCell(cell);
		cell.setPhrase(new Phrase("PlanName", cellFont));
		table.addCell(cell);
		cell.setPhrase(new Phrase("PlanStatus", cellFont));
		table.addCell(cell);
		cell.setPhrase(new Phrase("PlanStartDate", cellFont));
		table.addCell(cell);
		cell.setPhrase(new Phrase("PlanEndDate", cellFont));
		table.addCell(cell);
		cell.setPhrase(new Phrase("BenifitAmt", cellFont));
		table.addCell(cell);
		cell.setPhrase(new Phrase("DenialReason ", cellFont));
		table.addCell(cell);

		// add data cell to pdfTable
		table.addCell(String.valueOf(eligiEntity.getEdTraceId()));
		table.addCell(String.valueOf(eligiEntity.getCaseNo()));
		table.addCell(eligiEntity.getHoldername());
		table.addCell(String.valueOf(eligiEntity.getHolderSSN()));
		table.addCell(eligiEntity.getPlanName());
		table.addCell(eligiEntity.getPlanStatus());
		table.addCell(String.valueOf(eligiEntity.getPlanStartDate()));
		table.addCell(String.valueOf(eligiEntity.getPlanEndDate()));
		table.addCell(String.valueOf(eligiEntity.getBenifitAmt()));
		table.addCell(eligiEntity.getDenialReason());
		// add table to document
		document.add(table);
		// close the document
		document.close();

		// send the generated pdf doc as the email message
		String subject = "Plan approval/Denia eail";
		String body = "Hello Mr/Miss/Mrs " + citizenEntity.getFullName()
				+ " ,This mail contains complet details plan approval or denial ";
		mailUtil.sendMail(citizenEntity.getEmail(), subject, body, file);

		// update Co_Trigger table
		updateCoTrigger(eligiEntity.getCaseNo(), file);

	}

	private void updateCoTrigger(Integer caseNo, File file) throws Exception {
		// check Trigger availability based on the caseNo
		CoTriggersEntity triggerEntity = triggerRepo.findByCaseNo(caseNo);
		// get byte[] represnting pdf doc content
		byte[] pdfContent = new byte[(int) file.length()];
		FileInputStream fis = new FileInputStream(file);
		fis.read(pdfContent);
		if (triggerEntity != null) {
			triggerEntity.setCoNoticePdf(pdfContent);
			triggerEntity.setTriggerStatus("Completed...");
			triggerRepo.save(triggerEntity);
		}
		fis.close();
	}

}
