package dev.procrastineyaz.trellospring.security.jwt

import io.jsonwebtoken.*
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Component
import java.lang.Exception
import java.security.Key
import java.util.*
import java.util.Base64.getEncoder
import java.util.stream.Collectors
import javax.annotation.PostConstruct
import javax.crypto.spec.SecretKeySpec


@Component
class TokenProvider {
    private val encoder: Base64.Encoder = getEncoder()
    private var secretKey: String? = null
    private var tokenValidityInMilliseconds: Long = 0
    private lateinit var key: Key
    private lateinit var parser: JwtParser

    @PostConstruct
    fun init() {
        secretKey = encoder.encodeToString(SALT_KEY.toByteArray(Charsets.UTF_8))
        key = SecretKeySpec(secretKey?.hexToByteArray(), SignatureAlgorithm.HS256.jcaName)
        parser = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
        tokenValidityInMilliseconds = 1000 * TOKEN_VALIDITY.toLong()
    }

    fun createToken(authentication: Authentication): String {
        val authorities = authentication.authorities.stream()
            .map { obj: GrantedAuthority -> obj.authority }
            .collect(Collectors.joining(","))
        val now: Long = Date().time
        val validity = Date(now + tokenValidityInMilliseconds)
        println("secret: $secretKey")
        return Jwts.builder()
            .setSubject(authentication.name)
            .claim(AUTHORITIES_KEY, authorities)
            .signWith(key)
            .setExpiration(validity)
            .compact()
    }

    fun getAuthentication(token: String): Authentication {
        if (token.isEmpty() || !validateToken(token)) {
            throw BadCredentialsException("Invalid token")
        }
        val claims = try {
            parser
                .parseClaimsJws(token)
                .body
        } catch (e: Exception) {
            println("Bad Access Token: ${e.message}")
            throw AuthenticationCredentialsNotFoundException("bad token")
        }
        val authorities: Collection<GrantedAuthority> = Arrays.stream(claims[AUTHORITIES_KEY].toString().split(",").toTypedArray())
            .map { role: String? -> SimpleGrantedAuthority(role) }
            .collect(Collectors.toList())
        val principal = User(claims.subject, "", authorities)
        return UsernamePasswordAuthenticationToken(principal, token, authorities)
    }

    fun validateToken(authToken: String?): Boolean {
        try {
            parser.parseClaimsJws(authToken)
            return true
        } catch (e: SignatureException) {
            println("Invalid JWT signature.")
        } catch (e: MalformedJwtException) {
            println("Invalid JWT token.")
        } catch (e: UnsupportedJwtException) {
            println("Unsupported JWT token.")
        } catch (e: IllegalArgumentException) {
            println("JWT token compact of handler are invalid.")
        }
        return false
    }

    private fun String.hexToByteArray(): ByteArray {
        val data = ByteArray(this.length / 2)
        for(i in this.indices step 2) {
            data[i / 2] = ((Character.digit(this[i], 16) shl 4) + Character.digit(this[i+1], 16)).toByte()
        }
        println("Secret: $this or $data")
        return data
    }

    companion object {
        private const val SALT_KEY = "JpxM4e858rc673syopdZnMFb*ExeqJtUc0HJ_iOxu~jiSYu+yPdPw93OBBjF"
        private const val TOKEN_VALIDITY = 86400 // Value in second
        private const val AUTHORITIES_KEY = "auth"
    }
}