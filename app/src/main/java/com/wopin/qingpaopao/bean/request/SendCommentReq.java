package com.wopin.qingpaopao.bean.request;

public class SendCommentReq {

    /**
     * postId : 123
     * content : abca
     * parent : 12141
     */

    private int postId;
    private String content;
    private int parent;

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }
}
