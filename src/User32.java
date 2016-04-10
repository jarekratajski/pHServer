import com.sun.jna.Native;
import com.sun.jna.PointerType;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.win32.StdCallLibrary;

/**
 * Created by Gye Hyeon Park on 2016-04-10.
 */
public interface User32 extends StdCallLibrary
{
    User32 INSTANCE = (User32) Native.loadLibrary("user32", User32.class);
    WinDef.HWND GetForegroundWindow();
    int GetWindowTextA(PointerType hWnd, byte[] lpString, int nMaxCount);
}