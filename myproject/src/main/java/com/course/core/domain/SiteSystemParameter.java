package com.course.core.domain;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.course.core.support.Configurable;

/**
 * 
 * SiteSystemParameter 系统参数
 * 
 * 创 建 人：Ben.Fang
 * 
 * 日 期：2018年1月3日下午2:17:52
 * 
 * 版 本 号：1.0
 */
public class SiteSystemParameter implements Configurable {

	public static final String PREFIX = "sys_parameter_";
	/**
	 * 每周上课默认5天
	 */
	public static final int WEEK_DAYS_DEFAULT = 5;
	/**
	 * 每周上课天数
	 */
	public static final String WEEK_DAYS = PREFIX + "week_days";
	/**
	 * 会议提前XX分钟开始签到
	 */
	public static final String MEETING_SIGN_IN = PREFIX + "meeting_sign_in";
	/**
	 * 会议结束后XX分钟结束签退
	 */
	public static final String MEETING_SIGN_OUT = PREFIX + "meeting_sign_out";
	/**
	 * 会议开始后XX分钟开始记迟到
	 */
	public static final String MEETING_LATE = PREFIX + "meeting_late";
	/**
	 * 会议结束前XX分钟开始记早退
	 */
	public static final String MEETING_LATE_EARLY = PREFIX + "meeting_late_early";
	/**
	 * 是否必须签到
	 */
	public static final String MEETING_IS_SIGN_IN = PREFIX + "meeting_is_sign_in";
	/**
	 * 是否必须签退
	 */
	public static final String MEETING_IS_SIGN_OUT = PREFIX + "meeting_is_sign_out";
	/**
	 * 是否可以任签一次
	 */
	public static final String MEETING_IS_SIGN_ANY_TIMES = PREFIX + "meeting_is_sign_any_times";

	private Map<String, String> customs;

	public SiteSystemParameter() {
	}

	public SiteSystemParameter(Map<String, String> customs) {
		this.customs = customs;
	}

	public SiteSystemParameter(Site site) {
		this.customs = site.getCustoms();
	}

	public Integer getWeekDays() {
		String weekdays = getCustoms().get(WEEK_DAYS);
		if (StringUtils.isNotBlank(weekdays)) {
			return Integer.parseInt(weekdays);
		} else {
			return WEEK_DAYS_DEFAULT;
		}
	}

	public void setWeekDays(Integer weekdays) {
		if (weekdays != null) {
			getCustoms().put(WEEK_DAYS, weekdays.toString());
		} else {
			getCustoms().remove(weekdays);
		}
	}

	public void setMeetingSignIn(Integer meetingSignIn) {
		if (meetingSignIn != null) {
			getCustoms().put(MEETING_SIGN_IN, meetingSignIn.toString());
		} else {
			getCustoms().remove(meetingSignIn);
		}
	}

	public Integer getMeetingSignIn() {
		String meetingSignIn = getCustoms().get(MEETING_SIGN_IN);
		if (StringUtils.isNotBlank(meetingSignIn)) {
			return Integer.parseInt(meetingSignIn);
		}
		return null;
	}

	public void setMeetingSignOut(Integer meetingSignOut) {
		if (meetingSignOut != null) {
			getCustoms().put(MEETING_SIGN_OUT, meetingSignOut.toString());
		} else {
			getCustoms().remove(meetingSignOut);
		}
	}

	public Integer getMeetingSignOut() {
		String meetingSignOut = getCustoms().get(MEETING_SIGN_OUT);
		if (StringUtils.isNotBlank(meetingSignOut)) {
			return Integer.parseInt(meetingSignOut);
		}
		return null;
	}

	public void setMeetingLate(Integer meetingLate) {
		if (meetingLate != null) {
			getCustoms().put(MEETING_LATE, meetingLate.toString());
		} else {
			getCustoms().remove(meetingLate);
		}
	}

	public Integer getMeetingLate() {
		String meetingLate = getCustoms().get(MEETING_LATE);
		if (StringUtils.isNotBlank(meetingLate)) {
			return Integer.parseInt(meetingLate);
		}
		return null;
	}

	public void setMeetingLateEarly(Integer meetingLateEarly) {
		if (meetingLateEarly != null) {
			getCustoms().put(MEETING_LATE_EARLY, meetingLateEarly.toString());
		} else {
			getCustoms().remove(meetingLateEarly);
		}
	}

	public Integer getMeetingLateEarly() {
		String meetingLateEarly = getCustoms().get(MEETING_LATE_EARLY);
		if (StringUtils.isNotBlank(meetingLateEarly)) {
			return Integer.parseInt(meetingLateEarly);
		}
		return null;
	}

	public void setMeetingIsSigIn(Integer isSignIn) {
		if (isSignIn != null) {
			getCustoms().put(MEETING_IS_SIGN_IN, isSignIn.toString());
		} else {
			getCustoms().remove(isSignIn);
		}
	}

	public Integer getMeetingIsSigIn() {
		String isSignIn = getCustoms().get(MEETING_IS_SIGN_IN);
		if (StringUtils.isNotBlank(isSignIn)) {
			return Integer.parseInt(isSignIn);
		}
		return null;
	}

	public void setMeetingIsSigOut(Integer isSignOut) {
		if (isSignOut != null) {
			getCustoms().put(MEETING_IS_SIGN_OUT, isSignOut.toString());
		} else {
			getCustoms().remove(isSignOut);
		}
	}

	public Integer getMeetingIsSigOut() {
		String isSignOut = getCustoms().get(MEETING_IS_SIGN_OUT);
		if (StringUtils.isNotBlank(isSignOut)) {
			return Integer.parseInt(isSignOut);
		}
		return null;
	}

	public void setMeetingIsSigAnyTimes(Integer isSignAnyTimes) {
		if (isSignAnyTimes != null) {
			getCustoms().put(MEETING_IS_SIGN_ANY_TIMES, isSignAnyTimes.toString());
		} else {
			getCustoms().remove(isSignAnyTimes);
		}
	}

	public Integer getMeetingIsSigAnyTimes() {
		String isSignAnyTimes = getCustoms().get(MEETING_IS_SIGN_ANY_TIMES);
		if (StringUtils.isNotBlank(isSignAnyTimes)) {
			return Integer.parseInt(isSignAnyTimes);
		}
		return null;
	}

	public Map<String, String> getCustoms() {
		if (customs == null) {
			customs = new HashMap<String, String>();
		}
		return customs;
	}

	public void setCustoms(Map<String, String> customs) {
		this.customs = customs;
	}

	public String getPrefix() {
		return PREFIX;
	}
}
