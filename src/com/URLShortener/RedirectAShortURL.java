package com.URLShortener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RedirectAShortURL extends ShortenALongURL {
	public static void main(String[] args) {

		ShortenALongURL.main(null); //Since not using a DB. we will first have to create short URL's and then use the Maps, to redirect it.
		List<String> urls  = new ArrayList<String>();

		InputStream ir = null;
		BufferedReader br = null;

		try{

			ir  = ShortenALongURL.class.getResourceAsStream("/com/resources/ShortenedURLS.txt"); // File consisting of short URL's which has to be redirected.
			br = new BufferedReader(new InputStreamReader(ir));

			String currentURL = "";

			while( (currentURL = br.readLine() ) != null ) {
				urls.add(currentURL);
			}
			redriectURL(urls); //call to function that will redirect short URL's to Original long URL's.
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

	public static void redriectURL(List<String> urls) throws FileNotFoundException {

		if(urls == null || urls.size() == 0) {
			return;
		}

		PrintWriter writer = new PrintWriter("OutputRedirectedURL.txt");
		String currentURL = "";
		String[] parts;
		String shortURL = "";
		Long checksumValue = 0L;
		String domain = "";
		String[] pathParts;
		StringBuffer sb;
		int length = 0;
		String redirectURL = "";
		String path = "";
		int endIndex = 0;

		for(Iterator<String> i = urls.iterator(); i.hasNext();) {

			sb = new StringBuffer();
			currentURL = i.next();
			parts = currentURL.split("/");

			shortURL = parts[1];
			checksumValue = shortToLongURL.get(shortURL); //Find the checksum value of this short URL.

			if(checksumValue != null && checksumValue != 0L) {
				pathParts = sumToPath.get(checksumValue);
				length = pathParts.length; // retrieve the original parts of the long URL using this checksum.

				for(int j = 1; j < length; j++) {
					sb.append(pathParts[j]);
					sb.append("/");
				}

				domain = sumToDomainMap.get(checksumValue); // retrieve the original domain of this checksum.
				
				path = sb.toString();
				endIndex = path.lastIndexOf("/");
				
				if(endIndex != -1) {
					path = path.substring(0, endIndex);
				}
				
				redirectURL = "http://" + domain + "/" + path; // redirect the URL with domain and path found out.
			}

			else { // if the checksum value is not present, this short URL was not created.
				redirectURL = "Error 404, Page not Found. Accessing a short URL that was not created";
			}

			writer.println(redirectURL); // write the appropriate long URL's of the short URL's to the file.
		}

		try {
			if(writer != null) {
				writer.close();
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
