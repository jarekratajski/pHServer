import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;
import java.net.*;
import java.util.StringTokenizer;

/**
 * Created by Gye Hyeon Park on 2016-04-07.
 */
public class Connection
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
                        isConnected.setForeground(Color.GREEN);
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
