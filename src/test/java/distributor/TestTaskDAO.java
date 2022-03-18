package distributor;

import com.itt.tds.core.model.Task;
import com.itt.tds.distributor.db.exceptions.DBException;
import com.itt.tds.distributor.db.dao.TaskDAO;
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
public class TestTaskDAO {

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private Statement statement;

    @Mock
    private ResultSet resultSet;

    private TaskDAO taskDAO;

    @Before
    public void inititalizeTaskDAO() {
        taskDAO = new TaskDAO(connection);
    }

    @Test
    public void TestSaveTask() throws DBException, SQLException {
        when(preparedStatement.execute()).thenReturn(true);
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setString(any(int.class), any(String.class));
        Task task = new Task();
        taskDAO.save(task);
        verify(preparedStatement, atLeast(1)).execute();
    }

    @Test
    public void TestDeleteTask() throws DBException, SQLException {
        when(preparedStatement.execute()).thenReturn(true);
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setString(any(int.class), any(String.class));
        taskDAO.delete("");
        verify(preparedStatement, atLeast(1)).execute();
    }

    @Test
    public void TestGetAllTask() throws DBException, SQLException {
        lenient().when(resultSet.next()).thenReturn(true).thenReturn(false);
        lenient().when(resultSet.getString("id")).thenReturn("");
        lenient().when(resultSet.getString("file_path")).thenReturn("c://myfile.txt");
        lenient().when(resultSet.getString("node_id")).thenReturn("");
        lenient().when(resultSet.getString("result")).thenReturn("hello");
        lenient().when(resultSet.getString("status")).thenReturn("COMPLETED");
        lenient().when(resultSet.getString("client_id")).thenReturn("");
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        List<Task> taskList = taskDAO.getAllTask("");
        Assert.assertEquals(1, taskList.size());
    }

    @Test
    public void TestGetTask() throws DBException, SQLException {
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getString("id")).thenReturn("");
        when(resultSet.getString("file_path")).thenReturn("c://myfile.txt");
        when(resultSet.getString("node_id")).thenReturn("");
        when(resultSet.getString("result")).thenReturn("hello");
        when(resultSet.getString("status")).thenReturn("COMPLETED");
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        Task task = taskDAO.getTask("");
        Assert.assertEquals("", task.getId());
    }
}
