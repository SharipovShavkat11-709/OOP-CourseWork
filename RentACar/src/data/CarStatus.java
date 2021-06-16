package data;

/**
 *
 * @author Admin
 */
public enum CarStatus {
    Свободен(1),
    Бронь(2),
    Занят(3);
    
    private int status;
    
    CarStatus(int status){
        this.status = status;
    }
    public int getStatus(){ return status;}
}
