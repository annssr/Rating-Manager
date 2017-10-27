import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
public class RatingManager {
    // Constructor
    private BST<BST<Rating>> User;
    private BST<BST<Rating>> Item;
    // Constructor
    public RatingManager(){
        User = new BST<BST<Rating>>();
        Item = new BST<BST<Rating>>();
    }
     
    // Read ratings from a file and create a RatingManager object that stores these ratings. The ratings must be inserted in their order of appearance in the file.
    public static RatingManager read(String fileName){
        try {
            File f = new File(fileName);
            Scanner sc = new Scanner(f);
  
            RatingManager rm = new RatingManager();
            while (sc.hasNextInt()) {
                Rating r = new Rating(sc.nextInt(), sc.nextInt(), sc.nextInt());
                sc.nextInt();
                rm.addRating(r);
            }
            sc.close();
            return rm;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
 
    // Add a rating
    public void addRating(Rating rating){
         
        if( ((!Item.empty()) && (Item.findKey(rating.getItemId())) ) && (!User.empty()) && (User.findKey(rating.getUserId())) ){
            User.retrieve().insert(rating.getItemId(),rating);
            Item.retrieve().insert(rating.getUserId(),rating);
            return;
        }else if((!Item.empty()) && Item.findKey(rating.getItemId())){
            Item.retrieve().insert(rating.getUserId(),rating);
            BST<Rating> u = new BST<Rating>();
            u.insert(rating.getItemId(), rating);
            User.insert(rating.getUserId(),u);
            return;
        }else if((!User.empty()) && (User.findKey(rating.getUserId()))){
            User.retrieve().insert(rating.getItemId(),rating);
            BST<Rating> i = new BST<Rating>();
            i.insert(rating.getUserId(), rating);
            Item.insert(rating.getItemId(),i);
            return;
        }
         
        BST<Rating> i = new BST<Rating>();
        i.insert(rating.getUserId(), rating);
        Item.insert(rating.getItemId(),i);
         
        BST<Rating> u = new BST<Rating>();
        u.insert(rating.getItemId(), rating);
        User.insert(rating.getUserId(),u);  
    }
    // Return all ratings given by user i. 
    public LinkedList<Rating> getUserRatings(int i){
        if(!User.findKey(i))
            return null;
         
        return User.retrieve().getAllData();
    }
 
    // Return all ratings given to item j
    public LinkedList<Rating> getItemRatings(int j){
        if(!Item.findKey(j))
            return null;
         
        return Item.retrieve().getAllData();
    }
 
    // Return the list of highest rated items
    public LinkedList<Integer> getHighestRatedItems(){
        LinkedList<BST<Rating>> Items = Item.getAllData();
        Items.findFirst();
        double maxR = getAverageItemRating(Items.retrieve().retrieve().getItemId());
        Items.findNext();
         
        while(!Items.last()){
            if(getAverageItemRating(Items.retrieve().retrieve().getItemId()) > maxR){
                maxR = getAverageItemRating(Items.retrieve().retrieve().getItemId());
            }
            Items.findNext();
        }
        if(getAverageItemRating(Items.retrieve().retrieve().getItemId()) > maxR){
            maxR = getAverageItemRating(Items.retrieve().retrieve().getItemId());
        }
         
        LinkedList<Integer> hri = new LinkedList<Integer>();
        System.out.println(maxR);
        Items.findFirst();
        while(!Items.last()){
            if(getAverageItemRating(Items.retrieve().retrieve().getItemId()) == maxR)
                hri.insert(Items.retrieve().retrieve().getItemId());
            Items.findNext();
        }
        if(getAverageItemRating(Items.retrieve().retrieve().getItemId()) == maxR)
            hri.insert(Items.retrieve().retrieve().getItemId());
        return hri;
    }
 
    // Return the average rating of item j. If i has no ratings, -1 is returned
    public double getAverageItemRating(int j){
        if(!Item.findKey(j))
            return -1;
        LinkedList<Rating> i = Item.retrieve().getAllData();
        int count = 0;
        double tot = 0;
        i.findFirst();
        while(!i.last()){
            count++;
            tot += i.retrieve().getValue();
            i.findNext();
        }
        count++;
        tot += i.retrieve().getValue();
        return tot/count;
    }
 
    // Return the average rating given by user i. If i has no ratings, -1 is returned
    public double getAverageUserRating(int i){
        if(!User.findKey(i))
            return -1;
        LinkedList<Rating> j = User.retrieve().getAllData();
        int count = 0;
        double tot = 0;
        j.findFirst();
        while(!j.last()){
            count++;
            tot += j.retrieve().getValue();
            j.findNext();
        }
        count++;
        tot += j.retrieve().getValue();
        return tot/count;
    }
    //***************************************************************************
     
    // Return the rating of user i for item j. If there is no rating, -1 is returned.
    public int getRating(int i, int j){
        if(User.findKey(i))
            if((User.retrieve()).findKey(j))
                return User.retrieve().retrieve().getValue();
        return -1;
    }
 
    // Return the number of keys to compare with in order to find the rating of user i for item j.
    public int nbComp(int i, int j){
        int n1 = 0, n2 = 0;
        n1 = User.nbComp(i);
        if(User.findKey(i)){
            n2 = (User.retrieve()).nbComp(j);
        }
        return n1 + n2;
    }
    // Compute the distance between the two users ui and uj. If ui and uj have no common item in their ratings, then Double.POSITIVE_INFINITY is returned.
    public double getDist(int ui, int uj){
    	User.findKey(ui);
    	LinkedList<Rating> org = (User.retrieve()).getAllData();
    	User.findKey(uj);
    	BST<Rating> cpy = User.retrieve();
    	double n1 = 0, n2 = 0;
    	int i = 0;
    	org.findFirst();
    	while(! org.last()){
    		if(cpy.findKey(org.retrieve().getItemId())){
    			n2 = org.retrieve().getValue() - cpy.retrieve().getValue();
    			n1 += Math.pow(n2, 2);
    			i++;
    		}
    		org.findNext();
    	}
    	if(cpy.findKey(org.retrieve().getItemId())){
			n2 = org.retrieve().getValue() - cpy.retrieve().getValue();
			n1 += Math.pow(n2, 2);
			i++;
		}
    	if(i == 0)
    		return Double.POSITIVE_INFINITY;
    	n1 = Math.sqrt(n1);
    	n1 /= i;
    	return n1;
    }
 
    // Return a list of at most k nearest neighbors to user i from a list of users. User i and users at infinite distance should not be included (the number of users returned can therefore be less than k).
    public LinkedList<Integer> kNNUsers(int i, LinkedList<Integer> users, int k){
		if(users.empty())
			return null;
		int lng = 0;
		users.findFirst();
		while(! users.last()){
			users.findNext();
			lng++;
		}
		lng++;
		PQ q = new PQ<Integer>(lng);
		users.findFirst();
		while(! users.last()){
			q.enqueue(-1*getDist(i, users.retrieve()), users.retrieve());
			users.findNext();
		}
		q.enqueue(-1*getDist(i, users.retrieve()), users.retrieve());
		LinkedList<Integer> kn = new LinkedList<Integer>();
		PQElem<Integer> pq;
		for(int i1 = 0; i1<k; i1++){
			if((pq = q.serve()).priority == Double.POSITIVE_INFINITY)
				break;
			kn.insert(pq.data);
		}
		return kn;
	}
 
    // Return the average rating given to item j by a list of users. If the list users is empty or none of the users it contains rated item j, then the global average rating of item j (as computed by getAverageItemRating(j)) is returned.
    public double getAverageRating(int j, LinkedList<Integer> users){
    	int count = 0;
    	double ratingSum = 0, tmp = 0;
    	users.findFirst();
    	while(! users.last()){
    		tmp = getRating(users.retrieve(), j);
    		if((int)tmp != -1){
    			ratingSum+=tmp;
    			count++;
    		}
    		users.findNext();
    	}
    	tmp = getRating(users.retrieve(), j);
		if((int)tmp != -1){
			ratingSum+=tmp;
			count++;
		}
    	if(count == 0)
    		return getAverageItemRating(j);
    	return ratingSum/count;
    }
 
    // Return an estimation of the rating given by user i for item j using k nearest neighbor users.
    public double getEstimatedRating(int i, int j, int k){
        int r = getRating(i, j);
        if (r != -1) {
            return r;
        }
        LinkedList<Rating> ratings = getItemRatings(j);
        LinkedList<Integer> users = new LinkedList<Integer>();
        if ((ratings != null) && !ratings.empty()) {
            ratings.findFirst();
            while (!ratings.last()) {
                users.insert(ratings.retrieve().getUserId());
                ratings.findNext();
            }
            users.insert(ratings.retrieve().getUserId());
        }
        LinkedList<Integer> knn = kNNUsers(i, users, k);
        return getAverageRating(j, knn);
    }
}