package meerkat.modules.import_export.ftp;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import meerkat.modules.gui.IDialog;
import meerkat.modules.gui.IDialogBuilder;
import meerkat.modules.gui.IDialogBuilderFactory;
import meerkat.modules.gui.IFileValidator;
import meerkat.modules.gui.ILineEditValidator;
import meerkat.modules.import_export.IImportImplementation;

import java.io.File;
import java.nio.channels.Channels;
import java.nio.channels.InterruptibleChannel;
import java.nio.channels.WritableByteChannel;

/**
 * Wczytywanie plików z FTPa
 */
public class FtpImport implements IImportImplementation {

	private WritableByteChannel outputChannel = null;
	private String targetPath = null;

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
		idb.addFileChooser("Choose a file from disk: ", new IFileValidator() {
			@Override
			public boolean validate(String label, File value) {
				// TODO Auto-generated method stub
				return value.exists();
			}
		}).addLineEdit("Enter a path on server: ", new ILineEditValidator() {

			@Override
			public boolean validate(String label, String value) {
				// TODO Auto-generated method stub
				if(targetPath.length() > 0) {
					targetPath = value;
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

		if (targetPath == null || outputChannel == null)
			throw new NullPointerException();

		try {
			//TODO 
			return;
		} catch (Exception e) {
		} finally {

		}
	}
}