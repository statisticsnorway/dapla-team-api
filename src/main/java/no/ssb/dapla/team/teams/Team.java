package no.ssb.dapla.team.teams;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;
import no.ssb.dapla.team.groups.Group;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

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
