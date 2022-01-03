package distributor;

import com.itt.tds.core.model.Node;
import com.itt.tds.distributor.db.dao.NodeDAO;
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
public class TestNodeDAO {

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private Statement statement;

    @Mock
    private ResultSet resultSet;

    private NodeDAO nodeDAO;

    @Before
    public void inititalizeTaskDAO() {
        nodeDAO = new NodeDAO(connection);
    }

    @Test
    public void TestSaveNode() throws SQLException {
        lenient().when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        lenient().doNothing().when(preparedStatement).setString(any(int.class), any(String.class));
        lenient().doNothing().when(preparedStatement).setInt(any(int.class), any(int.class));
        Node node = new Node();
        nodeDAO.save(node);
        verify(preparedStatement, atLeast(1)).execute();
    }

    @Test
    public void TestDeleteNode() throws SQLException {
        when(preparedStatement.execute()).thenReturn(true);
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setLong(any(int.class), any(long.class));
        nodeDAO.delete(123);
        verify(preparedStatement, atLeast(1)).execute();
    }

    @Test
    public void TestGetAllNode() throws SQLException {
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getLong("id")).thenReturn(12L);
        when(resultSet.getInt("port_number")).thenReturn(40);
        when(resultSet.getString("ip_address")).thenReturn("192.168.20.142");
        when(resultSet.getString("name")).thenReturn("nitin.jangid");
        when(resultSet.getString("status")).thenReturn("available");
        when(statement.executeQuery(any(String.class))).thenReturn(resultSet);
        when(connection.createStatement()).thenReturn(statement);
        List<Node> nodeList = nodeDAO.getAllNodes();
        Assert.assertEquals(1, nodeList.size());
    }

    @Test
    public void TestGetNode() throws SQLException {
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getLong("id")).thenReturn(12L);
        when(resultSet.getInt("port_number")).thenReturn(40);
        when(resultSet.getString("ip_address")).thenReturn("192.168.20.142");
        when(resultSet.getString("name")).thenReturn("nitin.jangid");
        when(resultSet.getString("status")).thenReturn("available");
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        Node node = nodeDAO.getNode(12);
        Assert.assertEquals(12, node.getId());

    }

}
