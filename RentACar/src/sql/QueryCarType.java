package sql;

import data.CarType;
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
public class QueryCarType extends DBConnection implements Query<CarType>{
    
    private final String queryGetAll = "SELECT car_type.id, car_type.name FROM car_type";
    private final String queryAdd = "INSERT INTO car_type (name) VALUES (?)";
    private final String queryUpdate = "UPDATE car_type SET name = ? WHERE id = ?";
    private final String queryDelete = "DELETE FROM car_type WHERE id = ?";
    
    private Connection conn;
    
    @Override
    public List<CarType> getAll() {
        List<CarType> list = new ArrayList<>();
        Statement stat = null;
        ResultSet rs = null;
        try {
            conn = open();
            //получение объекта типа Statement
            stat = conn.createStatement();
            //получение результирующего объекта типа ResultSet
            rs = stat.executeQuery(queryGetAll);
            while (rs.next()) {
                //создание нового типа авто, полученных из БД
                CarType t = new CarType(rs.getLong("id"), rs.getString("name"));
                //добавление в список типов авто
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
                Logger.getLogger(QueryCarType.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex1) {
                Logger.getLogger(QueryCarType.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return list;
    }

    @Override
    public boolean add(CarType t) {       
        PreparedStatement ps = null;
        int executeUpdate = -1;
        try {
            conn = open();
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
                Logger.getLogger(QueryCarType.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex1) {
                Logger.getLogger(QueryCarType.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return executeUpdate >-1;
    }

    @Override
    public boolean update(CarType t) {
        
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
                Logger.getLogger(QueryCarType.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(QueryCarType.class.getName()).log(Level.SEVERE, null, ex);
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
                Logger.getLogger(QueryCarType.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(QueryCarType.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return executeUpdate>-1;
    }
    
}
