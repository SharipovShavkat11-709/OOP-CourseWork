package data;

import java.util.Objects;

/**
 * Класс - Центр проката авто
 * @author Admin
 */
public class CarCenter {
    
    private long id;
    private String name;
    private String city;
    private String street;
    private String house;

    public CarCenter() {
    }

    public CarCenter(String name, String city, String street, String house) {
        this.name = name;
        this.city = city;
        this.street = street;
        this.house = house;
    }

    public CarCenter(long id, String name, String city, String street, String house) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.street = street;
        this.house = house;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 61 * hash + Objects.hashCode(this.name);
        hash = 61 * hash + Objects.hashCode(this.city);
        hash = 61 * hash + Objects.hashCode(this.street);
        hash = 61 * hash + Objects.hashCode(this.house);
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
        final CarCenter other = (CarCenter) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.city, other.city)) {
            return false;
        }
        if (!Objects.equals(this.street, other.street)) {
            return false;
        }
        if (!Objects.equals(this.house, other.house)) {
            return false;
        }
        return true;
    }

    
    
    @Override
    public String toString() {
        return name + " (" + city + ", " + street + ", " + house + ")";
    }
    
    
}
