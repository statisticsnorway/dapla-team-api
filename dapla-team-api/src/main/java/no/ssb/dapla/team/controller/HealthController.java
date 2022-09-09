package no.ssb.dapla.team.controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
* Rest controller for Health Indicators
* @author  Anders Lunde
* @version 0.01
* @since   2022-09-09 
*/
@RestController
public class HealthController {
	
	/**
	 * Can be used to poll for API liveness
	 * @return Map containing {"name":"dapla-start-api","status":"UP"} 
	 */
	
	@GetMapping("health/liveness")
	public Map<String, String> getHealthMapping(){
		Map<String, String> response = new HashMap<>();
		response.put("name", "dapla-start-api");
		response.put("status", "UP");
		
		return response;
	}
	
	
	/**
	 * Can be used to poll for API readiness
	 * @return Map containing {"name":"dapla-start-api","status":"UP"} 
	 */
	
	@GetMapping("health/readiness")
	public Map<String, String> getHealthReadiness(){
		Map<String, String> response = new HashMap<>();
		response.put("name", "dapla-start-api");
		response.put("status", "UP");
		return response;
	}
	
	

}
