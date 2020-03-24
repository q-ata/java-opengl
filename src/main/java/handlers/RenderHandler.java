package handlers;

import constants.Colors;
import constants.Shaders;
import constants.StaticDraw;
import game.Game;
import items.Camera;
import items.CameraInstance;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL33;
import structs.GameEvent;
import structs.MapItem;
import structs.MapItemInstance;

import java.util.List;

public class RenderHandler implements GameEvent {

  private Matrix4f proj;
  private Matrix4f mvp = new Matrix4f();

  @Override
  public void reset() {
    // Reset MVP matrices.
    /*
    The MVP matrix is the product of the Model, View and Projection matrices.
    The model matrix applies a translation for the instance's world coordinates.
    The view matrix clips out all objects not visible by the camera.
    The projection matrix applies a perspective effect where objects farther away appear smaller.
     */
    proj = new Matrix4f().perspective((float) Math.toRadians(Game.game().getOption("fov")), Game.game().getOption("ar"), 0.05f, 200.0f);
    mvp.identity();
  }

  /**
   * Renders all objects onto the window.
   * @param game The singleton game.Game object.
   */
  @Override
  public void run(Game game) {

    CameraInstance camera = Game.game().getPlayer();

    // Apply view and projection matrices.
    mvp.identity();
    mvp.mul(proj);
    mvp.mul(camera.constructView());

    Shaders.RENDERER.use();
    // Iterate over all MapItems
    for (MapItem item : game.getItems()) {
      if (item instanceof Camera) {
        continue;
      }
      item.bind();
      // Instruct OpenGL to use the current structs.MapItem's VAO.
      GL33.glBindVertexArray(item.vao());
      if (item.vao() != 0) {
        List<MapItemInstance> ins = game.getInstances(item.id());
        // Iterate over all instances.
        for (int i = 0; i < ins.size(); i++) {
          MapItemInstance instance = ins.get(i);
          // If marked for deletion, remove from list of instances.
          if (instance.garbage()) {
            game.cleanInstance(item.id(), i);
          }
          // Apply model matrix.
          Matrix4f insMvp = new Matrix4f(mvp);
          Matrix4f instanceMat = new Matrix4f().translate(instance.world()).mul(item.model());
          insMvp.mul(instanceMat);
          // Give MVP matrix to OpenGL.
          Shaders.RENDERER.setUniformMatrix("mvp", insMvp);
          // Draw.
          GL33.glDrawElements(GL33.GL_TRIANGLES, item.getIndices().length, GL33.GL_UNSIGNED_INT, 0);
        }
      }
    }

    // Draw HUD - health bar, sprint bar, crosshair.
    StaticDraw.drawRect(0, 0f, 0.6f * (camera.getHealth() / 100f), 0.1f, Colors.RED);
    StaticDraw.drawRect(0, 0.18f, 0.5f * (camera.getSprint() / 400f), 0.05f, Colors.BLUE);
    StaticDraw.drawRect(0, 0, 0.02f, 0.02f, Colors.BLACK, true);

  }
}
