package com.cdbbbsp.www.Entity.Event.Bean;

import java.util.List;

/**
 * 创建人 xiaojun
 * 创建时间 2017/7/13-15:00
 */

public class AllGoodsBean {

    /**
     * success : 1
     * data : [{"goodsid":"28683b2f-7157-4ae1-aa95-160d6149bdf0","title":"饭烧光","imgs":[{"path":"/img/d1c24950-_1499932857343.jpg"},{"path":"/img/a4c7fce6-_1499932857343.png"}]}]
     */

    private String success;
    private List<GoodsBean> data;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<GoodsBean> getData() {
        return data;
    }

    public void setData(List<GoodsBean> data) {
        this.data = data;
    }

    public static class GoodsBean {
        /**
         * goodsid : 28683b2f-7157-4ae1-aa95-160d6149bdf0
         * title : 饭烧光
         * imgs : [{"path":"/img/d1c24950-_1499932857343.jpg"},{"path":"/img/a4c7fce6-_1499932857343.png"}]
         */

        private String goodsid;
        private String title;
        private List<ImgsBean> imgs;

        public String getGoodsid() {
            return goodsid;
        }

        public void setGoodsid(String goodsid) {
            this.goodsid = goodsid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<ImgsBean> getImgs() {
            return imgs;
        }

        public void setImgs(List<ImgsBean> imgs) {
            this.imgs = imgs;
        }

        public static class ImgsBean {
            /**
             * path : /img/d1c24950-_1499932857343.jpg
             */

            private String path;

            public String getPath() {
                return path;
            }

            public void setPath(String path) {
                this.path = path;
            }
        }
    }
}
