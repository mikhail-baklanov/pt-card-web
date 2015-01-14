package ru.relex.webClient.client.model;

import java.util.Date;

import com.google.gwt.json.client.JSONObject;

public class PassInfo {

	public static enum UserStatus {
		NONE("none", "пусто"), WORK("work", "на работе"), AWAY("away", "ушел"), ABSENT(
				"absent", "отошел");
		private String value;
		private String name;

		private UserStatus(String value, String name) {
			this.value = value;
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public static UserStatus mvalueOf(String value) {
			UserStatus result = NONE;
			for (UserStatus userStatus : UserStatus.values()) {
				if (userStatus.value.equalsIgnoreCase(value)) {
					result = userStatus;
					break;
				}
			}
			return result;
		}
	}

	private int id;
	private int userId;
	private String firstName;
	private String middleName;
	private String lastName;
	private UserStatus status;
	private Date passTime;

	public void setId(int id) {
		this.id = id;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setStatus(UserStatus status) {
		this.status = status;
	}

	public void setPassTime(Date passTime) {
		this.passTime = passTime;
	}

	public int getId() {
		return id;
	}

	public int getUserId() {
		return userId;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public UserStatus getStatus() {
		return status;
	}

	public Date getPassTime() {
		return passTime;
	}

	@Override
	public String toString() {
		return "PassInfo [id=" + id + ", userId=" + userId + ", firstName="
				+ firstName + ", middleName=" + middleName + ", lastName="
				+ lastName + ", status=" + status + ", passTime=" + passTime
				+ "]";
	}

  public static PassInfo fromJSONObject(JSONObject json) {
    PassInfo passInfo = new PassInfo();
    passInfo.setId((int) json.get("id").isNumber().doubleValue());
    passInfo.setFirstName(json.get("firstName").isString().stringValue());
    passInfo.setLastName(json.get("lastName").isString().stringValue());
    passInfo.setMiddleName(json.get("middleName").isString().stringValue());
    double time = json.get("passTime").isNumber().doubleValue();
    passInfo.setPassTime(new Date((new Double(time)).longValue()));
    UserStatus status = UserStatus.mvalueOf(json.get("status").isString().stringValue());
    passInfo.setStatus(status);
    return passInfo;
  }

}
