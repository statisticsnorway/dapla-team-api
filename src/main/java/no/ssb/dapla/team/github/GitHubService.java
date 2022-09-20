package no.ssb.dapla.team.github;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.NonNull;
import no.ssb.dapla.team.teams.Team;
import org.kohsuke.github.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
public class GitHubService {
    private final long jwtExpirationTimeInMS = 1000;

    private final String appId;

    private final String privateKeyPath;

    private final String organizationName;

    private GHOrganization ghOrganization;
    private GHAppInstallationToken ghAppInstallationToken;


    public GitHubService(@NonNull @Value("${github.id}") String appId,
                         @NonNull @Value("${github.privatekey}") String privateKeyPath,
                         @NonNull @Value("${github.organization}") String organizationName) {

        this.organizationName = organizationName;
        this.appId = appId;
        this.privateKeyPath = privateKeyPath;

        try {
            String jwtToken = this.createJWT(appId, privateKeyPath);
            GitHub gitHubApp = new GitHubBuilder().withJwtToken(jwtToken).build();

            GHAppInstallation appInstallation = gitHubApp.getApp().getInstallationByOrganization(organizationName);
            ghAppInstallationToken = appInstallation.createToken().create();
            GitHub githubAuthAsInst = new GitHubBuilder().withAppInstallationToken(ghAppInstallationToken.getToken()).build();
            ghOrganization = githubAuthAsInst.getOrganization(organizationName);

        } catch (Exception e) {
            new RuntimeException("Could not setup GitHubService");
        }

    }

    private void updateTokenIfExpired() {
        try {
            if (ghAppInstallationToken.getExpiresAt().before(new Date())) {

                String jwtToken = this.createJWT(appId, privateKeyPath); //sdk-github-api-app-test
                GitHub gitHubApp = new GitHubBuilder().withJwtToken(jwtToken).build();


                GHAppInstallation appInstallation = gitHubApp.getApp().getInstallationByOrganization(organizationName);
                ghAppInstallationToken = appInstallation.createToken().create();
                GitHub githubAuthAsInst = new GitHubBuilder().withAppInstallationToken(ghAppInstallationToken.getToken()).build();
                ghOrganization = githubAuthAsInst.getOrganization(organizationName);
            }

        } catch (Exception e) {
            new RuntimeException("Could not refresh token");
        }
    }

    public List<Team> getRepositoryInOrganizationWithTopic(String topic) {
        updateTokenIfExpired();
        LinkedList<Team> repositoryList = new LinkedList<>();
        try {
            for (GHRepository ghRepository : ghOrganization.getRepositories().values()) {
                for (String tempTopic : ghRepository.listTopics()) {
                    if (tempTopic.equals(topic)) {
                        repositoryList.add(Team.builder().
                                uniformTeamName(ghRepository.getName())
                                .displayTeamName(ghRepository.getName().replace("-", " "))
                                .build());
                        break;
                    }

                }
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to get organizations repositories containing: " + topic);
        }
        return repositoryList;
    }

    public List<GHRepository> getRepositoryInOrganizationWithNameContaining(String containing) {
        updateTokenIfExpired();
        List<GHRepository> repositoryList;
        try {

            repositoryList = ghOrganization.getRepositories()
                    .values()
                    .stream()
                    .filter(ghRepository -> ghRepository.getName().contains(containing))
                    .toList();


        } catch (IOException e) {
            throw new RuntimeException("Failed to get organizations repositories containing: " + containing);
        }
        return repositoryList;
    }

    private PrivateKey getPrivateKey(String filename) throws Exception {
        byte[] keyBytes = Files.readAllBytes(Paths.get(filename));

        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    private String createJWT(String githubAppId, String path) throws Exception {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.RS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //Sign JWT with private key
        Key signingKey = getPrivateKey(path);

        //Set the JWT Claims
        JwtBuilder builder = Jwts.builder().setIssuedAt(now).setIssuer(githubAppId).signWith(signingKey, signatureAlgorithm);

        //Add the expiration
        long expMillis = nowMillis + jwtExpirationTimeInMS;
        Date exp = new Date(expMillis);
        builder.setExpiration(exp);

        return builder.compact();
    }

}
