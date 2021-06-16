package sql;

import data.Rental;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Admin
 */
public class QueryRental extends DBConnection implements Query<Rental> {

    private final String queryGetAll = "SELECT rental.id, rental.reservation_id, rental.price, rental.total_price, rental.status, rental.date_receiving, rental.date_return FROM car_rental.rental";
    private final String queryAdd = "INSERT INTO rental (reservation_id, price, total_price, status, date_receiving, date_return) VALUES (?, ?, ?, ?)";
    private final String queryAddRental = "INSERT INTO rental (reservation_id, price, status, date_receiving, date_return) VALUES (?, ?, ?, ?, ?)";
    private final String queryUpdateTotalSum = "UPDATE rental SET total_price = ? WHERE id = ?";
    private final String queryUpdate = "UPDATE rental SET reservation_id = ?, price = ?, total_price = ?, status = ?, date_receiving = ?, date_return = ? WHERE id = ?";
    private final String queryUpdateReservationStatus = "UPDATE reservation SET status_reservation = ? WHERE id = ?";
    private final String queryClose = "UPDATE rental SET status = ?, total_price = ?, date_return = ? WHERE id = ?";
    private final String queryUpdateCarStatus = "UPDATE car SET status = ? WHERE id = ?";
    private final String queryDelete = "DELETE FROM rental WHERE id = ?";

    private Connection conn;

    @Override
    public List<Rental> getAll() {
        return getRental(queryGetAll);
    }

    /**
     * Получение проката по id
     *
     * @param id идентификатор
     * @return объект типа Rental, в случае отсутствия - null
     */
    public Rental getEntityById(long id) {
        String query = queryGetAll + " WHERE rental.id = " + id;
        List<Rental> list = getRental(query);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    /**
     * Получение всех прокатов относительно клиента и статуса
     *
     * @param clientId идентификатор клиента Если clientId = 0, то выбираем по
     * всем клиентам
     * @param status статус брони. Если статус равен 0, то выборка пройдет без
     * учета статуса.
     * @return список прокатов
     */
    public List<Rental> getRentalByClientAndStatus(long clientId, int status) {
        String query = queryGetAll + " INNER JOIN reservation ON rental.reservation_id = reservation.id "
                + "INNER JOIN client ON reservation.client_id = client.id" + " WHERE client.id = " + clientId + " AND status = " + status;
        if (clientId <= 0) {
            query = queryGetAll + " WHERE status = " + status;
        }

        if (status <= 0) {
            query = queryGetAll + " INNER JOIN reservation ON rental.reservation_id = reservation.id "
                    + "INNER JOIN client ON reservation.client_id = client.id" + " WHERE client.id = " + clientId;
        }

        if (clientId <= 0 && status <= 0) {
            query = queryGetAll;
        }

        return getRental(query);
    }

    private List<Rental> getRental(String query) {
        List<Rental> list = new ArrayList<>();
        Statement stat = null;
        ResultSet rs = null;
        System.out.println("getRental = " + query);
        try {
            conn = open();
            //получение объекта типа Statement
            stat = conn.createStatement();
            //получение результирующего объекта типа ResultSet
            rs = stat.executeQuery(query);
            while (rs.next()) {
                //создание нового проката, полученного из БД
                Date dateReturn = rs.getDate("date_return");
                LocalDate ldReturn;
                if(dateReturn == null){
                    ldReturn = null;
                } else {
                    ldReturn = dateReturn.toLocalDate();
                }
                Rental t = new Rental(rs.getLong("id"), rs.getLong("reservation_id"), 
                        rs.getFloat("price"), rs.getFloat("total_price"), rs.getInt("status"), 
                        rs.getDate("date_receiving").toLocalDate(), ldReturn);
                //добавление в список авто
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
                Logger.getLogger(QueryRental.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex1) {
                Logger.getLogger(QueryRental.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return list;
    }

    @Override
    public boolean add(Rental t) {
        return addRental(queryAdd, t, 0, 0);
    }
    
    private boolean addRental(String query, Rental t, long carId, long reservationId){
        boolean executeAdd = false;
        PreparedStatement ps_rental = null;
        PreparedStatement ps_car_status = null;
        PreparedStatement ps_reservation_status = null;

        try {
            conn = open();
            conn.setAutoCommit(false);
            //получение объекта типа PreparedStatement
            ps_rental = conn.prepareStatement(query);
            //установка параметров
            ps_rental.setLong(1, t.getReservationId());
            ps_rental.setFloat(2, t.getPrice());
            ps_rental.setInt(3, t.getRentalStatus().getStatus());
            ps_rental.setDate(4, Date.valueOf(t.getDateReceiving()));
            if(t.getDateReturn() != null){
            ps_rental.setDate(5, Date.valueOf(t.getDateReturn()));
            } else {
                ps_rental.setNull(5, Types.NULL);
            }
            //если идентификатор задан, то
            if (carId > 0) {
                //изменение статуса авто на "занят"
                ps_car_status = conn.prepareStatement(queryUpdateCarStatus);
                ps_car_status.setInt(1, 3);
                ps_car_status.setLong(2, carId);

                //запрос в БД
                ps_car_status.executeUpdate();
            }
            
            if(reservationId > 0){
                ps_reservation_status = conn.prepareStatement(queryUpdateReservationStatus);
                ps_reservation_status.setInt(1, 4);
                ps_reservation_status.setLong(2, reservationId);
                
                //запрос в БД
                ps_reservation_status.executeUpdate();
            }

            ps_rental.executeUpdate();
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
                    ps_rental.close();
                    if (ps_car_status != null) {
                        ps_car_status.close();
                    }
                    if (ps_reservation_status != null) {
                        ps_reservation_status.close();
                    }
                    close(conn);
                } catch (SQLException ex1) {
                    Logger.getLogger(QueryReservation.class.getName()).log(Level.SEVERE, null, ex1);

                }
            }
        }
        return executeAdd;
    }

    /**
     * Добавление нового проката, изменение статуса авто на "занят" и изменение статуса бронирования на "обработан"
     *
     * @param t прокат
     * @param carId идентификатор авто. Если carId = 0, то статус авто не
     * изменяется
     * @param reservationId идентификатор бронирования. Если reservationId = 0, то статус бронирования не
     * изменяется
     * @return true - в случае успешного добавления, инача - false
     */
    public boolean addAndSetCarBusyReservationProcessed(Rental t, long carId, long reservationId) {
        return addRental(queryAddRental, t, carId, reservationId);
    }
    
    /**
     * Изменение иготовой оплаты за прокат авто
     * @param rentalId идентификатор проката
     * @param sum сумма к оплате
     * @return true - в случае успешного добавления, инача - false
     */
    public boolean updateTotalSum(long rentalId, float sum){
        //обновление авто без изменения фото
        PreparedStatement ps = null;
        int executeUpdate = -1;

        try {
            conn = open();
            //получение объекта типа PreparedStatement
            ps = conn.prepareStatement(queryUpdateTotalSum);
            //установка параметров
            ps.setFloat(1, sum);
            ps.setLong(2, rentalId);

            //запрос в БД
            executeUpdate = ps.executeUpdate();
            ps.close();
            close(conn);
        } catch (SQLException ex) {
            try {
                ps.close();
                close(conn);
            } catch (SQLException ex1) {
                Logger.getLogger(QueryRental.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(QueryRental.class.getName()).log(Level.SEVERE, null, ex);
        }
        return executeUpdate > -1;
    }

    @Override
    public boolean update(Rental t) {
        //обновление авто без изменения фото
        PreparedStatement ps = null;
        int executeUpdate = -1;

        try {
            conn = open();
            //получение объекта типа PreparedStatement
            ps = conn.prepareStatement(queryUpdate);
            //установка параметров
            ps.setLong(1, t.getReservationId());
            ps.setFloat(2, t.getPrice());
            ps.setFloat(3, t.getTotalPrice());
            ps.setInt(4, t.getRentalStatus().getStatus());
            ps.setDate(5, Date.valueOf(t.getDateReceiving()));
            ps.setDate(6, Date.valueOf(t.getDateReturn()));
            ps.setLong(7, t.getId());

            //запрос в БД
            executeUpdate = ps.executeUpdate();
            ps.close();
            close(conn);
        } catch (SQLException ex) {
            try {
                ps.close();
                close(conn);
            } catch (SQLException ex1) {
                Logger.getLogger(QueryRental.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(QueryRental.class.getName()).log(Level.SEVERE, null, ex);
        }
        return executeUpdate > -1;
    }

    /**
     * Метод закрытия аренды
     *
     * @param rental объект аренды
     * @param carId идентификатор авто
     * @return true - в случае успешного добавления, инача - false
     */
    public boolean closeRental(Rental rental, long carId) {
        boolean executeClose = false;
        PreparedStatement ps_rental = null;
        PreparedStatement ps_car = null;
        try {
            conn = open();
            conn.setAutoCommit(false);
            ps_rental = conn.prepareStatement(queryClose);
            ps_rental.setInt(1, rental.getRentalStatus().getStatus());
            ps_rental.setFloat(2, rental.getTotalPrice());
            ps_rental.setDate(3, Date.valueOf(rental.getDateReturn()));
            ps_rental.setLong(4, rental.getId());

            ps_car = conn.prepareStatement(queryUpdateCarStatus);
            ps_car.setInt(1, 1);
            ps_car.setLong(2, carId);

            //запрос в БД
            ps_rental.executeUpdate();
            ps_car.executeUpdate();
            conn.commit();
            executeClose = true;
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
                ps_rental.close();
                ps_car.close();
                close(conn);
            } catch (SQLException ex) {
                Logger.getLogger(QueryReservation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return executeClose;
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
}
