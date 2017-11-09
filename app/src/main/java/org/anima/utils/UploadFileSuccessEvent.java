package org.anima.utils;

import com.google.firebase.storage.UploadTask;

/**
 * Created by momo on 21/12/16.
 */
public class UploadFileSuccessEvent {
    private final UploadTask.TaskSnapshot taskSnapshot;

    public UploadFileSuccessEvent(UploadTask.TaskSnapshot taskSnapshot) {
        this.taskSnapshot = taskSnapshot;
    }

    public UploadTask.TaskSnapshot getTaskSnapshot() {
        return taskSnapshot;
    }

}
