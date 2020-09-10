package com.ing.banking.converter;

import org.springframework.core.convert.converter.Converter;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public abstract class GenericConverter<IN, OUT> implements Converter<IN, OUT> {
    public abstract OUT convert(IN source);

    public List<OUT> convertFrom(Collection<IN> collection) {
        return collection.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }
}
