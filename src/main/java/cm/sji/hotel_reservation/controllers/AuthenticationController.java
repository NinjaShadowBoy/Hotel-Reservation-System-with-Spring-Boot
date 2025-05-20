package cm.sji.hotel_reservation.controllers;

import cm.sji.hotel_reservation.dtos.AuthenticationRequest;
import cm.sji.hotel_reservation.dtos.AuthenticationResponse;
import cm.sji.hotel_reservation.entities.User;
import cm.sji.hotel_reservation.services.AuthenticationService;
import cm.sji.hotel_reservation.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.ui.Model;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @PostMapping("/login")
    public String checkCredentials(@ModelAttribute("user") AuthenticationRequest request, Model model,
                                   RedirectAttributes redirectAttributes, HttpServletResponse response) {
        logger.info("Checking credentials {}", request);
        try {
            var auth = SecurityContextHolder.getContext().getAuthentication();

            var usernameOrEmail = request.getUsername();
            logger.info("Attempting to authenticate user: {}, {}", usernameOrEmail,
                    SecurityContextHolder.getContext().getAuthentication());

            AuthenticationResponse authResponse = authenticationService.authenticate(request);
            User user = authResponse.getUser();
            logger.info("Authentication successful for user: {}", user.getUsername());

            redirectAttributes.addFlashAttribute("token", authResponse.getToken());
            redirectAttributes.addFlashAttribute("user", user);
            model.addAttribute("user", user);

            // Set the token in HttpOnly cookie
            Cookie cookie = new Cookie("JWT", authResponse.getToken());
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(30 * 60); // Cookie expires in 30 minutes

            response.addCookie(cookie);
            response.addHeader("Authorization", "Bearer " + authResponse.getToken());

            logger.info("JWT Token set for user: {}", user.getUsername());

            if (User.UserRole.ADMIN == user.getRole()) {
                logger.info("Redirecting {} to the admin dashboard", user.getUsername());
                return "redirect:/admin/home/" + user.getId(); // Admin dashboard
            } else if (User.UserRole.CLIENT == user.getRole()) {
                logger.info("Redirecting {} to the client home", user.getUsername());
                return "redirect:/client/home/" + user.getId(); // PM dashboard (or other role)
            } else if (User.UserRole.OWNER == user.getRole()) {
                logger.info("Redirecting {} to the owner dashboard", user.getUsername());
                return "redirect:/owner/home/" + user.getId(); // PM dashboard (or other role)
            } else return "redirect:/client/home/";

        } catch (AuthenticationException e) {
            logger.error("Authentication failed for {}: {}", request.getUsername(), e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Invalid credentials");
            return "login"; // Stay on login page with error
        } catch (RuntimeException e) {
            logger.error("Runtime exception occurred: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "login";
        } catch (Exception e) {
            logger.error("Unkmown error occurred: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        logger.info("Logging out user and clearing JWT cookie");

        Cookie cookie = new Cookie("JWT", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0); // Deletes the cookie
        response.setHeader("Set-Cookie", "JWT=" + null + "; Path=/; HttpOnly; SameSite=Lax");

        response.addCookie(cookie);
        try {
            response.flushBuffer();
            logger.info("User logged out and cookie cleared.");
        } catch (IOException e) {
            logger.error("Error while flushing response during logout: {}", e.getMessage());
        }

        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login(Model model, @RequestParam(required = false) String error,
                        RedirectAttributes redirectAttributes) {

        var auth = SecurityContextHolder.getContext().getAuthentication();
        logger.info("Checking if user is already authenticated {}", auth);

        // If user is already authenticated, redirect to the home page
        // if (auth != null && auth.isAuthenticated() && !(auth instanceof
        // AnonymousAuthenticationToken)) {
        // User user = (User) auth.getPrincipal();

        // model.addAttribute("user", user);
        // redirectAttributes.addAttribute("user", user);
        // logger.info("Authenticated user: {}", user.getUsername());

        // for (var a : auth.getAuthorities()) {
        // if (a.getAuthority().equals("ADMIN")) {
        // logger.info("Redirecting {} to admin home", user.getUsername());
        // return "redirect:/admin/home";
        // }
        // }

        // if (Role.ADMIN == user.getRole()) {
        // logger.info("Redirecting {} to admin home", user.getUsername());
        // return "redirect:/admin/home";
        // } else {
        // logger.info("Redirecting {} to PM home", user.getUsername());
        // return "redirect:/pm/home";
        // }
        // }

        logger.info("No authentication found, returning to login page with error: {}", error);
        AuthenticationRequest authRequest = new AuthenticationRequest();
        model.addAttribute("user", authRequest);
        model.addAttribute("error", error);
        return "login";
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/client/home";
    }

}
