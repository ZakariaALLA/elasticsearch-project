package com.zakariaalla.elasticsearchdemo.controller;

import com.zakariaalla.elasticsearchdemo.service.IndexService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/index")
public class IndexeController {

    private final IndexService indexService;

    public IndexeController(IndexService indexService) {
        this.indexService = indexService;
    }

    @GetMapping("/recreate")
    public void recreateIndex() {
        this.indexService.recreateIndices(true);
    }
}
