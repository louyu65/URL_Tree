import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * @author stephenharlow
 *         Stephen Harlow
 *         srh150030
 *         Project: URL_Tree
 *         File: Tree
 *         Created on September 23, 2016
 */
public class Tree {

    public ArrayList<Tree> branches = new ArrayList<>();
    public URL headLink;
    public Tree parentTree; //Link to Tree Above
    protected boolean Valid; //Does Not Exist Prior and Real Link

    public Tree(){
        parentTree = null;
        headLink = null;
    }
    public Tree(String link) throws MalformedURLException {
        //Link Only (String Form)
        this(new URL(link));
    }
    private Tree(URL link){
        //Link Only (URL Form)
        this(null, link);
    }

    public Tree(Tree parent, URL links){
        this();
        setHeadLink(links);
        parentTree = parent;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!Tree.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        final Tree other = (Tree) obj;
        if ((this.headLink == null) ? (other.headLink != null) : !this.headLink.equals(other.headLink)) {
            return false;
        }
        return true;
    }

    protected Tree getRootTree(Tree here){
        if(here.parentTree != null){
            return here.getRootTree(here.parentTree);
        }
        return here;
    }



    //****************** Getters and Setters ******************
    public ArrayList<Tree> getBranches() {
        return branches;
    }

    public void addBranch(ArrayList<Tree> branches) {
        this.branches = branches;
    }

    public URL getHeadLink() {
        return headLink;
    }

    private void setHeadLink(URL headLink) {
        this.headLink = headLink;
    }

    public Tree getParentTree() {
        return parentTree;
    }

    private void setParentTree(Tree parentTree) {
        this.parentTree = parentTree;
    }
    public boolean checkValidity(){
        return this.Valid;
    }
}
