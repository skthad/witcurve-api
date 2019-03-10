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
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class KeywordResource {

    private final Logger log = LoggerFactory.getLogger(KeywordResource.class);

    @Autowired
    KeywordRepository keywordRepository;

    @PostMapping("/keywords")
    @Timed
    public ResponseEntity<Keyword> createKeyword(@RequestBody Keyword keyword) throws WitcurveException, URISyntaxException {
        log.debug("Request to create keywords: " + keyword.getName());
        keyword.setName(keyword.getName().toLowerCase());
        Keyword result = keywordRepository.save(keyword);
        return ResponseEntity.created(new URI("/api/keywords/" + result.getName()))
            .headers(HeaderUtil.createEntityCreationAlert("keywords", result.getName()))
            .body(result);
    }

    /**
     * searches keywords
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @GetMapping("/keywords")
    @Timed
    public ResponseEntity<List<Keyword>> searchKeywords(@RequestParam(value = "search", required = false) String search) {
        log.debug("Request to search keywords: {}", search);

        List<Keyword> result = new ArrayList<>();
        if(search!=null) {
            result = keywordRepository.searchKeywords(search.toLowerCase());
        } else {
            result = keywordRepository.findAll();
        }
        return ResponseEntity.ok(result);
    }

}
