package multiuser;

import debug.Logger;
import javafx.collections.*;

public class User
{
	/*
	 * @author Joel H�berli
	 */

	// Alle Angaben die ein User hat
	private String Name;
	private String Hash;
	private byte[] Salz;
	private String Email;
	// den Usertype braucht es erst, wenn man auch Lehrer erstellen kann.
	private Integer Usertype;
	// Das system mit klassen wird ev. nicht implementiert aus Zeitgr�nden
	private ObservableList<String> myGroups = FXCollections.observableArrayList();

	/** 
	 * Wird beim registrieren aufgerufen -> Wenn etwas falsch ist oder etwas
	 * nicht funktionierte wird false returnt
	 * 
	 * schritte beim registrieren:
	 * 1.) Die Eingaben mit einem Regex �berpr�fen
	 * 2.) Salz generieren
	 * 3.) Passwort hashen mit Salz
	 * 4.) Salz, Passworthash, Username, Email in DB speichern
	 * 
	 * @param name -> Username den der User angibt 
	 * @param mail -> Mail den der User angibt
	 * @param passwort -> Das gew�nschte PW des Users
	 * @param usertype -> Ob es sich um einen normalen User handelt oder nicht, wird momentan nicht gebraucht
	 * 
	 * @return  1 Aktion erfolgreich<br>
	 *         -1 kein int<br>
	 *         -2 Eingaben entsprechen nicht dem Regex <br>
	 *         -3 Der Hash konnte nicht erzeugt werden
	 */
	public int registration(String name, String mail, String passwort, String usertype)
	{
		try
		{
			int ut = Integer.parseInt(usertype); // TODO : Fehler behandlung
		} catch (Exception e)
		{
			return -1;
		}
		if (checkNameMachbarkeit(name) && checkEmailMachbarkeit(mail) && checkPasswortMachbarkeit(passwort))
		{
			try
			{
				setSalt();
				Hash = Security.createHash(passwort, Salz);
				return 1;
			} catch (Exception e)
			{
				Logger.log(e.getMessage());
				return -3;
			}
		} else
		{
			return -2;
		}
	}
	
	/**
	 * 	 Wird beim Login aufgerufen
	 *   Schritte beim Login:
	 *   1.) Salz und Passworthash aus DB laden wo name gleich name welcher
	 *       �bergeben wurde
	 *   2.) Salz und �bergebenes Passwort hashen
	 *   3.) Diesen generierten hash mit Passworthash vergleichen
	 *   4.) Wenn �bereinstimmt -> Namen des Users in lokale DB speichern und dann
	 *       das Profil anzeigen
	 * 
	 * @param name -> Username 
	 * @param passwort -> Passwort
	 * @return -> Wenn true Login korrekt / Wenn false Login nicht korrekt
	 * @throws CannotPerformOperationException -> ist eine Exception, welche aufgrund der Klasse Security existiert.
	 */
	public boolean login(String name, String passwort) throws CannotPerformOperationException
	{
		Salz = getSalt();
		Hash = getHash();

		if (Security.createHash(passwort, Salz) == Hash)
		{
			// TODO : Save to local db
			return true;
		} else
		{
			return false;
		}
	}

	/**
	 * wird beim L�schen von einem Account gebraucht.
	 * L�scht den User aus der Db mit all seinen Eintr�gen
	 * 
	 * @param name -> des Eingeloggten User
	 * @param mail -> des Eingeloggten User
	 * @return Wenn true konnte der User erfolgreich gel�scht werden / Wenn false nicht.
	 */
	public boolean delete(String name, String mail)
	{
		return false;
	}

	// TODO : Wenn datenbank funktioniert darin speichern
	private void setSalt()
	{
		Salz = Security.generateSalt();
	}

	// TODO : Wenn Datenbank funktioniiert Salt daraus auslesen
	@SuppressWarnings("unused")
	private byte[] getSalt()
	{
		return Salz = null;
	}

	// Validierung der drei Eingaben mit entsprechendem Regex
	private Boolean checkNameMachbarkeit(String name)
	{
		String regexName = "[A-Za-z\\d](4,100)";
		if (name.matches(regexName))
		{
			return true;
		} else
		{
			return false;
		}
	}

	private Boolean checkEmailMachbarkeit(String mail)
	{
		String regexMail = "[A-Za-z\\d]{@}[A-Za-z\\d]{\\.}[A-Za-z0-9]";
		if (mail.matches(regexMail))
		{
			return true;
		} else
		{
			return false;
		}
	}

	private Boolean checkPasswortMachbarkeit(String passwort)
	{
		String regexPw = "[A-Za-z\\d\\!\\?\\$\\*\\%\\&\\�\\@\\'](8,100)";
		if (passwort.matches(regexPw))
		{
			return true;
		} else
		{
			return false;
		}
	}

	// Getter und Setter der Angaben
	public String getName()
	{
		return Name;
	}

	// Wenn true -> gespeichert / Wenn false -> nicht gespeicher weil Fehler
	// (User eine Meldung geben)
	// TODO : Wenn datenbank funktioniert darin speichern
	public Boolean setName(String name)
	{
		if (checkNameMachbarkeit(name))
		{
			Name = name;
			return true;
		} else
		{
			return false;
		}
	}

	// Wenn true -> gespeichert / Wenn false -> nicht gespeicher weil Fehler
	// (User eine Meldung geben)
	// TODO : Wenn datenbank funktioniert darin speichern
	public Boolean setPasswort(String passwort)
	{
		// Wenn Passwort zul�ssig und das Salz gef�llt
		if (checkPasswortMachbarkeit(passwort) && !(Salz == null))
		{
			try
			{
				if (Security.createHash(passwort, Salz) == null)
				{
					return false;
				} else
				{
					Hash = passwort;
					return true;
				}
			} catch (CannotPerformOperationException e)
			{
				e.printStackTrace();
				return false;
			}
		} else
		{
			return false;
		}
	}

	// TODO : Wenn Datenbank funktioniert, den Passwort daraus holen
	private String getHash()
	{
		return Hash = null;
	}

	public String getEmail()
	{
		return Email;
	}

	// Wenn true -> gespeichert / Wenn false -> nicht gespeicher weil Fehler
	// (User eine Meldung geben)
	// TODO : Wenn datenbank funktioniert darin speichern
	public Boolean setEmail(String email)
	{
		if (checkEmailMachbarkeit(email))
		{
			Email = email;
			return true;
		} else
		{
			return false;
		}
	}

	public Integer getUsertype()
	{
		return Usertype;
	}

	public void setUsertype(Integer usertype)
	{
		Usertype = usertype;
	}

	public ObservableList<String> getMyGroups()
	{
		return myGroups;
	}

	public void setMyGroups(ObservableList<String> myGroups)
	{
	}
}
