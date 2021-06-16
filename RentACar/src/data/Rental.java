package data;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Класс - прокат
 * @author Admin
 */
public class Rental {
    
    private long id;
    private long reservationId;
    private float price;
    private float totalPrice;
    private RentalStatus rentalStatus;
    private LocalDate dateReceiving;//фактическя дата получения авто
    private LocalDate dateReturn;//фактическая дата возврата авто
    //текстовое представление авто для таблицы прокатов авто клиента
    private String carText;

    public Rental(long reservationId, float price, float totalPrice, int rentalStatus, LocalDate dateReceiving, LocalDate dateReturn) {
        this.reservationId = reservationId;
        this.price = price;
        this.totalPrice = totalPrice;
        setEnumStatus(rentalStatus);
        this.dateReceiving = dateReceiving;
        this.dateReturn = dateReturn;
    }  

    public Rental(long reservationId, float price, RentalStatus rentalStatus, LocalDate dateReceiving, LocalDate dateReturn) {
        this.reservationId = reservationId;
        this.price = price;
        this.rentalStatus = rentalStatus;
        this.dateReceiving = dateReceiving;
        this.dateReturn = dateReturn;
    }

    public Rental(long id, long reservationId, float price, float totalPrice, int rentalStatus, LocalDate dateReceiving, LocalDate dateReturn) {
        this.id = id;
        this.reservationId = reservationId;
        this.price = price;
        this.totalPrice = totalPrice;
        setEnumStatus(rentalStatus);
        this.dateReceiving = dateReceiving;
        this.dateReturn = dateReturn;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getReservationId() {
        return reservationId;
    }

    public void setReservationId(long reservationId) {
        this.reservationId = reservationId;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public RentalStatus getRentalStatus() {
        return rentalStatus;
    }

    public void setRentalStatus(RentalStatus rentalStatus) {
        this.rentalStatus = rentalStatus;
    }

    public LocalDate getDateReceiving() {
        return dateReceiving;
    }

    public void setDateReceiving(LocalDate dateReceiving) {
        this.dateReceiving = dateReceiving;
    }

    public LocalDate getDateReturn() {
        return dateReturn;
    }

    public void setDateReturn(LocalDate dateReturn) {
        this.dateReturn = dateReturn;
    }

    public String getCarText() {
        return carText;
    }

    public void setCarText(String carText) {
        this.carText = carText;
    }
    
    /**
     * Установка перечисления для статуса проката
     * @param status статус авто
     */
    private void setEnumStatus(int status){
        switch(status){
            case 1:
                this.rentalStatus = RentalStatus.Открыт;
                break;
            default:
                this.rentalStatus = RentalStatus.Закрыт;
                break;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 73 * hash + (int) (this.reservationId ^ (this.reservationId >>> 32));
        hash = 73 * hash + Float.floatToIntBits(this.price);
        hash = 73 * hash + Float.floatToIntBits(this.totalPrice);
        hash = 73 * hash + Objects.hashCode(this.rentalStatus);
        hash = 73 * hash + Objects.hashCode(this.dateReceiving);
        hash = 73 * hash + Objects.hashCode(this.dateReturn);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Rental other = (Rental) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.reservationId != other.reservationId) {
            return false;
        }
        if (Float.floatToIntBits(this.price) != Float.floatToIntBits(other.price)) {
            return false;
        }
        if (Float.floatToIntBits(this.totalPrice) != Float.floatToIntBits(other.totalPrice)) {
            return false;
        }
        if (this.rentalStatus != other.rentalStatus) {
            return false;
        }
        if (!Objects.equals(this.dateReceiving, other.dateReceiving)) {
            return false;
        }
        if (!Objects.equals(this.dateReturn, other.dateReturn)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Rental{" + "id=" + id + ", reservationId=" + reservationId + ", price=" + price + ", totalPrice=" + totalPrice + ", rentalStatus=" + rentalStatus + ", dateReceiving=" + dateReceiving + ", dateReturn=" + dateReturn + '}';
    }

}
