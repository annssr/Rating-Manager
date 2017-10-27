class HeapElem<T> {
	public double key;
	public T data;

	public HeapElem(double _key, T _data) {
		key = _key;
		data = _data;
	}
}

class Heap<T> {

	private int maxSize;
	private int size;
	private double[] keys;
	private T[] data;

	public Heap(int _maxSize) {

		maxSize = _maxSize;
		size = 0;
		keys = new double[maxSize + 1];
		data = (T[]) new Object[maxSize + 1];
	}

	public int size() {
		return size;
	}

	public boolean full() {
		return size == maxSize;
	}

	private void siftUp() {
		int k = size;
		while ((k > 1) && (keys[k] > keys[k / 2])) {

			// System.out.println(k);

			double tmpKey = keys[k];
			keys[k] = keys[k / 2];
			keys[k / 2] = tmpKey;

			T tmpData = data[k];
			data[k] = data[k / 2];
			data[k / 2] = tmpData;

			k = k / 2;
		}
	}

	public void insert(double key, T val) {

		size++;
		keys[size] = key;
		data[size] = val;
		siftUp();
	}

	private void siftDown(int i) {
		int k = i;
		while (((2 * k <= size) && (keys[k] < keys[2 * k])) || ((2 * k + 1 <= size) && (keys[k] < keys[2 * k + 1]))) {
			if ((2 * k + 1 <= size) && (keys[2 * k + 1] > keys[2 * k])) {

				double tmpKey = keys[k];
				keys[k] = keys[2 * k + 1];
				keys[2 * k + 1] = tmpKey;

				T tmpData = data[k];
				data[k] = data[2 * k + 1];
				data[2 * k + 1] = tmpData;

				k = 2 * k + 1;
			} else {

				double tmpKey = keys[k];
				keys[k] = keys[2 * k];
				keys[2 * k] = tmpKey;

				T tmpData = data[k];
				data[k] = data[2 * k];
				data[2 * k] = tmpData;

				k = 2 * k;
			}
		}
	}

	public HeapElem<T> removeRoot() {

		HeapElem<T> tmp = new HeapElem<T>(keys[1], data[1]);
		keys[1] = keys[size];
		data[1] = data[size];
		size--;
		siftDown(1);
		return tmp;
	}
}

public class PQ<T> {

	private Heap<T> heap;

	public PQ(int _maxSize) {
		heap = new Heap<T>(_maxSize);
	}

	public int length() {
		return heap.size();
	}

	public boolean full() {
		return heap.full();
	}

	public void enqueue(double pr, T val) {
		heap.insert(pr, val);
	}

	public PQElem<T> serve() {
		HeapElem<T> heapEl = heap.removeRoot();
		PQElem<T> pqEl = new PQElem<T>(heapEl.key, heapEl.data);
		return pqEl;
	}
}
