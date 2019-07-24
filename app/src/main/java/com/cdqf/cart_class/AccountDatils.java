package com.cdqf.cart_class;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class AccountDatils {
    private int id;
    private String type;
    private String describe;
    private String status;
    private String examine_price;
    private int staff_id;
    private int shop_id;
    private String login_account;
    private String shopname;
    private List<Img> img = new CopyOnWriteArrayList<>();
    private String shop_new_name;
//    private List<String> examines_img;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getExamine_price() {
        return examine_price;
    }

    public void setExamine_price(String examine_price) {
        this.examine_price = examine_price;
    }

    public int getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(int staff_id) {
        this.staff_id = staff_id;
    }

    public int getShop_id() {
        return shop_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }

    public String getLogin_account() {
        return login_account;
    }

    public void setLogin_account(String login_account) {
        this.login_account = login_account;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getShop_new_name() {
        return shop_new_name;
    }

    public void setShop_new_name(String shop_new_name) {
        this.shop_new_name = shop_new_name;
    }

    public List<Img> getImg() {
        return img;
    }

    public void setImg(List<Img> img) {
        this.img = img;
    }

    public class Img {
        private String image;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }
}
