package distributor;

import com.itt.tds.distributor.db.DBManager;
import java.sql.Connection;
import java.sql.SQLException;
import org.junit.Assert;
import org.junit.Test;

public class TestDBManager {

    @Test
    public void TestCreateConnection() throws SQLException, ClassNotFoundException {
        DBManager dbManager = DBManager.getInstance();
        Connection connection = dbManager.createConnection();
        Assert.assertNotNull(connection);
    }

    @Test
    public void TestCloseConnection() throws SQLException, ClassNotFoundException {
        DBManager dbManager = DBManager.getInstance();
        Connection connection = dbManager.createConnection();
        dbManager.closeConnection();
        Assert.assertEquals(true, connection.isClosed());
    }

}
