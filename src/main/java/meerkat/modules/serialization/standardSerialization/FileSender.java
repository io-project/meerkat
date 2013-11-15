package meerkat.modules.serialization.standardSerialization;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;

public class FileSender {

	public void sendFile(File file, WritableByteChannel outputChannel) throws IOException {
		FileInputStream f = new FileInputStream(file);
		FileChannel ch = f.getChannel();

		long red = 0L;
		
		do {
			
			long read = Math.min(Integer.MAX_VALUE, ch.size() - red);
			MappedByteBuffer mb = ch.map(FileChannel.MapMode.READ_ONLY, red,
					read);
			
			
			while (mb.hasRemaining()) {
				
				outputChannel.write(mb);
				
			}
			
			red += read;
			
		} while (red < ch.size());
	}
}
