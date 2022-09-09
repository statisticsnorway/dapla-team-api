package no.ssb.dapla.team.controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
	
	@GetMapping("health/liveness")
	public Map<String, String> getHealthMapping(){
	    //Can be used to poll for API liveness
		Map<String, String> response = new HashMap<>();
		response.put("name", "dapla-start-api");
		response.put("status", "UP");
		
		return response;
	}
	
	@GetMapping("health/readiness")
	public Map<String, String> getHealthReadiness(){
	    //Can be used to poll for API readiness
		Map<String, String> response = new HashMap<>();
		response.put("name", "dapla-start-api");
		response.put("status", "UP");
		return response;
	}
	
	

}
