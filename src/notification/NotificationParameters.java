package notification;

public class NotificationParameters {
    private Notify.Durability waitTime;
    private Notify.Animation animation;
    private Notify.Position position;
    private Notify.Border iconBorder;
    private Notify.Sounds sound;
    private String textColorTitle;
    private String textColorMessage;
    private String backgroundColor;
    private double backgroundOpacity;
    private String iconPathURL;

    NotificationParameters() {
        waitTime = Notify.Durability.SHORT;
        animation = Notify.Animation.DISPLAY;
        position = Notify.Position.RIGHT_BOTTOM;
        iconBorder = Notify.Border.CIRCLE;
        sound = Notify.Sounds.ICQ;
        textColorTitle = "#FFFFFF";
        textColorMessage = "#b0b0b0";
        backgroundColor = "#1c1c1c";
        backgroundOpacity = 1;
    }

    public Notify.Durability getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(Notify.Durability waitTime) {
        this.waitTime = waitTime;
    }

    public Notify.Animation getAnimation() {
        return animation;
    }

    public void setAnimation(Notify.Animation animation) {
        this.animation = animation;
    }

    public Notify.Position getPosition() {
        return position;
    }

    public Notify.Border getIconBorder() {
        return iconBorder;
    }

    public void setIconBorder(Notify.Border iconBorder) {
        this.iconBorder = iconBorder;
    }

    public void setPosition(Notify.Position position) {
        this.position = position;
    }

    public String getTextColorTitle() {
        return textColorTitle;
    }

    public void setTextColorTitle(String textColorTitle) {
        this.textColorTitle = textColorTitle;
    }

    public String getTextColorMessage() {
        return textColorMessage;
    }

    public void setTextColorMessage(String textColorMessage) {
        this.textColorMessage = textColorMessage;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public double getBackgroundOpacity() {
        return backgroundOpacity;
    }

    public void setBackgroundOpacity(double backgroundOpacity) {
        this.backgroundOpacity = backgroundOpacity;
    }

    public String getIconPathURL() {
        return iconPathURL;
    }

    public void setIconPathURL(String iconPathURL) {
        this.iconPathURL = iconPathURL;
    }

    public Notify.Sounds getSound() {
        return sound;
    }

    public void setSound(Notify.Sounds sound) {
        this.sound = sound;
    }
}
