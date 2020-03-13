package com.joezeo.joefgame.potal.service.impl;

import com.joezeo.joefgame.potal.service.SearchService;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    private HttpSolrClient solrClient;

}
