import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

/**
 * Class representing pair of Writables.
 */
public class PairOfWritables<L extends Writable, R extends Writable> implements Writable {

  private L leftElement;
  private R rightElement;

  /**
   * Creates a new <code>PairOfWritables</code>.
   */
  public PairOfWritables() {
  }

  /**
   * Creates a new <code>PairOfWritables</code>.
   */
  public PairOfWritables(L left, R right) {
    leftElement = left;
    rightElement = right;
  }

  /**
   * Deserializes the pair.
   *
   * @param in source for raw byte representation
   */
  @Override
  @SuppressWarnings("unchecked")
  public void readFields(DataInput in) throws IOException {
    String keyClassName = in.readUTF();
    String valueClassName = in.readUTF();

    try {
      Class<L> keyClass = (Class<L>) Class.forName(keyClassName);
      leftElement = (L) keyClass.newInstance();
      Class<R> valueClass = (Class<R>) Class.forName(valueClassName);
      rightElement = (R) valueClass.newInstance();

      leftElement.readFields(in);
      rightElement.readFields(in);
    } catch (Exception e) {
      throw new RuntimeException("Unable to create PairOfWritables!");
    }
  }

  /**
   * Serializes this pair.
   *
   * @param out where to write the raw byte representation
   */
  @Override
  public void write(DataOutput out) throws IOException {
    out.writeUTF(leftElement.getClass().getCanonicalName());
    out.writeUTF(rightElement.getClass().getCanonicalName());

    leftElement.write(out);
    rightElement.write(out);
  }

  /**
   * Returns the left element.
   *
   * @return the left element
   */
  public L getLeftElement() {
    return leftElement;
  }

  /**
   * Returns the right element.
   *
   * @return the right element
   */
  public R getRightElement() {
    return rightElement;
  }

  /**
   * Returns the key (left element).
   *
   * @return the key
   */
  public L getKey() {
    return leftElement;
  }

  /**
   * Returns the value (right element).
   *
   * @return the value
   */
  public R getValue() {
    return rightElement;
  }

  /**
   * Sets the right and left elements of this pair.
   *
   * @param left the left element
   * @param right the right element
   */
  public void set(L left, R right) {
    leftElement = left;
    rightElement = right;
  }

  /**
   * Generates human-readable String representation of this pair.
   *
   * @return human-readable String representation of this pair
   */
  public String toString() {
    return "(" + leftElement + ", " + rightElement + ")";
  }
}