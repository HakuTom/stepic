/**
 * Created by dokgo on 16.10.16.
 */
public class MyException extends Exception{
    public MyException(String message){
        super(message);
    }
    public MyException(String message, Throwable cause){
        super(message,cause);
    }
}
