package edu.java.bot.controllers;

import edu.java.bot.controllers.dto.LinkUpdateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LinkUpdateController {

    @PostMapping("/updates")
    public ResponseEntity<Void> updateProcess(@RequestBody LinkUpdateRequest linkUpdateRequest) {
        return ResponseEntity.ok().build();
    }
}
