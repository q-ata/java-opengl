package structs;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import constants.Logger;
import org.lwjgl.opengl.GL33;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryUtil;

/**
 * Represents textures that can be applied to models.
 */
public enum Sprite implements Resettable {
  
  APPLE("./res/apple.png"),
  CAMERA(""),
  WALL("./res/wall.png"),
  PINEAPPLE("./res/pineapple.png"),
  GRAPE("./res/grape.png"),
  SQUASH("./res/squash.png"),
  PEAR("./res/pear.png");

  private int handle;
  private String source;

  /**
   * Construct a new texture with the given source image.
   * @param source Path to the image.
   */
  private Sprite(String source) {
    this.source = source;
  }

  // Reload all sprites. Texture loading adapted from https://learnopengl.com/Getting-started/Textures
  @Override
  public void reset() {
    if (source.equals("")) {
      handle = 0;
      return;
    }
    STBImage.stbi_set_flip_vertically_on_load(true);
    // Allocate variables for width height and channels.
    IntBuffer width = MemoryUtil.memAllocInt(1);
    IntBuffer height = MemoryUtil.memAllocInt(1);
    IntBuffer channels = MemoryUtil.memAllocInt(1);
    // Load data to a buffer.
    ByteBuffer data = STBImage.stbi_load(source, width, height, channels, 0);
    if (data == null) {
      Logger.error(getClass(), "Failed to load texture source: " + source);
    }
    data.flip();
    // Generate unique identifier to represent this texture.
    handle = GL33.glGenTextures();
    // Equip current texture.
    GL33.glBindTexture(GL33.GL_TEXTURE_2D, handle);
    // Define how textures should act when out of bounds.
    GL33.glTexParameteri(GL33.GL_TEXTURE_2D, GL33.GL_TEXTURE_WRAP_S, GL33.GL_REPEAT);
    GL33.glTexParameteri(GL33.GL_TEXTURE_2D, GL33.GL_TEXTURE_WRAP_T, GL33.GL_REPEAT);
    // Nearest neighbor filtering when downscaling, bilinear filtering when upscaling.
    GL33.glTexParameteri(GL33.GL_TEXTURE_2D, GL33.GL_TEXTURE_MIN_FILTER, GL33.GL_LINEAR);
    GL33.glTexParameteri(GL33.GL_TEXTURE_2D, GL33.GL_TEXTURE_MAG_FILTER, GL33.GL_LINEAR);
    // Pixels are RGBA format if png, jpg has no transparency.
    int colorScheme = source.endsWith(".png") ? GL33.GL_RGBA : GL33.GL_RGB;
    // Associate image data with bound texture handle.
    GL33.glTexImage2D(GL33.GL_TEXTURE_2D, 0, colorScheme, width.get(), height.get(), 0, colorScheme, GL33.GL_UNSIGNED_BYTE, data);
    // Generate mipmaps for bound texture.
    GL33.glGenerateMipmap(GL33.GL_TEXTURE_2D);
    // Unbind.
    GL33.glBindTexture(GL33.GL_TEXTURE_2D, 0);
    STBImage.stbi_image_free(data);
    MemoryUtil.memFree(width);
    MemoryUtil.memFree(height);
    MemoryUtil.memFree(channels);
  }

  /**
   * Get this sprite's unique identifier.
   * @return The ID.
   */
  public int get() {
    return handle;
  }

}
