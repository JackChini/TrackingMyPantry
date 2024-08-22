package com.example.trackingmypantrygiacomochinilam;

public class Constants {
    //per le chiamate all'api
    public static final String BASE_URL = "https://lam21.iot-prism-lab.cs.unibo.it/";
    public static final String CREATE_ACCOUNT_URL = "https://lam21.iot-prism-lab.cs.unibo.it/users";
    public static final String LOGIN_URL = "https://lam21.iot-prism-lab.cs.unibo.it/auth/login";
    public static final String USER_URL = "https://lam21.iot-prism-lab.cs.unibo.it/users/me";
    public static final String PRODUCTS_BARCODE_URL = "https://lam21.iot-prism-lab.cs.unibo.it/products?barcode=";
    public static final String VOTES_URL = "https://lam21.iot-prism-lab.cs.unibo.it/votes";
    public static final String PRODUCTS_URL = "https://lam21.iot-prism-lab.cs.unibo.it/products";

    public static final int RESULT_LOAD_IMAGE = 200;
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1888;

    public static final String NOTIFICATION_CHANNEL_ID = "trackingmypantry";
    public static final String NOTIFICATION_CHANNEL_DESCRIPTION = "notifiche dell'app tracking my pantry";

    public static String DEFAULT_TYPE = "Nessuno";

    //aggiungi e rimuovi quantità item
    public static final int DEFAULT_ADD_OR_REMOVE_QUANTITY = 1;


}
