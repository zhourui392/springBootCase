package cn.zz.user.entity;

import cn.zz.user.entity.base.BaseUser;

public class User extends BaseUser {

	public static final byte STAUS_NORMAL = 0;
	public static final byte STATUS_DISABLE = 1;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		User user = (User) o;
		if (getId() != null ? !getId().equals(user.getId()) : user.getId() != null) return false;
		return true;
	}

	@Override
	public int hashCode() {
		return getId() != null ? getId().hashCode() : 0;
	}

	@Override
	public String toString() {
		return "User{" +
				"id=" + getId() +
				", showname=" + getShowName() +
				", username='" + getUsername() + '\'' +
				", password='" + getPassword() + '\'' +
				", status=" + getStatus() +
				'}';
	}
}
	
