package no.ssb.dapla.team;

import lombok.extern.slf4j.Slf4j;
import no.ssb.dapla.team.teams.Team;
import no.ssb.dapla.team.teams.TeamRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@Configuration
@Slf4j
public class LoadDatabase {

    @Bean
    CommandLineRunner initDatabase(TeamRepository teamRepository) {

        return args -> {

                    String[] groupsDef = {"-support","-data-admins","-managers","-developers"};
                    String[] team = {"demo-enhjoern-a","demo-enhjoern-b","demo-enhjoern-c","demo-enhjoern-d"};
                    String[] teamDisplay = {"Demo Enhjoern a","Demo Enhjoern B","Demo Enhjoern C","Demo Enhjoern D"};

                    for(int i = 0; i < team.length;i++){
                        log.info("Preloading" + teamRepository.save(

                        Team.builder().uniformTeamName(team[i]).displayTeamName("Demo Enhjørning A").build()));
                    }


        };
    }


}
