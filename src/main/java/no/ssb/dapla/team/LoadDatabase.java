package no.ssb.dapla.team;

import lombok.extern.slf4j.Slf4j;
import no.ssb.dapla.team.github.GitHubService;
import no.ssb.dapla.team.groups.Group;
import no.ssb.dapla.team.teams.Team;
import no.ssb.dapla.team.teams.TeamRepository;
import no.ssb.dapla.team.users.FilebasedUserService;
import no.ssb.dapla.team.users.User;
import no.ssb.dapla.team.users.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;


@Configuration
@Slf4j
public class LoadDatabase {
    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository,
                                   TeamRepository teamRepository,
                                   FilebasedUserService filebasedUserService,
                                   GitHubService gitHubService) {

        return args -> {
            // Users
            List<User> users = filebasedUserService.fetchAllUsers();
            userRepository.saveAll(users);
            //System.out.println(microsoftGraphService.getJsonTeamListFromGraph());
            // Teams
            //try {
            List<Team> teams = gitHubService.getTeamListWithTopic("dapla-team");
            teamRepository.saveAll(teams);
           /* }catch(NullPointerException e){
                new RuntimeException(e);
            }*/
        };
    }

    private static Team teamWithGroupsAndMembers(String uniformTeamName, String displayTeamName, List<User> users) {
        return Team.builder()
                .uniformTeamName(uniformTeamName)
                .displayTeamName(displayTeamName)
                .groups(List.of(
                        Group.builder()
                                .id(uniformTeamName + "-managers")
                                .users(users.subList(0, 1))
                                .build(),
                        Group.builder()
                                .id(uniformTeamName + "-data-admins")
                                .users(users.subList(1, 2))
                                .build(),
                        Group.builder()
                                .id(uniformTeamName + "-developers")
                                .users(users.subList(2, 4))
                                .build(),
                        Group.builder()
                                .id(uniformTeamName + "-support")
                                .users(users.subList(4, 5))
                                .build()
                )).build();
    }
}
