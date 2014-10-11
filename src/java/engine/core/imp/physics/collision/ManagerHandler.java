package engine.core.imp.physics.collision;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.contacts.Contact;

import engine.core.frame.Entity;

public class ManagerHandler implements ContactListener {
	private Map<Entity, List<CollisionEvent>> m_events = new HashMap<Entity, List<CollisionEvent>>();

	public void addEvent(Entity entity, CollisionEvent event) {
		if (!m_events.containsKey(entity)) {
			m_events.put(entity, new ArrayList<CollisionEvent>());
		}
		m_events.get(entity).add(event);
	}

	public void removeEvent(Entity entity, CollisionEvent event) {
		m_events.get(entity).remove(event);
	}

	@Override
	public void beginContact(Contact arg0) {
		Entity entity1 = (Entity) arg0.getFixtureA().getBody().getUserData();
		Entity entity2 = (Entity) arg0.getFixtureB().getBody().getUserData();

		if (entity1 != null && m_events.containsKey(entity1)) {
			for (CollisionEvent e : m_events.get(entity1))
				e.collidedWith(entity2);
		}
		if (entity2 != null && m_events.containsKey(entity2)) {
			for (CollisionEvent e : m_events.get(entity2))
				e.collidedWith(entity1);
		}
	}

	@Override
	public void endContact(Contact arg0) {

	}

	@Override
	public void postSolve(Contact arg0, ContactImpulse arg1) {

	}

	@Override
	public void preSolve(Contact arg0, Manifold arg1) {

	}
}
