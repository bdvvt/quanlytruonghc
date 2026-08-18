package com.example.quanlytruonghoc.configs;

import com.example.quanlytruonghoc.models.constants.CourseStatus;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToCourseStatusConverter implements Converter<String, CourseStatus> {
    @Override
    public CourseStatus convert(String source) {
        if (source == null || source.isBlank()) {
            return null;
        }
        return CourseStatus.valueOf(source.trim().toUpperCase());
    }
}
