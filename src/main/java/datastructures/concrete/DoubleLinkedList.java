package datastructures.concrete;
import datastructures.interfaces.IList;
import misc.exceptions.EmptyContainerException;
import java.util.Iterator;
import java.util.NoSuchElementException;



public class DoubleLinkedList<T> implements IList<T> {
    
    private Node<T> front;
    private Node<T> back;
    private int size;

    public DoubleLinkedList() {
        this.front = null;
        this.back = null;
        this.size = 0;
    }

    @Override
    public void add(T item) {
        if (front == null || back == null) {
            this.front = new Node<T>(null, item, null);
            this.back = this.front;
        } 
        else {
            Node<T> curr = this.back;
            this.back = new Node<T>(null, item, null);
            curr.next = this.back;
            this.back.prev = curr;
        }
        this.size++; 
    }

    @Override
    public T remove() {
        if (this.size==0) {
    			throw new EmptyContainerException();
    		}
    		Node<T> endNode = front;
    		Node<T> prevToEnd = null;
    		if (front.next == null) {
    			front = null;
    			back = null;
    		}
    		while (endNode.next != null) {
    			prevToEnd = endNode;
    			endNode = endNode.next;
    		}
    		if (front !=null) {
    			prevToEnd.next = null;
    		}
    		this.size--;    		
    		return (T) endNode.data;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index>=this.size) {
			throw new IndexOutOfBoundsException();
		}
    	Node<T> curr = front;
		for (int i = 0; i < index; i++) {
			curr = curr.next;
		}
		return (T) curr.data;
    }

    @Override
    public void set(int index, T item) {
        if (index <0 || index>=this.size) {
			throw new IndexOutOfBoundsException();
		}
    	Node<T> curr = front;
    	    if (index == 0) {
    	if (front.next!=null) {
    	    front = new Node<T>(item);
    	    front.next = curr.next; }
    	else {
    	    front = new Node<T>(item); }
    	return;
    	}
    	int counter = 1;
    	Node<T> prev1 = front;
    	curr = curr.next;
        while (counter <= index) {
    	if (counter == index) {
    	    Node<T> currBFrepl = curr;
    	if (curr.next != null) {
           curr = new Node<T>(item);
           prev1.next = curr;
           curr.next = currBFrepl.next; 
    	}
    	else {
    	   curr = new Node<T>(item);
           prev1.next = curr;
           curr.next = null; }
    	break;
    	}
    	  curr = curr.next;
    	  prev1 = prev1.next;
    	  counter++;
        }
    }

    @Override
    
    public void insert(int index, T item) {
        if (this.size == 0 && index != 0) {
            throw new IndexOutOfBoundsException();
        } else if (this.size == 0 && index == 0) {
            Node<T> newNode = new Node<T>(item);
            front = newNode;
            back = newNode;
        } else if (index == size) {
            back.next = new Node<T>(back, item, null);
            back = back.next;
        } else if (index == 0) {
            front.prev = new Node<T>(null, item, front);
            front = front.prev;
        } else {
            Node<T> originalNode = nodeAtIndex(index);
            originalNode.prev.next = new Node<T>(originalNode.prev, item, originalNode);
            originalNode.prev = originalNode.prev.next;
        }
        this.size++;
        
    }
    
    // Returns the node at a particular index
    // Throws IndexOutOfBounds Exception if index is out of range
    private Node<T> nodeAtIndex(int index) {
        if (index < 0 || index > this.size - 1) {
            throw new IndexOutOfBoundsException();
        }
        if (index < (this.size / 2)) {
            Node<T> curr = front;
            for (int i = 0; i < index; i++) {
                curr = curr.next;
            }
            return curr;
        } else {
            Node<T> curr = back;
            for (int i = size - 1; i > index; i--) {
                curr = curr.prev;
            }
            return curr;
        }
    }
 
 
    @Override
    public T delete(int index) {
        if (index < 0 || index >= this.size) {
			throw new IndexOutOfBoundsException();
		}
       
        Node<T> deletedNode = nodeAtIndex(index);
        if (size == 1) { //change
            front = null;
            back = null;
        } else {
            if (index == 0) {
                deletedNode.next.prev = null;
                front = deletedNode.next;
            } else if (index < size - 1) {
                deletedNode.prev.next = deletedNode.next;
                deletedNode.next.prev = deletedNode.prev;
            } else {
                deletedNode.prev.next = null;
                back = deletedNode.prev;
            }
            
        }
        this.size--;
        return deletedNode.data;
    }

    @Override
    public int indexOf(T item) {
    		Node<T> currNode = front;
    		for (int i = 0; i < this.size; i++) {
    		    if (item == null) {
    		        if (currNode.data == null) {
    		            return i;
    		        }
    		    } else if (item.equals(currNode.data)) {
    		        return i;
    		    }
    		    currNode = currNode.next;
    		}
    		return -1;
    }
 

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean contains(T other) {
        return (indexOf(other) > -1);
    }

    @Override
    public Iterator<T> iterator() {
        return new DoubleLinkedListIterator<>(this.front);
    }
    

    private static class Node<E> {
        // You may not change the fields in this node or add any new fields.
        public final E data;
        public Node<E> prev;
        public Node<E> next;

        public Node(Node<E> prev, E data, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }

        public Node(E data) {
            this(null, data, null);
        }

        // Feel free to add additional constructors or methods to this class.
    }

    private static class DoubleLinkedListIterator<T> implements Iterator<T> {
        // You should not need to change this field, or add any new fields.
        private Node<T> curr;

        public DoubleLinkedListIterator(Node<T> curr) {
            // You do not need to make any changes to this constructor.
            this.curr = curr;
        }

        /**
         * Returns 'true' if the iterator still has elements to look at;
         * returns 'false' otherwise.
         */
        public boolean hasNext() {
            return (curr != null);
        }

        /**
         * Returns the next item in the iteration and internally updates the
         * iterator to advance one element forward.
         *
         * @throws NoSuchElementException if we have reached the end of the iteration and
         *         there are no more elements to look at.
         */
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            T data = curr.data;
            curr = curr.next;
            return data;
          
        }
    }
}
