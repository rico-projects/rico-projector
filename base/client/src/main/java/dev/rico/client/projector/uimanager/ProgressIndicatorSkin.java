package dev.rico.client.projector.uimanager;

import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
import dev.rico.client.projector.mixed.Image;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.Random;

public class ProgressIndicatorSkin extends BehaviorSkinBase<ProgressIndicator, BehaviorBase<ProgressIndicator>> {

    protected ProgressIndicatorSkin(ProgressIndicator control, BehaviorBase<ProgressIndicator> behavior) {
        super(control, behavior);
        VBox vBox = new VBox();
        vBox.setSpacing(20);
        vBox.setAlignment(Pos.CENTER);
        ImageView imageView = new ImageView(Image.SPROUTS_PROGRESS_1);
        imageView.setFitWidth(130);
        imageView.setPreserveRatio(true);
        SequentialTransition sequentialTransition = new SequentialTransition();
        ScaleTransition scale1 = new ScaleTransition(Duration.millis(300), imageView);
        scale1.setFromX(1);
        scale1.setFromY(1);
        scale1.setToX(0.95);
        scale1.setToY(0.95);
        ScaleTransition scale2 = new ScaleTransition(Duration.millis(300), imageView);
        scale2.setFromX(0.97);
        scale2.setFromY(0.97);
        scale2.setToX(1);
        scale2.setToY(1);
        sequentialTransition.getChildren().setAll(scale1, scale2);
        sequentialTransition.setCycleCount(Timeline.INDEFINITE);
        sequentialTransition.play();
        Label label = new Label();
        label.setStyle("-fx-font-size: 24");
        vBox.getChildren().addAll(imageView, label);
        ChangeListener<Boolean> activePropertyListener = (observable, oldValue, newValue) -> {
            if (newValue) {
                String[] texts = {"Zurücklehnen!", "Bitte warten.", "Entspannen!", "Geduld bitte.",
                        "Wir arbeiten daran.", "Wir bereiten da mal was vor...", "Mal sehen...",
                        "Fast fertig...", "Unser Herz schlägt für Dich!", "Ganz schön anstrengend!",
                        "Für Dich tun wir alles!", "Gleich haben wir es.", "Wir schlagen alle Rekorde.",
                        "Puh!", "Ein Stündchen noch :-)"};
                Random random1 = new Random();
                int random = random1.nextInt(texts.length - 1 + 1);
                label.setText(texts[random]);
                imageView.setScaleX(1);
                imageView.setScaleY(1);
                imageView.setOpacity(1);
                sequentialTransition.play();
            } else {
                sequentialTransition.stop();
                SequentialTransition removeTransition = new SequentialTransition();
                FadeTransition fadeTransition =
                        new FadeTransition(Duration.millis(300), imageView);
                fadeTransition.setFromValue(1);
                fadeTransition.setToValue(0);
                removeTransition.getChildren().setAll(fadeTransition);
                removeTransition.play();
            }
        };
        activePropertyListener.changed(null, null, getSkinnable().isActive());
        getSkinnable().activeProperty().addListener(activePropertyListener);

        getChildren().addAll(vBox);
    }
}
