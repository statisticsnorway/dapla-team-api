package no.ssb.dapla.team.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The api´s home controller
 * @author Anders
 * @version 0.1
 */
@RestController
public class HomeController {
	/**
	 * Retrieves app version string from application.propperties
	 */
    @Value("${app.version}")
    private String appVersion;
    
	/**
	 * Retrieves app name string from application.propperties
	 */
    @Value("${app.name}")
    private String appName;

	/**
	 * Home end point, retrieves app name string from application.propperties
	 * @return Map with the keys app-name, app-version and their respective values
	 */
    @RequestMapping(value = "/")
    public Map<String,String> getStatus() {
    	Map<String,String> response = new HashMap<>();
    	response.put("app-name", appName);
    	response.put("app-version", appVersion);
        return response;
    }

}
