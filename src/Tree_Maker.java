import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

/**
 * @author stephenharlow
 *         Stephen Harlow
 *         srh150030
 *         Project: URL_Tree
 *         File: Tree_Maker
 *         Created on September 23, 2016
 */
public class Tree_Maker {
    private static HashMap<String, Tree> baseURLs = new HashMap<>();
    public static String PrintString = "";
    public static int counter = 0;//Link Counter. Once it hits the LEN_LIMIT, the LinkFinder Stops
    static Tree root;//Root Tree (First URL)
    public static int LEN_LIMIT = 5000; //GIVEN LIMIT TODO: Convert into Parameter
    public static long time;
    public static void main(String[] args) throws MalformedURLException {
        root = new Tree("https://en.wikipedia.org/wiki/Main_Page");//Start with my website
        time = System.nanoTime();
        LinkFinder(root.getHeadLink(), root);//Calls The initial LinkFinder
        System.out.println("\n");//Link Finder has ended, Print the Tree
        PrintTree(root, "");//Print with no pre-text

    }
    public static void PrintTree(Tree input, String beg){
        for(Tree sub: input.branches){//Loop through the Branches and Recursively Print
            System.out.println(beg + sub.headLink); //Print the Pre-Text and the link (Allows for more visual depth)
            PrintTree(sub, beg + "\t");//Add another tab to the pre-text
        }
    }
    public static boolean SearchTree(URL link){//Simple way to Search from the Root Tree
        return LoopTree(root.getRootTree(root), link);
    }
    public static boolean LoopTree(Tree search, URL link) { //Loop Through and Call SearchTree(__,___)
        boolean ret = true;
        for (Tree item : search.getBranches()) {
            ret = ret && SearchTree(item, link);//If All of them plus this one return true
        }
        return ret;

    }
    protected static boolean SearchTree(Tree search, URL link){//Overloaded SearchTree to search outside Root
        //Loop through the URL Keys in the Branches
        return (!search.getHeadLink().equals(link) && LoopTree(search, link));//Simple Way to find if the head link and desired link are equal
        //Then if they are: Return False and SKip this link
        //If they aren't: Loop Through the Branches
/*
       if(search.getHeadLink().equals(link)){
            //Already Exists. Abort
            return false;
        }
        else{
            return LoopTree(search, link);
        }
*/

    }
    protected static void LinkFinder(URL url, Tree toAdd){//Simpler Link Finder
        LinkFinder(url, toAdd, 200, 0);
    }
    protected static void LinkFinder(URL url, Tree toAdd, int layerlimit, int layer){
        Document doc = null;
        if(counter < LEN_LIMIT)//If left to the inner check, it wouldn't stop TODO: Look into better solutions to ensuring LinkFinder stops
        try {
                doc = Jsoup.connect(String.valueOf(url)).get();//Connect and Get URL
                Elements links = doc.select("a[href]");//Get all the Links

                for (Element link : links) {
                    String attr = link.attr("abs:href").toLowerCase();
                    if(!baseURLs.containsKey(attr)){
                        try {
                            //attr.compareTo(String.valueOf(toAdd.headLink)) != 0 && SearchTree(new URL(attr)) && layer < layerlimit && counter < LEN_LIMIT
                            if (layer < layerlimit && counter < LEN_LIMIT) {
                                //Compare HeadLink and toAdd.Link
                                //Search from the root for the link
                                //Check if layer is below the limit (Don't want to follow a single link too far and ignore others)
                                //Check if the LinkFinder should stop
//+ " " + trim(link.text(), 35)
                                counter += 1;

                                PrintString += (new String(new char[layer]).replace("\0", "_") +" "+ attr + " (" + counter + ") \n");
                                if(counter % 100 == 0 && counter > 0){
                                    System.out.println("\nBREAKPOINT");
                                    System.out.println((System.nanoTime()-time)/1000000000.0 + " seconds elapsed");
                                    System.out.println(counter + " URL's");
                                    System.out.println(((System.nanoTime()-time)/counter)/1000000000.0 + " seconds per URL");
                                    System.out.println("\n");
                                    if(counter %1000 == 0){
                                        System.out.print(PrintString);
                                        PrintString = "";
                                    }
                                }
                                //Used to print links and the titles given to them
                                Tree set = new Tree(toAdd, new URL(attr));
                                toAdd.branches.add(set);//Add the branches now
                                baseURLs.put(attr, set);
                            }
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    }


                }
                for (Tree newer: toAdd.branches) {
                    LinkFinder(newer.getHeadLink(), newer, layerlimit, layer + 1);//Loop through the branches later (Provides more even layer distribution)
                }

        }
        catch (HttpStatusException e){

        }
        catch (IOException e) {

        }

    }

    private static void print(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }

    private static String trim(String s, int width) {
        //Trims to a certain Width

        if (s.length() > width)
            return s.substring(0, width-1) + ".";
        else
            return s;
    }
}
