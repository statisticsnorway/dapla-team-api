package no.ssb.dapla.team.groups;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupRepository groupRepository;

    private final GroupModelAssembler assembler;

    @GetMapping()
    public CollectionModel<EntityModel<Group>> list() {
        List<EntityModel<Group>> groups = groupRepository.findAll().stream() //
                .map(assembler::toModel) //
                .toList();

        return CollectionModel.of(groups, //
                linkTo(methodOn(GroupController.class).list()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<Group> getById(@PathVariable String groupId) {
        Group team = groupRepository.findById(groupId) //
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Group " + groupId + " does not exist"));

        return assembler.toModel(team);
    }

}
