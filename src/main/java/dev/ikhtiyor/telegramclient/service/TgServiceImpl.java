package dev.ikhtiyor.telegramclient.service;

import dev.ikhtiyor.telegramclient.client.Client;
import dev.ikhtiyor.telegramclient.handlers.AuthorizationRequestHandler;
import it.tdlight.common.TelegramClient;
import it.tdlight.jni.TdApi;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * @author IkhtiyorDev  <br/>
 * Date 09/07/22
 **/

@Service
public class TgServiceImpl implements TgService {

    private TelegramClient client;

    @Override
    public HttpEntity<?> createClient() {
        client = Client.createClient();
        return ResponseEntity.ok().build();
    }

    @Override
    public HttpEntity<?> chatList() {

        client.send(new TdApi.GetContacts(), new AuthorizationRequestHandler());

        return ResponseEntity.ok().build();
    }
}
