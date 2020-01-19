import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL33;

import java.util.List;

public class RenderHandler implements GameEvent {

  private Matrix4f proj;
  private Matrix4f mvp = new Matrix4f();

  @Override
  public void reset() {
    proj = new Matrix4f().perspective((float) Math.toRadians(Game.game().getOption("fov")), Game.game().getOption("ar"), 0.05f, 200.0f);
    mvp.identity();
  }

  @Override
  public void run(Game game) {

    CameraInstance camera = Game.game().getPlayer();

    mvp.identity();
    mvp.mul(proj);
    mvp.mul(camera.constructView());

    Shaders.RENDERER.use();
    for (MapItem item : game.getItems()) {
      item.bind();
      GL33.glBindVertexArray(item.vao());
      if (item.vao() != 0) {
        List<MapItemInstance> ins = game.getInstances(item.id());
        for (int i = 0; i < ins.size(); i++) {
          MapItemInstance instance = ins.get(i);
          if (instance.garbage()) {
            game.cleanInstance(item.id(), i);
          }
          Matrix4f insMvp = new Matrix4f(mvp);
          Matrix4f instanceMat = new Matrix4f().translate(instance.world()).mul(item.model());
          insMvp.mul(instanceMat);
          Shaders.RENDERER.setUniformMatrix("mvp", insMvp);
          GL33.glDrawElements(GL33.GL_TRIANGLES, item.getIndices().length, GL33.GL_UNSIGNED_INT, 0);
        }
      }
    }

    StaticDraw.drawRect(0, 0f, 0.6f * (camera.getHealth() / 100f), 0.1f, Colors.RED);
    StaticDraw.drawRect(0, 0.18f, 0.5f * (camera.getSprint() / 400f), 0.05f, Colors.BLUE);
    StaticDraw.drawRect(0, 0, 0.02f, 0.02f, Colors.BLACK, true);

  }
}
