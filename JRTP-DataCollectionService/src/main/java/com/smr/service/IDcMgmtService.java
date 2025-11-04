package com.smr.service;

import java.util.List;

import com.smr.bindings.ChildInputs;
import com.smr.bindings.DcSummaryReport;
import com.smr.bindings.EducationInputs;
import com.smr.bindings.IncomeInputs;
import com.smr.bindings.PlanSelectionInputs;

public interface IDcMgmtService {

	public Integer generateCaseNo(Integer appId);
	public List<String> showAllPlanNames();
	public Integer savePlanSelection(PlanSelectionInputs plan);
	public Integer saveIncomeDetails(IncomeInputs income);
	public Integer saveEducationDetails(EducationInputs education);
	public Integer saveChildrenDetails(List<ChildInputs> children);
	public DcSummaryReport showDCSummary(Integer caseNo);  
}
