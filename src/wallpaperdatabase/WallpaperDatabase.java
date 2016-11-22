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
import java.awt.Image;
import javax.imageio.ImageIO;
import java.awt.image.*;

public class WallpaperDatabase {

    /**
     * @param args the command line arguments
     */

    static int fileIndex = 0;
    static String INSTALL_DIRECTORY = System.getProperty("user.home")+"\\WallpaperDatabase";
    static Set<String> wallpaperUrls = new HashSet<>();
    
    public static void main(String[] args) {
        // TODO code application logic here
        
        System.out.println("Wallpaper Database V1.0.0");
        System.out.println("0. Shutdown Program"+"\n"+
                "1. Refresh URL Data"+"\n"+
                "2. Download images"+"\n"+
                "3. Analyze Images"+"\n"+
                "8. Save Data and Exit"+"\n"+
                "9. Install WallpaperDatabase");
        
        Scanner sc = new Scanner(System.in);
        
        boolean isRunning = true;
        
        while(isRunning){
            int choice = sc.nextInt();
            switch(choice){
                case 0:
                    isRunning = false;
                    break;
                case 1:
                    wallpaperUrls=parseForImgur(downloadWebpageData("https://www.reddit.com/r/wallpapers/"));
                    System.out.println("Download Completed");
                    break;
                
                case 2:
                    Iterator urlIterator = wallpaperUrls.iterator();
                    downloadImage(urlIterator.next().toString());
                    System.out.println("Image Downloaded");
                    break;

                case 8:
                    saveConfig();
                    break;

                case 9:
                    install();
                    System.out.println("WallpaperDatabase Installed");
                    break;

                default:
                    break;
            }
        }
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
           }
        }
        catch(Exception e){
            System.out.println(e.toString());
        }
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

       return imgurUrls; 
    }
    
    static void downloadImage(String stringUrl){
        
        BufferedImage image = null;
        try{
            URL url = new URL(stringUrl);
            image = ImageIO.read(url);
            String imageName = getFileName().concat(".").concat(getFileFormat(stringUrl));
            File imageFile = new File(INSTALL_DIRECTORY+"\\downloads"+imageName);
            imageFile.createNewFile();
            System.out.println(imageFile.getAbsolutePath());
            ImageIO.write(image, getFileFormat(stringUrl), imageFile);
        }
        catch(Exception e){
            System.out.println(e.toString());
        }
    }
    
    static String getFileFormat(String stringUrl){
        String fileFormat = stringUrl.substring(stringUrl.length()-3, stringUrl.length()); //Return last 3 letters in URL, thus the file format
        return fileFormat;
    }
    
    static String getFileName(){
        String fileName = Integer.toString(fileIndex);
        WallpaperDatabase.fileIndex++;
        return fileName;

    }

    static void loadConfig(Config toLoad){

        Config config = null;  //Declaration outside of try/catch for visibility.

        try{
            FileInputStream fileIn = new FileInputStream(INSTALL_DIRECTORY+"\\config.ser");     //Logical fallacy, cannot get INSTALL_DIRECTORY
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);                         //without having loaded config. Possibly use
            config = (Config) objectIn.readObject();                                            //text file in same dir as .jar
            objectIn.close();
            fileIn.close();
        }
        catch(Exception e){
            System.out.println(e.toString());
        }

        fileIndex = config.getFileIndex();
        INSTALL_DIRECTORY = config.getINSTALL_DIRECTORY();
        wallpaperUrls = config.getWallpaperUrls();
    }

    static void saveConfig(){
        Config config = new Config(wallpaperUrls, INSTALL_DIRECTORY, fileIndex);

        try{
            FileOutputStream fileOut = new FileOutputStream(INSTALL_DIRECTORY);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(config);
            objectOut.close();
            fileOut.close();
        }
        catch(Exception e){
            System.out.println(e.toString());
        }

    }

    static void install(){
        File dir = new File(INSTALL_DIRECTORY);
        dir.mkdir();

        File downloads = new File(INSTALL_DIRECTORY+"\\downloads");
        downloads.mkdir();
    }
    
}
