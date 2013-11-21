package meerkat.modules.import_export.ftp;

import java.io.IOException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxException;

public class FtpClient {

	private static String server, username, password;
	public static FTPClient client = new FTPClient();
	public static boolean connected = false;

	private static boolean authorized = false;

	public static boolean connect(String s, String u, String p) {
		if (connected)
			return true;
		try {
			server = s;
			username = u;
			password = p;
			client.connect(server);

			int reply = client.getReplyCode();

			if (!FTPReply.isPositiveCompletion(reply)) {
				if(client.isConnected()) client.disconnect();
				System.err.println("FTP server refused connection.");
				return false;
			}
			
			if(client.login(username, password)) {
				client.setFileType(FTP.BINARY_FILE_TYPE, FTP.BINARY_FILE_TYPE);
				client.setFileTransferMode(FTP.BINARY_FILE_TYPE);
				client.enterLocalPassiveMode();
				return connected = true;
			}
			return false;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}

}
