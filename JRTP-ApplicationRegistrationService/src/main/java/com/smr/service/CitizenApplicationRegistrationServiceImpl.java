package com.smr.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;

import com.smr.bindings.CitizenAppRegistrationInputs;
import com.smr.entity.CitizenAppRegistrationEntity;
import com.smr.exceptions.InvalidSSNException;
import com.smr.repository.IApplicationRegistrationRepository;

import reactor.core.publisher.Mono;

@Service
public class CitizenApplicationRegistrationServiceImpl implements ICitizenApplicationRegistrationService {

	@Autowired
	private IApplicationRegistrationRepository citizenRepo;
	/*
	 * @Autowired private RestTemplate template;
	 */
	@Autowired
	private WebClient client;
	@Value("${ar.ssa-web.url}")
	private String endpointUrl;
	@Value("${ar.state}")
	private String targetState;

	@Override
	public Integer registerCitizenApplication(CitizenAppRegistrationInputs inputs) throws InvalidSSNException {

		/*
		 * //perform WebService call to check whether SSN is valid or not and to get the
		 * state name // ResponseEntity<String> response =
		 * template.exchange(endpointUrl, HttpMethod.GET, null,
		 * String.class,inputs.getSsn());
		 */

		// perform WebService call to check whether SSN is valid or not and to get the
		// state name(Using WebClient)
		// get state name
		Mono<String> response = client.get().uri(endpointUrl, inputs.getSsn()).retrieve()
				.onStatus(HttpStatus.BAD_REQUEST::equals,res -> res.bodyToMono(String.class).map(ex->new InvalidSSNException("Invalid SSN"))).bodyToMono(String.class);

		String stateName = response.block();

		// register citizen if he belongs to California state (CA)
		if (stateName.equalsIgnoreCase(targetState)) {
			// perform the Entity Object
			CitizenAppRegistrationEntity entity = new CitizenAppRegistrationEntity();
			BeanUtils.copyProperties(inputs, entity);
			entity.setStateName(stateName);
			// save the object
			int appId = citizenRepo.save(entity).getAppId();
			return appId;

		}
		throw new InvalidSSNException("Invalid SSN");
	}

}
