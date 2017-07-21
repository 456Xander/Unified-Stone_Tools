package at.xander.unifiedstone;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class LineIterator implements Iterable<String> {
	private BufferedReader reader;

	public LineIterator(BufferedReader reader) {
		this.reader = reader;
	}

	public Iterator<String> iterator() {
		return new Iterator<String>() {
			private String last = null;

			public boolean hasNext() {
				if (this.last == null) {
					try {
						this.last = LineIterator.this.reader.readLine();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				return this.last != null;
			}

			public String next() {
				if (hasNext()) {
					String tmp = this.last;
					this.last = null;
					return tmp;
				}
				throw new NoSuchElementException("No more Elements are in this Iterator");
			}
		};
	}

	public void close() {
		try {
			this.reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
