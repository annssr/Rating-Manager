public class PQElem<T> {
	public double priority;
	public T data;

	public PQElem(double _priority, T _data) {
		priority = _priority;
		data = _data;
	}
}
