package org.aminism.spider.dao;

import org.aminism.spider.entity.TaskUrlEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by xieyigang on 2019/1/19.
 */
public interface TaskUrlDao extends JpaRepository<TaskUrlEntity,Long> {
    TaskUrlEntity queryTop1ByUrl(String url);

    List<TaskUrlEntity> queryAllByTag(String tag);

    TaskUrlEntity queryTop1ByTagNotLikeAndAndIsdel(String tag,int isdel);


}
