package com.aurelius.springdgsdemo.controllers;

import com.aurelius.springdgsdemo.connectors.dgs.graphiqlonline.GraphiqlOnlineConnector;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/countries")
public class CountryController {
    @Autowired
    GraphiqlOnlineConnector graphiqlOnlineConnector;

    @GetMapping
    public Object getCountries() throws JsonProcessingException {
        return graphiqlOnlineConnector.getCountries();
    }
}
