package dev.ikhtiyor.telegramclient.service;

import org.springframework.http.HttpEntity;

import java.util.List;

/**
 * @author IkhtiyorDev  <br/>
 * Date 09/07/22
 **/

public interface TgService {
    HttpEntity<?> createClient();

    HttpEntity<?> chatList();
    HttpEntity<?> infoUsersFullList(List<Long> userIdList);

    HttpEntity<?> infoUsersList(List<Long> userIdList);

    HttpEntity<?> addDbUsersToChannel(Long channelId);
}
