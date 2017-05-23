package com.example.sikanla.myapplication;

import android.support.media.ExifInterface;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by Sikanla on 23/05/2017.
 */

public class ExifMethod {
    public static void copyExifData(String file1, String file2){
        try {
            ExifInterface file1Exif = new ExifInterface(file1);
            ExifInterface file2Exif = new ExifInterface(file2);

            String dateTime = file1Exif.getAttribute(ExifInterface.TAG_DATETIME);
            String exposureTime = file1Exif.getAttribute(ExifInterface.TAG_EXPOSURE_TIME);
            String flash = file1Exif.getAttribute(ExifInterface.TAG_FLASH);
            String focalLength = file1Exif.getAttribute(ExifInterface.TAG_FOCAL_LENGTH);
            String gpsAltitude = file1Exif.getAttribute(ExifInterface.TAG_GPS_ALTITUDE);
            String gpsAltitudeRef = file1Exif.getAttribute(ExifInterface.TAG_GPS_ALTITUDE_REF);
            String gpsDateStamp = file1Exif.getAttribute(ExifInterface.TAG_GPS_DATESTAMP);
            String gpsLatitude = file1Exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
            String gpsLatitudeRef = file1Exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
            String gpsLongitude = file1Exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
            String gpsLongitudeRef = file1Exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);
            String gpsProcessingMethod = file1Exif.getAttribute(ExifInterface.TAG_GPS_PROCESSING_METHOD);
            String gpsTimestamp = file1Exif.getAttribute(ExifInterface.TAG_GPS_TIMESTAMP);
            Integer imageLength = file1Exif.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, 0);
            Integer imageWidth = file1Exif.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, 0);
            String make = file1Exif.getAttribute(ExifInterface.TAG_MAKE);
            String model = file1Exif.getAttribute(ExifInterface.TAG_MODEL);
            Integer orientation = file1Exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            Integer whiteBalance = file1Exif.getAttributeInt(ExifInterface.TAG_WHITE_BALANCE, 0);


            file2Exif.setAttribute(ExifInterface.TAG_ORIENTATION, orientation.toString());
            file2Exif.setAttribute(ExifInterface.TAG_DATETIME, dateTime);
            file2Exif.setAttribute(ExifInterface.TAG_EXPOSURE_TIME, exposureTime);
            file2Exif.setAttribute(ExifInterface.TAG_FLASH, flash);
            file2Exif.setAttribute(ExifInterface.TAG_FOCAL_LENGTH, focalLength);
            file2Exif.setAttribute(ExifInterface.TAG_GPS_ALTITUDE, gpsAltitude);
            file2Exif.setAttribute(ExifInterface.TAG_GPS_ALTITUDE_REF, gpsAltitudeRef);
            file2Exif.setAttribute(ExifInterface.TAG_GPS_DATESTAMP, gpsDateStamp);
            file2Exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, gpsLatitude);
            file2Exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, gpsLatitudeRef);
            file2Exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, gpsLongitude);
            file2Exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, gpsLongitudeRef);
            file2Exif.setAttribute(ExifInterface.TAG_GPS_PROCESSING_METHOD, gpsProcessingMethod);
            file2Exif.setAttribute(ExifInterface.TAG_GPS_TIMESTAMP, gpsTimestamp);
            file2Exif.setAttribute(ExifInterface.TAG_IMAGE_LENGTH, imageLength.toString());
            file2Exif.setAttribute(ExifInterface.TAG_IMAGE_WIDTH, imageWidth.toString());
            file2Exif.setAttribute(ExifInterface.TAG_MAKE, make);
            file2Exif.setAttribute(ExifInterface.TAG_MODEL, model);
            file2Exif.setAttribute(ExifInterface.TAG_WHITE_BALANCE, whiteBalance.toString());
            file2Exif.saveAttributes();
        }
        catch (FileNotFoundException io) {}
        catch (IOException io) {}
        catch (NullPointerException np){}
    }
}
