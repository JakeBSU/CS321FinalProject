import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;



/**
 * @author ryanc
 *
 */
public class IUDoubleLinkedList<T> implements IndexedUnsortedList<T> {

	private Node2<T> head, tail;
	private int size,modCount;



	/**
	 * The constructor
	 */
	public IUDoubleLinkedList(){
		head = tail = null;
		size = modCount = 0;
	}

	/* (non-Javadoc)
	 * @see IndexedUnsortedList#addToFront(java.lang.Object)
	 */
	@Override
	public void addToFront(T element) {
		Node2<T> added = new Node2<T>(element);
		if(size==0) {
			head=tail=added;
		}else {
			added.setNext(head);
			head.setPrev(added);
			head=added;
		}
		size++;
		modCount++;

	}

	/* (non-Javadoc)
	 * @see IndexedUnsortedList#addToRear(java.lang.Object)
	 */
	@Override
	public void addToRear(T element) {
		Node2<T> added = new Node2<T>(element);
		added.setPrev(tail);
		if(!isEmpty()) {
			tail.setNext(added);
		}else{
			head=added;
		}
		tail=added;
		size++;
		modCount++;

	}

	/* (non-Javadoc)
	 * @see IndexedUnsortedList#add(java.lang.Object)
	 */
	@Override
	public void add(T element) {
		addToRear(element);

	}

	/* (non-Javadoc)
	 * @see IndexedUnsortedList#addAfter(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void addAfter(T element, T target) {
		int index = indexOf(target);
		if (index==-1) {
			throw new NoSuchElementException();
		}
		add(index+1,element);
	}

	/* (non-Javadoc)
	 * @see IndexedUnsortedList#add(int, java.lang.Object)
	 */
	@Override
	public void add(int index, T element) {
		if(index<0||index>size) {
			throw new IndexOutOfBoundsException();
		}
		ListIterator<T> itr = listIterator(index);
		itr.add(element);
	}

	/* (non-Javadoc)
	 * @see IndexedUnsortedList#removeFirst()
	 */
	@Override
	public T removeFirst() {
		if(isEmpty()) {
			throw new NoSuchElementException();
		}
		T returnElement = head.getElement();
		head = head.getNext();

		if(size==1) {
			tail = null;
		}else{
			head.setPrev(null);
		}
		size--;
		modCount++;
		return returnElement;
	}

	/* (non-Javadoc)
	 * @see IndexedUnsortedList#removeLast()
	 */
	@Override
	public T removeLast() {
		if(isEmpty()) {
			throw new NoSuchElementException();
		}
		T retVal;
		if(size==1) {
			retVal = tail.getElement();
			head=tail=null;
		}else {
			retVal = tail.getElement();
			tail.getPrev().setNext(null);
			tail = tail.getPrev();
		}
		size--;
		modCount++;
		return retVal;
	}

	/* (non-Javadoc)
	 * @see IndexedUnsortedList#remove(java.lang.Object)
	 */
	@Override
	public T remove(T element) {
		//using iterator
		int index = indexOf(element);
		if(index==-1) {
			throw new NoSuchElementException();
		}
		ListIterator<T> itr = listIterator(index);
		T retVal = itr.next();
		itr.remove();
		return retVal;
		//Independent method
//		int index = indexOf(element);
//		if(index==-1) {
//			throw new NoSuchElementException();
//		}
//		Node2<T> current = head;
//		for(int i = 0;i<index;i++) {
//			current = current.getNext();
//		}
//		T retVal = current.getElement();
//		if(current==head) {
//			head = head.getNext();
//		}else {
//			current.getPrev().setNext(current.getNext());
//		}
//		if(current == tail) {
//			tail = tail.getPrev();
//		}else {
//			current.getNext().setPrev(current.getPrev());
//		}
//		size--;
//		modCount++;
//		return retVal;
	}

	/* (non-Javadoc)
	 * @see IndexedUnsortedList#remove(int)
	 */
	@Override
	public T remove(int index) {
		if(index<0||index>=size) {
			throw new IndexOutOfBoundsException();
		}
		ListIterator<T> itr = listIterator(index);
		T retVal = itr.next();
		itr.remove();
		return retVal;
	}

	/* (non-Javadoc)
	 * @see IndexedUnsortedList#set(int, java.lang.Object)
	 */
	@Override
	public void set(int index, T element) {
		if(index<0||index>=size) {
			throw new IndexOutOfBoundsException();
		}
		ListIterator<T> itr = listIterator(index);
		itr.next();
		itr.set(element);

	}

	/* (non-Javadoc)
	 * @see IndexedUnsortedList#get(int)
	 */
	@Override
	public T get(int index) {
		if(index<0||index>=size) {
			throw new IndexOutOfBoundsException();
		}
		ListIterator<T> itr = listIterator(index);		
		return itr.next();
	}

	/* (non-Javadoc)
	 * @see IndexedUnsortedList#indexOf(java.lang.Object)
	 */
	@Override
	public int indexOf(T element) {
		ListIterator<T> itr = listIterator();		
		boolean found = false;
		while(itr.hasNext()&&!found) {
			if(itr.next().equals(element)) {
				found=true;
			}
		}
		if(found==false) {
			return -1;
		}
		return itr.previousIndex();
	}

	/* (non-Javadoc)
	 * @see IndexedUnsortedList#first()
	 */
	@Override
	public T first() {
		if(isEmpty()) {
			throw new NoSuchElementException();
		}
		return head.getElement();
	}

	/* (non-Javadoc)
	 * @see IndexedUnsortedList#last()
	 */
	@Override
	public T last() {
		if(isEmpty()) {
			throw new NoSuchElementException();
		}
		return tail.getElement();
	}

	/* (non-Javadoc)
	 * @see IndexedUnsortedList#contains(java.lang.Object)
	 */
	@Override
	public boolean contains(T target) {
		int index = indexOf(target);
		return (index!=-1);
	}

	/* (non-Javadoc)
	 * @see IndexedUnsortedList#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		return (size==0);
	}

	/* (non-Javadoc)
	 * @see IndexedUnsortedList#size()
	 */
	@Override
	public int size() {
		return size;
	}

	/* (non-Javadoc)
	 * @see IndexedUnsortedList#iterator()
	 */
	@Override
	public Iterator<T> iterator() {
		return listIterator();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if (!isEmpty()) {
			Iterator<T> itr = listIterator();
			StringBuilder str = new StringBuilder();
			str.append("[");
			for (int i = 0; i<size-1;i++) {
				str.append(itr.next().toString());
				str.append(", ");
			}
			str.append(itr.next().toString());
			str.append("]");
			return str.toString();
//			String string = "[";
//			for (int i = 0; i < rear - 1; i++) {
//				string += (array[i] + ", ");
//			}
//			string += (array[rear - 1] + "]");
//			return string;
		}else {
			return "[]";
		}
	}

	/**
	 * Private inner class for an Iterator or List Iterator
	 * @author ryanc
	 *
	 */
	private class DLLIterator implements ListIterator<T>{

		private Node2<T> nextNode,lastNode;
		private int iterModCount,nextIndex; 

		/**
		 * The constructor
		 */
		public DLLIterator() {
			nextNode=head;
			iterModCount = modCount;
			nextIndex = 0;
			lastNode=null;
		}

		/**
		 * Creates a List Iterator positioned at (right before) the given index
		 * @param startIndex
		 */
		public DLLIterator(int startIndex) {
			this();
			if(startIndex<0||startIndex>size) {
				throw new IndexOutOfBoundsException();
			}
			for (int i = 0; i < startIndex; i++) {
				next();
			}
			lastNode=null;
		}

		/* (non-Javadoc)
		 * @see java.util.ListIterator#add(java.lang.Object)
		 */
		@Override
		public void add(T element) {
			if(iterModCount!=modCount) {
				throw new ConcurrentModificationException();
			}
			Node2<T> added = new Node2<T>(element);
			added.setNext(nextNode);
			if(size==0) {// new head and tail
				head = tail = added;
			}else {
				if(nextNode==head) {//new head, not tail
					added.setPrev(nextNode.getPrev());
					nextNode.setPrev(added);
					head = added;
				}else if(nextNode==null){//tail, not a head
					tail.setNext(added);
					added.setPrev(tail);
					tail = added;
				}else {//not head, not tail
					nextNode.getPrev().setNext(added);
					added.setPrev(nextNode.getPrev());
					nextNode.setPrev(added);
				}			
			}
			nextIndex++;
			modCount++;
			iterModCount++;
			size++;
		}

		/* (non-Javadoc)
		 * @see java.util.ListIterator#hasNext()
		 */
		@Override
		public boolean hasNext() {
			if(iterModCount!=modCount) {
				throw new ConcurrentModificationException();
			}
			return (nextNode!=null);
		}

		/* (non-Javadoc)
		 * @see java.util.ListIterator#hasPrevious()
		 */
		@Override
		public boolean hasPrevious() {
			if(iterModCount!=modCount) {
				throw new ConcurrentModificationException();
			}
			return (nextNode!=head);
		}

		/* (non-Javadoc)
		 * @see java.util.ListIterator#next()
		 */
		@Override
		public T next() {
			T retVal=null;
			if(hasNext()) {
				retVal = nextNode.getElement();
				lastNode=nextNode;
				nextNode = nextNode.getNext();
				nextIndex++;
			}else {
				throw new NoSuchElementException();
			}
			return retVal;
		}

		/* (non-Javadoc)
		 * @see java.util.ListIterator#nextIndex()
		 */
		@Override
		public int nextIndex() {
			if(iterModCount!=modCount) {
				throw new ConcurrentModificationException();
			}
			return (nextIndex);
		}

		/* (non-Javadoc)
		 * @see java.util.ListIterator#previous()
		 */
		@Override
		public T previous() {
			T retVal=null;
			if(hasPrevious()) {
				if (nextNode!=null) {
					nextNode = nextNode.getPrev();
				}else {
					nextNode=tail;
				}
				retVal = nextNode.getElement();
				nextIndex--;
				lastNode = nextNode;
			}else {
				throw new NoSuchElementException();
			}
			return retVal;
		}

		/* (non-Javadoc)
		 * @see java.util.ListIterator#previousIndex()
		 */
		@Override
		public int previousIndex() {
			if(iterModCount!=modCount) {
				throw new ConcurrentModificationException();
			}
			return (nextIndex-1);
		}

		/* (non-Javadoc)
		 * @see java.util.ListIterator#remove()
		 */
		@Override
		public void remove() {
			if(iterModCount!=modCount) {
				throw new ConcurrentModificationException();
			}
			if(lastNode==null) {
				throw new IllegalStateException();
			}
			if(lastNode!=head) {//dealing with head node
				lastNode.getPrev().setNext(lastNode.getNext());
			}else {
				head=head.getNext();
			}
			if(lastNode!=tail) {
				lastNode.getNext().setPrev(lastNode.getPrev());
			}else {
				tail = tail.getPrev();
			}

			if(lastNode==nextNode) {//prev()
				nextNode=nextNode.getNext();
			}else {//next()
				nextIndex--;
			}
			lastNode=null;
			modCount++;
			iterModCount++;
			size--;
		}

		/* (non-Javadoc)
		 * @see java.util.ListIterator#set(java.lang.Object)
		 */
		@Override
		public void set(T element) {
			if(iterModCount!=modCount) {
				throw new ConcurrentModificationException();
			}
			if(lastNode==null) {
				throw new IllegalStateException();
			}
			lastNode.setElement(element);
			modCount++;
			iterModCount++;

		}

	}


	/* (non-Javadoc)
	 * @see IndexedUnsortedList#listIterator()
	 */
	@Override
	public ListIterator<T> listIterator() {
		return new DLLIterator();
	}

	/* (non-Javadoc)
	 * @see IndexedUnsortedList#listIterator(int)
	 */
	@Override
	public ListIterator<T> listIterator(int startingIndex) {
		return new DLLIterator(startingIndex);
	}

}
