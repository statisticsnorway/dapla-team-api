package no.ssb.dapla.team;

import lombok.extern.slf4j.Slf4j;
import no.ssb.dapla.team.groups.Group;
import no.ssb.dapla.team.groups.GroupRepository;
import no.ssb.dapla.team.teams.Team;
import no.ssb.dapla.team.teams.TeamRepository;
import no.ssb.dapla.team.users.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@Slf4j
public class LoadDatabase {

    @Bean
    CommandLineRunner initDatabase(TeamRepository teamRepository, GroupRepository groupRepository) {

        return args -> {

            String[] groupsPostfixList = {"-support", "-data-admins", "-managers", "-developers"};
            String[] teamNameList = {"demo-enhjoern-a", "demo-enhjoern-b", "demo-enhjoern-c", "demo-enhjoern-d"};
            String[] teamDisplayNameList = {"Demo Enhjoern a", "Demo Enhjoern B", "Demo Enhjoern C", "Demo Enhjoern D"};
            String[] names = {"Mikke", "Dolly", "Skrue", "Jokke", "Mons"};
            String[] shortEmail = {"Mi@ssb.no", "Do@ssb.no", "Sk@ssb.no", "Jo@sbb.no", "Mons@ssb.no"};
            String[] longEmail = {"Mikke@ssb.no", "Dolly@sbb.no", "Skrue.no", "Jokke@ssb.no", "Mons@ssb.no"};

            for (int i = 0; i < teamNameList.length; i++) {
                Team t = Team.builder()
                        .uniformTeamName(teamNameList[i])
                        .displayTeamName(teamDisplayNameList[i])
                        .groups(List.of(Group.builder()
                                .id(teamNameList[i] + groupsPostfixList[i])
                                .users(List.of(User.builder()
                                        .name(names[i])
                                        .emailShort(shortEmail[i])
                                        .email(longEmail[i]).build(), User.builder()
                                        .name("jens")
                                        .emailShort("mann@ssb.no")
                                        .email("ads@ssb.no").build()))
                                .build()))
                        .build();
                log.info("Preloading" + teamRepository.save(t));
            }
        };
    }
}
