package no.ssb.dapla.team.github;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Date;

public class GitHubUtils {

    protected static PrivateKey getPrivateKey(String filename) throws Exception {
        byte[] keyBytes = Files.readAllBytes(Paths.get(filename));

        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    protected static String createJWT(String githubAppId, String path, long jwtExpirationTimeInMS) throws Exception {
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
