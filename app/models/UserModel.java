package models;

import play.db.jpa.JPA;

import javax.persistence.*;

import java.sql.Date;
import at.ac.tuwien.big.we15.lab2.api.*;
import at.ac.tuwien.big.we15.lab2.api.User;


@Entity
public class UserModel implements User {

    @Id
    private String username;
    private String password;
    private Avatar avatar;
    private String firstName;
    private String lastName;
    private Date birthdate;
	/*
	* sex: female=0, male=1
	*/
	private boolean sex;
	
	public UserModel() {}
	
	@Override
    public String getName() {
        return username;
    }

    @Override
    public void setName(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
	
    @Override
    public Avatar getAvatar() {
        return avatar;
    }

    @Override
    public void setAvatar(Avatar avatar) {
        this.avatar = avatar;
    }
	
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public boolean getSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }
	
	public String toString() {
		return username + password + firstName + lastName + sex;
	}
}