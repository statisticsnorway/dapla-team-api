package no.ssb.dapla.team.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.ssb.dapla.team.groups.GroupRepository;
import no.ssb.dapla.team.users.User;
import no.ssb.dapla.team.util.GroupNameUtil;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class GroupAuthorizer {

    private final GroupRepository groupRepository;

    /**
     * For a given team name, check if the authenticated user is member of the managers group.
     *
     * @param principal JWT
     * @param teamName  e.g. demo-enhjoern-a
     * @return true iff the user is member of the team's managers group
     */
    public boolean isTeamManagerForTeam(Principal principal, String teamName) {
        log.debug("Check if principal (%s) is manager for team %s".formatted(principal.getName(), teamName));
        String groupName = GroupNameUtil.managersGroupNameOf(teamName);
        return isMemberOfGroup(principal, groupName);
    }

    /**
     * For a given group, check if the authenticated user is member of the associated team's managers group.
     * <p>
     * E.g. Given groupName=demo-enhjoern-a-developers, return true only if the user is member of
     * demo-enhjoern-a-managers.
     * <p>
     * This can be used to make sure that only managers are authorized to perform certain actions,
     * such as add new team members, etc.
     *
     * @param principal JWT
     * @param groupName e.g. demo-enhjoern-a-developers
     * @return true iff the user is member of the managers group associated with the group in question
     */
    public boolean isTeamManagerForAssociatedGroup(Principal principal, String groupName) {
        log.debug("Check if principal (%s) is manager for group %s".formatted(principal.getName(), groupName));
        String teamName = GroupNameUtil.deduceTeamNameFromGroup(groupName);
        return isTeamManagerForTeam(principal, teamName);
    }

    /**
     * Check if the authenticated user is member of a given group.
     *
     * @param principal JWT
     * @param groupName e.g. demo-enhjoern-a-developers
     * @return true iff the user is member of a specific group
     */
    public boolean isMemberOfGroup(Principal principal, String groupName) {
        boolean accessOk = false;
        try {
            String userId = principal.getName();
            log.debug("Check if user %s is member of group %s".formatted(userId, groupName));
            Set<String> members = groupRepository.getReferenceById(groupName)
                    .getUsers().stream()
                    .map(User::getEmailShort)
                    .collect(Collectors.toSet());
            accessOk = members.contains(userId);
            log.debug("%s members: %s".formatted(groupName, members));
        } catch (Exception e) {
            log.warn("Error checking if principal is member of group", e);
        }

        log.debug("isMemberOfGroup: is %s member of group %s: %s".formatted(principal.getName(), groupName, accessOk));
        return accessOk;
    }

}
