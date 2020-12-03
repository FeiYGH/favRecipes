package com.talentpath.favRecipEzSpringBt.security;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    JwtAuthEntryPoint entryPoint;

    //this exposes the parent class Authentication Manager as a bean
    //to user later

    @Bean //whenever someone calls for AuthManager, calls this bean that represents the class
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    //sets up the filter that will decode incoming jwts into
        //the basic username/password authentication tokens

    @Bean
    public AuthTokenFilter jwtFilter(){
        return new AuthTokenFilter();
    }

    //Do we really need this?, not a Bean over top
    //seem pointless
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        super.configure(auth);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
                .cors() //CorsConfigurer <HttpSecurity>, turns off cross origin res sharing checks, rest service shld be avail to everyone
                .and() //HttpSecurity, anytime do .and - give us original httpsecurity object
                    .csrf().disable()
                    .exceptionHandling()
                        .authenticationEntryPoint(entryPoint)
                .and() //httpsecurity
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //should be stateless, don't store who is logged in and out, SessionManagementConfigurer<HttpSecurity>
                .and()
                    .authorizeRequests()
                    .antMatchers(HttpMethod.POST, "/api/auth/signin").permitAll() //don't want to have to be logged in to log in or be logged in to sign up for account
                    .antMatchers(HttpMethod.POST, "/api/auth/register").permitAll()

                    .antMatchers(HttpMethod.GET, "/api/userdata", "/api/userdata/**").hasRole("ADMIN")
                    .antMatchers(HttpMethod.POST, "/api/userdata").hasRole("ADMIN")
                    .antMatchers(HttpMethod.PUT, "/api/userdata").hasRole("ADMIN")
                    .antMatchers(HttpMethod.DELETE, "/api/userdata").hasRole("ADMIN")

                    .antMatchers(HttpMethod.GET, "/api/recipes", "/api/recipes/**").permitAll()
                    .antMatchers(HttpMethod.POST, "/api/recipes").authenticated()
                    .antMatchers(HttpMethod.PUT, "/api/recipes").authenticated()
                    .antMatchers(HttpMethod.DELETE, "/api/recipes/**").authenticated()

                    .antMatchers(HttpMethod.GET, "/api/recipesSearch").permitAll()

                    .antMatchers(HttpMethod.POST, "/api/recipeInstructions").authenticated()
                    .antMatchers(HttpMethod.POST, "/api/recipeIngredients").authenticated()
                    .antMatchers(HttpMethod.GET, "/api/recipeInstructions/**").permitAll()
                    .antMatchers(HttpMethod.GET, "/api/recipeIngredients/**").permitAll()


                    //role widget, data constrained by just the user role
                    //users of one type are all allowed to access and modify this resource
                    //example : content -- dashboard of Rolewidgets, should be CRUDable by Admin and Auth users
                    //User users should just see a list of RoleWidgets
                    //unauthenticated users should be directed to /login
                    //right now, I don't have any of these
                    .antMatchers(HttpMethod.GET, "/api/role/wdgt").authenticated()

                    //what controlling here are allowed to get data from this endpoint
                    .antMatchers(HttpMethod.GET, "/api/profiles", "/api/profiles/**").authenticated()
                    .antMatchers(HttpMethod.POST, "/api/profiles").authenticated()
                    .antMatchers(HttpMethod.PUT, "/api/profiles").authenticated()
                    .antMatchers(HttpMethod.DELETE, "/api/profiles/**").authenticated()

                    //default -- at least logged in for endpoint
                    .anyRequest().authenticated().and() //HttpSecurity
                    //pass through the jwtFilter to make sure in right format
                    .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);

    }
}

