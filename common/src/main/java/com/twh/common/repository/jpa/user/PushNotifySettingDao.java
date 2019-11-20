package com.twh.common.repository.jpa.user;

import com.twh.common.entity.user.PushNotifySetting;
import com.twh.common.entity.user.PushNotifySettingKey;
import org.springframework.data.repository.CrudRepository;

public interface PushNotifySettingDao extends CrudRepository<PushNotifySetting, PushNotifySettingKey> {
}
