package org.techforumist.jwt.config;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Web security configuration class
 * 
 * @author Sarath Muraleedharan
 *
 */
@Configurable
@EnableWebSecurity
// Modifying or overriding the default spring boot security.
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	// This method is for overriding some configuration of the WebSecurity
	// If you want to ignore some request or request patterns then you can
	// specify that inside this method
	@Override
	public void configure(WebSecurity web) throws Exception {

		web.ignoring()
				// ignoring the "/", "/index.html", "/app/**", "/register",
				// "/favicon.ico"
				.antMatchers("/", "/index.html", "/app/**", "/register", "/authenticate", "/favicon.ico");
	}

	// This method is used for override HttpSecurity of the web Application.
	// We can specify our authorization criteria inside this method.
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				// starts authorizing configurations
				.authorizeRequests()
				// authenticate all remaining URLS
				.anyRequest().fullyAuthenticated().and()
				// adding JWT filter
				.addFilterBefore(new JWTFilter(), UsernamePasswordAuthenticationFilter.class)
				// enabling the basic authentication
				.httpBasic().and()
				// configuring the session as state less. Which means there is
				// no session in the server
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				// disabling the CSRF - Cross Site Request Forgery
				.csrf().disable();
	}

}
