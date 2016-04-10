import com.sun.jna.Native;
import com.sun.jna.PointerType;

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
                if(msg.equals("START"))
                {
                    isConnected.setText("Connected");
                    isConnected.setForeground(Color.GREEN);
                }

                byte[] windowText = new byte[512];
                PointerType hwnd = User32.INSTANCE.GetForegroundWindow();
                User32.INSTANCE.GetWindowTextA(hwnd, windowText, 512);
                // ppt focus되어 있는 경우만 동작
                if(Native.toString(windowText).contains("PowerPoint"))
                {
                    Robot robot = new Robot();
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
                }
                reader.close();
                socket.close();
            }
        }
        catch(Exception ignored){}
        finally
        {
            try{server.close();}
            catch(Exception ignored){}
        }
    }
}
