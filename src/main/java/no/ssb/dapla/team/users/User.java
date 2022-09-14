package no.ssb.dapla.team.users;

import jakarta.persistence.*;
import lombok.*;
import no.ssb.dapla.team.groups.Group;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="SSBUser")
public class User {
	@Id
	@NonNull
	private String emailShort;
	private String name;
	private String email;

}
