package io.gcandal.payments.server.db.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.gcandal.payments.server.core.Attributes;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;

@Converter(autoApply = true)
public class toJsonConverter implements AttributeConverter<Attributes, String> {

    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(final Attributes attributes) {
        try {
            return OBJECT_MAPPER.writeValueAsString(attributes);
        } catch (final JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Attributes convertToEntityAttribute(final String dbData) {
        try {
            return OBJECT_MAPPER.readValue(dbData, Attributes.class);
        } catch (final IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
