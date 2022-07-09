package dev.ikhtiyor.telegramclient;

import dev.ikhtiyor.telegramclient.client.Client;
import it.tdlight.common.TelegramClient;
import it.tdlight.jni.TdApi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TelegramClientApplication {

    private static TelegramClient client;

    public static void main(String[] args) {
        SpringApplication.run(TelegramClientApplication.class, args);

        client = Client.getClient();

        client.send(new TdApi.GetChats(), new Client.AuthorizationRequestHandler());

    }

}
