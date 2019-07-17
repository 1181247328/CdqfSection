package com.cdqf.cart_class;

public class ThroughsJudge {
    private int id;
    private int examine_id;
    private int admin_id;
    private String created_at;
    private AuditsJudge.Examines examines;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getExamine_id() {
        return examine_id;
    }

    public void setExamine_id(int examine_id) {
        this.examine_id = examine_id;
    }

    public int getAdmin_id() {
        return admin_id;
    }

    public void setAdmin_id(int admin_id) {
        this.admin_id = admin_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public AuditsJudge.Examines getExamines() {
        return examines;
    }

    public void setExamines(AuditsJudge.Examines examines) {
        this.examines = examines;
    }

    public class Examines {
        private int id;
        private String type;
        private String examine_price;
        private String describe;
        private int staff_id;
        private String status;
        private String name;

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

        public String getExamine_price() {
            return examine_price;
        }

        public void setExamine_price(String examine_price) {
            this.examine_price = examine_price;
        }

        public String getDescribe() {
            return describe;
        }

        public void setDescribe(String describe) {
            this.describe = describe;
        }

        public int getStaff_id() {
            return staff_id;
        }

        public void setStaff_id(int staff_id) {
            this.staff_id = staff_id;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
