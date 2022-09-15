package no.ssb.dapla.team.teams;

import lombok.RequiredArgsConstructor;
import no.ssb.dapla.team.groups.Group;
import no.ssb.dapla.team.users.User;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @GetMapping()
    public CollectionModel<EntityModel<Team>> list() {
        return teamService.list();
    }

    @GetMapping("/{id}")
    public EntityModel<Team> getById(@PathVariable String id) {
        return teamService.getById(id);
    }

    @GetMapping("/{teamName}/users")
    public CollectionModel<EntityModel<User>> getUsersInTeam(@PathVariable String teamName) {
        return teamService.getUsersInTeam(teamName);
    }

    @GetMapping("/{teamName}/groups")
    public CollectionModel<EntityModel<Group>> listGroupsOfSpecificTeam(@PathVariable String teamName) {
        return teamService.listGroupsOfSpecificTeam(teamName);
    }

}
