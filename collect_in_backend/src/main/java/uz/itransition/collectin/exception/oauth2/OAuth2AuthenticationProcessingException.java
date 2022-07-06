package uz.itransition.collectin.exception.oauth2;

import uz.itransition.collectin.entity.enums.AuthProvider;
import uz.itransition.collectin.exception.ApiException;

public class OAuth2AuthenticationProcessingException extends ApiException {

    private static final String WRONG_PROVIDER_ENG = "Please use your %s account to login";
    private static final String WRONG_PROVIDER_RUS = "Пожалуйста, используйте свою учетную запись %s для входа";

    private static final String ACCOUNT_SUSPENDED_ENG = "Your account suspended";
    private static final String ACCOUNT_SUSPENDED_RUS = "Ваш аккаунт заблокирован";

    private static final String EMAIL_NOT_FOUND_ENG = "Email not found from authentication provider";
    private static final String EMAIL_NOT_FOUND_RUS = "Электронная почта не найдена от поставщика аутентификации";

    private static final String NOT_SUPPORTED_ENG = "Login with %s is not supported yet";
    private static final String NOT_SUPPORTED_RUS = "Вход через %s пока не поддерживается";

    private OAuth2AuthenticationProcessingException(String messageENG, String messageRUS) {
        super(messageENG, messageRUS);
    }

    public static OAuth2AuthenticationProcessingException notSupported(String registrationId){
        return new OAuth2AuthenticationProcessingException(
                String.format(NOT_SUPPORTED_ENG,registrationId),
                String.format(NOT_SUPPORTED_RUS,registrationId)
        );
    }

    public static OAuth2AuthenticationProcessingException ofProvider(AuthProvider authProvider){
        return new OAuth2AuthenticationProcessingException(
                String.format(WRONG_PROVIDER_ENG,authProvider.name()),
                String.format(WRONG_PROVIDER_RUS,authProvider.name())
                );
    }

    public static OAuth2AuthenticationProcessingException suspended(){
        return new OAuth2AuthenticationProcessingException(
                String.format(ACCOUNT_SUSPENDED_ENG),
                String.format(ACCOUNT_SUSPENDED_RUS)
        );
    }

    public static OAuth2AuthenticationProcessingException ofEmail(){
        return new OAuth2AuthenticationProcessingException(EMAIL_NOT_FOUND_ENG,EMAIL_NOT_FOUND_RUS);
    }



}
