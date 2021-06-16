package sql;

import data.Client;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import service.Encryption;

/**
 *
 * @author Admin
 */
public class QueryClient extends DBConnection implements Query<Client>{
    private final String queryGetAll = "SELECT client.id, client.first_name, "
            + "client.patronymic, client.last_name, client.phone, client.birthday, "
            + "client.passport_series, client.passport_id FROM client";
    private final String queryAdd = "INSERT INTO client (first_name, patronymic, last_name, phone, birthday, passport_series, passport_id, password) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private final String queryUpdate = "UPDATE client SET first_name = ?, patronymic = ?, last_name = ?, phone = ?, birthday = ?, passport_series = ?, passport_id = ? WHERE id = ?";
    private final String queryUpdatePassword = "UPDATE client SET password = ? WHERE id = ?";
    private final String queryDelete = "DELETE FROM client WHERE id = ?";
    
    private Connection conn;
    
    @Override
    public List<Client> getAll() {
        return getClient(queryGetAll);
    }
    
    /**
     * Получение клиента по id
     * @param id идентификатор
     * @return объект типа Client, в случае отсутствия - null
     */
    public Client getEntityById(long id){
        String query = queryGetAll + " WHERE client.id = " + id;
        List<Client> list = getClient(query);
        if(list.isEmpty()) return null;
        return list.get(0);
    }
    
    private List<Client> getClient(String query){
        List<Client> list = new ArrayList<>();
        Statement stat = null;
        ResultSet rs = null;
        try {
            conn = open();
            //получение объекта типа Statement
            stat = conn.createStatement();
            //получение результирующего объекта типа ResultSet
            rs = stat.executeQuery(query);
            while (rs.next()) {
                //создание новых клиентов, полученных из БД
                
                Client t = new Client(rs.getLong("client.id"), rs.getString("client.first_name"), 
                        rs.getString("client.patronymic"), rs.getString("client.last_name"), 
                        rs.getString("client.phone"),  rs.getDate("client.birthday").toLocalDate(), 
                        rs.getInt("client.passport_series"), rs.getInt("client.passport_id"));
                //добавление в список клиентов
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
                Logger.getLogger(QueryClient.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex1) {
                Logger.getLogger(QueryClient.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return list;
    }

    @Override
    public boolean add(Client t) {       
        PreparedStatement ps = null;
        int executeUpdate = -1;
        try {
            conn = open();
            //получение объекта типа PreparedStatement
            ps = conn.prepareStatement(queryAdd);
            //установка параметров
            ps.setString(1, t.getFirstName());
            ps.setString(2, t.getPatronymic());
            ps.setString(3, t.getLastName());
            ps.setString(4, t.getPhone());           
            ps.setDate(5, Date.valueOf(t.getBirthday()));
            ps.setInt(6, t.getPassportSeries());
            ps.setInt(7, t.getPassport_id());
            ps.setString(8, Encryption.md5HashCoding(t.getPassword(), "MD5"));
            
            //запрос в БД
            executeUpdate = ps.executeUpdate();
            ps.close();
            close(conn);
        } catch (SQLException ex) {
            try {
                ps.close();
                close(conn);
                Logger.getLogger(QueryClient.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex1) {
                Logger.getLogger(QueryClient.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return executeUpdate >-1;
    }

    @Override
    public boolean update(Client t) {
        //обновление авто без изменения фото
        PreparedStatement ps = null;
        int executeUpdate = -1;

        try {
            conn = open();
            //получение объекта типа PreparedStatement
            ps = conn.prepareStatement(queryUpdate);
            //установка параметров
            ps.setString(1, t.getFirstName());
            ps.setString(2, t.getPatronymic());
            ps.setString(3, t.getLastName());
            ps.setString(4, t.getPhone());           
            ps.setDate(5, Date.valueOf(t.getBirthday()));
            ps.setInt(6, t.getPassportSeries());
            ps.setInt(7, t.getPassport_id());
            
            ps.setLong(8, t.getId());
            
            //запрос в БД
            executeUpdate = ps.executeUpdate();

            ps.close();
            close(conn);
        } catch (SQLException ex) {
            try {
                ps.close();
                close(conn);
            } catch (SQLException ex1) {
                Logger.getLogger(QueryClient.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(QueryClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return executeUpdate>-1;
    }
    
    public boolean updatePassword(long id, String password) {
        //обновление авто без изменения фото
        PreparedStatement ps = null;
        int executeUpdate = -1;

        try {
            conn = open();
            //получение объекта типа PreparedStatement
            ps = conn.prepareStatement(queryUpdatePassword);
            //установка параметров
            ps.setString(1, Encryption.md5HashCoding(password, "MD5"));
            ps.setLong(2, id);
            
            //запрос в БД
            executeUpdate = ps.executeUpdate();

            ps.close();
            close(conn);
        } catch (SQLException ex) {
            try {
                ps.close();
                close(conn);
            } catch (SQLException ex1) {
                Logger.getLogger(QueryClient.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(QueryClient.class.getName()).log(Level.SEVERE, null, ex);
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
                Logger.getLogger(QueryClient.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(QueryClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return executeUpdate>-1;
    }
}
