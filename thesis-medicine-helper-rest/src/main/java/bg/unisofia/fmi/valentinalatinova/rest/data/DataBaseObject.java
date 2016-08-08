package bg.unisofia.fmi.valentinalatinova.rest.data;

import java.sql.ResultSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bg.unisofia.fmi.valentinalatinova.core.json.BaseJson;

public abstract class DataBaseObject extends BaseJson {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public abstract void load(ResultSet resultSet);

    protected Logger getLogger() {
        return logger;
    }
}
