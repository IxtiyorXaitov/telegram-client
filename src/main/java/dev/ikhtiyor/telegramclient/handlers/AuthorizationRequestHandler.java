package dev.ikhtiyor.telegramclient.handlers;

import dev.ikhtiyor.telegramclient.client.Client;
import it.tdlight.common.ResultHandler;
import it.tdlight.jni.TdApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author IkhtiyorDev  <br/>
 * Date 09/07/22
 **/

@Slf4j
@RequiredArgsConstructor
public class AuthorizationRequestHandler implements ResultHandler {

    private Client client;

    @Override
    public void onResult(TdApi.Object object) {
//        log.info("object {}", object);
//        log.info("Constructor {}", object.getConstructor());

        switch (object.getConstructor()) {
            case TdApi.Error.CONSTRUCTOR:
                System.err.println("Receive an error: " + object);
                client.onAuthorizationStateUpdate(null);
                break;
            case TdApi.Ok.CONSTRUCTOR:
                System.err.println("OK: " + object);
                break;
            default:
                System.err.println("Receive wrong response from TDlib:" + object);
        }
    }

}
