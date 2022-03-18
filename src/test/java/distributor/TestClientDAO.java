package distributor;

import com.itt.tds.core.model.Client;
import com.itt.tds.distributor.db.exceptions.DBException;
import com.itt.tds.distributor.db.dao.ClientDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TestClientDAO {

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private Statement statement;

    @Mock
    private ResultSet resultSet;

    private ClientDAO clientDAO;

    @Before
    public void inititalizeTaskDAO() {
        clientDAO = new ClientDAO(connection);
    }

    @Test
    public void TestSaveClient() throws DBException, SQLException {
        lenient().when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        lenient().doNothing().when(preparedStatement).setString(any(int.class), any(String.class));
        lenient().doNothing().when(preparedStatement).setInt(any(int.class), any(int.class));
        Client client = new Client();
        clientDAO.save(client);
        verify(preparedStatement, atLeast(1)).execute();
    }

    @Test
    public void TestDeleteClient() throws DBException, SQLException {
        when(preparedStatement.execute()).thenReturn(true);
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setString(any(int.class), any(String.class));
        clientDAO.delete("");
        verify(preparedStatement, atLeast(1)).execute();
    }

    @Test
    public void TestGetAllClient() throws DBException, SQLException {
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        lenient().when(resultSet.getString("id")).thenReturn("1234-zw12-1234-wert");
        lenient().when(resultSet.getInt("port_number")).thenReturn(40);
        lenient().when(resultSet.getString("ip_address")).thenReturn("192.168.20.142");
        lenient().when(resultSet.getString("name")).thenReturn("nitin.jangid");
        lenient().when(statement.executeQuery(any(String.class))).thenReturn(resultSet);
        lenient().when(connection.createStatement()).thenReturn(statement);
        List<Client> clientList = clientDAO.getAllClients();
        Assert.assertEquals(1, clientList.size());
    }

    @Test
    public void TestGetClient() throws DBException, SQLException {
        lenient().when(resultSet.next()).thenReturn(true).thenReturn(false);
        lenient().when(resultSet.getString("id")).thenReturn("1234-zw12-1234-wert");
        lenient().when(resultSet.getInt("port_number")).thenReturn(40);
        lenient().when(resultSet.getString("ip_address")).thenReturn("192.168.20.142");
        lenient().when(resultSet.getString("name")).thenReturn("nitin.jangid");
        lenient().when(preparedStatement.executeQuery()).thenReturn(resultSet);
        lenient().when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        Client client = clientDAO.getClient("1234-zw12-1234-wert");
        Assert.assertEquals("1234-zw12-1234-wert", client.getId());
    }
}
