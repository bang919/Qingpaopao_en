package com.wopin.qingpaopao.bean.response;

public class BlogPostRsp extends NormalRsp {

    /**
     * result : {"id":387,"date":"2018-09-17T13:55:20","title":"Test pic","content":"<p><a href=\"http://wifi.h2popo.com:8081/images/AABAF93B-74E9-47F7-98EF-0F26A429EA4F.jpg\"><img src=\"http://wifi.h2popo.com:8081/images/AABAF93B-74E9-47F7-98EF-0F26A429EA4F.jpg\" class=\"size-full\"><\/a><\/p>\n","URL":"https://wifi.h2popo.com/2018/09/17/test-pic/"}
     */

    private BlogPostBean result;

    public BlogPostBean getResult() {
        return result;
    }

    public void setResult(BlogPostBean result) {
        this.result = result;
    }

    public static class BlogPostBean {
        /**
         * id : 387
         * date : 2018-09-17T13:55:20
         * title : Test pic
         * content : <p><a href="http://wifi.h2popo.com:8081/images/AABAF93B-74E9-47F7-98EF-0F26A429EA4F.jpg"><img src="http://wifi.h2popo.com:8081/images/AABAF93B-74E9-47F7-98EF-0F26A429EA4F.jpg" class="size-full"></a></p>
         * <p>
         * URL : https://wifi.h2popo.com/2018/09/17/test-pic/
         */

        private int id;
        private String date;
        private String title;
        private String content;
        private String URL;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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

        public String getURL() {
            return URL;
        }

        public void setURL(String URL) {
            this.URL = URL;
        }
    }
}
