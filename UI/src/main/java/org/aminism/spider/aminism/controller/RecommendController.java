package org.aminism.spider.aminism.controller;

import org.aminism.spider.aminism.model.BookSingleMessage;
import org.aminism.spider.aminism.provider.IBookDataProvder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by ygxie on 2017/10/26.
 */
@RestController
@RequestMapping("/rec")
public class RecommendController {

    @Autowired
    private IBookDataProvder bookDataProvder;

    @RequestMapping(value="/singlemessage/{isbn}")
    public BookSingleMessage index(@PathVariable String isbn){
        return bookDataProvder.recByIsbn(isbn);
    }

}
