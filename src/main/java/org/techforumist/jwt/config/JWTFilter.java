package org.techforumist.jwt.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.filter.GenericFilterBean;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;

/**
 * A generic filter for security. I will check token present in the header.
 * 
 * @author Sarath Muraleedharan
 *
 */
public class JWTFilter extends GenericFilterBean {
	private static final String AUTHORIZATION_HEADER = "Authorization";
	private static final String AUTHORITIES_KEY = "roles";

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		String authHeader = request.getHeader(AUTHORIZATION_HEADER);
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			((HttpServletResponse) res).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Authorization header.");
		} else {
			try {
				String token = authHeader.substring(7);
				Claims claims = Jwts.parser().setSigningKey("secretkey").parseClaimsJws(token).getBody();
				request.setAttribute("claims", claims);
				SecurityContextHolder.getContext().setAuthentication(getAuthentication(claims));
				filterChain.doFilter(req, res);
			} catch (SignatureException e) {
				((HttpServletResponse) res).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
			}

		}
	}

	/**
	 * Method for creating Authentication for Spring Security Context Holder
	 * from JWT claims
	 * 
	 * @param claims
	 * @return
	 */
	public Authentication getAuthentication(Claims claims) {
		List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		List<String> roles = (List<String>) claims.get(AUTHORITIES_KEY);
		for (String role : roles) {
			authorities.add(new SimpleGrantedAuthority(role));
		}
		User principal = new User(claims.getSubject(), "", authorities);
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
				principal, "", authorities);
		return usernamePasswordAuthenticationToken;
	}
}
