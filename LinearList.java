

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;


public class LinearList<E> implements LinearListADT<E> {

    private Node<E> head;
    private Node<E> tail;
    private int currentSize;
    private int modCounter;

    // constructors
    public LinearList() {
        head = null;
        tail = null;
        currentSize = 0;
        modCounter = 0;
    }
    @Override
    public void addLast(E obj) {
        Node<E> newNode = new Node<>(obj, null);
        if (isEmpty()){
            head = newNode;
        } else {
            //tail.setNext(newNode);
            tail.next = newNode;
        }
        tail = newNode;        
        modCounter++;
        currentSize++;
    }

    @Override
    public void addFirst(E obj) {        
        head = new Node<>(obj, head);
        if (currentSize == 0) {
            tail = head;
        }
        currentSize++;        
        modCounter++;
    }

    @Override
    public void insert(E obj, int location) {
        if ((location<1) || (location > size()+1)) {
            throw new RuntimeException("Insertion outside bounds of list is not allowed");
        } 
        if ( isEmpty() )
            throw new RuntimeException("Attempt to insert into empty list. Use addFirst method");
        Node<E> current = head;
        Node<E> previous = null;
        Node<E> nodeToBeInserted = new Node(obj);
        
        if (location == 1){
           addFirst(obj);
        } 
        else {
            for (int i = 1; i < location; i++) {                
                previous = current;
                current = current.next;
            }
            
            nodeToBeInserted.next = current;
            previous.next = nodeToBeInserted;
            modCounter++;
            currentSize++;
        }
        
    }

    @Override
    public E remove(int location) {
        if ((location<1) || (location > size())) {
            throw new RuntimeException("Deletion outside bounds of list is not allowed");
        } 
        if ( isEmpty() )
            throw new RuntimeException("Attempt to Delete empty list is not allowed.");
        Node<E> current = head;
        Node<E> previous = null;
        Node<E> nodeToBeRemoved;
        
        if (location == 1){
           return (E) removeFirst();
        } else if (location == size()){
           return (E) removeLast();
        } else {
            for (int i = 1; i < location; i++) {
                previous = current;
                current = current.next;
            }
            nodeToBeRemoved = current;
            previous.next = current.next;
            currentSize --;
            modCounter++;
            return (E) nodeToBeRemoved;
        }
       
    }
    
    @Override
    public E remove(E obj) {
        
        int loc = locate(obj);
        return(E) remove(loc) ;
    }

    @Override
    public E removeFirst() {
        if (isEmpty()) return null;
        
        E temp = head.getElement();
        head = head.getNext();
        if (currentSize == 0) tail = null;
        modCounter++;
        currentSize--;
        return temp;
    }

    @Override
    public E removeLast() {
        
        if (isEmpty()) return null;
        Node<E> current;
        Node<E> previous;
        
        current = head;
        previous = null;
        while ( current != null && current.next !=null){
            previous = current;
            current = current.next;
        }
        if (current == null)
            return null;
        if (current == head)
            return removeFirst();
        tail = previous;
        tail.next = null;
        modCounter++;
        currentSize--;
        return current.data;
    }

    @Override
    public E get(int location) {
        int index = 0;
        Node<E> current;
        current = head;
        if ( location < 1 || location > currentSize )
            throw new RuntimeException("Location does not map to a valid position within the list.");
        for (int i=1; i < location; i++) {
            current = current.next;
        }    
        return (E) current.data;
    }

    @Override
    public boolean contains(E obj) {
        
        if (currentSize == 0) return false;
        Node<E> current = head;
        
        if (((Comparable<E>)current.data).compareTo(obj)== 0) return true;
        //while ( current.next != null){
        for (int i=1; i<=size(); i++){    
            if ( (( Comparable<E>)current.data).compareTo(obj)== 0 ){
                return true;
            } 
            current = current.next;
        }
        return false;
    }
    
    @Override
    public int locate(E obj) {
        if (currentSize == 0) return -1;
        Node<E> current = head;
        
        if (((Comparable<E>)obj).compareTo(current.data)== 0) return 1;
        
//        for (int i=1; i<=size(); i++){
//            if ( (( Comparable<E>)obj).compareTo(current.data)== 0 ){
//                return i;
//            } 
//            current = current.next;            
//        }
        int i=1;
        while (current != null){
            if ( (( Comparable<E>)obj).compareTo(current.data)== 0 ){
                return i;
            } 
            current = current.next;   
            i++;
        }
        
        return -1;
    }

    @Override
    public void clear() {
        head = null;
        tail = null;
        currentSize = 0;
        modCounter++;
    }

    @Override
    public boolean isEmpty() {
            return currentSize == 0;
    }

    @Override
    public int size() {
        return currentSize;
    }

    @Override
    public Iterator<E> iterator() {
        return new IteratorHelper();
    }
    
    // Iterator Helper Class

    public class IteratorHelper implements Iterator<E> {

        Node<E> currentNode;
        Node<E> previousNode;
        int concurrentMod;

        public IteratorHelper() {
            currentNode = head;
            previousNode = null;
            concurrentMod = modCounter;
        }

        public boolean hasNext() {
            if (concurrentMod != modCounter) {
                throw new ConcurrentModificationException();
            }
            return currentNode != null;
        }

        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            previousNode = currentNode;
            currentNode = currentNode.next;
            return previousNode.data;
        }

        public void remove() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    
    
    // Node Helper Class
    protected class Node<E> {

        E data;
        Node<E> next;

        public Node(E d){
            data = d;
            next = null;
        }
        public Node(E d, Node<E> n) {
            data = d;
            next = n;
        }

        public E getElement() {
            return data;
        }

        public Node<E> getNext() {
            return next;
        }

        public void setNext(Node<E> node) {
            next = node;
        }
    }
}
