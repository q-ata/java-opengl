package game;

import constants.*;
import enemies.Apple;
import enemies.Pear;
import enemies.Squash;
import handlers.KeyboardInputHandler;
import handlers.MouseClickHandler;
import handlers.MouseMovementHandler;
import handlers.PlayerMovementHandler;
import items.Camera;
import items.CameraInstance;
import items.PineappleEasterEgg;
import items.Wall;
import org.joml.Vector3f;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL33;
import org.lwjgl.opengl.GLCapabilities;
import structs.*;

import java.util.*;

public class Game {

  // Singleton static Game instance.
  private static Game instance;

  /**
   * Get the currently playing Game.
   * @return The Game singleton.
   */
  public static Game game() {
    return instance;
  }

  // Reset the game.
  public static void reset() {
    instance = new Game();
  }

  // List of all MapItems.
  private List<MapItem> items = new ArrayList<>();
  // Map structs.MapItem to list of its instances.
  private Map<MapItem, List<MapItemInstance>> instances = new HashMap<>();
  // Map instance to its unique identifier.
  private Map<Class<? extends MapItemInstance>, Integer> mapping = new HashMap<>();
  // All drawable instances.
  private List<MapItemInstance> all = new ArrayList<>();
  // Player score.
  private int score = 0;

  // Input handlers.
  private PlayerMovementHandler keyHandler;
  private MouseMovementHandler mouseHandler;
  private MouseClickHandler clickHandler;

  private CameraInstance player;
  private Options options;

  /**
   * Start the game.
   */
  public void start() {

    Game game = game();
    game.loadOptions();
    Window window = game.initOpenGL();
    game.constructMap();

    Camera cam = new Camera();
    game.addItem(cam);
    game.addInstance(new Vector3f(0f, 2f, 0f), cam.id());

    // Load enemy MapItems.
    GameConfig.ALL_ENEMIES[0] = new Apple();
    GameConfig.ALL_ENEMIES[1] = new Squash();
    GameConfig.ALL_ENEMIES[2] = new Pear();
    for (MapItem item : GameConfig.ALL_ENEMIES) {
      game.addItem(item);
    }

    int err;
    while((err = GL33.glGetError()) != GL33.GL_NO_ERROR) {
      System.out.println(err);
    }

    Arrays.fill(KeyboardInputHandler.KEYS, false);
    // Reset GameEvents.
    for (GameEvent ev : GameConfig.ALL_EVENTS) {
      ev.reset();
    }
    // Reset shaders.
    StaticDraw.reset();
    Shaders.RENDERER.reset();
    Shaders.QUAD_RENDERER.reset();
    // Reset sprites.
    for (Sprite s : Sprite.values()) {
      s.reset();
    }

    // Background clear color.
    window.setClearColor(0.0f, 1.0f, 0.0f, 0.0f);
    while (!GLFW.glfwWindowShouldClose(window.get())) {

      window.clear();

      // Check if game should end.
      if (KeyboardInputHandler.KEYS[256] || getPlayer().getHealth() <= 0) {
        GLFW.glfwSetWindowShouldClose(window.get(), true);
      }

      // Run all GameEvents.
      for (GameEvent ev : GameConfig.ALL_EVENTS) {
        ev.run(game);
      }

      // Swap to other frame buffer.
      GLFW.glfwSwapBuffers(window.get());
      // Poll for window events and run callbacks.
      GLFW.glfwPollEvents();
    }

    // Cleanup.
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

  /**
   * Load options.
   */
  public void loadOptions() {
    if (options != null) {
      return;
    }
    options = new Options(GameConfig.OPTIONS_PATH);
  }

  /**
   * Prepare LWJGL and GLFW.
   * @return
   */
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
    Window window = new Window((int) Game.game().getOption("width"), (int) Game.game().getOption("height"), "Veggietales 2");
    GLFW.glfwMakeContextCurrent(window.get());
    GLCapabilities capabilities = GL.createCapabilities();
    if (!capabilities.OpenGL33) {
      Logger.error(getClass(), "OpenGL version 3.3 is not supported.");
      return null;
    }

    // Properly calculate z coordinates.
    GL33.glEnable(GL33.GL_DEPTH_TEST);

    // Enable v-sync.
    GLFW.glfwSwapInterval(1);
    // Show the window.
    GLFW.glfwShowWindow(window.get());

    GL33.glUniform1i(GL33.glGetUniformLocation(Shaders.RENDERER.get(), "texData"), 0);

    // Register input handlers.
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

  /**
   * Construct the immutable MapItems that form the playable map.
   */
  public void constructMap() {
    Wall wall = new Wall();
    addItem(wall);
    // Create the floor.
    for (int i = -20; i < 30; i += 10) {
      for (int j = -20; j < 30; j += 10) {
        addInstance(new Vector3f(i, -5f, j), wall.id());
      }
    }
    // Create each side of the wall.
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
    // Easter egg :)
    PineappleEasterEgg perry = new PineappleEasterEgg();
    addItem(perry);
    addInstance(new Vector3f(20f, 2f, 10f), perry.id());
  }

  /**
   * Create a VAO to represent all instances of the specified structs.MapItem. A Vertex Array Object stores all the calls needed to render a model.
   * Basic LWJGL rendering from https://learnopengl.com/Getting-started/Hello-Triangle
   * @param item The structs.MapItem to generate a VAO for.
   * @return The handle to the VAO.
   */
  public int genBinding(MapItem item) {
    int vao = GL33.glGenVertexArrays();
    GL33.glBindVertexArray(vao);
    // Vertex buffer object contains the coordinate information for the model.
    int vbo  = GL33.glGenBuffers();
    // Vertex buffer object indices information. The order in which to draw the vertices.
    int vboi = GL33.glGenBuffers();
    // Add the model's vertices to VBO.
    GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, vbo);
    GL33.glBufferData(GL33.GL_ARRAY_BUFFER, item.getVertices(), GL33.GL_STATIC_DRAW);
    // Add the model's vertex indices to VAO.
    GL33.glBindBuffer(GL33.GL_ELEMENT_ARRAY_BUFFER, vboi);
    GL33.glBufferData(GL33.GL_ELEMENT_ARRAY_BUFFER, item.getIndices(), GL33.GL_STATIC_DRAW);
    /*
    The index argument represents the attribute index in shader.
    The size argument is the number of components in each vector.
    The third argument is the type of component values.
    The fourth argument is whether to normalize the vector.
    The stride is how many bytes to move before encountering the start of the next vector.
    The pointer is the offset from the start of the vector to locate this index's information.
     */
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
   * Register a new structs.MapItem with the game.Game object.
   * @param instance The structs.MapItem to register.
   */
  public void addItem(MapItem instance) {
    items.add(instance);
    instances.put(instance, new ArrayList<>());
    // Assign unique ID to this structs.MapItem.
    instance.setIdentifier(items.size() - 1);
    mapping.put(instance.getType(), instance.id());
  }

  /**
   * Create a new structs.MapItemInstance and register it with the game.Game object.
   * @param pos The location of the instance in 3D world space.
   * @param id The unique identifer of the structs.MapItem.
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

  /**
   * Remove an instance from the list of all instances. The instance should be later cleaned from the instances map.
   * @param id Unique ID of the instance.
   */
  public void removeInstance(int id) {
    all.remove(id);
  }

  /**
   * Remove an instance from the instances map.
   * @param id Unique ID of the MapItem.
   * @param ind Index in the list value.
   */
  public void cleanInstance(int id, int ind) {
    instances.get(items.get(id)).remove(ind);
  }

  /**
   * Retrieve the structs.MapItem object associated with this instance.
   * @param instance The structs.MapItemInstance to retrieve the structs.MapItem for.
   * @return The singleton structs.MapItem that represents all such provided instances.
   */
  public MapItem retrieve(MapItemInstance instance) {
    return items.get(mapping.get(instance.getClass()));
  }

  /**
   * Get a read-only list of all instances of a structs.MapItem.
   * @param id THe unique identifier that represents the structs.MapItem.
   * @return The list of structs.MapItem instances.
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
   * Get a read-only list of all structs.MapItem instances of all types of structs.MapItem.
   * @return The list.
   */
  public List<MapItemInstance> getAll() {
    return Collections.unmodifiableList(all);
  }

  /**
   * Get keyboard input handler.
   * @return The handler.
   */
  public PlayerMovementHandler getKeyHandler() {
    return keyHandler;
  }

  /**
   * Get mouse movement handler.
   * @return The handler.
   */
  public MouseMovementHandler getMouseHandler() {
    return mouseHandler;
  }

  /**
   * Get mouse button handler.
   * @return The handler.
   */
  public MouseClickHandler getClickHandler() {
    return clickHandler;
  }

  /**
   * Get player.
   * @return The player.
   */
  public CameraInstance getPlayer() {
    return player;
  }

  /**
   * Get value of specific option.
   * @param name Name of option.
   * @return The value.
   */
  public float getOption(String name) {
    return options.get(name);
  }

  /**
   * Get current score.
   * @return The score.
   */
  public int getScore() {
    return score;
  }

  /**
   * Increase current score.
   * @param amt Amount to increase.
   */
  public void addScore(int amt) {
    score += amt;
  }

}
