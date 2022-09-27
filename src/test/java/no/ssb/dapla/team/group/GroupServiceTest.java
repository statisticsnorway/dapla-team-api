package no.ssb.dapla.team.group;

import no.ssb.dapla.team.groups.Group;
import no.ssb.dapla.team.groups.GroupService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@Disabled
@SpringBootTest
@ActiveProfiles("local")
class GroupServiceTest {

    @Autowired
    private GroupService groupService;

    @Test
    void testGetGroupMembers() {
        groupService.getGroupMembers("demo-enhjoern-a-managers");
    }

    @Test
    void testGetGroupByName() {
        Group group = groupService.findGroupByName("demo-enhjoern-a-managers").orElseThrow();
        System.out.println(group);
    }

}
