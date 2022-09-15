package no.ssb.dapla.team.user;

import no.ssb.dapla.team.users.*;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@WebMvcTest(UserController.class)
@Import({ UserModelAssembler.class, UserService.class})
class UserControllerTest {

    private final Map<String, User> users = Stream.of(
            User.builder().name("Harry").emailShort("hah@ssb.no").email("harry@ssb.no").build(),
            User.builder().name("Nikk").emailShort("nih@ssb.no").email("nikk@ssb.no").build()
    ).collect(Collectors.toMap(
            User::getEmailShort, Function.identity()
    ));

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserRepository repository;


    @Test
    void givenUsers_whenListAllUsers_thenReturnHalDocument()
            throws Exception {
        given(repository.findAll()).willReturn(
                new ArrayList<>(users.values()));

        mvc.perform(get("/users").accept(MediaTypes.HAL_JSON_VALUE)) //
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("$._embedded.userList[0].name", is("Harry")))
                .andExpect(jsonPath("$._embedded.userList[0].emailShort", is("hah@ssb.no")))
                .andExpect(jsonPath("$._embedded.userList[0].email", is("harry@ssb.no")))
                .andExpect(jsonPath("$._embedded.userList[0]._links.self.href", is("http://localhost/users/hah%40ssb.no")))
                .andExpect(jsonPath("$._embedded.userList[1].name", is("Nikk")))
                .andExpect(jsonPath("$._embedded.userList[1].emailShort", is("nih@ssb.no")))
                .andExpect(jsonPath("$._embedded.userList[1].email", is("nikk@ssb.no")))
                .andExpect(jsonPath("$._embedded.userList[1]._links.self.href", is("http://localhost/users/nih%40ssb.no")))
                .andReturn();
    }
}

