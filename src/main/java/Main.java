import org.joml.Matrix4f;
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
    GLCapabilities capabilities = GL.createCapabilities();
    if (!capabilities.OpenGL33) {
      Logger.error(getClass(), "OpenGL version 3.3 is not supported.");
      return;
    }

    GL33.glEnable(GL33.GL_DEPTH_TEST);
    
    // Enable v-sync.
    // GLFW.glfwSwapInterval(1);
    // Show the window.
    GLFW.glfwShowWindow(window.get());

    GL33.glUniform1i(GL33.glGetUniformLocation(Shaders.RENDERER.get(), "texData"), 0);

    Game game = Game.INSTANCE;
    DonaldTrump trump = new DonaldTrump();
    Camera cam = new Camera();
    Game.INSTANCE.addItem(trump);
    Game.INSTANCE.addInstance(new Vector3f(-8.0f, 0.0f, 0.0f), trump.id());
    Game.INSTANCE.addInstance(new Vector3f(4f, 0.0f, 0.0f), trump.id());
    Game.INSTANCE.addItem(cam);
    Game.INSTANCE.addInstance(new Vector3f(4f, 0.0f, 2.0f), cam.id());

    Wall wall = new Wall();
    Game.INSTANCE.addItem(wall);
    Game.INSTANCE.addInstance(new Vector3f(1f, -1.1f, 0f), wall.id());

    CameraInstance camera = (CameraInstance) Game.INSTANCE.getInstances(cam.id()).get(0);
    /*
    Wall wall = new Wall();
    wall.addInstance(new WallInstance(new Vector3f(0.5f, 0.0f, 0.0f)));
    game.getItems().add(wall);
    wall.setVao(game.genBinding(wall));
    */
    int err;
    while((err = GL33.glGetError()) != GL33.GL_NO_ERROR) {
      System.out.println(err);
    }

    KeyboardInputHandler keyHandler = new KeyboardInputHandler();
    GLFW.glfwSetKeyCallback(window.get(), keyHandler);
    PlayerMovementHandler moveHandler = new PlayerMovementHandler(keyHandler);
    GLFW.glfwSetCursorPosCallback(window.get(), new MouseMovementHandler(camera));
    GLFW.glfwSetInputMode(window.get(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);

    Matrix4f mvp = new Matrix4f();
    // TODO: Adjust projection matrix when window is resized.
    Matrix4f proj = new Matrix4f().perspective((float) Math.toRadians(50.0f), (float) window.getWidth() / window.getHeight(), 0.1f, 100.0f);

    int frames = 0;
    long delta = System.currentTimeMillis();
    long current;
    // Background clear color.
    window.setClearColor(0.0f, 1.0f, 0.0f, 0.0f);
    while (!GLFW.glfwWindowShouldClose(window.get())) {

      if (KeyboardInputHandler.KEYS[256]) {
        GLFW.glfwSetWindowShouldClose(window.get(), true);
      }

      Game.INSTANCE.adjust();
      Vector3f v = moveHandler.calculateVelocity(camera.target(), 0.003f * Game.INSTANCE.getAdjustment());
      camera.setVel(v.add(0, camera.vel().y, 0));

      //game.getInstances(trump.id()).get(0).setVel(new Vector3f(0.0002f, 0f, 0f));
      for (MapItemInstance instance : game.getAll()) {
        instance.onTick();
      }
      for (int i = 0; i < game.getAll().size(); i++) {
        for (int j = i + 1; j < game.getAll().size(); j++) {
          MapItemInstance a = game.getAll().get(i);
          MapItemInstance b = game.getAll().get(j);
          if (Collision.collision(a.world(), game.retrieve(a), b.world(), game.retrieve(b))) {
            a.onCollision(b);
            b.onCollision(a);
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

      frames++;
      if ((current = System.currentTimeMillis()) >= delta + 1000) {
        System.out.println(frames + " FPS");
        frames = 0;
        delta = current;
      }
      // Swap to other frame buffer.
      GLFW.glfwSwapBuffers(window.get());
      // Poll for window events and run callbacks.
      GLFW.glfwPollEvents();
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