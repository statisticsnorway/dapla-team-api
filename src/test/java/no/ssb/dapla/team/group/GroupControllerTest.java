package no.ssb.dapla.team.group;

import no.ssb.dapla.team.groups.Group;
import no.ssb.dapla.team.groups.GroupController;
import no.ssb.dapla.team.groups.GroupModelAssembler;
import no.ssb.dapla.team.groups.GroupRepository;
import no.ssb.dapla.team.users.User;
import no.ssb.dapla.team.users.UserModelAssembler;
import no.ssb.dapla.team.users.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GroupController.class)
@Import({GroupModelAssembler.class})
class GroupControllerTest {

    private static final User DONALD_DUCK = User.builder()
            .emailShort("kons-don@ssb.no")
            .name("Donald Duck")
            .email("donald.duck@ssb.no")
            .build();

    private static final User DOLLY_DUCK = User.builder()
            .emailShort("dol@ssb.no")
            .name("Dolly Duck")
            .email("dolly.duck@ssb.no")
            .build();

    private static final Group DEMO_ENHJOERN_A_MANAGERS = Group.builder()
            .id("demo-enhjoern-a-managers")
            .users(List.of(DOLLY_DUCK))
            .build();

    private static final Group DEMO_ENHJOERN_A_SUPPORT = Group.builder()
            .id("demo-enhjoern-a-support")
            .build();

    private static final Map<String, Group> GROUPS_BY_ID = Stream.of(DEMO_ENHJOERN_A_MANAGERS, DEMO_ENHJOERN_A_SUPPORT)
            .collect(Collectors.toMap(Group::getId, Function.identity()));


    @Autowired
    private MockMvc mvc;

    @MockBean
    private GroupRepository groupRepository;

    @MockBean
    private UserModelAssembler userModelAssembler;

    @MockBean
    private UserRepository userRepository;

    @Test
    @WithMockUser
    void givenGroups_whenListAllGroups_thenReturnHalDocument() throws Exception {
        given(groupRepository.findAll()).willReturn(new ArrayList<>(GROUPS_BY_ID.values()));

        mvc.perform(get("/groups").accept(MediaTypes.HAL_JSON_VALUE)) //
                .andDo(print())
                .andExpect(status().isOk()).andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("$._embedded.groupList[0].id", is("demo-enhjoern-a-managers")))
                .andExpect(jsonPath("$._embedded.groupList[0]._links.self.href", is("http://localhost/groups/demo-enhjoern-a-managers")))
                .andExpect(jsonPath("$._embedded.groupList[1].id", is("demo-enhjoern-a-support")))
                .andExpect(jsonPath("$._embedded.groupList[1]._links.self.href", is("http://localhost/groups/demo-enhjoern-a-support")))
                .andReturn();
    }

    // FIXME: This test is sketchy, also add a test for successful path
    @Test
    @WithMockUser(username = "kons-don@ssb.no")
    void givenNotManager_whenAddMemberToGroup_thenDenyRequest() throws Exception {
        given(groupRepository.getById(DEMO_ENHJOERN_A_MANAGERS.getId()))
                .willReturn(DEMO_ENHJOERN_A_MANAGERS);

        String requestBody = """
                    {
                      "emailShort": "kons-don@ssb.no",
                      "name": "Donald Duck",
                      "email": "donald.duck@ssb.no"
                    }
                """;

        mvc.perform(patch("/groups/demo-enhjoern-a-support")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isForbidden()).andReturn();
    }

}