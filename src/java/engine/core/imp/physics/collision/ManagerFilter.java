package engine.core.imp.physics.collision;

import java.util.HashMap;
import java.util.Map;

import org.jbox2d.callbacks.ContactFilter;
import org.jbox2d.dynamics.Fixture;

import engine.core.frame.Entity;

public class ManagerFilter extends ContactFilter {
	private Map<Entity, CollisionFilter> m_filters = new HashMap<Entity, CollisionFilter>();

	public void addFilter(Entity entity, CollisionFilter filter) {
		m_filters.put(entity, filter);
	}

	public void removeFilter(Entity entity) {
		m_filters.remove(entity);
	}

	@Override
	public boolean shouldCollide(Fixture fixtureA, Fixture fixtureB) {
		Entity entity1 = (Entity) fixtureA.getBody().getUserData();
		Entity entity2 = (Entity) fixtureB.getBody().getUserData();

		boolean continue1 = true;
		boolean continue2 = true;

		if (m_filters.containsKey(entity1)) {
			continue1 = m_filters.get(entity1).canCollideWith(entity2);
		}

		if (m_filters.containsKey(entity2)) {
			continue2 = m_filters.get(entity2).canCollideWith(entity1);
		}

		return continue1 && continue2;
	}
}
