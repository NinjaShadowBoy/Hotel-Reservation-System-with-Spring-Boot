package cm.sji.hotel_reservation.config;

import java.util.Map;
import java.util.function.Function;
import java.util.Date;
import java.util.HashMap;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

@Service
public class JwtService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final String SECRET_KEY = "wryteu234ty32tuytr68t6wrYTQWYTR6772367GR23TGGY/wf/34+98";

    // extract the username from the token
    public String extractUsername(String token) {
        try {
            logger.debug("Extracting username from token");
            String username = extractClaim(token, Claims::getSubject);
            logger.debug("Username extracted from token: {}", username);
            return username;
        } catch (Exception e) {
            logger.error("Error extracting username from token: {} - {}", 
                    e.getClass().getSimpleName(), e.getMessage());
            throw e;
        }
    }

    // extract a claim from the token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        try {
            logger.debug("Extracting claim from token");
            final Claims claims = extractAllClaims(token);
            T result = claimsResolver.apply(claims);
            logger.debug("Claim successfully extracted from token");
            return result;
        } catch (Exception e) {
            logger.error("Error extracting claim from token: {} - {}", 
                    e.getClass().getSimpleName(), e.getMessage());
            throw e;
        }
    }

    // extract all claims from the token
    private Claims extractAllClaims(String token) {
        try {
            logger.debug("Extracting all claims from token");
            Claims claims = Jwts.parser()
                    .verifyWith(getSignInKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            logger.debug("Successfully extracted all claims from token");
            return claims;
        } catch (ExpiredJwtException e) {
            logger.warn("Token has expired: {}", e.getMessage());
            throw e;
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token: {}", e.getMessage());
            throw e;
        } catch (MalformedJwtException e) {
            logger.error("Malformed JWT token: {}", e.getMessage());
            throw e;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error extracting claims from token: {} - {}", 
                    e.getClass().getSimpleName(), e.getMessage());
            throw e;
        }
    }

    // get the signing key
    private SecretKey getSignInKey() {
        try {
            logger.debug("Getting signing key");
            byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
            SecretKey key = Keys.hmacShaKeyFor(keyBytes);
            logger.debug("Successfully retrieved signing key");
            return key;
        } catch (Exception e) {
            logger.error("Error getting signing key: {} - {}", 
                    e.getClass().getSimpleName(), e.getMessage());
            throw e;
        }
    }

    // Generate a token. The token needs only the username and the role.
    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails) {
        try {
            logger.info("Generating JWT token for user: {}", userDetails.getUsername());

            String token = Jwts.builder()
                    .claims(extraClaims)
                    .subject(userDetails.getUsername())
                    .issuedAt(new Date(System.currentTimeMillis()))
                    .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                    .signWith(getSignInKey())
                    .compact();

            logger.info("JWT token generated successfully for user: {}", userDetails.getUsername());
            return token;
        } catch (Exception e) {
            logger.error("Error generating JWT token for user {}: {} - {}", 
                    userDetails.getUsername(), e.getClass().getSimpleName(), e.getMessage());
            throw e;
        }
    }

    // generate a token with no extra claims
    public String generateToken(UserDetails userDetails) {
        try {
            logger.debug("Generating JWT token with no extra claims for user: {}", userDetails.getUsername());
            return generateToken(new HashMap<>(), userDetails);
        } catch (Exception e) {
            logger.error("Error generating JWT token with no extra claims for user {}: {} - {}", 
                    userDetails.getUsername(), e.getClass().getSimpleName(), e.getMessage());
            throw e;
        }
    }

    // validate a token
    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            logger.debug("Validating token for user: {}", userDetails.getUsername());
            final String username = extractUsername(token);
            boolean isValid = (username.equals(userDetails.getUsername())) && !isTokenExpired(token);

            if (isValid) {
                logger.debug("Token is valid for user: {}", userDetails.getUsername());
            } else {
                logger.warn("Token validation failed for user: {}. Token username: {}, Token expired: {}", 
                        userDetails.getUsername(), username, isTokenExpired(token));
            }

            return isValid;
        } catch (ExpiredJwtException e) {
            logger.warn("Token validation failed - token has expired: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            logger.error("Error validating token for user {}: {} - {}", 
                    userDetails.getUsername(), e.getClass().getSimpleName(), e.getMessage());
            return false;
        }
    }

    // check if a token is expired
    private boolean isTokenExpired(String token) {
        try {
            logger.debug("Checking if token is expired");
            Date expirationDate = extractExpiration(token);
            boolean isExpired = expirationDate.before(new Date());

            if (isExpired) {
                logger.debug("Token is expired. Expiration date: {}", expirationDate);
            } else {
                logger.debug("Token is not expired. Expiration date: {}", expirationDate);
            }

            return isExpired;
        } catch (ExpiredJwtException e) {
            logger.debug("Token is already expired according to exception: {}", e.getMessage());
            return true;
        } catch (Exception e) {
            logger.error("Error checking if token is expired: {} - {}", 
                    e.getClass().getSimpleName(), e.getMessage());
            throw e;
        }
    }

    // extract the expiration date from a token
    private Date extractExpiration(String token) {
        try {
            logger.debug("Extracting expiration date from token");
            Date expirationDate = extractClaim(token, Claims::getExpiration);
            logger.debug("Expiration date extracted from token: {}", expirationDate);
            return expirationDate;
        } catch (Exception e) {
            logger.error("Error extracting expiration date from token: {} - {}", 
                    e.getClass().getSimpleName(), e.getMessage());
            throw e;
        }
    }

}
