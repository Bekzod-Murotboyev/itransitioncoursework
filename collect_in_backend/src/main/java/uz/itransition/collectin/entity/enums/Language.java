package uz.itransition.collectin.entity.enums;

public enum Language {
    ENG("ENG", "АНГЛ"),
    RUS("RUS", "РУС"),
    UZB("UZB", "UZB");

    public final String languageENG;
    public final String languageRUS;

    Language(String languageENG, String languageRUS) {
        this.languageENG = languageENG;
        this.languageRUS = languageRUS;
    }
}
