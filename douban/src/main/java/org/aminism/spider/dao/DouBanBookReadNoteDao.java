package org.aminism.spider.dao;

import org.aminism.spider.entity.DouBanBookReadNoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author ygxie
 * @date 2019/1/25.
 */
public interface DouBanBookReadNoteDao  extends JpaRepository<DouBanBookReadNoteEntity,Long> {
    /**
     * 选取大家最喜欢的一条正面笔记
     * @param bookid 书的id
     */
    DouBanBookReadNoteEntity queryFirstByBookidOrderByFollowsDescRateDesc(Long bookid);
}
