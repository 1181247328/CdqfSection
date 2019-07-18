package com.cdqf.cart_class;

public class Memebersship {
    private int userid;
    private String balance;
    private UserCar user_car = new UserCar();

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public UserCar getUser_car() {
        return user_car;
    }

    public void setUser_car(UserCar user_car) {
        this.user_car = user_car;
    }

    public class UserCar {

        private int id;
        private String phone;
        private String addtime;
        private GetCar get_car = new GetCar();

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

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public GetCar getGet_car() {
            return get_car;
        }

        public void setGet_car(GetCar get_car) {
            this.get_car = get_car;
        }
    }

    public class GetCar {
        private int id;
        private int userid;
        private String carnum;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUserid() {
            return userid;
        }

        public void setUserid(int userid) {
            this.userid = userid;
        }

        public String getCarnum() {
            return carnum;
        }

        public void setCarnum(String carnum) {
            this.carnum = carnum;
        }
    }
}
