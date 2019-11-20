package com.twh.common.entity.type;

import com.twh.common.entity.EnumType;

@EnumType
public enum CommonDelFlg {
    ACTIVE((byte)0), INVALID((byte)1);

    private final byte value;

    CommonDelFlg(byte i) {
        value = i;
    }

    public byte getValue() {
        return value;
    }

    public static CommonDelFlg of(byte value) {
        for (CommonDelFlg commonDelFlg : values()) {
            if (commonDelFlg.value == value) {
                return commonDelFlg;
            }
        }

        throw new IllegalArgumentException(String.valueOf(value));
    }
}
