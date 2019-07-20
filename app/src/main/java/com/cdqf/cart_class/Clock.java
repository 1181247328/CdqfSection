package com.cdqf.cart_class;

import java.util.ArrayList;
import java.util.List;

public class Clock {

    private String tips;
    private long display_time;
    private Shop shop;
    private List<GoWork> go_work = new ArrayList<>();

    private List<GoWork> leave_work = new ArrayList<>();

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public long getDisplay_time() {
        return display_time;
    }

    public void setDisplay_time(long display_time) {
        this.display_time = display_time;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public List<GoWork> getGo_work() {
        return go_work;
    }

    public void setGo_work(List<GoWork> go_work) {
        this.go_work = go_work;
    }

    public List<GoWork> getLeave_work() {
        return leave_work;
    }

    public void setLeave_work(List<GoWork> leave_work) {
        this.leave_work = leave_work;
    }

    public class Shop {

        private int id;
        private String name;
        private String longitude;
        private String latitude;

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

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }
    }

    public class GoWork {

        private int id;
        private int admin_id;
        private int shop_id;
        private String amap_lng;
        private String amap_lat;
        private int status;
        private int work_status;
        private int position_status;
        private String image;
        private String address;
        private String remarks;
        private int platform;
        private int plan_id;
        private String created_at;
        private String updated_at;
        private String deleted_at;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getAdmin_id() {
            return admin_id;
        }

        public void setAdmin_id(int admin_id) {
            this.admin_id = admin_id;
        }

        public int getShop_id() {
            return shop_id;
        }

        public void setShop_id(int shop_id) {
            this.shop_id = shop_id;
        }

        public String getAmap_lng() {
            return amap_lng;
        }

        public void setAmap_lng(String amap_lng) {
            this.amap_lng = amap_lng;
        }

        public String getAmap_lat() {
            return amap_lat;
        }

        public void setAmap_lat(String amap_lat) {
            this.amap_lat = amap_lat;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getWork_status() {
            return work_status;
        }

        public void setWork_status(int work_status) {
            this.work_status = work_status;
        }

        public int getPosition_status() {
            return position_status;
        }

        public void setPosition_status(int position_status) {
            this.position_status = position_status;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public int getPlatform() {
            return platform;
        }

        public void setPlatform(int platform) {
            this.platform = platform;
        }

        public int getPlan_id() {
            return plan_id;
        }

        public void setPlan_id(int plan_id) {
            this.plan_id = plan_id;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public String getDeleted_at() {
            return deleted_at;
        }

        public void setDeleted_at(String deleted_at) {
            this.deleted_at = deleted_at;
        }
    }
}
