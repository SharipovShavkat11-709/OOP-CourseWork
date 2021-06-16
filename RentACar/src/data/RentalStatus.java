package data;

/**
 *
 * @author Admin
 */
public enum RentalStatus {
    Открыт(1),
    Закрыт(2);
    
    private int status;
    
    RentalStatus(int status){
        this.status = status;
    }
    public int getStatus(){ return status;}
}
