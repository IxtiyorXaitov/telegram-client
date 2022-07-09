package dev.ikhtiyor.telegramclient.service;

import dev.ikhtiyor.telegramclient.client.Client;
import dev.ikhtiyor.telegramclient.entity.User;
import dev.ikhtiyor.telegramclient.repository.UserRepository;
import it.tdlight.common.ResultHandler;
import it.tdlight.jni.TdApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author IkhtiyorDev  <br/>
 * Date 09/07/22
 **/

@Slf4j
@Service
@RequiredArgsConstructor
public class HandlerService implements ResultHandler {

    private final Client client;
    private final UserRepository userRepository;

    @Override
    public void onResult(TdApi.Object object) {

        Class<? extends TdApi.Object> aClass = object.getClass();
        log.info("aClass.getName() {}", aClass.getName());

        if (object.getConstructor() == TdApi.UpdateAuthorizationState.CONSTRUCTOR) {

//                log.info("TdApi.Object object onResult {}", object);

            client.onAuthorizationStateUpdate(((TdApi.UpdateAuthorizationState) object).authorizationState);
        }

        if (object.getConstructor() == TdApi.Users.CONSTRUCTOR) {
//            log.info("TdApi.GetContacts.CONSTRUCTOR {}", object);

        }
        if (object.getConstructor() == TdApi.UserFullInfo.CONSTRUCTOR) {
//            log.info("TdApi.UserFullInfo.CONSTRUCTOR {}", object);

        }

//            Class<? extends TdApi.Object> aClass = object.getClass();
//            log.info("aClass.getName() {}", aClass.getName());
        if (object.getConstructor() == TdApi.UpdateUser.CONSTRUCTOR) {
            log.info("TdApi.User.CONSTRUCTOR {}", object);


            TdApi.UpdateUser user = (TdApi.UpdateUser) object;

            User newUser = new User(
                    user.user.id,
                    user.user.firstName,
                    user.user.lastName,
                    user.user.username,
                    user.user.phoneNumber
            );

            log.info("newUser {}", newUser);
            userRepository.save(newUser);
        }

    }
}
