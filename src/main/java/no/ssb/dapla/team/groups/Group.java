package no.ssb.dapla.team.groups;

import jakarta.persistence.*;
import lombok.*;
import no.ssb.dapla.team.teams.Team;
import no.ssb.dapla.team.users.User;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="groups")
public class Group {
    @Id
    @NonNull
    private String id;

    @OneToOne
    private Team team;

    @OneToMany
    private Set<User> users;
}
