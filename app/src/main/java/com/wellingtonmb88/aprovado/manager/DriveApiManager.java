package com.wellingtonmb88.aprovado.manager;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataBuffer;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;
import com.wellingtonmb88.aprovado.AppApplication;
import com.wellingtonmb88.aprovado.activity.MainActivity;
import com.wellingtonmb88.aprovado.preferences.DriveApiPreferences;
import com.wellingtonmb88.aprovado.utils.AprovadoLogger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DriveApiManager implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {

    private static final String FILE_TITLE = "appconfig.json";
    private static final String FILE_MIME_TYPE = "application/json";
    private static DriveApiManager sDriveApiManager = null;
    private String mJsonData;
    private GoogleApiClient mDriveApiClient;
    final private ResultCallback<DriveApi.DriveContentsResult> driveContentsCallback =
            new ResultCallback<DriveApi.DriveContentsResult>() {
                @Override
                public void onResult(DriveApi.DriveContentsResult result) {
                    if (!result.getStatus().isSuccess()) {
                        AprovadoLogger.e("Error while trying to create new file contents");
                        return;
                    }

                    MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                            .setTitle(FILE_TITLE)
                            .setMimeType(FILE_MIME_TYPE)
                            .setDescription(mJsonData)
                            .build();

                    Drive.DriveApi.getAppFolder(mDriveApiClient)
                            .createFile(mDriveApiClient, changeSet, result.getDriveContents())
                            .setResultCallback(fileCallback);
                }
            };
    private DriveConnectionCallback mDriveConnectionCallback;
    private GetDataResultCallback mGetDataResultCallback;
    final private ResultCallback<DriveFolder.DriveFileResult> fileCallback = new
            ResultCallback<DriveFolder.DriveFileResult>() {
                @Override
                public void onResult(DriveFolder.DriveFileResult result) {
                    if (!result.getStatus().isSuccess()) {
                        AprovadoLogger.e("Error while trying to create new file contents");
                        return;
                    }
                    if (mGetDataResultCallback != null) {
                        mGetDataResultCallback.onDataSyncFinished();
                    }
                    AprovadoLogger.i("Created a file in App Folder: " + result.getDriveFile().getDriveId());
                }
            };
    private Activity mActivity;

    private DriveApiManager() {
    }

    public static synchronized DriveApiManager getInstance() {
        if (sDriveApiManager == null) {
            sDriveApiManager = new DriveApiManager();
        }
        return sDriveApiManager;
    }

    public void setConnectionCallback(DriveConnectionCallback callback) {
        mDriveConnectionCallback = callback;
    }

    public void setDataResultCallback(GetDataResultCallback callback) {
        mGetDataResultCallback = callback;
    }

    public void setActivity(Activity activity) {
        mActivity = activity;
    }

    public void connectGoogleDriveApi() {
        final Context context = AppApplication.getAppContext();
        if (context != null) {
            if (mDriveApiClient == null) {
                mDriveApiClient = new GoogleApiClient.Builder(context)
                        .addApi(Drive.API)
                        .addScope(Drive.SCOPE_APPFOLDER) // required for App Folder sample
                        .addOnConnectionFailedListener(this)
                        .addConnectionCallbacks(this)
                        .build();
            }

            if (!mDriveApiClient.isConnected()) {
                mDriveApiClient.connect();
            } else {
                DriveApiPreferences.setDriveApiConnected(true);
                DriveApiPreferences.setDriveApiConnectionDenied(false);

                if (mDriveConnectionCallback != null) {
                    mDriveConnectionCallback.onDriveApiConnected();
                }
            }
        }
    }

    public void disConnectGoogleDriveApi() {
        if (mDriveApiClient != null && mDriveApiClient.isConnected()) {
            mDriveApiClient.disconnect();
            mDriveApiClient = null;
        }
    }

    public void saveDataToDrive(final String jsonData) {
        final Context context = AppApplication.getAppContext();
        if (context != null && mDriveApiClient != null && mDriveApiClient.isConnected()) {

            DriveApiPreferences.saveLastTimeUploaded();

            deleteAllDataAndCreateNew(true);
            mJsonData = jsonData;
        } else {
            connectionFailed();
        }
    }

    private void createNewData() {
        Drive.DriveApi.newDriveContents(mDriveApiClient)
                .setResultCallback(driveContentsCallback);
    }

    private void connectionFailed(){
        if (mDriveConnectionCallback != null) {
            mDriveConnectionCallback.onDriveApiConnectionFailed();
        }
        connectGoogleDriveApi();
    }

    public void getDataFromDrive() {

        if (mDriveApiClient != null && mDriveApiClient.isConnected()) {

            Drive.DriveApi.requestSync(mDriveApiClient).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(Status status) {

                    Query query = new Query.Builder().addFilter(Filters.and(
                            Filters.eq(SearchableField.MIME_TYPE, FILE_MIME_TYPE),
                            Filters.contains(SearchableField.TITLE, FILE_TITLE))).build();

                    Drive.DriveApi.getAppFolder(mDriveApiClient).queryChildren(mDriveApiClient, query)
                            .setResultCallback(new ResultCallback<DriveApi.MetadataBufferResult>() {
                                @Override
                                public void onResult(DriveApi.MetadataBufferResult metadataBufferResult) {
                                    if (!metadataBufferResult.getStatus().isSuccess()) {
                                        AprovadoLogger.e("Error while trying to query file contents");
                                        return;
                                    }

                                    MetadataBuffer metadataBuffer = metadataBufferResult.getMetadataBuffer();
                                    for (Metadata metadata : metadataBuffer) {
                                        String mimeType = metadata.getMimeType();
                                        String description = metadata.getDescription();

                                        AprovadoLogger.i(" metadataBufferResult: " + " mimeType: " + mimeType
                                                + "description: " + description);

                                        if (mGetDataResultCallback != null) {
                                            mGetDataResultCallback.onResultCallback(description);
                                        }

                                    }
                                    int count = metadataBuffer.getCount();
                                    if(count < 1){
                                        if (mGetDataResultCallback != null) {
                                            mGetDataResultCallback.onDataSyncFinished();
                                        }
                                    }
                                    metadataBuffer.release();
                                    metadataBufferResult.release();
                                }
                            });
                }
            });
        } else {
            connectionFailed();
        }
    }

    public void deleteAllDataAndCreateNew(final boolean shouldCreateNew) {

        if (mDriveApiClient != null && mDriveApiClient.isConnected()) {

            Drive.DriveApi.requestSync(mDriveApiClient).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(Status status) {

                    Query query = new Query.Builder().addFilter(Filters.and(
                            Filters.eq(SearchableField.MIME_TYPE, FILE_MIME_TYPE),
                            Filters.contains(SearchableField.TITLE, FILE_TITLE))).build();

                    Drive.DriveApi.query(mDriveApiClient, query)
                            .setResultCallback(new ResultCallback<DriveApi.MetadataBufferResult>() {
                                @Override
                                public void onResult(final DriveApi.MetadataBufferResult metadataBufferResult) {

                                    ExecutorService executorService = Executors.newSingleThreadExecutor();

                                    executorService.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (!metadataBufferResult.getStatus().isSuccess()) {
                                                AprovadoLogger.e("Error while trying to query file contents");
                                                return;
                                            }

                                            MetadataBuffer metadataBuffer = metadataBufferResult.getMetadataBuffer();
                                            for (Metadata metadata : metadataBuffer) {

                                                DriveId fileId = metadata.getDriveId();
                                                DriveFile driveFile = fileId.asDriveFile();

                                                // Call to delete file.
                                                driveFile.delete(mDriveApiClient).await();
                                                AprovadoLogger.i("Deleted a file in App Folder: " + fileId);

                                            }
                                            metadataBuffer.release();
                                            metadataBufferResult.release();

                                            if (shouldCreateNew) {
                                                createNewData();
                                            } else {
                                                if (mGetDataResultCallback != null) {
                                                    mGetDataResultCallback.onDataSyncFinished();
                                                }
                                            }
                                        }
                                    });
                                }
                            });

                }
            });
        } else {
            connectionFailed();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (mDriveConnectionCallback != null) {
            mDriveConnectionCallback.onDriveApiConnected();
        }
        AprovadoLogger.i("Drive API onDriveApiConnected ");
    }

    @Override
    public void onConnectionSuspended(int i) {
        AprovadoLogger.i("Drive API onConnectionSuspended ");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (mActivity != null) {
            if (!connectionResult.hasResolution()) {
                // show the localized error dialog.
                GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), mActivity, 0).show();
                return;
            }

            try {
                connectionResult.startResolutionForResult(mActivity, MainActivity.REQUEST_CODE_DRIVE_API);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        }
    }

    public interface GetDataResultCallback {
        void onResultCallback(String result);

        void onDataSyncFinished();
    }

    public interface DriveConnectionCallback {
        void onDriveApiConnected();

        void onDriveApiConnectionFailed();
    }
}
