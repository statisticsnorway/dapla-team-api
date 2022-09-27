package no.ssb.dapla.team;

import io.swagger.v3.oas.annotations.Operation;
import no.ssb.dapla.team.teams.TeamController;
import no.ssb.dapla.team.users.UserController;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class RootController {

    @Operation(summary = "Get root options")
    @GetMapping("/")
    ResponseEntity<RepresentationModel> root() {

        RepresentationModel model = new RepresentationModel();

        model.add(linkTo(methodOn(RootController.class).root()).withSelfRel());
        model.add(linkTo(methodOn(TeamController.class).list()).withRel("teams"));
        model.add(linkTo(methodOn(UserController.class).list()).withRel("users"));

        return ResponseEntity.ok(model);
    }
}
