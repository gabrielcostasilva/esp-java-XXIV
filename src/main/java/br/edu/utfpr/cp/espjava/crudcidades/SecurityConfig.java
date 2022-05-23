package br.edu.utfpr.cp.espjava.crudcidades;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()
            .antMatchers("/").hasAnyAuthority("listar", "admin")
            .antMatchers("/criar").hasAuthority("admin")
            .antMatchers("/excluir").hasAuthority("admin")
            .antMatchers("/preparaAlterar").hasAuthority("admin")
            .antMatchers("/alterar").hasAuthority("admin")
            .antMatchers("/mostrar").authenticated()
            .anyRequest().denyAll()
                .and()
            .formLogin()
            .loginPage("/login.html").permitAll()
            .defaultSuccessUrl("/", false)
                .and()
            .logout().permitAll();
    }

    @Bean
    public PasswordEncoder cifrador() {
        return new BCryptPasswordEncoder();
    }

    @EventListener(InteractiveAuthenticationSuccessEvent.class)
    public void printUsuarioAtual(InteractiveAuthenticationSuccessEvent event) {

        var usuario = event.getAuthentication().getName();
        
        System.out.println(usuario);
    }
}