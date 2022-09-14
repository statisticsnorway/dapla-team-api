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

import java.util.*;

@Configuration
@Slf4j
public class LoadDatabase {

    @Bean
    CommandLineRunner initDatabase(TeamRepository teamRepository, GroupRepository groupRepository) {

        return args -> {

            String[] groupsPostfixList = {"-support", "-data-admins", "-managers", "-developers"};
            String[] teamNameList = {"demo-enhjoern-a", "demo-enhjoern-b", "demo-enhjoern-c", "demo-enhjoern-d"};
            String[] teamDisplayNameList = {"Demo Enhjoern a", "Demo Enhjoern B", "Demo Enhjoern C", "Demo Enhjoern D"};
            String[] names = {"Mikke", "Dolly", "Skrue","Jokke","Mons"};
            String[] shortEmail = {"Mi@ssb.no", "Do@ssb.no", "Sk@ssb.no","Jo@sbb.no","Mons@ssb.no"};
            String[] longEmail = {"Mikke@ssb.no", "Dolly@sbb.no", "Skrue.no","Jokke@ssb.no","Mons@ssb.no"};

            /*
            Group g = Group.builder().id("demo-enhjoern-a-support").users(Set.of(
                    User.builder().name("Nikk").emailShort("nih@ssb.no").email("nikk@ssb.no").build(),
                    User.builder().name("Harry").emailShort("hah@ssb.no").email("harry@ssb.no").build(),
                    User.builder().name("Henry").emailShort("hh@ssb.no").email("henry@ssb.no").build()
            )).build();

            log.info("Preloading" + groupRepository.save(
                    g
            ));*/

            for(int i = 0; i < teamNameList.length;i++) {
                Team t = Team.builder()
                        .uniformTeamName(teamNameList[i])
                        .displayTeamName(teamDisplayNameList[i])
                        .Groups(Set.of(Group.builder()
                                        .id(teamNameList[i]+groupsPostfixList[i])
                                        .users(Set.of(User.builder()
                                                .name(names[i])
                                                .emailShort(shortEmail[i])
                                                .email(longEmail[i]).build()))
                                .build()))
                        .build();
                log.info("Preloading" + teamRepository.save(t));
            }

        };
    }
}
