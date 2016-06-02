package views;

import java.io.File;

import globals.Functions;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import mvc.fx.FXController;
import mvc.fx.FXView;
import views.components.AppButton;

/**
 * Hilfe System Index-Suche
 *  
 * @author hugo-lucca
 *
 */
public class HelpSerachView extends FXView
{

	public HelpSerachView(String newName, FXController newController) {
		// this constructor is the same for all view's
		super(newController);
		construct(newName);
	}

	@Override
	public Parent constructContainer() {
		// TODO Auto-generated method stub

		Label labelText;
		try {
			labelText = new Label (Functions.fileToString(new File(
					"src\\views\\txt\\index.txt")) );
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			labelText = new Label("leer");
		}
		
		
		labelText.setWrapText(true);
		labelText.setMaxWidth(800);
		labelText.setId("impressumtext");

		Label labelTitel = new Label("Index");
		labelTitel.setId("impressumtitel");

		AppButton backBtn = new AppButton("_Zur�ck");
		backBtn.setOnAction(e -> getFXController().showMainView());

		BorderPane headLayout = new BorderPane(labelTitel);
		headLayout.setPadding(new Insets(20));
		
		ScrollPane scroller = new ScrollPane();
		scroller.setMaxWidth(800);
		scroller.setContent(labelText);
		scroller.setHbarPolicy(ScrollBarPolicy.NEVER);
		scroller.setVbarPolicy(ScrollBarPolicy.ALWAYS);

		HBox controlLayout = new HBox(20);
		controlLayout.setAlignment(Pos.BOTTOM_CENTER);
		controlLayout.getChildren().addAll(backBtn);
		controlLayout.setPadding(new Insets(10));

		BorderPane mainLayout = new BorderPane();
		mainLayout.setPadding(new Insets(15));
		mainLayout.setTop(headLayout);
		mainLayout.setCenter(scroller);
		mainLayout.setBottom(controlLayout);

		return mainLayout;
	}

	@Override
	public void refreshView() {
		// TODO Auto-generated method stub
		
	}
}
