package com.URLShortener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class ShortenALongURL {

	static Map<Long, String> sumToDomainMap = new HashMap<Long, String>(); // Checksum to Domain Map.
	static Map<Long, String[]> sumToPath = new HashMap<Long, String[]>(); // Checksum to the original path Map.
	static Map<String, Long> shortToLongURL = new HashMap<String, Long>(); // Short URL generated to Checksum Map.
	static Map<Long, String> sumToShortURL = new HashMap<Long, String>(); // Checksum to Short URL Map.

	public static void main(String[] args) {

		List<String> urls  = new ArrayList<String>();

		InputStream ir = null;
		BufferedReader br = null;

		try{

			ir  = ShortenALongURL.class.getResourceAsStream("/com/resources/ShortenURLS.txt"); // A file consisiting of long URL's.
			br = new BufferedReader(new InputStreamReader(ir));

			String currentURL = "";

			while( (currentURL = br.readLine() ) != null ) {
				urls.add(currentURL); // Each URL that is read is added to a List.
			}
			shortenURL(urls); // Call to shorten the URL's.
		}

		catch (IOException e) {
			e.printStackTrace();
		}

		finally {
			try {
				if(br != null) {
					br.close();
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void shortenURL(List<String> urls) throws FileNotFoundException {
		PrintWriter writer = new PrintWriter("OutputShortenedURLS.txt"); //Output is written to this file.

		if(urls == null || urls.size() == 0) {
			return;
		}

		String currentURL = "";
		String[] parts;
		String domain = "";
		StringBuffer sb;
		byte bytes[];
		long checksumValue = 0L;
		String shortURL = "";
		String tempStr = "";

		for(Iterator<String> i = urls.iterator(); i.hasNext();) {

			sb = new StringBuffer();
			currentURL = i.next();
			currentURL = currentURL.replaceAll("^http(s)?://(www.)?",""); // Remove http:// or https://.

			parts = currentURL.split("/");
			domain = parts[0];

			for(int j = 1; j < parts.length; j++) {
				sb.append(parts[j]); //Apart from the domain, all other parts are combined, whose checksum will be calculated.
			}

			bytes = sb.toString().getBytes();

			Checksum checksum = new CRC32();
			checksum.update(bytes, 0, bytes.length);
			checksumValue = checksum.getValue(); //Checksum value of the String.

			if(! sumToDomainMap.containsKey(checksumValue)) {
				sumToDomainMap.put(checksumValue, domain);
			}

			else {
				tempStr = "";
				while((! tempStr.equals(domain)) && (sumToDomainMap.containsKey(checksumValue))) {
					tempStr = sumToDomainMap.get(checksumValue); //If the checksum is already present, See if the domain of that checksum is same.
					if(! tempStr.equals(domain)) {
						checksumValue += 1L; // if not add one to the checksum and check again.
					}
				}
				sumToDomainMap.put(checksumValue, domain);
			}

			shortURL = getPathFromChecksumValue(checksumValue); //assign a short URL based on checksum.

			sumToShortURL.put(checksumValue, shortURL);
			sumToPath.put(checksumValue, parts);
			shortToLongURL.put(shortURL, checksumValue);

			writer.println("san.dy/" + shortURL); //Write the short URL to the file.
		}

		try {
			if(writer != null) {
				writer.close();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getPathFromChecksumValue(long checksumValue) {

		if(checksumValue == 0) {
			return "";
		}

		String shortURL = "";
		long temp = 0L;
		long remainder = 0L;
		List<Long> values = new ArrayList<Long>();
		long currentLong = 0L;
		StringBuffer sb = new StringBuffer();
		char c;

		if(! sumToPath.containsKey(checksumValue)) { //If the checksum is already present, then retrieve the short URL, else create one.

			temp = checksumValue;

			while(temp > 0) {
				remainder = temp % 100;
				values.add(remainder); //Breaking the checksum values into 2 digit numbers.
				temp /= 100;
			}

			sb = new StringBuffer();

			for(Iterator<Long> k = values.iterator(); k.hasNext();) { //for each 2 digit number.
				currentLong = k.next();

				if(currentLong <= 51) { // see if it is below 51. 0 -> 0, 1-26 -> a-z, 27-51 -> A-Z is assigned.
					if(currentLong <= 26) {
						if(currentLong == 0) {
							c = '0';
						}
						else {
							c = (char) (96L + currentLong);
						}
						sb.append(c);
					}
					else {
						currentLong -= 26;
						c = (char) (64L + currentLong);
						sb.append(c);
					}

				}

				else {
					temp = currentLong; // if the 2 digit number is > 51.

					while(temp > 0) {
						remainder = temp % 10; //break it into two 1 digit numbers and assign a lower case letter.
						if(remainder == 0) {
							c = '0';
						}
						else {
							c = (char) (remainder + 64L);
						}
						sb.append(c);
						temp /= 10;
					}
				}
			}

			shortURL = sb.toString();
		}
		else {
			shortURL = sumToShortURL.get(checksumValue);
		}

		return shortURL;
	}
}
