package data;

/**
 * Класс - статус бронирования авто
 * @author Admin
 */
public enum ReservationStatus {
    Бронь(1),
    Одобрено(2),
    Отказ(3),
    Обработан(4);
    
    private int status;
    
    ReservationStatus(int status){
        this.status = status;
    }
    public int getStatus(){ return status;}
}
