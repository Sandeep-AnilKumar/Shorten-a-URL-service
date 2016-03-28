"# Shorten-a-URL-service" 

"#ShortenALongURL.java"
When a text file containing a list of long URL's is gievn as an input to this program, it firsts creates a list of these URL's and passes this to the function shortenURL(urls);

In the shortenURL function, the list of url's are replaced accordingly and split to extract domain and the path. The checksum value of the path using the CRC32 is calcualted and the values are put in the Map.

When a particular URL is being processed, if it is seen that the checksum is already present in the map, then the domain of that checksum is retrieved to see if the URL is a duplicate of the previous one.

e.g. : - "https://stackoverflow.com/questions/1085709/what-does-synchronized-mean".

If the domain is different that means, the path is same for the two different domains. i.e. 

e.g. : - "https://stackoverflow.com/questions/1085709/what-does-synchronized-mean"
"https://www.quora.com/questions/1085709/what-does-synchronized-mean".

In such a case the checksum will be same, but the later will one will be changed to checksum += 1. and again this checksum is checked for clashes.

Finally based on the checksum value a short URL is assigned to it. And the ouput is written to a file.

I/P file : - /com/resources/ShortenURLS.txt
O/P file : - OutputShortenedURLS.txt (at the home directory.).

"#RedirectAShortURL.java".
Since I haven't used a Database to store the values of the checksum and the short and long URL's, because I am just showing the working of the code. As such I will have to build the Map's of checksum to shortURl's and domains etc., therefore I am calling the ShortenALongURL.java and creating those Maps. 

And later based on the checksum value retrieved for the shortURL that is passed to RedirectAShortURL.java code, the domain and the paths are extracted.

If a checksum is not found in the Map, that means a short URL that was never created is given as an input. In such case "Error 404 page is shown."

I/P file : - /com/resources/ShortenedURLS.txt
O/P file : - OutputRedirectedURLS.txt (at the home directory.).

Do let me know in case of any errors or suggestions. I am open to explaining the code if there is some confusion.
Email: - sanilk2@uic.edu

"Thank you.".
