package me.richard.note.net.entity;

/**
 * Created by James
 * Date 2018/1/30.
 * description
 */

public class SignInEntity extends BaseEntity {

    /**
     * msg : 欢迎归来
     * data : {"user":{"id":1,"name":"richae0x1.62e798p40","age":"\u201c\u201d","sex":0,"header":"http://oow561q5i.bkt.clouddn.com/703de9f4b2264a16b9710402308f5849.jpg"},"token":"eyJhbGciOiJIUzUxMiJ9.eyJ1c2VyX25hbWUiOiI5ODVkNTA3OTY2ZXciLCJjcmVhdGVfZGF0ZSI6MTUzMzYwOTgyNzAzOCwiZXhwIjoxNTM0MjE0NjI3fQ.UpdI1ErxkAfXrbTa8HDl5Zgtdrw6AfetLWgbXAu4Egm92ZIXmHNsu1LB5LGV5ZbFrrPtnfN13ry4M0vHG8wVMg"}
     */

    private String msg;
    private DataBean data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * user : {"id":1,"name":"richae0x1.62e798p40","age":"\u201c\u201d","sex":0,"header":"http://oow561q5i.bkt.clouddn.com/703de9f4b2264a16b9710402308f5849.jpg"}
         * token : eyJhbGciOiJIUzUxMiJ9.eyJ1c2VyX25hbWUiOiI5ODVkNTA3OTY2ZXciLCJjcmVhdGVfZGF0ZSI6MTUzMzYwOTgyNzAzOCwiZXhwIjoxNTM0MjE0NjI3fQ.UpdI1ErxkAfXrbTa8HDl5Zgtdrw6AfetLWgbXAu4Egm92ZIXmHNsu1LB5LGV5ZbFrrPtnfN13ry4M0vHG8wVMg
         */

        private UserBean user;
        private String token;

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public static class UserBean {
            /**
             * id : 1
             * name : richae0x1.62e798p40
             * age : “”
             * sex : 0
             * header : http://oow561q5i.bkt.clouddn.com/703de9f4b2264a16b9710402308f5849.jpg
             */

            private int id;
            private String name;
            private String age;
            private int sex;
            private String header;

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

            public String getAge() {
                return age;
            }

            public void setAge(String age) {
                this.age = age;
            }

            public int getSex() {
                return sex;
            }

            public void setSex(int sex) {
                this.sex = sex;
            }

            public String getHeader() {
                return header;
            }

            public void setHeader(String header) {
                this.header = header;
            }
        }
    }
}
