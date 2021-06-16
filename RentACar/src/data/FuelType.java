package data;

/**
 *
 * @author Admin
 */
public class FuelType {
    
    private long id;
    private String name;

    public FuelType() {
    }

    public FuelType(String name) {
        this.name = name;
    }

    public FuelType(long id, String name) {
        this.id = id;
        this.name = name;
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

    @Override
    public String toString() {
        return name;
    }   
}
