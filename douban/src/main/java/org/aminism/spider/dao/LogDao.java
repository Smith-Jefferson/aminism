package org.aminism.spider.dao;

import org.aminism.spider.entity.LogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by xieyigang on 2019/1/19.
 */
public interface LogDao extends JpaRepository<LogEntity,Long> {
}
