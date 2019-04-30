package com.example.six_mothtest.model.bean;

import java.util.List;

/**
 * Created by Android Studio.
 * User: Administrator
 * Date: 2019/4/30 0030
 * Time: 13:54
 * Describe: ${as}
 */
public class GuShiBean {

    /**
     * code : 200
     * message : 成功!
     * result : [{"title":"日诗","content":"欲出未出光辣达，千山万山如火发。|须臾走向天上来，逐却残星赶却月。","authors":"宋太祖"},{"title":"句","content":"未离海底千山黑，才到天中万国明。","authors":"宋太祖"},{"title":"登戎州江楼闲望","content":"满目江山四望幽，白云高卷嶂烟收。|日回禽影穿疏木，风递猿声入小楼。|远岫似屏横碧落，断帆如叶截中流。","authors":"幸夤逊"},{"title":"雪","content":"片片飞来静又闲，楼头江上复山前。|飘零尽日不归去，帖破清光万里天。","authors":"幸夤逊"},{"title":"云","content":"因登巨石知来处，勃勃元生绿藓痕。|静即等闲藏草木，动时顷刻遍乾坤。|横天未必朋元恶，捧日还曾瑞至尊。|不独朝朝在巫峡，楚王何事谩劳魂。","authors":"幸夤逊"}]
     */

    private int code;
    private String message;
    private List<ResultBean> result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * title : 日诗
         * content : 欲出未出光辣达，千山万山如火发。|须臾走向天上来，逐却残星赶却月。
         * authors : 宋太祖
         */

        private String title;
        private String content;
        private String authors;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getAuthors() {
            return authors;
        }

        public void setAuthors(String authors) {
            this.authors = authors;
        }
    }
}
