package com.cdqf.cart_class;

import java.util.ArrayList;
import java.util.List;

public class MembersDatils {
    private int id;
    private String phone;
    private String balance_price;
    private String payment_price;
    private String use_price;
    private List<String> remarks = new ArrayList<>();
    private List<Service> service = new ArrayList<>();
    private List<GetCars> get_cars = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBalance_price() {
        return balance_price;
    }

    public void setBalance_price(String balance_price) {
        this.balance_price = balance_price;
    }

    public String getPayment_price() {
        return payment_price;
    }

    public void setPayment_price(String payment_price) {
        this.payment_price = payment_price;
    }

    public String getUse_price() {
        return use_price;
    }

    public void setUse_price(String use_price) {
        this.use_price = use_price;
    }

    public List<String> getRemarks() {
        return remarks;
    }

    public void setRemarks(List<String> remarks) {
        this.remarks = remarks;
    }

    public List<Service> getService() {
        return service;
    }

    public void setService(List<Service> service) {
        this.service = service;
    }

    public List<GetCars> getGet_cars() {
        return get_cars;
    }

    public void setGet_cars(List<GetCars> get_cars) {
        this.get_cars = get_cars;
    }

    public class GetCars {
        private String carnum;
        private int cartype;
        private int userid;

        public String getCarnum() {
            return carnum;
        }

        public void setCarnum(String carnum) {
            this.carnum = carnum;
        }

        public int getCartype() {
            return cartype;
        }

        public void setCartype(int cartype) {
            this.cartype = cartype;
        }

        public int getUserid() {
            return userid;
        }

        public void setUserid(int userid) {
            this.userid = userid;
        }
    }

    public class Service {
        private int id;
        private String ordernum;
        private String carnum;
        private String addtime;
        private int pay_type;
        private int cartype;
        private String zongprice;

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

        public String getCarnum() {
            return carnum;
        }

        public void setCarnum(String carnum) {
            this.carnum = carnum;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public int getPay_type() {
            return pay_type;
        }

        public void setPay_type(int pay_type) {
            this.pay_type = pay_type;
        }

        public int getCartype() {
            return cartype;
        }

        public void setCartype(int cartype) {
            this.cartype = cartype;
        }

        public String getZongprice() {
            return zongprice;
        }

        public void setZongprice(String zongprice) {
            this.zongprice = zongprice;
        }
    }
}
