package dev.ikhtiyor.telegramclient.controller;

import dev.ikhtiyor.telegramclient.service.TgService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author IkhtiyorDev  <br/>
 * Date 09/07/22
 **/

@RestController
@RequiredArgsConstructor
public class TgTgControllerImpl implements TgController {

    private final TgService tgService;

    @Override
    public HttpEntity<?> createClient() {
        return tgService.createClient();
    }

    @Override
    public HttpEntity<?> chatList() {
        return tgService.chatList();
    }
}
