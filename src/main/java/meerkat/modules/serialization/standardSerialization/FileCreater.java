package meerkat.modules.serialization.standardSerialization;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

public class FileCreater {

	private final static int bufferSize = 10 * 1024 * 1024;

	public void createFile(File file, long size,
			ReadableByteChannel inputChannel) throws IOException {
		
		int counter = 0;
		int x= 0;
		do {
			ByteBuffer buf = ByteBuffer.allocate((int) Math.min(size, bufferSize));

			int bytesRead = inputChannel.read(buf);
			byte[] bytes = buf.array();

			RandomAccessFile f = new RandomAccessFile(file, "rw");
			FileChannel ch = f.getChannel();

			MappedByteBuffer mb = ch.map(FileChannel.MapMode.READ_WRITE, counter,
					size);

			mb.put(bytes);
			mb.load();
			
			size -= bytesRead;
			counter += bytesRead;
			

		} while (size > 0);

	}
}
