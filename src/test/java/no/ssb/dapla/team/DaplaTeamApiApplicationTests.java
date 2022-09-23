package no.ssb.dapla.team;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class DaplaTeamApiApplicationTests {

    @MockBean LoadDatabase loadDatabase;

    @Test
    void contextLoads() {
    }

}
