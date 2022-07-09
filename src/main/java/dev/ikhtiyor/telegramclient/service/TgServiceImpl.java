package dev.ikhtiyor.telegramclient.service;

import dev.ikhtiyor.telegramclient.client.Client;
import dev.ikhtiyor.telegramclient.entity.User;
import dev.ikhtiyor.telegramclient.handlers.AuthorizationRequestHandler;
import dev.ikhtiyor.telegramclient.repository.UserRepository;
import it.tdlight.common.TelegramClient;
import it.tdlight.jni.TdApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
    private final UserRepository userRepository;

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

    @Override
    public HttpEntity<?> addDbUsersToChannel(Long channelId) {

        List<User> userList = userRepository.findAll();

        List<Long> collect = userList.stream().map(User::getUserId).collect(Collectors.toList());
        long[] longs = collect.stream().mapToLong(l -> l).toArray();

        client.send(new TdApi.AddChatMembers(channelId, longs), new AuthorizationRequestHandler());
        return ResponseEntity.ok().build();
    }

    @Override
    public HttpEntity<?> getChatInfo(Long channelId) {

        client.send(new TdApi.GetChat(channelId), new AuthorizationRequestHandler());
        return ResponseEntity.ok().build();
    }
}
