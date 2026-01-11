package edu.postech.csed232.control;

import edu.postech.csed232.event.Event;
import edu.postech.csed232.base.Observer;
import edu.postech.csed232.base.Subject;
import edu.postech.csed232.event.MessageEvent;
import edu.postech.csed232.view.MessageUI;
import org.jetbrains.annotations.NotNull;

/**
 * Observes message updates from a MessageService and updates the MessageUI component accordingly.
 */
public class MessageUIController implements Observer {

    @NotNull
    private final MessageUI messageUI;

    /**
     * Constructs the controller and registers it to the given MessageService.
     *
     * @param messageUI      the message display component
     * @param messageService the message model to observe
     */
    public MessageUIController(@NotNull MessageUI messageUI, @NotNull MessageService messageService) {
        this.messageUI = messageUI;
        messageService.addObserver(this);
    }

    /**
     * Updates the displayed message when a MessageEvent is received.
     *
     * @param caller the observable object
     * @param arg    the event containing the message text
     */
    @Override
    public void update(@NotNull Subject caller, @NotNull Event arg) {
        if (arg instanceof MessageEvent(String message)) {
            // TODO: implement this, and remove the following throw statement
            messageUI.setText(message);
        }
    }
}
