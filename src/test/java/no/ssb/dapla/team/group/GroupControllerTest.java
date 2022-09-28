package no.ssb.dapla.team.group;

import no.ssb.dapla.team.groups.Group;
import no.ssb.dapla.team.groups.GroupController;
import no.ssb.dapla.team.groups.GroupModelAssembler;
import no.ssb.dapla.team.groups.GroupRepository;
import no.ssb.dapla.team.users.UserModelAssembler;
import no.ssb.dapla.team.users.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithMockUser;
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

@WebMvcTest(GroupController.class)
@Import({GroupModelAssembler.class})
class GroupControllerTest {

    private final Map<String, Group> groups = Stream.of(Group.builder()
                            .id("demo-enhjoern-a-support").build(),
                    Group.builder().id("demo-enhjoern-b-support").build())
            .collect(Collectors.toMap(Group::getId, Function.identity()));


    @Autowired
    private MockMvc mvc;

    @MockBean
    private GroupRepository repository;

    @MockBean
    private UserModelAssembler userModelAssembler;

    @MockBean
    private UserRepository userRepository;

    @Test
    @WithMockUser
    void givenGroups_whenListAllGroups_thenReturnHalDocument() throws Exception {
        given(repository.findAll()).willReturn(new ArrayList<>(groups.values()));

        mvc.perform(get("/groups").accept(MediaTypes.HAL_JSON_VALUE)) //
                .andDo(print()).andExpect(status().isOk()).andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE)).andExpect(jsonPath("$._embedded.groupList[0].id", is("demo-enhjoern-b-support"))).andExpect(jsonPath("$._embedded.groupList[0]._links.self.href", is("http://localhost/groups/demo-enhjoern-b-support"))).andExpect(jsonPath("$._embedded.groupList[1].id", is("demo-enhjoern-a-support"))).andExpect(jsonPath("$._embedded.groupList[1]._links.self.href", is("http://localhost/groups/demo-enhjoern-a-support"))).andReturn();
    }
}