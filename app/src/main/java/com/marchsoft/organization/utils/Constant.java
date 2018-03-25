package com.marchsoft.organization.utils;


public final class Constant {
    public static final boolean DEBUG = false;

    // 修改代码值
    public static final int CODE_SUCCESS = 0;
    public static final int CODE_FAILURE = 1;
    public static final int CODE_TIMEOUT = 2;
    public static final int CODE_DATA_ERROR = 3;
    public static final int CODE_DB_ERROR = 4;
    public static final int CODE_SERVICE_ERROR = 5;
    //public static final int CODE_NO_TOKEN = 2333;'
    public static final int CODE_RELOGIN = 9;


    public static final int PAGE_SIZE = 8;

    public static final int ORGANIZATION_SUCCESS = 0x4000;

    // api url
    private static final String API_URL_PRE = DEBUG ? "http://172.16.28.44/organization/index.php/Api/"
            :"http://123.206.64.180/Api/";
    public static final String API_GET_BANNER = API_URL_PRE
            + "home/top_journalism";
    public static final String API_GET_MAIN_NEWS = API_URL_PRE
            + "home/journalism";
    public static final String API_GET_MAIN_DYNAMIC = API_URL_PRE
            + "home/activity";
    public static final String API_GET_MAIN_NEWS_DETAILS = API_URL_PRE
            + "corporation/news_list_show";
    public static final String API_GET_MAIN_DYNAMIC_DETAILS = API_URL_PRE
            + "corporation/particulars";
    public static final String API_GET_MAIN_DYNAMIC_JOINUS= API_URL_PRE
            + "corporation/enroll";

    public static final String API_GET_CORPORATION_PARTICULARS = API_URL_PRE
            + "corporation/search_new";




    public static final String API_GET_MINE_DETAILS = API_URL_PRE
            + "personal/details";
    public static final String API_GET_MINE_ACTIVITY = API_URL_PRE
            + "personal/get_all_activity";
    public static final String API_GET_MINE_ORGANIZATION = API_URL_PRE
            + "community/display";

    public static final String API_EDITOR_MINE = API_URL_PRE
            + "personal/editor";

    public  static final String API_SET_PHOTO=API_URL_PRE
            +"personal/set_image";
    public static final String API_GET_MINE_CHANGEPWD=API_URL_PRE
            +"user/change_password";
    public static final String API_POST_LOGIN=API_URL_PRE
            +"user/login";
    public static final String API_POST_LOGOUT=API_URL_PRE
            +"user/logout";
    public static final String API_POST_REGISTER=API_URL_PRE
            +"user/register";
   public static final String API_POST_FORGETPASSWORD=API_URL_PRE
           +"user/update_password";
    public static final String API_GET_FEEDBACK=API_URL_PRE
            +"personal/feedback";
    public static final String API_POST_CHECKTOKEN=API_URL_PRE+"user/check_token";
    public static final String API_GET_ABOUTUS=API_URL_PRE+"personal/about_us";
    /*社团start*/
    public static final String API_GET_ORGANIZATION_INTRODUCTION = API_URL_PRE + "community/display";
    public static final String API_ORGANIZATION_JOIN_CLUB = API_URL_PRE + "community/add_community";
    public static final String API_ORGANIZATION_SIGN_OUT = API_URL_PRE + "community/cancel_add_community";
    public static final String API_GET_ORGANIZATION_DETAIL_MESSAGE = API_URL_PRE + "community/details";
    public static final String API_GET_ORGANIZATION_LEAVE_MESSAGE = API_URL_PRE + "community/leavemessage";
    public static final String API_GET_ORGANIZATION_SHOW_MESSAGE = API_URL_PRE + "community/show_message";
    public static final String API_GET_ORGANIZATION_SHOW_MEMBER = API_URL_PRE + "community/user";
    public static final String API_GET_ORGANIZATION_SHOW_PRESIDENT_DATA= API_URL_PRE + "Personal/details";
    public static final String API_GET_ORGANIZATION_NEWS = API_URL_PRE + "community/news";
    public static final String API_GET_ORGANIZATION_ACTIVITY =  API_URL_PRE + "community/as_activity";
    public static final String API_GET_ORGANIZATION_JUDGE_ISMEMBER= API_URL_PRE + "community/check_commit";



    /*社团end*/
}
