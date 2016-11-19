/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wallpaperdatabase;

/**
 *
 * @author David
 */

import java.net.*;
import java.io.*;
import java.util.*;
import java.util.regex.*;

public class WallpaperDatabase {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        
        
        parseForImgur(downloadWebpageData("https://www.reddit.com/r/wallpapers/"));
    }
    
    
    
    static String downloadWebpageData(String stringUrl){
        
        String htmlData = "";
        
        try{
           URL url = new URL(stringUrl);
           URLConnection urlConnect = url.openConnection();
           urlConnect.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36");  //So default user agent isn't used and server doesn't return a 429
           
           BufferedReader in = new BufferedReader(new InputStreamReader(urlConnect.getInputStream())); 
           String dataBuffer;
           
           while((dataBuffer = in.readLine())!=null){
               htmlData = htmlData.concat(dataBuffer);
               //System.out.println(dataBuffer);
               System.out.println("Downloaded Data");
           }
        }
        catch(Exception e){
            System.out.println(e.toString());
        }
        System.out.println(htmlData);
        return htmlData;
    }
    
    
    static Set<String> parseForImgur(String htmlData){  //Uses Set collection to avoid duplicates
        Set<String> imgurUrls = new HashSet<>();
        
        Pattern imgurUrl = Pattern.compile("(http:\\/\\/i.imgur.com\\/[^a]......[^/]...)|(https:\\/\\/i.imgur.com\\/[^a]......[^/]...)|(http:\\/\\/imgur.com\\/[^a]......[^/]...)|(https:\\/\\/imgur.com\\/[^a]......[^/]...)");
        Matcher matchedUrls = imgurUrl.matcher(htmlData);
        matchedUrls.find();
        
        while(matchedUrls.find()){
            imgurUrls.add(matchedUrls.group());
        }
        
        System.out.println("Removed all characters but imgur urls");
        
       return imgurUrls; 
    }
    
}
