package com.example.cashin.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class clientServer extends Service {
    public clientServer() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
