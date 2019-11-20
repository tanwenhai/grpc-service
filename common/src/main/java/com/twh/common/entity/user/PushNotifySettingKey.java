package com.twh.common.entity.user;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper=false)
public class PushNotifySettingKey implements Serializable {
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "periods_id")
    private Integer periodsId;
}
