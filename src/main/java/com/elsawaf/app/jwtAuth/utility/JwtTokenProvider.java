package com.elsawaf.app.jwtAuth.utility;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.elsawaf.app.jwtAuth.constant.SecurityConstant;
import com.elsawaf.app.jwtAuth.domain.UserPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
@Component
@NoArgsConstructor
public class JwtTokenProvider {
//    @Value("${jwt.secret}")
    private static final String secret = "exVf714kMdl%R1j$HxrdK5r^Rl";

    public String generateJwtToken(UserPrincipal userPrincipal){
String[] claims = getClaimsFromUser(userPrincipal);
return JWT.create().withIssuer(SecurityConstant.ELSAWAF_COMPANY)
        .withAudience(SecurityConstant.ELSAWAF_ADMINISTRATION)
        .withIssuedAt(new Date()).withSubject(userPrincipal.getUsername())
        .withArrayClaim(SecurityConstant.AUTHORITIES , claims)
        .withExpiresAt(new Date(System.currentTimeMillis()+SecurityConstant.EXPIRATION_TIME))
        .sign(Algorithm.HMAC512(secret.getBytes()));
    }

    public List<GrantedAuthority> getAuthority(String token){
String[] claims = getClaimsFromToken(token);
return Arrays.stream(claims).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
public Authentication getAuthentication(String userName, List<GrantedAuthority> authorities , HttpServletRequest request){
    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =new UsernamePasswordAuthenticationToken(userName,null,authorities);
    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    return usernamePasswordAuthenticationToken;

}
public String getSubject(String token){
        JWTVerifier verifier = getJWTVerifier();
       return verifier.verify(token).getSubject();
}
public boolean isTokenValid(String token , String userName){
        JWTVerifier verifier = getJWTVerifier();
        return StringUtils.isNoneEmpty(userName) && !isTokenExpired(verifier,token);
}
private boolean isTokenExpired(JWTVerifier verifier , String token){
        Date expiration = verifier.verify(token).getExpiresAt();
        return expiration.before(new Date());
}
    private String[] getClaimsFromToken(String token) {
        JWTVerifier jwtVerifier = getJWTVerifier();
        return jwtVerifier.verify(token).getClaim(SecurityConstant.AUTHORITIES).asArray(String.class);
    }

    private JWTVerifier getJWTVerifier() {
        JWTVerifier verifier;
        try{
            verifier = JWT.require(Algorithm.HMAC512(secret.getBytes()))
                    .withIssuer(SecurityConstant.ELSAWAF_COMPANY)
                    .build();
        }
        catch (JWTVerificationException exception){
            throw new JWTVerificationException(SecurityConstant.TOKEN_CANNOT_BE_VERIFIED);
        }
        return verifier ;
    }

    private String[] getClaimsFromUser(UserPrincipal userPrincipal){
        List<String> authorities = new ArrayList<>();
        for (GrantedAuthority grantedAuthority : userPrincipal.getAuthorities()){
authorities.add(grantedAuthority.getAuthority());
        }
        return authorities.toArray(new String[0]);
    }
}
