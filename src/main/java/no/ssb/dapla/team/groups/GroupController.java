package no.ssb.dapla.team.groups;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import no.ssb.dapla.team.users.User;
import no.ssb.dapla.team.users.UserModelAssembler;
import no.ssb.dapla.team.users.UserRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupRepository groupRepository;

    private final GroupModelAssembler assembler;

    private final UserModelAssembler userModelAssembler;

    private final UserRepository userRepository;


    @Operation(summary = "Get all groups")
    @GetMapping()
    public CollectionModel<EntityModel<Group>> list() {
        List<EntityModel<Group>> groups = groupRepository.findAll().stream() //
                .map(assembler::toModel) //
                .toList();

        return CollectionModel.of(groups, //
                linkTo(methodOn(GroupController.class).list()).withSelfRel());
    }

    @Operation(summary = "Get group by id")
    @GetMapping("/{id}")
    public EntityModel<Group> getById(@PathVariable String id) {
        Group team = groupRepository.findById(id) //
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Group " + id + " does not exist"));

        return assembler.toModel(team);
    }

    @Operation(summary = "Add user to group")
    @ResponseStatus(code = HttpStatus.CREATED)
    @PatchMapping("/{groupId}")
    public EntityModel<User> patchUser(@PathVariable String groupId, @RequestBody User user) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Group " + groupId + " does not exist"));
        //User needs to be in db
        User userInDatabase = userRepository
                .findById(user.getEmailShort())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User " + user.getEmailShort() + " does not exist"));

        Optional<User> optionalUser = group
                .getUsers()
                .stream()
                .filter(userTemp -> userTemp.getEmailShort().equals(user.getEmailShort()))
                .findAny();

        if (optionalUser.isEmpty()) {
            group.getUsers().add(userInDatabase);
            groupRepository.saveAndFlush(group);
        }


        return userModelAssembler.toModel(userInDatabase);
    }

}
