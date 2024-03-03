package edu.java.controller;

import edu.java.controller.dto.DeleteLinkRequest;
import edu.java.controller.dto.LinkListResponse;
import edu.java.controller.dto.LinkResponse;
import edu.java.controller.dto.SetLinkRequest;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/links")
public class LinkController {

    @GetMapping
    public ResponseEntity<LinkListResponse> getLinksList(@RequestHeader(name = "Tg-Chat-Id") Long chatId) {
        LinkListResponse linkListResponse =
            new LinkListResponse(List.of(new LinkResponse(chatId, URI.create("http://github.com"))));
        return ResponseEntity.ok(linkListResponse);
    }

    @PostMapping
    public ResponseEntity<LinkResponse> addLink(
        @RequestHeader(name = "Tg-Chat-Id") Long chatId,
        @RequestBody SetLinkRequest setLinkRequest
    ) {
        LinkResponse linkResponse = new LinkResponse(chatId, URI.create(setLinkRequest.link()));
        return ResponseEntity.ok(linkResponse);
    }

    @DeleteMapping
    public ResponseEntity<LinkResponse> deleteLink(
        @RequestHeader(name = "Tg-Chat-Id") Long chatId,
        @RequestBody DeleteLinkRequest deleteLinkRequest
    ) {
        LinkResponse linkResponse = new LinkResponse(chatId, URI.create(deleteLinkRequest.link()));
        return ResponseEntity.ok(linkResponse);
    }
}
