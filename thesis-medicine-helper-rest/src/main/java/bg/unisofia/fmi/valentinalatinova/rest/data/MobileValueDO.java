package bg.unisofia.fmi.valentinalatinova.rest.data;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import bg.unisofia.fmi.valentinalatinova.core.json.MobileResultsValue;

public class MobileValueDO extends DataBaseObject {
    private MobileResultsValue mobileResultsValue;
    private Long resultId;

    @Override
    public void load(ResultSet resultSet) {
        try {
            mobileResultsValue = new MobileResultsValue();
            mobileResultsValue.setId(resultSet.getLong("ID"));
            mobileResultsValue.setMeasurement(resultSet.getString("measurement"));
            mobileResultsValue
                    .setMeasurementDate(new DateTime(resultSet.getTimestamp("date")).withZone(DateTimeZone.UTC));
            mobileResultsValue.setResultsId(resultSet.getLong("mtables_ID"));
            id = resultSet.getLong("ID");
            resultId = resultSet.getLong("mtables_ID");
        } catch (SQLException e) {
            getLogger().error(e.getMessage());
        }
    }

    public MobileResultsValue getMobileResultsValue() {
        return mobileResultsValue;
    }

    public void setMobileResultsValue(MobileResultsValue mobileResultsValue) {
        this.mobileResultsValue = mobileResultsValue;
    }

    public Long getResultId() {
        return resultId;
    }

    public void setResultId(Long resultId) {
        this.resultId = resultId;
    }
}
