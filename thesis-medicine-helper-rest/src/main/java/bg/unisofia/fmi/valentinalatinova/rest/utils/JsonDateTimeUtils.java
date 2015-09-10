package bg.unisofia.fmi.valentinalatinova.rest.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.joda.time.DateTime;

import java.io.IOException;

public class JsonDateTimeUtils {
    public static class DateTimeSerializer extends JsonSerializer<DateTime> {
        @Override
        public void serialize(DateTime dateTime, JsonGenerator generator, SerializerProvider arg2) throws IOException {
            generator.writeString(dateTime.toString());
        }
    }

    public static class DateTimeDeserializer extends JsonDeserializer<DateTime> {
        @Override
        public DateTime deserialize(JsonParser parser, DeserializationContext context) throws IOException {
            return DateTime.parse(parser.getText());
        }
    }
}
