package com.uniquedu.cemetery;

/**
 * Created by baoyi on 2016/3/7.
 */
public class Address {
    public static final String URL = "http://219.140.77.9/";

    //网上灵堂分页带查询接口
    public static final String PersonalMemorial = URL + "mvcwebmis/" + "nologin/FindWithPagerForPersonalMemorial";
    //图片前缀
    public static final String IMAGEADDRESS = URL + "mvcwebmis";
    //歌曲播放地址
    public static final String MUSIC = URL + "mvcwebmis/nologin/" +
            "ChangeMp3State?callback=callbakename&";
    public static String listurl = URL + "mvcwebmis/nologin/FindWithPagerForPersonalMemorial?callback=callbake&page=";
    //灵堂初始化
    public static final String INFOMATION = URL + "mvcwebmis/nologin/InitPersonalMemorial?callback=callbakename&id=";
    //获取祭扫日志
    public static final String WORSHIP_DAILY = URL + "mvcwebmis/nologin/FindWithPagerForPersonalMemorialNotePad";
    //历史照片
    public static final String WORSHIP_PHOTO = URL + "mvcwebmis/nologin/FindWithPagerForPersonalMemorialPhoto";
    //纪念文选
    public static final String WONSHIP_ANTHOLOGY = URL + "mvcwebmis/nologin/FindWithPagerForPMArticle";
    public static final String WONSHIP_ANTHOLOGY_INFO = URL + "mvcwebmis/nologin/GetMemorialArticleById";
    //执行祭扫动作
    public static final String WONSHIP_THEME = URL + "mvcwebmis/nologin/MemorialAction?callback=callbakename&id=";
}
