package ntu.wg.cq.ibook.app;
/**
 * Created by C_Q on 2018/1/20.
 */
public class API {
    //public static final String BASE_URL = "http://192.168.43.220:8080/server/";
   public static final String BASE_URL = "http://106.15.195.16:8080/server/";
   // public static final String BASE_SOCKET = "192.168.43.220";
    //用户
   public static final String USER =BASE_URL+"user/";
    /*登录接口*/
    public static final String LOGIN = USER+"login";
    //登出
    public static final String LOGOUT = USER+"logout";
    //验证码
    public static final String CAPTCHA = USER+"captcha";
    //注册
    public static final String REGISTER = USER+"register";
    //修改密码
    public static final String PASSWORD = USER+"reset";
    /**用户行为*/
    public static final String BEHAVIOR = USER+"action";
    //书籍
    public static final String BOOK =BASE_URL+"book/";
    //查找书籍
    public static final String SEARCH_BOOK = BOOK+"search";
    //获取内容
    public static final String GET_CONTENT = BOOK+"content";
    //获取目录
    public static final String GET_CATALOG =BOOK+"catalog";
    //排行
    public static final String RANK =BOOK+"rank";
    //推荐
    public static final String COMMEND = BOOK+"commend";
    //添加
    public static final String ADD_BOOK = BOOK + "add";
    public static final String SEND_BARRAGE = BOOK + "send_barrage";
    public static final String GET_BARRAGE = BOOK + "get_barrage";


    //书架
    public static final String BOOK_SHELF = BASE_URL+"shelf/";
    public static final String ADD = BOOK_SHELF+"add";
    public static final String DELETE = BOOK_SHELF+"delete";
    public static final String GET_SHELF = BOOK_SHELF+"get";

    //分享
    public static final String SHARE = BASE_URL+"share/";
    public static final String DO_SHARE = SHARE+"do";
    public static final String GET_SHARE = SHARE+"get";

    //书吧
    public static final String BA = BASE_URL+"ba/";
    public static final String BA_DO_FOLLOW = BA + "follow";
    public static final String BA_FOLLOWS = BA + "bas";
    public static final String BA_CARDS = BA + "cards";
    public static final String BA_COMMENTS = BA + "comments";
    public static final String BA_POST_CARD = BA + "pcard";
    public static final String BA_POST_COMMENT = BA + "pcomment";
}