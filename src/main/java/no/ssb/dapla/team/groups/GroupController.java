package no.ssb.dapla.team.groups;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping(value = "/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @GetMapping()
    public CollectionModel<EntityModel<Group>> list() {
        return groupService.list();
    }

    @GetMapping("/{id}")
    public EntityModel<Group> getById(@PathVariable String id) {
        return groupService.getById(id);
    }

}
