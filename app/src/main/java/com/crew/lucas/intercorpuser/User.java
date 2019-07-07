package com.crew.lucas.intercorpuser;

public class User {

    private String mName;
    private String mLastName;
    private int mAge;
    private String mBirthdate;

    public User(String mName, String mLastName, int mAge, String mBirthdate) {
        this.mName = mName;
        this.mLastName = mLastName;
        this.mAge = mAge;
        this.mBirthdate = mBirthdate;
    }

    public User() {
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String lastName) {
        this.mLastName = lastName;
    }

    public int getAge() {
        return mAge;
    }

    public void setAge(int age) {
        this.mAge = age;
    }

    public String getBirthdate() {
        return mBirthdate;
    }

    public void setBirthdate(String birthdate) {
        this.mBirthdate = birthdate;
    }
}
