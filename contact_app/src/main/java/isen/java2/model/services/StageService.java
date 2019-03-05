package isen.java2.model.services;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class StageService {
	
	private StageService() {
	}

	private static class StageServiceHolder {
		private static final StageService INSTANCE = new StageService();
	}

	public static StageService getInstance() {
		return StageServiceHolder.INSTANCE;
	}

	private Stage primaryStage;

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	public void closeStage() {
		primaryStage.fireEvent(new WindowEvent(primaryStage, WindowEvent.WINDOW_CLOSE_REQUEST));

	}

}
