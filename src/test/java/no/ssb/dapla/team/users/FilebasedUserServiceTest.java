package no.ssb.dapla.team.users;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class FilebasedUserServiceTest {

    @Test
    void fetchAllUsers() {
        FilebasedUserService userService = new FilebasedUserService("src/test/resources/ssb-users.csv");
        List<User> users = userService.fetchAllUsers();
        assertThat(users).isNotEmpty();
        assertThat(users.get(0).getName()).isEqualTo("Ku Klara");
    }
}