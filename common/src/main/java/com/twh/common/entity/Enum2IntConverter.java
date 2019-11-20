package com.twh.common.entity;


import javax.persistence.AttributeConverter;

public class Enum2IntConverter<X extends Enum<X>, Y> implements AttributeConverter<X, Y> {

    @Override
    public Y convertToDatabaseColumn(X attribute) {
        return null;
    }

    @Override
    public X convertToEntityAttribute(Y dbData) {
        return null;
    }
}
