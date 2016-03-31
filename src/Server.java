import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
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
        JFrame frame = new JFrame("pH Server");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(screenSize.width/4, screenSize.height/4);
        frame.setResizable(false);
        Dimension frameSize = frame.getSize();
        frame.setLocation((screenSize.width - frameSize.width)/2, (screenSize.height - frameSize.height)/2);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 2));
        Font font = new Font("Sans Serif", Font.ITALIC, 40);

        JLabel ipLabel = new JLabel("IP");
        ipLabel.setBackground(Color.CYAN);
        ipLabel.setOpaque(true);
        ipLabel.setFont(font);
        ipLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(ipLabel);

        JLabel ipAddress = new JLabel(getLocalIp());
        ipAddress.setFont(font);
        ipAddress.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(ipAddress);

        JLabel portLabel = new JLabel("Port");
        portLabel.setBackground(Color.RED);
        portLabel.setOpaque(true);
        portLabel.setFont(font);
        portLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(portLabel);

        JLabel portNum = new JLabel("7777");
        portNum.setFont(font);
        portNum.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(portNum);

        frame.add(panel);

        JLabel isConnected = new JLabel("Ready");
        isConnected.setFont(font);
        isConnected.setForeground(Color.WHITE);
        isConnected.setBackground(Color.DARK_GRAY);
        isConnected.setOpaque(true);
        isConnected.setHorizontalAlignment(SwingConstants.CENTER);
        frame.add(isConnected, BorderLayout.SOUTH);

        frame.setVisible(true);

        new Connection(Integer.parseInt(portNum.getText()), isConnected);
    }

    // 현재 시스템의 모든 네트워크 인터페이스를 읽어와서 loopback장치인지 랜선에 연결된 장치인지 여부를 확인하여 실제 사용중인 인터페이스의 IP 주소 반환
    public static String getLocalIp()
    {
        try
        {
            Enumeration<NetworkInterface> networks = NetworkInterface.getNetworkInterfaces();
            while(networks.hasMoreElements())
            {
                NetworkInterface network = networks.nextElement();
                Enumeration<InetAddress> inetAddresses = network.getInetAddresses();
                while(inetAddresses.hasMoreElements())
                {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress() && inetAddress.isSiteLocalAddress())
                        return inetAddress.getHostAddress().toString();
                }
            }
        }
        catch(SocketException ignored){}
        return null;
    }
}

class Connection
{
    private ServerSocket server;
    private BufferedReader reader;

    public Connection(int portNum, JLabel isConnected)
    {
        try
        {
            server = new ServerSocket(portNum);
            while(true)
            {
                Socket socket = server.accept();
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String msg = reader.readLine();
                StringTokenizer strtok = new StringTokenizer(msg, " \0");
                msg = strtok.nextToken();
                System.out.println(msg);
                Robot robot = new Robot();
                if(msg == null)
                    break;
                switch(msg)
                {
                    case "START":
                        isConnected.setText("Connected");
                        break;
                    case "F5":
                        robot.keyPress(KeyEvent.VK_F5);
                        break;
                    case "ENTER":
                        robot.keyPress(KeyEvent.VK_ENTER);
                        break;
                    case "BACK_SPACE":
                        robot.keyPress(KeyEvent.VK_BACK_SPACE);
                        break;
                    case "ESC":
                        robot.keyPress(KeyEvent.VK_ESCAPE);
                        break;
                }
                reader.close();
                socket.close();
            }
            reader.close();
        }
        catch(Exception ignored){}
        finally
        {
            try{server.close();}
            catch(Exception ignored){}
        }
    }
}