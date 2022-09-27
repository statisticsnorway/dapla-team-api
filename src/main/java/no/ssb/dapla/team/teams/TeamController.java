package no.ssb.dapla.team.teams;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import no.ssb.dapla.team.groups.Group;
import no.ssb.dapla.team.groups.GroupController;
import no.ssb.dapla.team.groups.GroupModelAssembler;
import no.ssb.dapla.team.users.User;
import no.ssb.dapla.team.users.UserController;
import no.ssb.dapla.team.users.UserModelAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamRepository teamRepository;
    private final TeamModelAssembler assembler;
    private final UserModelAssembler userModelAssembler;
    private final GroupModelAssembler groupModelAssembler;

    @Operation(summary = "Get all teams")
    @GetMapping()
    public CollectionModel<EntityModel<Team>> list() {
        List<EntityModel<Team>> teams = teamRepository.findAll().stream() //
                .map(assembler::toModel).toList();

        return CollectionModel.of(teams, //
                linkTo(methodOn(TeamController.class).list()).withSelfRel());
    }

    @Operation(summary = "Get team by id")
    @GetMapping("/{id}")
    public EntityModel<Team> getById(@PathVariable String id) {
        Team team = teamRepository.findById(id) //
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Team " + id + " does not exist"));

        return assembler.toModel(team);
    }

    @Operation(summary = "Get users in a team by teamId")
    @GetMapping("/{teamId}/users")
    public CollectionModel<EntityModel<User>> getUsersInTeam(@PathVariable String teamId) {
        Team team = teamRepository.findById(teamId) //
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Team " + teamId + " does not exist"));

        HashSet<User> resultSet = new HashSet<>();

        team.getGroups().forEach(group -> resultSet.addAll(group.getUsers()));

        List<EntityModel<User>> resultModel = resultSet.stream().map(userModelAssembler::toModel).toList();

        return CollectionModel.of(resultModel, //
                linkTo(methodOn(UserController.class).list()).withSelfRel());
    }

    @Operation(summary = "Get groups of a team by teamId")
    @GetMapping("/{teamId}/groups")
    public CollectionModel<EntityModel<Group>> listGroupsOfSpecificTeam(@PathVariable String teamId) {
        Team team = teamRepository.findById(teamId) //
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Team " + teamId + " does not exist"));

        List<EntityModel<Group>> resultModel = team.getGroups().stream().map(groupModelAssembler::toModel).toList();

        return CollectionModel.of(resultModel, //
                linkTo(methodOn(GroupController.class).list()).withSelfRel());
    }


}
