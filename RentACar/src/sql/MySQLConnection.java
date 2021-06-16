package sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Класс для соединения с БД MySQL
 * @author Admin
 */
public class MySQLConnection {

    private MySQLConnection() {
    }

    private static Connection conn;

    /**
     * Создание соединения с базой данных
     *
     * @param user имя
     * @param password пароль
     * @return соединение к БД
     */
    public static Connection getConnection(String user, String password) {
        try {

            //Driver driver = (Driver) Class.forName("com.mysql.jdbc.Driver").newInstance();

                String host = "localhost";
                String port = "3306";
                //Создание подключения к базе данных
                String url = "jdbc:mysql://" + host + ":" + port + "/car_rental?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=" + TimeZone.getDefault().getID();
                conn = DriverManager.getConnection(url, user, password);

            return conn;

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } 

        return null;
    }

    /**
     * Закрытие соединения с базой данных
     */
    public static void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(MySQLConnection.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Error: close connection");
            }
        }
    }
}
