package bg.unisofia.fmi.valentinalatinova.rest.data;

import java.sql.ResultSet;
import java.sql.SQLException;

import bg.unisofia.fmi.valentinalatinova.core.json.MobileResults;

public class MobileResultsDO extends DataBaseObject {
    private MobileResults mobileResults;

    @Override
    public void load(ResultSet resultSet) {
        try {
            mobileResults = new MobileResults();
            mobileResults.setId(resultSet.getLong("ID"));
            mobileResults.setName(resultSet.getString("name"));
            mobileResults.setUnits(resultSet.getString("units"));
            id = resultSet.getLong("ID");
        } catch (SQLException e) {
            getLogger().error(e.getMessage());
        }
    }

    public MobileResults getMobileResults() {
        return mobileResults;
    }

    public void setMobileResults(MobileResults mobileResults) {
        this.mobileResults = mobileResults;
    }
}
