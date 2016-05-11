import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Created by Gye Hyeon Park on 2016-03-26.
 */

public class Server
{
    public static void main(String[] args)
    {
        JFrame frame = new JFrame("pH");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(screenSize.width/3, screenSize.height/3);
        frame.setResizable(false);
        Dimension frameSize = frame.getSize();
        frame.setLocation((screenSize.width - frameSize.width), (screenSize.height - frameSize.height));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Font font = new Font("Sans Serif", Font.ITALIC, 30);

        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new GridLayout(2, 1, 0, 2));
        labelPanel.setBackground(Color.WHITE);
        labelPanel.setOpaque(true);

        JLabel ipLabel = new JLabel(" IP ");
        ipLabel.setForeground(Color.WHITE);
        ipLabel.setBackground(Color.CYAN);
        ipLabel.setOpaque(true);
        ipLabel.setFont(font);
        ipLabel.setHorizontalAlignment(SwingConstants.CENTER);
        labelPanel.add(ipLabel);

        JLabel portLabel = new JLabel(" Port ");
        portLabel.setForeground(Color.WHITE);
        portLabel.setBackground(Color.RED);
        portLabel.setOpaque(true);
        portLabel.setFont(font);
        portLabel.setHorizontalAlignment(SwingConstants.CENTER);
        labelPanel.add(portLabel);

        frame.add(labelPanel, BorderLayout.WEST);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new GridLayout(2, 1, 0, 2));
        textPanel.setBackground(Color.WHITE);
        textPanel.setOpaque(true);

        JLabel ipAddress = new JLabel(getLocalIp());
        ipAddress.setOpaque(true);
        ipAddress.setFont(font);
        ipAddress.setHorizontalAlignment(SwingConstants.CENTER);
        textPanel.add(ipAddress);

        JLabel portNum = new JLabel("7777");
        portNum.setOpaque(true);
        portNum.setFont(font);
        portNum.setHorizontalAlignment(SwingConstants.CENTER);
        textPanel.add(portNum);

        frame.add(textPanel);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(3, 1, 0, 2));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setOpaque(true);

        JLabel isConnected = new JLabel("Ready");
        isConnected.setFont(font);
        isConnected.setForeground(Color.YELLOW);
        isConnected.setBackground(Color.DARK_GRAY);
        isConnected.setOpaque(true);
        isConnected.setHorizontalAlignment(SwingConstants.CENTER);
        bottomPanel.add(isConnected);

        JButton send = new JButton("IP Broadcasting");
        send.setOpaque(true);
        send.setFont(font);
        send.setHorizontalAlignment(SwingConstants.CENTER);
        bottomPanel.add(send);
        try
        {
            send.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    try
                    {
                        String ip = getLocalIp();
                        StringTokenizer strtok = new StringTokenizer(ip, ".");
                        new DatagramSocket().send(new DatagramPacket(ip.getBytes(), ip.getBytes().length, InetAddress.getByName("255.255.255.255"), 7777));
                        String gateway = strtok.nextToken();
                        new DatagramSocket().send(new DatagramPacket(ip.getBytes(), ip.getBytes().length, InetAddress.getByName(gateway + ".255.255.255"), 7777));
                        gateway += "." + strtok.nextToken();
                        new DatagramSocket().send(new DatagramPacket(ip.getBytes(), ip.getBytes().length, InetAddress.getByName(gateway + ".255.255"), 7777));
                        gateway += "." + strtok.nextToken();
                        new DatagramSocket().send(new DatagramPacket(ip.getBytes(), ip.getBytes().length, InetAddress.getByName(gateway + ".255"), 7777));
                    }
                    catch(IOException ignored){}
                }
            });
        }
        catch(Exception ignored){}

        JLabel description = new JLabel("Microsoft PowerPoint에서만 동작합니다.");
        description.setFont(new Font("Sans Serif", Font.BOLD, 20));
        description.setForeground(Color.RED);
        description.setOpaque(true);
        description.setHorizontalAlignment(SwingConstants.CENTER);
        bottomPanel.add(description);

        frame.add(bottomPanel, BorderLayout.SOUTH);

        frame.setVisible(true);

        new Connection(Integer.parseInt(portNum.getText()), isConnected);
    }

    // 현재 시스템의 모든 네트워크 인터페이스x를 읽어와서 loopback장치인지 유무선 여부를 확인하여 실제 사용중인 인터페이스의 IP 주소 반환
    public static String getLocalIp()
    {
        try
        {
            // 유선
            Enumeration<NetworkInterface> networks = NetworkInterface.getNetworkInterfaces();
            while(networks.hasMoreElements())
            {
                NetworkInterface network = networks.nextElement();
                Enumeration<InetAddress> inetAddresses = network.getInetAddresses();
                while(inetAddresses.hasMoreElements())
                {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress() && !inetAddress.getHostAddress().contains(":") && !inetAddress.isSiteLocalAddress())
                        return inetAddress.getHostAddress();
                }
            }
            // 무선
            networks = NetworkInterface.getNetworkInterfaces();
            while(networks.hasMoreElements())
            {
                NetworkInterface network = networks.nextElement();
                Enumeration<InetAddress> inetAddresses = network.getInetAddresses();
                while(inetAddresses.hasMoreElements())
                {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress() && !inetAddress.getHostAddress().contains(":") && inetAddress.isSiteLocalAddress())
                        return inetAddress.getHostAddress();
                }
            }
        }
        catch(SocketException ignored){}
        return null;
    }
}