package UI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

/**
 * Welcome Window
 */

public class WelcomeWindow {

	// private static Logger logger =
	// LoggerFactory.getLogger(WelcomeWindow.class);

	private Shell shell;

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		configureShell();
		shell.open();
		// shell.layout();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Configure shell
	 * 
	 * @param shell
	 */
	protected void configureShell() {
		shell.pack();

		Rectangle rctDisplay = shell.getDisplay().getBounds();
		Rectangle rctShell = shell.getBounds();
		int x = (rctDisplay.width - rctShell.width) / 2;
		int y = (rctDisplay.height - rctShell.height) / 2;
		shell.setLocation(x, y);
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell(SWT.ON_TOP);
		shell.setLayout(new FillLayout());
		// Composite as container
		Composite container = new Composite(shell, SWT.NONE);
		Image pic = new Image(shell.getDisplay(), "1.jpg");
		container.setBackgroundImage(pic);
		container.setBackgroundMode(SWT.INHERIT_DEFAULT);
		FormLayout layout = new FormLayout();
		container.setLayout(layout);

		// ProgressBar
		final ProgressBar bar = new ProgressBar(container, SWT.HORIZONTAL);
		bar.setMinimum(0);
		bar.setMaximum(100);
		final int min = bar.getMinimum();
		final int max = bar.getMaximum();

		FormData formData = null;
		formData = new FormData();
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100, 0);
		formData.bottom = new FormAttachment(100, 0);
		bar.setLayoutData(formData);

		// Label Message
		final Label lblMessage = new Label(container, SWT.INHERIT_DEFAULT);
		Color white = new Color(shell.getDisplay(), 255, 255, 255);
		lblMessage.setForeground(white);
		formData = new FormData();
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100);
		formData.bottom = new FormAttachment(bar, 0);
		lblMessage.setLayoutData(formData);

		// Label Image
		Label lblImage = new Label(container, SWT.NONE);
		// lblImage.setImage(Registry.getImage("logo.bmp"));
		lblImage.setImage(pic);
		formData = new FormData();
		formData.left = new FormAttachment(0, 0);
		formData.top = new FormAttachment(0, 0);
		lblImage.setLayoutData(formData);

		final int step = 10;
		new Thread(new Runnable() {
			public void run() {
				shell.getDisplay().asyncExec(new Runnable() {
					public void run() {
						for (int i = min; i < max; i += step) {
							if (bar.isDisposed()) {
								return;
							}
							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							String text = "Loading...";
							// text = MessageFormat.format(text,
							// bar.getSelection(), StringUtils.repeat('.', i /
							// step));
							lblMessage.setText(text);
							bar.setSelection(bar.getSelection() + i);
						}
						lblMessage.setText("Load ontology...");
						Main.win.myDrawPane.event_load();
						Main.win.show.setEnabled(true);
						lblMessage.setText("Check ontology...");
						Main.win.myDrawPane.event_check();
						shell.dispose();
					}
				});
			}
		}).start();
	}

	public static void main(String[] args) {
		new WelcomeWindow().open();
	}

}