package no.ssb.dapla.team.users;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping()
    public CollectionModel<EntityModel<User>> list() {
        return userService.list();
    }

    @GetMapping("/{id}")
    public EntityModel<User> getById(@PathVariable String id) {
        return userService.getById(id);
    }

}
