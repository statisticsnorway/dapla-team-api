package no.ssb.dapla.team.teams;

import jakarta.persistence.*;
import lombok.*;
import no.ssb.dapla.team.groups.Group;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Team {
    @Id
    @NonNull
    @Column(unique = true)
    private String uniformTeamName;
    private String displayTeamName;
    private String repo;

    @OneToMany(cascade = CascadeType.MERGE)
    private List<Group> groups;

    public Team(String uniformTeamName, String displayTeamName, String repo){
        this.repo = repo;
        this.displayTeamName = displayTeamName;
        this.uniformTeamName = uniformTeamName;

        List<String> defaultTeamNames = List.of("-support","-developers",
                "-data-admins", "-managers","-consumers");

        groups = defaultTeamNames.stream().map(group -> Group
                .builder().id(uniformTeamName + group).build())
                .toList();
    }

}
