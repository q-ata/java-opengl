import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL33;
import org.lwjgl.system.MemoryUtil;

/**
 * A new window to render on.
 */
public class Window {

  private String title;
  private int width;
  private int height;
  private long handle;
  
  /**
   * Construct a new window.
   * @param width
   * @param height
   * @param title
   */
  public Window(int width, int height, String title) {
    this.width = width;
    this.height = height;
    this.title = title;
    handle = GLFW.glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL);
    GLFW.glfwSetFramebufferSizeCallback(handle, new WindowResizeCallback(this));
    center();
  }
  
  /**
   * Get this window's ID.
   * @return
   */
  public long get() {
    return handle;
  }
  
  /**
   * Center this window's location on the screen.
   */
  public void center() {
    // Get monitor resolution.
    GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
    GLFW.glfwSetWindowPos(handle, (vidmode.width() - width) / 2, (vidmode.height() - height) / 2);
  }
  
  /**
   * Set the color used to clear the window.
   * @param r
   * @param g
   * @param b
   * @param a
   */
  public void setClearColor(float r, float g, float b, float a) {
    GL33.glClearColor(r, g, b, a);
  }
  
  /**
   * Clear color and depth buffer.
   */
  public void clear() {
    GL33.glClear(GL33.GL_COLOR_BUFFER_BIT | GL33.GL_DEPTH_BUFFER_BIT);
  }
  
  /**
   * Get width of this window.
   * @return
   */
  public int getWidth() {
    return width;
  }
  
  /**
   * Set width of this window.
   * @param width
   */
  public void setWidth(int width) {
    this.width = width;
  }
  
  /**
   * Get height of this window.
   * @return
   */
  public int getHeight() {
    return height;
  }
  
  /**
   * Set height of this window.
   * @param height
   */
  public void setHeight(int height) {
    this.height = height;
  }
  
  /**
   * Get title of this window.
   * @return
   */
  public String getTitle() {
    return title;
  }
  
}
