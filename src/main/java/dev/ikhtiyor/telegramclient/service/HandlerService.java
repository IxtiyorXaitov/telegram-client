package dev.ikhtiyor.telegramclient.service;

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

    private final UserRepository userRepository;

    @Override
    public void onResult(TdApi.Object object) {

        if (object.getConstructor() == TdApi.Users.CONSTRUCTOR) {
            log.info("TdApi.GetContacts.CONSTRUCTOR {}", object);

        }
        if (object.getConstructor() == TdApi.UserFullInfo.CONSTRUCTOR) {
            log.info("TdApi.UserFullInfo.CONSTRUCTOR {}", object);

        }
        if (object.getConstructor() == TdApi.User.CONSTRUCTOR) {
            log.info("TdApi.User.CONSTRUCTOR {}", object);

            TdApi.User user = (TdApi.User) object;

            User newUser = new User(
                    user.id,
                    user.firstName,
                    user.lastName,
                    user.username,
                    user.phoneNumber
            );

            userRepository.save(newUser);
        }

    }
}
