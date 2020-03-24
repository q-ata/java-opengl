package structs;

/**
 * Represents an object with a unique identifier that can be passed to OpenGL.
 */
public abstract class Handle {

  protected int handle;
  
  /**
   * Get the OpenGL assigned identifier for this object.
   * @return
   */
  public int get() {
    return handle;
  }
  
}
