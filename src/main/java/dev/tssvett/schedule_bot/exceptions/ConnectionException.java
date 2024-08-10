package dev.tssvett.schedule_bot.exceptions;

public class ConnectionException extends RuntimeException{

    public ConnectionException(String message){
        super(message);
    }

    public ConnectionException(Exception e){
        super(e);
    }
}
