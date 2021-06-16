package sql;

import data.Reservation;
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
public class QueryReservation extends DBConnection implements Query<Reservation> {

    private final String queryGetAll = "SELECT id, client_id, car_id, car_center_receiving_id, car_center_return_id, "
            + "date_application, date_receiving, date_return, status_reservation, discount_percent, payment "
            + "FROM car_rental.reservation";

    private final String queryAdd = "INSERT INTO reservation (client_id, car_id, car_center_receiving_id, car_center_return_id, date_application, date_receiving, date_return, status_reservation, discount_percent, payment) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private final String queryUpdate = "UPDATE reservation SET client_id = ?, car_id = ?, car_center_receiving_id = ?, car_center_return_id = ?, date_application = ?, date_receiving = ?, date_return = ?, status_reservation = ?, discount_percent = ?, payment = ? WHERE id = ?";
    private final String queryUpdateStatus = "UPDATE reservation SET reservation.status_reservation = ? WHERE id = ?";
    private final String queryUpdateCarStatus = "UPDATE car SET car.status = ? WHERE id = ?";
    private final String queryUpdateCarCenter = "UPDATE car SET car.car_center_id = ? WHERE id = ?";
    private final String queryDelete = "DELETE FROM reservation WHERE id = ?";

    private Connection conn;

    @Override
    public List<Reservation> getAll() {
        return getReservation(queryGetAll);
    }

    /**
     * Получение всех броней относительно статуса
     *
     * @param client_id идентификатор клиента Если client_id = 0, то выбираем по
     * всем клиентам
     * @param status статус брони. Если статус равен 0, то выборка пройдет без
     * учета статуса.
     * @return список броней
     */
    public List<Reservation> getReservationByClientAndStatus(long client_id, int status) {
        String query = queryGetAll + " WHERE reservation.client_id = " + client_id + " AND reservation.status_reservation = " + status;
        if (status == 0) {
            query = queryGetAll + " WHERE reservation.client_id = " + client_id;
        }

        if (client_id == 0) {
            query = queryGetAll + " WHERE reservation.status_reservation = " + status;
        }

        if (status == 0 && client_id == 0) {
            query = queryGetAll;
        }

        return getReservation(query);
    }
    
    /**
     * Получение объекта бронирования по id
     * @param id идентификатор бронирования
     * @return объект типа Reservation, если нет - null
     */
    public Reservation getEntityById(long id) {
        String query = queryGetAll + " WHERE id = " + id;
        List<Reservation> list = getReservation(query);
        if(list.isEmpty()) return null;
        return list.get(0);
    }

    private List<Reservation> getReservation(String query) {
        List<Reservation> list = new ArrayList<>();
        Statement stat = null;
        ResultSet rs = null;
        System.out.println("query = " + query);
        try {
            conn = open();
            //получение объекта типа Statement
            stat = conn.createStatement();
            //получение результирующего объекта типа ResultSet
            rs = stat.executeQuery(query);
            while (rs.next()) {
                //создание новой брони, полученных из БД
                Reservation t = new Reservation(rs.getLong("id"), rs.getLong("client_id"), rs.getLong("car_id"),
                        rs.getLong("car_center_receiving_id"), rs.getLong("car_center_return_id"),
                        rs.getDate("date_application").toLocalDate(), rs.getDate("date_receiving").toLocalDate(), rs.getDate("date_return").toLocalDate(),
                        rs.getInt("status_reservation"), rs.getFloat("discount_percent"), rs.getFloat("payment"));
                //добавление в список броней
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
                Logger.getLogger(QueryReservation.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex1) {
                Logger.getLogger(QueryReservation.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return list;
    }

    @Override
    public boolean add(Reservation t) {
        boolean executeAdd = false;
        PreparedStatement ps_reservation = null;
        PreparedStatement ps_car_status = null;

        try {
            conn = open();
            conn.setAutoCommit(false);
            //получение объекта типа PreparedStatement
            ps_reservation = conn.prepareStatement(queryAdd);
            //установка параметров
            ps_reservation.setLong(1, t.getClient_id());
            ps_reservation.setLong(2, t.getCar_id());
            ps_reservation.setLong(3, t.getCar_center_receiving_id());
            ps_reservation.setLong(4, t.getCar_center_return_id());
            ps_reservation.setDate(5, Date.valueOf(t.getDate_application()));           
            ps_reservation.setDate(6, Date.valueOf(t.getDate_receiving()));
            ps_reservation.setDate(7, Date.valueOf(t.getDate_return()));
            System.out.println("Date.valueOf(t.getDate_application()) = " + Date.valueOf(t.getDate_application()));
            System.out.println("Date.valueOf(t.getDate_receiving()) = " + Date.valueOf(t.getDate_receiving()));
            System.out.println("Date.valueOf(t.getDate_return()) = " + Date.valueOf(t.getDate_return()));
            ps_reservation.setInt(8, t.getReservationStatus());
            ps_reservation.setFloat(9, t.getDiscountPercent());
            ps_reservation.setFloat(10, t.getPayment());

            //изменение статуса авто на забронированное
            ps_car_status = conn.prepareStatement(queryUpdateCarStatus);
            ps_car_status.setInt(1, 2);
            ps_car_status.setLong(2, t.getCar_id());

            //запрос в БД
            ps_reservation.executeUpdate();
            ps_car_status.executeUpdate();

            conn.commit();
            executeAdd = true;

        } catch (SQLException ex) {
            try {
                conn.rollback();
                Logger.getLogger(QueryReservation.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex1) {
                Logger.getLogger(QueryReservation.class.getName()).log(Level.SEVERE, null, ex1);

            } finally {
                try {
                    conn.setAutoCommit(true);
                    ps_reservation.close();
                    ps_car_status.close();
                    close(conn);
                } catch (SQLException ex1) {
                    Logger.getLogger(QueryReservation.class.getName()).log(Level.SEVERE, null, ex1);

                }
            }
        }
        return executeAdd;
    }

    @Override
    public boolean update(Reservation t) {
        PreparedStatement ps = null;
        int executeUpdate = -1;

        try {
            conn = open();
            ps = conn.prepareStatement(queryUpdate);

            //установка параметров
            ps.setLong(1, t.getClient_id());
            ps.setLong(2, t.getCar_id());
            ps.setLong(3, t.getCar_center_receiving_id());
            ps.setLong(4, t.getCar_center_return_id());
            ps.setDate(5, Date.valueOf(t.getDate_application()));
            ps.setDate(6, Date.valueOf(t.getDate_receiving()));
            ps.setDate(7, Date.valueOf(t.getDate_return()));
            ps.setInt(8, t.getReservationStatus());
            ps.setFloat(9, t.getDiscountPercent());
            ps.setFloat(10, t.getPayment());

            ps.setLong(11, t.getId());

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
                Logger.getLogger(QueryCarCenter.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(QueryCarCenter.class.getName()).log(Level.SEVERE, null, ex);
        }

        return executeUpdate > -1;
    }

    /**
     * Отмена бронирования
     *
     * @param reservation_id идентификатор бронирования
     * @param car_id идентификатор авто
     * @return true - в случае успешной отмены, иначе - false
     */
    public boolean cancel(long reservation_id, long car_id) {
        boolean executeCancel = false;
        PreparedStatement ps_reservation = null;
        PreparedStatement ps_car = null;
        try {
            conn = open();
            conn.setAutoCommit(false);
            ps_reservation = conn.prepareStatement(queryDelete);
            ps_reservation.setLong(1, reservation_id);

            ps_car = conn.prepareStatement(queryUpdateCarStatus);
            ps_car.setInt(1, 1);
            ps_car.setLong(2, car_id);

            //запрос в БД
            ps_reservation.executeUpdate();
            ps_car.executeUpdate();
            conn.commit();
            executeCancel = true;
        } catch (SQLException ex) {

            try {
                conn.rollback();

            } catch (SQLException ex1) {
                Logger.getLogger(QueryCarCenter.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(QueryCarCenter.class.getName()).log(Level.SEVERE, null, ex);

        } finally {
            try {
                conn.setAutoCommit(true);
                ps_reservation.close();
                ps_car.close();
                close(conn);
            } catch (SQLException ex) {
                Logger.getLogger(QueryReservation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return executeCancel;
    }

    /**
     * Изменение статуса бронирования
     *
     * @param reservationId идентификатор бронирования
     * @param reservationStatus статус
     * @param carId идентификатор авто. Если carId = 0, то статус авто не
     * изменяется
     * @param carStatus статус авто
     * @param carCarCenter идентификатор центра нахождения авто. Если carCarCenter = 0, то центра нахождения авто не изменяется
     * @return true - в случае успеха, иначе - false
     */
    public boolean updateStatus(long reservationId, int reservationStatus, long carId, int carStatus, long carCarCenter) {
        boolean executeUpdate = false;
        PreparedStatement ps_reservation_status = null;
        PreparedStatement ps_car_status = null;
        PreparedStatement ps_car_car_center = null;

        try {
            conn = open();
            conn.setAutoCommit(false);
            //получение объекта типа PreparedStatement
            ps_reservation_status = conn.prepareStatement(queryUpdateStatus);
            //установка параметров
            ps_reservation_status.setInt(1, reservationStatus);
            ps_reservation_status.setLong(2, reservationId);

            if (carId > 0 && carStatus > 0) {
                //изменение статуса авто
                ps_car_status = conn.prepareStatement(queryUpdateCarStatus);
                ps_car_status.setInt(1, carStatus);
                ps_car_status.setLong(2, carId);

                ps_car_status.executeUpdate();
            }
            
            if (carId > 0 && carCarCenter > 0) {
                //изменение центра нахлждения авто
                ps_car_car_center = conn.prepareStatement(queryUpdateCarCenter);
                ps_car_car_center.setLong(1, carCarCenter);
                ps_car_car_center.setLong(2, carId);

                ps_car_car_center.executeUpdate();
            }
            
            //запрос в БД
            ps_reservation_status.executeUpdate();

            conn.commit();
            executeUpdate = true;

        } catch (SQLException ex) {
            try {
                conn.rollback();
                Logger.getLogger(QueryReservation.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex1) {
                Logger.getLogger(QueryReservation.class.getName()).log(Level.SEVERE, null, ex1);

            } finally {
                try {
                    conn.setAutoCommit(true);
                    ps_reservation_status.close();
                    if (ps_car_status != null) {
                        ps_car_status.close();
                    }
                    if(ps_car_car_center != null){
                        ps_car_car_center.close();
                    }
                    close(conn);
                } catch (SQLException ex1) {
                    Logger.getLogger(QueryReservation.class.getName()).log(Level.SEVERE, null, ex1);

                }
            }
        }
        return executeUpdate;
    }
}
