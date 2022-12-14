package no.ssb.dapla.team.github;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import lombok.Data;
import lombok.NonNull;
import no.ssb.dapla.team.teams.Team;
import org.apache.commons.lang3.StringUtils;
import org.kohsuke.github.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;

@Data
@Service
public class GitHubService {

    private static final long JWT_EXPIRATION_TIME_IN_MS = 600000;

    private final String appId;

    private final String privateKeyPath;

    private final String organizationName;

    private GHOrganization ghOrganization;
    private GHAppInstallationToken ghAppInstallationToken;

    /**
     * Loads GitHub properties and creates the GitHub application, if the application is installed
     * in the Organization it will create an application installation token that can be used to access the
     * GitHub API.
     *
     * @param appId string GitHubApplication ID, loads github.app.id
     * @param privateKeyPath string GitHub organization name, loads github.organization
     * @param organizationName string Path to github application private key, loads github.app.privatekey.file
     */
    public GitHubService(@NonNull @Value("${github.app.id}") String appId,
                         @NonNull @Value("${github.app.privatekey.file}") String privateKeyPath,
                         @NonNull @Value("${github.organization}") String organizationName) throws Exception {

        this.organizationName = organizationName;
        this.appId = appId;
        this.privateKeyPath = privateKeyPath;

        String jwtToken = GitHubUtils.createJWT(appId, privateKeyPath, JWT_EXPIRATION_TIME_IN_MS);
        GitHub gitHubApp = new GitHubBuilder().withJwtToken(jwtToken).build();

        GHAppInstallation appInstallation = gitHubApp.getApp().getInstallationByOrganization(organizationName);
        ghAppInstallationToken = appInstallation.createToken().create();
        GitHub githubAuthAsInst = new GitHubBuilder().withAppInstallationToken(ghAppInstallationToken.getToken()).build();
        ghOrganization = githubAuthAsInst.getOrganization(organizationName);
    }

    /**
     * Updates the ghAppInstallationToken if it has expired
     */
    protected void updateTokenIfExpired() {
        try {
            if (ghAppInstallationToken.getExpiresAt().before(new Date())) {

                String jwtToken = GitHubUtils.createJWT(appId, privateKeyPath, JWT_EXPIRATION_TIME_IN_MS);
                GitHub gitHubApp = new GitHubBuilder().withJwtToken(jwtToken).build();


                GHAppInstallation appInstallation = gitHubApp.getApp().getInstallationByOrganization(organizationName);
                ghAppInstallationToken = appInstallation.createToken().create();
                GitHub githubAuthAsInst = new GitHubBuilder().withAppInstallationToken(ghAppInstallationToken.getToken()).build();
                ghOrganization = githubAuthAsInst.getOrganization(organizationName);
            }

        } catch (Exception e) {
            throw new GitHubServiceException("Could not refresh token");
        }
    }

    /**
     * Request a list of all GitHub repositories in the organization with a given topic
     *
     * @param topic GitHub repository topic
     * @return response as string
     */
    public String getRepositoryInOrganizationWithTopicAsJsonString(String topic) throws IOException {
        updateTokenIfExpired();
        String accessToken = ghAppInstallationToken.getToken();
        URL url = new URL("https://api.github.com/search/repositories?per_page=100&q=org:statisticsnorway+topic:" + topic);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "bearer " + accessToken);
        conn.setRequestProperty("Accept", "application/json");

        int httpResponseCode = conn.getResponseCode();
        if (httpResponseCode == HTTPResponse.SC_OK) {

            StringBuilder response;
            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()))) {

                String inputLine;
                response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
            }
            return response.toString();
        } else {
            return String.format("Connection returned HTTP code: %s with message: %s",
                    httpResponseCode, conn.getResponseMessage());
        }
    }

    /**
     * Deserializes JSON from getRepositoryInOrganizationWithTopicAsJsonString
     * and make a list of all GitHub repositories in the organization with a given topic
     *
     * @param topic GitHub repository topic
     * @return list of teams with given topic
     */
    public List<Team> getTeamListWithTopic(String topic) throws Exception {
        GithubSearchResult githubSearchResult = new ObjectMapper().readValue(getRepositoryInOrganizationWithTopicAsJsonString(topic), GithubSearchResult.class);

        return githubSearchResult
                .getItems()
                .stream()
                .map(adTeam -> new Team(adTeam.getRepoName().replace("-iac", ""),
                        StringUtils.capitalize(adTeam.getRepoName()
                                .replace("-iac", "")
                                .replace("-", " ")),
                        adTeam.getFullRepoName()))
                .toList();
    }

    /*TODO: read a repositories terraform.tfvars file, waiting for contents read-only privliges
       https://ssb-norge.slack.com/archives/C014MLD3US3/p1663663159673499 */
    public void readTfVars(String repoName) throws IOException {
        GHRepository ghRepository = ghOrganization.getRepository(repoName);
        GHContent ghContent = ghRepository.getFileContent("terraform.tfvars");
        StringBuilder fileData;

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(ghContent.read()));
        String line;
        fileData = new StringBuilder();

        while ((line = bufferedReader.readLine()) != null) {
            fileData.append(line);
        }
    }

    /**
     * Compiles a list of all GitHub repositories in the organization that contains
     *
     * @param contains GitHub repository topic
     * @return list of teams with repository name containing name
     */
    public List<GHRepository> getRepositoryInOrganizationWithNameContaining(String contains) {
        updateTokenIfExpired();
        List<GHRepository> repositoryList;
        try {

            repositoryList = ghOrganization.getRepositories()
                    .values()
                    .stream()
                    .filter(ghRepository -> ghRepository.getName().contains(contains))
                    .toList();


        } catch (IOException e) {
            throw new GitHubServiceException("Failed to get organizations repositories containing: " + contains);
        }
        return repositoryList;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class AdTeam {
        @JsonProperty("name")
        private String repoName;
        @JsonProperty("full_name")
        private String fullRepoName;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class GithubSearchResult {
        @JsonProperty("incomplete_results")
        private boolean incompleteResults;
        @JsonProperty("items")
        private List<AdTeam> items;
    }

}
