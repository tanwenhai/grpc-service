package com.twh.common.entity.user;

import com.twh.common.entity.BaseEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalTime;

@Entity
@Table(name = "push_notify_setting")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper=false)
public class PushNotifySetting extends BaseEntity {
    @EmbeddedId
    private PushNotifySettingKey id;

    @Column(name = "time", columnDefinition = "time")
    private LocalTime time;

}
