package com.datien.movie;

import com.datien.movie.role.Role;
import com.datien.movie.role.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.List;

@SpringBootApplication
@EnableAsync
public class MovieApplication {

    public static void main(String[] args) {
        SpringApplication.run(MovieApplication.class, args);
    }

    @Bean
    public CommandLineRunner runner(RoleRepository roleRepository) {
        return args -> {
            List<String> roles = List.of("USER", "ADMIN", "MANAGER", "CONTRIBUTOR", "SUBSCRIBER");
            roles.forEach(role -> {
                if (roleRepository.findByName(role).isEmpty()) {
                    roleRepository.save(Role.builder().name(role).build());
                }
            });
        };
    }


}
