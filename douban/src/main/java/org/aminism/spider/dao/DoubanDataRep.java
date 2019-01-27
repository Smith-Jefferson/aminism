package org.aminism.spider.dao;


import org.aminism.spider.entity.*;
import org.aminism.spider.log.BloomFilterUtil;
import org.aminism.spider.log.CLogManager;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by hello world on 2017/1/11.
 */
@Component
public class DoubanDataRep {

    @Autowired
    DoubanbookDao doubanbookDao;
    @Autowired
    DoubanbookCommentDao doubanbookCommentDao;
    @Autowired
    DoubanbookReviewDao doubanbookReviewDao;
    @Autowired
    DoubanbookReviewCommentDao doubanbookReviewCommentDao;
    @Autowired
    UserDao userDao;

    public List<Long> getDoubanBook() {
        return doubanbookDao.findAll().stream().map(DoubanbookEntity::getBookid).collect(Collectors.toList());
    }

    public List<DoubanbookCommentEntity> getDoubanbookComment() {
        return doubanbookCommentDao.findAll();
    }

    public List<DoubanbookReviewEntity> getDoubanbookReview() {
        return doubanbookReviewDao.findAll();
    }

    public List<DoubanbookReviewCommentEntity> getDoubanbookReviewCommet() {
        return doubanbookReviewCommentDao.findAll();
    }

    public List<UserEntity> getDoubanUser() {
        return userDao.findAll();
    }

    public List<UserEntity> getUsersByName(String username, String doubanid) {
        return userDao.queryByUnameAndDoubanuserid(username, doubanid);
    }

    @Cacheable(value = "user",key = "#p0")
    public UserEntity getUser(String doubanuserid,String uname,String avatar){
        UserEntity user=new UserEntity();
        user.setDoubanuserid(doubanuserid);
        user.setUname(uname);
        user.setAvatar(avatar);
        user.setFlag(0);
        return getUser(user);
    }

    @Cacheable(value = "user",key = "#p0.doubanuserid")
   public UserEntity getUser(UserEntity user) {
        List<UserEntity> tmp = getUsersByName(user.getUname(), user.getDoubanuserid());
        if (tmp != null && !tmp.isEmpty()) {
            UserEntity u = tmp.get(0);
            if(StringUtils.isBlank(u.getAvatar()) && StringUtils.isNotBlank(user.getAvatar())){
                u.setAvatar(user.getAvatar());
                return userDao.save(user);
            }
            return u;
        }
       return userDao.save(user);

   }

    public long saveUser(UserEntity user) {
        return userDao.save(user).getUserid();
    }


    @Cacheable(value = "comment",key = "#p0.doubanuserid.concat('_').concat(#p0.bookid).concat('_').concat(#p0.ratedate)")
    public DoubanbookCommentEntity saveComment(DoubanbookCommentEntity comment) {
        return doubanbookCommentDao.save(comment);
    }

    public void saveOrUpdateBookDerail(DoubanbookEntity detail) {
        doubanbookDao.save(detail);
    }

    @Autowired
    DoubanbookOfferDao doubanbookOfferDao;

    public void saveBookOffer(DoubanbookOfferEntity offer) {
        doubanbookOfferDao.save(offer);
    }

    public void saveReview(DoubanbookReviewEntity bookreview) {
        doubanbookReviewDao.save(bookreview);
    }


    public void saveReviewComment(DoubanbookReviewCommentEntity commet) {
        doubanbookReviewCommentDao.save(commet);
    }

    @Autowired
    TaskUrlDao taskUrlDao;

    public void saveTaskUrl(TaskUrlEntity url) {
        if (taskUrlDao.queryTop1ByUrl(url.getUrl()) != null)
            return;
        taskUrlDao.save(url);
    }

    public List<String> listTaskUrl() {
        return taskUrlDao.findAll().stream().map(TaskUrlEntity::getUrl).collect(Collectors.toList());
    }

    public void deleteTaskUrl(String url) {
        TaskUrlEntity v = taskUrlDao.queryTop1ByUrl(url);
        if (v != null) {
            taskUrlDao.delete(v);
        }
    }

    public void addToSchedule(Elements books) {
        if (books == null) {
            return;
        }
        for (Element book : books) {
            if (book == null) return;
            addToSchedule(book);
        }
    }

    public void addToSchedule(Element book) {
        String url = book.attr("abs:href");
        addToSchedule(url);
    }

    public void addToSchedule(String url) {
        if (isBookUrl(url)) {
            try {
                String bookid = url.split("subject/")[1].split("/")[0];
                url = "https://book.douban.com/subject/" + bookid;
                if (!BloomFilterUtil.getBloomFilter().contains(bookid)) {
                    saveTaskUrl(new TaskUrlEntity(url));
                }
            } catch (Exception e) {
                CLogManager.error(e);
            }
        }
    }

    public static boolean isBookUrl(String url) {
        if (url != null && url.contains("book") && url.contains("com/subject"))
            return true;
        return false;
    }
}
