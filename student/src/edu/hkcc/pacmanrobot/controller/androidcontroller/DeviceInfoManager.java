package edu.hkcc.pacmanrobot.controller.androidcontroller;

import edu.hkcc.pacmanrobot.utils.Config;
import edu.hkcc.pacmanrobot.utils.message.udpmessage.Decoder;
import edu.hkcc.pacmanrobot.utils.message.udpmessage.Encoder;
import edu.hkcc.pacmanrobot.utils.message.udpmessage.UDPMessengerSingleton;
import edu.hkcc.pacmanrobot.utils.network.NetworkUtils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Random;

/**
 * Created by beenotung on 4/29/15.
 */
public class DeviceInfoManager extends Thread {
    Random random = new Random(System.currentTimeMillis());
    boolean running = false;
    String name = "android controller-" + random.nextInt(100);
    String ip;
    final byte[] macAddress;

    public DeviceInfoManager() throws SocketException {
        InetAddress inetAddress = NetworkUtils.getOnlineInetAddress();
        this.macAddress = NetworkInterface.getByInetAddress(inetAddress).getHardwareAddress();
    }

    private InputThread inputThread = new InputThread();
    private OutputThread outputThread = new OutputThread();

    private class InputThread extends Thread {
        @Override
        public void run() {
            while (running) {
                try {
                    byte[] data = UDPMessengerSingleton.getInstance(new UDPMessengerSingleton.ReceiveActor() {
                        @Override
                        public void apply(String s) {
                            //no need
                        }
                    }).deviceInfoBytesDrawer.waitGetContent();
                    String newName = Decoder.getInstance().getDeviceName(macAddress, data);
                    if (newName != null)
                        name = newName;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(Config.CLIENT_REPORT_CYCLE_INTERVAL);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class OutputThread extends Thread {
        @Override
        public void run() {
            while (running) {
                try {
                    InetAddress inetAddress = NetworkUtils.getOnlineInetAddress();
                    ip = inetAddress.getHostAddress();
                    byte[] data = Encoder.getInstance().getDeviceInfo(macAddress, name, ip, (byte) 1, System.currentTimeMillis(), true);
                    UDPMessengerSingleton.getInstance().send(data, 0, data.length, Config.PORT_DEVICE_INFO);
                } catch (SocketException e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(Config.CLIENT_REPORT_CYCLE_INTERVAL);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void run() {
        running = true;
        inputThread.start();
        outputThread.start();
    }

    @Override
    public void start() {
        if (!running)
            super.start();
    }
}
