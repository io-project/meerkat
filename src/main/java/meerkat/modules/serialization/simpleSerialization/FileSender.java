package meerkat.modules.serialization.simpleSerialization;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;

public class FileSender {
	private static final int SIZE = 1024 * 100; // wielkość bufferu przy
												// mapowaniu pliku
	private byte[] buffer = new byte[SIZE];

	public void sendFile(File file, WritableByteChannel outputChannel) throws IOException {
		FileInputStream f = new FileInputStream(file);
		FileChannel ch = f.getChannel();

		long red = 0L;
		
		do {
			
			long read = Math.min(Integer.MAX_VALUE, ch.size() - red);
			MappedByteBuffer mb = ch.map(FileChannel.MapMode.READ_ONLY, red,
					read);
			
			
			int nGet;
			
			while (mb.hasRemaining()) {
				
				nGet = Math.min(mb.remaining(), SIZE);
				mb.get(buffer, 0, nGet);
				

				ByteBuffer buf = ByteBuffer.allocate(nGet+1);
				buf.clear();
				buf.put(buffer);

				buf.flip();
				
				while (buf.hasRemaining()) {
					outputChannel.write(buf);
				}
				
			}
			
			red += read;
			
		} while (red < ch.size());
	}
}
