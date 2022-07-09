package dev.ikhtiyor.telegramclient.controller;

import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author IkhtiyorDev  <br/>
 * Date 09/07/22
 **/

@RequestMapping(TgController.TG_CONTROLLER_PATH)
public interface TgController {
    String TG_CONTROLLER_PATH = "/tg";

    String CREATE_CLIENT = "/create-client";
    String CHAT_LIST = "/chat-list";

    String INFO_USERS_FULL_LIST = "/info-users-full-list";
    String INFO_USERS_LIST = "/info-users-list";
    String ADD_DB_USERS_TO_CHANNEL = "/add-db-users-to-channel";


    @GetMapping(CREATE_CLIENT)
    HttpEntity<?> createClient();

    @GetMapping(CHAT_LIST)
    HttpEntity<?> chatList();

    @PostMapping(INFO_USERS_FULL_LIST)
    HttpEntity<?> infoUsersFullList(
            @RequestBody List<Long> userIdList
    );

    @PostMapping(INFO_USERS_LIST)
    HttpEntity<?> infoUsersList(
            @RequestBody List<Long> userIdList
    );

    @PostMapping(ADD_DB_USERS_TO_CHANNEL)
    HttpEntity<?> addDbUsersToChannel(
            @RequestParam(name = "channelId") Long channelId
    );

}
