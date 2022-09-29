package no.ssb.dapla.team.security;

import no.ssb.dapla.team.groups.Group;
import no.ssb.dapla.team.groups.GroupRepository;
import no.ssb.dapla.team.users.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.Principal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GroupAuthorizerTest {

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

    private static final Group DEMO_ENHJOERN_A_MANAGERS_MANAGED_BY_DOLLY = Group.builder()
            .id("demo-enhjoern-a-managers")
            .users(List.of(DOLLY_DUCK))
            .build();

    @Mock
    GroupRepository groupRepository;

    @Mock Principal principal;

    GroupAuthorizer authorizer;

    @BeforeEach
    void setup() {
        authorizer = new GroupAuthorizer(groupRepository);
    }

    @Test
    void givenMemberOfGroup_whenCheckingIsMemberOfGroup_thenReturnTrue() {
        when(principal.getName()).thenReturn(DOLLY_DUCK.getEmailShort());
        when(groupRepository.getReferenceById("demo-enhjoern-a-managers")).thenReturn(Group.builder()
                        .id("demo-enhjoern-a-managers")
                        .users(List.of(DOLLY_DUCK))
                .build());

        assertThat(authorizer.isMemberOfGroup(principal, "demo-enhjoern-a-managers")).isTrue();
    }

    @Test
    void givenNotMemberOfGroup_whenCheckingIsMemberOfGroup_thenReturnFalse() {
        when(principal.getName()).thenReturn(DONALD_DUCK.getEmailShort());
        when(groupRepository.getReferenceById("demo-enhjoern-a-managers"))
                .thenReturn(DEMO_ENHJOERN_A_MANAGERS_MANAGED_BY_DOLLY);

        assertThat(authorizer.isMemberOfGroup(principal, "demo-enhjoern-a-managers")).isFalse();
    }

    @Test
    void givenManagerOfTeam_whenCheckingIsTeamManagerForTeam_thenReturnTrue() {
        when(principal.getName()).thenReturn(DOLLY_DUCK.getEmailShort());
        when(groupRepository.getReferenceById("demo-enhjoern-a-managers"))
                .thenReturn(DEMO_ENHJOERN_A_MANAGERS_MANAGED_BY_DOLLY);

        assertThat(authorizer.isTeamManagerForTeam(principal, "demo-enhjoern-a")).isTrue();
    }

    @Test
    void givenNotManagerOfTeam_whenCheckingIsTeamManagerForTeam_thenReturnFalse() {
        when(principal.getName()).thenReturn(DONALD_DUCK.getEmailShort());
        when(groupRepository.getReferenceById("demo-enhjoern-a-managers"))
                .thenReturn(DEMO_ENHJOERN_A_MANAGERS_MANAGED_BY_DOLLY);

        assertThat(authorizer.isTeamManagerForTeam(principal, "demo-enhjoern-a")).isFalse();
    }

    @Test
    void givenManagerOfTeam_whenCheckingIsTeamManagerForAssociatedGroup_thenReturnTrue() {
        when(principal.getName()).thenReturn(DOLLY_DUCK.getEmailShort());
        when(groupRepository.getReferenceById("demo-enhjoern-a-managers"))
                .thenReturn(DEMO_ENHJOERN_A_MANAGERS_MANAGED_BY_DOLLY);

        assertThat(authorizer.isTeamManagerForAssociatedGroup(principal, "demo-enhjoern-a-support")).isTrue();
    }

    @Test
    void givenNotManagerOfTeam_whenCheckingIsTeamManagerForAssociatedGroup_thenReturnFalse() {
        when(principal.getName()).thenReturn(DONALD_DUCK.getEmailShort());
        when(groupRepository.getReferenceById("demo-enhjoern-a-managers"))
                .thenReturn(DEMO_ENHJOERN_A_MANAGERS_MANAGED_BY_DOLLY);

        assertThat(authorizer.isTeamManagerForAssociatedGroup(principal, "demo-enhjoern-a-support")).isFalse();
    }

}