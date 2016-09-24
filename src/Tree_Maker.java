import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * @author stephenharlow
 *         Stephen Harlow
 *         srh150030
 *         Project: URL_Tree
 *         File: Tree_Maker
 *         Created on September 23, 2016
 */
public class Tree_Maker {
    private static HashSet<String> baseURLs = new HashSet<>();
    public static String PrintString = "";
    public static int counter = 0;//Link Counter. Once it hits the LEN_LIMIT, the LinkFinder Stops
    public static int LEN_LIMIT = 10001; //GIVEN LIMIT TODO: Convert into Parameter
    public static long time;
    public static boolean printBreak = false;
    public static void main(String[] args) throws MalformedURLException {
        time = System.nanoTime();
        LinkFinder(new URL("https://en.wikipedia.org/wiki/Main_Page"));//Calls The initial LinkFinder
        System.out.println("\n");//Link Finder has ended
        System.out.println("\nBREAKPOINT");
        System.out.println((System.nanoTime() - time) / 1000000000.0 + " seconds elapsed");
        System.out.println(counter + " URL's");
        System.out.println(((System.nanoTime() - time) / counter) / 1000000000.0 + " seconds per URL");
        System.out.println("\n");
    }
    protected static void LinkFinder(URL url){//Simpler Link Finder
        LinkFinder(url, 200, 0);
    }
    protected static void LinkFinder(URL url, int layerlimit, int layer){
        Document doc = null;
        ArrayList<String> cur_branch = new ArrayList<>();
        if(counter < LEN_LIMIT)//If left to the inner check, it wouldn't stop TODO: Look into better solutions to ensuring LinkFinder stops
        try {
                doc = Jsoup.connect(String.valueOf(url)).get();//Connect and Get URL
                Elements links = doc.select("a[href]");//Get all the Links

                for (Element link : links) {
                    String attr = link.attr("abs:href");
                    if(!baseURLs.contains(attr)){
                        //attr.compareTo(String.valueOf(toAdd.headLink)) != 0 && SearchTree(new URL(attr)) && layer < layerlimit && counter < LEN_LIMIT
                        if (layer < layerlimit && counter < LEN_LIMIT) {
                            //Compare HeadLink and toAdd.Link
                            //Search from the root for the link
                            //Check if layer is below the limit (Don't want to follow a single link too far and ignore others)
                            //Check if the LinkFinder should stop
//+ " " + trim(link.text(), 35)
                            counter += 1;

                            PrintString += (new String(new char[layer]).replace("\0", ":") +" "+ attr + " (" + counter + ") \n");
                            if(counter % 100 == 0 && counter > 0){
                                if(printBreak) {
                                    System.out.println("\nBREAKPOINT");
                                    System.out.println((System.nanoTime() - time) / 1000000000.0 + " seconds elapsed");
                                    System.out.println(counter + " URL's");
                                    System.out.println(((System.nanoTime() - time) / counter) / 1000000000.0 + " seconds per URL");
                                    System.out.println("\n");
                                }
                                if(counter %1000 == 0){
                                    System.out.print(PrintString);
                                    PrintString = "";
                                }
                            }
                            //Used to print links and the titles given to them
                            cur_branch.add(attr);//Add the branches now
                            baseURLs.add(attr);
                        }
                    }


                }
                for (String newer: cur_branch) {
                    LinkFinder(new URL(newer), layerlimit, layer + 1);//Loop through the branches later (Provides more even layer distribution)
                }

        }
        catch (HttpStatusException e){

        }
        catch (IOException e) {

        }

    }

}
