package no.ssb.dapla.team.teams;

import lombok.Data;
import no.ssb.dapla.team.groups.Group;
import no.ssb.dapla.team.users.User;
import no.ssb.dapla.team.users.UserController;
import no.ssb.dapla.team.users.UserModelAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Data
@Service
public class TeamService {
    private final TeamRepository teamRepository;

    private final TeamModelAssembler assembler;

    private final UserModelAssembler userModelAssembler;

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

        Set<User> resultSet = new HashSet();
        for(Group g : team.getGroups()){
            g.getUsers().forEach(elem -> resultSet.add(elem));
        }

        List<EntityModel<User>> resultModel = resultSet.stream()
                .map(userModelAssembler::toModel)
                .toList();

        return CollectionModel.of(resultModel, //
                linkTo(methodOn(UserController.class).list()).withSelfRel());

    }


}
