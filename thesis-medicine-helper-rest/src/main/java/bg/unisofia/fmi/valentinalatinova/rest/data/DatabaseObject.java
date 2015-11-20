package bg.unisofia.fmi.valentinalatinova.rest.data;

import java.sql.ResultSet;

public abstract class DataBaseObject {
    public abstract void load(ResultSet resultSet);
}
