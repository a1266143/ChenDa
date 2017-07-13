package com.cdbbbsp.www.Entity.Event.Bean;

import java.util.List;

/**
 * 创建人 xiaojun
 * 创建时间 2017/7/13-13:43
 */

public class AllCategoryBean {

    /**
     * success : 1
     * data : [{"categoryid":"e43780a1-16fa-4864-a6d9-03ea1eed412c","categoryname":"(┬＿┬)"},{"categoryid":"3e211345-9602-48b3-8fd6-f2f2cdcc2e83","categoryname":"豆瓣酱"},{"categoryid":"3436c430-9164-423b-aabb-36318d5acdc1","categoryname":"辣椒酱"}]
     */

    private String success;
    private List<CategoryBean> data;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<CategoryBean> getData() {
        return data;
    }

    public void setData(List<CategoryBean> data) {
        this.data = data;
    }

    public static class CategoryBean {
        /**
         * categoryid : e43780a1-16fa-4864-a6d9-03ea1eed412c
         * categoryname : (┬＿┬)
         */

        private String categoryid;
        private String categoryname;

        public String getCategoryid() {
            return categoryid;
        }

        public void setCategoryid(String categoryid) {
            this.categoryid = categoryid;
        }

        public String getCategoryname() {
            return categoryname;
        }

        public void setCategoryname(String categoryname) {
            this.categoryname = categoryname;
        }
    }
}
