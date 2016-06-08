package digitalpromo.cabsdemo.dialogs;

import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Should be implemented by activity/fragment to process dialog buttons press
 */
public interface DialogButtonsListener {
    void OnDialogPositiveClick(DialogInterface dialog, Bundle data);
    void OnDialogNegativeClick(DialogInterface dialog);
}
