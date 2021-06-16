package sql;

import data.CarCenter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Admin
 */
public class QueryCarCenter extends DBConnection implements Query<CarCenter>{
    private final String queryGetAll = "SELECT car_center.id, car_center.name, car_center.city, car_center.street, car_center.house FROM car_center";
    private final String queryAdd = "INSERT INTO car_center (name, city, street, house) VALUES (?, ?, ?, ?)";
    private final String queryUpdate = "UPDATE car_center SET name = ?, city = ?, street = ?, house = ? WHERE id = ?";
    private final String queryDelete = "DELETE FROM car_center WHERE id = ?";
    
    private Connection conn;
    
    @Override
    public List<CarCenter> getAll() {
        return getCarCenter(queryGetAll);
    }
    
    /**
     * Получение центра авто по id
     * @param id идентификатор
     * @return объект типа CarCenter, в случае отсутствия - null
     */
    public CarCenter getEntityById(long id){
        String query = queryGetAll + " WHERE car_center.id = " + id;
        List<CarCenter> list = getCarCenter(query);
        if(list.isEmpty()) return null;
        return list.get(0);
    }
    
    private List<CarCenter> getCarCenter(String query){
        List<CarCenter> list = new ArrayList<>();
        Statement stat = null;
        ResultSet rs = null;
        try {
            conn = open();
            //получение объекта типа Statement
            stat = conn.createStatement();
            //получение результирующего объекта типа ResultSet
            rs = stat.executeQuery(query);
            while (rs.next()) {
                //создание нового центра автопроката, полученных из БД
                CarCenter t = new CarCenter(rs.getLong("id"), rs.getString("name"), rs.getString("city"), rs.getString("street"), rs.getString("house"));
                //добавление в список центров автопрокатов
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
                Logger.getLogger(QueryCarCenter.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex1) {
                Logger.getLogger(QueryCarCenter.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return list;
    }

    @Override
    public boolean add(CarCenter t) {       
        PreparedStatement ps = null;
        int executeUpdate = -1;
        try {
            conn = open();
            //получение объекта типа PreparedStatement
            ps = conn.prepareStatement(queryAdd);
            //установка параметров
            ps.setString(1, t.getName());
            ps.setString(2, t.getCity());
            ps.setString(3, t.getStreet());
            ps.setString(4, t.getHouse());
            
            //запрос в БД
            executeUpdate = ps.executeUpdate();
            ps.close();
            close(conn);
        } catch (SQLException ex) {
            try {
                ps.close();
                close(conn);
                Logger.getLogger(QueryCarCenter.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex1) {
                Logger.getLogger(QueryCarCenter.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return executeUpdate >-1;
    }

    @Override
    public boolean update(CarCenter t) {
        
        PreparedStatement ps = null;
        int executeUpdate = -1;
        
        try {
            conn = open();
            //получение объекта типа PreparedStatement
            ps = conn.prepareStatement(queryUpdate);
            //установка параметров
            ps.setString(1, t.getName());
            ps.setString(2, t.getCity());
            ps.setString(3, t.getStreet());
            ps.setString(4, t.getHouse());
            ps.setLong(5, t.getId());
            //запрос в БД
            executeUpdate = ps.executeUpdate();

            ps.close();
            close(conn);
        } catch (SQLException ex) {
            try {
                ps.close();
                close(conn);
            } catch (SQLException ex1) {
                Logger.getLogger(QueryCarCenter.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(QueryCarCenter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return executeUpdate>-1;
    }

    @Override
    public boolean delete(long id) {
        
        PreparedStatement ps = null;
        int executeUpdate = -1;
        try {
            conn = open();
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
                Logger.getLogger(QueryCarCenter.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(QueryCarCenter.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return executeUpdate>-1;
    }
}
