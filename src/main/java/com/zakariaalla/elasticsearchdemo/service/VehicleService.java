package com.zakariaalla.elasticsearchdemo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class VehicleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(VehicleService.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();

}
