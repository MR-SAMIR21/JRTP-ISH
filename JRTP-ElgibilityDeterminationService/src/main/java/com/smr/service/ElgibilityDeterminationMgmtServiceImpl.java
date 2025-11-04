package com.smr.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smr.bindings.ElgibilityDetailsOutput;
import com.smr.entity.CitizenAppRegistrationEntity;
import com.smr.entity.CoTriggersEntity;
import com.smr.entity.DcCaseEntity;
import com.smr.entity.DcChildrenEntity;
import com.smr.entity.DcEducationEntity;
import com.smr.entity.DcIncomeEntity;
import com.smr.entity.ElgibilityDetailsEntity;
import com.smr.entity.PlanEntity;
import com.smr.repository.IApplicationRegistrationRepository;
import com.smr.repository.ICoTriggerRepository;
import com.smr.repository.IDcCaseRepository;
import com.smr.repository.IDcChildrenRepository;
import com.smr.repository.IDcEducationRepository;
import com.smr.repository.IDcInComeRepository;
import com.smr.repository.IElgibilityDeterminationRepository;
import com.smr.repository.IPlanRepository;

@Service
public class ElgibilityDeterminationMgmtServiceImpl implements IElgibilityDeterminationMgmtService {

	@Autowired
	private IDcCaseRepository caseRepo;
	@Autowired
	private IPlanRepository planRepo;
	@Autowired
	private IDcInComeRepository incomeRepo;
	@Autowired
	private IDcChildrenRepository childrenRepo;
	@Autowired
	private IApplicationRegistrationRepository citizenRepo;
	@Autowired
	private IDcEducationRepository educationRepo;
	@Autowired
	private ICoTriggerRepository triggerRepo;
	
	@Autowired
	private IElgibilityDeterminationRepository elgiRepo;
	
	
	@Override
    public ElgibilityDetailsOutput determineElgibility(Integer caseNo) {

    	Integer appId=null;
    	Integer planId=null;
//get planId and appId based on caseNo
    	Optional<DcCaseEntity> optCaseEntity = caseRepo.findById(caseNo);
    	if(optCaseEntity.isPresent()) {
    		DcCaseEntity caseEntity = optCaseEntity.get();
    		planId = caseEntity.getPlanId();
    		appId = caseEntity.getAppId();
    	}

//getPlan Name
    	String planName=null;
    	Optional<PlanEntity> optPlanEntity = planRepo.findById(planId);
    	if(optPlanEntity.isPresent()) {
    		PlanEntity planEntity = optPlanEntity.get();
    		planName = planEntity.getPlanName();
    	}
    	
// calculate citizen age by getting citizen DOB through appId
    	Optional<CitizenAppRegistrationEntity> optCitizenEntity = citizenRepo.findById(appId);
    	
    	int citizenAge = 0;
    	String citizenName = null;
    	long citigenSSN = 0;
    	if(optCitizenEntity.isPresent()) {
    		CitizenAppRegistrationEntity citizenEntity = optCitizenEntity.get();
    		LocalDate citizenDOB = citizenEntity.getDob();
    		citizenName = citizenEntity.getFullName();
    		LocalDate SysDate = LocalDate.now();
    		citizenAge = Period.between(citizenDOB, SysDate).getYears();
    		citigenSSN = citizenEntity.getSsn();
    	}
    			
    	
    	// call helper method to plan conditions
    	ElgibilityDetailsOutput elgiOutput = applyPlanConditions(caseNo, planName,citizenAge);
    	
    	
    	//set Citizen Name
    	elgiOutput.setHoldername(citizenName);
    	
    	
    	//save Eligibility entity object
    	ElgibilityDetailsEntity elgiEntity = new ElgibilityDetailsEntity();
    	BeanUtils.copyProperties(elgiOutput, elgiEntity);
    	elgiEntity.setCaseNo(caseNo);
    	elgiEntity.setHolderSSN(citigenSSN);
    	elgiRepo.save(elgiEntity);
    	
    	
    	//save CoTriggers object
    	CoTriggersEntity triggerEntity = new CoTriggersEntity();
    	triggerEntity.setCaseNo(caseNo);
    	triggerEntity.setTriggerStatus("Pending...");
    	triggerRepo.save(triggerEntity);
    	
    	
        return elgiOutput;
    }

	
	//helper method
	private ElgibilityDetailsOutput applyPlanConditions(Integer caseNo,String planName, int citizenAge) {
    	
		ElgibilityDetailsOutput elgiOutput=new  ElgibilityDetailsOutput();
		elgiOutput.setPlanName(planName);
		
		//get income details of the citizen
		DcIncomeEntity incomeEntity = incomeRepo.findByCaseNo(caseNo);
		Double empIncome = incomeEntity.getEmpIncome();
		Double propertyIncome = incomeEntity.getPropertyIncome();
		
		
// for SNAP
		if(planName.equalsIgnoreCase("SNAP")) {
			if(empIncome<=300) {
				elgiOutput.setPlanStatus("Approved");
				elgiOutput.setBenifitAmt(200.0);
			}else {
				elgiOutput.setPlanStatus("Denied");
				elgiOutput.setDenialReason("High Income");
			}
		}
		
// for CCAP		
		else if(planName.equalsIgnoreCase("CCAP")) {
			
			boolean kidsCountCondition = false;
			boolean kidAgeCondition = true;
			
			List<DcChildrenEntity> listChilds = childrenRepo.findByCaseNo(caseNo);
			if(listChilds.isEmpty()){
				kidsCountCondition=true;
				
				for(DcChildrenEntity child:listChilds) {
					int kidAge=Period.between(child.getChildDOB(),LocalDate.now()).getYears();
					if(kidAge>16) {
						kidAgeCondition=false;
						break;
					}//if
				}////for
			}//if
			if(empIncome<=300 && kidsCountCondition && kidAgeCondition) {
				elgiOutput.setPlanStatus("Approved");
				elgiOutput.setBenifitAmt(300.0);
			}
			else {
				elgiOutput.setPlanStatus("Denied");
				elgiOutput.setDenialReason("CCAP rules are not satsfied");
			}
					
		}
		
//for MEDCARE
		else if(planName.equalsIgnoreCase("MEDCARE")) {
			if(citizenAge>=65) {
				elgiOutput.setPlanStatus("Approved");
				elgiOutput.setBenifitAmt(300.0);
			}
			else {
				elgiOutput.setPlanStatus("Denied");
				elgiOutput.setDenialReason("MEDCARE rules are not satsfied");
			}
		}
		
//for MEDAID
		else if(planName.equalsIgnoreCase("MEDAID")) {
			if(empIncome<=300 && propertyIncome==0) {
				elgiOutput.setPlanStatus("Approved");
				elgiOutput.setBenifitAmt(200.0);
			}
			else {
				elgiOutput.setPlanStatus("Denied");
				elgiOutput.setDenialReason("CCAP rules are not satsfied");
			}
		}
		
//for CAJW		
		else if(planName.equalsIgnoreCase("CAJW")) {
			DcEducationEntity educationEntity = educationRepo.findByCaseNo(caseNo);
			int passOutYear = educationEntity.getPassOutYear();
			if(empIncome==0 && passOutYear<LocalDate.now().getYear()) {
				elgiOutput.setPlanStatus("Approved");
				elgiOutput.setBenifitAmt(300.0);
			}
			else {
				elgiOutput.setPlanStatus("Denied");
				elgiOutput.setDenialReason("CAJW rules are not satsfied");
			}
		}
		
		
//for QHP
		else if(planName.equalsIgnoreCase("QHP"))
			if(citizenAge>=1) {
				elgiOutput.setPlanStatus("Approved");
			}
			else {
				elgiOutput.setPlanStatus("Denied");
				elgiOutput.setDenialReason("QHP rules are not satsfied");
			}
			
			
			
		
		//set the common properties for elgiOutput object only if the plan is approved
		if(elgiOutput.getPlanStatus().equalsIgnoreCase("Approved")) {
			elgiOutput.setPlanStartDate(LocalDate.now());
			elgiOutput.setPlanEndDate(LocalDate.now().plusYears(2));
		}
		return elgiOutput;
    }
}
