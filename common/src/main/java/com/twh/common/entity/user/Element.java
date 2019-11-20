package com.twh.common.entity.user;

import com.twh.common.entity.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "element")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper=false)
public class Element extends BaseEntity {
    @Id
    private Integer id;

    @Column(name = "type")
    private Byte time;

    @Column(name = "category_id")
    private Integer categoryId;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "cover")
    private String cover;

    @Column(name = "words_num")
    private String words_num;

    @Column(name = "min_age")
    private Short min_age;

    @Column(name = "max_age")
    private Short max_age;

    @Column(name = "min_level")
    private Short min_level;

    @Column(name = "max_level")
    private Short max_level;

    @Column(name = "weight")
    private Short weight;
}
