/**
 * Copyright (c) 2004-2005, Regents of the University of California
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * Neither the name of the University of California, Los Angeles nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package avrora.avrora.sim.platform.sensors;

import avrora.avrora.sim.mcu.ADC;
import avrora.avrora.sim.mcu.AtmelMicrocontroller;

/**
 * The <code>AccelSensor</code> class implements a accelerometer like that
 * present on the Mica2 MTS300 sensorboard.
 *
 * @author Ben L. Titzer
 * @author Zainul M. Charbiwala
 * @author Daniel Minder
 */
public class AccelSensor extends Sensor
{

    protected final AccelSensorPower asp;
    protected ADC adcDevice;


    public AccelSensor(AtmelMicrocontroller m, int adcChannel,
            AccelSensorPower asp)
    {
        adcDevice = (ADC) m.getDevice("adc");
        adcDevice.connectADCInput(new ADCInput(), adcChannel);
        this.asp = asp;
    }

    class ADCInput implements ADC.ADCInput
    {
        public float getVoltage()
        {
            if (data == null)
                return ADC.GND_LEVEL;
            if (!asp.isOn())
                return ADC.GND_LEVEL;
            int read = data.reading();
            // scale the reading back to a voltage.
            return adcDevice.getVoltageRef() * ((float) read) / 0x3ff;
        }
    }

}
