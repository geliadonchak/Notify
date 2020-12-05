package notification;

import javafx.animation.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.awt.*;
import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;

public class Notify {

    private final NotificationParameters parameters;

    private final String title;
    private final String message;
    private final String appName;
    private final Stage popup;

    public Notify(Builder builder) {
        this.title = builder.title;
        this.message = builder.message;
        this.appName = builder.appName;
        this.parameters = builder.parameters;
        this.popup = builder.popup;
        VBox content = builder.content;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getAppName() {
        return appName;
    }

    public String getIconPathURL() {
        return parameters.getIconPathURL();
    }

    public NotificationParameters getParameters() {
        return parameters;
    }

    public enum Position {
        RIGHT_BOTTOM, RIGHT_TOP, LEFT_BOTTOM, LEFT_TOP
    }

    public enum Border {
        SQUARE, CIRCLE
    }

    public enum Durability {
        SHORT, LONG, NEVER
    }

    public enum Animation {
        TRANSPARENT, DISPLAY, ROTATE
    }

    public enum Sounds {
        APPLE, ICQ, TELEGRAM, VK
    }

    public void show() {
        popup.show();
    }

    public static class Builder {
        private final Stage primaryStage;
        private final Stage popup = new Stage();
        private final VBox content = new VBox();
        private final VBox messageLayout = new VBox();
        private final HBox messageContent = new HBox();
        private final VBox inputContent = new VBox();
        private final HBox actionsContent = new HBox();

        private final NotificationParameters parameters = new NotificationParameters();
        private static final Duration ANIMATION_DURATION = Duration.millis(600);
        private static final double POPUP_WIDTH = 350;
        final ComboBox<String> comboBox = new ComboBox<>();
        private static final ArrayList<String> arrayListComboBox = new ArrayList<>();
        private TextField textField;

        private String newValueTextField;
        private String title;
        private String message;
        private String appName;
        private String okButtonText;
        private String cancelButtonText;
        private EventHandler<ActionEvent> okButtonListener;
        private EventHandler<ActionEvent> cancelButtonListener;
        private String comboBoxSelectedValue;

        public Builder(Stage primaryStage) {
            this.primaryStage = primaryStage;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder appName(String app_name) {
            this.appName = app_name;
            return this;
        }

        public String getComboBoxValue() {
            return comboBox.getValue();
        }

        public String getTextFieldValue() {
            return textField.getText();
        }

        // ==================
        // Parameters setters
        // ==================

        public Builder sound(Sounds sound) {
            parameters.setSound(sound);
            return this;
        }

        public Builder waitTime(Durability waitTime) {
            parameters.setWaitTime(waitTime);
            return this;
        }

        public Builder animation(Animation animation) {
            parameters.setAnimation(animation);
            return this;
        }

        public Builder position(Position pos) {
            this.parameters.setPosition(pos);
            return this;
        }

        public Builder textColorTitle(String textColor) {
            parameters.setTextColorTitle(textColor);
            return this;
        }

        public Builder iconBorder(Border border) {
            parameters.setIconBorder(border);
            return this;
        }

        public Builder textColorMessage(String textColor) {
            parameters.setTextColorMessage(textColor);
            return this;
        }

        public Builder backgroundColor(String backgroundColor) {
            parameters.setBackgroundColor(backgroundColor);
            return this;
        }

        public Builder backgroundOpacity(double backgroundOpacity) {
            parameters.setBackgroundOpacity(backgroundOpacity);
            return this;
        }

        public Builder iconPathURL(String iconPathURL) {
            parameters.setIconPathURL(iconPathURL);
            return this;
        }

        // ===============
        // Elements layout
        // ===============

        private void createBaseLayout() {
            content.setStyle("-fx-background-color:" + parameters.getBackgroundColor());
            content.setPadding(new Insets(5));
            messageContent.setPadding(new Insets(5));
            messageContent.setSpacing(10.0);

            addImage();
            addLabel();

            inputContent.setSpacing(10);
            inputContent.setPadding(new Insets(5));

            if (!arrayListComboBox.isEmpty()) {
                addComboBox();
            }

            content.getChildren().add(inputContent);

            addButtons();
        }

        public Notify build() {
            createBaseLayout();

            new Thread(new Task<>() {
                @Override
                protected Object call() throws Exception {
                    if (parameters.getWaitTime() != Durability.NEVER) {
                        if (parameters.getWaitTime() == Durability.SHORT)
                            Thread.sleep(5000);
                        else if (parameters.getWaitTime() == Durability.LONG)
                            Thread.sleep(24000);
                        closeAnimation();
                    }
                    return null;
                }
            }).start();

            StringProperty height = new SimpleStringProperty();
            height.addListener((observable, oldValue, newValue) -> newValueTextField = newValue);

            if (textField != null) {
                textField.textProperty().bindBidirectional(height);
            }

            content.addEventFilter(MouseEvent.MOUSE_ENTERED, MouseEvent -> popup.setOpacity(1));
            content.addEventFilter(MouseEvent.MOUSE_EXITED, MouseEvent -> popup.setOpacity(parameters.getBackgroundOpacity()));

            Rectangle2D screenRect = Screen.getPrimary().getBounds();
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice defaultScreenDevice = ge.getDefaultScreenDevice();
            GraphicsConfiguration defaultConfiguration = defaultScreenDevice.getDefaultConfiguration();
            java.awt.Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(defaultConfiguration);

            double shift = 15;
            switch (parameters.getPosition()) {
                case LEFT_TOP -> {
                    popup.setX(shift + screenInsets.left);
                    popup.setY(shift + screenInsets.top);
                }
                case RIGHT_TOP -> {
                    popup.setX(screenRect.getWidth() - POPUP_WIDTH - shift - screenInsets.right);
                    popup.setY(shift + screenInsets.top);
                }
                case LEFT_BOTTOM -> {
                    popup.setX(shift + screenInsets.left);
                    popup.setY(screenRect.getHeight() - screenInsets.bottom - 205 - shift - content.getHeight());
                }
                case RIGHT_BOTTOM -> {
                    popup.setX(screenRect.getWidth() - POPUP_WIDTH - shift - screenInsets.right);
                    popup.setY(screenRect.getHeight() - screenInsets.bottom - 205 - shift - content.getHeight());
                }
            }

            Scene scene = new Scene(content);
            scene.setFill(Color.TRANSPARENT);
            popup.setWidth(POPUP_WIDTH);
            popup.setAlwaysOnTop(true);
            popup.initOwner(primaryStage);
            popup.initStyle(StageStyle.TRANSPARENT);
            popup.show();

            Media media = switch (parameters.getSound()) {
                case APPLE -> new Media(Paths.get("resources/apple.wav").toUri().toString());
                case ICQ -> new Media(Paths.get("resources/icq.wav").toUri().toString());
                case TELEGRAM -> new Media(Paths.get("resources/telegram.wav").toUri().toString());
                case VK -> new Media(Paths.get("resources/vk.wav").toUri().toString());
            };

            MediaPlayer player = new MediaPlayer(media);
            player.play();

            popup.setScene(scene);
            openAnimation();

            return new Notify(this);
        }

        public Builder textInput() {
            textField = new TextField();
            inputContent.getChildren().add(textField);
            return this;
        }

        public Builder comboBox(String selectedValue, String... values) {
            Collections.addAll(arrayListComboBox, values);
            comboBoxSelectedValue = selectedValue;
            return this;
        }

        public Builder okButton(String name, final EventHandler<ActionEvent> listener) {
            this.okButtonText = name;
            this.okButtonListener = listener;
            return this;
        }

        public Builder cancelButton(String name, final EventHandler<ActionEvent> listener) {
            this.cancelButtonText = name;
            this.cancelButtonListener = listener;
            return this;
        }

        // ==================
        // Elements functions
        // ==================

        private void addComboBox() {
            comboBox.getItems().addAll(arrayListComboBox);
            comboBox.setValue(comboBoxSelectedValue);
            comboBox.setVisibleRowCount(5);
            comboBox.setPrefWidth(POPUP_WIDTH);
            comboBox.setStyle("-fx-background-color: #ffffff; -fx-text-fill: #000000");
            comboBox.setPadding(new Insets(0, 5, 0, 5));
            inputContent.getChildren().addAll(comboBox);
        }

        private void addButtons() {
            actionsContent.setPrefWidth(POPUP_WIDTH);
            actionsContent.setSpacing(10.0);
            actionsContent.setPadding(new Insets(5));

            if (this.okButtonListener != null) {
                Button okButton = new Button(this.okButtonText);
                okButton.setPrefWidth(actionsContent.getPrefWidth());
                okButton.setStyle("-fx-background-color: #626262; -fx-text-fill: white");
                okButton.setOnAction(this.okButtonListener);
                okButton.addEventFilter(MouseEvent.MOUSE_PRESSED, MouseEvent -> closeAnimation());
                actionsContent.getChildren().add(okButton);
            }

            if (this.cancelButtonListener != null) {
                Button cancelButton = new Button(this.cancelButtonText);
                cancelButton.setPrefWidth(actionsContent.getPrefWidth());
                cancelButton.setStyle("-fx-background-color: #626262; -fx-text-fill: white");
                cancelButton.setOnAction(this.cancelButtonListener);
                cancelButton.addEventFilter(MouseEvent.MOUSE_PRESSED, MouseEvent -> closeAnimation());
                actionsContent.getChildren().add(cancelButton);
            }

            content.getChildren().add(actionsContent);
        }

        private void addImage() {
            String path = parameters.getIconPathURL();

            if (!parameters.getIconPathURL().isEmpty()) {
                if (!parameters.getIconPathURL().startsWith("http")) {
                    try {
                        path = new File(parameters.getIconPathURL()).toURI().toURL().toString();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }

                Shape iconBorder = switch (parameters.getIconBorder()) {
                    case CIRCLE -> new Circle(80, 80, 40);
                    case SQUARE -> new Rectangle(0, 0, 80, 80);
                };
                iconBorder.setFill(new ImagePattern(new Image(path)));
                messageContent.getChildren().add(iconBorder);
            }
        }

        private void addLabel() {
            if (this.title != null) {
                Label title = new Label(this.title);
                title.setFont(Font.font(24));
                title.setStyle("-fx-text-fill:" + parameters.getTextColorTitle());
                messageLayout.getChildren().add(title);
            }

            if (this.message != null) {
                Label message = new Label(this.message);
                message.setMaxWidth(POPUP_WIDTH - 100);
                message.setWrapText(true);
                message.setFont(Font.font(18));
                message.setStyle("-fx-text-fill:" + parameters.getTextColorMessage());
                messageLayout.getChildren().add(message);
            }

            if (this.appName != null) {
                Label app = new Label(this.appName);
                app.setFont(Font.font(14));
                app.setStyle("-fx-text-fill:" + parameters.getTextColorMessage());
                messageLayout.getChildren().add(app);
            }

            messageContent.getChildren().add(messageLayout);
            content.getChildren().add(messageContent);
        }

        // ==========
        // Animations
        // ==========

        private void openAnimation() {
            int xDirection = parameters.getPosition() == Position.RIGHT_BOTTOM || parameters.getPosition() == Position.RIGHT_TOP ? 1 : -1;

            switch (parameters.getAnimation()) {
                case ROTATE -> {
                    RotateTransition animation = new RotateTransition(Builder.ANIMATION_DURATION, content);
                    animation.setFromAngle(xDirection == 1 ? 360 : 0);
                    animation.setToAngle(xDirection == 1 ? 0 : 360);
                    animation.setCycleCount(1);
                    animation.play();
                }
                case TRANSPARENT -> {
                    FadeTransition animation = new FadeTransition(Builder.ANIMATION_DURATION, content);
                    animation.setFromValue(0);
                    animation.setToValue(1);
                    animation.setCycleCount(1);
                    animation.play();
                }
                case DISPLAY -> {
                    TranslateTransition animation = new TranslateTransition(Builder.ANIMATION_DURATION, content);
                    animation.setByX(-xDirection * Builder.POPUP_WIDTH);
                    animation.setFromX(xDirection * Builder.POPUP_WIDTH);
                    animation.play();
                }
            }
        }

        private void closeAnimation() {
            int xDirection = parameters.getPosition() == Position.RIGHT_BOTTOM || parameters.getPosition() == Position.RIGHT_TOP ? 1 : -1;

            switch (parameters.getAnimation()) {
                case ROTATE -> {
                    RotateTransition animation = new RotateTransition(Builder.ANIMATION_DURATION, content);
                    animation.setFromAngle(xDirection == 1 ? 0 : 360);
                    animation.setToAngle(xDirection == 1 ? 360 : 0);
                    animation.setCycleCount(1);
                    animation.setOnFinished(event -> popup.close());
                    animation.play();
                }
                case TRANSPARENT -> {
                    FadeTransition animation = new FadeTransition(Builder.ANIMATION_DURATION, content);
                    animation.setFromValue(1);
                    animation.setToValue(0);
                    animation.setCycleCount(1);
                    animation.setOnFinished(event -> popup.close());
                    animation.play();
                }
                case DISPLAY -> {
                    TranslateTransition animation = new TranslateTransition(Builder.ANIMATION_DURATION, content);
                    animation.setByX(xDirection * Builder.POPUP_WIDTH);
                    animation.setOnFinished(event -> popup.close());
                    animation.play();
                }
            }
        }
    }
}