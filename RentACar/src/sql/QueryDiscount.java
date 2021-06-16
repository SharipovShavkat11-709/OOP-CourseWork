package sql;

import data.Discount;
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
public class QueryDiscount extends DBConnection implements Query<Discount>{
    private final String queryGetAll = "SELECT discount.id, discount.days, discount.percent FROM discount";
    private final String queryGetPercentByDays = "SELECT discount.percent FROM discount WHERE discount.days <= ? ORDER BY days DESC LIMIT 1";
    private final String queryAdd = "INSERT INTO discount (days, percent) VALUES (?, ?)";
    private final String queryUpdate = "UPDATE discount SET days = ?, percent = ? WHERE id = ?";
    private final String queryDelete = "DELETE FROM discount WHERE id = ?";
    
    private Connection conn;
    
    @Override
    public List<Discount> getAll() {
        List<Discount> list = new ArrayList<>();
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
                Discount t = new Discount(rs.getLong("id"), rs.getInt("days"), rs.getFloat("percent"));
                //добавление в список скидок
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
                Logger.getLogger(QueryDiscount.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex1) {
                Logger.getLogger(QueryDiscount.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return list;
    }
    
    /**
     * Метод вычисления размера скидки в процентах
     * @param days количество дней аренды авто
     * @return количество процентов скидки, в случае ошибки - -1
     */
    public float getPercentByDays(int days){
        PreparedStatement stat = null;
        ResultSet rs = null;
        float percent = 0;
        try {
            conn = open();
            //получение объекта типа Statement
            stat = conn.prepareStatement(queryGetPercentByDays);
            stat.setInt(1, days);
            //получение результирующего объекта типа ResultSet
            rs = stat.executeQuery();
            boolean next = rs.next();           
            if(next){
                percent = rs.getFloat("percent");
            }
            rs.close();
            stat.close();  
            close(conn);
        } catch (SQLException ex) {
            try {
                rs.close();
                stat.close();
                close(conn);
                Logger.getLogger(QueryDiscount.class.getName()).log(Level.SEVERE, null, ex);
                return -1;
            } catch (SQLException ex1) {
                Logger.getLogger(QueryDiscount.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return percent;
    }

    @Override
    public boolean add(Discount t) {       
        PreparedStatement ps = null;
        int executeUpdate = -1;
        try {
            conn = open();
            //получение объекта типа PreparedStatement
            ps = conn.prepareStatement(queryAdd);
            //установка параметров
            ps.setInt(1, t.getDays());
            ps.setFloat(2, t.getPercent());
            
            //запрос в БД
            executeUpdate = ps.executeUpdate();
            ps.close();
            close(conn);
        } catch (SQLException ex) {
            try {
                ps.close();
                close(conn);
                Logger.getLogger(QueryDiscount.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex1) {
                Logger.getLogger(QueryDiscount.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return executeUpdate >-1;
    }

    @Override
    public boolean update(Discount t) {
        
        PreparedStatement ps = null;
        int executeUpdate = -1;
        
        try {
            conn = open();
            //получение объекта типа PreparedStatement
            ps = conn.prepareStatement(queryUpdate);
            //установка параметров
            ps.setInt(1, t.getDays());
            ps.setFloat(2, t.getPercent());
            ps.setLong(3, t.getId());
            //запрос в БД
            executeUpdate = ps.executeUpdate();

            ps.close();
            close(conn);
        } catch (SQLException ex) {
            try {
                ps.close();
                close(conn);
            } catch (SQLException ex1) {
                Logger.getLogger(QueryDiscount.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(QueryDiscount.class.getName()).log(Level.SEVERE, null, ex);
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
                Logger.getLogger(QueryDiscount.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(QueryDiscount.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return executeUpdate>-1;
    }
}
