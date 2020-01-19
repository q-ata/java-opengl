import org.joml.Vector3f;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL33;
import org.lwjgl.opengl.GLCapabilities;

import java.util.*;

public class Game {

  private static Game instance;

  public static Game game() {
    return instance;
  }

  public static void reset() {
    instance = new Game();
  }

  private List<MapItem> items = new ArrayList<>();
  private Map<MapItem, List<MapItemInstance>> instances = new HashMap<>();
  private Map<Class<? extends MapItemInstance>, Integer> mapping = new HashMap<>();
  private List<MapItemInstance> all = new ArrayList<>();

  private PlayerMovementHandler keyHandler;
  private MouseMovementHandler mouseHandler;
  private MouseClickHandler clickHandler;
  private CameraInstance player;
  private Options options;

  public void start() {

    Game game = game();
    game.loadOptions();
    Window window = game.initOpenGL();
    game.constructMap();

    Camera cam = new Camera();
    game.addItem(cam);
    game.addInstance(new Vector3f(5f, 2f, 5f), cam.id());

    for (MapItem i : GameConstants.ALL_ENEMIES) {
      i.reset();
      game.addItem(i);
    }

    int err;
    while((err = GL33.glGetError()) != GL33.GL_NO_ERROR) {
      System.out.println(err);
    }

    Arrays.fill(KeyboardInputHandler.KEYS, false);
    for (GameEvent ev : GameConstants.ALL_EVENTS) {
      ev.reset();
    }
    StaticDraw.reset();
    Shaders.RENDERER.reset();
    Shaders.QUAD_RENDERER.reset();
    for (Sprite s : Sprite.values()) {
      s.reset();
    }

    // Background clear color.
    window.setClearColor(0.0f, 1.0f, 0.0f, 0.0f);
    while (!GLFW.glfwWindowShouldClose(window.get())) {

      window.clear();

      if (KeyboardInputHandler.KEYS[256]) {
        GLFW.glfwSetWindowShouldClose(window.get(), true);
      }

      for (GameEvent ev : GameConstants.ALL_EVENTS) {
        ev.run(game);
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

  public void loadOptions() {
    if (options != null) {
      return;
    }
    options = new Options(GameConstants.OPTIONS_PATH);
  }

  public Window initOpenGL() {
    GLFWErrorCallback.createPrint(System.err).set();

    if (!GLFW.glfwInit()) {
      Logger.error(getClass(), "Failed to initialize GLFW.");
      return null;
    }

    // Configure GLFW settings.
    GLFW.glfwDefaultWindowHints();
    GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
    // Make the window resizable.
    GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);

    // Create a new window.
    Window window = new Window(GameConstants.WINDOW_WIDTH, GameConstants.WINDOW_HEIGHT, "A window name.");
    GLFW.glfwMakeContextCurrent(window.get());
    GLCapabilities capabilities = GL.createCapabilities();
    if (!capabilities.OpenGL33) {
      Logger.error(getClass(), "OpenGL version 3.3 is not supported.");
      return null;
    }

    GL33.glEnable(GL33.GL_DEPTH_TEST);

    // Enable v-sync.
    GLFW.glfwSwapInterval(1);
    // Show the window.
    GLFW.glfwShowWindow(window.get());

    GL33.glUniform1i(GL33.glGetUniformLocation(Shaders.RENDERER.get(), "texData"), 0);

    KeyboardInputHandler keyHandler = new KeyboardInputHandler();
    GLFW.glfwSetKeyCallback(window.get(), keyHandler);
    this.keyHandler = new PlayerMovementHandler(keyHandler);
    mouseHandler = new MouseMovementHandler();
    GLFW.glfwSetCursorPosCallback(window.get(), mouseHandler);
    clickHandler = new MouseClickHandler();
    GLFW.glfwSetMouseButtonCallback(window.get(), clickHandler);
    GLFW.glfwSetInputMode(window.get(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);

    return window;
  }

  public void constructMap() {
    Wall wall = new Wall();
    addItem(wall);
    for (int i = -20; i < 30; i += 10) {
      for (int j = -20; j < 30; j += 10) {
        addInstance(new Vector3f(i, -5f, j), wall.id());
      }
    }
    for (int i = -20; i < 30; i += 10) {
      addInstance(new Vector3f(i, 5f, -20f), wall.id());
    }
    for (int i = -20; i < 30; i += 10) {
      addInstance(new Vector3f(-20f, 5f, i), wall.id());
    }
    for (int i = -20; i < 30; i += 10) {
      addInstance(new Vector3f(i, 5f, 20f), wall.id());
    }
    for (int i = -20; i < 30; i += 10) {
      addInstance(new Vector3f(20f, 5f, i), wall.id());
    }
    PineappleEasterEgg perry = new PineappleEasterEgg();
    addItem(perry);
    addInstance(new Vector3f(20f, 2f, 10f), perry.id());
  }

  /**
   * Create a VAO to represent all instances of the specified MapItem.
   * @param item The MapItem to generate a VAO for.
   * @return The handle to the VAO.
   */
  public int genBinding(MapItem item) {
    int vao = GL33.glGenVertexArrays();
    GL33.glBindVertexArray(vao);
    int vbo  = GL33.glGenBuffers();
    int vboi = GL33.glGenBuffers();
    GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, vbo);
    GL33.glBufferData(GL33.GL_ARRAY_BUFFER, item.getVertices(), GL33.GL_STATIC_DRAW);
    GL33.glBindBuffer(GL33.GL_ELEMENT_ARRAY_BUFFER, vboi);
    GL33.glBufferData(GL33.GL_ELEMENT_ARRAY_BUFFER, item.getIndices(), GL33.GL_STATIC_DRAW);
    GL33.glVertexAttribPointer(0, 3, GL33.GL_FLOAT, false, 20, 0);
    GL33.glEnableVertexAttribArray(0);
    GL33.glVertexAttribPointer(1, 2, GL33.GL_FLOAT, false, 20, 12);
    GL33.glEnableVertexAttribArray(1);
    // Unbind buffers.
    GL33.glBindVertexArray(0);
    GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, 0);
    GL33.glBindBuffer(GL33.GL_ELEMENT_ARRAY_BUFFER, 0);
    return vao;
  }

  /**
   * Register a new MapItem with the Game object.
   * @param instance The MapItem to register.
   */
  public void addItem(MapItem instance) {
    items.add(instance);
    instances.put(instance, new ArrayList<>());
    instance.setIdentifier(items.size() - 1);
    mapping.put(instance.getType(), instance.id());
  }

  /**
   * Create a new MapItemInstance and register it with the Game object.
   * @param pos The location of the instance in 3D world space.
   * @param id The unique identifer of the MapItem.
   */
  public MapItemInstance addInstance(Vector3f pos, int id) {
    MapItemInstance instance = items.get(id).create(pos);
    if (instance instanceof CameraInstance) {
      if (player != null) {
        Logger.error(getClass(), "Tried to create player instance but one already exists.");
        return null;
      }
      player = (CameraInstance) instance;
    }
    all.add(instance);
    instances.get(items.get(id)).add(instance);
    return instance;
  }

  public void removeInstance(int id) {
    all.remove(id);
  }

  public void cleanInstance(int id, int ind) {
    instances.get(items.get(id)).remove(ind);
  }

  /**
   * Retrieve the MapItem object associated with this instance.
   * @param instance The MapItemInstance to retrieve the MapItem for.
   * @return The singleton MapItem that represents all such provided instances.
   */
  public MapItem retrieve(MapItemInstance instance) {
    return items.get(mapping.get(instance.getClass()));
  }

  /**
   * Get a read-only list of all instances of a MapItem.
   * @param id THe unique identifier that represents the MapItem.
   * @return The list of MapItem instances.
   */
  public List<MapItemInstance> getInstances(int id) {
    return Collections.unmodifiableList(instances.get(items.get(id)));
  }

  /**
   * Get a read-only list of all MapItems.
   * @return The list of MapItems.
   */
  public List<MapItem> getItems() {
    return Collections.unmodifiableList(items);
  }

  /**
   * Get a read-only list of all MapItem instances of all types of MapItem.
   * @return The list.
   */
  public List<MapItemInstance> getAll() {
    return Collections.unmodifiableList(all);
  }

  public PlayerMovementHandler getKeyHandler() {
    return keyHandler;
  }

  public MouseMovementHandler getMouseHandler() {
    return mouseHandler;
  }

  public MouseClickHandler getClickHandler() {
    return clickHandler;
  }

  public CameraInstance getPlayer() {
    return player;
  }

  public float getOption(String name) {
    return options.get(name);
  }

}
