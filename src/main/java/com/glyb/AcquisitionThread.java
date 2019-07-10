package com.glyb;

import com.oceanoptics.omnidriver.api.wrapper.Wrapper;

public class AcquisitionThread extends Thread {
    boolean isTerminated; // set to false while we are actively acquiring spectra
    int spectrometerIndex; // indicates which spectrometer we are communicating with
    boolean terminateAcquisition; // when set to true, we stop acquiring spectra for this spectrometer
    Wrapper wrapper;

    /**
     * IMPORTANT: this thread must only access the spectrometer specified by spectrometerIndex.
     * Otherwise you may cause a race condition, resulting in unpredictable behaviour.
     *
     * @param wrapperObject
     * @param spectrometerIndexNumber
     */
    public AcquisitionThread(Wrapper wrapperObject, int spectrometerIndexNumber) {
        this.wrapper = wrapperObject;
        this.spectrometerIndex = spectrometerIndexNumber;
        this.terminateAcquisition = false; // no one has requested us to terminate yet
        this.isTerminated = true; // we are currently idle
    }

    public boolean isTerminated() {
        return this.isTerminated;
    }

    public void stopAcquisition() {
        this.terminateAcquisition = true;
    }

    @Override
    public void run() {
        double[] wavelengths, spectrumValues;

        this.isTerminated = false;
        while (this.terminateAcquisition == false) {
            // The following call will BLOCK until the spectrometer returns
            // the next spectrum.
//            spectrumValues = wrapper.getSpectrum(spectrometerIndex);
            wavelengths = wrapper.getWavelengths(0);
//            System.out.println("Received spectrum from spectrometer: " + wrapper.getSerialNumber(spectrometerIndex)
//                    + " pixel[100] = " + spectrumValues[100]);
            System.out.println("Received spectrum from spectrometer: " + wrapper.getSerialNumber(spectrometerIndex)
                    + " pixel[100] = " + wavelengths[100]);

            // At this point you could add logic to call a callback method for one or more classes
            // which have registered themselves as interested listeners for new spectra.
            // This is done with ordinary java logic.  There is nothing  unique to OmniDriver
            // at this point.
        }
        this.isTerminated = true;
    }
}
