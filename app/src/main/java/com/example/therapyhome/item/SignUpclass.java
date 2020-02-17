package com.example.therapyhome.item;

public class SignUpclass {

    /**
     * strSignUpName  // 이름 (name)
     * strSignUpId  // 아이디 (id)
     * strSignUpPwd // 비밀번호 (pwd)
     * strSignUpNum // 휴대폰 (num)
     * strSignUpComId // 의료진 아이디 (comId)
     */

    String name;
    String id;
    String pwd;
    String num;
    String com;
    String comId;
    // 파이어베이스에서는 꼭 공용의 빈 생성자가 필요하다
    // 지우지 말것!!!
    public SignUpclass() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getCom() {
        return com;
    }

    public void setCom(String com) {
        this.com = com;
    }

    public String getComId() {
        return comId;
    }

    public void setComId(String comId) {
        this.comId = comId;
    }

    public SignUpclass(String name, String id, String pwd, String num, String com, String comId) {
        this.name = name;
        this.id = id;
        this.pwd = pwd;
        this.num = num;
        this.com = com;
        this.comId = comId;
    }
}
