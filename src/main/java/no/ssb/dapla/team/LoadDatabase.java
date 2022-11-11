package no.ssb.dapla.team;

import lombok.extern.slf4j.Slf4j;
import no.ssb.dapla.team.github.GitHubService;
import no.ssb.dapla.team.groups.GroupRepository;
import no.ssb.dapla.team.groups.GroupService;
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
                                   GroupRepository groupRepository,
                                   FilebasedUserService filebasedUserService,
                                   GitHubService gitHubService,
                                   GroupService groupService) {

        return args -> {
            // Users
            List<User> users = filebasedUserService.fetchAllUsers();
            userRepository.saveAll(users);
            List<Team> teams = gitHubService.getTeamListWithTopic("dapla-team");
            log.info("Fetched teams from GitHub:\n {}", teams.stream().map(t -> t.getUniformTeamName()).toList());
            teamRepository.saveAll(teams);

            List<String> teamSuffixes = List.of("managers", "data-admins", "developers", "consumers", "support");
            teamSuffixes.forEach(teamSuffix -> {
                        teams.forEach(team -> {
                            groupService.findGroupByName(team.getUniformTeamName().replace("-iac", "") + "-" + teamSuffix)
                                    .ifPresent(groupRepository::save);
                        });
                    }
            );
        };
    }

}
