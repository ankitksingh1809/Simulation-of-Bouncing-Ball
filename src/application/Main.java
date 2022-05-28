package application;
	
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


public class Main extends Application { 
	private double restitution;
	@Override
	public void start(Stage primaryStage) {
        Slider s1 = new Slider(0,1,0.9);
        Slider s2 = new Slider(0,400,100);
        restitution=0.9;
        Ball b1=new Ball();
        Circle ball = new Circle(200,(400-b1.height),10);
        ball.setFill(Color.RED);
        

        s2.setShowTickLabels(true);
        s2.setShowTickMarks(true);
        s1.setShowTickLabels(true);
        s1.setShowTickMarks(true);
        
        Button btn=new Button("Start Simulation");  
        VBox pane = new VBox();
        HBox pane1 = new HBox();
        pane1.getChildren().addAll(s2,s1,btn);
        Pane pane2 = new Pane(ball);
        pane.getChildren().addAll(pane1,pane2);
        b1.height=s2.getValue();
        s2.valueProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(
               ObservableValue<? extends Number> observableValue, 
               Number oldValue, 
               Number newValue) { 
                  b1.height=newValue.doubleValue();
              }
        });
        
        s1.valueProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(
               ObservableValue<? extends Number> observableValue, 
               Number oldValue, 
               Number newValue) { 
                  restitution=newValue.doubleValue();
              }
        });
        

        AnimationTimer timer = new AnimationTimer() {

            long lastUpdate=0;
//            long lastUpdate1=0;
            double changeY=0;
            double gravity=9.8;
            int n2=0;

            @Override
            public void handle(long now) {
                if (lastUpdate == 0) {
                    lastUpdate = now;
                    return;
                }
                long elapsedNanos = now - lastUpdate;
                double elapsedSeconds = elapsedNanos / 1000000000.0;
//                lastUpdate1=lastUpdate;
                lastUpdate = now ;
                double n1=changeY;
                changeY=(b1.velocity*(elapsedSeconds*50));
                if (ball.getCenterY() + changeY + ball.getRadius() >= (pane.getHeight()-pane1.getHeight())) {
                    b1.velocity = - b1.velocity * restitution;
                    
                } else {
                	
                    b1.velocity = b1.velocity + gravity * elapsedSeconds;
                    if(n1<=0) {
                    	if(changeY>0) {
                    		n2++;
                    		System.out.println("height of " + n2 + "th bounce is: " + ((pane.getHeight()-pane1.getHeight())-ball.getCenterY()));
                    		if(n2==10) {
                    			lastUpdate = 0;
                    	        changeY = 0;
                    	        n2=0;
                    	        b1.velocity=0;
//                    	        lastUpdate1=0;
                    			btn.setDisable(false);
                    			stop();
                    		}
                    	}
                    }
                }
                ball.setCenterY(Math.min(ball.getCenterY() + changeY,(pane.getHeight()-pane1.getHeight())-ball.getRadius()));
            }
            
        };

        primaryStage.setScene(new Scene(pane, 400, 400));
        primaryStage.show();
        btn.setOnAction(new EventHandler<ActionEvent>() {  
            
            @Override  
            public void handle(ActionEvent arg0) {  
            	ball.setCenterY((pane.getHeight()-pane1.getHeight())-(b1.height));
            	btn.setDisable(true);
            	 timer.start();
            }  
        } );  
    }

    public static void main(String[] args) {
        launch(args);
    }
}
