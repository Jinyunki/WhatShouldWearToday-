package com.example.weathertest;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class WeatherResponse {
    @SerializedName("response")
    private Response response;

    public WeatherResponse() {}

    public WeatherResponse(Response response) {
        this.response = response;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public class Response {
        @SerializedName("header")
        Header header;
        @SerializedName("body")
        Body body;

        public Response() {}

        public Response(Header header, Body body) {
            this.header = header;
            this.body = body;
        }

        public Header getHeader() {
            return header;
        }

        public void setHeader(Header header) {
            this.header = header;
        }

        public Body getBody() {
            return body;
        }

        public void setBody(Body body) {
            this.body = body;
        }

        public class Header {
            @SerializedName("resultCode")
            String resultCode;
            @SerializedName("resultMsg")
            String resultMsg;

            public Header() {}

            public Header(String resultCode, String resultMsg) {
                this.resultCode = resultCode;
                this.resultMsg = resultMsg;
            }

            public String getResultCode() {
                return resultCode;
            }

            public void setResultCode(String resultCode) {
                this.resultCode = resultCode;
            }

            public String getResultMsg() {
                return resultMsg;
            }

            public void setResultMsg(String resultMsg) {
                this.resultMsg = resultMsg;
            }
        }

    }

    public class Body {
        @SerializedName("dataType")
        private String dataType;
        @SerializedName("items")
        private Items items;
        @SerializedName("pageNo")
        private int pageNo;
        @SerializedName("numOfRows")
        private int numOfRows;
        @SerializedName("totalCount")
        private int totalCount;

        public Body() {}

        public Body(String dataType, Items items, int pageNo, int numOfRows, int totalCount) {
            this.dataType = dataType;
            this.items = items;
            this.pageNo = pageNo;
            this.numOfRows = numOfRows;
            this.totalCount = totalCount;
        }

        public String getDataType() {
            return dataType;
        }

        public void setDataType(String dataType) {
            this.dataType = dataType;
        }

        public Items getItems() {
            return items;
        }

        public void setItems(Items items) {
            this.items = items;
        }

        public int getPageNo() {
            return pageNo;
        }

        public void setPageNo(int pageNo) {
            this.pageNo = pageNo;
        }

        public int getNumOfRows() {
            return numOfRows;
        }

        public void setNumOfRows(int numOfRows) {
            this.numOfRows = numOfRows;
        }

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }
    }

    public class Items {
        @SerializedName("item")
        private ArrayList<Item> item;

        public Items() {}

        public Items(ArrayList<Item> item) {
            this.item = item;
        }

        public ArrayList<Item> getItem() {
            return item;
        }

        public void setItem(ArrayList<Item> item) {
            this.item = item;
        }

        public class Item {
            private String baseDate;
            private String baseTime;
            private String category;
            private String fcstDate;
            private String fcstTime;
            private String fcstValue;
            private int nx;
            private int ny;

            public Item() {}

            public Item(String baseDate, String baseTime, String category, String fcstDate, String fcstTime, String fcstValue, int nx, int ny) {
                this.baseDate = baseDate;
                this.baseTime = baseTime;
                this.category = category;
                this.fcstDate = fcstDate;
                this.fcstTime = fcstTime;
                this.fcstValue = fcstValue;
                this.nx = nx;
                this.ny = ny;
            }

            public String getBaseDate() {
                return baseDate;
            }

            public void setBaseDate(String baseDate) {
                this.baseDate = baseDate;
            }

            public String getBaseTime() {
                return baseTime;
            }

            public void setBaseTime(String baseTime) {
                this.baseTime = baseTime;
            }

            public String getCategory() {
                return category;
            }

            public void setCategory(String category) {
                this.category = category;
            }

            public String getFcstDate() {
                return fcstDate;
            }

            public void setFcstDate(String fcstDate) {
                this.fcstDate = fcstDate;
            }

            public String getFcstTime() {
                return fcstTime;
            }

            public void setFcstTime(String fcstTime) {
                this.fcstTime = fcstTime;
            }

            public String getFcstValue() {
                return fcstValue;
            }

            public void setFcstValue(String fcstValue) {
                this.fcstValue = fcstValue;
            }

            public int getNx() {
                return nx;
            }

            public void setNx(int nx) {
                this.nx = nx;
            }

            public int getNy() {
                return ny;
            }

            public void setNy(int ny) {
                this.ny = ny;
            }
        }

    }
}
