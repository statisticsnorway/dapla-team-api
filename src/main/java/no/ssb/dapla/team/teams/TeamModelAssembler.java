package no.ssb.dapla.team.teams;

import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TeamModelAssembler implements RepresentationModelAssembler<Team, TeamModel> {
    @Override
    public TeamModel toModel(Team team) {
        TeamModel model = new TeamModel(team);
        model.add(linkTo(methodOn(TeamController.class).getById(team.getUniformTeamName())).withSelfRel(),
                linkTo(methodOn(TeamController.class).listGroupsOfSpecificTeam(team.getUniformTeamName())).withRel("groups"),
                linkTo(methodOn(TeamController.class).getUsersInTeam(team.getUniformTeamName())).withRel("users"));
        return model;
    }
}
