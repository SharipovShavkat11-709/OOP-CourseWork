package sql;

import data.CarBrand;
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
public class QueryCarBrand extends DBConnection implements Query<CarBrand>{
    private final String queryGetAll = "SELECT car_brand.id, car_brand.name FROM car_brand";
    private final String queryAdd = "INSERT INTO car_brand (name) VALUES (?)";
    private final String queryUpdate = "UPDATE car_brand SET name = ? WHERE id = ?";
    private final String queryDelete = "DELETE FROM car_brand WHERE id = ?";
    
    private Connection conn;
    
    @Override
    public List<CarBrand> getAll() {
        List<CarBrand> list = new ArrayList<>();
        Statement stat = null;
        ResultSet rs = null;
        try {
            conn = open();
            //получение объекта типа Statement
            stat = conn.createStatement();
            //получение результирующего объекта типа ResultSet
            rs = stat.executeQuery(queryGetAll);
            while (rs.next()) {
                //создание новой марки авто, полученных из БД
                CarBrand t = new CarBrand(rs.getLong("id"), rs.getString("name"));
                //добавление в список марок авто
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
                Logger.getLogger(QueryCarBrand.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex1) {
                Logger.getLogger(QueryCarBrand.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return list;
    }

    @Override
    public boolean add(CarBrand t) {       
        PreparedStatement ps = null;
        int executeUpdate = -1;
        conn = open();
        try {
            //получение объекта типа PreparedStatement
            ps = conn.prepareStatement(queryAdd);
            //установка параметров
            ps.setString(1, t.getName());
            
            //запрос в БД
            executeUpdate = ps.executeUpdate();
            ps.close();
            close(conn);
        } catch (SQLException ex) {
            try {
                ps.close();
                close(conn);
                Logger.getLogger(QueryCarBrand.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex1) {
                Logger.getLogger(QueryCarBrand.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return executeUpdate >-1;
    }

    @Override
    public boolean update(CarBrand t) {
        
        PreparedStatement ps = null;
        int executeUpdate = -1;
        
        try {
            conn = open();
            //получение объекта типа PreparedStatement
            ps = conn.prepareStatement(queryUpdate);
            //установка параметров
            ps.setString(1, t.getName());
            ps.setLong(2, t.getId());
            //запрос в БД
            executeUpdate = ps.executeUpdate();

            ps.close();
            close(conn);
        } catch (SQLException ex) {
            try {
                ps.close();
                close(conn);
            } catch (SQLException ex1) {
                Logger.getLogger(QueryCarBrand.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(QueryCarBrand.class.getName()).log(Level.SEVERE, null, ex);
        }
        return executeUpdate>-1;
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
                Logger.getLogger(QueryCarBrand.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(QueryCarBrand.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return executeUpdate>-1;
    }
}
