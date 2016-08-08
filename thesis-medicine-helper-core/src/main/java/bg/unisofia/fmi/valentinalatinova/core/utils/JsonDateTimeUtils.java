package bg.unisofia.fmi.valentinalatinova.core.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.io.Serializable;

import org.joda.time.DateTime;

public class JsonDateTimeUtils {

    public static class DateTimeSerializer extends JsonSerializer<DateTime> implements Serializable {
        @Override
        public void serialize(DateTime dateTime, JsonGenerator generator, SerializerProvider arg2) throws IOException {
            generator.writeString(dateTime.toString());
        }
    }

    public static class DateTimeDeserializer extends JsonDeserializer<DateTime> implements Serializable {
        @Override
        public DateTime deserialize(JsonParser parser, DeserializationContext context) throws IOException {
            return DateTime.parse(parser.getText());
        }
    }
}
