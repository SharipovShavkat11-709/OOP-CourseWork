package sql;

import data.Client;
import data.Manager;
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

/**
 *
 * @author Admin
 */
public class QueryManager extends DBConnection implements Query<Manager> {

    private final String queryGetAll = "SELECT manager.id, manager.first_name, "
            + "manager.patronymic, manager.last_name, client.phone, client.birthday, "
            + "client.passport_series, client.passport_id FROM client";
    private final String queryAdd = "INSERT INTO manager (first_name, patronymic, last_name, phone, birthday) VALUES (?, ?, ?, ?, ?)";
    private final String queryUpdate = "UPDATE manager SET first_name = ?, patronymic = ?, last_name = ?, phone = ?, birthday = ? WHERE id = ?";
    private final String queryDelete = "DELETE FROM manager WHERE id = ?";

    private Connection conn;

    @Override
    public List<Manager> getAll() {
        List<Manager> list = new ArrayList<>();
        Statement stat = null;
        ResultSet rs = null;
        try {
            conn = open();
            //получение объекта типа Statement
            stat = conn.createStatement();
            //получение результирующего объекта типа ResultSet
            rs = stat.executeQuery(queryGetAll);
            while (rs.next()) {
                //создание новых менеджеров, полученных из БД
                Manager t = new Manager(rs.getLong("manager.id"), rs.getString("manager.first_name"),
                        rs.getString("manager.patronymic"), rs.getString("manager.last_name"),
                        rs.getString("manager.phone"), rs.getDate("client.birthday").toLocalDate());
                //добавление в список менеджеров
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
                Logger.getLogger(QueryManager.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex1) {
                Logger.getLogger(QueryManager.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return list;
    }

    @Override
    public boolean add(Manager t) {
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

            //запрос в БД
            executeUpdate = ps.executeUpdate();
            ps.close();
            close(conn);
        } catch (SQLException ex) {
            try {
                ps.close();
                close(conn);
                Logger.getLogger(QueryManager.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex1) {
                Logger.getLogger(QueryManager.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return executeUpdate > -1;
    }

    @Override
    public boolean update(Manager t) {
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

            ps.setLong(6, t.getId());

            //запрос в БД
            executeUpdate = ps.executeUpdate();

            ps.close();
            close(conn);
        } catch (SQLException ex) {
            try {
                ps.close();
                close(conn);
            } catch (SQLException ex1) {
                Logger.getLogger(QueryManager.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(QueryManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return executeUpdate > -1;
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
                Logger.getLogger(QueryManager.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(QueryManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return executeUpdate > -1;
    }

}
