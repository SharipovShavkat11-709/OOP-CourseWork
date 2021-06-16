package data;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Класс - менеджер
 * @author Admin
 */
public class Manager {
    private long id;
    private String firstName;
    private String patronymic;
    private String lastName;
    private String phone;
    private LocalDate birthday;
    private String password;

    public Manager() {
    }

    public Manager(String firstName, String patronymic, String lastName, String phone, LocalDate birthday) {
        this.firstName = firstName;
        this.patronymic = patronymic;
        this.lastName = lastName;
        this.phone = phone;
        this.birthday = birthday;
    }

    public Manager(String firstName, String patronymic, String lastName, String phone, LocalDate birthday, String password) {
        this.firstName = firstName;
        this.patronymic = patronymic;
        this.lastName = lastName;
        this.phone = phone;
        this.birthday = birthday;
        this.password = password;
    }

    public Manager(long id, String firstName, String patronymic, String lastName, String phone, LocalDate birthday) {
        this.id = id;
        this.firstName = firstName;
        this.patronymic = patronymic;
        this.lastName = lastName;
        this.phone = phone;
        this.birthday = birthday;
    }

    public Manager(long id, String firstName, String patronymic, String lastName, String phone, LocalDate birthday, String password) {
        this.id = id;
        this.firstName = firstName;
        this.patronymic = patronymic;
        this.lastName = lastName;
        this.phone = phone;
        this.birthday = birthday;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 59 * hash + Objects.hashCode(this.firstName);
        hash = 59 * hash + Objects.hashCode(this.patronymic);
        hash = 59 * hash + Objects.hashCode(this.lastName);
        hash = 59 * hash + Objects.hashCode(this.phone);
        hash = 59 * hash + Objects.hashCode(this.birthday);
        hash = 59 * hash + Objects.hashCode(this.password);
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
        final Manager other = (Manager) obj;
        if (this.id != other.id) {
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
        if (!Objects.equals(this.password, other.password)) {
            return false;
        }
        if (!Objects.equals(this.birthday, other.birthday)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Manager{" + "id=" + id + ", firstName=" + firstName + ", patronymic=" + patronymic + ", lastName=" + lastName + ", phone=" + phone + ", birthday=" + birthday + '}';
    }

    
}
