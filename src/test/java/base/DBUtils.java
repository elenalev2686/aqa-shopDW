package base;


import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DBUtils {
    private static String url = System.getProperty("db.url");
    private static String appURL = System.getProperty("app.url");
    private static String appPORT = System.getProperty("app.port");
    private static String userDB = System.getProperty("app.userDB");
    private static String password = System.getProperty("app.password");
    private static final QueryRunner runner = new QueryRunner();
    private DBUtils() {
    }
    private static Connection getConn() throws SQLException {
        return DriverManager.getConnection(System.getProperty("db.url"), "app", "pass");
    }
    @SneakyThrows
    public static void clearAllData()  {
        var connection = getConn();
        runner.execute(connection, "DELETE FROM credit_request_entity;");
        runner.execute(connection, "DELETE FROM payment_entity;");
        runner.execute(connection, "DELETE FROM order_entity;");
    }
    @SneakyThrows
    public static void getPaymentStatus(Status status) {
        var paymentDataSQL = "SELECT status FROM payment_entity;";
        var connection = getConn();
        var payment = runner.query(connection, paymentDataSQL, new BeanHandler<>(PaymentModel.class));

        assertEquals (status, payment.status);
    }

    @SneakyThrows
    public static void getCreditStatus(Status status) {
        var creditDataSQL = "SELECT status FROM credit_request_entity;";
        var connection = getConn();
        var credit = runner.query(connection, creditDataSQL, new BeanHandler<>(CreditModel.class));

        assertEquals(status, credit.status);
    }
}