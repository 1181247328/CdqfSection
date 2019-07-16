package com.cdqf.cart_class;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Datils {
    private int id;
    private String ordernum;
    private int userid;
    private String phone;
    private String carnum;
    private String remarks;
    private String zongprice;
    private String addtime;
    private int cartype;
    private int pay_type;
    private String car_type_name;
    private String goods_names;
    private int cost_price;
    private List<String> staff_sevice = new CopyOnWriteArrayList<>();
    private List<UserRemarks> user_remarks = new CopyOnWriteArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrdernum() {
        return ordernum;
    }

    public void setOrdernum(String ordernum) {
        this.ordernum = ordernum;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCarnum() {
        return carnum;
    }

    public void setCarnum(String carnum) {
        this.carnum = carnum;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getZongprice() {
        return zongprice;
    }

    public void setZongprice(String zongprice) {
        this.zongprice = zongprice;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public int getCartype() {
        return cartype;
    }

    public void setCartype(int cartype) {
        this.cartype = cartype;
    }

    public int getPay_type() {
        return pay_type;
    }

    public void setPay_type(int pay_type) {
        this.pay_type = pay_type;
    }

    public String getCar_type_name() {
        return car_type_name;
    }

    public void setCar_type_name(String car_type_name) {
        this.car_type_name = car_type_name;
    }

    public String getGoods_names() {
        return goods_names;
    }

    public void setGoods_names(String goods_names) {
        this.goods_names = goods_names;
    }

    public int getCost_price() {
        return cost_price;
    }

    public void setCost_price(int cost_price) {
        this.cost_price = cost_price;
    }

    public List<String> getStaff_sevice() {
        return staff_sevice;
    }

    public void setStaff_sevice(List<String> staff_sevice) {
        this.staff_sevice = staff_sevice;
    }

    public List<UserRemarks> getUser_remarks() {
        return user_remarks;
    }

    public void setUser_remarks(List<UserRemarks> user_remarks) {
        this.user_remarks = user_remarks;
    }

    public class UserRemarks {
        private int id;
        private String content;
        private int staff_id;
        private String created_at;
        private String staff_name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getStaff_id() {
            return staff_id;
        }

        public void setStaff_id(int staff_id) {
            this.staff_id = staff_id;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getStaff_name() {
            return staff_name;
        }

        public void setStaff_name(String staff_name) {
            this.staff_name = staff_name;
        }
    }
}
