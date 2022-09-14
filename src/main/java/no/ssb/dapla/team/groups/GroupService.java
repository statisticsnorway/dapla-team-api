package no.ssb.dapla.team.groups;

import lombok.Data;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Data
@Service
public class GroupService {
    private final GroupRepository groupRepository;

    private final GroupModelAssembler assembler;

    public CollectionModel<EntityModel<Group>> list() {
        List<EntityModel<Group>> groups = groupRepository.findAll().stream() //
                .map(assembler::toModel) //
                .toList();

        return CollectionModel.of(groups, //
                linkTo(methodOn(GroupController.class).list()).withSelfRel());
    }

    public EntityModel<Group> getById(String id) {
        Group team = groupRepository.findById(id) //
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Group " + id + " does not exist"));

        return assembler.toModel(team);
    }
}
