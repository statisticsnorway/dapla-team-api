package no.ssb.dapla.team.teams;

import lombok.Data;
import no.ssb.dapla.team.groups.Group;
import no.ssb.dapla.team.groups.GroupController;
import no.ssb.dapla.team.groups.GroupModelAssembler;
import no.ssb.dapla.team.users.*;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Data
@Service
public class TeamService {
    private final TeamRepository teamRepository;
    private final TeamModelAssembler assembler;

    private final UserModelAssembler userModelAssembler;
    private final GroupModelAssembler groupModelAssembler;

    public CollectionModel<EntityModel<Team>> list() {
        List<EntityModel<Team>> teams = teamRepository.findAll().stream() //
                .map(assembler::toModel)
                .toList();

        return CollectionModel.of(teams, //
                linkTo(methodOn(TeamController.class).list()).withSelfRel());
    }

    public EntityModel<Team> getById(String id) {
        Team team = teamRepository.findById(id) //
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Team " + id + " does not exist"));

        return assembler.toModel(team);
    }

    public CollectionModel<EntityModel<User>> getUsersInTeam(String teamName) {
        Team team = teamRepository.findById(teamName) //
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Team " + teamName + " does not exist"));

        HashSet<User> resultSet = new HashSet<>();

        team.getGroups()
                .forEach(group -> resultSet.addAll(group.getUsers()));

        List<EntityModel<User>> resultModel = resultSet.stream()
                .map(userModelAssembler::toModel)
                .toList();

        return CollectionModel.of(resultModel, //
                linkTo(methodOn(UserController.class).list()).withSelfRel());

    }

    public CollectionModel<EntityModel<Group>> listGroupsOfSpecificTeam(String teamName) {
        Team team = teamRepository.findById(teamName) //
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Team " + teamName + " does not exist"));

        List<EntityModel<Group>> resultModel = team.getGroups().stream()
                .map(groupModelAssembler::toModel)
                .toList();

        return CollectionModel.of(resultModel, //
                linkTo(methodOn(GroupController.class).list()).withSelfRel());
    }

    public EntityModel<User> patchUser(String teamName, String groupName,User user) {
        Team team = teamRepository.findById(teamName) //
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Team " + teamName + " does not exist"));

        Group group =team.getGroups().stream()
                .filter(groupTemp -> groupTemp.getId().equals(groupName))
                .findAny()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Group " + groupName + " does not exist"));

        Optional<User> optionalUser = group.getUsers().stream()
                .filter(userTemp -> userTemp.getEmailShort().equals(user.getEmailShort()))
                .findAny();

        if(optionalUser.isEmpty()){
            group.getUsers().add(user);
            teamRepository.saveAndFlush(team);
        }


        return userModelAssembler.toModel(user);
    }
}
