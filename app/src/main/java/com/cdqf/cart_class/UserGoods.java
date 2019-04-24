package com.cdqf.cart_class;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class UserGoods {
    private String name;
    private String classifyid;
    private List<Data> data = new CopyOnWriteArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassifyid() {
        return classifyid;
    }

    public void setClassifyid(String classifyid) {
        this.classifyid = classifyid;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public class Data {
        private String id;
        private String price;
        private String goodsname;
        private String classifyid;
        private boolean isSelect = false;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getGoodsname() {
            return goodsname;
        }

        public void setGoodsname(String goodsname) {
            this.goodsname = goodsname;
        }

        public String getClassifyid() {
            return classifyid;
        }

        public void setClassifyid(String classifyid) {
            this.classifyid = classifyid;
        }

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }
    }
}
