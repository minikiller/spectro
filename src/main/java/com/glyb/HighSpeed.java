package com.glyb;
import com.oceanoptics.highrestiming.HighResTimeStamp;
import com.oceanoptics.omnidriver.api.wrapper.Wrapper;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HighSpeed {
    Wrapper wrapper;

    /**
     * @param args the command line arguments (not used in this application)
     */
    public static void main(String[] args) {
        HighSpeed    myself;

        myself = new HighSpeed();
        myself.run(args);
    }

    public void run(String[] args) {

        int minimumIntegrationTime; // units: microseconds
        int numberOfSpectraRequested;
        int numberOfSpectrometers;
        int spectrometerIndex;
        int spectrumNumber;

        wrapper = new Wrapper();

        numberOfSpectrometers = wrapper.openAllSpectrometers();
        if (numberOfSpectrometers == -1) {
            System.out.println(wrapper.getLastException());
            System.out.println(wrapper.getLastExceptionStackTrace());
            System.out.println("Error occured while attempting to access spectrometers.  Exiting the application.");
            return;
        } else if (numberOfSpectrometers == 0) {
            System.out.println("No spectrometers found.  Exiting the application.");
            return;
        }

        spectrometerIndex = 0; // arbitrarily choose the first spectrometer we found
        System.out.println("Spectrometer type: " + wrapper.getName(spectrometerIndex));
        System.out.flush();

        numberOfSpectraRequested = 1000; // this is how may spectra we want to collect
        wrapper.highSpdAcq_AllocateBuffer(spectrometerIndex,numberOfSpectraRequested);

        minimumIntegrationTime = wrapper.getMinimumIntegrationTime(spectrometerIndex);
        wrapper.setIntegrationTime(spectrometerIndex,minimumIntegrationTime);

        System.out.println();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
        System.out.println("Begin acquisition: " + sdf.format(new Date()));
        long startTime = new Date().getTime(); // a count of milliseconds from some reference point

        // ACQUIRE THE SPECTRA
        wrapper.highSpdAcq_StartAcquisition(spectrometerIndex); // this method will not return until we have acquired all requested spectra

        long endTime = new Date().getTime(); // a count of milliseconds from some reference point
        float elapsedTimeSeconds = (float) ((float) (endTime - startTime) / 1000.0);
        float spectraPerSecond = (float)wrapper.highSpdAcq_GetNumberOfSpectraAcquired() / elapsedTimeSeconds;
        System.out.println("End acquisition:   " + sdf.format(new Date()));
        System.out.printf("Elapsed time: %4.2f seconds, number of spectra acquired: %d,  rate (spectra per second): %f\n",
                elapsedTimeSeconds,wrapper.highSpdAcq_GetNumberOfSpectraAcquired(),spectraPerSecond);
        System.out.println();

        // Extract the newly acquired spectra from the internal buffer area
        for (spectrumNumber=0; spectrumNumber<wrapper.highSpdAcq_GetNumberOfSpectraAcquired(); ++spectrumNumber) {
            HighResTimeStamp hrts = wrapper.highSpdAcq_GetTimeStamp(spectrumNumber); // this is optional
            //System.out.println(spectrumNumber + ". " + hrts.toString()); // display the exact time we obtained this spectrum
            double[] oneSpectrum; // this will contain the raw pixel values comprising one spectrum
            oneSpectrum = wrapper.highSpdAcq_GetSpectrum(spectrumNumber);
            // the oneSpectrum array now contains all the raw CCD pixel values for one spectrum
            // You might want to call wrapper.highSpdAcq_IsSaturated(spectrumNumber) at this point.
            // For example...
            //boolean weHaveAProblem = wrapper.highSpdAcq_IsSaturated(spectrumNumber);
        }

        System.out.println();
        System.out.println("Application terminating successfully.");
    }
}
