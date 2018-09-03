package com.wopin.qingpaopao.bean.response;

import java.util.ArrayList;

public class CupListRsp extends NormalRsp {

    /**
     * status : 0
     * msg :
     * result : [{"_id":"5b8ad041c2e95a522c5e083f","userId":"5b819f0591297b561a79eeaa","name":"Hello ","type":"BLE","firstRegisterTime":"2018-09-02 1:45","registerTime":"2018-09-02 1:45","uuid":"F5548508-50D5-094B-3A9A-5288586D2F44","produceScores":1001,"__v":0},{"_id":"5b8b939fc2e95a522c5e0858","userId":"5b819f0591297b561a79eeaa","name":"Hello ","type":"BLE","firstRegisterTime":"2018-09-02 15:39","registerTime":"2018-09-02 15:39","uuid":"00001002-0000-1000-8000-00805f9b34fb","produceScores":1000,"__v":0}]
     */

    private ArrayList<CupBean> result;

    public ArrayList<CupBean> getResult() {
        return result;
    }

    public void setResult(ArrayList<CupBean> result) {
        this.result = result;
    }

    public static class CupBean {
        /**
         * _id : 5b8ad041c2e95a522c5e083f
         * userId : 5b819f0591297b561a79eeaa
         * name : Hello
         * type : BLE
         * firstRegisterTime : 2018-09-02 1:45
         * registerTime : 2018-09-02 1:45
         * uuid : F5548508-50D5-094B-3A9A-5288586D2F44
         * produceScores : 1001
         * __v : 0
         */

        private String _id;
        private String userId;
        private String name;
        private String type;
        private String firstRegisterTime;
        private String registerTime;
        private String uuid;
        private int produceScores;
        private int __v;
        private boolean isConnecting;

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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getFirstRegisterTime() {
            return firstRegisterTime;
        }

        public void setFirstRegisterTime(String firstRegisterTime) {
            this.firstRegisterTime = firstRegisterTime;
        }

        public String getRegisterTime() {
            return registerTime;
        }

        public void setRegisterTime(String registerTime) {
            this.registerTime = registerTime;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public int getProduceScores() {
            return produceScores;
        }

        public void setProduceScores(int produceScores) {
            this.produceScores = produceScores;
        }

        public int get__v() {
            return __v;
        }

        public void set__v(int __v) {
            this.__v = __v;
        }

        public boolean isConnecting() {
            return isConnecting;
        }

        public void setConnecting(boolean connecting) {
            isConnecting = connecting;
        }
    }
}
