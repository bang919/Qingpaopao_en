package com.wopin.qingpaopao.bean.response;

import java.util.ArrayList;

public class ExploreListRsp {

    /**
     * found : 5
     * posts : [{"ID":82,"date":"2018-07-18T14:57:29+00:00","title":"吃猪蹄不能补充胶原蛋白？那么正确补充胶原蛋白的是\u2026\u2026","featured_image":"https://wifi.h2popo.com/wp-content/uploads/2018/07/1516959529649042816.jpg"},{"ID":78,"date":"2018-07-18T14:56:49+00:00","title":"做好这一点，就能减少80%的心血管疾病死亡！","featured_image":"https://wifi.h2popo.com/wp-content/uploads/2018/07/1517389043703021867.jpg"},{"ID":56,"date":"2018-06-10T08:57:19+00:00","title":"染发\u201c新年头等大事\u201d，警惕肝肾损伤!","featured_image":"https://wifi.h2popo.com/wp-content/uploads/2018/06/1517998306904039595.png.jpeg"},{"ID":43,"date":"2018-06-10T07:53:28+00:00","title":"春节\u201c酒局\u201d多？这个方法将酒精伤害降到最低","featured_image":"https://wifi.h2popo.com/wp-content/uploads/2018/06/1517645365268060825.jpeg"},{"ID":1,"date":"2018-06-09T14:00:47+00:00","title":"头晕又心悸，警惕低血糖作怪！家里糖尿病友要注意了！","featured_image":"https://wifi.h2popo.com/wp-content/uploads/2018/06/1517462338615090663.jpg"}]
     * meta : {"links":{"counts":"https://public-api.wordpress.com/rest/v1.1/sites/147539272/post-counts/post"}}
     */

    private int found;
    private MetaBean meta;
    private ArrayList<PostsBean> posts;

    public int getFound() {
        return found;
    }

    public void setFound(int found) {
        this.found = found;
    }

    public MetaBean getMeta() {
        return meta;
    }

    public void setMeta(MetaBean meta) {
        this.meta = meta;
    }

    public ArrayList<PostsBean> getPosts() {
        return posts;
    }

    public void setPosts(ArrayList<PostsBean> posts) {
        this.posts = posts;
    }

    public static class MetaBean {
        /**
         * links : {"counts":"https://public-api.wordpress.com/rest/v1.1/sites/147539272/post-counts/post"}
         */

        private LinksBean links;

        public LinksBean getLinks() {
            return links;
        }

        public void setLinks(LinksBean links) {
            this.links = links;
        }

        public static class LinksBean {
            /**
             * counts : https://public-api.wordpress.com/rest/v1.1/sites/147539272/post-counts/post
             */

            private String counts;

            public String getCounts() {
                return counts;
            }

            public void setCounts(String counts) {
                this.counts = counts;
            }
        }
    }

    public static class PostsBean {
        /**
         * ID : 82
         * date : 2018-07-18T14:57:29+00:00
         * title : 吃猪蹄不能补充胶原蛋白？那么正确补充胶原蛋白的是……
         * featured_image : https://wifi.h2popo.com/wp-content/uploads/2018/07/1516959529649042816.jpg
         */

        private int ID;
        private String date;
        private String title;
        private String featured_image;

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getFeatured_image() {
            return featured_image;
        }

        public void setFeatured_image(String featured_image) {
            this.featured_image = featured_image;
        }
    }
}
