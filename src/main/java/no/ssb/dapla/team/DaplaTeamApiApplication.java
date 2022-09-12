package no.ssb.dapla.team;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class DaplaTeamApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(DaplaTeamApiApplication.class, args);
		log.info("Version {}", BuildInfo.INSTANCE.getVersionAndBuildTimestamp());
	}

}
