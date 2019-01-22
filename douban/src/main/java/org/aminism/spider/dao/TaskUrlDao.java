package org.aminism.spider.dao;

import org.aminism.spider.entity.TaskUrlEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by xieyigang on 2019/1/19.
 */
public interface TaskUrlDao extends JpaRepository<TaskUrlEntity,Long> {
}
