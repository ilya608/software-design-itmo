package ru.itmo.kirpichev.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.sql.DataSource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import ru.itmo.kirpichev.dto.TasksListDto;
import ru.itmo.kirpichev.model.Task;
import ru.itmo.kirpichev.model.TaskList;

/**
 * @author ilyakirpichev
 */
public class TasksJdbcDao extends JdbcDaoSupport implements TasksDao {
    private final ObjectMapper objectMapper;

    public TasksJdbcDao(DataSource dataSource) {
        super();
        setDataSource(dataSource);
        objectMapper = new ObjectMapper();
    }

    @Override
    public void addEmptyTaskList(String name) throws RuntimeException {
        TasksListDto dto = new TasksListDto(name, new ArrayList<>());
        final String tasks;
        try {
            tasks = objectMapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        String sql = String.format("insert into taskslist(NAME, TASKS) values(\"%s\", \"{[]}\")", name);
        executeUpdate(sql);
    }

    private int executeUpdate(String sql) throws RuntimeException {
        try {
            var conn = Objects.requireNonNull(getDataSource()).getConnection();
            var statement = conn.createStatement();
            int res = statement.executeUpdate(sql);
            conn.close();
            statement.close();
            return res;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<TaskList> executeQuery(String sql) throws RuntimeException {
        try {
            var conn = Objects.requireNonNull(getDataSource()).getConnection();
            var statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            List<TaskList> result = new ArrayList<>();
            while (rs.next()) {
                String name = rs.getString("name");
                String text = rs.getString("text");
            }
            conn.close();
            statement.close();
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<TaskList> getProductsBySql(String sql) throws RuntimeException {
        final List<TaskList> lists = new ArrayList<>();
        executeQuery("select * from taskslist");

        return lists;

    }

    private List<TasksListDto> getProductsByRequest(String sql) {
        return getJdbcTemplate().query(sql, new BeanPropertyRowMapper(TasksListDto.class));
    }

    @Override
    public TaskList getTasksByListId(int id) {
        return null;
    }

    @Override
    public boolean deleteTaskListById(int id) {
        return false;
    }

    @Override
    public void setTaskDone(int listId, int taskId) {

    }

    @Override
    public void addTask(int listId, Task task) {
        String sql = "insert into product(";
        executeUpdate(sql);
    }

    @Override
    public List<TaskList> getLists() throws SQLException {
        return null;
    }
}
