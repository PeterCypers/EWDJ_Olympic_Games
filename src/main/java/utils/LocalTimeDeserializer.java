package utils;

import java.io.IOException;
import java.time.LocalTime;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import static utils.InitFormatter.*;

public class LocalTimeDeserializer extends JsonDeserializer<LocalTime>{

	@Override
	public LocalTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
		
		String valueAsString = p.getValueAsString();
        if (valueAsString == null || valueAsString.isBlank()) {
            return null;
        }
        return LocalTime.parse(valueAsString, TIME_FORMATTER);
	}

}
