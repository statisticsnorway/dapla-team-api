package no.ssb.dapla.team.groups;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class GroupModelAssembler implements RepresentationModelAssembler<Group, EntityModel<Group>> {
    @Override
    public EntityModel<Group> toModel(Group group) {

        return EntityModel.of(group, //
                linkTo(methodOn(GroupController.class).getById(group.getId())).withSelfRel());
    }
}
