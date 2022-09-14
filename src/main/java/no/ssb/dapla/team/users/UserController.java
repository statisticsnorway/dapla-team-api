package no.ssb.dapla.team.users;

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
@RequestMapping(value = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    private final UserModelAssembler assembler;

    @GetMapping()
    public CollectionModel<EntityModel<User>> list() {
        List<EntityModel<User>> user = userRepository.findAll().stream() //
                .map(assembler::toModel)
                .toList();

        return CollectionModel.of(user, //
                linkTo(methodOn(UserController.class).list()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<User> getById(@PathVariable String id) {
        User user = userRepository.findById(id) //
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User " + id + " does not exist"));

        return assembler.toModel(user);
    }

}
