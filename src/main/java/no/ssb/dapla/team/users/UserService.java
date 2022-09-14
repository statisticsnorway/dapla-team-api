package no.ssb.dapla.team.users;

import lombok.Data;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Data
@Service
public class UserService {
    private final UserRepository userRepository;

    private final UserModelAssembler assembler;

    public CollectionModel<EntityModel<User>> list() {
        List<EntityModel<User>> user = userRepository.findAll().stream() //
                .map(assembler::toModel)
                .toList();

        return CollectionModel.of(user, //
                linkTo(methodOn(UserController.class).list()).withSelfRel());
    }

    public EntityModel<User> getById(String id) {
        User user = userRepository.findById(id) //
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User " + id + " does not exist"));

        return assembler.toModel(user);
    }
}
