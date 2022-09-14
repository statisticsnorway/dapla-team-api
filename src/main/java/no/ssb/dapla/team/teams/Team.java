package no.ssb.dapla.team.teams;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;
import no.ssb.dapla.team.groups.Group;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
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
    private String uniformTeamName;
    private String displayTeamName;

    @OneToMany
    @Cascade(CascadeType.ALL)
    @NotFound(action = NotFoundAction.IGNORE)
    private Set<Group> Groups;

}
