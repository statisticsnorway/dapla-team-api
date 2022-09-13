package no.ssb.dapla.team.teams;

import jakarta.persistence.*;
import lombok.*;
import no.ssb.dapla.team.groups.Group;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="teams")
public class Team {
    @Id
    @NonNull
    private String uniformTeamName;
    private String displayTeamName;

    @OneToMany
    Set<Group> groups;
}
