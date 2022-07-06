package uz.itransition.collectin.exception.user;

import uz.itransition.collectin.exception.ApiException;


public class UserNotFoundException extends ApiException {

    private static final String DEFAULT_MESSAGE_ENG ="User not found with ";

    private static final String DEFAULT_MESSAGE_RUS ="Пользователь не найден с ";

    private UserNotFoundException(String messageENG, String messageRUS) {
        super(messageENG, messageRUS);
    }

    public static UserNotFoundException of(String parameter){
        return new UserNotFoundException(DEFAULT_MESSAGE_ENG + parameter, DEFAULT_MESSAGE_RUS+parameter);
    }
}
