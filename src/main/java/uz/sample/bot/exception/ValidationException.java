package uz.sample.bot.exception;

public class ValidationException extends Exception {
    private ValidationException(String msg) {
        super(msg);
    }
    public static ValidationException me(String msg) {
        return new ValidationException(msg);
    }

    public static ValidationException me() {
        return me("Unknown exception");
    }

}
