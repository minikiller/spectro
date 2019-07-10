package com.glyb;

import com.oceanoptics.omnidriver.api.wrapper.Wrapper;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.JOptionPane;

/*
 * ThreadedSpectrometerTest.java
 *
 * Created on September 21, 2007, 11:25 AM
 * Updated on 5/1/2008 to use the new Wrapper API
 */
public class ThreadedSpectrometerTest
{
    public final static String APP_TITLE = "Java Threading Sample";

    private Vector  acquisitionThreads; // collection of AcquisitionThread objects
    private Wrapper wrapper;
    
    public ThreadedSpectrometerTest()
    {
        acquisitionThreads = new Vector(); // collection of AcquisitionThread objects
    }
    
    public static void main(String args[])
    {
        ThreadedSpectrometerTest myself = new ThreadedSpectrometerTest();
        myself.run(args);
    }
    
    private void run(String args[])
    {
        AcquisitionThread   acquisitionThread;
        int     externalTriggerMode;
        int     numberOfSpectrometersFound;
        int     spectrometerIndex;
        
        System.out.println("ThreadedSpectrometerTest beginning");
        System.out.println();
        
        if (args.length != 1)
        {
            System.out.println("No external trigger mode number was specified on the command line.");
            externalTriggerMode = 3;
        } else {
            externalTriggerMode = Integer.parseInt(args[0]);
        }
        System.out.println("Setting external trigger mode to mode " + externalTriggerMode);
        
        wrapper = new Wrapper();
        numberOfSpectrometersFound = wrapper.openAllSpectrometers();
        if (numberOfSpectrometersFound == 0)
        {
            System.out.println("No spectrometers were found. Exiting the application.");
            JOptionPane.showMessageDialog(null,"No spectrometers were found. Exiting the application.",APP_TITLE, JOptionPane.ERROR_MESSAGE);
            return;
        }
        System.out.println();
        System.out.println("Number of spectrometers found: " + numberOfSpectrometersFound);
        for (spectrometerIndex=0; spectrometerIndex<numberOfSpectrometersFound; ++spectrometerIndex)
        {
            System.out.println("Starting data acquisition for:  spectrometer type: " + wrapper.getName(spectrometerIndex) + 
                    " s/n: " + wrapper.getSerialNumber(spectrometerIndex) + 
                    " firmware: " + wrapper.getFirmwareVersion(spectrometerIndex));
            
            wrapper.setExternalTriggerMode(spectrometerIndex,externalTriggerMode); // set the external trigger mode
            acquisitionThread = new AcquisitionThread(wrapper,spectrometerIndex);
            synchronized(this)
            {
                acquisitionThreads.add(acquisitionThread);
                acquisitionThread.start();
            }
        }
        
        // For this simple example, we will just run for 10 seconds and then shut everything down
        try
        {
            Thread.sleep(10 * 1000); // units: milliseconds
            System.out.println("Shutting down all acquisitions");
            synchronized(this)
            {
                Iterator iterator = this.acquisitionThreads.iterator();
                while(iterator.hasNext())
                {
                    acquisitionThread = (AcquisitionThread)iterator.next();
                    acquisitionThread.stopAcquisition();
                    // Wait for this thread to indicate it has shut down
                    while(acquisitionThread.isTerminated() == false)
                    {
                        try
                        {
                            Thread.sleep(100);
                        } catch (InterruptedException ie) {
                            ie.printStackTrace();
                        }
                    }
                }
            }
        }
        catch(InterruptedException ie)
        {
            ie.printStackTrace();
        }
        
        System.out.println("ThreadedSpectrometerTest all done");
    }
}
