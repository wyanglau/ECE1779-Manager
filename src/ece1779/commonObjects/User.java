package ece1779.commonObjects;

import java.util.ArrayList;
import java.util.List;

public class User {

	private int id;
	private String userName;
	private List<Images> imgs;

	public User(int id, String userName, List<Images> imgs) {

		this.id = id;

		this.userName = userName;

		if (imgs == null) {
			this.imgs = new ArrayList<Images>();
		} else {
			this.imgs = imgs;
		}
	}

	public List<Images> getImgs() {
		return imgs;
	}

	public void setImgs(List<Images> imgs) {
		this.imgs = imgs;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void addImage(Images img) {
		this.imgs.add(img);
	}
}
