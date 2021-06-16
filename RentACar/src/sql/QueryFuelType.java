package sql;

import data.FuelType;
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
public class QueryFuelType extends DBConnection implements Query<FuelType>{
    private final String queryGetAll = "SELECT fuel_type.id, fuel_type.name FROM fuel_type";
    private final String queryAdd = "INSERT INTO fuel_type (name) VALUES (?)";
    private final String queryUpdate = "UPDATE fuel_type SET name = ? WHERE id = ?";
    private final String queryDelete = "DELETE FROM fuel_type WHERE id = ?";
    
    private Connection conn;
    
    @Override
    public List<FuelType> getAll() {
        List<FuelType> list = new ArrayList<>();
        Statement stat = null;
        ResultSet rs = null;
        try {
            conn = open();
            //получение объекта типа Statement
            stat = conn.createStatement();
            //получение результирующего объекта типа ResultSet
            rs = stat.executeQuery(queryGetAll);
            while (rs.next()) {
                //создание нового типа топлива, полученных из БД
                FuelType t = new FuelType(rs.getLong("id"), rs.getString("name"));
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
                Logger.getLogger(QueryFuelType.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex1) {
                Logger.getLogger(QueryFuelType.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return list;
    }

    @Override
    public boolean add(FuelType t) {       
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
                Logger.getLogger(QueryFuelType.class.getName()).log(Level.SEVERE, null, ex);
                return executeUpdate >-1;
            } catch (SQLException ex1) {
                Logger.getLogger(QueryFuelType.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return executeUpdate >-1;
    }

    @Override
    public boolean update(FuelType t) {
        
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
                return executeUpdate >-1;
            } catch (SQLException ex1) {
                Logger.getLogger(QueryFuelType.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(QueryFuelType.class.getName()).log(Level.SEVERE, null, ex);
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
                return executeUpdate >-1;
            } catch (SQLException ex1) {
                Logger.getLogger(QueryFuelType.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(QueryFuelType.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return executeUpdate>-1;
    }
}
