package com.m4rc310.ml.base;

import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.di.extensions.Preference;

import io.swagger.client.Configuration;
import io.swagger.client.api.DefaultApi;

@Singleton
@Creatable
@SuppressWarnings("restriction")
public class ML {
	@Inject
	@Preference(nodePath = "com.m4rc310.rcp.project.ml")
	private IEclipsePreferences pref;
	private String accessToken;
	private Date dateToRefreshToken;

	private Long clientId = 2190762367007185l;
	private String secretKey = "KHV6D45e8CFV3W8ME0b2g8bwW1Tch5Iq";
//	private String redirectUri = "https://m4rc310.000webhostapp.com/ml";
	private DefaultApi api;

	public final static String KEY_SERIAL = "key.serial";

	public final static String KEY_ML_CONNECTED = "key.ml.connected";
	public final static String KEY_REFRESH_TOKEN = "key.refresh.token";
	public final static String KEY_ACCESS_TOKEN = "key.access.token";
	public final static String KEY_REDIRECT_URL = "key.redirect.url";
	private final static String KEY_DATE_TO_REFRESH_ACCESS_TOKEN = "key.date.to.refresh.access.token";

	@Inject
	public ML() {
		this.api = new DefaultApi(Configuration.getDefaultApiClient(), clientId, secretKey);
	}

	public DefaultApi getApi() {
		return api;
	}

	public String getSerial() {
		return pref.get(KEY_SERIAL, createUniqueSerialNumber());
	}

	private String createUniqueSerialNumber() {
		String serialDefault = new SecureRandom().ints(0, 36).mapToObj(i -> Integer.toString(i, 36))
				.map(String::toUpperCase).distinct().limit(5).collect(Collectors.joining())
		.replaceAll("([A-Z0-9]{5})", "$1").substring(0, 5);

		return serialDefault;
	}

	public String getAccessToken() {

//		https://m4rc310.herokuapp.com/api/ml?code=12333

		boolean isRefresh = false;

		DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

		accessToken = pref.get(KEY_ACCESS_TOKEN, "");

		if (accessToken.isEmpty()) {
			isRefresh = true;
		}

		try {
			String sdate = pref.get(KEY_DATE_TO_REFRESH_ACCESS_TOKEN, "");
			dateToRefreshToken = df.parse(sdate);
			isRefresh = dateToRefreshToken.before(new Date());
		} catch (Exception e) {
			isRefresh = true;
		}

		if (isRefresh) {
			String refreshToken = pref.get(KEY_REFRESH_TOKEN, "-");
			try {

				Calendar calendar = new GregorianCalendar();

				this.accessToken = api.refreshAccessToken(refreshToken).getAccess_token();
				dateToRefreshToken = new Date();
				calendar.setTime(dateToRefreshToken);
				calendar.add(Calendar.HOUR, 6);
				dateToRefreshToken = calendar.getTime();

				pref.put(KEY_ACCESS_TOKEN, accessToken);
				pref.put(KEY_REFRESH_TOKEN, refreshToken);
				pref.put(KEY_DATE_TO_REFRESH_ACCESS_TOKEN, df.format(dateToRefreshToken));

				pref.flush();

				return accessToken;
			} catch (Exception e) {
				throw new UnsupportedOperationException(e);
			}
		} else {
			return accessToken;
		}
	}

	public boolean isConnected() {
		return pref.getBoolean(KEY_ML_CONNECTED, false);
	}
	
	public static void main(String[] args) {
		System.out.println(new ML().createUniqueSerialNumber());
	}
	

	public void logout(int userId, String token) {
		try {
			api = getApi();
			api.removeAppPermission(userId, token);
			
			pref.clear();
			pref.flush();
		} catch (Exception e) {
		}

	}

}
