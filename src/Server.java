import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

/**
 * Created by Gye Hyeon Park on 2016-03-26.
 */

public class Server
{
    private static ConnectionThread thread;
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

        JTextField ipText = null;
        try{ipText = new JTextField(InetAddress.getLocalHost().getHostAddress()); }
        catch(UnknownHostException ignored){}
        ipText.setFont(font);
        ipText.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(ipText);

        JLabel portLabel = new JLabel("Port");
        portLabel.setBackground(Color.RED);
        portLabel.setOpaque(true);
        portLabel.setFont(font);
        portLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(portLabel);

        JTextField portText = new JTextField("7777");
        portText.setFont(font);
        portText.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(portText);

        frame.add(panel);
        JButton button = new JButton("Connect");
        button.setFont(font);
        button.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(button.getText().equals("Connect"))
                {
                    thread = new ConnectionThread(Integer.parseInt(portText.getText()));
                    thread.start();
                    button.setText("Disconnect");
                }
                else
                {
                    thread.kill();
                    button.setText("Connect");
                }
            }
        });
        frame.add(button, BorderLayout.SOUTH);

        frame.setVisible(true);
    }
}

class ConnectionThread extends Thread
{
    private ServerSocket server;
    private BufferedReader reader;

    public ConnectionThread(int portNum)
    {
        try{server = new ServerSocket(portNum);}
        catch(IOException ignored){}
    }

    @Override
    public void run()
    {
        try
        {
            Socket socket = server.accept();
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while(true)
            {
                String msg = reader.readLine();
                System.out.println(msg);
                Robot robot = new Robot();
                if(msg == null)
                {
                    System.out.println("null");
                    break;
                }
                switch(msg)
                {
                    case "F5":
                        robot.keyPress(KeyEvent.VK_F5);
                        break;
                    case "Enter":
                        robot.keyPress(KeyEvent.VK_ENTER);
                        break;
                    case "BackSpace":
                        robot.keyPress(KeyEvent.VK_BACK_SPACE);
                        break;
                }
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

    public void kill()
    {
        try
        {
            reader = null;
//            reader.close();
            server.close();
        }
        catch(Exception ignored){}
    }
}