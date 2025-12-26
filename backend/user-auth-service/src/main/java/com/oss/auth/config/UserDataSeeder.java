package com.oss.auth.config;

import com.oss.auth.entity.Address;
import com.oss.auth.entity.Role;
import com.oss.auth.entity.User;
import com.oss.auth.repository.AddressRepository;
import com.oss.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        seedUsers();
    }

    private void seedUsers() {
        // Ensure Admin exists and has valid data
        User admin = userRepository.findByEmail("admin@gmail.com").orElse(
                User.builder()
                        .firstName("Admin")
                        .lastName("User")
                        .email("admin@gmail.com")
                        .role(Role.ADMIN)
                        .build());
        // Update fields to force re-encryption with current key
        admin.setPhoneNumber("1234567890");
        admin.setAddress("Admin Street");
        admin.setPassword(passwordEncoder.encode("Admin@123"));
        if (admin.getRole() == null)
            admin.setRole(Role.ADMIN);
        userRepository.save(admin);

        // Ensure Regular User exists
        User user = userRepository.findByEmail("user@gmail.com").orElse(
                User.builder()
                        .firstName("Regular")
                        .lastName("User")
                        .email("user@gmail.com")
                        .role(Role.USER)
                        .build());
        user.setPhoneNumber("0987654321");
        user.setAddress("User Street");
        user.setPassword(passwordEncoder.encode("User@123"));
        if (user.getRole() == null)
            user.setRole(Role.USER);
        User savedUser = userRepository.save(user);

        // Seed Address for regular user if not exists
        if (addressRepository.findByUser(savedUser).isEmpty()) {
            Address address = Address.builder()
                    .user(savedUser)
                    .street("twbsa")
                    .city("banga")
                    .state("ka")
                    .zipCode("980")
                    .country("in")
                    .label("Home")
                    .build();
            addressRepository.save(address);
        }

        System.out.println("Users and addresses seeded/updated: admin@gmail.com and user@gmail.com");
    }
}
