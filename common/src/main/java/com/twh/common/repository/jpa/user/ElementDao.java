package com.twh.common.repository.jpa.user;

import com.twh.common.entity.user.Element;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ElementDao extends CrudRepository<Element, Integer> {

}
