package textCompressor;

//---- FrequencyStack ----
class FrequencyStack {
	// ---- nested Item class ----
	public class Node {
		private String element;
		private Node next;

		public Node(String s, Node n) {
			element = s;
			next = n;

		}

		public void setElement(String s) {
			element = s;
		}

		public String getElement() {
			return element;
		}

		public Node getNext() {
			return next;
		}

		public void setNext(Node n) {
			next = n;
		}

	}

	// ---- instance variables ----
	private Node head;
	private int size;

	// ---- access methods -----
	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public String first() {
		if (isEmpty()) {
			return null;
		}
		return head.getElement();
	}

	// ---- update methods ----
	public boolean addFirst(String token) {
		if (token == null) {
			return false;
		}
		head = new Node(token, head);
		size++;
		return true;
	}

	// ---- helper methods ----

	public boolean moveUp(String target, int loc) {
		// Checks that the string is not null
		if (target == null) {
			return false;
		}
		// If the list is empty or if the element does not already exist in a
		// non-empty list, then it is added to the front of the list
		if (isEmpty() || loc == -1) {
			head = new Node(target, head);
			size++;
			return true;
		}
		// If the element is the first element in the list, then it does not
		// need to be repositioned
		if (loc == 1) {
			return true;
		} else { // The element is in the list but is not in the first position.
					// Therefore, it must be removed from it's original location
					// and added to the front of the list 
			Node p = head;
			Node f = null;
			for (int i = 1; i < loc; i++) {
				f = p;
				p = p.getNext();
			}
			f.setNext(p.next);
			head = new Node(target, head);
			return true;
		}

	}

	// Finds the position of an element in the frequencyStack
	public int find(String target) {
		if (target == null) {
			throw new IllegalArgumentException("Null target");
		}
		int i = 1;
		for (Node p = head; p != null; p = p.next) {
			String element = p.getElement();
			if (element != null) {
				if (element.equals(target)) {
					return i;
				}
				i++;
			}
		}
		return -1;

	}

	// Prints the FrequencyStack for testing purposes //
	public void printStack() {
		System.out.print("{");
		Node p = head;
		while (p != null) {
			System.out.print(p.getElement() + ", ");
			p = p.next;
		}
		System.out.print("}");
	}

	public String bringBack(int r) {
		// create a pointer to head
		Node p = head;
		for (int i = 0; i < r - 1; i++) {
			p = p.getNext();
		}
		return p.getElement();
	}

}