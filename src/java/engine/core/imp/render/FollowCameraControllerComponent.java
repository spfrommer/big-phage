package engine.core.imp.render;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import engine.commons.utils.Vector2f;
import engine.core.exec.GameState;
import engine.core.frame.Component;
import engine.core.frame.Entity;
import gltools.vector.Matrix3f;
import gltools.vector.MatrixFactory;
import gltools.vector.Vector3f;

public class FollowCameraControllerComponent extends Component {
	private static final Set<String> IDENTIFIERS = new HashSet<String>(Arrays.asList("sys_camTargetEntity",
			"sys_camScale", "sys_camRotation"));
	private Vector2f m_currentPosition = new Vector2f(0f, 0f);
	private static final float CLOSE_FACTOR = 0.3f;

	@Override
	public void update(float time, GameState state) {
		Entity camTarget = (Entity) getData("sys_camTargetEntity");
		Vector2f camScale = (Vector2f) getData("sys_camScale");
		Vector2f targetPosition = (Vector2f) camTarget.getData("sys_position");

		float depth = 1f;

		Vector3f dest = new Vector3f();

		Matrix3f matrix = new Matrix3f(state.renderer.getModelMatrix());
		matrix.mul(MatrixFactory.create2DScaleMatrix(new gltools.vector.Vector2f(depth, depth)));
		matrix.mul(MatrixFactory.create2DTranslationMatrix(new gltools.vector.Vector2f(targetPosition.x,
				targetPosition.y)));
		matrix.mul(MatrixFactory.create2DTranslationMatrix(new gltools.vector.Vector2f(
				-1 / depth * m_currentPosition.x, -1 / depth * m_currentPosition.y)));

		Matrix3f.mul(matrix, new Vector3f(0f, 0f, 1f), dest);

		// Vector2f move =
		// targetPosition.sub(m_currentPosition).mul(CLOSE_FACTOR);
		// m_currentPosition = m_currentPosition.add(move);
		m_currentPosition = new Vector2f(dest.x, dest.y);
		// m_currentPosition = dest;

		state.renderer.viewTrans(-m_currentPosition.x, -m_currentPosition.y);
		state.renderer.viewScale(camScale.x, camScale.y);
	}

	@Override
	public Set<String> getDataIdentifiers() {
		return IDENTIFIERS;
	}
}
