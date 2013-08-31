import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.simpleframework.http.Query;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.core.Container;


public class BoggleServer implements Container {

	// List for the log display
	private List<String> log = new ArrayList<String>();

	// String to display the current status
	private String status = "Welcome to Boggle";
	
	// The Boggle game
	Boggle boggle;
	
	public BoggleServer(Boggle b){
		boggle = b;
	}

	@Override
	public void handle(Request request, Response response) {
		try{
			PrintStream body = response.getPrintStream();
			long time = System.currentTimeMillis();

			// Set headers
			response.setValue("Content-Type", "text/html");
			response.setValue("Server", "BoggleServer/1.0 (Simple 4.0)");
			response.setDate("Date", time);
			response.setDate("Last-Modified", time);

			// Get the query action
			Query query = request.getQuery();
			String action = query.get("action");
			
			// If null then return the full page
			if(action == null){
				File page = new File("webpage.html");
				BufferedReader reader = new BufferedReader(new FileReader(page));
				while(reader.ready()){
					String ln = reader.readLine().replaceAll("@status", status);
					body.println(ln);
				}
				reader.close();
			}else{
				
				if(action.equals("start")){
					boggle.start();
				}
				if(action.equals("showBoard")){
					boggle.showBoard();
				}
				body.print(status);
			}
			
			body.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void updateStatus(String s){
		status = s;
	}

	public void log(String s){
		log.add(s);
	}
}
