package engine.core.imp.render;

import glcommon.Color;
import glcommon.util.ResourceLocator;
import glcommon.util.ResourceLocator.ClasspathResourceLocator;
import glextra.material.Material;
import glextra.material.MaterialXMLLoader;
import glextra.renderer.Renderer2D;
import gltools.shader.Program.ProgramLinkException;
import gltools.shader.Shader.ShaderCompileException;
import gltools.texture.Texture2D;
import gltools.texture.TextureFactory;
import gltools.texture.TextureWrapMode;

import java.io.IOException;

public class MaterialFactory {
	private Renderer2D m_renderer;
	
	public MaterialFactory(Renderer2D renderer) {
		m_renderer = renderer;
	}

	private static ResourceLocator locator;
	private static String MAT_LOC = "Materials/M2D/2d_deferred.mat";
	/**
	 * @return a white Material
	 */
	public Material createBasicMaterial() {
		if (locator == null)
			locator = new ClasspathResourceLocator();

		Material mat = null;
		try {
			// Hack global params in there for now
			mat = MaterialXMLLoader.s_load(m_renderer.getGL(), MAT_LOC, locator).get(0);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ShaderCompileException e) {
			e.printStackTrace();
		} catch (ProgramLinkException e) {
			e.printStackTrace();
		}

		mat.setColor("materialDiffuseColor", new Color(1, 1, 1));
		return mat;
	}

	/**
	 * @param color
	 * @return a colored Material
	 */
	public Material createBasicMaterial(Color color) {
		if (locator == null)
			locator = new ClasspathResourceLocator();

		Material mat = null;
		try {
			mat = MaterialXMLLoader.s_load(m_renderer.getGL(), MAT_LOC, locator).get(0);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ShaderCompileException e) {
			e.printStackTrace();
		} catch (ProgramLinkException e) {
			e.printStackTrace();
		}

		mat.setColor("materialDiffuseColor", color);
		return mat;
	}

	/**
	 * @param texture
	 * @return a textured Material
	 */
	public Material createBasicMaterial(String texture) {
		if (locator == null)
			locator = new ClasspathResourceLocator();

		Material mat = null;
		try {
			mat = MaterialXMLLoader.s_load(m_renderer.getGL(), MAT_LOC, locator).get(0);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ShaderCompileException e) {
			e.printStackTrace();
		} catch (ProgramLinkException e) {
			e.printStackTrace();
		}

		try {
			mat.setTexture2D("materialDiffuseTexture", TextureFactory.s_loadTexture(m_renderer.getGL(), texture, locator));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return mat;
	}

	/**
	 * @param texture
	 * @param lighted
	 * @param repeated
	 * @return a textured Material
	 */
	public Material createBasicMaterial(String texture, boolean lighted, boolean repeated) {
		if (locator == null)
			locator = new ClasspathResourceLocator();

		Material mat = null;
		try {
			mat = MaterialXMLLoader.s_load(m_renderer.getGL(), MAT_LOC, locator).get(0);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ShaderCompileException e) {
			e.printStackTrace();
		} catch (ProgramLinkException e) {
			e.printStackTrace();
		}

		try {
			mat.setBoolean("useLighting", lighted);
			Texture2D t = TextureFactory.s_loadTexture(m_renderer.getGL(), texture, locator);
			if (repeated) {
				t.bind(m_renderer.getGL());
				t.setSWrapMode(TextureWrapMode.REPEAT);
				t.setTWrapMode(TextureWrapMode.REPEAT);
				t.loadParams(m_renderer.getGL());
				t.unbind(m_renderer.getGL());
			}
			mat.setTexture2D("materialDiffuseTexture", t);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return mat;
	}

	/**
	 * Creates a Material with a bumpMap and a texture.
	 * 
	 * @param texture
	 * @param bumpMap
	 * @param lightPosition
	 * @return the created Material.
	 */
	public Material createBasicMaterial(String texture, String bumpMap) {
		if (locator == null)
			locator = new ClasspathResourceLocator();

		Material mat = null;
		try {
			mat = MaterialXMLLoader.s_load(m_renderer.getGL(), MAT_LOC, locator).get(0);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ShaderCompileException e) {
			e.printStackTrace();
		} catch (ProgramLinkException e) {
			e.printStackTrace();
		}

		mat.setColor("materialDiffuseColor", new Color(1f, 1f, 1f));
		try {
			mat.setTexture2D("materialDiffuseTexture", TextureFactory.s_loadTexture(m_renderer.getGL(), texture, locator));
			mat.setTexture2D("materialNormalMap", TextureFactory.s_loadTexture(m_renderer.getGL(), bumpMap, locator));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return mat;
	}
}
