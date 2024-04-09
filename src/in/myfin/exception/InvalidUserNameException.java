package in.myfin.exception;

public class InvalidUserNameException extends  Exception{
    public InvalidUserNameException(String message){
        System.out.println("Invalid Username Or password");
    }

}
