package meerkat.modules.import_export.ftp;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import meerkat.modules.gui.IDialog;
import meerkat.modules.gui.IDialogBuilder;
import meerkat.modules.gui.IDialogBuilderFactory;
import meerkat.modules.gui.IDirectoryValidator;
import meerkat.modules.gui.IFileValidator;
import meerkat.modules.gui.ILineEditValidator;
import meerkat.modules.import_export.IImportImplementation;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.InterruptibleChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

/**
 * Wczytywanie plików z FTPa
 */
public class FtpImport implements IImportImplementation {

	private WritableByteChannel outputChannel = null;
	private String filePath = null;

	@Override
	public <T extends WritableByteChannel & InterruptibleChannel> void setOutputChannel(T channel) {
		this.outputChannel = channel;
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
		idb.addLineEdit("Path to file on server: ", new ILineEditValidator() {

			@Override
			public boolean validate(String label, String value) {
				// TODO Auto-generated method stub
				if(value.length() > 0) {
					filePath = value;
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

		if (outputChannel == null)
			throw new NullPointerException();

		OutputStream outputStream = null;

		try {
			outputStream = Channels
					.newOutputStream((WritableByteChannel) outputChannel);
			ByteBuffer buffer = ByteBuffer.allocate(32768);

			BufferedOutputStream bos = new BufferedOutputStream(outputStream);
			FtpClient.client.retrieveFile(filePath, bos);
			bos.close();
			
		} catch (ClosedChannelException e) {
			throw e;
		} finally {
			outputStream.close();
		}
	}
}