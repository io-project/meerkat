package meerkat.modules.import_export.dropbox;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import meerkat.modules.gui.IDialog;
import meerkat.modules.gui.IDialogBuilder;
import meerkat.modules.gui.IDialogBuilderFactory;
import meerkat.modules.gui.ILineEditValidator;
import meerkat.modules.import_export.IImportImplementation;

import java.nio.channels.Channels;
import java.nio.channels.InterruptibleChannel;
import java.nio.channels.WritableByteChannel;

/**
 * Wczytywanie plików z Dropboxa
 */
public class DropboxImport implements IImportImplementation {

	class CodeValidator implements ILineEditValidator {
		@Override
		public boolean validate(String label, String value) {
			// TODO Auto-generated method stub
			if ((client = DropboxClient.authorize(value)) == null)
				return false;
			return true;
		}

	}

	private DbxClient client = null;

	private WritableByteChannel outputChannel = null;
	private String filePath = null;

	@Override
	public <T extends WritableByteChannel & InterruptibleChannel> void setOutputChannel(
			T channel) {
		this.outputChannel = channel;
	}

	private IDialog buildUrlDialog(IDialogBuilderFactory dialogBuilderFactory,
			String url) {
		// metoda tworzy okienko do wpisania kodu
		// w celu zalogowania się na Dropboxa
		IDialogBuilder idb = dialogBuilderFactory.newDialogBuilder();
		idb.addHyperLink("Obtain an authentication code at dropbox website",
				url).addLineEdit("Enter a code: ", new CodeValidator());
		return idb.build();
	}

	private IDialog buildDirectoryDialog(
			IDialogBuilderFactory dialogBuilderFactory) {
		// metoda tworzy okienko do wybrania pliku
		IDialogBuilder idb = dialogBuilderFactory.newDialogBuilder();
		idb.addLineEdit("Choose a directory: ", new ILineEditValidator() {

			@Override
			public boolean validate(String label, String value) {
				return value != null && value.length() > 0;
			}
		});
		// idb.addDirectoryChooser("Choose a directory: ");
		return idb.build();
	}

	@Override
	public boolean prepare(IDialogBuilderFactory<?> dialogBuilderFactory) {
		String response = DropboxClient.connect();
		if (response != DropboxClient.CONNECTED) {
			IDialog idb = buildUrlDialog(dialogBuilderFactory, response);
			if (idb.exec()) {
				response = DropboxClient.connect();
			}
		}

		IDialog idb = buildDirectoryDialog(dialogBuilderFactory);
		if (idb.exec()) {
                        filePath = idb.getLineEditValue("Choose a directory: ");
                        if (!filePath.startsWith("/"))
                                filePath = "/" + filePath;
                        return true;
		}
		return false;
	}

	@Override
	public void run() throws Exception {

		if (filePath == null || outputChannel == null)
			throw new NullPointerException();

		try {
			DbxEntry.File downloadedFile = client.getFile(filePath, null,
					Channels.newOutputStream(outputChannel));
		} catch (Exception e) {
			throw new Exception(e);
		} finally {

		}
	}
}