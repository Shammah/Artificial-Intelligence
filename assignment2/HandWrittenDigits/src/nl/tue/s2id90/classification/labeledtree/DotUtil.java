/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tue.s2id90.classification.labeledtree;

import java.awt.Desktop;
import java.awt.HeadlessException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * The utilities for getting dot images from the google api are seriously limited
 * by that api's limitations as formulated at <a href="https://developers.google.com/chart/image/docs/gallery/graphviz">
 * the google graphviz chart documentation</a>. Furthermore, the api is deprecated :-)
 * @author huub
 * @see 
 */
public class DotUtil {
    /** opens a webpage in a browser.
     * 
     * @param url  url of the web page
     */
    public static void openWebpage(URL url) {
        try {
            URI uri = url.toURI();
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.BROWSE)) {
                desktop.browse(uri);
            }
        } catch (UnsupportedOperationException|URISyntaxException | IOException ex) {
            Logger.getLogger(DotUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * opens the given dot specification of a graph in a browser.
     * 
     * @param dot specification
     * @see DotVisitor
     */
    public static void showDotInBrowser(String dot) {
        URL url = getDotImageURL(dot);
        DotUtil.openWebpage(url);        
    }

    private static URL getDotImageURL(String dot) {
        String template = "http://chart.googleapis.com/chart?chl=DOT&cht=gv";
        try {
            String urlString = template.replaceFirst("DOT", URLEncoder.encode(dot,"UTF-8"));
            URL url = new URL(urlString);
            return url;        
        } catch(MalformedURLException|UnsupportedEncodingException ex) {
            Logger.getLogger(DotUtil.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    /** 
     * @return an ImageIcon after remote rendering by a google chart service. 
     **/
    public static ImageIcon getDotImage(String dot) {
        return new ImageIcon(getDotImageURL(dot));
    }
    
    public static void showDotInFrame(String dot, String title) throws HeadlessException {       
        JFrame frame = new JFrame(title);
        frame.add(new JLabel(DotUtil.getDotImage(dot)));
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }    
}
