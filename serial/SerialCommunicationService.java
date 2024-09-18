package com.example.demo.serial;

import com.fazecast.jSerialComm.SerialPort;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SerialCommunicationService {

    private SerialPort serialPort;

    private static final Pattern DEFAULT_TEMPERATURE_PATTERN = Pattern.compile("Temp:\\s*(\\d+\\.\\d+)");
    private static final String JSON_TEMPERATURE_KEY = "temperature";

    // Lista tillgängliga seriella portar
    public void listAvailablePorts() {
        SerialPort[] ports = SerialPort.getCommPorts();
        System.out.println("Tillgängliga portar:");
        for (SerialPort port : ports) {
            System.out.println(port.getSystemPortName() + ": " + port.getDescriptivePortName());
        }
    }

    // Öppna seriell port för kommunikation
    // Öppna seriell port för kommunikation
    public void openSerialPort() {
        listAvailablePorts(); // Lista alla portar innan vi öppnar en

        serialPort = SerialPort.getCommPort("COM4"); // Ändra COM-port till rätt port för ditt system
        serialPort.setComPortParameters(9600, 8, 1, 0);  // Baudrate 9600, 8 databitar, 1 stoppbit, ingen paritet
        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 1000, 1000);  // Timeout på 1 sekund

        if (serialPort.openPort()) {
            System.out.println("Porten är öppen och redo att ta emot data på " + serialPort.getSystemPortName());

            try {
                Thread.sleep(1000); // Vänta 1 sekund innan du börjar läsa data
            } catch (InterruptedException e) {
                System.out.println("Fel vid väntan: " + e.getMessage());
            }

        } else {
            System.out.println("Kunde inte öppna porten " + serialPort.getSystemPortName());
            throw new RuntimeException("Kunde inte öppna porten. Kontrollera att porten inte används av ett annat program.");
        }
    }


    // Läs data från sensorn och logga den
    // Läs data från sensorn och logga den
    // Läs data från sensorn och logga den
    public String readSensorData() {
        StringBuilder sensorData = new StringBuilder();
        try {
            if (serialPort != null && serialPort.isOpen()) {
                System.out.println("Porten " + serialPort.getSystemPortName() + " är öppen, läser data...");

                byte[] readBuffer = new byte[1024];  // Buffert för att läsa inkommande data
                int numRead;
                long startTime = System.currentTimeMillis();
                boolean isComplete = false;

                // Läs kontinuerligt tills vi har fått hela meddelandet eller timeout på 5 sekunder
                while (System.currentTimeMillis() - startTime < 5000 && !isComplete) {
                    numRead = serialPort.readBytes(readBuffer, readBuffer.length);
                    if (numRead > 0) {
                        sensorData.append(new String(readBuffer, 0, numRead));
                        System.out.println("Rådata mottagen från seriell port: " + sensorData.toString());

                        // Kolla om vi har fått hela meddelandet (ny rad '\n')
                        if (sensorData.toString().contains("\n")) {
                            isComplete = true;
                        }
                    } else {
                        // Om inget läses, vänta en kort stund innan nästa försök
                        Thread.sleep(100);  // Vänta 100ms
                    }
                }

                // Logga den kompletta strängen
                System.out.println("Data mottagen från Arduino (som sträng): " + sensorData.toString());
            } else {
                System.out.println("Porten är inte öppen.");
                throw new RuntimeException("Porten är inte öppen när vi försöker läsa. Kontrollera om porten har stängts.");
            }
        } catch (Exception e) {
            System.out.println("Ett fel uppstod vid läsning av data: " + e.getMessage());
        }
        return sensorData.toString();
    }


    // Metod för att extrahera temperaturen från den mottagna datan
    public double extractTemperature(String sensorData) {
        try {
            // Försök med det standard regex-uttrycket
            Matcher matcher = DEFAULT_TEMPERATURE_PATTERN.matcher(sensorData);
            if (matcher.find()) {
                return Double.parseDouble(matcher.group(1));
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Ogiltigt temperaturvärde i data: " + sensorData, e);
        }

        throw new IllegalArgumentException("Kunde inte extrahera temperatur från data: " + sensorData);
    }
    // Stäng seriell port när vi är klara
    public void closeSerialPort() {
        if (serialPort != null && serialPort.isOpen()) {
            serialPort.closePort();
            System.out.println("Seriella porten " + serialPort.getSystemPortName() + " är stängd.");
        } else {
            System.out.println("Porten var redan stängd.");
        }
    }
}
