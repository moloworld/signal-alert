package com.example.alertworld; // Asegúrate de que el paquete sea correcto

import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {  // Asegúrate de que la clase esté bien definida

    // Aquí defines tus variables de instancia
    private Camera camera;
    private Camera.Parameters parameters;
    private boolean isFlashing = false;
    private Handler handler;
    private Runnable strobeRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicialización del botón y el handler
        Button button3 = findViewById(R.id.button3);
        handler = new Handler();

        // Configuración del listener para el botón
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFlashing) {
                    stopStrobe();
                } else {
                    startStrobe();
                }
            }
        });
    }

    private void startStrobe() {
        camera = Camera.open();
        parameters = camera.getParameters();

        strobeRunnable = new Runnable() {
            @Override
            public void run() {
                if (isFlashing) {
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    camera.setParameters(parameters);
                    camera.startPreview();
                } else {
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    camera.setParameters(parameters);
                    camera.stopPreview();
                }
                isFlashing = !isFlashing;

                handler.postDelayed(strobeRunnable, 500); // Cambia este valor para modificar la frecuencia
            }
        };

        isFlashing = true;
        handler.post(strobeRunnable);

        // Detener automáticamente después de 5 segundos
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                stopStrobe();
            }
        }, 5000); // 5000 milisegundos = 5 segundos
    }

    private void stopStrobe() {
        handler.removeCallbacks(strobeRunnable);
        if (camera != null) {
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(parameters);
            camera.stopPreview();
            camera.release();
            camera = null;
        }
        isFlashing = false;
    }
}

