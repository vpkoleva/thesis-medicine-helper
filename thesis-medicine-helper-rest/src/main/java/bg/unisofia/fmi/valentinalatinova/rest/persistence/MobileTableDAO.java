package bg.unisofia.fmi.valentinalatinova.rest.persistence;

import bg.unisofia.fmi.valentinalatinova.rest.json.MobileTableField;
import bg.unisofia.fmi.valentinalatinova.rest.json.MobileTableValue;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MobileTableDAO {
    private static List<MobileTableField> fields = new ArrayList<MobileTableField>();
    private static List<MobileTableValue> values = new ArrayList<MobileTableValue>();

    static {
        fields.add(new MobileTableField(1, "Field1", 1, 1));
        fields.add(new MobileTableField(2, "Field2", 2, 1));

        values.add(new MobileTableValue(1, "Measurement1", DateTime.now(), 1));
        values.add(new MobileTableValue(1, "Measurement2", DateTime.now(), 1));
        values.add(new MobileTableValue(1, "Measurement1", DateTime.now(), 2));
        values.add(new MobileTableValue(1, "Measurement2", DateTime.now(), 2));
    }

    public static HashMap<String, List> getTable(long userId) {
        HashMap<String, List> table = new HashMap<String, List>();
        table.put("fields", fields);
        table.put("values", values);
        return table;
    }
}
