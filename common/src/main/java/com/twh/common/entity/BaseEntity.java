package com.twh.common.entity;

import com.twh.common.entity.type.CommonDelFlg;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper=false)
@MappedSuperclass
public class BaseEntity {
//    @Type(type = "org.hibernate.type.NumericBooleanType")
    @Column(name = "is_del")
    @Type(type = "com.twh.common.entity.CustomEnumType")
    private CommonDelFlg isDel;

    /**
     * 记录创建时间 insert/update 忽略 DEFAULT CURRENT_TIMESTAMP
     */
    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * 记录最后更新时间 insert/update 忽略 DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
     */
    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updateAt;
}
