package urban_robot_controller.logging;

import java.io.File; 

import javax.swing.JFileChooser; 
import javax.swing.filechooser.FileNameExtensionFilter;

// TODO: Auto-generated Javadoc
/**
 * The Class openDialog.
 */
public class OpenDialog { 

	/** The directory. */
	private final String directory;
	
    /**
     * Instantiates a new open dialog.
     *
     * @param directory the directory
     */
    public OpenDialog(String directory) {
		this.directory = directory;
	}
    
	/**
	 * Open.
	 *
	 * @return the file
	 */
	public File open() { 
        final JFileChooser chooser = new JFileChooser("chose directory"); 
        FileNameExtensionFilter filter = new FileNameExtensionFilter(".txt", "txt", "text");
        chooser.setFileFilter(filter);
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setDialogType(JFileChooser.OPEN_DIALOG); 
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES); 
        final File file = new File(directory); 

        chooser.setCurrentDirectory(file); 

        chooser.setVisible(true); 
        final int result = chooser.showOpenDialog(null); 

        if (result == JFileChooser.APPROVE_OPTION) { 
            File inputVerzFile = chooser.getSelectedFile(); 
            
            return inputVerzFile;
        } 
        
        //if no file selected exit the program
        System.out.println("Exiting...\nNo file chosen"); 
        chooser.setVisible(false);
        try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        System.exit(0);
		
		return null;       
    } 
} 