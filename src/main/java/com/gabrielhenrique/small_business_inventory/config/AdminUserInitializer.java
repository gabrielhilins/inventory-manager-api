package com.gabrielhenrique.small_business_inventory.config;

import com.gabrielhenrique.small_business_inventory.model.Enum.Role;
import com.gabrielhenrique.small_business_inventory.model.User;
import com.gabrielhenrique.small_business_inventory.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminUserInitializer implements ApplicationListener<ContextRefreshedEvent> {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.user.name}")
    private String adminName;

    @Value("${admin.user.email}")
    private String adminEmail;

    @Value("${admin.user.password}")
    private String adminPassword;

    public AdminUserInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (!userRepository.existsByEmail(adminEmail)) {
            User adminUser = new User();
            adminUser.setName(adminName);
            adminUser.setEmail(adminEmail);
            adminUser.setPassword(passwordEncoder.encode(adminPassword));
            adminUser.setRole(Role.ADMIN);
            userRepository.save(adminUser);
        }
    }
}
