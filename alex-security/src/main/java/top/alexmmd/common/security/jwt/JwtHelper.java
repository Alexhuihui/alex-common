package top.alexmmd.common.security.jwt;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.util.Date;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wangyonghui
 * @since 2022年12月28日 17:12:00
 */
@Slf4j
public class JwtHelper {

    private static final RSAKey RSA_JWK;

    private static final RSAKey RSA_PUBLIC_JWK;

    private static final JWSSigner SIGNER;
    public static final String USER_INFO = "userInfo";

    static {
        try {
            RSA_JWK = new RSAKeyGenerator(2048)
                    .keyID(IdUtil.randomUUID())
                    .generate();
            RSA_PUBLIC_JWK = RSA_JWK.toPublicJWK();
            SIGNER = new RSASSASigner(RSA_JWK);
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    public static String encode(String userInfo) {
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject("alex")
                .issuer("https://alexmmd.top")
                .expirationTime(DateUtil.offsetHour(DateUtil.date(), 2))
                .jwtID(IdUtil.fastSimpleUUID())
                .claim(USER_INFO, userInfo)
                .build();

        SignedJWT signedJWT = new SignedJWT(
                new JWSHeader.Builder(JWSAlgorithm.RS256).keyID(RSA_JWK.getKeyID()).build(),
                claimsSet);

        signedJWT.sign(SIGNER);

        return signedJWT.serialize();
    }

    @SneakyThrows
    public static String decode(String token) {
        SignedJWT signedJWT = SignedJWT.parse(token);

        JWSVerifier verifier = new RSASSAVerifier(RSA_PUBLIC_JWK);
        Assert.isTrue(signedJWT.verify(verifier));

        Assert.equals("alex", signedJWT.getJWTClaimsSet().getSubject());
        Assert.equals("https://alexmmd.top", signedJWT.getJWTClaimsSet().getIssuer());
        Assert.isTrue(new Date().before(signedJWT.getJWTClaimsSet().getExpirationTime()));
        return signedJWT.getJWTClaimsSet().getStringClaim(USER_INFO);
    }

}
