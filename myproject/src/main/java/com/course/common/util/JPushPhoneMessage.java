package com.course.common.util;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.ClientConfig;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;

public class JPushPhoneMessage {

	/**
	 * 善喻科技
	 */
	public static String AppKey = "748f54581bb5d83abe89b43a";
	public static String MasterSecret = "";// "93d101af668bcfad0807b060";

	public static String PushAll(String paramAlert) {
		if (MasterSecret.length() <= 0)
			return "";
		
		ClientConfig clientConfig = ClientConfig.getInstance();

		clientConfig.setApnsProduction(true);

		JPushClient jpushClient = new JPushClient(MasterSecret, AppKey, null, clientConfig);

		PushPayload payload = buildPushObject_all_all_alert(paramAlert);

		PushResult result = new PushResult();
		try {
			result = jpushClient.sendPush(payload);
			System.out.println(result);
		} catch (APIConnectionException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (APIRequestException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}

		return result.toString();
	}

	public static String PushByUserID(Integer userid, String paramAlert, String paramURL) {
		if (MasterSecret.length() <= 0)
			return "";

		ClientConfig clientConfig = ClientConfig.getInstance();

		clientConfig.setApnsProduction(true);

		JPushClient jpushClient = new JPushClient(MasterSecret, AppKey, null, clientConfig);

		PushPayload payload = buildPushObject_android_and_ios("shanyu" + userid, paramAlert, paramURL);

		PushResult result = new PushResult();
		try {
			result = jpushClient.sendPush(payload);
		} catch (APIConnectionException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (APIRequestException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return result.toString();
	}

	public static String PushByUserID(Integer userid, String paramAlert) {
		if (MasterSecret.length() <= 0)
			return "";

		ClientConfig clientConfig = ClientConfig.getInstance();

		clientConfig.setApnsProduction(true);

		JPushClient jpushClient = new JPushClient(MasterSecret, AppKey, null, clientConfig);

		PushPayload payload = buildPushObject_android_and_ios("shanyu" + userid, paramAlert);

		PushResult result = new PushResult();
		try {
			result = jpushClient.sendPush(payload);
			// System.out.println(result);
		} catch (APIConnectionException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (APIRequestException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}

		return result.toString();
	}

	public static String PushBadgeByUserID(Integer userid, String paramAlert, Integer badge) {
		if (MasterSecret.length() <= 0)
			return "";

		ClientConfig clientConfig = ClientConfig.getInstance();

		clientConfig.setApnsProduction(false);

		JPushClient jpushClient = new JPushClient(MasterSecret, AppKey, null, clientConfig);

		PushPayload payload = buildPushObject_android_and_ios("shanyu" + userid, paramAlert);

		PushResult result = new PushResult();
		try {
			result = jpushClient.sendPush(payload);
			System.out.println(result);
		} catch (APIConnectionException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (APIRequestException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}

		return result.toString();
	}

	public static PushPayload buildPushObject_all_all_alert(String paramAlert) {
		return PushPayload.alertAll(paramAlert);
	}

	public static PushPayload buildPushObject_all_all_alert(String paramAlert, String url) {
		return PushPayload.newBuilder().setPlatform(Platform.all())
				.setNotification(Notification.newBuilder().setAlert(paramAlert)
						.addPlatformNotification(
								AndroidNotification.newBuilder().setTitle(paramAlert).addExtra("url", url).build())
						.addPlatformNotification(IosNotification.newBuilder().setSound("happy").setBadge(0)
								.setAlert(paramAlert).addExtra("url", url).build())
						.build())
				.build();
	}

	public static PushPayload buildPushObject_all_alias_alert(String paramAlias, String paramAlert) {
		return PushPayload.newBuilder().setPlatform(Platform.all()).setAudience(Audience.alias(paramAlias))
				.setNotification(Notification.alert(paramAlert)).build();
	}

	public static PushPayload buildPushObject_android_and_ios(String paramAlias, String paramAlert) {
		return PushPayload.newBuilder().setPlatform(Platform.android_ios()).setAudience(Audience.alias(paramAlias))
				.setNotification(Notification.newBuilder().setAlert(paramAlert)
						.addPlatformNotification(AndroidNotification.newBuilder().setTitle(paramAlert).build())
						.addPlatformNotification(
								IosNotification.newBuilder().setSound("happy").setBadge(0).setAlert(paramAlert).build())
						.build())
				.build();
	}

	public static PushPayload buildPushObject_android_and_ios(String paramAlias, String paramAlert, String url) {
		return PushPayload.newBuilder().setPlatform(Platform.android_ios()).setAudience(Audience.alias(paramAlias))
				.setOptions(Options.newBuilder().setApnsProduction(false).build())
				.setNotification(Notification.newBuilder().setAlert(paramAlert)
						.addPlatformNotification(
								AndroidNotification.newBuilder().setTitle(paramAlert).addExtra("url", url).build())
						.addPlatformNotification(IosNotification.newBuilder().setSound("happy").setBadge(1)
								.setAlert(paramAlert).addExtra("url", url).build())
						.build())
				.build();
	}

	public static void main(String[] args) {
		JPushPhoneMessage.PushByUserID(1,
				"优化平台系统以及提高用户体验，平台将于今晚（1月11日）10点开始进行系统升级维护，系统升级维护将持续到晚12点为止，期间网站无法打开、操作，用户可在维护完成后进行操作，给您带来的不便，敬请谅解。");

	}
}
