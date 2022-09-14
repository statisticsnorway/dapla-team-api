package no.ssb.dapla.team.teams;

import no.ssb.dapla.team.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;


public interface TeamRepository extends JpaRepository<Team, String> {

}
