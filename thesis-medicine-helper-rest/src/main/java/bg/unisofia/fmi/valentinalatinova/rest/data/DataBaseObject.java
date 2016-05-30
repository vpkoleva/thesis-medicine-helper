package bg.unisofia.fmi.valentinalatinova.rest.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;

public abstract class DataBaseObject {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public abstract void load(ResultSet resultSet);

    protected Logger getLogger() {
        return logger;
    }
}
