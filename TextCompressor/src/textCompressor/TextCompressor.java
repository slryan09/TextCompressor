package textCompressor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

public class TextCompressor {

	// Runs the program
	public static void main(String[] args) throws IOException {
		Scanner in = new Scanner(System.in);
		System.out.print("File name:  ");
		String fileName = in.next();
		in.close();
		processFile(fileName);
	}

	// Takes the file and determines whether it needs to be compressed or
	// decompressed
	public static void processFile(String fileName) throws IOException {
		File file = new File(fileName);
		Scanner scanner = new Scanner(file);
		if (scanner.hasNext()) {
			// If the first character of the file is not a 0, then it needs to
			// be compressed
			if (!scanner.hasNextInt()) {
				compress(file);
			} else { // the file needs to be decompressed
				decompress(file);
			}
		}
		scanner.close();
	}

	// Decompresses the file
	public static void decompress(File file) throws IOException {

		// -----set up FreqStack, String Builders, and BufferedReader-----//
		// The freqstack represents a list of the strings that are ordered
		// according to the MTF heuristic
		FrequencyStack freqstack = new FrequencyStack();
		// The s String will reprsent the final output string of the file
		StringBuilder s = new StringBuilder();
		// word is the current word being examined
		StringBuilder word = new StringBuilder();
		// number is the current position number being examined
		StringBuilder number = new StringBuilder();
		// in reads from the file character by character
		BufferedReader in = new BufferedReader(new FileReader(file));
		// -------------------------------------------------------------//

		// Throw away the initial 0 and first space of a decompression file
		int r = in.read();
		r = in.read();

		// Read the first character to begin the while loop
		r = in.read();
		// While there are still characters to be read...
		while (r > -1) {
			// ch is the character represented by the integer value r
			char ch = (char) r;
			// if ch is a letter, then we need to add it to the word string
			if (Character.isLetter(ch) || ch == '\'') {
				word.append(ch);
			} else if (Character.isDigit(ch)) {
				// System.out.println("R: " + r);
				// if ch is a DIGIT, then it represents one of the words already
				// in the freqstack and we need to retrieve that information and
				// append it to the s string
				number.append(ch);

				// Process the formatting codes
			} else if (ch == '<') { // alphanumeric characters contained within
				// a set of chevron brackets are considered
				// speical formating characters are are not
				// included in the freqstack.
				StringBuilder formatting = new StringBuilder("<");
				while (ch != '>') {
					r = in.read();
					ch = (char) r;
					formatting.append(ch);
				}
				s.append(formatting.toString());
			} else {
				// if ch is any other non-letter digit, then we need to end the
				// word and process the word through the freqstack
				if (word.length() != 0) {
					int loc = freqstack.find(word.toString());
					freqstack.moveUp(word.toString(), loc);
					s.append(word);
				} else if (number.length() != 0) {
					String numString = number.toString();
					int num = Integer.parseInt(numString);
					String retrieved = freqstack.bringBack(num);
					s.append(retrieved);
					freqstack.moveUp(retrieved, num);
				}
				s.append(ch);
				word = new StringBuilder();
				number = new StringBuilder();
			}
			r = in.read();
		}
		s.append(word.toString());

		// -------------------- save results --------------------//
		System.out.println();
		PrintStream output = new PrintStream(file);
		output.print(s);
		output.close();
		// ------------------------------------------------------//

	}

	// Compresses the file and outputs a line of statistics that indicates the
	// original size and the new size
	public static void compress(File file) throws IOException {
		// The freqstack will contain the elements (Words) of the document
		// ordered according to frequency using the MTF heuristic
		FrequencyStack freqstack = new FrequencyStack();
		// S will be the final output of the compressed file
		StringBuilder s = new StringBuilder("0 ");
		// word will represent the current word being compared to other elements
		// in the freqstack
		StringBuilder word = new StringBuilder();

		BufferedReader in = new BufferedReader(new FileReader(file));
		// r will represent the character as an int
		int r = in.read();
		// while r exists
		// Count the bytes for the statistics generator
		int byteCount = 0;

		// The file's text is processed character by character
		while (r != -1) {
			char ch = (char) r;

			if (Character.isLetter(ch)) { // each letter will be appended to the
											// word
				word.append(ch);

			} else if (ch == '\'') { // when the next character is NOT a letter,
										// then that indicates the end of the
										// word
				word.append(ch);

			} else if (ch == '<') { // alphanumeric characters contained within
									// a set of chevron brackets are considered
									// speical formating characters are are not
									// included in the freqstack.
				StringBuilder formatting = new StringBuilder("<");
				while (ch != '>') {
					r = in.read();
					ch = (char) r;
					formatting.append(ch);
				}
				s.append(formatting.toString());

			} else { // When we reach a non-apostrphe special character, it
						// indicates we have reached the end of the word.
				if (word.length() != 0) { // If the word is not empty, it must
											// be processed
					int loc = freqstack.find(word.toString());
					// if the word is not in the freqstack, then the word itself
					// is added to the end of the s string
					if (loc == -1) {
						s.append(word.toString());

					} else { // if the word IS in the freqstack, then the
								// position is added to the s String
						s.append(loc);
					}
					// Now handle the word in the freqstack. moveUp will remove
					// an instance of the word from the list and add it to the
					// front
					freqstack.moveUp(word.toString(), loc);
				}
				// After processing the word, the non-letter character is now
				// added to the s string
				s.append(ch);
				// The word is reset to empty
				word = new StringBuilder();
			}

			// Read the next character in the file to continue in the while loop
			r = in.read();
			if ((char) r != '\n') {
				byteCount++;
			}
		}
		s.append(word.toString());
		int compressedCount = s.length();
		in.close();

		PrintStream output = new PrintStream(file);
		output.print(s);
		output.close();
		System.out.println("\n0 Uncompressed: " + byteCount + " bytes.  Compressed: " + compressedCount + " bytes");

	}
}
