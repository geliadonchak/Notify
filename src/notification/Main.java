package notification;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Notify.Builder notifyJava = new Notify.Builder(primaryStage);
        Notify notify = notifyJava
                .title("Уведомление!")
                .message("Какой-то текст")
                .appName("Название приложения")
                .backgroundOpacity(1)
                .textInput()
                .comboBox("Ubuntu", "Ubuntu", "Fedora", "Mint", "Debian", "Arch", "ElementaryOS", "Gentoo")
                .okButton("OK", event -> {
                    System.out.println(notifyJava.getTextFieldValue());
                    System.out.println(notifyJava.getComboBoxValue());
                })
                .cancelButton("CANCEL", event -> System.out.println("cancel"))
                .iconPathURL("https://yt3.ggpht.com/a/AATXAJzrZnDG0bZJwgw4Bg1BpvS4dRqLzE5ZcQXFcIe9=s900-c-k-c0xffffffff-no-rj-mo")
                .build();
    }
}