package sql;

import data.Client;
import data.Manager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import service.Encryption;

/**
 * Класс для выполнения входа в систему всех пользователей
 * @author Admin
 */
public class QueryEnterUser extends DBConnection{
    
    private final String queryGetManager = "SELECT manager.id, manager.first_name, "
            + "manager.patronymic, manager.last_name, manager.phone, manager.birthday FROM manager "
            + "WHERE manager.last_name = ? AND manager.password = ?";
    
    private final String queryGetClient = "SELECT client.id, client.first_name, "
            + "client.patronymic, client.last_name, client.phone, client.birthday, "
            + "client.passport_series, client.passport_id FROM client "
            + "WHERE client.last_name = ? AND client.password = ?";
    
    private Connection conn;
    
    /**
     * Вход менеджера в систему
     * @param login логин (фамилия)
     * @param password пароль
     * @return объект типа Manager
     */
    public Manager enterManager(String login, String password){
        PreparedStatement ps = null;
        ResultSet rs = null;
        Manager manager = null;
        //шифрование пароля
        String pass = Encryption.md5HashCoding(password, "MD5");
        
        try {
            conn = open();
            //получение объекта типа Statement
            ps = conn.prepareStatement(queryGetManager);
            ps.setString(1, login);
            ps.setString(2, pass);
            //получение результирующего объекта типа ResultSet
            rs = ps.executeQuery();
            
            if(rs.next()){
                manager = new Manager(rs.getLong("manager.id"), rs.getString("manager.first_name"), 
                        rs.getString("manager.patronymic"), rs.getString("manager.last_name"), 
                        rs.getString("manager.phone"),rs.getDate("manager.birthday").toLocalDate());
            }
            rs.close();
            ps.close();
            close(conn);
        } catch (SQLException ex) {
            try {
                rs.close();
                ps.close();
                close(conn);
                Logger.getLogger(QueryEnterUser.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex1) {
                Logger.getLogger(QueryEnterUser.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return manager;
    }
    
    /**
     * Вход клиента в систему
     * @param login логин (фамилия)
     * @param password пароль
     * @return объект типа Client
     */
    public Client enterClient(String login, String password){
        PreparedStatement ps = null;
        ResultSet rs = null;
        Client client = null;
        //шифрование пароля
        String pass = Encryption.md5HashCoding(password, "MD5");
        
        try {
            conn = open();
            //получение объекта типа Statement
            ps = conn.prepareStatement(queryGetClient);
            ps.setString(1, login);
            ps.setString(2, pass);
            //получение результирующего объекта типа ResultSet
            rs = ps.executeQuery();
            
            if(rs.next()){
                client = new Client(rs.getLong("client.id"), rs.getString("client.first_name"), 
                        rs.getString("client.patronymic"), rs.getString("client.last_name"), 
                        rs.getString("client.phone"), rs.getDate("client.birthday").toLocalDate(), 
                        rs.getInt("client.passport_series"), rs.getInt("client.passport_id"));
            }
            rs.close();
            ps.close();
            close(conn);
        } catch (SQLException ex) {
            try {
                rs.close();
                ps.close();
                close(conn);
                Logger.getLogger(QueryEnterUser.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex1) {
                Logger.getLogger(QueryEnterUser.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return client;
    }
    
}
