package bg.unisofia.fmi.valentinalatinova.rest.persistence;

import bg.unisofia.fmi.valentinalatinova.rest.MedicineConfig;
import bg.unisofia.fmi.valentinalatinova.rest.data.DataBaseObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataBaseCommander {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataBaseCommander.class);

    private Connection connection;

    public DataBaseCommander(MedicineConfig.Database database)
            throws ClassNotFoundException, SQLException, IllegalAccessException, InstantiationException {
        // Register MySQL Connector/J
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        // Initialise connection
        connection = DriverManager.getConnection(
                "jdbc:mysql://" + database.getHost() + ":" + database.getPort() + "/" + database.getDatabase()
                        + "?user=" + database.getUsername() + "&password=" + database.getPassword());
    }

    public PreparedStatement createPreparedStatement(String sql, Object... statementData) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            if (statementData != null && statementData.length > 0) {
                for (int i = 0; i < statementData.length; i++) {
                    preparedStatement.setObject(i + 1, statementData[i]);
                }
            }
        } catch (SQLException sqlEx) {
            LOGGER.error("Error during prepared statement creation: ", sqlEx);
        }
        return preparedStatement;
    }

    public <T extends DataBaseObject> List<T> select(Class<T> clazz, String sql, Object... statementData) {
        ResultSet resultSet = null;
        List<T> result = new ArrayList<>();
        PreparedStatement preparedStatement = createPreparedStatement(sql, statementData);
        try {
            if (preparedStatement != null) {
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    T obj = clazz.newInstance();
                    obj.load(resultSet);
                    result.add(obj);
                }
            }
        } catch (SQLException | InstantiationException | IllegalAccessException ex) {
            LOGGER.error("Error during SELECT of '" + sql + "': ", ex);
        } finally {
            close(resultSet, preparedStatement);
        }
        return result;
    }

    public long insert(String sql, Object... statementData) {
        PreparedStatement preparedStatement = createPreparedStatement(sql, statementData);
        try {
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            long id = -1;
            if (resultSet.next()) {
                id = resultSet.getLong(1);
            }
            return id;
        } catch (SQLException sqlEx) {
            LOGGER.error("Error during INSERT of '" + preparedStatement.toString() + "': ", sqlEx);
        } finally {
            close(null, preparedStatement);
        }
        return -1;
    }

    public boolean execute(PreparedStatement... preparedStatements) {
        String sql = "";
        try {
            connection.setAutoCommit(false);
            for (PreparedStatement preparedStatement : preparedStatements) {
                sql += preparedStatement.toString();
                preparedStatement.executeUpdate();
            }
            connection.commit();
            return true;
        } catch (SQLException sqlEx) {
            LOGGER.error("Error during INSERT of '" + sql + "': ", sqlEx);
            try {
                connection.rollback();
            } catch (SQLException e) {
                LOGGER.error("Error during rollback: ", sqlEx);
            }
        } finally {
            close(null, preparedStatements);
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                LOGGER.error("Error setAutoCommit(true): ", e);
            }
        }
        return false;
    }


    private void close(ResultSet resultSet, PreparedStatement... preparedStatements) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException sqlEx) {
                LOGGER.warn("Warning during closing of result set: ", sqlEx);
            }
        }

        if (preparedStatements != null) {
            for (PreparedStatement preparedStatement : preparedStatements) {
                try {
                    preparedStatement.close();
                } catch (SQLException sqlEx) {
                    LOGGER.warn("Warning during closing of prepared statement: ", sqlEx);
                }
            }
        }
    }
}
