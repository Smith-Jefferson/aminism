package org.aminism.spider.aminism.provider.impl;

import org.aminism.spider.aminism.model.BookSingleMessage;
import org.aminism.spider.aminism.model.ReateScoreModel;
import org.aminism.spider.aminism.provider.IBookDataProvder;
import org.aminism.spider.dao.DouBanBookReadNoteDao;
import org.aminism.spider.dao.DoubanbookDao;
import org.aminism.spider.dao.UserDao;
import org.aminism.spider.entity.DouBanBookReadNoteEntity;
import org.aminism.spider.entity.DoubanbookEntity;
import org.aminism.spider.entity.UserEntity;
import org.aminism.spider.log.Lunar;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author ygxie
 * @date 2019/1/27.
 */
@Component
public class BookDataProvider implements IBookDataProvder {

    @Autowired
    DouBanBookReadNoteDao readNoteDao;
    @Autowired
    DoubanbookDao doubanbookDao;
    @Autowired
    UserDao userDao;

    @Override
    public BookSingleMessage recByIsbn(String isbn) {
        if (StringUtils.isBlank(isbn) && !StringUtils.isNumeric(isbn)) {
            return new BookSingleMessage();
        }
        BookSingleMessage msg = new BookSingleMessage();
        DoubanbookEntity book = doubanbookDao.queryFirstByIsbn(Long.parseLong(isbn));
        msg.setBookid(book.getBookid());
        msg.setBookname(book.getBookname());
        msg.setBookface(book.getFaceimg());
        msg.setAuthor(book.getAuthor());
        msg.setPublishDate(book.getPublishdate());
        msg.setRatescore(new ReateScoreModel().paser(book.getRate()).getRate());
        DouBanBookReadNoteEntity readnote = readNoteDao.queryFirstByBookidOrderByFollowsDescRateDesc(book.getBookid());
        msg.setContent(readnote.getContent());
        msg.setFollows(readnote.getFollows());
        UserEntity user = userDao.findOne(readnote.getUserid());
        msg.setUserid(user.getUserid());
        msg.setUsername(user.getUname());
        msg.setUseravator(user.getAvatar());
        msg.setDate(new Lunar().build());
        return msg;
    }
}
