package bg.unisofia.fmi.valentinalatinova.rest.data;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DataBaseResult extends DataBaseObject {
    private Map<String, String> results;

    @Override
    public void load(ResultSet resultSet) {
        try {
            results = new HashMap<>();
            ResultSetMetaData metaData = resultSet.getMetaData();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                results.put(metaData.getColumnName(i), resultSet.getString(i));
            }
        } catch (SQLException e) {
            getLogger().error(e.getMessage());
        }
    }

    public String getValue(String key) {
        return results.get(key);
    }
}
