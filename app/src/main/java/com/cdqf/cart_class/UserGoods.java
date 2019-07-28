package com.cdqf.cart_class;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class UserGoods {
    private int id;
    private String name;
    private List<Children> children = new CopyOnWriteArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Children> getChildren() {
        return children;
    }

    public void setChildren(List<Children> children) {
        this.children = children;
    }

    public class Children {
        private int id;
        private String goodsname;
        private String active_price;
        private String price;
        private int classifyid;

        private boolean isSelect = false;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getGoodsname() {
            return goodsname;
        }

        public void setGoodsname(String goodsname) {
            this.goodsname = goodsname;
        }

        public String getActive_price() {
            return active_price;
        }

        public void setActive_price(String active_price) {
            this.active_price = active_price;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public int getClassifyid() {
            return classifyid;
        }

        public void setClassifyid(int classifyid) {
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
