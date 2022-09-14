package no.ssb.dapla.team.teams;

import no.ssb.dapla.team.groups.Group;
import no.ssb.dapla.team.users.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TeamController.class)
@Import({ TeamModelAssembler.class })
class TeamControllerTest {

    private final Map<String, Team> teams = Set.of(
            Team.builder().uniformTeamName("demo-enhjoern-a").displayTeamName("Demo Enhjørning A")
                    .Groups(Set.of(Group.builder()
                            .id("group_id_test")
                            .users(Set.of(User.builder().emailShort("test_email_short")
                                    .build()))
                            .build()))
                    .build(),
            Team.builder().uniformTeamName("demo-enhjoern-b").displayTeamName("Demo Enhjørning B")
                    .Groups(Set.of(Group.builder()
                            .id("group_id_test_2")
                            .users(Set.of(User.builder().emailShort("test_email_short_2")
                                    .build()))
                            .build()))
                    .build()
    ).stream().collect(Collectors.toMap(
            Team::getUniformTeamName, Function.identity()
    ));

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TeamRepository repository;

    @MockBean
    private TeamService teamService;

    @Test
    void givenTeams_whenListAllTeams_thenReturnHalDocument()
            throws Exception {

        given(repository.findAll()).willReturn(
                new ArrayList<>(teams.values()));

        mvc.perform(get("/teams").accept(MediaTypes.HAL_JSON_VALUE)) //
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("$._embedded.teamList[0].uniformTeamName", is("demo-enhjoern-a")))
                .andExpect(jsonPath("$._embedded.teamList[0].displayTeamName", is("Demo Enhjørning A")))
                .andExpect(jsonPath("$._embedded.teamList[0]._links.self.href", is("http://localhost/teams/demo-enhjoern-a")))
                .andExpect(jsonPath("$._embedded.teamList[1].uniformTeamName", is("demo-enhjoern-b")))
                .andExpect(jsonPath("$._embedded.teamList[1].displayTeamName", is("Demo Enhjørning B")))
                .andExpect(jsonPath("$._embedded.teamList[1]._links.self.href", is("http://localhost/teams/demo-enhjoern-b")))
                .andReturn();
    }
}