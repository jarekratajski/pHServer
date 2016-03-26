import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Gye Hyeon Park on 2016-03-26.
 */

public class Server
{
    public static void main(String[] args)
    {
        ServerSocket server = null;
        try
        {
            server = new ServerSocket(7777);
            Socket socket = server.accept();
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while(true)
            {
                String msg = reader.readLine();
                System.out.println(msg);
                Robot robot = new Robot();
                if(msg == null)
                    break;
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
            catch(IOException ignored){}
        }
    }
}