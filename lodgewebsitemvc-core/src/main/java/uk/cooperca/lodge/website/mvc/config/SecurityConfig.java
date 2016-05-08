package uk.cooperca.lodge.website.mvc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static uk.cooperca.lodge.website.mvc.entity.Role.RoleName.ROLE_ADMIN;

/**
 * A configuration class that configures Spring Security.
 *
 * @author Charlie Cooper
 */
@Configuration
@EnableWebSecurity
@ComponentScan(basePackages = {"uk.cooperca.lodge.website.mvc.service.security"})
@Import(DataConfig.class)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                .loginPage("/login")
                .failureUrl("/login-error")
            .and()
                .authorizeRequests()
                .antMatchers("/account/**")
                .authenticated()
            .and()
                .authorizeRequests()
                .antMatchers("/admin/**")
                .hasAuthority(ROLE_ADMIN.name())
            .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
            // TODO: if not authorised then this is the accessDenied page
            .and()
                .exceptionHandling()
                .accessDeniedPage("/accessDenied");
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources**");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(userDetailsService);
        return authenticationProvider;
    }
}
