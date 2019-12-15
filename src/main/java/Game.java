import org.joml.Vector3f;
import org.lwjgl.opengl.GL33;

import java.util.*;

public class Game {

  public static final Game INSTANCE = new Game();

  private List<MapItem> items = new ArrayList<>();
  private Map<MapItem, List<MapItemInstance>> instances = new HashMap<>();
  private Map<Class<? extends MapItemInstance>, Integer> mapping = new HashMap<>();
  private List<MapItemInstance> all = new ArrayList<>();

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

  public void addItem(MapItem instance) {
    items.add(instance);
    instances.put(instance, new ArrayList<>());
    instance.setIdentifier(items.size() - 1);
    mapping.put(instance.getType(), instance.id());
  }

  public void addInstance(Vector3f pos, int id) {
    MapItemInstance instance = items.get(id).create(pos);
    all.add(instance);
    instances.get(items.get(id)).add(instance);
  }

  public MapItem retrieve(MapItemInstance instance) {
    return items.get(mapping.get(instance.getClass()));
  }

  public List<MapItemInstance> getInstances(int id) {
    return Collections.unmodifiableList(instances.get(items.get(id)));
  }

  public List<MapItem> getItems() {
    return Collections.unmodifiableList(items);
  }

  public List<MapItemInstance> getAll() {
    return Collections.unmodifiableList(all);
  }

}
