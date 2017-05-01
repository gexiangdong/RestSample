package cn.devmgr.sample.component;

import java.util.List;

public interface Login{
	public int getId();
	public void setId(int id);
	public String getName();
	public void setName(String name);
	
	public List<String> getRoles();
}
