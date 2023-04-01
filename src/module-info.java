module Assignment5 {
	requires javafx.controls;
	requires javafx.web;
	requires javafx.fxml;
	requires javafx.graphics;
	
	opens application to javafx.graphics, javafx.fxml;
}
