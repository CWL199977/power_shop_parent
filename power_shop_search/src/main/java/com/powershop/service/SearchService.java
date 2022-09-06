package com.powershop.service;

import com.powershop.pojo.SearchItem;

import java.io.IOException;
import java.util.List;

public interface SearchService {
    void importAll() throws IOException;

    List<SearchItem> list(String q, Long page, Integer pageSize);

    void addDoc(Long itemId);
}
