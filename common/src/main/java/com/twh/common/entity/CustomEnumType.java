package com.twh.common.entity;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Properties;

/**
 * 枚举映射 暂时只有byte/int
 * @param <T>
 */
@Slf4j
public class CustomEnumType<T extends Enum> extends org.hibernate.type.EnumType {
    private Class enumClass;
    private Method factoryMethod;
    private Method valueMethod;

    @Override
    public void setParameterValues(Properties parameters) {
        String returnClass = (String)parameters.get(RETURNED_CLASS);
        try {
            Class<?> enumClass = Class.forName(returnClass);
            if (enumClass.isEnum()) {
                this.enumClass = enumClass;
                EnumType enumType = enumClass.getAnnotation(EnumType.class);
                for (Method method : enumClass.getDeclaredMethods()) {
                    if (method.getName().equals(enumType.factoryMethod())) {
                        factoryMethod = method;
                    } else if (method.getName().equals(enumType.valueMethod())) {
                        valueMethod = method;
                    }
                }
            }
        } catch (Exception ignore) {}
    }

    @Override
    public int[] sqlTypes() {
        return new int[]{Types.NUMERIC};
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner) throws HibernateException, SQLException {
        Object value = rs.getByte(names[0]);
        try {
            return factoryMethod.invoke(null, value);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session) throws HibernateException, SQLException {
        try {
            Object dbValue = valueMethod.invoke(value);
            if (dbValue instanceof Byte) {
                st.setByte(index, (byte)dbValue);
            } else if (dbValue instanceof Integer) {
                st.setInt(index, (int)dbValue);
            }
            log.trace("binding parameter [{}] as [{}}] - [{}]", index, dbValue.getClass(), dbValue);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
