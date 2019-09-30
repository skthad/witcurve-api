package com.witcurve.service.util;

import com.google.common.base.Strings;
import com.google.gson.Gson;

import javax.persistence.AttributeConverter;
import java.util.Map;

public class MapToStringConverter implements AttributeConverter<Map<String, String>, String> {

    @Override
    public String convertToDatabaseColumn(Map<String, String> tokens) {
        if (tokens == null || tokens.size() == 0) {
            return null;
        }
        Gson parser = new Gson();
        return parser.toJson(tokens, Map.class);
    }

    @Override
    public Map<String, String> convertToEntityAttribute(String json) {
        if (Strings.isNullOrEmpty(json)) {
            return null;
        }
        Gson parser = new Gson();
        return parser.fromJson(json, Map.class);
    }
}
