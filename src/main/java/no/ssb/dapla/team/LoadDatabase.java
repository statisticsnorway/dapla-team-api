package no.ssb.dapla.team;

import lombok.extern.slf4j.Slf4j;
import no.ssb.dapla.team.teams.Team;
import no.ssb.dapla.team.teams.TeamRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class LoadDatabase {

    @Bean
    CommandLineRunner initDatabase(TeamRepository teamRepository) {

        return args -> {
            log.info("Preloading" + teamRepository.save(
                    Team.builder().uniformTeamName("demo-enhjoern-a").displayTeamName("Demo Enhjørning A").build()));
            log.info("Preloading" + teamRepository.save(
                    Team.builder().uniformTeamName("demo-enhjoern-b").displayTeamName("Demo Enhjørning B").build()));
        };
    }


}
