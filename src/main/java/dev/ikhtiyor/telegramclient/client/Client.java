package dev.ikhtiyor.telegramclient.client;

import dev.ikhtiyor.telegramclient.handlers.DefaultHandler;
import dev.ikhtiyor.telegramclient.handlers.ErrorHandler;
import it.tdlight.common.Init;
import it.tdlight.common.ResultHandler;
import it.tdlight.common.TelegramClient;
import it.tdlight.common.utils.CantLoadLibrary;
import it.tdlight.jni.TdApi;
import it.tdlight.tdlight.ClientManager;
import lombok.extern.slf4j.Slf4j;

import java.io.IOError;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author IkhtiyorDev  <br/>
 * Date 08/07/22
 **/

@Slf4j
public class Client {
    private static TelegramClient client = null;
    private static final Lock authorizationLock = new ReentrantLock();
    private static final Condition getAuthorization = authorizationLock.newCondition();
    private static volatile boolean haveAuthorization = false;


    public static TelegramClient getClient() {
        try {
            Init.start();
        } catch (CantLoadLibrary e) {
            e.printStackTrace();
        }

        client = ClientManager.create();
        client.initialize(new RegHandler(), new ErrorHandler(), new ErrorHandler());

        client.execute(new TdApi.SetLogVerbosityLevel(0));

        if (client.execute(new TdApi.SetLogStream(new TdApi.LogStreamFile("tdlib.log", 1 << 27, false))) instanceof TdApi.Error) {
            throw new IOError(new IOException("Write access to the current directory is required"));
        }

        TdApi.Object object = DefaultHandler.result(client.execute(new TdApi.GetTextEntities("@telegram /test_comm")));

        System.out.println(object);


        authorizationLock.lock();

        try {
            while (!haveAuthorization) {
                try {
                    getAuthorization.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } finally {
            authorizationLock.unlock();
        }

        if (haveAuthorization) {
            return client;
        } else {
            return null;
        }

    }


    public static class RegHandler implements ResultHandler {


        @Override
        public void onResult(TdApi.Object object) {

            log.info("TdApi.Object object onResult {}", object);

            if (object.getConstructor() == TdApi.UpdateAuthorizationState.CONSTRUCTOR) {
                onAuthorizationStateUpdate(((TdApi.UpdateAuthorizationState) object).authorizationState);
            }

            if (object.getConstructor() == TdApi.GetChats.CONSTRUCTOR) {
                log.info("TdApi.GetChats.CONSTRUCTOR");
                log.info("o {}", object);
            }


        }

        private static void onAuthorizationStateUpdate(TdApi.AuthorizationState authorizationState) {
            AuthorizationRequestHandler authorizationRequestHandler = new AuthorizationRequestHandler();
            log.info("authorizationRequestHandler {}", authorizationRequestHandler);
            switch (authorizationState.getConstructor()) {

                case TdApi.AuthorizationStateWaitTdlibParameters.CONSTRUCTOR:
                    log.info("TdApi.AuthorizationStateWaitTdlibParameters.CONSTRUCTOR");

                    TdApi.TdlibParameters parameters = new TdApi.TdlibParameters();

                    parameters.databaseDirectory = "tdlib";
                    parameters.useMessageDatabase = true;
                    parameters.useSecretChats = true;
                    parameters.apiId = 16198866;
                    parameters.apiHash = "ad1bbfb8a79ac9f99ab66766c806b2f1";
                    parameters.systemLanguageCode = "en";
                    parameters.deviceModel = "Other";
                    parameters.applicationVersion = "1.0";
                    parameters.enableStorageOptimizer = true;

                    TdApi.SetTdlibParameters tdlibParameters = new TdApi.SetTdlibParameters(parameters);

                    log.info("tdlibParameters {}", tdlibParameters);
                    client.send(tdlibParameters, authorizationRequestHandler);

                    break;
                case TdApi.AuthorizationStateWaitEncryptionKey.CONSTRUCTOR:
                    log.info("TdApi.AuthorizationStateWaitEncryptionKey.CONSTRUCTOR:");
                    TdApi.CheckDatabaseEncryptionKey encryptionKey = new TdApi.CheckDatabaseEncryptionKey();

                    log.info("encryptionKey {}", encryptionKey);
                    client.send(encryptionKey, authorizationRequestHandler);
                    break;

                case TdApi.AuthorizationStateWaitPhoneNumber.CONSTRUCTOR:
                    log.info("TdApi.AuthorizationStateWaitPhoneNumber.CONSTRUCTOR:");
                    String phoneNumber = getString("Please enter phone number: ");

                    TdApi.SetAuthenticationPhoneNumber authenticationPhoneNumber = new TdApi.SetAuthenticationPhoneNumber(phoneNumber, null);
                    log.info("authenticationPhoneNumber {}", authenticationPhoneNumber);
                    client.send(authenticationPhoneNumber, authorizationRequestHandler);
                    break;

                case TdApi.AuthorizationStateWaitOtherDeviceConfirmation.CONSTRUCTOR:
                    log.info("TdApi.AuthorizationStateWaitOtherDeviceConfirmation.CONSTRUCTOR:");
                    String link = ((TdApi.AuthorizationStateWaitOtherDeviceConfirmation) authorizationState).link;
                    System.out.println("Please confirm this login link on another device: " + link);

                    break;

                case TdApi.AuthorizationStateWaitCode.CONSTRUCTOR:
                    log.info("TdApi.AuthorizationStateWaitCode.CONSTRUCTOR:");
                    String code = getString("Please enter authentication code: ");
                    TdApi.CheckAuthenticationCode checkAuthenticationCode = new TdApi.CheckAuthenticationCode(code);

                    log.info("checkAuthenticationCode {}", checkAuthenticationCode);
                    client.send(checkAuthenticationCode, authorizationRequestHandler);

                    break;

                case TdApi.AuthorizationStateWaitRegistration.CONSTRUCTOR:
                    log.info("TdApi.AuthorizationStateWaitRegistration.CONSTRUCTOR:");

                    String firstName = getString("Please enter your first name: ");
                    String lastName = getString("Please enter your last name: ");

                    TdApi.RegisterUser registerUser = new TdApi.RegisterUser(firstName, lastName);
                    log.info("registerUser {}", registerUser);
                    client.send(registerUser, authorizationRequestHandler);

                    break;

                case TdApi.AuthorizationStateWaitPassword.CONSTRUCTOR:

                    log.info("TdApi.AuthorizationStateWaitPassword.CONSTRUCTOR:");
                    String password = getString("Please enter password: ");

                    TdApi.CheckAuthenticationPassword checkAuthenticationPassword = new TdApi.CheckAuthenticationPassword(password);

                    log.info("checkAuthenticationPassword {}", checkAuthenticationPassword);

                    client.send(checkAuthenticationPassword, authorizationRequestHandler);

                    break;

                case TdApi.AuthorizationStateReady.CONSTRUCTOR:
                    log.info("TdApi.AuthorizationStateReady.CONSTRUCTOR:");
                    haveAuthorization = true;
                    authorizationLock.lock();

                    try {
                        getAuthorization.signal();
                    } finally {
                        authorizationLock.unlock();
                    }

                default:
                    System.err.println("Unsupported authorization state: " + authorizationState);

            }
        }

        private static String getString(String str) {
            String consoleStr = null;
            Scanner scanner = new Scanner(System.in);

            do {
                System.out.println(str);
                consoleStr = scanner.nextLine();
                consoleStr = consoleStr.trim();

                if (consoleStr.length() < 1) {
                    consoleStr = null;
                    continue;
                } else {
                    break;
                }

            } while (consoleStr == null);

            return consoleStr;
        }


    }

    public static class AuthorizationRequestHandler implements ResultHandler {
        @Override
        public void onResult(TdApi.Object object) {
            log.info("object {}", object);
            log.info("Constructor {}", object.getConstructor());

            switch (object.getConstructor()) {
                case TdApi.Error.CONSTRUCTOR:
                    System.err.println("Receive an error: " + object);
                    RegHandler.onAuthorizationStateUpdate(null);

                    break;

                case TdApi.Ok.CONSTRUCTOR:
                    //

                    break;
                default:
                    System.err.println("Receive wrong response from TDlib:" + object);
            }
        }
    }

}

