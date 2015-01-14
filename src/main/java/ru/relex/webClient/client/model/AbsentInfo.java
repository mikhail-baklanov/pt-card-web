package ru.relex.webClient.client.model;

import java.util.Date;

import com.google.gwt.json.client.JSONObject;

public class AbsentInfo {

	public static enum AbsentType {
		WORK("work"), PERSONAL("personal");
		private String value;

		private AbsentType(String value) {
			this.value = value;
		}

		public static AbsentType mvalueOf(String value) {
			AbsentType result = PERSONAL;
			for (AbsentType userStatus : AbsentType.values()) {
				if (userStatus.value.equalsIgnoreCase(value)) {
					result = userStatus;
					break;
				}
			}
			return result;
		}
	}

	private int userId;
	private int absentTimeMin;
	private Date passTime;
	private AbsentType absentType;
	private String firstName;
	private String middleName;
	private String lastName;

	public int getAbsentTimeMin() {
		return absentTimeMin;
	}

	public void setAbsentTimeMin(int absentTimeMin) {
		this.absentTimeMin = absentTimeMin;
	}

	public String getAvatarUrl() {
		return "/users/avatar?userId=" + userId;
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

	public void setAbsentType(AbsentType absentType) {
		this.absentType = absentType;
	}

	public void setPassTime(Date passTime) {
		this.passTime = passTime;
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

	public AbsentType getAbsentType() {
		return absentType;
	}

	public Date getPassTime() {
		return passTime;
	}

  public static AbsentInfo fromJSONObject(JSONObject user) {
    AbsentInfo absentInfo = new AbsentInfo();
    absentInfo.setFirstName(user.get("firstName").isString().stringValue());
    absentInfo.setMiddleName(user.get("middleName").isString().stringValue());
    absentInfo.setLastName(user.get("lastName").isString().stringValue());
    absentInfo.setAbsentTimeMin((int) user.get("absentTime").isNumber().doubleValue());
    int userId = (int) user.get("userId").isNumber().doubleValue();
    absentInfo.setUserId(userId);
    Date date = new Date((long) user.get("passTime").isNumber().doubleValue());
    absentInfo.setPassTime(date);
    return absentInfo;
  }

}
