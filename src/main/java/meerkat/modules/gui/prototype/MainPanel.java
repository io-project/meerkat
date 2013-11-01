package meerkat.modules.gui.prototype;

import org.eclipse.swt.widgets.Display;

/**
 * Prototyp najprostszego GUI, ktory mogloby byc wykorzystane jako plugin w naszej aplikacji. Napisany przy uzyciu SWT.
 * 
 * @author Jakub Ciesla
 */
public class MainPanel {

	protected Shell shlApplicationName;
	private Text txtEncryption;
	private Text txtDecryption;
	private Text text;
	private Text txtFile;
	private Text text_1;
	private Text text_2;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			MainPanel window = new MainPanel();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shlApplicationName.open();
		shlApplicationName.layout();
		while (!shlApplicationName.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlApplicationName = new Shell();
		shlApplicationName.setMinimumSize(new Point(800, 410));
		shlApplicationName.setSize(450, 300);
		shlApplicationName.setText("Meerkat");
		
		Combo combo_1 = new Combo(shlApplicationName, SWT.NONE);
		combo_1.setItems(new String[] {"ExportPlugin1", "ExportPlugin2", "ExportPlugin3", "ExportPlugin4"});
		combo_1.setBounds(46, 72, 270, 33);
		combo_1.setText("Export Settings");
		
		Combo combo_2 = new Combo(shlApplicationName, SWT.NONE);
		combo_2.setItems(new String[] {"Algorithm 1", "Algorithm 2", "Algorithm 3"});
		combo_2.setBounds(350, 72, 270, 33);
		combo_2.setText("Encryption Algorithm");
		
		txtEncryption = new Text(shlApplicationName, SWT.CENTER);
		txtEncryption.setFont(SWTResourceManager.getFont("Ubuntu", 24, SWT.BOLD));
		txtEncryption.setForeground(SWTResourceManager.getColor(SWT.COLOR_LINK_FOREGROUND));
		txtEncryption.setText("ENCRYPTION");
		txtEncryption.setEditable(false);
		txtEncryption.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtEncryption.setBounds(253, 10, 237, 48);
		
		Label lblSep = new Label(shlApplicationName, SWT.SEPARATOR | SWT.HORIZONTAL);
		lblSep.setEnabled(false);
		lblSep.setText("sep");
		lblSep.setBounds(0, 186, 800, 14);
		
		txtDecryption = new Text(shlApplicationName, SWT.CENTER);
		txtDecryption.setText("DECRYPTION");
		txtDecryption.setForeground(SWTResourceManager.getColor(SWT.COLOR_LINK_FOREGROUND));
		txtDecryption.setFont(SWTResourceManager.getFont("Ubuntu", 24, SWT.BOLD));
		txtDecryption.setEditable(false);
		txtDecryption.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtDecryption.setBounds(253, 206, 237, 48);
		
		Combo combo_4 = new Combo(shlApplicationName, SWT.NONE);
		combo_4.setItems(new String[] {"ExportPlugin1", "ExportPlugin2", "ExportPlugin3", "ExportPlugin4"});
		combo_4.setBounds(46, 272, 270, 33);
		combo_4.setText("Import Settings");
		
		Combo combo_5 = new Combo(shlApplicationName, SWT.NONE);
		combo_5.setItems(new String[] {"Algorithm 1", "Algorithm 2", "Algorithm 3"});
		combo_5.setBounds(350, 272, 270, 33);
		combo_5.setText("Encryption Algorithm");
		
		Button btnNewButton = new Button(shlApplicationName, SWT.NONE);
		btnNewButton.setBounds(645, 72, 115, 33);
		btnNewButton.setText("RUN!!!");
		
		Button button = new Button(shlApplicationName, SWT.NONE);
		button.setText("RUN!!!");
		button.setBounds(645, 272, 115, 33);
		
		text = new Text(shlApplicationName, SWT.BORDER);
		text.setBounds(161, 130, 459, 33);
		
		txtFile = new Text(shlApplicationName, SWT.CENTER);
		txtFile.setText("File:");
		txtFile.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtFile.setFont(SWTResourceManager.getFont("Ubuntu", 20, SWT.BOLD));
		txtFile.setEditable(false);
		txtFile.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtFile.setBounds(81, 132, 77, 31);
		
		text_1 = new Text(shlApplicationName, SWT.BORDER);
		text_1.setBounds(161, 328, 459, 33);
		
		text_2 = new Text(shlApplicationName, SWT.CENTER);
		text_2.setText("File:");
		text_2.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		text_2.setFont(SWTResourceManager.getFont("Ubuntu", 20, SWT.BOLD));
		text_2.setEditable(false);
		text_2.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		text_2.setBounds(81, 328, 77, 31);

	}
}

