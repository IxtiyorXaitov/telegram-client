package dev.ikhtiyor.telegramclient.service;

import dev.ikhtiyor.telegramclient.client.Client;
import dev.ikhtiyor.telegramclient.handlers.AuthorizationRequestHandler;
import it.tdlight.common.TelegramClient;
import it.tdlight.jni.TdApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author IkhtiyorDev  <br/>
 * Date 09/07/22
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class TgServiceImpl implements TgService {

    private final Client createClient;
    private TelegramClient client;

    @Override
    public HttpEntity<?> createClient() {

        client = createClient.createClient();
        return ResponseEntity.ok().build();
    }

    @Override
    public HttpEntity<?> chatList() {

        client.send(new TdApi.GetContacts(), new AuthorizationRequestHandler());

        return ResponseEntity.ok().build();
    }

    @Override
    public HttpEntity<?> infoUsersFullList(List<Long> userIdList) {
        for (Long userId : userIdList) {

            log.info("userId {}", userId);
            client.send(new TdApi.GetUserFullInfo(userId), new AuthorizationRequestHandler());

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        return ResponseEntity.ok().build();
    }

    @Override
    public HttpEntity<?> infoUsersList(List<Long> userIdList) {
        for (Long userId : userIdList) {

            log.info("userId {}", userId);
            client.send(new TdApi.GetUser(userId), new AuthorizationRequestHandler());

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        return ResponseEntity.ok().build();
    }
}
