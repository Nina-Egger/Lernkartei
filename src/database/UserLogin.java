package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import debug.Logger;


public class UserLogin extends MYSQLConnector {

	protected static String myTableName  = "Users";
	protected static String mySeekAttribute = "Username";
	protected static String myPrimaryKey = "PK_Usr";
//	private   static String myAttributeList = mySeekAttribute + ", Email, Password, Rule";
//	private   static String myAttributes = 
//									myPrimaryKey + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
//									mySeekAttribute +" TEXT NOT NULL," +
//									" Email 	TEXT NOT NULL," +
//									" Password 	TEXT NOT NULL," +
//									" Rule 		TEXT NOT NULL";
	/**
	 * F�gt der Datenbank einen neuen User hinzu
	 * 
	 * @param values
	 *            --> String Array mit 3 Werten. Username, E-Mail, Passwort
	 * @param teacher
	 *            --> Boolean: true = Lehrer, false = Sch�ler
	 */
	public static void newUser (String[] values, boolean teacher) {

		Connection c = null;
		Statement stmt = null;

		try {
			Class.forName(driver);
			c = DriverManager.getConnection(mysqlURL, username, password);
			stmt = c.createStatement();

			String studValues = "";

			for (int i = 0; i < values.length; i++) {

				if (i < values.length - 1) {

					studValues += "'" + values[i] + "',";

				}
				else {

					studValues += "'" + values[i] + "'";

				}

			}

			String teachValues = "";

			for (int i = 0; i < values.length; i++) {

				if (i < values.length - 1) {

					teachValues += "'" + values[i] + "',";

				}
				else {

					teachValues += "'" + values[i] + "'";

				}

			}

			String usedb = "USE " + "userdb";
			stmt.executeQuery(usedb);

			debug.Debugger.out("Opened database successfully");

			String sql1 = "CREATE TABLE IF NOT EXISTS Teachers " +
					"(PK_Teachers INT PRIMARY KEY AUTO_INCREMENT," +
					" Username TEXT NOT NULL," +
					" Email TEXT NOT NULL," +
					" Password TEXT NOT NULL" + ")";

			debug.Debugger.out(sql1);
			stmt.executeUpdate(sql1);
			debug.Debugger.out("Lehrertabelle erstellt!");

			String sql2 = "CREATE TABLE IF NOT EXISTS Students " +
					"(PK_Students INT PRIMARY KEY AUTO_INCREMENT," +
					" Username TEXT NOT NULL," +
					" Email TEXT NOT NULL," +
					" Password TEXT NOT NULL" + ")";

			debug.Debugger.out(sql2);
			stmt.executeUpdate(sql2);
			debug.Debugger.out("Sch�lertabelle erstellt!");

			if (teacher) {

				String newTeach = "INSERT INTO Teachers (Username, Email, Password) VALUES (" + teachValues + ")";
				stmt.executeUpdate(newTeach);
				debug.Debugger.out(newTeach);
				debug.Debugger.out("Teacher sucessfully added!");

			}
			else if (!teacher) {

				String newStud = "INSERT INTO Students (Username, Email, Password) VALUES (" + studValues + ")";
				debug.Debugger.out(newStud);
				stmt.executeUpdate(newStud);
				debug.Debugger.out("Student sucessfully added!");

			}

			stmt.close();
			c.close();
		}
		catch (Exception e) {
			Logger.log("Database.newUser(): " + e.getMessage());
		}

	}

	/**
	 * �berpr�ft ob ein Wert bereits vorhanden ist
	 * 
	 * @param values
	 *            --> String Array mit 2 Werten, Username und Email
	 * @return --> Retourniert true, wenn m�glich, sonst false
	 */

	public static boolean checkPossible (String[] values) {

		boolean possible = true;
		boolean noTeacher = true;
		boolean noStudent = true;

		// �berpr�fen ob ein Lehrer mit dem Username oder Email vorhanden ist

		Connection c = null;
		Statement stmt = null;

		try {
			Class.forName(driver);
			c = DriverManager.getConnection(mysqlURL, username, password);
			stmt = c.createStatement();

			String sql1 = "CREATE TABLE IF NOT EXISTS Teachers " +
					"(PK_Lehrer INT PRIMARY KEY AUTO_INCREMENT," +
					" Username TEXT NOT NULL," +
					" Email TEXT NOT NULL," +
					" Password TEXT NOT NULL" + ")";

			debug.Debugger.out(sql1);
			stmt.executeUpdate(sql1);
			debug.Debugger.out("Lehrertabelle erstellt!");

			String sql2 = "CREATE TABLE IF NOT EXISTS Students " +
					"(PK_Lehrer INT PRIMARY KEY AUTO_INCREMENT," +
					" Username TEXT NOT NULL," +
					" Email TEXT NOT NULL," +
					" Password TEXT NOT NULL" + ")";

			debug.Debugger.out(sql2);
			stmt.executeUpdate(sql2);
			debug.Debugger.out("Sch�lertabelle erstellt!");

			c.setAutoCommit(false);
			ResultSet rsUsern = stmt.executeQuery("SELECT Username FROM Teachers WHERE Username = " + "'" + values[0] + "'");
			ResultSet rsEmail = stmt.executeQuery("SELECT Email FROM Teachers WHERE Email = " + "'" + values[1] + "'");
			c.setAutoCommit(true);
			
			if (rsUsern.next() || rsEmail.next()) {

				noTeacher = false;

			}

			rsUsern.close();
			rsEmail.close();
			stmt.close();
			c.close();
		}
		catch (Exception e) {
			Logger.log("UserLogin.checkPossible(): " + e.getMessage());
		}

		// �berpr�fen ob ein Sch�ler mit dem Username oder Email vorhanden ist

		try {
			Class.forName(driver);
			c = DriverManager.getConnection(mysqlURL, username, password);
			stmt = c.createStatement();

			c.setAutoCommit(false);
			ResultSet rsUsern = stmt
					.executeQuery("SELECT Username FROM Students WHERE Username = " + "'" + values[0] + "'");
			ResultSet rsEmail = stmt.executeQuery("SELECT Email FROM Students WHERE Email = " + "'" + values[1] + "'");
			c.setAutoCommit(true);			
			
			if (rsUsern.next() || rsEmail.next()) {

				noStudent = false;

			}

			rsUsern.close();
			rsEmail.close();
			stmt.close();
			c.close();
		}
		catch (Exception e) {
			Logger.log("UserLogin.checkPossible(): " + e.getMessage());
		}

		// Wenn keine Lehrer und keine Sch�ler mit passendem Username oder Email
		// vorhanden ist, ist es m�glich

		if (noTeacher && noStudent) {

			possible = true;

		}
		else {

			possible = false;

		}

		return possible;

	}
	
	/**
	 * Wird gebraucht bei der �nderung des Usernamens
	 * 
	 * @param newName -> der gew�nschte neue Name
	 * @return true -> Konnte �ndern | false -> Konnte nicht �ndern
	 */
	
	public static boolean changeUsername(String newName, String oldName) {
		
		Connection c = null;
		Statement stmt = null;
		
		try
		{
			Class.forName(driver);
			c = DriverManager.getConnection(mysqlURL, username, password);
			stmt = c.createStatement();
			
			String checkT = "SELECT * FROM Teachers WHERE Username = " + newName + ";";
			String checkS = "SELECT * FROM Students WHERE Username = " + newName + ";";
			String insertT ="INSERT INTO Teachers (Username) VALUES (" + newName + ") WHERE Username = " + oldName + ";";
			String insertS ="INSERT INTO Students (Username) VALUES (" + newName + ") WHERE Username = " + oldName + ";";
			
			c.setAutoCommit(false);
			ResultSet rsT = stmt.executeQuery(checkT);
			ResultSet rsS = stmt.executeQuery(checkS);
			c.setAutoCommit(true);
			
			if (rsT == null && rsS == null)
			{
				try
				{
					stmt.executeUpdate(insertT);
				} catch (Exception e)
				{
					stmt.executeUpdate(insertS);
				}
				return true;
			} else {
				return false;
			}
		
		} catch (Exception e)
		{
			Logger.log("UserLogin.changeUsername(): " + e.getMessage());
			return false;
		}
	}

	/**
	 * L�scht den angegebenen User
	 * 
	 * @param delName
	 *            --> String mit Username
	 * 
	 */

	public static void delUser (String delName) {

		Connection c = null;
		Statement stmt = null;

		try {
			Class.forName(driver);
			c = DriverManager.getConnection(mysqlURL, username, password);
			stmt = c.createStatement();

			String delStudent = "DELETE FROM Students WHERE Username = " + delName;
			String delTeacher = "DELETE FROM Teachers WHERE Username = " + delName;

			stmt.executeUpdate(delStudent);
			stmt.executeUpdate(delTeacher);

			debug.Debugger.out("Successfully deleted User: " + delName);

			stmt.close();
			c.close();
		}
		catch (Exception e) {
			Logger.log("UserLogin.delUser(): " + e.getMessage());
		}

	}

	/**
	 * �berpr�ft die Login Daten
	 * 
	 * @param userData
	 *            --> Ben�tigt ein String Array mit Username, Password
	 * @return --> True = Korrekt, False = Inkorrekt
	 */

	public static boolean loginUser (String[] userData) {

		boolean loggedIn = false;
		String password = null;

		Connection c = null;
		Statement stmt = null;

		try {
			Class.forName(driver);
			c = DriverManager.getConnection(mysqlURL, username, password);
			stmt = c.createStatement();
			
			c.setAutoCommit(false);
			ResultSet rs = stmt.executeQuery("SELECT Password FROM Students WHERE Username = '" + userData[0] + "'");
			c.setAutoCommit(true);
			
			while (rs.next()) {

				password = rs.getString("Password");

			}

			stmt.close();
			c.close();
		}
		catch (Exception e) {
			Logger.log("UserLogin.loginUser(): " + e.getMessage());
		}

		if (userData[1].equals(password)) {

			loggedIn = true;

		}

		return loggedIn;
	}	
	
}

