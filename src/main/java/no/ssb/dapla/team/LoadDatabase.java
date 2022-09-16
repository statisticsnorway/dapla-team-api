package no.ssb.dapla.team;

import lombok.extern.slf4j.Slf4j;
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
    CommandLineRunner initDatabase(UserRepository userRepository, FilebasedUserService filebasedUserService) {

        return args -> {
            List<User> users = filebasedUserService.fetchAllUsers();
            for (User user : users) userRepository.save(user);
        };
    }
}
