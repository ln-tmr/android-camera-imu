package de.markusfisch.android.cameraviewdemo.activity;

import de.markusfisch.android.cameraview.widget.CameraView;

import de.markusfisch.android.cameraviewdemo.R;

import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
	private static final int REQUEST_CAMERA = 1;

	private static boolean frontFacing = false;

	private CameraView cameraView;

	@Override
	public void onRequestPermissionsResult(
			int requestCode,
			@NonNull String permissions[],
			@NonNull int grantResults[]) {
		switch (requestCode) {
			case REQUEST_CAMERA:
				if (grantResults.length > 0 &&
						grantResults[0] != PackageManager.PERMISSION_GRANTED) {
					Toast.makeText(
							this,
							R.string.error_camera,
							Toast.LENGTH_SHORT).show();
					finish();
				}
				break;
			default:
				break;
		}
	}

	@Override
	protected void onCreate(Bundle state) {
		super.onCreate(state);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

		checkPermissions();

		cameraView = new CameraView(this);
		cameraView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				invertCamera();
			}
		});

		setContentView(cameraView);
	}

	@Override
	public void onResume() {
		super.onResume();
		cameraView.openAsync(CameraView.findCameraId(getFacing()));
	}

	@Override
	public void onPause() {
		super.onPause();
		cameraView.close();
	}

	private void checkPermissions() {
		String permission = android.Manifest.permission.CAMERA;

		if (ContextCompat.checkSelfPermission(this, permission) !=
				PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(
					this,
					new String[]{permission},
					REQUEST_CAMERA);
		}
	}

	private void invertCamera() {
		frontFacing ^= true;
		cameraView.close();
		cameraView.openAsync(CameraView.findCameraId(getFacing()));
	}

	private int getFacing() {
		return frontFacing ?
				Camera.CameraInfo.CAMERA_FACING_FRONT :
				Camera.CameraInfo.CAMERA_FACING_BACK;
	}
}
