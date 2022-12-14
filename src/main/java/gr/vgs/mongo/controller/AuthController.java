package gr.vgs.mongo.controller;


import gr.vgs.mongo.enums.ERole;
import gr.vgs.mongo.entity.Role;
import gr.vgs.mongo.entity.User;
import gr.vgs.mongo.model.UserInfoModel;
import gr.vgs.mongo.payload.JwtResponse;
import gr.vgs.mongo.payload.LoginRequest;
import gr.vgs.mongo.payload.MessageResponse;
import gr.vgs.mongo.payload.SignupRequest;
import gr.vgs.mongo.repository.RoleRepository;
import gr.vgs.mongo.repository.UserRepository;
import gr.vgs.mongo.security.JwtUtils;
import gr.vgs.mongo.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

    @Autowired
    JwtUtils jwtUtils;

    @GetMapping("/info")
    public ResponseEntity<?> getUser() {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails)principal).getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        UserInfoModel userInfoModel = new UserInfoModel();
        userInfoModel.setUsername(user.getUsername());
        userInfoModel.setEmail(user.getEmail());

        return ResponseEntity.ok(userInfoModel);
    }

    @PostMapping("/edit/info")
    public ResponseEntity<?> editUser(@Valid @RequestBody UserInfoModel userInfoModel) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            String username = ((UserDetails)principal).getUsername();
            User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
            user.setUsername("testusername");
            user.setEmail("testusername@testuseremail.com");
            userRepository.save(user);
            if (userInfoModel.getUsername() != null && !userRepository.existsByUsername(userInfoModel.getUsername())) user.setUsername(userInfoModel.getUsername());
            if (userInfoModel.getEmail() != null && !userRepository.existsByEmail(userInfoModel.getEmail())) user.setEmail(userInfoModel.getEmail());
            if (userInfoModel.getPassword() != null) user.setPassword(encoder.encode(userInfoModel.getPassword()));
            userRepository.save(user);
            return ResponseEntity.ok("User info edited successfully");
        } catch (Exception e) {
            return ResponseEntity.ok("User info edit failed");
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}