package OnlineDoctorAppointmentSystem.Config;

import OnlineDoctorAppointmentSystem.Security.JWTAuthenticationFilter;
import OnlineDoctorAppointmentSystem.Security.JwtAuthenticationEntryPoint;
import OnlineDoctorAppointmentSystem.Security.JwtTokenProvider;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
@EnableMethodSecurity
public class WebSecurityConfig {
    @Autowired
    private JWTAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final String[] whiteListUrls={
            "/docplus.in/auth/register",
            "/docplus.in/auth/verifyRegistration*",
            "/docplus.in/auth/resendVerificationToken*",
            "/docplus.in/auth/resetPassword",
            "/docplus.in/auth/savePassword*",
            "/docplus.in/auth/changePassword",
            "/docplus.in/auth/login",
            //User
            "/docplus.in/users-list",
            "/docplus.in/add-user",
            "/docplus.in/user",
            "/docplus.in/user/**",
            "/docplus.in/reg-users-count",
            "/docplus.in/activated-users-count",
            //Doctor
            "/docplus.in/doctors-list",
            "/docplus.in/add-doctor",
            "/docplus.in/doc-login",
            "/docplus.in/doctor/**",
            //Appointment
            "/docplus.in/book-appointment",
            "/docplus.in/all-appointments",
            "/docplus.in/appointment/**",

            //Payment
            "docplus.in/payment/*",
            //Booking
            "/docplus.in/check-availability",
            //Messaging
            "docplus.in/storeMessage",
            "docplus.in/retrieveMessage"
            };
    @Bean
    public PasswordEncoder passwordEncoder()
    {

        return new BCryptPasswordEncoder(11);
    }
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf()
                .disable()
                .authorizeHttpRequests((authorize)->{
                authorize.requestMatchers(whiteListUrls).permitAll();
                authorize.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();
                authorize.anyRequest().authenticated();
                }).httpBasic(Customizer.withDefaults());

        http.exceptionHandling( exception -> exception
                .authenticationEntryPoint(jwtAuthenticationEntryPoint));

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
    return configuration.getAuthenticationManager();
    }
}
