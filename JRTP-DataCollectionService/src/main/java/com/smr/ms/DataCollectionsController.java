package com.smr.ms;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smr.bindings.ChildInputs;
import com.smr.bindings.DcSummaryReport;
import com.smr.bindings.EducationInputs;
import com.smr.bindings.IncomeInputs;
import com.smr.bindings.PlanSelectionInputs;
import com.smr.service.IDcMgmtService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/dc-api")
@Tag(name="dc-api", description="Data Collection module microservice")
public class DataCollectionsController {

	@Autowired
	private IDcMgmtService dcService;
	
	
	@Tag(name="PlanNames-api")
	@GetMapping("/planNames")
	public ResponseEntity<List<String>> displayPlanName(){
		// use service
		List<String> listPlanNames = dcService.showAllPlanNames();
		return new ResponseEntity<List<String>>(listPlanNames, HttpStatus.OK);
	}
	
	
	@Tag(name="GenerateCaseNo-api")
	@PostMapping("/generateCaseNo/{appId}")
	public ResponseEntity<Integer> generateCaseNo(@PathVariable Integer appId){
		//use service
		 Integer caseNo = dcService.generateCaseNo(appId);
		 return new ResponseEntity<Integer>(caseNo,HttpStatus.OK);
	}
	
	
	@Tag(name="UpdatePlanSelection-api")
	@PutMapping("/updatePlanSelection")
	public ResponseEntity<Integer> savePlanSelection(@RequestBody PlanSelectionInputs inputs){
		//use Service
		Integer caseNo = dcService.savePlanSelection(inputs);
		return new ResponseEntity<Integer>(caseNo,HttpStatus.CREATED); 
	}
	
	
	@Tag(name="SaveIncome-api")
	@PostMapping("/saveIncome")
	public ResponseEntity<Integer> saveIncomeDetails(@RequestBody IncomeInputs income){
		//use service
		Integer caseNo = dcService.saveIncomeDetails(income);
		return new ResponseEntity<Integer>(caseNo,HttpStatus.CREATED);
	}
	
	
	@Tag(name="SaveEducation-api")
	@PostMapping("/saveEducation")
	public ResponseEntity<Integer> saveEducationDetails(@RequestBody EducationInputs education){
		//use service
		Integer caseNo = dcService.saveEducationDetails(education);
		return new ResponseEntity<Integer>(caseNo,HttpStatus.CREATED);
	}
	
	@Tag(name="SaveChild-api")
	@PostMapping("/saveChilds")
	public ResponseEntity<Integer> saveChildrenDetails(@RequestBody List<ChildInputs> childs){
		//use service
		Integer caseNo = dcService.saveChildrenDetails(childs);
		return new ResponseEntity<Integer>(caseNo,HttpStatus.CREATED);
	}
	
	
	@Tag(name="Summary-api", description="Summary in data collection")
	@GetMapping("/summary/{caseNo}")
	public ResponseEntity<DcSummaryReport> showSummaryReport(@PathVariable Integer caseNo){
		//use Service
		DcSummaryReport report = dcService.showDCSummary(caseNo);
		return new ResponseEntity<DcSummaryReport>(report,HttpStatus.OK);
	}
	
	
	
}
