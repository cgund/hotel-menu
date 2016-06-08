package menu;

/*
Class to encapsulate two elements that have a natural pair structure
*/
public class Pair<T,E>
{
  private final T left;
  private final E right;

  public Pair(T left, E right) 
  {
    this.left = left;
    this.right = right;
  }

  public T getLeft() { return left; }
  public E getRight() { return right; }
}
