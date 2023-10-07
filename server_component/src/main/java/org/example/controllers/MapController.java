package org.example.controllers;

import org.example.entities.Pair;
import org.example.services.MapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path="/keys")
public class MapController {

    @Autowired
    MapService mapService;

    @PutMapping(path = "/{key}",  params = {"value"})
    public ResponseEntity<Pair> addPair(@PathVariable String key,
                                        @RequestParam String value){
        return ResponseEntity.ok(mapService.addPair(key, value));
    }

    @GetMapping(path = "/{key}")
    public ResponseEntity<Pair> getPair(@PathVariable String key) {
        return ResponseEntity.ok(mapService.getPair(key));
    }
}
