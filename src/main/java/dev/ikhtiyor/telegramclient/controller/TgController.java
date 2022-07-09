package dev.ikhtiyor.telegramclient.controller;

import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author IkhtiyorDev  <br/>
 * Date 09/07/22
 **/

@RequestMapping(TgController.TG_CONTROLLER_PATH)
public interface TgController {
    String TG_CONTROLLER_PATH = "/tg";

    String CREATE_CLIENT = "/create-client";
    String CHAT_LIST = "/chat-list";

    @GetMapping(CREATE_CLIENT)
    HttpEntity<?> createClient();

    @GetMapping(CHAT_LIST)
    HttpEntity<?> chatList();

}
