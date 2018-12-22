package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.domain.Keyword;
import com.witcurve.repository.KeywordRepository;
import com.witcurve.web.rest.errors.WitcurveException;
import com.witcurve.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class KeywordResource {

    private final Logger log = LoggerFactory.getLogger(KeywordResource.class);

    @Autowired
    KeywordRepository keywordRepository;

    /**
     * searches keywords
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @GetMapping("/keywords")
    @Timed
    public ResponseEntity<List<Keyword>> searchKeywords(@RequestParam("search") String search) throws WitcurveException, URISyntaxException {
        log.debug("Request to search keywords: {}", search);
        List<Keyword> result = keywordRepository.searchKeywords(search.toLowerCase());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/keywords")
    @Timed
    public ResponseEntity<Keyword> createKeyword(@RequestParam("keyword") String keyword) throws WitcurveException, URISyntaxException {
        log.debug("Request to create keyword: {}", keyword);
        Keyword result = keywordRepository.create(keyword.toLowerCase());
        return ResponseEntity.created(new URI("/api/keywords/" + result.getName()))
            .headers(HeaderUtil.createEntityCreationAlert("keywords", result.getName()))
            .body(result);
    }

}
