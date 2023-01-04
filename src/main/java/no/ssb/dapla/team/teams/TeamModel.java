package no.ssb.dapla.team.teams;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Getter
@Setter
@Relation(collectionRelation = "teamList")
public class TeamModel extends RepresentationModel<TeamModel> {

    private String uniformTeamName;
    private String displayTeamName;
    private String repo;

    public TeamModel(@NotNull Team team) {
        this.uniformTeamName = team.getUniformTeamName();
        this.displayTeamName = team.getDisplayTeamName();
        this.repo = team.getRepo();
    }



}
