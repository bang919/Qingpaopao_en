package com.wopin.qingpaopao.bean.response;

import java.io.Serializable;
import java.util.ArrayList;

public class SystemMessageRsp extends NormalRsp {

    private ArrayList<ResultBean> result;

    public ArrayList<ResultBean> getResult() {
        return result;
    }

    public void setResult(ArrayList<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean implements Serializable {
        /**
         * id : 435
         * author : {"id":"5b66bbf64238af6a804aacaf","name":"氢泡泡","avatar_URL":"http://0.gravatar.com/avatar/c128c9f6bdce5ca00de551f2689b8af8?s=96&d=mm&r=g"}
         * date : 2018-11-10T17:30:07
         * title : 一杯好水改变生活轨迹
         * content : <p>欢迎加入丰富多彩的氢泡泡！</p>
         * <p>
         * featured_media : -1
         * URL : https://wifi.h2popo.com/2018/11/10/%e4%b8%80%e6%9d%af%e5%a5%bd%e6%b0%b4%e6%94%b9%e5%8f%98%e7%94%9f%e6%b4%bb%e8%bd%a8%e8%bf%b9/
         * likes : 1
         * stars : 1
         * comments : 0
         * read : 18
         * myLike : false
         * myStar : false
         * featured_image : https://wifi.h2popo.com/wp-content/uploads/2018/09/dafault.jpg
         */

        private int id;
        private AuthorBean author;
        private String date;
        private String title;
        private String content;
        private int featured_media;
        private String URL;
        private int likes;
        private int stars;
        private int comments;
        private int read;
        private boolean myLike;
        private boolean myStar;
        private String featured_image;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public AuthorBean getAuthor() {
            return author;
        }

        public void setAuthor(AuthorBean author) {
            this.author = author;
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

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getFeatured_media() {
            return featured_media;
        }

        public void setFeatured_media(int featured_media) {
            this.featured_media = featured_media;
        }

        public String getURL() {
            return URL;
        }

        public void setURL(String URL) {
            this.URL = URL;
        }

        public int getLikes() {
            return likes;
        }

        public void setLikes(int likes) {
            this.likes = likes;
        }

        public int getStars() {
            return stars;
        }

        public void setStars(int stars) {
            this.stars = stars;
        }

        public int getComments() {
            return comments;
        }

        public void setComments(int comments) {
            this.comments = comments;
        }

        public int getRead() {
            return read;
        }

        public void setRead(int read) {
            this.read = read;
        }

        public boolean isMyLike() {
            return myLike;
        }

        public void setMyLike(boolean myLike) {
            this.myLike = myLike;
        }

        public boolean isMyStar() {
            return myStar;
        }

        public void setMyStar(boolean myStar) {
            this.myStar = myStar;
        }

        public String getFeatured_image() {
            return featured_image;
        }

        public void setFeatured_image(String featured_image) {
            this.featured_image = featured_image;
        }

        public static class AuthorBean {
            /**
             * id : 5b66bbf64238af6a804aacaf
             * name : 氢泡泡
             * avatar_URL : http://0.gravatar.com/avatar/c128c9f6bdce5ca00de551f2689b8af8?s=96&d=mm&r=g
             */

            private String id;
            private String name;
            private String avatar_URL;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getAvatar_URL() {
                return avatar_URL;
            }

            public void setAvatar_URL(String avatar_URL) {
                this.avatar_URL = avatar_URL;
            }
        }
    }
}
