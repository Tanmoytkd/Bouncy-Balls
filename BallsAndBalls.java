/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package balls.and.balls;

import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author Tanmoy Krishna Das
 */
class movingBall extends Circle {

    double dx = 0;
    double dy = 0;
    Parent canvas;
    boolean Moving = false;
    Timeline timeline;

    public double getDx() {
        return dx;
    }

    public double getDy() {
        return dy;
    }

    public Parent getCanvas() {
        return canvas;
    }

    public boolean isMoving() {
        return Moving;
    }

    public movingBall() {
        this(0);
    }

    public movingBall(double radius) {
        this(radius, 0, 0);
    }

    public movingBall(double radius, double dx, double dy) {
        super();
        this.setRadius(radius);

        this.dx = dx;
        this.dy = dy;
    }

    public movingBall(double radius, double dx, double dy, Parent canvas) {
        super();
        this.setRadius(radius);

        this.dx = dx;
        this.dy = dy;

        this.canvas = canvas;

        timeline = Animator.createAnimation(canvas, this, dx, dy);
    }

    public movingBall(double radius, Parent canvas) {
        this(radius, 0, 0, canvas);
    }

    void play() {
        if (timeline != null) {
            timeline.play();
            Moving = true;
        }
    }

    void playFromStart() {
        if (timeline != null) {
            timeline.stop();
            timeline.play();
            Moving = true;
        }
    }

    void stop() {
        if (timeline != null) {
            timeline.stop();
            Moving = false;
        }
    }

    void clearTimeline() {
        if (timeline != null) {
            timeline.stop();
            timeline = null;
        }
    }

    void setParent(Parent canvas) {
        this.canvas = canvas;
        clearTimeline();

        timeline = Animator.createAnimation(this);
        if (Moving) {
            timeline.play();
        }
    }

    void setDx(double DX) {
        this.dx = DX;
        clearTimeline();

        if (canvas != null) {
            timeline = Animator.createAnimation(this);
            if (Moving) {
                timeline.play();
            }
        }
    }

    void setDy(double DY) {
        this.dy = DY;
        clearTimeline();

        if (canvas != null) {
            timeline = Animator.createAnimation(this);
            if (Moving) {
                timeline.play();
            }
        }
    }

    void increaseDx(double DX) {
        setDx(dx + DX);
    }

    void decreaseDx(double DX) {
        setDx(dx - DX);
    }

    void increaseDy(double DY) {
        setDy(dy + DY);
    }

    void decreaseDy(double DY) {
        setDy(dy - DY);
    }

    void reverseDx() {
        setDx(0 - dx);
    }

    void reverseDy() {
        setDy(0 - dy);
    }

    void reverse() {
        reverseDx();
        reverseDy();
    }
}

class Animator {

    public static Timeline createAnimation(movingBall ball) {
        Parent canvas = ball.getCanvas();
        double dx = ball.getDx();
        double dy = ball.getDy();
        return createAnimation(canvas, ball, dx, dy);
    }

    public static Timeline createAnimation(Parent canvas, Circle ball, double DX, double DY) {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(20),
                new EventHandler<ActionEvent>() {

            double dx = DX;
            double dy = DY;

            @Override
            public void handle(ActionEvent t) {

                ball.setLayoutX(ball.getLayoutX() + dx);
                ball.setLayoutY(ball.getLayoutY() + dy);

                Bounds bounds = canvas.getBoundsInLocal();

                if (ball.getLayoutX() <= (bounds.getMinX() + ball.getRadius())) {
                    dx = Math.abs(dx);
                }
                if (ball.getLayoutX() >= (bounds.getMaxX() - ball.getRadius())) {
                    dx = -Math.abs(dx);
                }

                if (ball.getLayoutY() <= (bounds.getMinY() + ball.getRadius())) {
                    dy = Math.abs(dy);
                }
                if ((ball.getLayoutY() >= (bounds.getMaxY() - ball.getRadius()))) {
                    dy = -Math.abs(dy);
                }

            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);

        return timeline;
    }

}

class BallController implements Runnable {

    ArrayList<movingBall> balls;

    public BallController(ArrayList<movingBall> balls) {
        this.balls = balls;
    }

    public static double distance(movingBall a, movingBall b) {
        double x1 = a.getLayoutX();
        double x2 = b.getLayoutX();
        double y1 = a.getLayoutY();
        double y2 = b.getLayoutY();

        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    @Override
    public void run() {
        while (true) {
            for (int i = 0; i < balls.size(); i++) {
                for (int j = i + 1; j < balls.size(); j++) {
                    movingBall b1 = balls.get(i);
                    movingBall b2 = balls.get(j);
                    if (distance(b1, b2) < (b1.getRadius() + b2.getRadius())) {
                        if((b1.getDx()>0 && b2.getDx()>0) || (b1.getDx()<=0 && b2.getDx()<=0) ) {
                            if(b1.getLayoutX()<b2.getLayoutX())b1.reverseDx();
                            else b2.reverseDx();
                        }
                        else {
                            double dy1 = b1.getDy();
                            double dy2 = b2.getDy();

                            if((dy1<0 && dy2<0) || (dy1>0 && dy2>0)) {
                                b1.reverseDx();
                                b2.reverseDx();
                            }
                            else {
                                b1.reverseDy();
                                b2.reverseDy();
                            }
                        }

                        //b1.reverse();
                        //b2.reverse();
                        try {
                            sleep(100);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(BallController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        }
    }
}

public class BallsAndBalls extends Application {

    ArrayList<movingBall> balls = new ArrayList<movingBall>();

    @Override
    public void start(Stage stage) {

        Pane canvas = new Pane();
        Scene scene = new Scene(canvas, 500, 500, Color.ALICEBLUE);

        movingBall b1 = new movingBall(20, 15, 7, canvas);
        b1.setFill(Color.RED);
        b1.relocate(10, 10);
        movingBall b2 = new movingBall(20, 10, -5, canvas);
        b2.setFill(Color.GREEN);
        b2.relocate(50, 50);

        balls.add(b1);
        balls.add(b2);

        canvas.getChildren().addAll(b1, b2);

        stage.setTitle("Animated Ball");
        stage.setScene(scene);
        stage.show();

        b1.play();
        b2.play();

        new Thread(new BallController(balls)).start();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
