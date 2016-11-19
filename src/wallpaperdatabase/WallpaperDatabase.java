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
    
    
    static ArrayList<String> parseForImgur(String htmlData){
        ArrayList<String> imgurUrls = new ArrayList<>();
        
        Pattern imgurUrl = Pattern.compile("(http:\\/\\/i.imgur.com\\/[^a]......[^/]...)|(https:\\/\\/i.imgur.com\\/[^a]......[^/]...)|(http:\\/\\/imgur.com\\/[^a]......[^/]...)|(https:\\/\\/imgur.com\\/[^a]......[^/]...)");
        
        String[] splitData = htmlData.split("data-href-url=\"");
        
        for(int i = 0; i<splitData.length; i++){
            imgurUrls.add(splitData[i]);
        }
        
        System.out.println("Split input html");
        
        for(int i = 0; i<imgurUrls.size(); i++){
            if(!((imgurUrls.get(i).substring(0, 17)).equals("http://i.imgur.com"))||!((imgurUrls.get(i).substring(0, 15)).equals("http://imgur.com"))){ //If arraylist entry is not an imgur url, remove
                imgurUrls.remove(i);
            }
        }
        
        for(int i = 0; i<imgurUrls.size(); i++){
            if((imgurUrls.get(i).substring(0,20)).equals("http://i.imgur.com/a/")||(imgurUrls.get(i).substring(0,21)).equals("https://i.imgur.com/a/")||(imgurUrls.get(i).substring(0,18)).equals("http://imgur.com/a/")||(imgurUrls.get(i).substring(0,19)).equals("https://imgur.com/a/")){ //Remove albums
                imgurUrls.remove(i);
            }
        }
        
        for(int i = 0; i<imgurUrls.size(); i++){
            if((imgurUrls.get(i).substring(0,26)).equals("http://i.imgur.com/gallery/")||(imgurUrls.get(i).substring(0,27)).equals("https://i.imgur.com/gallery/")||(imgurUrls.get(i).substring(0,24)).equals("http://imgur.com/gallery/")||(imgurUrls.get(i).substring(0,25)).equals("https://imgur.com/gallery/")){ //Remove galleries
                imgurUrls.remove(i);
            }
        }
        
        System.out.println("Removed extra data.");
        
        for(int i = 0; i<imgurUrls.size(); i++){
            imgurUrls.set(i, imgurUrls.get(i).substring(0, 30));  //set imgurUrls entry to raw imgur url //imgur url is 30 characters long
            System.out.println(imgurUrls.get(i));
        }
        
        System.out.println("Removed all characters but imgur urls");
        
       return imgurUrls; 
    }
    
}
