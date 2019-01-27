package org.aminism.spider.dao;

import org.aminism.spider.entity.DoubanbookEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by xieyigang on 2019/1/19.
 */
public interface DoubanbookDao extends JpaRepository<DoubanbookEntity,Long> {
    DoubanbookEntity queryFirstByIsbn(Long isbn);
}
