package taxi.lemon.events;

/**
 * Created by Takeitez on 02.03.2016.
 */
public class MessageEvent {
    private final int event;

    public static final int EVENT_MAKE_ORDER_FROM_HISTORY = 0x0;
    public static final int EVENT_SET_USER_PHONE = 0x1;

    public MessageEvent(int event) {
        this.event = event;
    }

    public int getEvent() {
        return event;
    }
}
