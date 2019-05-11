import java.util.ListIterator;

public class Cache<T> {

	private IUDoubleLinkedList<T> cache1;
	private IUDoubleLinkedList<T> cache2;
	private int size1;
	private int size2;
	 
	private int NH=0, NH1=0, NH2=0, NR=0, NR1=0, NR2=0;
	private double HR=0, HR1=0, HR2=0;
//	HR: (global) cache hit ratio
//	HR1: 1st-level cache hit ratio
//	HR2: 2nd-level cache hit ratio
//	NH: total number of cache hits
//	NH1: number of 1st-level cache hits
//	NH2: number of 2nd-level cache hits
//	NR: total references to cache
//	NR1: number of references to 1st-level cache
//	NR2: number of references to 2nd-level cache (= number of 1st-level cache misses)
	
	
	
	
	
	
	
	public Cache(int depth,int size1) {
		if(depth!=1) {
			System.err.println("Usage:java Test 1 <cache size> <input textfile name> or \n java Test 2 <1st-level cache size> <2nd-level cache size> <input textfile name>");
			System.exit(0);
		}
		this.size1=size1;
		System.out.println("First level cache with "+size1+"entries has been created.");
		System.out.println("..................................................................");
		cache1 = new IUDoubleLinkedList<T>();
		cache2 = null;
	}
	
	public Cache(int depth,int size1,int size2) {
		if(depth!=2) {
			System.err.println("Usage:java Test 1 <cache size> <input textfile name> or \n java Test 2 <1st-level cache size> <2nd-level cache size> <input textfile name>");
			System.exit(0);
		}
		this.size1=size1;
		cache1 = new IUDoubleLinkedList<T>();
		this.size2 = size2;
		cache2 = new IUDoubleLinkedList<T>();
		System.out.println("First level cache with size "+size1+"entries has been created.");
		System.out.println("Second level cache with size "+size1+"entries has been created.");
		System.out.println("..................................................................");
	}
	
	public T getObject(T object) {
		NR++;
		ListIterator<T> iter1 = cache1.listIterator();
		NR1++;
		boolean found = false;
		boolean done=false;
		T retval = null;
		if(cache1.size()==0) {
		}else {
		while (!done) {
			retval = iter1.next();
			if(retval.equals(object)) {
				found = true;
				done=true;
			}
			if(!iter1.hasNext()) {
				done=true;
			}
		}
		if(found==true) {
			iter1.remove();
			NH1++;
			NH++;
		}else {
			if(cache1.size()==size1) {
				cache1.removeLast();
			}
		}
		}
		
		cache1.addToFront(object);
		if(cache2!=null) {
			if(cache2.size()==0) {
				NR2++;
			}else {
			ListIterator<T> iter2 = cache2.listIterator();
			boolean prev = found;
			if(found==false) {
				NR2++;
			}
			found=false;
			done=false;
			while (!done) {
				retval = iter2.next();
				if(retval.equals(object)) {
					found = true;
					done=true;
				}
				if(!iter2.hasNext()) {
					done=true;
				}
			}
			if(found==true) {
				iter2.remove();
				if(prev==false) {
					NH2++;
					NH++;
				}
			}else if(cache2.size()==size2) {
				cache2.removeLast();
			}
			}
			cache2.addToFront(object);
		}
		if(found==false) {
			retval=null;
		}
		return retval;
	}
	
	public T find(T object) {
		NR++;
		ListIterator<T> iter1 = cache1.listIterator();
		NR1++;
		boolean found = false;
		boolean done=false;
		T retval = null;
		if(cache1.size()==0) {
		}else {
		while (!done) {
			retval = iter1.next();
			if(retval.equals(object)) {
				found = true;
				done=true;
			}
			if(!iter1.hasNext()) {
				done=true;
			}
		}
		if(found==true) {
			iter1.remove();
			cache1.addToFront(object);
			NH1++;
			NH++;
		}
		}
		
		
		if(cache2!=null) {
			if(cache2.size()==0) {
				NR2++;
			}else {
			ListIterator<T> iter2 = cache2.listIterator();
			boolean prev = found;
			if(found==false) {
				NR2++;
			}
			found=false;
			done=false;
			while (!done) {
				retval = iter2.next();
				if(retval.equals(object)) {
					found = true;
					done=true;
				}
				if(!iter2.hasNext()) {
					done=true;
				}
			}
			if(found==true) {
				iter2.remove();
				cache2.addToFront(object);
				if(prev==false) {
					NH2++;
					NH++;
				}
			}
			}
			
		}
		if(found==false) {
			retval=null;
		}
		return retval;
	}
	
	
	
	public void addObject(T object) {
		getObject(object);
	}
	
	public void removeObject(T object) {
		getObject(object);
		cache1.removeFirst();
		if(cache2!=null) {
			cache2.removeFirst();
		}
	}
	
	public void clearCache() {
		cache1 = new IUDoubleLinkedList<T>();
		cache2 = new IUDoubleLinkedList<T>();
	}
	
	public void printStats() {
		HR=(double)NH/NR;
		HR1=(double)NH1/NR1;
		HR2=(double)NH2/NR2;
		System.out.println("Total number of references: "+NR);
		System.out.println("Total number of cache hits: "+NH);
		System.out.println("The global hit ratio: \t\t"+HR);
		System.out.println("Number of 1st-level cache hits: "+NH1);
		System.out.println("1st-level cache hit ratio: \t\t"+HR1);
		if(cache2!=null) {
			System.out.println("Number of 2nd-level cache hits: "+NH2);
			System.out.println("2nd-level cache hit ratio: \t\t"+HR2);
		}
	}
	
	
	
	
	
	
}
