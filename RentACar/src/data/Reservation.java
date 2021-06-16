package data;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Класс - Бронирование
 * @author Admin
 */
public class Reservation {

    private long id;
    private long client_id;
    private long car_id;
    private long car_center_receiving_id;
    private long car_center_return_id;
    private LocalDate date_application;
    private LocalDate date_receiving;
    private LocalDate date_return;
    private ReservationStatus enumReservationStatus;
    private int reservationStatus;
    private float discountPercent;
    private float payment;
    //текстовое представление авто для таблицы заявок клиента
    private String carText;

    public Reservation() {
    }

    public Reservation(long client_id, long car_id, long car_center_receiving_id, long car_center_return_id, LocalDate date_application, LocalDate date_receiving, LocalDate date_return, int reservationStatus, float discountPercent, float payment) {
        this.client_id = client_id;
        this.car_id = car_id;
        this.car_center_receiving_id = car_center_receiving_id;
        this.car_center_return_id = car_center_return_id;
        this.date_application = date_application;
        this.date_receiving = date_receiving;
        this.date_return = date_return;
        this.reservationStatus = reservationStatus;
        this.discountPercent = discountPercent;
        this.payment = payment;

        setReservationStatus(reservationStatus);
    }

    public Reservation(long id, long client_id, long car_id, long car_center_receiving_id, long car_center_return_id, LocalDate date_application, LocalDate date_receiving, LocalDate date_return, int reservationStatus, float discountPercent, float payment) {
        this.id = id;
        this.client_id = client_id;
        this.car_id = car_id;
        this.car_center_receiving_id = car_center_receiving_id;
        this.car_center_return_id = car_center_return_id;
        this.date_application = date_application;
        this.date_receiving = date_receiving;
        this.date_return = date_return;
        this.reservationStatus = reservationStatus;
        this.discountPercent = discountPercent;
        this.payment = payment;

        setReservationStatus(reservationStatus);
    }

    /**
     * Установка перечисления для статуса бронирования авто
     *
     * @param status статус авто
     */
    private void setEnumStatus(int status) {
        switch (status) {
            case 1:
                this.enumReservationStatus = ReservationStatus.Бронь;
                break;
            case 2:
                this.enumReservationStatus = ReservationStatus.Одобрено;
                break;
            case 3:
                this.enumReservationStatus = ReservationStatus.Отказ;
                break;
            default:
                this.enumReservationStatus = ReservationStatus.Обработан;
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getClient_id() {
        return client_id;
    }

    public void setClient_id(long client_id) {
        this.client_id = client_id;
    }

    public long getCar_id() {
        return car_id;
    }

    public void setCar_id(long car_id) {
        this.car_id = car_id;
    }

    public long getCar_center_receiving_id() {
        return car_center_receiving_id;
    }

    public void setCar_center_receiving_id(long car_center_receiving_id) {
        this.car_center_receiving_id = car_center_receiving_id;
    }

    public long getCar_center_return_id() {
        return car_center_return_id;
    }

    public void setCar_center_return_id(long car_center_return_id) {
        this.car_center_return_id = car_center_return_id;
    }

    public LocalDate getDate_application() {
        return date_application;
    }

    public void setDate_application(LocalDate date_application) {
        this.date_application = date_application;
    }

    public LocalDate getDate_receiving() {
        return date_receiving;
    }

    public void setDate_receiving(LocalDate date_receiving) {
        this.date_receiving = date_receiving;
    }

    public LocalDate getDate_return() {
        return date_return;
    }

    public void setDate_return(LocalDate date_return) {
        this.date_return = date_return;
    }

    public int getReservationStatus() {
        return reservationStatus;
    }

    public void setReservationStatus(int reservationStatus) {
        this.reservationStatus = reservationStatus;
        setEnumStatus(reservationStatus);
    }

    public ReservationStatus getEnumReservationStatus() {
        return enumReservationStatus;
    }

    public float getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(float discountPercent) {
        this.discountPercent = discountPercent;
    }

    public float getPayment() {
        return payment;
    }

    public void setPayment(float payment) {
        this.payment = payment;
    }

    public String getCarText() {
        return carText;
    }

    public void setCarText(String carText) {
        this.carText = carText;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 83 * hash + (int) (this.client_id ^ (this.client_id >>> 32));
        hash = 83 * hash + (int) (this.car_id ^ (this.car_id >>> 32));
        hash = 83 * hash + (int) (this.car_center_receiving_id ^ (this.car_center_receiving_id >>> 32));
        hash = 83 * hash + (int) (this.car_center_return_id ^ (this.car_center_return_id >>> 32));
        hash = 83 * hash + Objects.hashCode(this.date_application);
        hash = 83 * hash + Objects.hashCode(this.date_receiving);
        hash = 83 * hash + Objects.hashCode(this.date_return);
        hash = 83 * hash + Objects.hashCode(this.enumReservationStatus);
        hash = 83 * hash + this.reservationStatus;
        hash = 83 * hash + Float.floatToIntBits(this.discountPercent);
        hash = 83 * hash + Float.floatToIntBits(this.payment);
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
        final Reservation other = (Reservation) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.client_id != other.client_id) {
            return false;
        }
        if (this.car_id != other.car_id) {
            return false;
        }
        if (this.car_center_receiving_id != other.car_center_receiving_id) {
            return false;
        }
        if (this.car_center_return_id != other.car_center_return_id) {
            return false;
        }
        if (this.reservationStatus != other.reservationStatus) {
            return false;
        }
        if (Float.floatToIntBits(this.discountPercent) != Float.floatToIntBits(other.discountPercent)) {
            return false;
        }
        if (Float.floatToIntBits(this.payment) != Float.floatToIntBits(other.payment)) {
            return false;
        }
        if (!Objects.equals(this.date_application, other.date_application)) {
            return false;
        }
        if (!Objects.equals(this.date_receiving, other.date_receiving)) {
            return false;
        }
        if (!Objects.equals(this.date_return, other.date_return)) {
            return false;
        }
        if (this.enumReservationStatus != other.enumReservationStatus) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Reservation{" + "id=" + id + ", client_id=" + client_id + ", car_id=" + car_id + ", car_center_receiving_id=" + car_center_receiving_id + ", car_center_return_id=" + car_center_return_id + ", date_application=" + date_application + ", date_receiving=" + date_receiving + ", date_return=" + date_return + ", enumReservationStatus=" + enumReservationStatus + ", reservationStatus=" + reservationStatus + ", discountPercent=" + discountPercent + ", payment=" + payment + '}';
    }

}
