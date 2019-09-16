import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL33;

public class Main {

  private void start() {
    GLFWErrorCallback.createPrint(System.err).set();

    if (!GLFW.glfwInit())
      throw new IllegalStateException("Unable to initialize GLFW");

    // Configure GLFW settings.
    GLFW.glfwDefaultWindowHints();
    GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
    // Make the window resizable.
    GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);

    // Create a new window.
    Window window = new Window(800, 600, "A window name.");
    GLFW.glfwMakeContextCurrent(window.get());
    GL.createCapabilities();

    GL33.glEnable(GL33.GL_DEPTH_TEST);
    
    // Enable v-sync.
    GLFW.glfwSwapInterval(1);
    // Show the window.
    GLFW.glfwShowWindow(window.get());

    Shader shader = new Shader("./res/basic_shader.vs", "./res/basic_shader.fs");
    shader.use();
    GL33.glUniform1i(GL33.glGetUniformLocation(shader.get(), "texData"), 0);
    DonaldTrump trump = new DonaldTrump();
    int vao = GL33.glGenVertexArrays();
    GL33.glBindVertexArray(vao);
    int vbo = GL33.glGenBuffers();
    GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, vbo);

    GL33.glBufferData(GL33.GL_ARRAY_BUFFER, trump.getVertices(), GL33.GL_STATIC_DRAW);
    GL33.glVertexAttribPointer(0, 3, GL33.GL_FLOAT, false, 20, 0);
    GL33.glEnableVertexAttribArray(0);
    GL33.glVertexAttribPointer(1, 2, GL33.GL_FLOAT, false, 20, 12);
    GL33.glEnableVertexAttribArray(1);
    GL33.glBindVertexArray(0);
    
    int err;
    while((err = GL33.glGetError()) != GL33.GL_NO_ERROR) {
      System.out.println(err);
    }

    Camera camera = new Camera();

    GLFW.glfwSetKeyCallback(window.get(), new KeyboardInputHandler());
    GLFW.glfwSetCursorPosCallback(window.get(), new MouseMovementHandler(camera));
    GLFW.glfwSetInputMode(window.get(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);

    Matrix4f mvp;
    Matrix4f proj = new Matrix4f().perspective((float) Math.toRadians(45.0f), (float) window.getWidth() / window.getHeight(), 0.1f, 100.00f);

    // Background clear color.
    window.setClearColor(0.0f, 1.0f, 0.0f, 0.0f);
    while (!GLFW.glfwWindowShouldClose(window.get())) {

      if (KeyboardInputHandler.KEYS[256]) {
        GLFW.glfwSetWindowShouldClose(window.get(), true);
      }
      if (KeyboardInputHandler.KEYS['W']) {
        camera.addPos(new Vector3f(0.0f, 0.0f, -0.02f));
      }
      if (KeyboardInputHandler.KEYS['A']) {
        camera.addPos(new Vector3f(-0.02f, 0.0f, 0.0f));
      }
      if (KeyboardInputHandler.KEYS['S']) {
        camera.addPos(new Vector3f(0.0f, 0.0f, 0.02f));
      }
      if (KeyboardInputHandler.KEYS['D']) {
        camera.addPos(new Vector3f(0.02f, 0.0f, 0.0f));
      }
      
      // Clear frame buffers.
      window.clear();

      shader.use();
      trump.bind();
      GL33.glBindVertexArray(vao);

      mvp = new Matrix4f();
      proj.mul(camera.constructView(), mvp);
      mvp.mul(trump.model());
      shader.setUniformMatrix("mvp", mvp);
      GL33.glDrawElements(GL33.GL_TRIANGLES, trump.getIndices());

      // Swap to other frame buffer.
      GLFW.glfwSwapBuffers(window.get());
      // Poll for window events and run callbacks.
      GLFW.glfwPollEvents();
    }

    Callbacks.glfwFreeCallbacks(window.get());
    GLFW.glfwDestroyWindow(window.get());

    GLFW.glfwTerminate();
    GLFW.glfwSetErrorCallback(null).free();
  }

  public static void main(String[] args) {
    new Main().start();
  }

}