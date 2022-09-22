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
}
