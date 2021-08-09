package com.company;

public class Users {
    int id;
    String username;
    String password;
    int money;
    String kuplenoe;
    public Users(){

    }
    public Users(int id,String username,String password,int money,String kuplenoe){
        this.id = id;
        this.username = username;
        this.password = password;
        this.money = money;
        this.kuplenoe = kuplenoe;
    }
    public int getMoney(){
        return money;
    }

    public int setMoney(int money){
        this.money = money;
        return money;
    }

    public int getId(){
        return id;
    }

    public int setId(){
        this.id = id;
        return id;
    }

    public String getUsername(){
        return username;
    }

    public String setUsername(String username){
        this.username = username;
        return username;
    }

    public String getPassword(){
        return password;
    }

    public String setPassword(String password){
        this.password = password;
        return password;
    }

    public String getKuplenoe(){
        return kuplenoe;
    }

    public String setKuplenoe(String kuplenoe){
        this.kuplenoe = kuplenoe;
        return kuplenoe;
    }

    @Override
    public String toString(){
        return "Users{" +
                "id = " + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", money='" + money + '\'' +
                ", kuplenoe='" + kuplenoe + '\'' +
                '}';
    }
}
