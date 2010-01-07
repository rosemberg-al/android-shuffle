package org.dodgybits.android.shuffle.server.tracks;

import android.content.ContentResolver;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.os.AsyncTask;
import org.dodgybits.android.shuffle.R;
import org.dodgybits.android.shuffle.model.Preferences;
import org.dodgybits.android.shuffle.service.Progress;

import java.util.LinkedList;


/**
 * This task synchronizes shuffle with a tracks service.
 * 
 * @author Morten Nielsen
 */
public class TracksSynchronizer extends AsyncTask<String, Progress, Void> {
    private static TracksSynchronizer synchronizer;
    private static LinkedList<SyncProgressListener> progressListeners = new LinkedList<SyncProgressListener>();
    private ContextWrapper activity;

    private TracksSynchronizer(ContextWrapper activity, WebClient client, String tracksUrl) {
        this(activity.getContentResolver(), activity.getResources(), client, activity, tracksUrl);
    }

    public static TracksSynchronizer getActiveSynchronizer(ContextWrapper context) throws WebClient.ApiException {
        TracksSynchronizer synchronizer = getSingletonSynchronizer(context);
        while(synchronizer.getStatus() == Status.FINISHED) {
                   synchronizer = getSingletonSynchronizer(context);
        }
        return synchronizer;
    }

     private static TracksSynchronizer getSingletonSynchronizer(ContextWrapper activity) throws WebClient.ApiException {
        if (synchronizer == null || synchronizer.getStatus() == Status.FINISHED) {

            synchronizer = new TracksSynchronizer(activity,  new WebClient(activity, Preferences.getTracksUser(activity), Preferences.getTracksPassword(activity)), Preferences.getTracksUrl(activity));
        }
        return synchronizer;
    }


    private final ContextSynchronizer contextSynchronizer;
    private final ProjectSynchronizer projectSynchronizer;
    private final TaskSynchronizer taskSynchronizer;
    private final Resources resources;

    private TracksSynchronizer(ContentResolver contentResolver, Resources resources, WebClient client, ContextWrapper activity, String tracksUrl) {
        this.contextSynchronizer = new ContextSynchronizer(contentResolver, resources, client, activity, this, tracksUrl, 0);
        this.projectSynchronizer = new ProjectSynchronizer(contentResolver, resources, client, activity, this, tracksUrl, 33);
        this.taskSynchronizer = new TaskSynchronizer(contentResolver, resources, client, activity, this, tracksUrl, 66);

        this.resources = resources;

        this.activity = activity;

    }

    @Override
    protected void onProgressUpdate(Progress... progresses) {
        for (SyncProgressListener listener : progressListeners) {
            listener.progressUpdate(progresses[0]);
        }
    }

    public void RegisterListener(SyncProgressListener listener) {
        progressListeners.add(listener);
    }

    public void unRegisterListener(SyncProgressListener
            listener) {
        progressListeners.remove(listener);
    }


    @Override
    protected Void doInBackground(String... strings) {
        try {
            contextSynchronizer.synchronize();
            projectSynchronizer.synchronize();
            taskSynchronizer.synchronize();
            publishProgress(Progress.createProgress(100, "Synchronization Complete"));
        } catch (Exception e) {
            publishProgress(Progress.createErrorProgress(resources.getString(R.string.error_message)));
        }
        return null;

    }

    @Override
    protected void onPostExecute(Void result) {
        try {
            synchronizer = getSingletonSynchronizer(activity);
        } catch (WebClient.ApiException ignored) {

        }
    }


    public void reportProgress(Progress progress) {
        publishProgress(progress);
    }
}