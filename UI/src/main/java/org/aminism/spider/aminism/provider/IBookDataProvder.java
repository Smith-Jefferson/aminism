package org.aminism.spider.aminism.provider;

import org.aminism.spider.aminism.model.BookSingleMessage;

/**
 * @author ygxie
 * @date 2019/1/27.
 */
public interface IBookDataProvder {
    BookSingleMessage recByIsbn(String isbn);
}
