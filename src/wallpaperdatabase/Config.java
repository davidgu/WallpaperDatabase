package wallpaperdatabase;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by David on 11/21/2016.
 */

public class Config implements Serializable{

        private Set<String> wallpaperUrls = new HashSet<>();
        private String INSTALL_DIRECTORY = "";
        private int fileIndex;

        Config(Set<String> wallpaperUrls, String INSTALL_DIRECTORY, int fileIndex ){
                this.wallpaperUrls = wallpaperUrls;
                this.INSTALL_DIRECTORY = INSTALL_DIRECTORY;
                this.fileIndex = fileIndex;
        }

        public Set<String> getWallpaperUrls(){
                return wallpaperUrls;
        }

        public String getINSTALL_DIRECTORY(){
                return INSTALL_DIRECTORY;
        }

        public int getFileIndex(){
                return fileIndex;
        }


}
