package com.smr.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smr.bindings.ChildInputs;
import com.smr.bindings.CitizenAppRegistrationInputs;
import com.smr.bindings.DcSummaryReport;
import com.smr.bindings.EducationInputs;
import com.smr.bindings.IncomeInputs;
import com.smr.bindings.PlanSelectionInputs;
import com.smr.entity.CitizenAppRegistrationEntity;
import com.smr.entity.DcCaseEntity;
import com.smr.entity.DcChildrenEntity;
import com.smr.entity.DcEducationEntity;
import com.smr.entity.DcIncomeEntity;
import com.smr.entity.PlanEntity;
import com.smr.repository.IApplicationRegistrationRepository;
import com.smr.repository.IDcCaseRepository;
import com.smr.repository.IDcChildrenRepository;
import com.smr.repository.IDcEducationRepository;
import com.smr.repository.IDcInComeRepository;
import com.smr.repository.IPlanRepository;

@Service
public class DcMgmtServiceImpl implements IDcMgmtService {

	@Autowired
	private IDcCaseRepository caseRepo;
	@Autowired
	private IApplicationRegistrationRepository citizenAppRepo;
	@Autowired
	private IPlanRepository planRepo;
	@Autowired
	private IDcInComeRepository incomeRepo;
	@Autowired
	private IDcEducationRepository educationRepo;
	@Autowired
	private IDcChildrenRepository childrenRepo;
	
	
	
	@Override
	public Integer generateCaseNo(Integer appId) {
		// Load Citizen Data
		Optional<CitizenAppRegistrationEntity> appCitizen = citizenAppRepo.findById(appId);
		if(appCitizen.isPresent()) {
			DcCaseEntity caseEntity = new DcCaseEntity();
			caseEntity.setAppId(appId);
			return caseRepo.save(caseEntity).getCaseNo();	//save operation
		}
		return 0;
	}

	@Override
	public List<String> showAllPlanNames() {
		List<PlanEntity> planList = planRepo.findAll();
		//get only plan names using streaming api
		List<String> planNamesList = planList.stream().map(plan->plan.getPlanName()).toList();
		return planNamesList;
	}

	@Override
	public Integer savePlanSelection(PlanSelectionInputs plan) {
		// Load DcCaseEntity object
		Optional<DcCaseEntity> opt = caseRepo.findById(plan.getCaseNo());
		if(opt.isPresent()) {
			DcCaseEntity caseEntity = opt.get();
			caseEntity.setPlanId(plan.getPlanId());
			//update the DcCaseEntity with plan Id;
			caseRepo.save(caseEntity); //update obj operation
			return caseEntity.getCaseNo();
		}
		return 0;
	}

	@Override
	public Integer saveIncomeDetails(IncomeInputs income) {
		// Convert binding obj data to Entity class obj data
		DcIncomeEntity incomeEntity = new DcIncomeEntity();
		BeanUtils.copyProperties(income, incomeEntity);
		//save the income details
		incomeRepo.save(incomeEntity);
		//return caseNo;
		return income.getCaseNo();
	}

	@Override
	public Integer saveEducationDetails(EducationInputs education) {
		// Convert Binding object to Entity Object
		DcEducationEntity educationEntity = new DcEducationEntity();
		BeanUtils.copyProperties(education, educationEntity);
		//save the object 
		educationRepo.save(educationEntity);
		//return the caseNumber
		return education.getCaseNo();
	}

	@Override
	public Integer saveChildrenDetails(List<ChildInputs> children) {
		// Convert each Binding class obj to each Entity class obj
		children.forEach(child->{
			DcChildrenEntity childEntity = new DcChildrenEntity();
			BeanUtils.copyProperties(child, childEntity);
			//save each entity obj
			childrenRepo.save(childEntity);
		});
		//return CaseNo
		return children.get(0).getCaseNo();
	}

	@Override
	public DcSummaryReport showDCSummary(Integer caseNo) {
		//get multiple entity object based on caseNo
		DcIncomeEntity incomeEntity = incomeRepo.findByCaseNo(caseNo);
		DcEducationEntity educationEntity = educationRepo.findByCaseNo(caseNo);
		List<DcChildrenEntity> childsEntityList = childrenRepo.findByCaseNo(caseNo);
		Optional<DcCaseEntity> optCaseEntity = caseRepo.findById(caseNo);
		//get planName
		String planName = null;
		Integer appId = null;
		if (optCaseEntity.isPresent()) {
			DcCaseEntity caseEntity = optCaseEntity.get();
			Integer planId = caseEntity.getPlanId();
			appId = caseEntity.getAppId();
			Optional<PlanEntity> optPlanEntity = planRepo.findById(planId);
			if(optPlanEntity.isPresent()) {
				optPlanEntity.get().getPlanName();
			}
		}
		
		Optional<CitizenAppRegistrationEntity> optCitizenEntity = citizenAppRepo.findById(appId);
		CitizenAppRegistrationEntity citizenEntity = null;
		if (optCitizenEntity.isPresent())
			citizenEntity = optCitizenEntity.get();
		
		
		//convert Entity Objs to Binding objs
		IncomeInputs income = new IncomeInputs();
		BeanUtils.copyProperties(incomeEntity, income);
		EducationInputs education = new EducationInputs();
		BeanUtils.copyProperties(educationEntity, education);
		List<ChildInputs> listChilds = new ArrayList<>();
		childsEntityList.forEach(childEntity->{
			ChildInputs child = new ChildInputs();
			BeanUtils.copyProperties(childEntity, child);
			listChilds.add(child);
		});
		
		CitizenAppRegistrationInputs citizen = new CitizenAppRegistrationInputs();
		BeanUtils.copyProperties(citizenEntity, citizen);
		
		//prepare DcSummaryReport object
		DcSummaryReport report = new DcSummaryReport();
		report.setPlanName(planName);
		report.setIncomeDetails(income);
		report.setEducationDetails(education);
		report.setCitizenDetails(citizen);
		report.setChildDetails(listChilds);
		
		return report;
	}

}
