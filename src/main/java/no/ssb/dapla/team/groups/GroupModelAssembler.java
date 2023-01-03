package no.ssb.dapla.team.groups;

import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class GroupModelAssembler implements RepresentationModelAssembler<Group, GroupModel> {
    @Override
    public GroupModel toModel(Group group) {

        GroupModel model = new GroupModel(group);
        model.add(linkTo(methodOn(GroupController.class).getById(group.getId())).withSelfRel());
        model.add(linkTo(methodOn(GroupController.class).getUsers(group.getId())).withRel("users"));

        return model;
    }
}
