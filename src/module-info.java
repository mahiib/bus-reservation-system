module DbmsProj {


		requires javafx.controls;
		requires java.sql;
		requires javafx.graphics;
		requires javafx.base;
		
		opens application to javafx.graphics, javafx.fxml,javafx.base;
	

}
