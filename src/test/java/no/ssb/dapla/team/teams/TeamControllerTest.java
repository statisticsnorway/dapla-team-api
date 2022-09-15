package no.ssb.dapla.team.teams;

import no.ssb.dapla.team.groups.GroupModelAssembler;
import no.ssb.dapla.team.users.UserModelAssembler;
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
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TeamController.class)
@Import({ TeamModelAssembler.class, TeamService.class})
class TeamControllerTest {

    private final Map<String, Team> teams = Stream.of(
            Team.builder().uniformTeamName("demo-enhjoern-a").displayTeamName("Demo Enhjørning A").build(),
            Team.builder().uniformTeamName("demo-enhjoern-b").displayTeamName("Demo Enhjørning B").build()
    ).collect(Collectors.toMap(
            Team::getUniformTeamName, Function.identity()
    ));


    @Autowired
    private MockMvc mvc;

    @MockBean
    private TeamRepository repository;

    @MockBean
    private UserModelAssembler userModelAssembler;

    @MockBean
    private GroupModelAssembler groupModelAssembler;

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