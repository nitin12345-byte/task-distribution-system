package distributor;

import com.itt.tds.core.model.Node;
import com.itt.tds.distributor.db.exceptions.DBException;
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
    public void TestSaveNode() throws DBException, SQLException {
        lenient().when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        lenient().doNothing().when(preparedStatement).setString(any(int.class), any(String.class));
        lenient().doNothing().when(preparedStatement).setInt(any(int.class), any(int.class));
        Node node = new Node();
        nodeDAO.save(node);
        verify(preparedStatement, atLeast(1)).execute();
    }

    @Test
    public void TestDeleteNode() throws DBException, SQLException {
        when(preparedStatement.execute()).thenReturn(true);
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setString(any(int.class), any(String.class));
        nodeDAO.delete("");
        verify(preparedStatement, atLeast(1)).execute();
    }

    @Test
    public void TestGetAllNode() throws DBException, SQLException {
        lenient().when(resultSet.next()).thenReturn(true).thenReturn(false);
        lenient().when(resultSet.getString("id")).thenReturn("");
        lenient().when(resultSet.getInt("port_number")).thenReturn(40);
        lenient().when(resultSet.getString("ip_address")).thenReturn("192.168.20.142");
        lenient().when(resultSet.getString("name")).thenReturn("nitin.jangid");
        lenient().when(resultSet.getString("status")).thenReturn("AVAILABLE");
        lenient().when(preparedStatement.executeQuery()).thenReturn(resultSet);
        lenient().when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        List<Node> nodeList = nodeDAO.getAllAvailableNodesFor("JAVA");
        Assert.assertEquals(1, nodeList.size());
    }

    @Test
    public void TestGetNode() throws DBException, SQLException {
        lenient().when(resultSet.next()).thenReturn(true).thenReturn(false);
        lenient().when(resultSet.getString("id")).thenReturn("");
        lenient().when(resultSet.getInt("port_number")).thenReturn(40);
        lenient().when(resultSet.getString("ip_address")).thenReturn("192.168.20.142");
        lenient().when(resultSet.getString("name")).thenReturn("nitin.jangid");
        lenient().when(resultSet.getString("status")).thenReturn("available");
        lenient().when(preparedStatement.executeQuery()).thenReturn(resultSet);
        lenient().when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        Node node = nodeDAO.getNode("");
        Assert.assertEquals("", node.getId());

    }

}
