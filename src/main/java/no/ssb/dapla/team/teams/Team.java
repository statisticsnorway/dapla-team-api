package no.ssb.dapla.team.teams;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;
import no.ssb.dapla.team.groups.Group;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Team {
    @Id
    //@NonNull
    private String uniformTeamName;
    private String displayTeamName;

    @OneToMany
    private Set<Group> Groups;

}
