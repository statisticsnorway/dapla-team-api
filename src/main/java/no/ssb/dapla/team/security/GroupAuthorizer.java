package no.ssb.dapla.team.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.ssb.dapla.team.groups.GroupRepository;
import no.ssb.dapla.team.users.User;
import no.ssb.dapla.team.util.GroupNameUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;

import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class GroupAuthorizer {

    private static final String PREFERRED_USERNAME = "preferred_username";

    private final GroupRepository groupRepository;

    /**
     * For a given team name, check if the authenticated user is member of the managers group.
     *
     * @param authentication jwt
     * @param teamName e.g. demo-enhjoern-a
     * @return true iff the user is member of the team's managers group
     */
    public boolean isTeamManagerForTeam(Authentication authentication, String teamName) {
        String groupName = GroupNameUtil.managersGroupNameOf(teamName);
        return isMemberOfGroup(authentication, groupName);
    }

    /**
     * For a given group, check if the authenticated user is member of the associated team's managers group.
     *
     * E.g. Given groupName=demo-enhjoern-a-developers, return true only if the user is member of
     * demo-enhjoern-a-managers.
     *
     * This can be used to make sure that only managers are authorized to perform certain actions,
     * such as add new team members, etc.
     *
     * @param authentication jwt token
     * @param groupName e.g. demo-enhjoern-a-developers
     * @return true iff the user is member of the managers group associated with the group in question
     */
    public boolean isTeamManagerForAssociatedGroup(Authentication authentication, String groupName) {
        String teamName = GroupNameUtil.deduceTeamNameFromGroup(groupName);
        return isTeamManagerForTeam(authentication, teamName);
    }

    /**
     * Check if the authenticated user is member of a given group
     *
     * @param authentication
     * @param groupName
     * @return
     */
    public boolean isMemberOfGroup(Authentication authentication, String groupName) {
        boolean accessOk = false;
        try {
            String userId = userIdOf(authentication);
            log.debug("Check if user %s is member of group %s".formatted(userId, groupName));
            Set<String> members = groupRepository.getReferenceById(groupName)
                    .getUsers().stream()
                    .map(User::getEmailShort)
                    .collect(Collectors.toSet());
            accessOk = members.contains(userId);
            log.debug("%s members: %s".formatted(groupName, members));
        } catch (Exception e) {
            log.debug("Failed to check if principal is member of group", e);
        }

        log.debug("isMemberOfGroup %s: %s".formatted(groupName, accessOk));
        return accessOk;
    }

    /**
     * Return the value of a named claim (e.g. userId) from the authentication context
     *
     * @param authentication jwt
     * @param claim claim name
     * @return Optional value of the claim
     * @throws IllegalStateException if the authentication object is not a JWT
     */
    private static Optional<Object> claimOf(Authentication authentication, String claim) {
        if (authentication instanceof JwtAuthenticationToken) {
            Jwt jwt = (Jwt) authentication.getPrincipal();
            return Optional.ofNullable(jwt.getClaim(claim));
        }
        else {
            throw new IllegalStateException("Expected authentication object to be a JWT, but was " + authentication.getClass());
        }
    }

    private static String userIdOf(Authentication authentication) {
        String claim = PREFERRED_USERNAME;
        return (String) claimOf(authentication, claim)
                .orElseThrow((() -> new IllegalStateException("Unable to find '%s' claim in keycloak token".formatted(claim))));
    }

}
