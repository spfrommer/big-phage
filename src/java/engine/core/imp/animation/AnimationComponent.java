package engine.core.imp.animation;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import engine.core.exec.GameState;
import engine.core.frame.Component;
import glextra.material.Material;

/**
 * Animates an Entity's material with a list of frames.
 */
public class AnimationComponent extends Component {
	private static final Set<String> IDENTIFIERS = new HashSet<String>(Arrays.asList("sys_frames", "sys_material",
			"sys_timePerFrame", "sys_repeatAnimation"));

	// Ticks since last update
	private float m_timeElapsed;
	private int m_currentFrame;
	private int m_frameCount;

	public AnimationComponent() {
	}

	@Override
	public void update(float time, GameState state) {
		@SuppressWarnings("unchecked")
		List<Material> frames = (List<Material>) getData("sys_frames");
		float timePerFrame = (Float) getData("sys_timePerFrame");
		boolean repeat = (Boolean) getData("sys_repeatAnimation");
		m_frameCount = frames.size();

		m_timeElapsed += time;

		while (m_timeElapsed >= timePerFrame) {
			if (isFinished()) {
				if (repeat) {
					m_currentFrame = 0;
				} else {
					m_timeElapsed = 0f;
					return;
				}
			}
			setData("sys_material", frames.get(m_currentFrame));
			m_currentFrame++;
			m_timeElapsed -= timePerFrame;
		}
	}

	@Override
	public Set<String> getDataIdentifiers() {
		return IDENTIFIERS;
	}

	public boolean isFinished() {
		return (m_currentFrame >= m_frameCount);
	}

	public boolean isAtStart() {
		return (m_currentFrame == 0);
	}
}
