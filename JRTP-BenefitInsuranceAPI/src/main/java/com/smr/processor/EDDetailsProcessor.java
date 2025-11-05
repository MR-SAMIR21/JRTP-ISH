package com.smr.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.smr.binding.ElgibilityDetails;
import com.smr.entity.ElgibilityDetailsEntity;

@Component
public class EDDetailsProcessor implements ItemProcessor<ElgibilityDetailsEntity, ElgibilityDetails> {

	@Override
	public ElgibilityDetails process(ElgibilityDetailsEntity item) throws Exception {
		if(item.getPlanStatus().equalsIgnoreCase("Approved...")) {
			ElgibilityDetails details = new ElgibilityDetails();
			BeanUtils.copyProperties(item, details);
			return details;
		}	
		else {
				return null;
		}
	}

}
