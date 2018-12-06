import static java.lang.Math.abs;

public class Hashtable <k,v> {

	public Hashtable(int newSize) {
	}

	// Data will be held in nodes
	class HashNode <k,v> {
		public k key;
		public v value;
		public HashNode<k,v> next;

		public HashNode(k key, v value) {
			this.key = key;
			this.value = value;
			this.next = null;
		}
	}

	private HashNode[] buckets; // Heads of key linked lists with sometimes matching hash codes
	private int size; // Number of items
	private int num_buckets; // Size of table

	public Hashtable() {
		this.buckets = new HashNode[2027]; // Feel free to choose whichever starting bucket list amount you'd like
		this.size = 0;
		this.num_buckets = 2027; // Make sure this matches length of list
	}


	public Hashtable(int newSize, int itemCount) {
		this.buckets = new HashNode[newSize]; // When growing the array, you will have to change the size
		this.size = itemCount; // Amount of items in structure 
		this.num_buckets = newSize; // Keep this the same as array length
	}


	// Check for key in structure
	public boolean containsKey(String key) {
		int bucket_id = getBucket(key); // Get the index 
		HashNode temp = buckets[bucket_id]; // Temporary head of matching key index

		// Iterate through the linked list until the head is null
		while (temp != null) {
			// If the key is found, return true
			if (temp.key == key)
				return true;
			else
				temp = temp.next;
		}

		// If it is made to this point, that means the key is not present in the structure
		// Therfore return false
		return false;
	}

	// Return the value if found
	// Essentially the same as containsKey, except we're returning the value if it's found instead of true
	public String get(String key) {
		int bucket_id = getBucket(key);
		HashNode temp = buckets[bucket_id];
		while (temp != null) {
			if (temp.key == key)
				return temp.value.toString();
			else
				temp = temp.next;
		}
		return null;

	}

	// Insert an item into the structure
	public void put(String key, String value) {
		int bucket_id = getBucket(key); // Get the index it should go in
		HashNode curr = buckets[bucket_id]; // Get the head of that index, if there are items already there
		
		// THis will iterate through the list until there is a spot
		// Note: IF it starts off null, then the new item will become the head
		while (curr != null) {
			if (curr.key == key) {
				curr.value = value;
				return;
			}
			curr = curr.next;
		}
		
		curr = new HashNode(key, value);
		curr.next = buckets[bucket_id];
		buckets[bucket_id] = curr;
		++size;

		// Keep the perforance up by keeping landam, or lamdum, or idk how to spell it.
		// Just keep the ratio below .5 to ensure good perforance
		if (((1.0 * size) / (1.0 * num_buckets)) > .5) {
			grow_array();
		}
	}

	// Remove an item
	public String remove(String key) throws NotFoundException {
		int bucket_id = getBucket(key); // Get the index of the item
		HashNode curr = buckets[bucket_id]; // Get the head of that
		HashNode previous = null; // Keep track of what is behind the current node
		boolean found = false; // Flag variable for when the value is found, if it is found

		while (!found && curr != null) { // Iterate through the linked list until key is found or there are no more items
			if (curr.key == key)
				found = true; // If it's found, set found to true
			if (!found) { // Otherwise, keep iterating
				previous = curr;
				curr = curr.next;
			}
		}

		if (curr == null) // Key was never in list
			throw new NotFoundException("Key was not found!");
		else if (previous == null) // Deleting the head
			buckets[bucket_id] = curr.next;	
		else // Deleting a node betweeen nodes
			previous.next = curr.next;

		return curr.value.toString(); // Return the value
	}

	// Simple, just getting a value and modding it so that it can be applied to the current array 
	private int getBucket(String key) {
		return abs(key.hashCode() % num_buckets);
	}

	// Grow the array
	private void grow_array() {
		// The new array will need a bigger size
		int newSize;

		// This may or may not have been necessary. While testing the speed of this algorithm in intellij,
		// I came to find out that it was way too high. But later in terminal, I realized it was an intellij problem
		if (num_buckets < 400)
			newSize = 400;
		else if (num_buckets < 16000)
			newSize = 16000;
		else if (num_buckets < 640000)
			newSize = 640000;
		else if (num_buckets < 256000000)
			newSize = 25600000;
		else
			newSize = (int)(num_buckets * 20);

		// Temporary structure that will become new structure
		Hashtable temp = new Hashtable(newSize);
		
		// Copy data
		for (int i = 0; i < num_buckets; i++) {
			HashNode curr = buckets[i];
			while(curr != null) {
				temp.put(curr.key.toString(), curr.value.toString());
				curr = curr.next;
			}
		}

		// Assign the data of the new structure to the old structure	
		this.buckets = temp.buckets;
		this.num_buckets = temp.num_buckets;
		this.size = temp.size;
	}


}
