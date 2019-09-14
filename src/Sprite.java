import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL33;
import org.lwjgl.stb.STBImage;

public enum Sprite {
  
  TRUMP("./res/trump.png");
  
  private final int handle;
  
  private Sprite(String source) {
    STBImage.stbi_set_flip_vertically_on_load(true);
    IntBuffer width = BufferUtils.createIntBuffer(1);
    IntBuffer height = BufferUtils.createIntBuffer(1);
    IntBuffer channels = BufferUtils.createIntBuffer(1);
    ByteBuffer data = STBImage.stbi_load(source, width, height, channels, 0);
    if (data == null) {
      Logger.error(getClass(), "Failed to load texture source: " + source);
    }
    data.flip();
    handle = GL33.glGenTextures();
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
    
    STBImage.stbi_image_free(data);
  }
  
  public int get() {
    return handle;
  }

}