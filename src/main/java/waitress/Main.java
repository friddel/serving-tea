package waitress;

import jade.Boot;

public class Main {

	public static void main(String[] args) {
		//String[] parameters = new String[2];
		//parameters[0] = "-gui";
		//parameters[1] = "waitress:waitress.Waitress;";
		
		String[] parameters = new String[] { "-gui",
				 "-host", "localhost",
				"waitress:waitress.Waitress;" };
		Boot.main(parameters);
		

	}
}
