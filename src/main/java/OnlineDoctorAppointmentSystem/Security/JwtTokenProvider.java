package OnlineDoctorAppointmentSystem.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;


@Component
public class JwtTokenProvider {
    @Value("${app.jwt-secret}")
    private String jwtSecret;
    @Value("${app.jwt-expiration-milliseconds}")
    private long jwtExpirationDate;

    //Generate JWT token
    public String generateJwtToken(Authentication authentication)
    {
        String username= authentication.getName();
        Date currentDate=new Date();
        Date expirationDate= new Date(currentDate.getTime()+jwtExpirationDate);
        String jwtToken = Jwts.builder()
            .setSubject(username)
            .setIssuedAt(new Date())
            .setExpiration(expirationDate)
            .signWith(key())
            .compact();
        return jwtToken;

    }

    private Key key()
    {

        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }


    //Get Username from JWT token
    public String getUserName(String token)
    {
        Claims claims = Jwts.parser()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();

        String username = claims.getSubject();
        return username;
    }

    // Validate JWT Token
    public boolean validateToken(String token){
        Jwts.parser()
                .setSigningKey(key())
                .build()
                .parse(token);
        return true;
    }


}
