package org.example.proyectoReservaa.run;

import org.openxava.util.*;

/**
 * Execute this class to start the application.
 *
 * With OpenXava Studio/Eclipse: Right mouse button > Run As > Java Application
 */

public class proyectoReservaa {

	public static void main(String[] args) throws Exception {
		//DBServer.start("proyectoReservaa-db"); // To use your own database comment this line and configure src/main/webapp/META-INF/context.xml
		AppServer.run("proyectoReservaa"); // Use AppServer.run("") to run in root context
	}

}
