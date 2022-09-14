package no.ssb.dapla.team;

import lombok.extern.slf4j.Slf4j;
import no.ssb.dapla.team.groups.Group;
import no.ssb.dapla.team.groups.GroupRepository;
import no.ssb.dapla.team.teams.Team;
import no.ssb.dapla.team.teams.TeamRepository;
import no.ssb.dapla.team.users.User;
import no.ssb.dapla.team.users.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sound.sampled.Line;
import java.util.*;
import java.util.stream.Collectors;

@Configuration
@Slf4j
public class LoadDatabase {

    @Bean
    CommandLineRunner initDatabase(TeamRepository teamRepository, GroupRepository groupRepository, UserRepository userRepository) {

        return args -> {

            String[] groupsPostfixList = {"-support", "-data-admins", "-managers", "-developers"};
            String[] teamNameList = {"demo-enhjoern-a", "demo-enhjoern-b", "demo-enhjoern-c", "demo-enhjoern-d"};
            String[] teamDisplayNameList = {"Demo Enhjoern a", "Demo Enhjoern B", "Demo Enhjoern C", "Demo Enhjoern D"};
            String[] names = {"Mikke", "Dolly", "Skrue"};


            LinkedList<Team> teams = new LinkedList<>();
            LinkedList<Group> groups = new LinkedList<>();
            LinkedList<User> users = new LinkedList<>();


            Team t = new Team();
            t.setDisplayTeamName("Demo Enhjoern a");
            t.setUniformTeamName("demo-enhjoern-a");

            Group g = new Group();
            g.setId("demo-enhjoern-a-support");

            User u = new User();
            u.setName("Donald");
            u.setEmailShort("kons-don@ssb.no");
            u.setEmail("donald.duck@ssb.no");

            g.setUsers(Set.of(u));
            log.info(g.getId());
            t.setGroups(Set.of(g));
            log.info("Preloading" + groupRepository.save(
                    g
            ));
            log.info("Preloading" + teamRepository.save(
                    t
            ));

            t = new Team();
            t.setDisplayTeamName("Demo Enhjoern b");
            t.setUniformTeamName("demo-enhjoern-b");

            g = new Group();
            g.setId("demo-enhjoern-b-support");

            u = new User();
            u.setName("Mikke");
            u.setEmailShort("kons-mikke@ssb.no");
            u.setEmail("mus.mikke@ssb.no");

            g.setUsers(Set.of(u));
            log.info(g.getId());
            t.setGroups(Set.of(g));
            log.info("Preloading" + groupRepository.save(
                    g
            ));
            log.info("Preloading" + teamRepository.save(
                    t
            ));


        };
    }


}
