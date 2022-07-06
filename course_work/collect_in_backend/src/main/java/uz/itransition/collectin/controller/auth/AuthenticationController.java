package uz.itransition.collectin.controller.auth;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping
@RestController
public class AuthenticationController {

    @GetMapping
    public String authenticateViaGithub(@AuthenticationPrincipal OAuth2User user){
        return user.toString();
    }

}
