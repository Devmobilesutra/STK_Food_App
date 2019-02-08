package com.stk.orderingapp.Config;

/**
 * Created by JARVIS on 28-Mar-18.
 */

public class Config {
    static String GCM_PROJECT_NUMBER = "";
    static String BASE_URL = "";
    static AppMode appMode = AppMode.DEV;
    static String GOOGLE_URL = "";

    static public String getBaseURL() {
        init(appMode);
        return BASE_URL;
    }

    static public String getGoogleUrl(){
        init(appMode);
        return GOOGLE_URL;
    }


    static public String getGCMProjectNumber() {
        init(appMode);
        return GCM_PROJECT_NUMBER;
    }


    /**
     * Initialize all the variable in this method
     *
     * @param appMode
     */
    public static void init(AppMode appMode) {

        switch (appMode) {
            case DEV:
                BASE_URL = "http://mobilesutra.com/stkfoods-dashboard/index.php/service/";
                GCM_PROJECT_NUMBER = "";
                GOOGLE_URL = "";
                break;

            case TEST:
                BASE_URL = "";
                GCM_PROJECT_NUMBER = "";
                GOOGLE_URL = "";
                break;

            case LIVE:
                BASE_URL = "";
                GCM_PROJECT_NUMBER = "";
                GOOGLE_URL = "";
                break;
        }

    }

    public enum AppMode {
        DEV, TEST, CLIENT, LIVE
    }
}
