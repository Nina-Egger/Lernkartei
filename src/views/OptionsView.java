package views;

import globals.Globals;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import mvc.ModelInterface.Command;
import mvc.fx.FXController;
import mvc.fx.FXView;
import views.components.Alert;
import views.components.BackButton;


/**
 * Optionen
 * 
 * @author miro
 *
 */
public class OptionsView extends FXView
{
	public OptionsView (String newName, FXController newController)
	{
		super(newController);
		construct(newName);
	}

	boolean	resetChange	= false;
	String	lastValidCardLimit;
	TextField cardLearnLimit;
	
	@Override
	public Parent constructContainer ()
	{
		Label cardLimitDescription = new Label("Anzahl Karten, die auf einmal gelernt werden, limitieren.");
		cardLearnLimit = new TextField(getFXController().getModel("config").getDataList("cardLimit").get(0)); // Achtung

		lastValidCardLimit = cardLearnLimit.getText();

		cardLimitDescription.setWrapText(true);
		cardLearnLimit.focusedProperty().addListener(e ->
		{
			if (!resetChange)
			{
				try
				{
					int cardLimit = Integer.parseInt(cardLearnLimit.getText());
					cardLimit = cardLimit < Globals.minStackPartSize ? Globals.minStackPartSize : cardLimit;
					String cardLimitParam = "" + cardLimit; // TODO bessere l�sung
					getFXController().getModel("config").doAction(Command.SET, "cardLimit", cardLimitParam);
					resetChange = true;
					cardLearnLimit.setText(cardLimitParam);
					lastValidCardLimit = cardLearnLimit.getText();
					resetChange = false;
				}
				catch (Exception ex)
				{
					Alert.complexChoiceBox("Achtung", "Es muss eine g�ltige Ganzzahl eingegeben werden!", new String[] { "OK" });
					resetChange = true;
					cardLearnLimit.setText(lastValidCardLimit);
					resetChange = false;
				}
			}
		});

		Label autoWidthDescription = new Label("Wenn aktiviert, werden alle Buttons dem Gr�ssten angepasst. Sonst orientiert sich die Gr�sse jeweils am Namen des Buttons.");
		autoWidthDescription.setWrapText(true);

		boolean oldValue = false;
		if (getFXController().getModel("config").getDataList("widthState") != null && getFXController().getModel("config").getDataList("widthState").get(0) != null && getFXController().getModel("config").getDataList("widthState").get(0).equals("true"))
		{
			oldValue = true;
		}

		CheckBox autoWidth = new CheckBox("Gr�sse Anpassen");
		autoWidth.setSelected(oldValue);
		autoWidth.selectedProperty().addListener(e ->
		{
			debug.Debugger.out("Width property has changed");
			String value = autoWidth.selectedProperty().getValue() ? "true" : "000";
			getFXController().getModel("config").doAction(Command.SET, "widthState", value);
		});

		BackButton back = new BackButton(getFXController());

		VBox vLayout = new VBox(20);
		vLayout.setPadding(new Insets(30));
		vLayout.setMaxWidth(400);
		vLayout.setAlignment(Pos.CENTER);
		vLayout.getChildren().addAll(cardLimitDescription, cardLearnLimit, sepp(), autoWidthDescription, autoWidth, sepp());
		
		ScrollPane sc = new ScrollPane(vLayout);
		sc.setMaxWidth(400);
		sc.setHbarPolicy(ScrollBarPolicy.NEVER);
		
		HBox controlLayout = new HBox(back);
		controlLayout.setAlignment(Pos.CENTER);
		controlLayout.setPadding(new Insets(30));
		
		BorderPane mainLayout = new BorderPane(sc);
		mainLayout.setPadding(new Insets(30, 50, 0, 50));
		mainLayout.setBottom(controlLayout);
		
		getFXController().getModel("config").registerView(this);
		
		return mainLayout;
	}

	@Override
	public void refreshView ()
	{
		
	}
	
	private Separator sepp ()
	{
		return new Separator();
	}
}
