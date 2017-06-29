package dk.tv2.bulldog.backend.db.managers;

import dk.tv2.bulldog.backend.db.EntityManager;
import dk.tv2.bulldog.backend.db.annotations.InjectConnection;
import dk.tv2.bulldog.backend.db.entities.Epgs;
import dk.tv2.bulldog.backend.io.Configuration;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

/**
 *
 * @author hjep
 */
public class EpgsManager extends Epgs {

    private static final Logger logger = Logger.getLogger(EpgsManager.class.getName());
    private final DataSource dataSource = DataSourceManager.getInstance(Configuration.getProperty("datasource"));
    @InjectConnection
    private final Connection cachedConnection = null;

    private final static String CREATE_EPGS_SQL = "INSERT INTO epgs (channel_id,page_id, channel_name, program_id, title, data, start_time, end_time, category) VALUES (?,0,?,?,?,?,?,?,?)".intern();
    private final static String UPDATE_EPGS_SQL = "UPDATE epgs SET title=?, start_time=?, end_time = ?, data=?, category=?, updated_at=NOW() WHERE channel_id = ? and program_id = ?".intern();
    private final static String UPDATE_EPGS_WITH_PAGEID_SQL = "UPDATE epgs SET page_id=? WHERE channel_id = ? and program_id = ?".intern();
    private final static String REMOVE_PROGRAM_SQL = "DELETE FROM epgs WHERE channel_id = ? and program_id = ?".intern();
    private final static String REMOVE_PROGRAMS_BY_TIME_SQL = "DELETE FROM epgs WHERE channel_id = ? and end_time < ? ".intern();
    private final static String SELECT_PROGRAM_SQL = "SELECT * FROM epgs WHERE channel_id = ? and program_id = ? ".intern();
    private final static String SELECT_PROGRAMS_WITH_TIME_SQL = "SELECT * FROM epgs WHERE channel_id = ? and start_time >= ? ORDER BY start_time".intern();
    private final static String SELECT_PROGRAMS_WITH_START_AND_END_TIME_SQL = "SELECT * FROM epgs WHERE channel_id = ? and start_time between ? and  ? ORDER BY start_time".intern();
    private final static String SELECT_RUNNING_PROGRAMS_SQL = "SELECT * FROM epgs WHERE start_time < ? AND end_time > ? ORDER BY channel_id, updated_at DESC".intern();
    private final static String SELECT_CATEGORY_WITH_TIME_SQL = "SELECT * FROM epgs WHERE category = ? AND channel_id = ? and start_time > ? ".intern();

    @Override
    public boolean create() {
        try (Connection connection = (cachedConnection != null && !cachedConnection.isClosed()) ? cachedConnection : dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(CREATE_EPGS_SQL);

            preparedStatement.setInt(1, getChannelId());
            preparedStatement.setString(2, getChannelName());
            preparedStatement.setInt(3, getProgramId());
            preparedStatement.setString(4, getTitle());
            preparedStatement.setString(5, getData());
            preparedStatement.setLong(6, getStartTime());
            preparedStatement.setLong(7, getEndTime());
            preparedStatement.setString(8, getCategory());

            int resultSet = preparedStatement.executeUpdate();
            return resultSet > 0;

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "EpgsManager.create() threw an exception", e);
            return false;

        }
    }

    @Override
    public boolean updatePageId() {
        try (Connection connection = (cachedConnection != null && !cachedConnection.isClosed()) ? cachedConnection : dataSource.getConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_EPGS_WITH_PAGEID_SQL);

            preparedStatement.setInt(1, getPageId());
            preparedStatement.setInt(2, getChannelId());
            preparedStatement.setInt(3, getProgramId());

            int affectedCount = preparedStatement.executeUpdate();

            return affectedCount > 0;

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "EpgsManager.updatePageId() threw an exception", e);
            return false;
        }
    }

    @Override
    public boolean update() {
        try (Connection connection = (cachedConnection != null && !cachedConnection.isClosed()) ? cachedConnection : dataSource.getConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_EPGS_SQL);

            preparedStatement.setString(1, getTitle());
            preparedStatement.setLong(2, getStartTime());
            preparedStatement.setLong(3, getEndTime());
            preparedStatement.setString(4, getData());
            preparedStatement.setString(5, getCategory());
            preparedStatement.setInt(6, getChannelId());
            preparedStatement.setInt(7, getProgramId());

            int affectedCount = preparedStatement.executeUpdate();

            return affectedCount > 0;

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "EpgsManager.update() threw an exception", e);
            return false;
        }
    }

    @Override
    public boolean delete(int channelId, int programId) {
        try (Connection connection = (cachedConnection != null && !cachedConnection.isClosed()) ? cachedConnection : dataSource.getConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement(REMOVE_PROGRAM_SQL);

            preparedStatement.setInt(1, channelId);
            preparedStatement.setInt(2, programId);

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "EpgsManager.delete() threw an exception", e);
            return false;

        }
    }

    @Override
    public boolean deleteOldRecords(int channelId, long timeout) {
        try (Connection connection = (cachedConnection != null && !cachedConnection.isClosed()) ? cachedConnection : dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(REMOVE_PROGRAMS_BY_TIME_SQL);

            preparedStatement.setInt(1, channelId);
            preparedStatement.setLong(2, timeout);

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "EpgsManager.deleteOldRecords() threw an exception", e);
            return false;

        }
    }

    @Override
    public Map<String, Epgs> loadProgramsWithInTime(int channelId, long startTime, long endTime) {

        Map<String, Epgs> map = new LinkedHashMap<>();
        try (Connection connection = (cachedConnection != null && !cachedConnection.isClosed()) ? cachedConnection : dataSource.getConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_PROGRAMS_WITH_START_AND_END_TIME_SQL);
            preparedStatement.setInt(1, channelId);
            preparedStatement.setLong(2, startTime);
            preparedStatement.setLong(3, endTime);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Epgs epgs = readEntity(resultSet);
                String key = epgs.getChannelId() + "-" + epgs.getProgramId();
                map.put(key, epgs);
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "EpgsManager.loadProgramsWithInTime() threw an exception", e);
        }
        return map;
    }

    @Override
    public Map<String, Epgs> loadProgramsWithInTime(int channelId, long startTime) {
        Map<String, Epgs> map = new LinkedHashMap<>();
        try (Connection connection = (cachedConnection != null && !cachedConnection.isClosed()) ? cachedConnection : dataSource.getConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_PROGRAMS_WITH_TIME_SQL);
            preparedStatement.setInt(1, channelId);
            preparedStatement.setLong(2, startTime);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Epgs epgs = readEntity(resultSet);
                String key = epgs.getChannelId() + "-" + epgs.getProgramId();
                map.put(key, epgs);
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "EpgsManager.loadProgramsWithInTime() threw an exception", e);
        }
        return map;
    }

    private Epgs readEntity(ResultSet resultSet) throws SQLException {
        Epgs epgs = EntityManager.create(Epgs.class);

        epgs.setChannelId(resultSet.getInt("channel_id"));
        epgs.setPageId(resultSet.getInt("page_id"));
        epgs.setChannelName(resultSet.getString("channel_name"));
        epgs.setProgramId(resultSet.getInt("program_id"));
        epgs.setTitle(resultSet.getString("title"));
        epgs.setData(resultSet.getString("data"));
        epgs.setStartTime(resultSet.getLong("start_time"));
        epgs.setEndTime(resultSet.getLong("end_time"));
        epgs.setCategory(resultSet.getString("category"));
        epgs.setCreatedAt(new Date(resultSet.getTimestamp("created_at").getTime()));
        epgs.setUpdatedAt(new Date(resultSet.getTimestamp("updated_at").getTime()));

        return epgs;
    }

    @Override
    public List<Epgs> loadRunningPrograms() {
        List<Epgs> elements = new LinkedList<>();
        try (Connection connection = (cachedConnection != null && !cachedConnection.isClosed()) ? cachedConnection : dataSource.getConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_RUNNING_PROGRAMS_SQL);
            long currentTime = System.currentTimeMillis();
            preparedStatement.setLong(1, currentTime);
            preparedStatement.setLong(2, currentTime);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                elements.add(readEntity(resultSet));
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "EpgsManager.loadRunningPrograms() threw an exception", e);
        }

        return elements;
    }

    @Override
    public Map<String, Epgs> loadPrgramsByCategoryForChannelWithInTime(String category, int channelId, long startTime) {
        Map<String, Epgs> map = new LinkedHashMap<>();
        try (Connection connection = (cachedConnection != null && !cachedConnection.isClosed()) ? cachedConnection : dataSource.getConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_CATEGORY_WITH_TIME_SQL);
            preparedStatement.setString(1, category);
            preparedStatement.setInt(2, channelId);
            preparedStatement.setLong(3, startTime);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Epgs epgs = readEntity(resultSet);
                String key = epgs.getChannelId() + "-" + epgs.getProgramId();
                map.put(key, epgs);
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "EpgsManager.loadPrgramsByCategoryForChannelWithInTime() threw an exception", e);
        }

        return map;
    }

    @Override
    public Epgs load(int channelId, int programId) {
        try (Connection connection = (cachedConnection != null && !cachedConnection.isClosed()) ? cachedConnection : dataSource.getConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_PROGRAM_SQL);
            preparedStatement.setInt(1, channelId);
            preparedStatement.setInt(2, programId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return readEntity(resultSet);
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "ImportManager.load() threw an exception", e);

        }
        return null;
    }

}
