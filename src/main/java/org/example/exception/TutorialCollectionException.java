package org.example.exception;
//neww
public class TutorialCollectionException extends Exception{
    private static final long serialVersionUID =1L;

    public TutorialCollectionException(String message){
        super(message);
    }
    public static String NotFoundException(String id) {
        return "Tutorial with "+id+" not found";
    }
    public static String TutorialAlreadyExists() {
        return "Tutorial with given name already exists";

    }
}
