package cn.devmgr.sample.domain;


import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

public class User  implements Principal {
	private String name;
	private int id;
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	

	/**
	 * 
	 * @return
	 */
	public List<String> getRoles(){
		 //TODO:
		 List<String> roles = new ArrayList<String>();
		 roles.add("user");
		 return roles;
	}
}
