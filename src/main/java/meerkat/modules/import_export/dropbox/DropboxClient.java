package meerkat.modules.import_export.dropbox;
import java.util.Locale;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuthNoRedirect;

public class DropboxClient {

	private static final String APP_KEY = "bx2m0g5gri658iw";
	private static final String APP_SECRET = "rgtfgh5237gg06u";
	public  static final String CONNECTED = "CONNECTED";
	private static DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);
	private static DbxRequestConfig config = new DbxRequestConfig(
			"Meerkat/1.0", Locale.getDefault().toString());
	private static DbxWebAuthNoRedirect webAuth = new DbxWebAuthNoRedirect(
			config, appInfo);

	private static DbxAuthFinish authFinish = null;
	static DbxClient client = null;

	private static boolean authorized = false;

	public static String connect() {
		if (!authorized) {
			return webAuth.start();
		}
		return CONNECTED;
	}

	public static DbxClient authorize(String code) {
		try {
			authFinish = webAuth.finish(code);
			authorized = true;
			return client = new DbxClient(config, authFinish.accessToken);
		} catch (DbxException e) {
			return null;
		}
	}
}
