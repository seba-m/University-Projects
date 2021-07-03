package com.pruebas.modelo.configuraciones;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.pruebas.modelo.tokens.JWTAuthorizationFilter;
import com.pruebas.modelo.usuario.UserService;

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Value("${aws.access}")
	private String accessKey;
	@Value("${aws.secret}")
	private String secretKey;
	@Value("${aws.region}")
	private String region;

	@Autowired
	private UserService userService;

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public JWTAuthorizationFilter jtwAuthorizationFilter() {
		return new JWTAuthorizationFilter();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
		auth.setUserDetailsService(userService);
		auth.setPasswordEncoder(passwordEncoder());
		return auth;
	}

	@Bean
	public BasicAWSCredentials basicAWSCredentials() {
		return new BasicAWSCredentials(accessKey, secretKey);
	}

	@Bean
	public AmazonS3 s3(AWSCredentials awsCredentials) {
		AmazonS3ClientBuilder builder = AmazonS3ClientBuilder.standard();
		builder.withCredentials(new AWSStaticCredentialsProvider(awsCredentials));
		builder.setRegion(region);
		return builder.build();
	}

	/*-
	@Bean
	public AmazonS3 s3() {
		AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
		return AmazonS3ClientBuilder.standard().withRegion(region)
				.withCredentials(new AWSStaticCredentialsProvider(awsCredentials)).build();
	
	}
	 */

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
				// .addFilterAfter(new JWTAuthorizationFilter(),
				// UsernamePasswordAuthenticationFilter.class)
				.authorizeRequests().antMatchers(HttpMethod.POST, "/api/v1/signup").permitAll()
				.antMatchers(HttpMethod.POST, "/api/v1/login").permitAll()
				.antMatchers(HttpMethod.POST, "/api/v1/forgot").permitAll()
				.antMatchers(HttpMethod.POST, "/api/v1/productos/**", "/api/v1/tags/**").authenticated()
				.antMatchers(HttpMethod.GET, "/api/v1/productos/**", "/api/v1/tags/**").authenticated()
				.antMatchers(HttpMethod.PUT, "/api/v1/productos/**", "/api/v1/tags/**").authenticated()
				.antMatchers(HttpMethod.DELETE, "/api/v1/productos/**", "/api/v1/tags/**").authenticated()
				.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')").antMatchers("/app/**")
				.access("hasRole('ROLE_USER')").antMatchers("/**").permitAll().anyRequest().authenticated().and()
				.formLogin().loginPage("/login").permitAll().defaultSuccessUrl("/", true)
				.successHandler(new AuthenticationSuccessHandler() {
					@Override
					public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
							Authentication authentication) throws IOException, ServletException {
						if (authentication.isAuthenticated()
								&& authentication.getAuthorities().toString().contains("USER")) {
							response.sendRedirect("/app");
						} else if (authentication.isAuthenticated()
								&& authentication.getAuthorities().toString().contains("ADMIN")) {
							response.sendRedirect("/admin");
						} /*- else if (authentication.isAuthenticated() && authentication.getAuthorities().toString().contains("CANCELED")) {
	            			response.sendRedirect("/login?banned");
	            		}*/
					}
				}).and().logout().invalidateHttpSession(true).clearAuthentication(true)
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login?logout")
				.permitAll();
		/*-
		.and()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);*/
		http.addFilterBefore(jtwAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
	}
}