package sql;

import data.Car;
import data.CarBrand;
import data.CarCenter;
import data.CarType;
import data.FuelType;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.Image;

/**
 *
 * @author Admin
 */
public class QueryCar extends DBConnection implements Query<Car> {

    private final String queryGetAll = "SELECT car.id, car.model, car.color, car.registration_number, car.consumption_100, car.price, car.status, "
            + "brand.id as brand_id, brand.name as brand_name, "
            + "c_type.id as type_id, c_type.name as type_name, "
            + "fuel.id as fuel_id, fuel.name as fuel_name, "
            + "center.id as center_id, center.name as center_name, "
            + "center.city as center_city, center.street as center_streer, "
            + "center.house as center_house "
            + "FROM car "
            + "INNER JOIN car_brand as brand ON car.car_brand_id = brand.id "
            + "INNER JOIN car_type as c_type ON car.car_type_id = c_type.id "
            + "INNER JOIN fuel_type as fuel ON car.fuel_type_id = fuel.id "
            + "INNER JOIN car_center as center ON car.car_center_id = center.id";
    private final String queryGetPhotoByCarId = "SELECT car.photo FROM car WHERE id = ?";
    private final String queryUpdatePhoto = "UPDATE car SET car.photo = ? WHERE id = ?";
    private final String queryAdd = "INSERT INTO car (car_brand_id, model, car_type_id, color, consumption_100, fuel_type_id, registration_number, car_center_id, price, photo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private final String queryUpdateWithPhoto = "UPDATE car SET car_brand_id = ?, model = ?, car_type_id = ?, color = ?, consumption_100 = ?, fuel_type_id = ?, registration_number = ?, car_center_id = ?, price = ?, photo = ? WHERE id = ?";
    private final String queryUpdateWithoutPhoto = "UPDATE car SET car_brand_id = ?, model = ?, car_type_id = ?, color = ?, consumption_100 = ?, fuel_type_id = ?, registration_number = ?, car_center_id = ?, price = ? WHERE id = ?";
    private final String queryDelete = "DELETE FROM car WHERE id = ?";

    private Connection conn;

    @Override
    public List<Car> getAll() {
        return getCar(queryGetAll  + " ORDER BY id");
    }
    
    /**
     * Получение авто по id
     * @param id идентификатор
     * @return объект типа Car, в случае отсутствия - null
     */
    public Car getEntityById(long id){
        String query = queryGetAll + " WHERE car.id = " + id + " ORDER BY id";
        List<Car> list = getCar(query);
        if(list.isEmpty()) return null;
        return list.get(0);
    }
    
    /**
     * Получение всех авто относительно статуса
     * @param status статус авто
     * @return список авто
     */
    public List<Car> getCarByStatus(int status) {
        String query = queryGetAll + " WHERE car.status = " + status + " ORDER BY id";
        return getCar(query);
    }
    
    private List<Car> getCar(String query) {
        List<Car> list = new ArrayList<>();
        Statement stat = null;
        ResultSet rs = null;
        try {
            conn = open();
            //получение объекта типа Statement
            stat = conn.createStatement();
            //получение результирующего объекта типа ResultSet
            rs = stat.executeQuery(query);
            while (rs.next()) {
                //создание нового авто, полученных из БД
                CarBrand carBrand = new CarBrand(rs.getLong("brand_id"), rs.getString("brand_name"));
                CarType carType = new CarType(rs.getLong("type_id"), rs.getString("type_name"));
                FuelType fuelType = new FuelType(rs.getLong("fuel_id"), rs.getString("fuel_name"));
                CarCenter carCenter = new CarCenter(rs.getLong("center_id"), rs.getString("center_name"),
                        rs.getString("center_city"), rs.getString("center_streer"), rs.getString("center_house"));

                Car t = new Car(rs.getLong("car.id"), carBrand, rs.getString("car.model"), carType, rs.getString("car.color"),
                        rs.getFloat("car.consumption_100"), fuelType, rs.getString("car.registration_number"), carCenter, rs.getFloat("car.price"), rs.getInt("car.status"));
                //добавление в список авто
                list.add(t);
            }
            rs.close();
            stat.close();
            close(conn);
        } catch (SQLException ex) {
            try {
                rs.close();
                stat.close();
                close(conn);
                Logger.getLogger(QueryCar.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex1) {
                Logger.getLogger(QueryCar.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return list;
    }

    /**
     * Получение фотографии авто по id
     *
     * @param id идентификатор авто
     * @return объет типа Image, в случае отсутствия - null
     */
    public Image getPhotoByCarId(long id) {
        PreparedStatement stat = null;
        ResultSet rs = null;
        //BufferedImage image = null;
        Image image = null;
        try {
            conn = open();
            //получение объекта типа Statement
            stat = conn.prepareStatement(queryGetPhotoByCarId);
            stat.setLong(1, id);
            //получение результирующего объекта типа ResultSet
            rs = stat.executeQuery();
            if (rs.next()) {
                Blob blob = rs.getBlob("photo");
                if (blob != null) {
                    InputStream is = blob.getBinaryStream(1, blob.length());
                    image = new Image(is);
                }
            }
            rs.close();
            stat.close();
            close(conn);
        } catch (SQLException ex) {
            try {
                rs.close();
                stat.close();
                close(conn);
                Logger.getLogger(QueryCar.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex1) {
                Logger.getLogger(QueryCar.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }

        return image;
    }

    @Override
    public boolean add(Car t) {
        PreparedStatement ps = null;
        int executeUpdate = -1;
        try {
            conn = open();
            //получение объекта типа PreparedStatement
            ps = conn.prepareStatement(queryAdd);
            //установка параметров
            ps.setLong(1, t.getCarBrand().getId());
            ps.setString(2, t.getCarModel());
            ps.setLong(3, t.getCarType().getId());
            ps.setString(4, t.getColor());
            ps.setFloat(5, t.getConsumption());
            ps.setLong(6, t.getFuelType().getId());
            ps.setString(7, t.getRegistrationNumber());
            ps.setLong(8, t.getCarCenter().getId());
            ps.setFloat(9, t.getPrice());

            FileInputStream fis = null;
            if (t.getPhotoPath() == null) {
                ps.setNull(10, Types.NULL);
            } else {
                File file = new File(t.getPhotoPath());
                fis = new FileInputStream(file);
                ps.setBinaryStream(10, fis, (int) file.length());
            }

            //запрос в БД
            executeUpdate = ps.executeUpdate();
            ps.close();
            close(conn);
            if (fis != null) {
                fis.close();
            }

        } catch (SQLException ex) {
            try {
                ps.close();
                close(conn);
                Logger.getLogger(QueryCarCenter.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex1) {
                Logger.getLogger(QueryCarCenter.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(QueryCar.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(QueryCar.class.getName()).log(Level.SEVERE, null, ex);
        }
        return executeUpdate > -1;
    }

    /**
     * Обновление фото. Если путь равен null, то удалить фото из БД
     * @param id идентификатор авто
     * @param path путь к файлу с фото
     * @return true - в случае успеха, иначе - false
     */
    public boolean updatePhoto(long id, String path) {
        PreparedStatement ps = null;
        int executeUpdate = -1;
        try {
            conn = open();
            //получение объекта типа PreparedStatement
            ps = conn.prepareStatement(queryUpdatePhoto);
            //установка параметров
            FileInputStream fis = null;
            if (path != null) {
                File file = new File(path);
                fis = new FileInputStream(file);
                ps.setBinaryStream(1, fis, (int) file.length());
            } else {
                ps.setNull(1, Types.NULL);
            }
            ps.setLong(2, id);
            //запрос в БД
            executeUpdate = ps.executeUpdate();
            ps.close();
            if (fis != null) {
                fis.close();
            }

        } catch (SQLException ex) {
            try {
                ps.close();
                close(conn);
                Logger.getLogger(QueryCarCenter.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex1) {
                Logger.getLogger(QueryCarCenter.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(QueryCar.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(QueryCar.class.getName()).log(Level.SEVERE, null, ex);
        }
        return executeUpdate > -1;
    }

    @Override
    public boolean update(Car t) {
        //обновление авто без изменения фото
        PreparedStatement ps = null;
        int executeUpdate = -1;

        try {
            conn = open();
            //получение объекта типа PreparedStatement
            if (t.getPhotoPath() != null) {
                ps = conn.prepareStatement(queryUpdateWithPhoto);
            } else {
                ps = conn.prepareStatement(queryUpdateWithoutPhoto);
            }
            //установка параметров
            ps.setLong(1, t.getCarBrand().getId());
            ps.setString(2, t.getCarModel());
            ps.setLong(3, t.getCarType().getId());
            ps.setString(4, t.getColor());
            ps.setFloat(5, t.getConsumption());
            ps.setLong(6, t.getFuelType().getId());
            ps.setString(7, t.getRegistrationNumber());
            ps.setLong(8, t.getCarCenter().getId());
            ps.setFloat(9, t.getPrice());
            FileInputStream fis = null;
            
            if (t.getPhotoPath() != null) {
                File file = new File(t.getPhotoPath());
                fis = new FileInputStream(file);
                ps.setBinaryStream(10, fis, (int) file.length());
                ps.setLong(11, t.getId());
            } else {
                ps.setLong(10, t.getId());
            }
            

            //запрос в БД
            executeUpdate = ps.executeUpdate();
            if(fis != null) try {
                fis.close();
            } catch (IOException ex) {
                Logger.getLogger(QueryCar.class.getName()).log(Level.SEVERE, null, ex);
            }
            ps.close();
        } catch (SQLException ex) {
            try {
                ps.close();
                close(conn);
            } catch (SQLException ex1) {
                Logger.getLogger(QueryCarCenter.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(QueryCar.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(QueryCar.class.getName()).log(Level.SEVERE, null, ex);
        }
        return executeUpdate > -1;
    }

    @Override
    public boolean delete(long id) {

        PreparedStatement ps = null;
        int executeUpdate = -1;
        conn = open();
        try {
            ps = conn.prepareStatement(queryDelete);
            ps.setLong(1, id);
            //запрос в БД
            executeUpdate = ps.executeUpdate();
            ps.close();
            close(conn);
        } catch (SQLException ex) {
            try {
                ps.close();
                close(conn);
            } catch (SQLException ex1) {
                Logger.getLogger(QueryCar.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(QueryCar.class.getName()).log(Level.SEVERE, null, ex);
        }

        return executeUpdate > -1;
    }
}
