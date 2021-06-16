package data;

import java.util.Objects;

/**
 * Класс - автомобиль
 * @author Admin
 */
public class Car {
    
    private long id;
    private CarBrand carBrand;
    private String carModel;
    private CarType carType;
    private String color;
    private float consumption; //расход топлива на 100км
    private FuelType fuelType;
    private String registrationNumber; //регистрационный номер
    private CarCenter carCenter;
    private float price;
    private String photoPath;
    private int status; //статус авто (1 - свободен, 2 - бронь, 3 - занят)
    private CarStatus enumStatus;

    public Car() {
    }

    public Car(CarBrand carBrand, String carModel, CarType carType, String color, float consumption, FuelType fuelType, String registrationNumber, CarCenter carCenter, float price, int status, String photoPath) {
        this.carBrand = carBrand;
        this.carModel = carModel;
        this.carType = carType;
        this.color = color;
        this.consumption = consumption;
        this.fuelType = fuelType;
        this.registrationNumber = registrationNumber;
        this.carCenter = carCenter;
        this.price = price;
        this.status = status;
        setEnumStatus(status);
        
        this.photoPath = photoPath;
    }

    public Car(long id, CarBrand carBrand, String carModel, CarType carType, String color, float consumption, FuelType fuelType, String registrationNumber, CarCenter carCenter, float price, int status) {
        this.id = id;
        this.carBrand = carBrand;
        this.carModel = carModel;
        this.carType = carType;
        this.color = color;
        this.consumption = consumption;
        this.fuelType = fuelType;
        this.registrationNumber = registrationNumber;
        this.carCenter = carCenter;
        this.price = price;
        this.status = status;
        setEnumStatus(status);
        
    }

    public Car(long id, CarBrand carBrand, String carModel, CarType carType, String color, float consumption, FuelType fuelType, String registrationNumber, CarCenter carCenter, float price, String photoPath, int status) {
        this.id = id;
        this.carBrand = carBrand;
        this.carModel = carModel;
        this.carType = carType;
        this.color = color;
        this.consumption = consumption;
        this.fuelType = fuelType;
        this.registrationNumber = registrationNumber;
        this.carCenter = carCenter;
        this.price = price;
        this.photoPath = photoPath;
        this.status = status;
        setEnumStatus(status);
    }
    
    /**
     * Установка перечисления для статуса авто
     * @param status статус авто
     */
    private void setEnumStatus(int status){
        switch(status){
            case 1:
                this.enumStatus = CarStatus.Свободен;
                break;
            case 2:
                this.enumStatus = CarStatus.Бронь;
                break;
            default:
                this.enumStatus = CarStatus.Занят;
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public CarBrand getCarBrand() {
        return carBrand;
    }

    public void setCarBrand(CarBrand carBrand) {
        this.carBrand = carBrand;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }
    

    public CarType getCarType() {
        return carType;
    }

    public void setCarType(CarType carType) {
        this.carType = carType;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public float getConsumption() {
        return consumption;
    }

    public void setConsumption(float consumption) {
        this.consumption = consumption;
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    public void setFuelType(FuelType fuelType) {
        this.fuelType = fuelType;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public CarCenter getCarCenter() {
        return carCenter;
    }

    public void setCarCenter(CarCenter carCenter) {
        this.carCenter = carCenter;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
        setEnumStatus(status);
    }

    public CarStatus getEnumStatus() {
        return this.enumStatus;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 11 * hash + Objects.hashCode(this.carBrand);
        hash = 11 * hash + Objects.hashCode(this.carModel);
        hash = 11 * hash + Objects.hashCode(this.carType);
        hash = 11 * hash + Objects.hashCode(this.color);
        hash = 11 * hash + Float.floatToIntBits(this.consumption);
        hash = 11 * hash + Objects.hashCode(this.fuelType);
        hash = 11 * hash + Objects.hashCode(this.registrationNumber);
        hash = 11 * hash + Objects.hashCode(this.carCenter);
        hash = 11 * hash + Float.floatToIntBits(this.price);
        hash = 11 * hash + Objects.hashCode(this.photoPath);
        hash = 11 * hash + this.status;
        hash = 11 * hash + Objects.hashCode(this.enumStatus);
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
        final Car other = (Car) obj;
        if (this.id != other.id) {
            return false;
        }
        if (Float.floatToIntBits(this.consumption) != Float.floatToIntBits(other.consumption)) {
            return false;
        }
        if (Float.floatToIntBits(this.price) != Float.floatToIntBits(other.price)) {
            return false;
        }
        if (this.status != other.status) {
            return false;
        }
        if (!Objects.equals(this.carModel, other.carModel)) {
            return false;
        }
        if (!Objects.equals(this.color, other.color)) {
            return false;
        }
        if (!Objects.equals(this.registrationNumber, other.registrationNumber)) {
            return false;
        }
        if (!Objects.equals(this.photoPath, other.photoPath)) {
            return false;
        }
        if (!Objects.equals(this.carBrand, other.carBrand)) {
            return false;
        }
        if (!Objects.equals(this.carType, other.carType)) {
            return false;
        }
        if (!Objects.equals(this.fuelType, other.fuelType)) {
            return false;
        }
        if (!Objects.equals(this.carCenter, other.carCenter)) {
            return false;
        }
        if (this.enumStatus != other.enumStatus) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return carBrand + " " + carModel + " " + carType + " " + color + " " + registrationNumber;
    }

}
