package maniananana.chm.locationPoint;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class Storage {

    private  static LocationPointRepository locationPointRepository = new LocationPointRepository();
    public static LocationPointRepository getLocationPointRepository() {
        return locationPointRepository;
    }

}
