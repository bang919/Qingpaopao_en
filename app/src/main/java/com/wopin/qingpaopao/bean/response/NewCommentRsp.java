package com.wopin.qingpaopao.bean.response;

import java.io.Serializable;
import java.util.ArrayList;

public class NewCommentRsp extends NormalRsp implements Serializable {
    private static final long serialVersionUID = 1564842961L;

    /**
     * result : {"_id":"5bed6c24584aae46eb0f1357","userId":"5b99d45a4c1a744b372b9b39","time":0,"__v":0,"newComment":[{"id":143,"post":444,"parent":142,"author":19,"author_name":"chen-dao","avatar_URL":"http://wifi.h2popo.com:8081/images/1541419743266421541419743482.jpg","content":"<p>222<\/p>\n","date":"2018-11-15T20:52:51","type":"comment","postTitle":"測試","postThumbnail":"https://wifi.h2popo.com/wp-content/uploads/2018/09/dafault.jpg","_id":"5bed6c24584aae46eb0f1358"}]}
     */

    private ResultBean result;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean implements Serializable {
        private static final long serialVersionUID = 156421342961L;
        /**
         * _id : 5bed6c24584aae46eb0f1357
         * userId : 5b99d45a4c1a744b372b9b39
         * time : 0
         * __v : 0
         * newComment : [{"id":143,"post":444,"parent":142,"author":19,"author_name":"chen-dao","avatar_URL":"http://wifi.h2popo.com:8081/images/1541419743266421541419743482.jpg","content":"<p>222<\/p>\n","date":"2018-11-15T20:52:51","type":"comment","postTitle":"測試","postThumbnail":"https://wifi.h2popo.com/wp-content/uploads/2018/09/dafault.jpg","_id":"5bed6c24584aae46eb0f1358"}]
         */

        private String _id;
        private String userId;
        private int time;
        private int __v;
        private ArrayList<NewCommentBean> newComment;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public int get__v() {
            return __v;
        }

        public void set__v(int __v) {
            this.__v = __v;
        }

        public ArrayList<NewCommentBean> getNewComment() {
            return newComment;
        }

        public void setNewComment(ArrayList<NewCommentBean> newComment) {
            this.newComment = newComment;
        }

        public static class NewCommentBean implements Serializable {
            private static final long serialVersionUID = 159452342961L;
            /**
             * id : 143
             * post : 444
             * parent : 142
             * author : 19
             * author_name : chen-dao
             * avatar_URL : http://wifi.h2popo.com:8081/images/1541419743266421541419743482.jpg
             * content : <p>222</p>
             * <p>
             * date : 2018-11-15T20:52:51
             * type : comment
             * postTitle : 測試
             * postThumbnail : https://wifi.h2popo.com/wp-content/uploads/2018/09/dafault.jpg
             * _id : 5bed6c24584aae46eb0f1358
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
            private String postTitle;
            private String postThumbnail;
            private String _id;

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

            public String getPostTitle() {
                return postTitle;
            }

            public void setPostTitle(String postTitle) {
                this.postTitle = postTitle;
            }

            public String getPostThumbnail() {
                return postThumbnail;
            }

            public void setPostThumbnail(String postThumbnail) {
                this.postThumbnail = postThumbnail;
            }

            public String get_id() {
                return _id;
            }

            public void set_id(String _id) {
                this._id = _id;
            }
        }
    }
}
