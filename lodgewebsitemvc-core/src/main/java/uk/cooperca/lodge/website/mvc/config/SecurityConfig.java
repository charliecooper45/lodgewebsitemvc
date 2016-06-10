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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import uk.cooperca.lodge.website.mvc.config.util.SecurityConfigImportSelector;
import uk.cooperca.lodge.website.mvc.security.CustomAuthenticationSuccessHandler;

import java.util.Locale;

import static uk.cooperca.lodge.website.mvc.entity.Role.RoleName.ROLE_ADMIN;

/**
 * A configuration class that configures Spring Security.
 *
 * @author Charlie Cooper
 */
@Configuration
@EnableWebSecurity
@ComponentScan(basePackages = {"uk.cooperca.lodge.website.mvc.service.security"})
@Import(SecurityConfigImportSelector.class)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                .loginPage("/login")
                .failureUrl("/login-error")
                .successHandler(authenticationSuccessHandler())
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
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(encoder);
        authenticationProvider.setUserDetailsService(userDetailsService);
        return authenticationProvider;
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler(localeResolver());
    }

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver localeResolver = new SessionLocaleResolver();
        localeResolver.setDefaultLocale(new Locale("en"));
        return localeResolver;
    }
}
