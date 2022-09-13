package no.ssb.dapla.team.users;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.*;
import no.ssb.dapla.team.groups.Group;
import org.springframework.jmx.export.annotation.ManagedNotification;

import java.util.Set;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="users")
public class User {
	@Id
	@NonNull
	private String emailShort;
	private String name;
	private String email;

	@ManyToMany
	private Set<Group> groups;

}
