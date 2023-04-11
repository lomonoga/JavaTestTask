package naumen.Exceptions;

public class EmptyFileException extends Exception {
    public EmptyFileException() {
        super("The file is empty!");
    }
}