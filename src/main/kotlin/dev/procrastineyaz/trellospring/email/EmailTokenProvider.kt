package dev.procrastineyaz.trellospring.email

import dev.procrastineyaz.trellospring.models.User
import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtParser
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*
import java.util.Base64.getEncoder
import javax.annotation.PostConstruct
import javax.crypto.spec.SecretKeySpec


@Component
class EmailTokenProvider {
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

    fun createToken(user: User): String {
        val now: Long = Date().time
        val validity = Date(now + tokenValidityInMilliseconds)
        return Jwts.builder()
            .setSubject(user.email)
            .claim("username", user.username)
            .signWith(key)
            .setExpiration(validity)
            .compact()
    }

    fun getTokenClaims(token: String): Claims {
        if (token.isEmpty()) {
            throw BadCredentialsException("Invalid token")
        }
        return try {
            parser
                .parseClaimsJws(token)
                .body
        } catch (e: Exception) {
            println("Bad Access Token: ${e.message}")
            throw AuthenticationCredentialsNotFoundException("bad token")
        }
//        val authorities: Collection<GrantedAuthority> = Arrays.stream(claims[AUTHORITIES_KEY].toString().split(",").toTypedArray())
//            .map { role: String? -> SimpleGrantedAuthority(role) }
//            .collect(Collectors.toList())
//        // val principal = User(claims.subject, "", authorities)
//        val principal = UserDetailsImpl(claims.subject, claims[AUTHORITIES_KEY].toString(), claims["username"].toString())
//        return UsernamePasswordAuthenticationToken(principal, token, authorities)
    }

    private fun String.hexToByteArray(): ByteArray {
        val data = ByteArray(this.length / 2)
        for(i in this.indices step 2) {
            data[i / 2] = ((Character.digit(this[i], 16) shl 4) + Character.digit(this[i+1], 16)).toByte()
        }
        return data
    }

    companion object {
        private const val SALT_KEY = "JpxM4e858rc673syopdZnMFb*ExeqJtUc0HJ_iOxu~jiSYu+yPdPw93OBBjF"
        private const val TOKEN_VALIDITY = 86400 // Value in second
    }
}