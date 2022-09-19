package no.ssb.dapla.team;

import no.ssb.dapla.team.teams.TeamController;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class RootController {

    @GetMapping("/")
    ResponseEntity<RepresentationModel> root() {

        RepresentationModel model = new RepresentationModel();

        model.add(linkTo(methodOn(RootController.class).root()).withSelfRel());
        model.add(linkTo(methodOn(TeamController.class).list()).withRel("teams"));
        model.add(linkTo(methodOn(TeamController.class).list()).withRel("users"));

        return ResponseEntity.ok(model);
    }
}
