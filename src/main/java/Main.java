import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL33;
import org.lwjgl.opengl.GLCapabilities;

public class Main {

  private void start() {

    GLFWErrorCallback.createPrint(System.err).set();

    if (!GLFW.glfwInit()) {
      Logger.error(getClass(), "Failed to initialize GLFW.");
      return;
    }

    // Configure GLFW settings.
    GLFW.glfwDefaultWindowHints();
    GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
    // Make the window resizable.
    GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);

    // Create a new window.
    Window window = new Window(960, 540, "A window name.");
    GLFW.glfwMakeContextCurrent(window.get());
    GLCapabilities capabilities = GL.createCapabilities();
    if (!capabilities.OpenGL33) {
      Logger.error(getClass(), "OpenGL version 3.3 is not supported.");
      return;
    }

    GL33.glEnable(GL33.GL_DEPTH_TEST);
    
    // Enable v-sync.
    GLFW.glfwSwapInterval(1);
    // Show the window.
    GLFW.glfwShowWindow(window.get());

    GL33.glUniform1i(GL33.glGetUniformLocation(Shaders.RENDERER.get(), "texData"), 0);

    Game game = Game.INSTANCE;
    Apple apple = new Apple();
    Camera cam = new Camera();
    Game.INSTANCE.addItem(apple);
    Game.INSTANCE.addInstance(new Vector3f(14f, 2f, 10.0f), apple.id());
    Game.INSTANCE.addItem(cam);
    Game.INSTANCE.addInstance(new Vector3f(15f, 2f, 15f), cam.id());

    Wall wall = new Wall();
    Game.INSTANCE.addItem(wall);
    Game.INSTANCE.addInstance(new Vector3f(10f, 0f, 10f), wall.id());
    Game.INSTANCE.addInstance(new Vector3f(20.1f, 0f, 10f), wall.id());
    Game.INSTANCE.addInstance(new Vector3f(10f, 0f, 20.1f), wall.id());
    Game.INSTANCE.addInstance(new Vector3f(20.1f, 0f, 20.1f), wall.id());

    CameraInstance camera = (CameraInstance) Game.INSTANCE.getInstances(cam.id()).get(0);

    int err;
    while((err = GL33.glGetError()) != GL33.GL_NO_ERROR) {
      System.out.println(err);
    }

    KeyboardInputHandler keyHandler = new KeyboardInputHandler();
    GLFW.glfwSetKeyCallback(window.get(), keyHandler);
    PlayerMovementHandler moveHandler = new PlayerMovementHandler(keyHandler);
    Game.INSTANCE.setKeyboardHandler(moveHandler);
    GLFW.glfwSetCursorPosCallback(window.get(), new MouseMovementHandler(camera));
    MouseClickHandler mouseHandler = new MouseClickHandler();
    GLFW.glfwSetMouseButtonCallback(window.get(), mouseHandler);
    Game.INSTANCE.setClickHandler(mouseHandler);
    GLFW.glfwSetInputMode(window.get(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);

    FrameCounter fps = new FrameCounter();
    Matrix4f mvp = new Matrix4f();
    // TODO: Adjust projection matrix when window is resized.
    Matrix4f proj = new Matrix4f().perspective((float) Math.toRadians(60.0f), (float) window.getWidth() / window.getHeight(), 0.05f, 200.0f);

    // Background clear color.
    window.setClearColor(0.0f, 1.0f, 0.0f, 0.0f);
    while (!GLFW.glfwWindowShouldClose(window.get())) {

      if (KeyboardInputHandler.KEYS[256]) {
        GLFW.glfwSetWindowShouldClose(window.get(), true);
      }

      for (int i = 0; i < game.getAll().size(); i++) {
        game.getAll().get(i).behaviour();
      }
      for (int i = 0; i < game.getAll().size(); i++) {
        game.getAll().get(i).onTick();
      }
      for (int i = 0; i < game.getAll().size(); i++) {
        for (int j = i + 1; j < game.getAll().size(); j++) {
          MapItemInstance a = game.getAll().get(i);
          MapItemInstance b = game.getAll().get(j);
          if (Collision.collision(a.world(), game.retrieve(a), b.world(), game.retrieve(b)) && a.onCollision(b) && b.onCollision(a)) {
            a.onCollisionResolution(b);
            b.onCollisionResolution(a);
          }
        }

      }
      for (MapItemInstance instance : game.getAll()) {
        instance.onGravity();
      }

      // Clear frame buffers.
      window.clear();

      mvp.identity();
      mvp.mul(proj);
      mvp.mul(camera.constructView());

      Shaders.RENDERER.use();
      for (MapItem item : game.getItems()) {
        item.bind();
        GL33.glBindVertexArray(item.vao());
        if (item.vao() != 0) {
          for (MapItemInstance instance : game.getInstances(item.id())) {
            Matrix4f insMvp = new Matrix4f(mvp);
            Matrix4f instanceMat = new Matrix4f().translate(instance.world()).mul(item.model());
            insMvp.mul(instanceMat);
            Shaders.RENDERER.setUniformMatrix("mvp", insMvp);
            GL33.glDrawElements(GL33.GL_TRIANGLES, item.getIndices().length, GL33.GL_UNSIGNED_INT, 0);
          }
        }
      }

      StaticDraw.drawRect(0, 0, 0.6f * (camera.getHealth() / 100f), 0.1f, Colors.RED);
      StaticDraw.drawRect(0, 0, 0.02f, 0.02f, Colors.BLACK, true);

      // Swap to other frame buffer.
      GLFW.glfwSwapBuffers(window.get());
      // Poll for window events and run callbacks.
      GLFW.glfwPollEvents();

      if (fps.increment()) {
        System.out.println(fps.get() + " FPS");
      }
    }

    Callbacks.glfwFreeCallbacks(window.get());
    GLFW.glfwDestroyWindow(window.get());

    GLFW.glfwTerminate();

    GLFWErrorCallback cb = GLFW.glfwSetErrorCallback(null);
    if (cb != null) {
      cb.free();
    }
    else {
      Logger.error(getClass(), "Could not free error callback buffer.");
    }
  }

  public static void main(String[] args) {
    new Main().start();
  }

}