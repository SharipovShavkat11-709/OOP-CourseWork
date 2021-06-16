package data;

/**
 * Класс - Скидки
 * @author Admin
 */
public class Discount {
    private long id;
    private int days;
    private float percent;

    public Discount(int days, float percent) {
        this.days = days;
        this.percent = percent;
    }

    public Discount(long id, int days, float percent) {
        this.id = id;
        this.days = days;
        this.percent = percent;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public float getPercent() {
        return percent;
    }

    public void setPercent(float percent) {
        this.percent = percent;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 47 * hash + this.days;
        hash = 47 * hash + Float.floatToIntBits(this.percent);
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
        final Discount other = (Discount) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.days != other.days) {
            return false;
        }
        if (Float.floatToIntBits(this.percent) != Float.floatToIntBits(other.percent)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "От " + days + " дней скидка " + percent;
    }
    
    
}
