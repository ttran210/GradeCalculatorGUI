
import javafx.collections.FXCollections;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;

import java.awt.Color;
import java.awt.Paint;

import javafx.application.*;
import javafx.scene.control.*;
import javafx.stage.*;
import javafx.scene.layout.*;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;

@SuppressWarnings("restriction")

public class CalcGUI extends Application {
	private TextArea grade;
	private TextField classField, categoryField, gradeDesiredField, weightField, ptsEarnedField, ptsAvailField;
	private TableView<GradingItem> table;
	private Button addItem, calculateGradeNeeded, clear;

	public void start(Stage primaryStage) {
		// Making main scene
		int sceneWidth = 800, sceneHeight = 400;
		primaryStage.setTitle("Final Grade Calculator");

		BorderPane mainPane = new BorderPane();
		mainPane.setPrefSize(sceneWidth, sceneHeight);

		// Creating table
		table = new TableView<>();
		// Create table
		final ObservableList<GradingItem> data = FXCollections.observableArrayList();
		//data.add(new GradingItem("test", 30, 20, 20));
		//table.setEditable(true);
	

		// Adding columns and associating data
		TableColumn<GradingItem, String> catCol = new TableColumn<>("Category");
		catCol.setMinWidth(120);
		// use names of fields in GradingItems class
		catCol.setCellValueFactory(new PropertyValueFactory<GradingItem, String>("category"));

		// Creating weight column
		TableColumn<GradingItem, Double> weightCol = new TableColumn<>("Weight");
		weightCol.setMinWidth(120);
		weightCol.setCellValueFactory(new PropertyValueFactory<GradingItem, Double>("weight"));

//		// Creating points received column
//		TableColumn<GradingItem, Double> pointsRecCol = new TableColumn<>("Pts for Category");
//		pointsRecCol.setMinWidth(100);
//		pointsRecCol.setCellValueFactory(new PropertyValueFactory<GradingItem, Double>("pointsForCat"));
		
		// Creating points out of total column
		TableColumn<GradingItem, Double> pointsOutOfTotCol = new TableColumn<>("Pts out of Total");
		pointsOutOfTotCol.setMinWidth(120);
		pointsOutOfTotCol.setCellValueFactory(new PropertyValueFactory<GradingItem, Double>("pointsTowardTotal"));
		
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		
		table.setItems(data);
		// List that keeps all data in table

		// Creating and populating table
		table.getColumns().add(catCol);
		table.getColumns().add(weightCol);
	//	table.getColumns().add(pointsRecCol);
		table.getColumns().add(pointsOutOfTotCol);

		mainPane.setLeft(table);
		
		// Creating the input area
		// Input fields to calculate grades
		
		VBox inputArea = new VBox(20);
		//inputArea.setPrefSize(sceneWidth / 4, sceneHeight / 4);
		
		// HBox for info
		HBox row1 = new HBox(20);
		HBox row2 = new HBox(20);
		HBox row3 = new HBox(10);
		HBox row4 = new HBox(20);
		
		// Row 1
		Label className = new Label("Class: ");
		classField = new TextField();
		row1.getChildren().addAll(className, classField);

		// Row 2
		Label gradeDesired = new Label("Grade desired: ");
		gradeDesiredField = new TextField();
		gradeDesiredField.setPrefSize(60, 40);
		row2.getChildren().addAll(gradeDesired, gradeDesiredField);
		
		// Row 3
		Label categoryLabel = new Label("Category: ");
		categoryField = new TextField();
		categoryField.setPrefSize(80, 20);

		Label weightLabel = new Label("Weight (in percentage points): ");
		weightField = new TextField();
		weightField.setPrefSize(40, 20);
		
		row3.getChildren().addAll(categoryLabel, categoryField, weightLabel, weightField);

		// Row 4
		Label ptsEarnedLabel = new Label("Points Earned: ");
		ptsEarnedField = new TextField();
		ptsEarnedField.setPrefSize(40, 20);

		Label ptsAvailLabel = new Label("Points Available: ");
		ptsAvailField = new TextField();
		ptsAvailField.setPrefSize(40, 20);

		row4.getChildren().addAll(ptsEarnedLabel, ptsEarnedField, ptsAvailLabel, ptsAvailField);


		// Add to input borderpane
		inputArea.getChildren().addAll(row1, row2, row3, row4);
		
//		 Adding buttons to input area
		BorderPane bottom = new BorderPane();
		VBox buttons = new VBox(20);
		addItem = new Button("Add");
		calculateGradeNeeded = new Button("Calculate");
		clear = new Button("Clear");
		addItem.setPrefSize(80, 40);
		calculateGradeNeeded.setPrefSize(80, 40);
		clear.setPrefSize(80, 40);
		
		buttons.getChildren().addAll(addItem, calculateGradeNeeded, clear);
		buttons.setPadding(new Insets(10, 20, 10, 20));
		bottom.setLeft(buttons);
		
		// Adding grade info to input area
		VBox gradeArea = new VBox();
		Label gradeTitle = new Label("Grade needed: ");
		gradeTitle.setFont(Font.font(STYLESHEET_CASPIAN, FontWeight.BOLD, 20));
		grade = new TextArea();
		grade.setPrefSize(200, 100);
		grade.setFont(Font.font(STYLESHEET_CASPIAN, 40));
		gradeArea.getChildren().addAll(gradeTitle, grade);
		bottom.setRight(gradeArea);
		inputArea.getChildren().add(bottom);
		inputArea.setPadding(new Insets(20, 10, 20, 20));
		mainPane.setRight(inputArea);


		// Buttons

		// AddItem button
		addItem.setOnAction(e -> {
			// Creating grading item and adding to data
			data.add(new GradingItem(categoryField.getText(), Double.parseDouble(weightField.getText()),
					Double.parseDouble(ptsEarnedField.getText()), Double.parseDouble(ptsAvailField.getText())));
			// clearing input fields
			categoryField.clear();
			weightField.clear();
			ptsEarnedField.clear();
			ptsAvailField.clear();
			table.setItems(data);

		});

		// Calculation button
		calculateGradeNeeded.setOnAction(e -> {
			double totalPtsEarned = 0;
			double gradeNeeded;
			double otherWeights = 0;
			
			for (GradingItem g : data) {
				totalPtsEarned += g.getPointsTowardTotal();
				otherWeights += g.getWeight();
			}
			
			double finalWeight = 100 - otherWeights;
	
			gradeNeeded = (Double.parseDouble(gradeDesiredField.getText()) - totalPtsEarned) / finalWeight * 100;
			// Display grade
			grade.setText(Double.toString(Math.round(gradeNeeded)));
		
			
		});

		// Clear button
		clear.setOnAction(e -> {
			data.clear();
		});

		// display the stage
		Scene scene = new Scene(mainPane, sceneWidth, sceneHeight);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		Application.launch(args);
	}

	/* Class for a grading item */

	public static class GradingItem {

		private SimpleStringProperty category;
		private SimpleDoubleProperty weight;
		private SimpleDoubleProperty pointsForCat;
		private SimpleDoubleProperty pointsTowardTotal;

		// Constructor for grading item. Has a category, weight in percentage points, points
		// received is calculated, points toward total
		private GradingItem(String catIn, double weightIn, double pointsEarned, double totalPoints) {
			category = new SimpleStringProperty(catIn);
			weight = new SimpleDoubleProperty(weightIn);
			pointsForCat = new SimpleDoubleProperty(pointsEarned / totalPoints);
			pointsTowardTotal = new SimpleDoubleProperty(weight.get() * pointsForCat.get());
		}

		public String getCategory() {
			return category.get();
		}

		public void setCategory(String catIn) {
			category.set(catIn);
		}

		public Double getWeight() {
			return weight.get();
		}

		public void setWeight(double weightIn) {
			weight.set(weightIn);
		}

		public Double getPointsForCat() {
			return pointsForCat.get();
		}
		
		public void setPointsForCat(double ptsIn) {
			pointsForCat.set(ptsIn);
		}

		public Double getPointsTowardTotal() {
			return pointsTowardTotal.get();
		}

		public void setPtsTot(double pts) {
			pointsTowardTotal.set(pts);
		}

		public String toString() {
			return category + weight.toString();
		}

	}

}
