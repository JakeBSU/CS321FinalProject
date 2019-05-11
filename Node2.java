
/**
 * Helper class for IUDoubleLinkedList. Each node holds one element and knows what node preceded it and which node follows it. 
 * 
 * @author ryanc
 *
 * @param <T>
 */
public class Node2<T> {
	
	private T element;
	private Node2<T> next,prev;
	
	/**
	 * @param startElement
	 */
	public Node2(T startElement) {
		element = startElement;
		next=prev=null;
	}
	
	
	/**
	 * @return previous node
	 */
	public Node2<T> getPrev() {
		return prev;
	}


	/**
	 * @param prev
	 */
	public void setPrev(Node2<T> prev) {
		this.prev = prev;
	}


	/**
	 * @return the element
	 */
	public T getElement() {
		return element;
	}
	/**
	 * @param element
	 */
	public void setElement(T element) {
		this.element = element;
	}
	/**
	 * @return next
	 */
	public Node2<T> getNext() {
		return next;
	}
	/**
	 * @param next
	 */
	public void setNext(Node2<T> next) {
		this.next = next;
	}
	
	

}
