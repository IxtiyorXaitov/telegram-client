package dev.ikhtiyor.telegramclient.service;

import it.tdlight.common.ResultHandler;
import it.tdlight.jni.TdApi;
import lombok.extern.slf4j.Slf4j;

/**
 * @author IkhtiyorDev  <br/>
 * Date 09/07/22
 **/

@Slf4j
public class HandlerService implements ResultHandler {

    @Override
    public void onResult(TdApi.Object object) {

        if (object.getConstructor() == TdApi.Users.CONSTRUCTOR) {
            log.info("TdApi.GetContacts.CONSTRUCTOR {}", object);

        }

    }
}
