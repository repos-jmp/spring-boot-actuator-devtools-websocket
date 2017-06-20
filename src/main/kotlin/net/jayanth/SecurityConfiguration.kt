package net.jayanth

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

/**
 * Created by jmp on 6/20/2017.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfiguration: WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity?) {

        http!!.authorizeRequests()
                .antMatchers(HttpMethod.GET,"/","/images/**","/main.css","/webjars/**").permitAll()
                .antMatchers(HttpMethod.POST,"/images").hasRole("USER")
                .antMatchers("/imageMessages/**").permitAll()
             .and()
                .formLogin()
                .permitAll()
             .and()
                .logout()
                .logoutSuccessUrl("/")
    }

    @Autowired
    fun configureJpaBasedUsers(auth: AuthenticationManagerBuilder, userDetailsService: SpringDataUserDetialsService){
        auth.userDetailsService(userDetailsService)
    }
}