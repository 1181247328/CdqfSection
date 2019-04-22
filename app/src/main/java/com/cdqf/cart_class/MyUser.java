package com.cdqf.cart_class;

public class MyUser {
    private String id;
    private String name;
    private String phone;
    private String shopid;
    private String login_account;
    private String login_password;
    private String position_id;
    private String type;
    private String urgent_phone;
    private String urgent_name;
    private String id_card;
    private String state;
    private String add_time;
    private String update_time;
    private String serviceAll;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getShopid() {
        return shopid;
    }

    public void setShopid(String shopid) {
        this.shopid = shopid;
    }

    public String getLogin_account() {
        return login_account;
    }

    public void setLogin_account(String login_account) {
        this.login_account = login_account;
    }

    public String getLogin_password() {
        return login_password;
    }

    public void setLogin_password(String login_password) {
        this.login_password = login_password;
    }

    public String getPosition_id() {
        return position_id;
    }

    public void setPosition_id(String position_id) {
        this.position_id = position_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrgent_phone() {
        return urgent_phone;
    }

    public void setUrgent_phone(String urgent_phone) {
        this.urgent_phone = urgent_phone;
    }

    public String getUrgent_name() {
        return urgent_name;
    }

    public void setUrgent_name(String urgent_name) {
        this.urgent_name = urgent_name;
    }

    public String getId_card() {
        return id_card;
    }

    public void setId_card(String id_card) {
        this.id_card = id_card;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getServiceAll() {
        return serviceAll;
    }

    public void setServiceAll(String serviceAll) {
        this.serviceAll = serviceAll;
    }

    public class Serviceall {

        private String months;
        private String count;

        public String getMonths() {
            return months;
        }

        public void setMonths(String months) {
            this.months = months;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }
    }
}
