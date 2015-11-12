import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Pipe implements Runnable {

	InputStream inputStream;
	OutputStream outputStream;

	public Pipe(InputStream inputStream, OutputStream outputStream) {
		this.inputStream = inputStream;
		this.outputStream = outputStream;

	}
	public void run() {
		try {
			byte[] buffer = new byte[1024];
			int len;
			len = inputStream.read(buffer);
			while (len != -1) {
				BufferedOutputStream bos = new BufferedOutputStream(
						outputStream);
				bos.write(buffer, 0, len);
				bos.flush();
				len = inputStream.read(buffer);
				if (Thread.interrupted()) {
					throw new InterruptedException();
				}
			}
		} catch (IOException e) {
			return;
		} catch (InterruptedException e) {
			return;
		} finally {
			try {
				this.inputStream.close();
			} catch (IOException e) {
			}
			try {
				this.outputStream.close();
			} catch (IOException e) {
			}
		}
	}

}