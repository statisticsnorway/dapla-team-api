package no.ssb.dapla.team.teams;

import jakarta.persistence.*;
import lombok.*;
import no.ssb.dapla.team.groups.Group;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import java.util.Set;

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

    @OneToMany(cascade = CascadeType.ALL)
    @NotFound(action = NotFoundAction.IGNORE)
    private Set<Group> Groups;

}
