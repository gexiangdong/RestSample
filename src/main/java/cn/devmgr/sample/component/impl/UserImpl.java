package cn.devmgr.sample.component.impl;

import java.util.List;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.devmgr.sample.component.Login;


public class UserImpl implements Login {
	private static final Log log = LogFactory.getLog(UserImpl.class);
	private int id;
	private String name;
	private List<String> roles;
	
	public UserImpl(){
		if(log.isTraceEnabled()){
			log.trace("User constructor()....");
		}
		this.id = new Random().nextInt();
	}

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
	
	
	@Override
	public String toString(){
		return "User{id:" + id + ";name:" + name;
	}

	@Override
	public List<String> getRoles() {
		return roles;
	}
	
	public void setRoles(List<String> roles){
		this.roles = roles;
	}
}
