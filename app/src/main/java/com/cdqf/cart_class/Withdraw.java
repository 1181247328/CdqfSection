package com.cdqf.cart_class;

public class Withdraw {
    private int id;
    private String created_at;
    private String examine_price;
    private int staff_id;
    private String login_account;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
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

    public String getLogin_account() {
        return login_account;
    }

    public void setLogin_account(String login_account) {
        this.login_account = login_account;
    }
}
