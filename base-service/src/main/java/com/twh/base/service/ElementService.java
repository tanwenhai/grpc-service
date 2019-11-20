package com.twh.base.service;

import com.twh.common.entity.user.Element;
import com.twh.common.repository.jpa.user.ElementDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ElementService {
    @Autowired
    private ElementDao elementDao;

    public void getJson() {
        Iterable<Element> elements = elementDao.findAll();
        for (Element element : elements) {
//            Object data = JSON.parse(element.getContent());
//            System.out.println(data);
        }
    }
}
