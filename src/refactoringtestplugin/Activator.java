package refactoringtestplugin;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import saferefactor.core.analysis.safira.Constants;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "RefactoringTestPlugin";

	// The shared instance
	private static Activator plugin;
	 
	/**
	 * The constructor
	 */
	public Activator() {
		plugin = this;
		saferefactor.core.Parameters.workspacePath = getPluginFolder() + Constants.FILE_SEPARATOR;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		System.out.println("Funcionou!!");
		super.start(context);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	public File getFileInPlugin(IPath path) throws CoreException {
		try {
            URL installURL= new URL(getBundle().getEntry("/"), path.toString());
            URL localURL= FileLocator.toFileURL(installURL);
            return new File(localURL.getFile());
        } catch (IOException e) {
            throw new CoreException(new Status(IStatus.ERROR, PLUGIN_ID, IStatus.ERROR, e.getMessage(), e));
        } 
	}
	
	/**
	 * @return The File Location of this plugin
	 */
	 public String getPluginFolder()  {
		 URL url = getBundle().getEntry("C:\\TIM\\RefactoringTestPluginASM\\");  // TODO - Tive que ajustar esse ponto
	     try {
			url = Platform.asLocalURL(url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	     return url.getPath();
	 }

}
