import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author stephenharlow
 *         Stephen Harlow
 *         srh150030
 *         Project: URL_Tree
 *         File: Tree_Maker
 *         Created on September 23, 2016
 */
public class Tree_Maker {
    public static int counter = 0;
    static Tree root;
    public static int LEN_LIMIT = 1000;
    public static void main(String[] args) throws MalformedURLException {
        root = new Tree("http://stephenharlow.com/");
        LinkFinder(root.getHeadLink(), root);
        System.out.println("\n");
        PrintTree(root, "");

    }
    public static void PrintTree(Tree input, String beg){
        for(Tree sub: input.branches){
            System.out.println(beg + sub.headLink);
            PrintTree(sub, beg + "\t");
        }
    }
    public static boolean SearchTree(URL link){
        return LoopTree(root.getRootTree(root), link);
    }
    public static boolean LoopTree(Tree search, URL link) {
        boolean ret = true;
        for (Tree item : search.getBranches()) {
            ret = ret && SearchTree(item, link);
        }
        return ret;

    }
    protected static boolean SearchTree(Tree search, URL link){
        //Loop through the URL Keys in the Branches
        return (search.getHeadLink().equals(link) ? false:LoopTree(search, link));
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
    protected static void LinkFinder(URL url, Tree toAdd){
        LinkFinder(url, toAdd, 10, 0);
    }
    protected static void LinkFinder(URL url, Tree toAdd, int layerlimit, int layer){
        Document doc = null;
        if(counter < LEN_LIMIT)
        try {
            doc = Jsoup.connect(String.valueOf(url)).get();
            Elements links = doc.select("a[href]");
            System.out.println(toAdd.headLink);
            for (Element link : links) {
                String attr = link.attr("abs:href");
                try {
                    if(attr.compareTo(String.valueOf(toAdd.headLink)) != 0 && SearchTree(new URL(attr)) && layer < layerlimit && counter < LEN_LIMIT){
                        System.out.println(attr + " " + trim(link.text(), 35));

                        toAdd.branches.add(new Tree(toAdd, new URL(attr)));
                        counter += 1;
                        System.out.println(counter);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }


            }
            for(Tree newer: toAdd.branches){
                LinkFinder(newer.getHeadLink(), newer, layerlimit, layer + 1);
            }
        } catch (HttpStatusException e){
        } catch (IOException e) {
        }

    }

    private static void print(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }

    private static String trim(String s, int width) {
        if (s.length() > width)
            return s.substring(0, width-1) + ".";
        else
            return s;
    }

    public void LinkFinder(String url) throws MalformedURLException {
        LinkFinder(new URL(url), root);
    }
}
