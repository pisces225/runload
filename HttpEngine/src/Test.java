import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Test {
	protected static Logger logger = Logger.getLogger("task");
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Properties prop = new Properties();  
		JDBCLoad load = new JDBCLoad();
		load.setLogger(logger);
		try {
			FileHandler handler = new FileHandler("./HttpEngine/test.log");
			handler.setLevel(Level.INFO);
			logger.addHandler(handler);
		} catch (SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    FileInputStream fis = null;
		try {
			fis = new FileInputStream("./HttpEngine/sample.properties");
			prop.load(fis);
			load.setTask_prop(prop);		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		load.startLoad();
	}

}
