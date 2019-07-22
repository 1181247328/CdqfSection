package com.cdqf.cart_class;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class User {
    private int id;
    private String username;
    private String name;
    private String avatar;
    private String remember_token;
    private String created_at;
    private String updated_at;
    private String phone;
    private int shop_id;
    private String urgent_name;
    private String urgent_phone;
    private String id_card;
    private String idcard_file_just;
    private String idcard_file_back;
    private int status;
    private String job_number;
    private String shopName;
    private int is_shop_manage;
    private String password;
    private UserRoles user_roles = null;
    private List<String> permission = new CopyOnWriteArrayList<>();
    private List<Menu> menu = new CopyOnWriteArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getRemember_token() {
        return remember_token;
    }

    public void setRemember_token(String remember_token) {
        this.remember_token = remember_token;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getShopid() {
        return shop_id;
    }

    public void setShopid(int shop_id) {
        this.shop_id = shop_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrgent_name() {
        return urgent_name;
    }

    public void setUrgent_name(String urgent_name) {
        this.urgent_name = urgent_name;
    }

    public String getUrgent_phone() {
        return urgent_phone;
    }

    public void setUrgent_phone(String urgent_phone) {
        this.urgent_phone = urgent_phone;
    }

    public String getId_card() {
        return id_card;
    }

    public void setId_card(String id_card) {
        this.id_card = id_card;
    }

    public String getIdcard_file_just() {
        return idcard_file_just;
    }

    public void setIdcard_file_just(String idcard_file_just) {
        this.idcard_file_just = idcard_file_just;
    }

    public String getIdcard_file_back() {
        return idcard_file_back;
    }

    public void setIdcard_file_back(String idcard_file_back) {
        this.idcard_file_back = idcard_file_back;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getJob_number() {
        return job_number;
    }

    public void setJob_number(String job_number) {
        this.job_number = job_number;
    }

    public int getIs_shop_manage() {
        return is_shop_manage;
    }

    public void setIs_shop_manage(int is_shop_manage) {
        this.is_shop_manage = is_shop_manage;
    }

    public UserRoles getUser_roles() {
        return user_roles;
    }

    public void setUser_roles(UserRoles user_roles) {
        this.user_roles = user_roles;
    }

    public List<String> getPermission() {
        return permission;
    }

    public void setPermission(List<String> permission) {
        this.permission = permission;
    }

    public List<Menu> getMenu() {
        return menu;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public void setMenu(List<Menu> menu) {
        this.menu = menu;
    }

    public class UserRoles {

        private int id;
        private int role_id;
        private int user_id;
        private String created_at;
        private String updated_at;
        private RolesName roles_name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getRole_id() {
            return role_id;
        }

        public void setRole_id(int role_id) {
            this.role_id = role_id;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
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

        public RolesName getRoles_name() {
            return roles_name;
        }

        public void setRoles_name(RolesName roles_name) {
            this.roles_name = roles_name;
        }
    }

    public class RolesName {

        private int id;
        private String name;
        private String slug;
        private String created_at;
        private String updated_at;
        private int group_id;

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

        public String getSlug() {
            return slug;
        }

        public void setSlug(String slug) {
            this.slug = slug;
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

        public int getGroup_id() {
            return group_id;
        }

        public void setGroup_id(int group_id) {
            this.group_id = group_id;
        }
    }

    public class Menu {

        private String title;
        private String icon;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }
    }
}
