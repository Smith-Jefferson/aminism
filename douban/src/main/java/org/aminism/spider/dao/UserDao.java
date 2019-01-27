package org.aminism.spider.dao;

import org.aminism.spider.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by xieyigang on 2019/1/19.
 */
public interface UserDao extends JpaRepository<UserEntity,Long> {
    List<UserEntity> queryByUnameAndDoubanuserid(String username,String doubanid);
}
