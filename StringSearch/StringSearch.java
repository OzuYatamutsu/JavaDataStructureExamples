import java.util.ArrayList;
import java.util.List;


public class StringSearch implements StringSearchInterface {
	
	/**
	 * The base of the hash functions.
	 */
	public static final int BASE = 433;

	@Override
	public List<Integer> boyerMoore(String needle, String haystack) {
		if (needle.equals(null) || haystack.equals(null) 
				|| needle.length() == 0) {
			throw new IllegalArgumentException("Needle or haystack cannot be null!");
		}
		List<Integer> matches = new ArrayList<Integer>();
		int[] map = buildLastTable(needle);
		boolean endOfString = false;
		boolean mismatch = false;
		if (needle.length() <= haystack.length()) {
			// Align needle with first char in haystack
			int lastCharIndex = (needle.length() - 1);
			
			while (!endOfString) {
				if (lastCharIndex >= haystack.length()) {
					endOfString = true;
				} else {
					for (int i = (needle.length() - 1); i > -1 && !mismatch; i--) {
						if (needle.charAt(i) != haystack.charAt((lastCharIndex - needle.length() + 1) + i)) {
							mismatch = true;
						}
					}
	
					if (!mismatch) {
						// Found substring in string!
						matches.add(lastCharIndex - needle.length() + 1);
					}
					
					lastCharIndex += map[haystack.charAt(lastCharIndex)];
					mismatch = false;
				}
			}
		}
		
		return matches;
	}

	@Override
	public int[] buildLastTable(String needle) {
		if (needle.equals(null)) {
			throw new IllegalArgumentException("Provided String cannot be empty!");
		}
		
		int[] table = new int[Character.MAX_VALUE + 1];
		for (int i = 0; i != Character.MAX_VALUE + 1; i++) {
			if (needle.indexOf(i) != -1) {
				table[i] = Math.max((needle.length() - needle.lastIndexOf(i) - 1), 1);
			} else {
				// All other characters, shift full length
				table[i] = needle.length();
			}
		}
		
		return table;
	}

	@Override
	public int generateHash(String current) {
		if (current.equals(null)) {
			throw new IllegalArgumentException("Provided String cannot be empty!");
		}
		
		int hash = 0;
		
		for (int i = 0; i < current.length(); i++) {
			hash += current.charAt(i) * power(BASE, ((current.length() - 1) - i));
		}
		
		return hash;
	}
	
	/**
	 * Calculates the result of a base to the power of an exponential.
	 * 
	 * @param base The base of the exponential
	 * @param exp The power
	 * @return The result of the base to the power of the exponential
	 */
	public int power(int base, int exp) {
		int result = base;
		
		for (int i = 1; i < exp; i++) {
			result = (base * result);
		}
		
		return (exp != 0) ? result : 1;
	}

	@Override
	public int updateHash(int oldHash, int length, char oldChar, char newChar) {
		int updatedHash = oldHash;
		updatedHash = (oldHash - (oldChar * power(BASE, (length - 1)))) * 433 
				+ (newChar * power(BASE, 0));
		
		return updatedHash;
	}

	@Override
	public List<Integer> rabinKarp(String needle, String haystack) {
		if (needle.equals(null) || haystack.equals(null) 
				|| needle.length() == 0) {
			throw new IllegalArgumentException("Needle or haystack cannot be null!");
		}
		
		List<Integer> matches = new ArrayList<Integer>();
		if (needle.length() <= haystack.length()) {
			int needleHash = generateHash(needle);
			int haystackHash = generateHash(haystack.substring(0, needle.length()));
			for (int i = 0; i <= haystack.length() - needle.length(); i++) {
				if (needleHash == haystackHash) {
					boolean match = true;
					// Possible match, check char-by-char
					for (int j = 0; j < needle.length() && match; j++) {
						if (needle.charAt(j) != haystack.charAt(i + j)) {
							match = false;
						}
					}
					
					if (match) {
						matches.add(i);
					}
				} 
				
				if (i < (haystack.length() - needle.length())) {
					haystackHash = updateHash(haystackHash, needle.length(), 
							haystack.charAt(i), haystack.charAt(i + needle.length()));
				}
			}
		}
		
		return matches;
	}

}
