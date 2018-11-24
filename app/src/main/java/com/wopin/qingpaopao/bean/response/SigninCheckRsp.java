package com.wopin.qingpaopao.bean.response;

public class SigninCheckRsp extends NormalRsp {

    /**
     * result : {"signinCup":false}
     */

    private ResultBean result;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * signinCup : false
         */

        private boolean signinCup;

        public boolean isSigninCup() {
            return signinCup;
        }

        public void setSigninCup(boolean signinCup) {
            this.signinCup = signinCup;
        }
    }
}
