package com.wopin.qingpaopao.bean.response;

import java.util.ArrayList;

public class CommentRsp extends NormalRsp {

    private ArrayList<CommentBean> result;

    public ArrayList<CommentBean> getComments() {
        return result;
    }

    public void setComments(ArrayList<CommentBean> result) {
        this.result = result;
    }

    public static class CommentBean {
        /**
         * id : 39
         * post : 82
         * parent : 27
         * author : 14
         * author_name : Control.
         * avatar_URL : http://1.gravatar.com/avatar/440e1c664d8909679a88a3b54228e112?s=96&d=mm&r=g
         * content : <p>@HydeGuo ？？？</p>
         * <p>
         * date : 2018-09-18T17:06:38
         * type : comment
         * myLike : false
         */

        private int id;
        private int post;
        private int parent;
        private int author;
        private String author_name;
        private String avatar_URL;
        private String content;
        private String date;
        private String type;
        private boolean myLike;
        private ArrayList<CommentBean> followComments;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getPost() {
            return post;
        }

        public void setPost(int post) {
            this.post = post;
        }

        public int getParent() {
            return parent;
        }

        public void setParent(int parent) {
            this.parent = parent;
        }

        public int getAuthor() {
            return author;
        }

        public void setAuthor(int author) {
            this.author = author;
        }

        public String getAuthor_name() {
            return author_name;
        }

        public void setAuthor_name(String author_name) {
            this.author_name = author_name;
        }

        public String getAvatar_URL() {
            return avatar_URL;
        }

        public void setAvatar_URL(String avatar_URL) {
            this.avatar_URL = avatar_URL;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public boolean isMyLike() {
            return myLike;
        }

        public void setMyLike(boolean myLike) {
            this.myLike = myLike;
        }

        public void setFollowComment(ArrayList<CommentBean> comments) {
            followComments = comments;
        }

        public ArrayList<CommentBean> getFollowComment() {
            return followComments;
        }
    }
}
