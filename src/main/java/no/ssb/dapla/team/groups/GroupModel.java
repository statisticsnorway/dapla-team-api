package no.ssb.dapla.team.groups;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Getter
@Setter
@Relation(collectionRelation = "groupList")
public class GroupModel extends RepresentationModel<GroupModel> {

    private String id;

    private String azureId;

    private String name;

    public GroupModel(@NotNull Group group) {
        this.id = group.getId();
        this.azureId = group.getAzureAdId();
        this.name = group.getName();
    }

}
