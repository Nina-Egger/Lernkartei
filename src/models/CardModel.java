package models;

import java.util.ArrayList;

import database.CardEntity;
import debug.Supervisor;
import mvc.Model;


public class CardModel extends Model
{

	/**
	 * @deprecated
	 */
	@Override
	public int doAction (String functionName, String paramS, double paramD)
	{
		Supervisor.errorAndDebug(this, "Derprecated method (CardModel). Please uses the new doAction");
		return -9;
	}

	@Override
	public int doAction (Command command, String... param)
	{
		String defaultPriority = "1";
		String defaultColor = "-16777216";

		switch (command)
		{
			case NEW:
				if (param.length != 3) { return -2; }
				for (String s : param)
				{
					s = s.replace("'", "''");
				}
				String[] arguments = new String[] { param[0], param[1], param[2], defaultPriority, defaultColor };
				boolean successfulNew = CardEntity.pushToStock(arguments);
				if (successfulNew)
				{
					refreshViews();
					return 1;
				}
				return -1;

			case UPDATE:
				if (param.length != 3) { return -2; }
				for (String s : param)
				{
					s = s.replace("'", "''");
				}
				boolean successfulUpdate = CardEntity.editEntry(param[0], param[1], param[2]);
				return successfulUpdate ? 1 : -1;

			case DELETE:
				if (param.length != 1) { return -2; }
				boolean successfulDelete = CardEntity.delEntry(param[0]);
				if (successfulDelete)
				{
					refreshViews();
					return 1;
				}
				return -1;

			default:
				int successfulSuper = super.doAction(command, param);
				return successfulSuper;
		}
	}

	@Override
	public ArrayList<String> getDataList (String query)
	{

		ArrayList<String> result = new ArrayList<String>();
		ArrayList<String[]> cards = CardEntity.pullFromStock(query);
		if (cards == null)
		{
			debug.Debugger.out("getData cards = null");
			return result;

		}
		else
		{

			for (String[] s : cards)
			{
				debug.Debugger.out(s[0]);
				String data = s[0] + globals.Globals.SEPARATOR + s[1] + globals.Globals.SEPARATOR + s[2];

				result.add(data);

			}

			return result;

		}

	}
}
