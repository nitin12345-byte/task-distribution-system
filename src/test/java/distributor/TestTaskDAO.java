package distributor;

import com.itt.tds.core.model.Task;
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
    public void TestSaveTask() throws SQLException {
        when(preparedStatement.execute()).thenReturn(true);
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setString(any(int.class), any(String.class));
        doNothing().when(preparedStatement).setLong(any(int.class), any(long.class));
        Task task = new Task();
        taskDAO.save(task);
        verify(preparedStatement, atLeast(1)).execute();
    }

    @Test
    public void TestDeleteTask() throws SQLException {
        when(preparedStatement.execute()).thenReturn(true);
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setLong(any(int.class), any(long.class));
        taskDAO.delete(123);
        verify(preparedStatement, atLeast(1)).execute();
    }

    @Test
    public void TestGetAllTask() throws SQLException {
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getLong("id")).thenReturn(12L);
        when(resultSet.getString("file_path")).thenReturn("c://myfile.txt");
        when(resultSet.getLong("node_id")).thenReturn(123L);
        when(resultSet.getString("result")).thenReturn("hello");
        when(resultSet.getString("status")).thenReturn("Completed");
        when(statement.executeQuery(any(String.class))).thenReturn(resultSet);
        when(connection.createStatement()).thenReturn(statement);
        List<Task> taskList = taskDAO.getAllTask();
        Assert.assertEquals(1, taskList.size());
    }

    @Test
    public void TestGetTask() throws SQLException {
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getLong("id")).thenReturn(12L);
        when(resultSet.getString("file_path")).thenReturn("c://myfile.txt");
        when(resultSet.getLong("node_id")).thenReturn(123L);
        when(resultSet.getString("result")).thenReturn("hello");
        when(resultSet.getString("status")).thenReturn("Completed");
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        Task task = taskDAO.getTask(12);
        Assert.assertEquals(12, task.getId());
    }
}
