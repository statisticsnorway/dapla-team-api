package no.ssb.dapla.team.util;


import org.junit.jupiter.api.Test;

import java.util.List;

import static no.ssb.dapla.team.util.GroupNameUtil.*;
import static org.assertj.core.api.Assertions.*;

class GroupNameUtilTest {

    @Test
    void givenGroupName_whenDeduceTeamNameFromGroup_thenReturnTeamName() {
        assertThat(deduceTeamNameFromGroup("demo-enhjoern-a-support")).isEqualTo("demo-enhjoern-a");
        assertThat(deduceTeamNameFromGroup("demo-enhjoern-a-support-blah")).isEqualTo("demo-enhjoern-a");
        assertThat(deduceTeamNameFromGroup("demo-enhjoern-a-support2-blah")).isEqualTo("demo-enhjoern-a");
        assertThat(deduceTeamNameFromGroup("demo-enhjoern-a-abc")).isEqualTo("demo-enhjoern-a-abc");
        assertThat(deduceTeamNameFromGroup("demo-enhjoern-a")).isEqualTo("demo-enhjoern-a");

        assertThatNullPointerException().isThrownBy(() -> {
            deduceTeamNameFromGroup(null);
        });
        assertThatIllegalArgumentException().isThrownBy(() -> {
            deduceTeamNameFromGroup("");
        });
        assertThatIllegalArgumentException().isThrownBy(() -> {
            deduceTeamNameFromGroup(" ");
        });
    }

    @Test
    void defaultGroupNamesForTeam() {
        List<String> groupNames = GroupNameUtil.defaultGroupNamesForTeam("demo-enhjoern-a");

        assertThat(groupNames)
                .hasSize(5)
                .hasSameElementsAs(List.of(
                        "demo-enhjoern-a-managers",
                        "demo-enhjoern-a-data-admins",
                        "demo-enhjoern-a-developers",
                        "demo-enhjoern-a-consumers",
                        "demo-enhjoern-a-support"
                ));
    }

    @Test
    void givenTeamName_whenDeduceManagersGroupName_thenReturnManagersGroupName() {
        assertThat(managersGroupNameOf("demo-enhjoern-a")).isEqualTo("demo-enhjoern-a-managers");
    }

    @Test
    void givenTeamName_whenDeduceGroupNameFromTeam_thenReturnGroupName() {
        assertThat(managersGroupNameOf("demo-enhjoern-a")).isEqualTo("demo-enhjoern-a-managers");
        assertThat(dataAdminsGroupNameOf("demo-enhjoern-a")).isEqualTo("demo-enhjoern-a-data-admins");
        assertThat(developersGroupNameOf("demo-enhjoern-a")).isEqualTo("demo-enhjoern-a-developers");
        assertThat(consumersGroupNameOf("demo-enhjoern-a")).isEqualTo("demo-enhjoern-a-consumers");
        assertThat(supportGroupNameOf("demo-enhjoern-a")).isEqualTo("demo-enhjoern-a-support");
    }

}
