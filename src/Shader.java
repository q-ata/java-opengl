import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL33;

/**
 * Represents a shader program with a vertex and fragment shader.
 */
public class Shader extends Handle {
  
  /**
   * Construct a shader program.
   * @param vsPath Path to vertex shader file.
   * @param fsPath Path to fragment shader file.
   */
  public Shader(String vsPath, String fsPath) {
    // Attempt to load contents of the shader files.
    String vsSource = loadShaderSource(vsPath);
    if (vsSource == null)
      Logger.error(getClass(), "Could not find source file for vertex shader: " + vsPath);
    String fsSource = loadShaderSource(fsPath);
    if (fsSource == null)
      Logger.error(getClass(), "Could not find source file for fragment shader: " + fsPath);
    // Attempt to compile the vertex shader.
    int vs = GL33.glCreateShader(GL33.GL_VERTEX_SHADER);
    GL33.glShaderSource(vs, vsSource);
    GL33.glCompileShader(vs);
    if (GL33.glGetShaderi(vs, GL33.GL_COMPILE_STATUS) == GL33.GL_FALSE) {
      System.out.println(GL33.glGetShaderInfoLog(vs, 500));
      Logger.error(getClass(), "Could not compile vertex shader: "+ vsPath);
    }
    // Attempt to compile the fragment shader.
    int fs = GL33.glCreateShader(GL33.GL_FRAGMENT_SHADER);
    GL33.glShaderSource(fs, fsSource);
    GL33.glCompileShader(fs);
    if (GL33.glGetShaderi(fs, GL33.GL_COMPILE_STATUS) == GL33.GL_FALSE) {
      System.out.println(GL33.glGetShaderInfoLog(fs, 500));
      Logger.error(getClass(), "Could not compile fragment shader: " + fsPath);
    }
    // Create combined shader program.
    handle = GL33.glCreateProgram();
    GL33.glAttachShader(handle, vs);
    GL33.glAttachShader(handle, fs);
    GL33.glLinkProgram(handle);
    if (GL33.glGetProgrami(handle, GL33.GL_LINK_STATUS) == GL33.GL_FALSE) {
      System.out.println(GL33.glGetShaderInfoLog(handle, 500));
      Logger.error(getClass(), "Could not compile shader program: " + vsPath + " | " + fsPath);
    }
    // Delete individual shaders.
    GL33.glDeleteShader(vs);
    GL33.glDeleteShader(fs);
    
  }
  
  /**
   * Bind this shader for use.
   */
  public void use() {
    GL33.glUseProgram(handle);
  }
  
  public void setUniformMatrix(String name, Matrix4f mat) {
    FloatBuffer fb = BufferUtils.createFloatBuffer(16);
    mat.get(fb);
    GL33.glUniformMatrix4fv(GL33.glGetUniformLocation(handle, name), false, fb);
  }
  
  /**
   * Loads the contents of a single shader from a path.
   * @param path
   * @return
   */
  private static String loadShaderSource(String path) {
    try {
      BufferedReader reader = new BufferedReader(new FileReader(new File(path)));
      StringBuilder sb = new StringBuilder();
      String line;
      while ((line = reader.readLine()) != null) {
        sb.append(line + "\n");
      }
      reader.close();
      return sb.toString();
    }
    catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

}
