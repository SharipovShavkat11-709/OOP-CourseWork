package data;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Класс - Клиент
 * @author Admin
 */
public class Client {
    
    private long id;
    private String firstName;
    private String patronymic;
    private String lastName;
    private String phone;
    private LocalDate birthday;
    private int passportSeries;
    private int passport_id;
    private String password;

    public Client() {
    }

    public Client(String firstName, String patronymic, String lastName, String phone, LocalDate birthday, int passportSeries, int passport_id, String password) {
        this.firstName = firstName;
        this.patronymic = patronymic;
        this.lastName = lastName;
        this.phone = phone;
        this.birthday = birthday;
        this.passportSeries = passportSeries;
        this.passport_id = passport_id;
        this.password = password;
    }

    public Client(long id, String firstName, String patronymic, String lastName, String phone, LocalDate birthday, int passportSeries, int passport_id) {
        this.id = id;
        this.firstName = firstName;
        this.patronymic = patronymic;
        this.lastName = lastName;
        this.phone = phone;
        this.birthday = birthday;
        this.passportSeries = passportSeries;
        this.passport_id = passport_id;
    }

    public Client(long id, String firstName, String patronymic, String lastName, String phone, LocalDate birthday, int passportSeries, int passport_id, String password) {
        this.id = id;
        this.firstName = firstName;
        this.patronymic = patronymic;
        this.lastName = lastName;
        this.phone = phone;
        this.birthday = birthday;
        this.passportSeries = passportSeries;
        this.passport_id = passport_id;
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public int getPassportSeries() {
        return passportSeries;
    }

    public void setPassportSeries(int passportSeries) {
        this.passportSeries = passportSeries;
    }

    public int getPassport_id() {
        return passport_id;
    }

    public void setPassport_id(int passport_id) {
        this.passport_id = passport_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 37 * hash + Objects.hashCode(this.firstName);
        hash = 37 * hash + Objects.hashCode(this.patronymic);
        hash = 37 * hash + Objects.hashCode(this.lastName);
        hash = 37 * hash + Objects.hashCode(this.phone);
        hash = 37 * hash + Objects.hashCode(this.birthday);
        hash = 37 * hash + this.passportSeries;
        hash = 37 * hash + this.passport_id;
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
        final Client other = (Client) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.passportSeries != other.passportSeries) {
            return false;
        }
        if (this.passport_id != other.passport_id) {
            return false;
        }
        if (!Objects.equals(this.firstName, other.firstName)) {
            return false;
        }
        if (!Objects.equals(this.patronymic, other.patronymic)) {
            return false;
        }
        if (!Objects.equals(this.lastName, other.lastName)) {
            return false;
        }
        if (!Objects.equals(this.phone, other.phone)) {
            return false;
        }
        if (!Objects.equals(this.birthday, other.birthday)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Client{" + "id=" + id + ", firstName=" + firstName + ", patronymic=" + patronymic + ", lastName=" + lastName + ", phone=" + phone + ", birthday=" + birthday + ", passportSeries=" + passportSeries + ", passport_id=" + passport_id + '}';
    }
    
    
}
