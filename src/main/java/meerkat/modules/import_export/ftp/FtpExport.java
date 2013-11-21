package meerkat.modules.import_export.ftp;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.Channels;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.InterruptibleChannel;
import java.nio.channels.ReadableByteChannel;

import meerkat.modules.gui.IDialog;
import meerkat.modules.gui.IDialogBuilder;
import meerkat.modules.gui.IDialogBuilderFactory;
import meerkat.modules.gui.IFileValidator;
import meerkat.modules.gui.ILineEditValidator;
import meerkat.modules.import_export.IExportImplementation;

/**
 * Export do FTPa
 * 
 * @author
 */
public class FtpExport implements IExportImplementation {

	private Channel inputChannel = null;
	private String targetPath = null;

	@Override
	public <T extends ReadableByteChannel & InterruptibleChannel> void setInputChannel(
			T channel) {
		this.inputChannel = channel;
	}

	private IDialog buildUrlDialog(IDialogBuilderFactory dialogBuilderFactory) {
		// metoda tworzy okienko do wpisania adresu serwera
		IDialogBuilder idb = dialogBuilderFactory.newDialogBuilder();
		idb.addLineEdit("Enter a server address: ")
			.addLineEdit("Enter username: ")
			.addLineEdit("Enter password: ");
		return idb.build();
	}

	private IDialog buildDirectoryDialog(
			IDialogBuilderFactory dialogBuilderFactory) {
		// metoda tworzy okienko do wybrania pliku
		IDialogBuilder idb = dialogBuilderFactory.newDialogBuilder();
		idb.addLineEdit("Enter a path on server: ", new ILineEditValidator() {

			@Override
			public boolean validate(String label, String value) {
				// TODO Auto-generated method stub
				if(value.length() > 0) {
					targetPath = value;
					System.out.println(targetPath);
					return true;
				}
				return false;
			}
			
		});
		return idb.build();
	}

	@Override
	public boolean prepare(IDialogBuilderFactory<?> dialogBuilderFactory) {
		while (!FtpClient.connected) {
			IDialog idb = buildUrlDialog(dialogBuilderFactory);
			if(!idb.exec()) return false;
			else {
				String server = idb.getLineEditValue("Enter a server address: ");
				String username = idb.getLineEditValue("Enter username: ");
				String password = idb.getLineEditValue("Enter password: ");
				FtpClient.connect(server, username, password);
			}
		}

		IDialog idb = buildDirectoryDialog(dialogBuilderFactory);
		return idb.exec();
	}

	@Override
	public void run() throws Exception {

		if (targetPath == null || inputChannel == null)
			throw new NullPointerException();

		InputStream inputStream = null;

		try {
			inputStream = Channels
					.newInputStream((ReadableByteChannel) inputChannel);
			ByteBuffer buffer = ByteBuffer.allocate(32768);

			BufferedInputStream bis = new BufferedInputStream(inputStream);
			FtpClient.client.storeFile(targetPath, bis);
			bis.close();
			
		} catch (ClosedChannelException e) {
			throw e;
		} finally {
			inputStream.close();
		}
	}
}
