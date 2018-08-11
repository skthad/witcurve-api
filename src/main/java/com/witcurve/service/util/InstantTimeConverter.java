package com.witcurve.service.util;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Converter
public class InstantTimeConverter implements AttributeConverter<Instant, Timestamp> {
    @Override
    public Timestamp convertToDatabaseColumn(Instant instant) {
        if (instant == null) {
            return null;
        }
        return new Timestamp(instant.toEpochMilli());
    }

    @Override
    public Instant convertToEntityAttribute(Timestamp localTime) {
        if (localTime == null) {
            return null;
        }
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(localTime.getTime()), ZoneId.systemDefault()).toInstant();
    }
}
