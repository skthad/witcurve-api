package com.witcurve.service.util;

import com.google.common.base.Strings;
import com.google.gson.Gson;

import javax.persistence.AttributeConverter;
import java.util.List;

public class ListToStringConverter implements AttributeConverter<List<String>, String> {
    @Override
    public String convertToDatabaseColumn(List<String> tokens) {
        if (tokens == null || tokens.size() == 0) {
            return null;
        }
        Gson parser = new Gson();
        return parser.toJson(tokens, List.class);
    }

    @Override
    public List<String> convertToEntityAttribute(String json) {
        if (Strings.isNullOrEmpty(json)) {
            return null;
        }
        Gson parser = new Gson();
        return parser.fromJson(json, List.class);
    }
}
