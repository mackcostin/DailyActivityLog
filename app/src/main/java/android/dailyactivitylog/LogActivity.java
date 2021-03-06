package android.dailyactivitylog;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MenuItem;
import java.util.UUID;

import static android.dailyactivitylog.GoogleMapFragment.MY_PERMISSIONS_REQUEST_LOCATION;

public class LogActivity extends SingleFragmentActivity {


    private static final String EXTRA_LOG_ID = "android.dailyactivitylog.log_id";

    @Override
    protected Fragment createFragment() {
        android.util.Log.d("createFragment", "LogActivity");
        UUID logID = (UUID) getIntent().getSerializableExtra(EXTRA_LOG_ID);

        checkLocationPermission();
        return LogFragment.newInstance(logID);
    }


    public static Intent newIntent(Context packageContext, UUID logId) {
        Intent intent = new Intent(packageContext, LogActivity.class);
        intent.putExtra(EXTRA_LOG_ID, logId);
        return intent;
    }


    @Override
    public void onBackPressed(){
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            Log.i("MainActivity", "popping backstack");
            fm.popBackStack();
        } else {
            Log.i("MainActivity", "nothing on backstack, calling super");
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Prompts the user for Location Services permission to be displayed
     * at the start of the activity.
     */
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(LogActivity.this , Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(LogActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(LogActivity.this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(LogActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(LogActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }
}
