package uz.itransition.collectin.exception;

public class DataNotFoundException extends ApiException{

    private static final String DEFAULT_MESSAGE_ENG = " not found with ";
    private static final String DEFAULT_MESSAGE_RUS = " не найдено с ";

    private DataNotFoundException(String messageENG, String messageRUS) {
        super(messageENG, messageRUS);
    }

    public static DataNotFoundException of(String resourceENG, String resourceRUS, String parameter)
    {
        return new DataNotFoundException(
                resourceENG + DEFAULT_MESSAGE_ENG + parameter,
                resourceRUS + DEFAULT_MESSAGE_RUS + parameter
                );
    }
}
