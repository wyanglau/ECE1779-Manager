package ece1779.commonObjects;

import java.util.List;

public class Images {

	private int userId;
	private int imgId;
	private List<String> keys;

	/**
	 * if you don't have a imgId or userId, set -1;
	 * 
	 * @param userId
	 * @param imgId
	 * @param keys
	 */
	public Images(int userId, int imgId, List<String> keys) {

		this.setUserId(userId);
		this.imgId = imgId;
		this.keys = keys;

	}

	public int getImgId() {
		return imgId;
	}

	public void setImgId(int imgId) {
		this.imgId = imgId;
	}

	public List<String> getKeys() {
		return keys;
	}

	public void setKeys(List<String> keys) {
		this.keys = keys;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

}
