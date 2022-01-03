/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package distributor;

import com.itt.tds.core.model.Client;
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
    public void TestSaveClient() throws SQLException {
        lenient().when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        lenient().doNothing().when(preparedStatement).setString(any(int.class), any(String.class));
        lenient().doNothing().when(preparedStatement).setInt(any(int.class), any(int.class));
        Client client = new Client();
        clientDAO.save(client);
        verify(preparedStatement, atLeast(1)).execute();
    }

    @Test
    public void TestDeleteClient() throws SQLException {
        when(preparedStatement.execute()).thenReturn(true);
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setLong(any(int.class), any(long.class));
        clientDAO.delete(123);
        verify(preparedStatement, atLeast(1)).execute();
    }

    @Test
    public void TestGetAllClient() throws SQLException {
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getLong("id")).thenReturn(12L);
        when(resultSet.getInt("port_number")).thenReturn(40);
        when(resultSet.getString("ip_address")).thenReturn("192.168.20.142");
        when(resultSet.getString("name")).thenReturn("nitin.jangid");
        when(statement.executeQuery(any(String.class))).thenReturn(resultSet);
        when(connection.createStatement()).thenReturn(statement);
        List<Client> clientList = clientDAO.getAllClients();
        Assert.assertEquals(1, clientList.size());
    }

    @Test
    public void TestGetClient() throws SQLException {
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getLong("id")).thenReturn(12L);
        when(resultSet.getInt("port_number")).thenReturn(40);
        when(resultSet.getString("ip_address")).thenReturn("192.168.20.142");
        when(resultSet.getString("name")).thenReturn("nitin.jangid");
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        Client client = clientDAO.getClient(12);
        Assert.assertEquals(12, client.getId());

    }
}
