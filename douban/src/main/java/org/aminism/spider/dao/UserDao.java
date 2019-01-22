package org.aminism.spider.dao;

import org.aminism.spider.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by xieyigang on 2019/1/19.
 */
public interface UserDao extends JpaRepository<UserEntity,Long> {
}
