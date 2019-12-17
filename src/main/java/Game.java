import org.joml.Vector3f;
import org.lwjgl.opengl.GL33;

import java.util.*;

public class Game {

  public static final Game INSTANCE = new Game();

  private List<MapItem> items = new ArrayList<>();
  private Map<MapItem, List<MapItemInstance>> instances = new HashMap<>();
  private Map<Class<? extends MapItemInstance>, Integer> mapping = new HashMap<>();
  private List<MapItemInstance> all = new ArrayList<>();

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
  public void addInstance(Vector3f pos, int id) {
    MapItemInstance instance = items.get(id).create(pos);
    all.add(instance);
    instances.get(items.get(id)).add(instance);
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

}
